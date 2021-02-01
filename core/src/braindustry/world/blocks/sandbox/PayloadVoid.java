package braindustry.world.blocks.sandbox;

import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.meta.BlockGroup;

public class PayloadVoid extends Block {

    public PayloadVoid(String name) {
        super(name);
        outputsPayload=false;
        this.group = BlockGroup.transportation;
        this.update = this.solid =  true;
    }
    public class PayloadVoidBuild extends Building {


        public boolean acceptItem(Building source, Item item) {
            return this.enabled;
        }

        @Override
        public void handlePayload(Building source, Payload payload) {

        }
    }
}
