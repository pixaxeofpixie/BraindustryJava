package braindustry.world;

import braindustry.gen.ModContentRegions;
import mindustry.world.Block;

public class ModBlock extends Block {
    public ModBlock(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        ModContentRegions.loadRegions(this);
    }
}
