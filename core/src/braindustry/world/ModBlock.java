package braindustry.world;

import braindustry.gen.ModContentRegions;
import braindustry.world.meta.AStats;
import mindustry.world.Block;

public class ModBlock extends Block {
    AStats aStats = new AStats();
    public ModBlock(String name) {
        super(name);
        super.stats=aStats.copy(stats);
    }

    @Override
    public void load() {
        super.load();
        ModContentRegions.loadRegions(this);
    }
}
