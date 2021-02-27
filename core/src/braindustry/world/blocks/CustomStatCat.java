package braindustry.world.blocks;

import arc.Core;
import arc.struct.Seq;
import braindustry.annotations.ModAnnotations;
import braindustry.world.meta.CustomStat;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

@ModAnnotations.CustomStatCat
public enum CustomStatCat {
    gasses;
    public String localized(){
        return Core.bundle.get("category." + name());
    }
    public static CustomStatCat fromExist(StatCat stat){
        return Seq.with(values()).find((v)-> v.name().equals(stat.name()));
    }
}
