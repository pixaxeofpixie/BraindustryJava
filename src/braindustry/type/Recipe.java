package braindustry.type;

import arc.struct.Seq;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustryAddition.type.ModLiquidStack;

public class Recipe {
    public static Recipe empty = with(null, -1);
    public ItemStack outputItem;
    public LiquidStack outputLiquid;
    public ItemStack[] consumeItems;
    public LiquidStack[] consumeLiquids;

    //        public TextureRegion textureRegion;
    public float produceTime;

    public Recipe() {
        consumeItems = ItemStack.empty;
        consumeLiquids = ModLiquidStack.empty;

    }

    public static Recipe with(ItemStack outputItem, ItemStack[] consumeItems, LiquidStack[] consumeLiquids, float produceTime) {
        Recipe recipe = new Recipe();
        if (outputItem != null) recipe.outputItem = outputItem;
        if (consumeItems != null) recipe.consumeItems = consumeItems;
        if (consumeLiquids != null) recipe.consumeLiquids = consumeLiquids;
        recipe.produceTime = produceTime;
        recipe.check();
        return recipe;
    }

    public static Recipe with(ItemStack outputItem, LiquidStack[] consumeLiquids, float produceTime) {
        return with(outputItem, ItemStack.empty, consumeLiquids, produceTime);
    }

    public static Recipe with(ItemStack outputItem, float produceTime) {
        return with(outputItem, ModLiquidStack.empty, produceTime);
    }

    public static Recipe with(ItemStack outputItem, ItemStack[] consumeItems, float produceTime) {
        return with(outputItem, consumeItems, ModLiquidStack.empty, produceTime);
    }

    private void checkItems() {
        if (true) return;
        Seq<ItemStack> itemStacks = new Seq<>(consumeItems);
        Seq<ItemStack> newItemsStacks = new Seq<>();
        itemStacks.each((itemStack -> {
            ItemStack found = newItemsStacks.find(s -> s.item == itemStack.item);
            if (found != null) {
                found.amount += itemStack.amount;
            } else {
                newItemsStacks.add(itemStack);
            }
        }));
        this.consumeItems = newItemsStacks.toArray(ItemStack.class);
    }

    private void checkLiquids() {
        Seq<LiquidStack> liquidsStacks = new Seq<>(consumeLiquids);
        Seq<LiquidStack> newLiquidStacks = new Seq<>();
        liquidsStacks.each((liquidStack -> {
            LiquidStack found = newLiquidStacks.find(s -> s.liquid == liquidStack.liquid);
            if (found != null) {
                found.amount += liquidStack.amount;
            } else {
                newLiquidStacks.add(liquidStack);
            }
        }));
        this.consumeLiquids = newLiquidStacks.toArray(LiquidStack.class);
    }

    private void check() {
//            checkItems();
//            checkLiquids();
    }
}