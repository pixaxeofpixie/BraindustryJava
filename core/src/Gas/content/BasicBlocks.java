package Gas.content;

import Gas.world.blocks.gas.*;
import braindustry.content.ModItems;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class BasicBlocks {
    public static Block gasSource;
    public static Block gasConduit;

    public void load(){
        gasSource = new GasSource("gas-source") {
            {
                this.localizedName = "Gas Source";
                this.description = "Infinite gas source, only for sandbox";
                this.size = 1;
                this.buildVisibility = BuildVisibility.sandboxOnly;
                this.requirements(Category.liquid, ItemStack.with(Items.copper, 3, Items.silicon, 10));
            }
        };
        gasConduit = new GasConduit("gas-conduit") {
            {
                this.description = "Basic gas conduit from Chromium.";
                this.localizedName = "Gas Conduit";
                this.size = 1;
                this.requirements(Category.liquid, ItemStack.with(ModItems.chromium, 4, Items.silicon, 6));
            }
        };
    }
}
