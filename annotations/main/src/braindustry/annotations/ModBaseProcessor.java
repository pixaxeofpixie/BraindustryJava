package braindustry.annotations;

import arc.struct.Seq;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import mindustry.annotations.BaseProcessor;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.OutputStream;

public abstract class ModBaseProcessor extends BaseProcessor {

    public static final String packageName = "braindustry.gen";
    public static final String parentName = "mindustry.gen";
    public static TypeName tname(String pack, String simple){
        return ClassName.get(pack, simple );
    }

    public static TypeName tname(String name){
        if(!name.contains(".")) return ClassName.get(packageName, name);

        String pack = name.substring(0, name.lastIndexOf("."));
        String simple = name.substring(name.lastIndexOf(".") + 1);
        return ClassName.get(pack, simple);
    }

    public static TypeName tname(Class<?> c){
        return ClassName.get(c).box();
    }
    public static void write(TypeSpec.Builder builder) throws Exception{
        write(builder, (Seq<String>)null);
    }

    public static void write(TypeSpec.Builder builder, Seq<ClassName> imports,int ZERO) throws Exception{
        write(builder,imports.<String>map(className -> "import "+className.reflectionName()+";"));
    }
    public static void write(TypeSpec.Builder builder, Seq<String> imports) throws Exception{
        JavaFile file = JavaFile.builder(packageName, builder.build()).skipJavaLangImports(true).build();

        if(imports != null){
            String rawSource = file.toString();
            Seq<String> result = new Seq<>();
            for(String s : rawSource.split("\n", -1)){
                result.add(s);
                if (s.startsWith("package ")){
                    result.add("");
                    for (String i : imports){
                        result.add(i);
                    }
                }
            }

            String out = result.toString("\n");
            JavaFileObject object = filer.createSourceFile(file.packageName + "." + file.typeSpec.name, file.typeSpec.originatingElements.toArray(new Element[0]));
            OutputStream stream = object.openOutputStream();
            stream.write(out.getBytes());
            stream.close();
        }else{
            file.writeTo(filer);
        }
    }
}
