package braindustry.annotations.RulesEditDialog;

import arc.scene.ui.TextArea;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.struct.StringMap;
import arc.util.Strings;
import braindustry.annotations.ModAnnotations;
import braindustry.annotations.ModBaseProcessor;
import com.squareup.javapoet.*;
import mindustry.annotations.BaseProcessor;
import mindustry.annotations.remote.RemoteProcess;
import mindustry.annotations.util.Selement;
import mindustry.annotations.util.Stype;
import mindustry.annotations.util.Svar;
import mindustry.game.Rules;
import mindustry.type.Sector;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;


@SupportedAnnotationTypes({
        "braindustry.annotations.ModAnnotations.Rules",
        "braindustry.annotations.ModAnnotations.RulesTable",
})
public class RulesProcessor extends ModBaseProcessor {

    private Seq<Selement> elements;
    protected CodeBlock.Builder cashBuilder=null;
    {
        rounds = 1;
    }
    protected void addCode(CodeBlock.Builder builder,String text,Object... args){
        builder.addStatement(Strings.format(text,args));
    }
    protected void addCode(String text,Object... args){
        addCode(cashBuilder,text,args);
    }
    protected  void setBuilder(CodeBlock.Builder builder){
        cashBuilder=builder;
    }
    protected void addSlider(CodeBlock.Builder builder,Svar field,String cast){
        builder.beginControlFlow(Strings.format("table.add(\"\").update(label->"));
        setBuilder(builder);
        addCode("label.setText(\"@: \"+rules.@)",field.name(),field.name());
        builder.endControlFlow(")");
        addCode("textArea = table.area(\"\"+rules.@, text -> rules.@=@Strings.parseFloat(text,0f)).width(100).get()",field.name(),field.name(),cast);
        addCode("textArea.setMaxLength((Float.MAX_VALUE + \"\").length())");
        addCode("textArea.setFilter(@.floatsOnly)", ClassName.get(TextField.TextFieldFilter.class));
//        addCode(builder,"table.slider(0f,10000f,1f,rules.@,(f)->rules.@=@f).growX().width(240f);",field.name(),field.name(),cast);
    }
    protected void build(CodeBlock.Builder builder, Svar field) {
        if (check(field.tname().box().toString())){
            if (is(field,Boolean.class)){
                addCode(builder,"table.check(\"@\",rules.@,(b)->rules.@=b)",field.name(),field.name(),field.name());
            } else if (is(field,Integer.class)){
                addSlider(builder,field,"(int)");
            } else if (is(field,Float.class)){
                addSlider(builder,field,"");
            } else {
                return;
            }
            builder.addStatement("table.row()");
        }
    }
protected boolean is(Svar var,Class<?> c){
        return var.tname().box().toString().equals(c.getName());
}
    protected boolean check(String c) {
        Seq<Class> with = Seq.<Class>with(ObjectSet.class, Seq.class, StringMap.class, Sector.class, Rules.TeamRules.class);
        return ! with.map((Class::getName)).contains(c.split("<")[0]);
//        return !with.contains(c);
    }

    public void process(RoundEnvironment env) throws Exception {
        elements = elements(ModAnnotations.Rules.class);
        Stype type = elements.first().asType().superclass();
        Seq<Svar> fields = type.fields();
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("CheatModRulesTable").addModifiers(Modifier.PUBLIC);
        classBuilder.addJavadoc(RemoteProcess.autogenWarning);
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Table.class, "table") //buffer to read form
                .addParameter(Rules.class, "rules") //ID of method type to read
                .returns(void.class);
        CodeBlock.Builder buildBlock = CodeBlock.builder();

        buildBlock.addStatement(ClassName.get(TextArea.class).reflectionName()+" textArea");
//        buildBlock.addStatement(ClassName.get(Strings.class).reflectionName()+" Strings=new "+ClassName.get(Strings.class).reflectionName()+"()");
        for (Svar field : fields) {
            build(buildBlock,field);
//            System.out.println(field.tname().box().toString());
//            System.out.println((field.tname().toString() + "-" + field.name()));
        }
//        classBuilder.alwaysQualifiedNames.add(ClassName.get(parentName,className.replace("Mod","")).reflectionName());
//        classBuilder.originatingElements.add();
        buildMethod.addCode(buildBlock.build());
        classBuilder.addMethod(buildMethod.build());

        TypeSpec spec = classBuilder.build();
        Seq<ClassName> imports=new Seq<>();
        imports.add(ClassName.get(Strings.class));
write(classBuilder,imports,0);
imports.clear();
//        JavaFile.builder(packageName, spec).build().writeTo(BaseProcessor.filer);
    }
}
