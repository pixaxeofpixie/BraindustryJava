package Gas.content;

import Gas.AllGenerator;
import Gas.world.blocks.power.AllBurnerGenerator;
import Gas.world.consumers.ConsumeGasses;
import braindustry.content.ModGasses;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.power.BurnerGenerator;

public class DebugBlocks {
    static {
        Blocks.class.getClass();
    }
    public static Block gasGenerator,methaneBurner;
    public void load(){
        gasGenerator = new AllBurnerGenerator("combustion-generator") {
            {
                this.requirements(Category.power, ItemStack.with(Items.copper, 25, Items.lead, 15));
                this.powerProduction = 1.0F;
                this.itemDuration = 120.0F;
                this.ambientSound = Sounds.smelter;
                this.ambientSoundVolume = 0.03F;
            }
        };
        methaneBurner = new AllBurnerGenerator("methane-burner") {
            {
               this.update = true;
               this.hasPower = true;
               this.hasGas = true;
               this.localizedName = "methane burner";
               this.description = "burn all you methane";
               this.powerProduction = 5f;
               this.gasCapacity = 5f;
               this.size = 2;
               this.consumes.add(new ConsumeGasses(Gasses.methane, 1));
               this.requirements(Category.power, ItemStack.with(Items.silicon, 60, Items.copper, 50, Items.graphite, 90));
            }
        };
    }
}
