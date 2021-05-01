package braindustry.content.Blocks;

import Gas.content.Gasses;
import Gas.world.blocks.power.AllBurnerGenerator;
import Gas.world.consumers.ConsumeGasses;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.world.blocks.power.BlackHoleReactor;
import braindustry.world.blocks.power.MaterialReactor;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.BuildVisibility;

import static braindustry.content.Blocks.ModBlocks.*;

class ModPowerBlocks implements ContentList {
    @Override
    public void load() {
        methaneBurner = new AllBurnerGenerator("methane-burner") {{
            this.hasPower = true;
            this.hasGas = true;
            this.localizedName = "Methane Burner";
            this.description = "Burn Methane to produce heat energy.";
            this.powerProduction = 5f;
            this.gasCapacity = 11f;
            this.size = 2;
            this.consumes.addGas(new ConsumeGasses(Gasses.methane, 1));
            this.requirements(Category.power, ItemStack.with(Items.silicon, 60, Items.titanium, 50, ModItems.chromium, 90));
        }};
        hyperMethaneBurner = new AllBurnerGenerator("hyper-methane-burner") {{
            this.hasPower = true;
            this.hasGas = true;
            this.localizedName = "Hyper Methane Burner";
            this.description = "Burn Methane more effective but consumes water.";
            this.powerProduction = 11f;
            this.gasCapacity = 25f;
            this.size = 3;
            this.consumes.addGas(new ConsumeGasses(Gasses.methane, 1));
            this.consumes.liquid(Liquids.water, 0.5f);
            this.requirements(Category.power, ItemStack.with(Items.silicon, 60, Items.plastanium, 60, Items.titanium, 50, ModItems.chromium, 1200));
        }};
        phaseTower = new PowerNode("phase-tower") {{
            this.localizedName = "Phase Tower";
            this.health = 240;
            this.size = 4;
            this.maxNodes = 12;
            this.laserRange = 45;
            this.requirements(Category.power, ItemStack.with(ModItems.phaseAlloy, 15, Items.silicon, 20, Items.titanium, 10, Items.lead, 10));
        }};
        grapheniteSolarCollectorLarge = new SolarGenerator("graphenite-solar-collector-large") {{
            this.localizedName = "Graphenite Solar Collector Large";
            this.description = "Solar panel with Graphenite elements, better than Large Solar Panel.";
            this.powerProduction = 1.6f;
            this.size = 3;
            this.health = 210;
            this.requirements(Category.power, ItemStack.with(Items.metaglass, 100, Items.silicon, 40, ModItems.graphenite, 70));
        }};
        differentialMagmaGenerator = new SingleTypeGenerator("differential-magma-generator") {{
            this.localizedName = "Hyper Magma Generator";
            this.description = "Consumes Magma and Pyratite to produce much heat power.";
            this.size = 3;
            this.hasPower = true;
            this.health = 130;
            this.hasItems = true;
            this.itemCapacity = 30;
            this.hasLiquids = true;
            this.liquidCapacity = 40;
            this.itemDuration = 100;
            this.powerProduction = 20.6f;
            this.consumes.liquid(ModLiquids.magma, 0.2f);
            this.consumes.items(ItemStack.with(Items.pyratite, 3));
            this.requirements(Category.power, ItemStack.with(ModItems.graphenite, 140, ModItems.chromium, 190, Items.plastanium, 120, Items.titanium, 200, ModItems.odinum, 70));
        }};
        magmaGenerator = new BurnerGenerator("magma-generator") {{
            this.localizedName = "Magma Generator";
            this.description = "Consumes Magma and Coal to produce a lot of power.";
            this.size = 2;
            this.hasPower = true;
            this.hasItems = true;
            this.itemCapacity = 10;
            this.hasLiquids = true;
            this.liquidCapacity = 15;
            this.itemDuration = 90;
            this.powerProduction = 8;
            this.consumes.liquid(ModLiquids.magma, 0.04f);
            this.consumes.items(ItemStack.with(Items.coal, 1));
            this.requirements(Category.power, ItemStack.with(ModItems.graphenite, 50, Items.silicon, 80, Items.plastanium, 120, ModItems.chromium, 160));
        }};
        refrigerantReactor = new NuclearReactor("refrigerant-reactor") {{
            this.localizedName = "Refrigerant Reactor";
            this.description = "Upgraded Thorium Reactor. Now it uses the Refrigerant.";
            this.size = 4;
            this.hasPower = true;
            this.hasLiquids = true;
            this.hasItems = true;
            this.itemCapacity = 10;
            this.liquidCapacity = 10;
            this.itemDuration = 120;
            this.powerProduction = 78;
            this.heating = 0.07f;
            this.smokeThreshold = 0.48f;
            this.explosionRadius = 90;
            this.explosionDamage = 1900;
            this.flashThreshold = 0.34f;
            this.coolantPower = 0.9f;
            this.consumes.power(3f);
            this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f);
            this.consumes.items(ItemStack.with(Items.thorium, 3));
            this.requirements(Category.power, ItemStack.with(Items.metaglass, 300, ModItems.odinum, 100, Items.silicon, 210, ModItems.graphenite, 270, Items.thorium, 200, Items.titanium, 140));
        }};
        odinumReactor = new ImpactReactor("odinum-reactor") {{
            this.localizedName = "Odinum Reactor";
            this.description = "Atomic fission and collision reactor.Consumes Odinum and Liquid Graphenite.";
            this.size = 4;
            this.hasPower = true;
            this.hasLiquids = true;
            this.hasItems = true;
            this.itemCapacity = 20;
            this.liquidCapacity = 20;
            this.itemDuration = 90;
            this.powerProduction = 126;
            this.consumes.power(9f);
            this.consumes.liquid(ModLiquids.liquidGraphenite, 0.1f);
            this.consumes.items(ItemStack.with(ModItems.odinum, 1));
            this.requirements(Category.power, ItemStack.with(Items.metaglass, 500, ModItems.odinum, 300, Items.silicon, 400, ModItems.graphenite, 200, Items.plastanium, 200));
        }};
        materialReactor = new MaterialReactor("materia-reactor") {
            {
                localizedName = "Material Reactor";
                description = "Produce power from different type of matter, amount of power depends on the characteristics of matter ";
                itemCapacity = 150;
                liquidCapacity = 150;
                hasPower = true;
                hasItems = true;
                hasLiquids = true;
                size = 6;
                health = 8250;
                requirements(Category.power, BuildVisibility.shown, ItemStack.with(
                        Items.silicon, 350,
                        Items.plastanium, 400, Items.surgeAlloy, 300, ModItems.graphenite, 375, ModItems.chloroAlloy, 210
                ));
            }
        };
        blackHoleReactor = new BlackHoleReactor("black-hole-reactor") {{
            this.localizedName = "Blackhole Reactor";
            this.description = "Create power from small Blackhole in center.";
            this.size = 6;
            this.hasPower = true;
            this.hasLiquids = true;
            this.hasItems = true;
            this.hasGas = true;
            this.gasCapacity = 100;
            this.itemCapacity = 60;
            this.liquidCapacity = 100;
            this.itemDuration = 240;
            this.powerProduction = 220;
            this.consumes.addGas(new ConsumeGasses(Gasses.methane, 1));
            this.consumes.power(46f);
            this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.5f);
            this.consumes.item(ModItems.odinum, 12);
            this.requirements(Category.power, ItemStack.with(ModItems.chloroAlloy, 300, Items.surgeAlloy, 200, Items.graphite, 500, ModItems.odinum, 100));
        }};
        phaseAlloySolarPanel = new SolarGenerator("phase-alloy-solar-panel") {{
            this.localizedName = "Dense Composite Solar Panel";
            this.description = "An improved version of the Large Solar Panel. Takes up a lot of space.";
            this.health = 960;
            this.size = 5;
            this.hasPower = true;
            this.powerProduction = 8.4f;
            this.requirements(Category.power, ItemStack.with(Items.thorium, 280, Items.silicon, 400, Items.titanium, 90, ModItems.graphenite, 80, ModItems.phaseAlloy, 115));
            this.buildCostMultiplier = 1.1f;
        }};
    }
}
