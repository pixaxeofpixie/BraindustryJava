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
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Locale;
import java.util.Set;

@SupportedAnnotationTypes({
        "braindustry.annotations.ModAnnotations.CustomStat",
})
public class AStatProcessor extends ModBaseProcessor {
    public String metaPackage = "braindustry.world.meta";
    ObjectMap<String, String> enumVars = new ObjectMap<>();
    ObjectMap<String, Svar> fieldsVars = new ObjectMap<>();
    ObjectMap<String, String> parentEnumVars = new ObjectMap<>();
    ObjectMap<Smethod, String> methodBlocks = new ObjectMap<>();

    {
        rounds = 1;
    }

    private void addEnums(Stype type, TypeSpec.Builder map) {
        for (Svar f : type.fields()) {
            VariableTree tree = f.tree();
            //add initializer if it exists
            if (tree.getInitializer() != null) {
                String replace = tree.getInitializer().toString().replace(Strings.format("new @(", type.name()), "");
                replace = replace.replace("AStatCat", "StatCat").replace("StatCat", "AStatCat");
                String init = replace.substring(0, replace.length() - 1);
                TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder(init);
                map.addEnumConstant(f.name(), builder.build());
            }
        }
    }

    @Override
    public void process(RoundEnvironment env) throws Exception {
        Stype type = types(ModAnnotations.CustomStat.class).first();
        TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(ClassName.get(Stat.class).reflectionName());
        Stype parent = new Stype(typeElement);
        addUnInitedFields(type, fieldsVars);

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
            methodBlocks.put(method, value.substring(1, value.length() - 1) //fix vars
            );
        }
        TypeSpec.Builder aStat = TypeSpec.enumBuilder("AStat").addModifiers(Modifier.PUBLIC);

        addEnums(type, aStat);
        addEnumsReflect(aStat);
        fieldsVars.each((var, field) -> {
            Set<Modifier> flags = field.tree().getModifiers().getFlags();
            Seq<Modifier> flagsS = Seq.with().addAll(flags).map(o -> (Modifier) o);
            TypeName tname = field.tname();
            if (field.cname().simpleName().equals("StatCat")) {
                tname = ClassName.get(metaPackage, "AStatCat");
            }
            aStat.addField(tname, var, flagsS.toArray(Modifier.class));
        });
        type.constructors().each(constructor -> {
            MethodSpec.Builder builder = MethodSpec.constructorBuilder();
            constructor.params().each(param -> {
                if (!param.cname().simpleName().equals("StatCat")) {
                    ParameterSpec parameterSpec = ParameterSpec.get(param.e);
                    builder.addParameter(parameterSpec);
                } else {
                    builder.addParameter(ClassName.get(metaPackage, "AStatCat"), param.name());
                }
            });
            String body = constructor.tree().getBody().toString();
            body = body.substring(1, body.length() - 1).replace("AStatCat", "StatCat").replace("StatCat", "AStatCat");
            builder.addCode(body);
            aStat.addMethod(builder.build());
        });
//        MethodSpec.methodBuilder()
        methodBlocks.each((method, block) -> {
            MethodTree tree = method.tree();
            MethodSpec.Builder builder = MethodSpec.methodBuilder(method.name()).addModifiers(tree.getModifiers().getFlags());
            builder.addCode(block);
            method.params().each(param -> {
                builder.addParameter(ParameterSpec.get(param.e));
            });
            TypeName returnType = method.retn();
            if (returnType.toString().equals(type.fullName())) {
                returnType = ClassName.get(metaPackage, "AStat");
            }
            builder.returns(returnType);
            aStat.addMethod(builder.build());
        });
        Seq<ClassName> imports = new Seq<>();
        imports.add(ClassName.get(Core.class), ClassName.get(Seq.class), ClassName.get(Locale.class));
        imports.add(ClassName.get(metaPackage, "AStatCat"));
        write(aStat, metaPackage, imports, 0);
    }

    private void addUnInitedFields(Stype type, ObjectMap<String, Svar> map) {
        for (Svar f : type.fields()) {
            VariableTree tree = f.tree();
            if (tree.getInitializer() == null) {
                String init = f.cname().simpleName();
                map.put(f.name(), f);
            }
        }
    }

    private void addEnumsReflect(TypeSpec.Builder map) {
        for (Stat field : Seq.<Stat>with().addAll(Stat.values()).sort(Enum::ordinal)) {
            StatCat category = field.category;
            String init = category == StatCat.general ? "" : Strings.format("AStatCat.@", category.name());
            TypeSpec.Builder builder = TypeSpec.anonymousClassBuilder(init);
            map.addEnumConstant(Strings.format("@", field.name()), builder.build());
        }
    }
}
