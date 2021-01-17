package mindustryAddition.world.meta.values;

import arc.scene.ui.layout.Table;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.LiquidDisplay;
import mindustry.world.meta.StatValue;

public class LiquidListValue implements StatValue {
    private final LiquidStack[] stacks;
    private final boolean displayName;

    public LiquidListValue(LiquidStack... stacks) {
        this(true, stacks);
    }

    public LiquidListValue(boolean displayName, LiquidStack... stacks) {
        this.stacks = stacks;
        this.displayName = displayName;
    }

    public void display(Table table) {
        LiquidStack[] var2 = this.stacks;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            LiquidStack stack = var2[var4];
            table.add(new ModLiquidDisplay(stack.liquid, stack.amount, this.displayName)).padRight(5.0F);
        }

    }
}
