package braindustry.annotations.ModEntities;

import arc.func.Cons;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import braindustry.annotations.ModAnnotations;
import braindustry.annotations.ModBaseProcessor;
import braindustry.annotations.RemoteProc.ModTypeIOResolver;
import com.squareup.javapoet.*;

//import com.sun.source.tree.*;
import mindustry.annotations.Annotations;
import mindustry.annotations.entity.EntityProcess;
import mindustry.annotations.util.*;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypesException;
import java.lang.annotation.Annotation;

@SupportedAnnotationTypes({
        "braindustry.annotations.ModAnnotations.EntityInterface",
})
public class ModEntitiesProc extends ModBaseProcessor {
    Seq<Stype> allInterfaces = new Seq<>();
    Seq<Selement> allDefs = new Seq<>();
    Seq<Stype> baseComponents;
    ObjectMap<String, Stype> componentNames = new ObjectMap<>();
    ObjectMap<Stype, Seq<Stype>> componentDependencies = new ObjectMap<>();
    ObjectMap<Selement, Seq<Stype>> defComponents = new ObjectMap<>();
    ObjectMap<String, String> varInitializers = new ObjectMap<>();
    ObjectMap<String, String> methodBlocks = new ObjectMap<>();
    ObjectMap<Stype, ObjectSet<Stype>> baseClassDeps = new ObjectMap<>();
    ObjectSet<String> imports = new ObjectSet<>();
    Seq<TypeSpec.Builder> baseClasses = new Seq<>();
    TypeIOResolver.ClassSerializer serializer;
    {
        rounds = 3;
    }

    @Override
    public void process(RoundEnvironment env) throws Exception {
        updateRounds();
        switch (round) {
            case 1:
                firstRound();
            case 2:
                secondRound();
            case 3:
                thirdRound();
        }

    }

    private void updateRounds() {
        allDefs.addAll(elements(Annotations.EntityDef.class));
        allInterfaces.addAll(types(ModAnnotations.EntityInterface.class));
    }

    private void thirdRound() {

    }

    private void secondRound() {

    }

    private void firstRound() throws Exception {
        serializer = ModTypeIOResolver.resolve(this);
        baseComponents = types(Annotations.BaseComponent.class);
        Seq<Stype> allComponents = types(Annotations.Component.class);

        //store code
        for(Stype component : allComponents){
            for(Svar f : component.fields()){
//                Object tree = f.tree();

                //add initializer if it exists
                /*if(f.tree() .getInitializer() != null){
                    String init = f.tree().getInitializer().toString();
                    varInitializers.put(f.descString(), init);
                }*/
            }

            for(Smethod elem : component.methods()){
                if(elem.is(Modifier.ABSTRACT) || elem.is(Modifier.NATIVE)) continue;
                //get all statements in the method, store them
                methodBlocks.put(elem.descString(), elem.tree().getBody().toString()
                        .replaceAll("this\\.<(.*)>self\\(\\)", "this") //fix parameterized self() calls
                        .replaceAll("self\\(\\)", "this") //fix self() calls
                        .replaceAll(" yield ", "") //fix enchanced switch
                        .replaceAll("\\/\\*missing\\*\\/", "var") //fix vars
                );
            }
        }

        //store components
        for(Stype type : allComponents){
            componentNames.put(type.name(), type);
        }

        //add component imports
        for(Stype comp : allComponents){
            imports.addAll(getImports(comp.e));
        }

        //create component interfaces
        for(Stype component : allComponents){
            TypeSpec.Builder inter = TypeSpec.interfaceBuilder(interfaceName(component))
                    .addModifiers(Modifier.PUBLIC).addAnnotation(Annotations.EntityInterface.class);

            inter.addJavadoc("Interface for {@link $L}", component.fullName());

            //implement extra interfaces these components may have, e.g. position
            for(Stype extraInterface : component.interfaces().select(i -> !isCompInterface(i))){
                //javapoet completely chokes on this if I add `addSuperInterface` or create the type name with TypeName.get
                inter.superinterfaces.add(tname(extraInterface.fullName()));
            }

            //implement super interfaces
            Seq<Stype> depends = getDependencies(component);
            for(Stype type : depends){
                inter.addSuperinterface(ClassName.get(packageName, interfaceName(type)));
            }

            ObjectSet<String> signatures = new ObjectSet<>();

            //add utility methods to interface
            for(Smethod method : component.methods()){
                //skip private methods, those are for internal use.
                if(method.isAny(Modifier.PRIVATE, Modifier.STATIC)) continue;

                //keep track of signatures used to prevent dupes
                signatures.add(method.e.toString());

                inter.addMethod(MethodSpec.methodBuilder(method.name())
                        .addJavadoc(method.doc() == null ? "" : method.doc())
                        .addExceptions(method.thrownt())
                        .addTypeVariables(method.typeVariables().map(TypeVariableName::get))
                        .returns(method.ret().toString().equals("void") ? TypeName.VOID : method.retn())
                        .addParameters(method.params().map(v -> ParameterSpec.builder(v.tname(), v.name())
                                .build())).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).build());
            }

            for(Svar field : component.fields().select(e -> !e.is(Modifier.STATIC) && !e.is(Modifier.PRIVATE) && !e.has(Annotations.Import.class))){
                String cname = field.name();

                //getter
                if(!signatures.contains(cname + "()")){
                    inter.addMethod(MethodSpec.methodBuilder(cname).addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                            .addAnnotations(Seq.with(field.annotations()).select(a -> a.toString().contains("Null")).map(AnnotationSpec::get))
                            .addJavadoc(field.doc() == null ? "" : field.doc())
                            .returns(field.tname()).build());
                }

                //setter
                if(!field.is(Modifier.FINAL) && !signatures.contains(cname + "(" + field.mirror().toString() + ")") &&
                        !field.annotations().contains(f -> f.toString().equals("@mindustry.annotations.Annotations.ReadOnly"))){
                    inter.addMethod(MethodSpec.methodBuilder(cname).addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                            .addJavadoc(field.doc() == null ? "" : field.doc())
                            .addParameter(ParameterSpec.builder(field.tname(), field.name())
                                    .addAnnotations(Seq.with(field.annotations())
                                            .select(a -> a.toString().contains("Null")).map(AnnotationSpec::get)).build()).build());
                }
            }

            write(inter);

            //generate base class if necessary
            //SPECIAL CASE: components with EntityDefs don't get a base class! the generated class becomes the base class itself
            if(component.annotation(Annotations.Component.class).base()){

                Seq<Stype> deps = depends.copy().and(component);
                baseClassDeps.get(component, ObjectSet::new).addAll(deps);

                //do not generate base classes when the component will generate one itself
                if(!component.has(Annotations.EntityDef.class)){
                    TypeSpec.Builder base = TypeSpec.classBuilder(baseName(component)).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

                    //go through all the fields.
                    for(Stype type : deps){
                        //add public fields
                        for(Svar field : type.fields().select(e -> !e.is(Modifier.STATIC) && !e.is(Modifier.PRIVATE) && !e.has(Annotations.Import.class) && !e.has(Annotations.ReadOnly.class))){
                            FieldSpec.Builder builder = FieldSpec.builder(field.tname(),field.name(), Modifier.PUBLIC);

                            //keep transience
                            if(field.is(Modifier.TRANSIENT)) builder.addModifiers(Modifier.TRANSIENT);
                            //keep all annotations
                            builder.addAnnotations(field.annotations().map(AnnotationSpec::get));

                            //add initializer if it exists
                            if(varInitializers.containsKey(field.descString())){
                                builder.initializer(varInitializers.get(field.descString()));
                            }

                            base.addField(builder.build());
                        }
                    }

                    //add interfaces
                    for(Stype type : deps){
                        base.addSuperinterface(tname(packageName, interfaceName(type)));
                    }

                    //add to queue to be written later
                    baseClasses.add(base);
                }
            }

            //LOGGING

            Log.debug("&gGenerating interface for " + component.name());

            for(TypeName tn : inter.superinterfaces){
                Log.debug("&g> &lbimplements @", simpleName(tn.toString()));
            }

            //log methods generated
            for(MethodSpec spec : inter.methodSpecs){
                Log.debug("&g> > &c@ @(@)", simpleName(spec.returnType.toString()), spec.name, Seq.with(spec.parameters).toString(", ", p -> simpleName(p.type.toString()) + " " + p.name));
            }

            Log.debug("");
        }

    }
    Seq<String> getImports(Element elem){
        return Seq.with(trees.getPath(elem).getCompilationUnit().getImports()).map(Object::toString);
    }

    /** @return interface for a component type */
    String interfaceName(Stype comp){
        String suffix = "Comp";
        if(!comp.name().endsWith(suffix)) err("All components must have names that end with 'Comp'", comp.e);

        //example: BlockComp -> IBlock
        return comp.name().substring(0, comp.name().length() - suffix.length()) + "c";
    }

    /** @return base class name for a component type */
    String baseName(Stype comp){
        String suffix = "Comp";
        if(!comp.name().endsWith(suffix)) err("All components must have names that end with 'Comp'", comp.e);

        return comp.name().substring(0, comp.name().length() - suffix.length());
    }

    @Nullable
    Stype interfaceToComp(Stype type){
        //example: IBlock -> BlockComp
        String name = type.name().substring(0, type.name().length() - 1) + "Comp";
        return componentNames.get(name);
    }

    /** @return all components that a entity def has */
    Seq<Stype> allComponents(Selement<?> type){
        if(!defComponents.containsKey(type)){
            //get base defs
            Seq<Stype> interfaces = types(type.annotation(Annotations.EntityDef.class), Annotations.EntityDef::value);
            Seq<Stype> components = new Seq<>();
            for(Stype i : interfaces){
                Stype comp = interfaceToComp(i);
                if(comp != null){
                    components.add(comp);
                }else{
                    throw new IllegalArgumentException("Type '" + i + "' is not a component interface!");
                }
            }

            ObjectSet<Stype> out = new ObjectSet<>();
            for(Stype comp : components){
                //get dependencies for each def, add them
                out.add(comp);
                out.addAll(getDependencies(comp));
            }

            defComponents.put(type, out.asArray());
        }

        return defComponents.get(type);
    }

    Seq<Stype> getDependencies(Stype component){
        if(!componentDependencies.containsKey(component)){
            ObjectSet<Stype> out = new ObjectSet<>();
            //add base component interfaces
            out.addAll(component.interfaces().select(this::isCompInterface).map(this::interfaceToComp));
            //remove self interface
            out.remove(component);

            //out now contains the base dependencies; finish constructing the tree
            ObjectSet<Stype> result = new ObjectSet<>();
            for(Stype type : out){
                result.add(type);
                result.addAll(getDependencies(type));
            }

            if(component.annotation(Annotations.BaseComponent.class) == null){
                result.addAll(baseComponents);
            }

            //remove it again just in case
            out.remove(component);
            componentDependencies.put(component, result.asArray());
        }

        return componentDependencies.get(component);
    }

    boolean isCompInterface(Stype type){
        return interfaceToComp(type) != null;
    }

    String createName(Selement<?> elem){
        Seq<Stype> comps = types(elem.annotation(Annotations.EntityDef.class), Annotations.EntityDef::value).map(this::interfaceToComp);;
        comps.sortComparing(Selement::name);
        return comps.toString("", s -> s.name().replace("Comp", ""));
    }

    <T extends Annotation> Seq<Stype> types(T t, Cons<T> consumer){
        try{
            consumer.get(t);
        }catch(MirroredTypesException e){
            return Seq.with(e.getTypeMirrors()).map(Stype::of);
        }
        throw new IllegalArgumentException("Missing types.");
    }

    class GroupDefinition{
        final String name;
        final ClassName baseType;
        final Seq<Stype> components;
        final boolean spatial, mapping, collides;
        final ObjectSet<Selement> manualInclusions = new ObjectSet<>();

        public GroupDefinition(String name, ClassName bestType, Seq<Stype> components, boolean spatial, boolean mapping, boolean collides){
            this.baseType = bestType;
            this.components = components;
            this.name = name;
            this.spatial = spatial;
            this.mapping = mapping;
            this.collides = collides;
        }

        @Override
        public String toString(){
            return name;
        }
    }

    class EntityDefinition{
        final Seq<GroupDefinition> groups;
        final Seq<Stype> components;
        final Seq<FieldSpec> fieldSpecs;
        final TypeSpec.Builder builder;
        final Selement naming;
        final String name;
        final @Nullable TypeName extend;
        int classID;

        public EntityDefinition(String name, TypeSpec.Builder builder, Selement naming, TypeName extend, Seq<Stype> components, Seq<GroupDefinition> groups, Seq<FieldSpec> fieldSpec){
            this.builder = builder;
            this.name = name;
            this.naming = naming;
            this.groups = groups;
            this.components = components;
            this.extend = extend;
            this.fieldSpecs = fieldSpec;
        }

        @Override
        public String toString(){
            return "Definition{" +
                    "groups=" + groups +
                    "components=" + components +
                    ", base=" + naming +
                    '}';
        }
    }
}
