package Gas.annotations;

import arc.struct.Seq;
import arc.util.Strings;
import braindustry.annotations.ModBaseProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class GasBaseProcessor extends ModBaseProcessor {
    public static String packageName = "Gas.gen";
    public static void write(TypeSpec.Builder builder) throws Exception{
        write(builder, (Seq<String>)null);
    }

    public static void write(TypeSpec.Builder builder, Seq<ClassName> imports, int ZERO) throws Exception{
        write(builder,imports.<String>map(className -> "import "+className.reflectionName()+";"));
    }
    public void delete(String name) throws IOException {
//        print("delete name: @",name);
        FileObject resource;
        resource = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, name);
//        boolean delete = resource.delete();
//        print("delete: @ ,named: @, filer: @",delete,resource.getName(),resource.getClass().getName());
        Files.delete(Paths.get(resource.getName()+".java"));

    }
    public static void write(TypeSpec.Builder builder, Seq<String> imports) throws Exception{
//        Log.logger=new Log.DefaultLogHandler();
//        Log.err());
        String message = Strings.format("builder.build().name=@", builder.build().name);

//      if (message.contains("Stealthc"))  new RuntimeException(message).printStackTrace();
//        System.out.println(message);
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
//            processingEnv
            OutputStream stream = object.openOutputStream();
            stream.write(out.getBytes());
            stream.close();
        }else{
            file.writeTo(filer);
        }
    }
}
