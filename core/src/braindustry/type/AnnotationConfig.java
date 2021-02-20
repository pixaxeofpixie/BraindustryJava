package braindustry.type;

import braindustry.annotations.ModAnnotations;
import mindustry.gen.Unitc;
@ModAnnotations.AssetFolderFinder
public class AnnotationConfig {
    @ModAnnotations.EntitySuperClass
    public static interface Unitc extends mindustry.gen.Unitc{
    }
    @ModAnnotations.EntitySuperClass
    public static interface Mechc extends mindustry.gen.Mechc{
    }
    @ModAnnotations.EntitySuperClass
    public static interface Flyingc extends mindustry.gen.Flyingc{
    }
//    @ModAnnotations.EntitySuperComp
//    public static interface UnitComp extends mindustry.entities.comp.UnitComp{
//    }
}
