package mindustryAddition.world.blocks;

import arc.util.Disposable;
import mindustry.ctype.Content;
import mindustryAddition.world.meta.AStats;

public interface BlockAdvancedStats extends Comparable<Content>, Disposable {
    public AStats aStats = new AStats();
    default void initAStats(){
//        aStats;
    }
    default AStats getAStats(){
        return aStats;
    }
}
