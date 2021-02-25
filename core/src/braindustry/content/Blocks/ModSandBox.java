package braindustry.content.Blocks;

import braindustry.world.blocks.sandbox.PayloadSource;
import braindustry.world.blocks.sandbox.PayloadVoid;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

import static braindustry.content.Blocks.ModBlocks.*;
class ModSandBox {
    public void load(){
        payloadSource=new PayloadSource("payload-source"){
            {
                requirements(category, BuildVisibility.debugOnly, ItemStack.empty);
            }
        };
        payloadVoid=new PayloadVoid("payload-void"){
            {
                requirements(category,BuildVisibility.debugOnly, ItemStack.empty);
            }
        };
    }
}
