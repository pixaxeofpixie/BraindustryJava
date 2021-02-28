package braindustry.world.blocks.power;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import braindustry.annotations.ModAnnotations;
import mindustry.world.blocks.power.PowerNode;

public class ReceivingPowerNode extends PowerNode {
    public float rotateSpeed=2f;
    public @ModAnnotations.Load("@-rotator")
    TextureRegion rotatorRegion;
    public @ModAnnotations.Load("@-bottom")
    TextureRegion bottomRegion;
    public ReceivingPowerNode(String name) {
        super(name);
    }
    public class ReceivingPowerNodeBuild extends PowerNodeBuild{
        float timeDrilled;

        @Override
        public void updateTile() {
            super.updateTile();
            timeDrilled+=delta();
        }

        @Override
        public void draw() {
            region=bottomRegion;
            super.draw();
//            Draw.rect(rotatorRegion,x,y,rotateSpeed*timeDrilled);
        }
    }
}
