package Gas.world.draw;

import Gas.world.blocks.production.GasGenericCrafter;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;

public class GasDrawBlock {
    public GasDrawBlock() {
    }

    public void draw(GasGenericCrafter.GasGenericCrafterBuild entity) {
        Draw.rect(entity.block.region, entity.x, entity.y, entity.block.rotate ? entity.rotdeg() : 0.0F);
    }

    public void load(Block block) {
    }

    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{block.region};
    }
}
