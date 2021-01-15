package Gas.content;

import Gas.world.blocks.gas.*;
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
                this.size = 1;
                this.buildVisibility = BuildVisibility.sandboxOnly;
                this.requirements(Category.liquid, ItemStack.with(Items.copper, 3, Items.silicon, 10));
            }
        };
        gasConduit = new GasConduit("gas-conduit") {
            {
                this.size = 1;
                this.requirements(Category.liquid, ItemStack.with(Items.copper, 3, Items.silicon, 10));
            }
        };
    }
}
