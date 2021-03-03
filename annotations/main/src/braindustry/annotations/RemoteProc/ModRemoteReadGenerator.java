package braindustry.annotations.RemoteProc;

import arc.func.Cons;
import arc.struct.Seq;
import arc.util.io.Reads;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import mindustry.annotations.BaseProcessor;
import mindustry.annotations.remote.MethodEntry;
import mindustry.annotations.remote.RemoteProcess;
import mindustry.annotations.remote.SerializerResolver;
import mindustry.annotations.util.TypeIOResolver;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static braindustry.annotations.ModBaseProcessor.write;

public class ModRemoteReadGenerator {
    /**
     * Creates a read generator that uses the supplied serializer setup.
     *
     * @param serializers
     */
    private final TypeIOResolver.ClassSerializer serializers;

    /** Creates a read generator that uses the supplied serializer setup. */
    public ModRemoteReadGenerator(TypeIOResolver.ClassSerializer serializers){
        this.serializers = serializers;
    }

    public void generateFor(Seq<MethodEntry> entries, String className, String packageName,String parentName, boolean needsPlayer) throws Exception{

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);
        classBuilder.addJavadoc(RemoteProcess.autogenWarning);
        //create main method builder
        MethodSpec.Builder readMethod = MethodSpec.methodBuilder("readPacket")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Reads.class, "read") //buffer to read form
                .addParameter(int.class, "id") //ID of method type to read
                .returns(void.class);

        if(needsPlayer){
            //add player parameter
            readMethod.addParameter(ClassName.get(parentName, "Player"), "player");
        }

        CodeBlock.Builder readBlock = CodeBlock.builder(); //start building block of code inside read method
        boolean started = false; //whether an if() statement has been written yet

        for(MethodEntry entry : entries){
            //write if check for this entry ID
            if(!started){
                started = true;
                readBlock.beginControlFlow("if(id == " + entry.id + ")");
            }else{
                readBlock.nextControlFlow("else if(id == " + entry.id + ")");
            }

            readBlock.beginControlFlow("try");

            //concatenated list of variable names for method invocation
            StringBuilder varResult = new StringBuilder();

            //go through each parameter
            for(int i = 0; i < entry.element.getParameters().size(); i++){
                VariableElement var = entry.element.getParameters().get(i);

                if(!needsPlayer || i != 0){ //if client, skip first parameter since it's always of type player and doesn't need to be read
                    //full type name of parameter
                    String typeName = var.asType().toString();
                    //name of parameter
                    String varName = var.getSimpleName().toString();
                    //captialized version of type name for reading primitives
                    String pname = typeName.equals("boolean") ? "bool" : typeName.charAt(0) + "";

                    //write primitives automatically
                    if(BaseProcessor.isPrimitive(typeName)){
                        readBlock.addStatement("$L $L = read.$L()", typeName, varName, pname);
                    }else{
                        //else, try and find a serializer
                        String ser = serializers.readers.get(typeName.replace(parentName+".", "").replace(packageName+".",""), SerializerResolver.locate(entry.element, var.asType(), false));

                        if(ser == null){ //make sure a serializer exists!
                            BaseProcessor.err("No read method to read class type '" + typeName + "' in method " + entry.targetMethod + "; " + serializers.readers, var);
                            return;
                        }

                        //add statement for reading it
                        readBlock.addStatement(typeName + " " + varName + " = " + ser + "(read)");
                    }

                    //append variable name to string builder
                    varResult.append(var.getSimpleName());
                    if(i != entry.element.getParameters().size() - 1) varResult.append(", ");
                }else{
                    varResult.append("player");
                    if(i != entry.element.getParameters().size() - 1) varResult.append(", ");
                }
            }

            //execute the relevant method before the forward
            //if it throws a ValidateException, the method won't be forwarded
            readBlock.addStatement("$N." + entry.element.getSimpleName() + "(" + varResult.toString() + ")", ((TypeElement)entry.element.getEnclosingElement()).getQualifiedName().toString());

            //call forwarded method, don't forward on the client reader
            if(entry.forward && entry.where.isServer && needsPlayer){
                //call forwarded method
                readBlock.addStatement(packageName + "." + entry.className + "." + entry.element.getSimpleName() +
                        "__forward(player.con" + (varResult.length() == 0 ? "" : ", ") + varResult.toString() + ")");
            }

            readBlock.nextControlFlow("catch (java.lang.Exception e)");
            readBlock.addStatement("throw new java.lang.RuntimeException(\"Failed to read remote method '" + entry.element.getSimpleName() + "'!\", e)");
            readBlock.endControlFlow();
        }

        //end control flow if necessary
        Cons<CodeBlock.Builder> addEnd=(block)->{
//            block.addStatement("$1N.readPacket(read,id"+(needsPlayer?",player":"")+");",className.replace("Mod","")); //handle parent ids
        };
        if(started){
            readBlock.nextControlFlow("else");
addEnd.get(readBlock);
            readBlock.endControlFlow();
        } else {
            addEnd.get(readBlock);
        }

        //add block and method to class
        readMethod.addCode(readBlock.build());
//        classBuilder.alwaysQualifiedNames.add(ClassName.get(parentName,className.replace("Mod","")).reflectionName());
//        classBuilder.originatingElements.add();
        classBuilder.addMethod(readMethod.build());
        write(classBuilder,Seq.with(ClassName.get(parentName,className.replace("Mod",""))),0);
        TypeSpec spec = classBuilder.build();

//        JavaFile.builder(packageName, spec).build().writeTo(BaseProcessor.filer);
    }
}
