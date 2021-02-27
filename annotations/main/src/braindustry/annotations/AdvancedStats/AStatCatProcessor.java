package braindustry.annotations.AdvancedStats;


import arc.Core;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Strings;
import braindustry.annotations.ModAnnotations;
import braindustry.annotations.ModBaseProcessor;
import com.squareup.javapoet.*;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import mindustry.annotations.util.Smethod;
import mindustry.annotations.util.Stype;
import mindustry.annotations.util.Svar;
import mindustry.world.meta.StatCat;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({
        "braindustry.annotations.ModAnnotations.CustomStatCat",
})
public class AStatCatProcessor extends ModBaseProcessor {
    public String metaPackage = "braindustry.world.meta";
    ObjectMap<String, String> enumVars = new ObjectMap<>();
    ObjectMap<String, String> parentEnumVars = new ObjectMap<>();
    ObjectMap<Smethod, String> methodBlocks = new ObjectMap<>();

    {
        rounds = 1;
    }
    private void addFields(Stype type,ObjectMap<String,String> map){
        for (Svar f : type.fields()) {
            VariableTree tree = f.tree();
            //add initializer if it exists
            if (tree.getInitializer() != null) {
                String init = tree.getInitializer().toString();
                map.put(f.name(), init);
            }
        }
    }
    @Override
    public void process(RoundEnvironment env) throws Exception {
        Stype type = types(ModAnnotations.CustomStatCat.class).first();
        TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(ClassName.get(StatCat.class).reflectionName());
        Stype parent = new Stype(typeElement);
        addFields(type, enumVars);
        addFieldsReflect(parentEnumVars,StatCat.class);

        for (Smethod method : type.methods()) {
            if (method.is(Modifier.ABSTRACT) || method.is(Modifier.NATIVE)) continue;
            //get all statements in the method, store them
//            print("method name: @",method.name());
            String value = null;
            try {
                value = method.tree().getBody().toString()
                        .replaceAll("this\\.<(.*)>self\\(\\)", "this") //fix parameterized self() calls
                        .replaceAll("self\\(\\)", "this") //fix self() calls
                        .replaceAll(" yield ", "") //fix enchanced switch
                        .replaceAll("\\/\\*missing\\*\\/", "var");
            } catch (NullPointerException exception) {
                continue;
            }
            methodBlocks.put(method, value //fix vars
            );
        }
        TypeSpec.Builder aStatCat = TypeSpec.enumBuilder("AStatCat").addModifiers(Modifier.PUBLIC);
        parentEnumVars.keys().toSeq().each(aStatCat::addEnumConstant);
        enumVars.keys().toSeq().each(aStatCat::addEnumConstant);
//        MethodSpec.methodBuilder()
        methodBlocks.each((method,block)->{
            MethodTree tree = method.tree();
            MethodSpec.Builder builder = MethodSpec.methodBuilder(method.name()).addModifiers(tree.getModifiers().getFlags());
            builder.addCode(block);
            method.params().each(param -> {
                builder.addParameter(ParameterSpec.get(param.e));
            });
            TypeName returnType = method.retn();
            if (returnType.toString().equals(type.fullName())) {
                returnType = ClassName.get(metaPackage, "AStatCat");
            }
            builder.returns(returnType);
            aStatCat.addMethod(builder.build());
        });
        Seq<ClassName> imports = new Seq<>();
        imports.add(ClassName.get(Core.class),ClassName.get(Seq.class));
        write(aStatCat, metaPackage, imports,0);
    }

    private void addFieldsReflect( ObjectMap<String, String> map,Class<? extends StatCat> aClass) {
//        Seq<Svar> fields = type.fields();
        for (StatCat field : aClass.getEnumConstants()) {
            map.put(Strings.format("@",field.name()),"new StatCat()");
        }
    }
}
