package braindustry.entities.Advanced;

import arc.struct.Seq;

public interface UnitExtensionsImplements {
    Seq<UnitExtensions> unitExtensions=new Seq<>();
    default void extensions(UnitExtensions... extensions){
        unitExtensions.addAll(extensions);
    }
}
