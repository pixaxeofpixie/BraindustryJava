package braindustry.world.blocks.distribution;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import braindustry.annotations.ModAnnotations;
import mindustry.world.blocks.distribution.PayloadConveyor;

public class ArmoredPayloadConveyor extends PayloadConveyor {
    public @ModAnnotations.Load("@-up")  TextureRegion up;
    public ArmoredPayloadConveyor(String name) {
        super(name);
    }
    public class ArmoredPayloadConveyorBuild extends PayloadConveyorBuild{
        @Override
        public void draw() {
            super.draw();
            Draw.rect(up,x,y,rotation);
        }

        @Override
        public void drawBottom() {
            super.drawBottom();
        }
    }
}
