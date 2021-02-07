package braindustry.content.Blocks;

import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.content.ModUnitTypes;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

import static braindustry.content.ModBlocks.*;
public class ModUnitsBlocks implements ContentList {

    @Override
    public void load() {
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
        hyperGroundFactory = new UnitFactory("hyper-ground-factory") {
            {
                this.localizedName = "Hyper Ground Factory";
                this.size = 3;
                this.consumes.power(2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.ibis, 550, ItemStack.with(Items.silicon, 10, ModItems.graphenite, 5, ModItems.odinum, 5)),
                        new UnitPlan(ModUnitTypes.tyzen, 510, ItemStack.with(Items.silicon, 10, ModItems.plastic, 2))
                );
                this.requirements(Category.units, ItemStack.with(Items.copper, 50, Items.lead, 140, Items.silicon, 70, Items.plastanium, 60));
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
        hyperAdditiveReconstructor = new Reconstructor("hyper-additive-reconstructor") {
            {
                this.localizedName = "Hyper Additive Reconstructor";
                this.size = 3;
                this.consumes.power(3.2f);
                this.consumes.items(ItemStack.with(Items.silicon, 55, ModItems.graphenite, 25, Items.thorium, 20, Items.titanium, 30));
                this.constructTime = 900;
                this.requirements(Category.units, ItemStack.with(Items.copper, 300, ModItems.odinum, 100, Items.titanium, 120, Items.silicon, 190, ModItems.plastic, 20));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.ibis, ModUnitTypes.aries},
                        new UnitType[]{ModUnitTypes.armor, ModUnitTypes.shield},
                        new UnitType[]{ModUnitTypes.venti, ModUnitTypes.lyra},
                        new UnitType[]{ModUnitTypes.tyzen, ModUnitTypes.kryox}
                );
            }
        };
        hyperExponentialReconstructor = new Reconstructor("hyper-exponential-reconstructor") {
            {
                this.localizedName = "Hyper Exponential Reconstructor";
                this.size = 7;
                this.consumes.power(7f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.3f).optional(false, false);
                this.consumes.items(ItemStack.with(Items.silicon, 120, ModItems.graphenite, 100, Items.titanium, 125, Items.plastanium, 75));
                this.constructTime = 2100;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 1200, Items.titanium, 900, ModItems.odinum, 500, Items.plastanium, 200, Items.phaseFabric, 300, Items.silicon, 130));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.capra, ModUnitTypes.lacerta},
                        new UnitType[]{ModUnitTypes.chestplate, ModUnitTypes.chainmail},
                        new UnitType[]{ModUnitTypes.tropsy, ModUnitTypes.cenda},
                        new UnitType[]{ModUnitTypes.intelix, ModUnitTypes.nemesis}
                );
            }
        };
        hyperMultiplicativeReconstructor = new Reconstructor("hyper-multiplicative-reconstructor") {
            {
                this.localizedName = "Hyper Multiplicative Reconstructor";
                this.size = 5;
                this.consumes.power(12.2f);
                this.consumes.items(ItemStack.with(Items.silicon, 300, ModItems.graphenite, 250, Items.metaglass, 130, Items.titanium, 70, ModItems.odinum, 70, ModItems.plastic, 50));
                this.constructTime = 1620;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 600, Items.titanium, 300, Items.silicon, 300, ModItems.odinum, 500, ModItems.plastic, 100));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.aries, ModUnitTypes.capra},
                        new UnitType[]{ModUnitTypes.shield, ModUnitTypes.chestplate},
                        new UnitType[]{ModUnitTypes.lyra, ModUnitTypes.tropsy},
                        new UnitType[]{ModUnitTypes.kryox, ModUnitTypes.intelix}
                );
            }
        };
        hyperTetrativeReconstructor = new Reconstructor("hyper-tetrative-reconstructor") {
            {
                this.localizedName = "Hyper Tetrative Reconstructor";
                this.size = 9;
                this.consumes.power(9f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.3f).optional(false, false);
                this.consumes.items(ItemStack.with(ModItems.graphenite, 800, Items.surgeAlloy, 400, ModItems.odinum, 500, ModItems.plastic, 250, ModItems.exoticAlloy, 210));
                this.constructTime = 3600;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 3100, Items.surgeAlloy, 500, ModItems.odinum, 500, Items.plastanium, 400, Items.phaseFabric, 600, Items.silicon, 1500, ModItems.exoticAlloy, 1000));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.lacerta, ModUnitTypes.aquila},
                        new UnitType[]{ModUnitTypes.chainmail, ModUnitTypes.broadsword},
                        new UnitType[]{ModUnitTypes.cenda, ModUnitTypes.vyvna},
                        new UnitType[]{ModUnitTypes.nemesis, ModUnitTypes.maverix}
                );
            }
        };
        ultraReconstructor = new Reconstructor("ultra-reconstructor") {
            {
                this.localizedName = "Ultra Reconstructor";
                this.size = 11;
                this.consumes.power(120f);
                this.consumes.liquid(ModLiquids.liquidMethane, 1.2f).optional(false, false);
                this.consumes.items(ItemStack.with(Items.silicon, 1700, ModItems.graphenite, 2200, Items.surgeAlloy, 1700, ModItems.odinum, 2000, ModItems.plastic, 1000, ModItems.exoticAlloy, 700));
                this.constructTime = 7200;
                this.requirements(Category.units, ItemStack.with(ModItems.graphenite, 3100, Items.surgeAlloy, 400, ModItems.odinum, 700, Items.plastanium, 400, Items.phaseFabric, 600, Items.silicon, 1500, ModItems.exoticAlloy, 1000));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.ibis, ModUnitTypes.griffon},
                new UnitType[]{ModUnitTypes.nemesis, ModUnitTypes.maverix}//НЕ УДАЛЯЙ ЭТО
                );
            }
        };
    }
}
