package braindustry.content.Blocks;

import ModVars.modVars;
import arc.Core;
import braindustry.world.blocks.logic.AdvancedSwitcher;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;

import static braindustry.content.Blocks.ModBlocks.*;

class ModLogicBlocks {
    public void load(){
        advancedSwitcher=new AdvancedSwitcher("advanced-switcher"){
            {
                this.requirements(Category.logic, ItemStack.with(Items.graphite, 10));
            }

            @Override
            public void load() {
                if (modVars.packSprites){
                    super.load();
                    return;
                }
                Core.atlas.addRegion(name,Core.atlas.find(Blocks.switchBlock.name));
                Core.atlas.addRegion(name+"-on",Core.atlas.find(Blocks.switchBlock.name+"-on"));
                super.load();
            }
        };
    }
}
