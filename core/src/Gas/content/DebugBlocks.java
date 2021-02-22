package Gas.content;

import Gas.world.blocks.power.AllBurnerGenerator;
import Gas.world.consumers.ConsumeGasses;
import braindustry.content.ModItems;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

public class DebugBlocks {
    static {
        Blocks.class.getClass();
    }
    public static Block gasGenerator,methaneBurner;
    public void load(){
       /* gasGenerator = new AllBurnerGenerator("combustion-generator") {
            {
                this.localizedName = "Combustion Generator";
                this.size = 2;
                this.requirements(Category.power, ItemStack.with(Items.copper, 50, Items.lead, 40, Items.titanium, 25));
                this.powerProduction = 1.0F;
                this.itemDuration = 120.0F;
                this.ambientSound = Sounds.smelter;
                this.ambientSoundVolume = 0.03F;
            }
        };*/
        methaneBurner = new AllBurnerGenerator("methane-burner") {
            {
//                Blocks.steamGenerator
                this.hasPower = true;
                this.hasGas = true;
                this.localizedName = "Methane Burner";
                this.description = "Burn Methane to produce heat energy.";
                this.powerProduction = 5f;
                this.gasCapacity = 11f;
                this.size = 2;
                this.consumes.addGas(new ConsumeGasses(Gasses.methane, 1));
                this.requirements(Category.power, ItemStack.with(Items.silicon, 60, Items.titanium, 50, ModItems.chromium, 90));
            }
        };
    }
}
