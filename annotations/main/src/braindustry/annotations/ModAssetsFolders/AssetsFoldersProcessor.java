package braindustry.annotations.ModAssetsFolders;

import arc.files.Fi;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import braindustry.annotations.ModBaseProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;
import java.util.Objects;

@SupportedAnnotationTypes({
        "braindustry.annotations.ModAnnotations.AssetFolderFinder",
})
public class AssetsFoldersProcessor extends ModBaseProcessor {
    {
        rounds=1;
    }

    private MethodSpec getFoldersMethod(){
        MethodSpec.Builder method = MethodSpec.methodBuilder("getFolders").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(ClassName.get(Seq.class),
                        tname(String.class)));
method.addStatement("return Seq.with(\""+ Seq.with(rootDirectory.child("core").child("assets").list()).map(Fi::name).toString("\",\"")+"\")")
        ;
        return method.build();
    }
    @Override
    public void process(RoundEnvironment env) throws Exception {
//        print("absolutePath: @",);
        TypeSpec.Builder assetsFolders = TypeSpec.classBuilder("AssetsFolders").addModifiers(Modifier.PUBLIC);
        assetsFolders.addMethod(getFoldersMethod());
        write(assetsFolders);
//        throw  new Exception("SOmeeeer");
    }
}
