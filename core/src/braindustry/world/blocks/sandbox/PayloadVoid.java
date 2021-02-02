package braindustry.world.blocks.sandbox;

import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.PayloadConveyor;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.meta.BlockGroup;

public class PayloadVoid extends Block {

    public PayloadVoid(String name) {
        super(name);
        outputsPayload=false;
        this.group = BlockGroup.transportation;
        this.update = this.solid =  true;
        category= Category.distribution;
    }
    public class PayloadVoidBuild extends Building {

        @Override
        public void updateTile() {
            proximity.select(b->b instanceof PayloadConveyor.PayloadConveyorBuild).map(b->(PayloadConveyor.PayloadConveyorBuild) b).each(conv->{
                Point2 point= Geometry.d4(conv.rotation);
                int csize = conv.block.size;
                if (conv.nearby(point.x* csize,point.y*csize)==this && conv.next!=this){
                    conv.next=this;
                }
            });
        }

        public boolean acceptItem(Building source, Item item) {
            return this.enabled;
        }

        @Override
        public void handlePayload(Building source, Payload payload) {

        }
    }
}
