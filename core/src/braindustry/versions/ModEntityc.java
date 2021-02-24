package braindustry.versions;

import braindustry.annotations.ModAnnotations;
import mindustry.gen.Entityc;
import braindustry.gen.ModEntityMapping;

@ModAnnotations.EntityInterface
public interface ModEntityc extends Entityc {
    /*static int classId(){
        return -66;
    }*/
    @ModAnnotations.DefaultValue("66")
    int classId();
    @ModAnnotations.DefaultValue(value = "ModEntityMapping.getId(this.getClass())",imports = {ModEntityMapping.class})
    int modClassId();
}
