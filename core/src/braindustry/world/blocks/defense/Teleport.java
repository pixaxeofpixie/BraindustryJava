package braindustry.world.blocks.defense;

import arc.scene.ui.layout.Table;
import braindustry.gen.ModBuilding;
import braindustry.world.ModBlock;
import mindustry.gen.Building;
import mindustry.world.meta.BlockGroup;

public class Teleport extends ModBlock {
    public Teleport(String name) {
        super(name);
        update=true;
        solid=false;
        group = BlockGroup.projectors;
        hasPower = true;
        hasItems = true;
        canOverdrive = true;
    }

    public class TeleportBuild extends ModBuilding{
        @Override
        public void drawSelect() {
            super.drawSelect();
        }

        @Override
        public boolean onConfigureTileTapped(Building other) {
            return super.onConfigureTileTapped(other);
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
        }
    }
}
