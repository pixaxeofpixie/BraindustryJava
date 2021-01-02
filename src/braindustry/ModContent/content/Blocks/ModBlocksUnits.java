package braindustry.ModContent.content.Blocks;

import braindustry.ModContent.content.ModItems;
import braindustry.ModContent.content.ModLiquids;
import braindustry.ModContent.content.ModUnitTypes;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

import static braindustry.ModContent.content.ModBlocks.*;
public class ModBlocksUnits implements ContentList {

    @Override
    public void load() {
        hyperAdditiveReconstructor = new Reconstructor("hyper-additive-reconstructor") {
            {
                this.localizedName = "Hyper Additive Reconstructor";
                this.size = 3;
                this.consumes.power(3.2f);
                this.consumes.items(ItemStack.with(Items.silicon, 35, ModItems.graphenite, 15, Items.thorium, 20, Items.titanium, 20));
                this.constructTime = 900;
                this.requirements(Category.units, ItemStack.with(Items.copper, 200, ModItems.odinum, 100, Items.titanium, 120, Items.silicon, 90, Items.plastanium, 80));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.ibis, ModUnitTypes.aries},
                        new UnitType[]{ModUnitTypes.armor, ModUnitTypes.shield},
                        new UnitType[]{ModUnitTypes.venti, ModUnitTypes.lyra}
                );
            }
        };
        hyperAirFactory = new UnitFactory("hyper-air-factory") {
            {
                this.localizedName = "Hyper Air Factory";
                this.size = 3;
                this.consumes.power(2.2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.armor, 600, ItemStack.with(Items.silicon, 15, ModItems.graphenite, 5, Items.lead, 5))
                );
                this.requirements(Category.units, ItemStack.with(Items.copper, 50, Items.lead, 150, Items.silicon, 70, Items.plastanium, 30, ModItems.odinum, 60));
            }
        };
        hyperExponentialReconstructor = new Reconstructor("hyper-exponential-reconstructor") {
            {
                this.localizedName = "Hyper Exponential Reconstructor";
                this.size = 7;
                this.consumes.power(7f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(false, false);
                this.consumes.items(ItemStack.with(Items.silicon, 100, ModItems.graphenite, 100, Items.titanium, 125, Items.plastanium, 75, ModItems.exoticAlloy, 40));
                this.constructTime = 2100;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 1200, Items.titanium, 900, ModItems.odinum, 500, Items.plastanium, 200, Items.phaseFabric, 300, Items.silicon, 750, ModItems.exoticAlloy, 130));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.capra, ModUnitTypes.lacerta},
                        new UnitType[]{ModUnitTypes.chestplate, ModUnitTypes.chainmail},
                        new UnitType[]{ModUnitTypes.tropsy, ModUnitTypes.cenda}
                );
            }
        };
        hyperGroundFactory = new UnitFactory("hyper-ground-factory") {
            {
                this.localizedName = "Hyper Ground Factory";
                this.size = 3;
                this.consumes.power(2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.ibis, 550, ItemStack.with(Items.silicon, 10, ModItems.graphenite, 5, ModItems.odinum, 5))
                );
                this.requirements(Category.units, ItemStack.with(Items.copper, 50, Items.lead, 140, Items.silicon, 70, Items.plastanium, 60));
            }
        };
        hyperMultiplicativeReconstructor = new Reconstructor("hyper-multiplicative-reconstructor") {
            {
                this.localizedName = "Hyper Multiplicative Reconstructor";
                this.size = 5;
                this.consumes.power(6.2f);
                this.consumes.items(ItemStack.with(Items.silicon, 60, ModItems.graphenite, 50, Items.metaglass, 30, Items.titanium, 30, ModItems.odinum, 30));
                this.constructTime = 1620;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 600, Items.titanium, 300, Items.silicon, 300, ModItems.odinum, 500, Items.plastanium, 150));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.aries, ModUnitTypes.capra},
                        new UnitType[]{ModUnitTypes.shield, ModUnitTypes.chestplate},
                        new UnitType[]{ModUnitTypes.lyra, ModUnitTypes.tropsy}
                );
            }
        };
        hyperNavalFactory = new UnitFactory("hyper-naval-factory") {
            {
                this.localizedName = "Hyper Naval Factory";
                this.size = 3;
                this.consumes.power(2.2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.venti, 630, ItemStack.with(Items.silicon, 5, ModItems.graphenite, 10, Items.metaglass, 10))
                );
                this.requirements(Category.units, ItemStack.with(Items.lead, 150, Items.silicon, 80, Items.plastanium, 25, Items.metaglass, 85, ModItems.odinum, 55));
            }
        };
        hyperTetrativeReconstructor = new Reconstructor("hyper-tetrative-reconstructor") {
            {
                this.localizedName = "Hyper Tetrative Reconstructor";
                this.size = 9;
                this.consumes.power(9f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.3f).optional(false, false);
                this.consumes.items(ItemStack.with(ModItems.graphenite, 300, Items.surgeAlloy, 300, ModItems.odinum, 200, Items.plastanium, 400, ModItems.exoticAlloy, 200));
                this.constructTime = 3600;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 3100, Items.surgeAlloy, 500, ModItems.odinum, 500, Items.plastanium, 400, Items.phaseFabric, 600, Items.silicon, 1500, ModItems.exoticAlloy, 1000));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.lacerta, ModUnitTypes.aquila},
                        new UnitType[]{ModUnitTypes.chainmail, ModUnitTypes.broadsword}
                );
            }
        };
    }
}
