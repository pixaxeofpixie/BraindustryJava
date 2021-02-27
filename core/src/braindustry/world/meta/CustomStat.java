package braindustry.world.meta;

import arc.Core;
import arc.struct.Seq;
import braindustry.annotations.ModAnnotations;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

import java.util.Locale;

@ModAnnotations.CustomStat
public enum  CustomStat {
    maxConnections(StatCat.function),
    recipes(StatCat.function),
    gasCapacity(StatCat.function);
    public final StatCat category;
    public static CustomStat fromExist(Stat stat){
        return Seq.with(values()).find((v)-> v.name().equals(stat.name()));
    }
    private CustomStat(StatCat category) {
        this.category = category;
    }

    private CustomStat() {
        this.category = StatCat.general;
    }

    public String localized() {
        return Core.bundle.get("stat." + this.name().toLowerCase(Locale.ROOT));
    }
}
