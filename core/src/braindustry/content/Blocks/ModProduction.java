package braindustry.content.Blocks;

import Gas.GasStack;
import Gas.content.Gasses;
import Gas.world.blocks.production.GasGenericCrafter;
import Gas.world.consumers.ConsumeGasses;
import braindustry.content.ModFx;
import braindustry.content.ModGasses;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.type.Recipe;
import braindustry.world.blocks.production.MultiCrafter;
import braindustry.world.blocks.production.MultiGenericSmelter;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.GenericSmelter;
import braindustry.type.ModLiquidStack;

import static braindustry.content.Blocks.ModBlocks.*;

class ModProduction implements ContentList {
    @Override
    public void load() {
        multiCrafter = new MultiCrafter("multi-crafter") {
            {
                this.size = 3;
                this.localizedName = "Universal Smelter";
                this.update = true;
                this.destructible = true;
                this.hasLiquids = true;
                this.hasShadow = true;

                this.health = 360;
                this.category = Category.crafting;
                dynamicItem = true;
                dynamicLiquid=true;
                this.extraStorageLiquid=3;
                this.extraStorageItem=3;
//                this.changeTexture = true;
                //1st itemStack = выход, вход писать во втором itemStack

                recipes(Recipe.with(new ItemStack(ModItems.graphenite, 1),ItemStack.with(Items.graphite, 2, Items.silicon, 3, Items.titanium, 2, Items.lead, 2), ModLiquidStack.with(Liquids.slag, 1), 150),
                        Recipe.with(new ItemStack(ModItems.exoticAlloy, 1),ItemStack.with(Items.sporePod, 3, Items.titanium, 4, Items.thorium, 3), ModLiquidStack.with(Liquids.water, 2), 140)
                );
                this.requirements(this.category, ItemStack.with(Items.thorium, 200, ModItems.graphenite, 230, Items.lead, 500, Items.plastanium, 120));
//                addResearch(Blocks.mechanicalDrill, this);
            }
        };
        largeMultiCrafter = new MultiCrafter("multi-kiln") {
            {
                this.size = 6;
                this.localizedName = "Multi Kiln";
                this.update = true;
                this.destructible = true;
                this.hasLiquids = true;
                this.hasShadow = true;

                this.health = 980;
                this.category = Category.crafting;
                dynamicItem = true;
                dynamicLiquid = true;
//                this.changeTexture = true;
                recipes(Recipe.with(new ItemStack(ModItems.graphenite, 2),ItemStack.with(Items.graphite, 2, Items.silicon, 3, Items.titanium, 2, Items.lead, 2), ModLiquidStack.with(Liquids.slag, 1), 150),
                        Recipe.with(new ItemStack(ModItems.exoticAlloy, 2),ItemStack.with(Items.sporePod, 3, Items.titanium, 4, Items.thorium, 3), ModLiquidStack.with(Liquids.water, 2), 140),
                        Recipe.with(new ItemStack(ModItems.chromium, 1),ItemStack.with(Items.titanium, 3, Items.metaglass, 4), ModLiquidStack.with(Liquids.oil, 2), 125),
                        Recipe.with(new ItemStack(ModItems.odinum, 2),ItemStack.with(Items.thorium, 3, Items.titanium, 1, Items.plastanium, 3), ModLiquidStack.with(Liquids.water, 2), 155)
                );
                this.requirements(this.category, ItemStack.with(ModItems.odinum, 200, ModItems.graphenite, 230, ModItems.phaseAlloy, 500, ModItems.plastic, 220));
//                addResearch(Blocks.mechanicalDrill, this);
            }
        };

        chromiumForge = new GenericSmelter("chromium-forge") {
            {
                this.localizedName = "Chromium Forge";
                this.description = "This forge can smelt a Chromium from Metaglass and Titanium.";
                this.health = 220;
                this.liquidCapacity = 0;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(2.5f);
                this.consumes.items(ItemStack.with(Items.metaglass, 2, Items.titanium, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 80, Items.titanium, 100, Items.metaglass, 120, Items.silicon, 200, ModItems.graphenite, 200));
                this.outputItem = new ItemStack(ModItems.chromium, 1);
            }
        };
        exoticAlloySmelter = new GenericSmelter("exotic-alloy-smelter") {
            {
                this.localizedName = "Exometal Smelter";
                this.description = "Cultivates Exometal from Thorium, Titanium and Spore Pods.";
                this.health = 140;
                this.liquidCapacity = 10;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(1.2f);
                this.consumes.items(ItemStack.with(Items.sporePod, 1, Items.thorium, 3, Items.titanium, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 40, Items.lead, 100, Items.graphite, 80, ModItems.graphenite, 100, Items.metaglass, 80));
                this.outputItem = new ItemStack(ModItems.exoticAlloy, 2);
            }
        };
        grapheniteFluidizer = new GenericCrafter("graphenite-fluidizer") {
            {
                this.localizedName = "Graphenite Fluidizer";
                this.description = "A fluidizer that turns graphenite into slurry using huge pressure. Consumes water and graphenite.";
                this.size = 2;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.craftTime = 50;
                this.updateEffect = Fx.steam;
                this.consumes.power(4f);
                this.consumes.liquid(Liquids.water, 0.2f);
                this.consumes.items(ItemStack.with(ModItems.graphenite, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.lead, 50, Items.thorium, 80, Items.silicon, 70, Items.titanium, 50, ModItems.graphenite, 85));
                this.outputLiquid = new LiquidStack(ModLiquids.liquidGraphenite, 12f);
            }
        };
        grapheniteForge = new GenericSmelter("graphenite-forge") {
            {
                this.localizedName = "Graphenite Forge";
                this.description = "Combines Graphite, Titanium and Silicon to produce Graphenite.";
                this.health = 80;
                this.liquidCapacity = 0;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.craftTime = 70;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(1.2f);
                this.consumes.items(ItemStack.with(Items.graphite, 2, Items.silicon, 1, Items.titanium, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.lead, 100, Items.titanium, 30, Items.thorium, 40, Items.silicon, 70, Items.graphite, 80));
                this.outputItem = new ItemStack(ModItems.graphenite, 2);
            }
        };
        hyperAlloySmelter = new GenericSmelter("hyper-alloy-smelter") {
            {
                this.localizedName = "Hyper Alloy Smelter";
                this.description = "Bigger Surge Kiln, uses more materials and energy as well as oil for speed and higher amounts of alloy produced.";
                this.health = 300;
                this.liquidCapacity = 80;
                this.size = 5;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(9f);
                this.consumes.liquid(Liquids.oil, 0.2f).optional(false, false);
                this.consumes.items(ItemStack.with(Items.copper, 4, Items.titanium, 4, Items.lead, 5, Items.silicon, 5));
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 120, Items.titanium, 150, Items.metaglass, 100, Items.silicon, 300, ModItems.graphenite, 170, Items.surgeAlloy, 100));
                this.outputItem = new ItemStack(Items.surgeAlloy, 4);
            }
        };
        hyperPhaseWeaver = new GenericSmelter("hyper-phase-weaver") {
            {
                this.localizedName = "Hyper Phase Weaver";
                this.description = "Bigger Phase Weaver, uses more materials and Silicon to produce more Phase Fabric.";
                this.health = 100;
                this.liquidCapacity = 0;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.itemCapacity = 30;
                this.craftTime = 120;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(6f);
                this.consumes.items(ItemStack.with(Items.thorium, 6, Items.silicon, 3, Items.sand, 14));
                this.requirements(Category.crafting, ItemStack.with(Items.metaglass, 200, Items.titanium, 90, Items.phaseFabric, 80, Items.silicon, 200, ModItems.graphenite, 180));
                this.outputItem = new ItemStack(Items.phaseFabric, 5);
            }
        };
        magmaMixer = new GenericCrafter("magma-mixer") {
            {
                this.localizedName = "Magma Mixer";
                this.description = "Makes hot Magma liquid from Copper and Slag.";
                this.size = 2;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.craftTime = 70;
                this.updateEffect = Fx.steam;
                this.consumes.power(6f);
                this.consumes.liquid(Liquids.slag, 0.3f);
                this.consumes.items(ItemStack.with(Items.copper, 8));
                this.requirements(Category.crafting, ItemStack.with(Items.lead, 150, ModItems.chromium, 200, Items.silicon, 90, Items.metaglass, 100, ModItems.graphenite, 85));
                this.outputLiquid = new LiquidStack(ModLiquids.magma, 26f);
            }
        };
        grapheniteKiln = new MultiGenericSmelter("graphenite-kiln") {
            {
                this.topPoints = Seq.with(
                        new Vec3(1.25f, 1.25f, 0.9f),
                        new Vec3(1.25f, 4.75f, 0.9f),
                        new Vec3(3.f, 3.f, 1.1f),
                        new Vec3(4.75f, 1.25f, 0.9f),
                        new Vec3(4.75f, 4.75f, 0.9f)
                );
                this.localizedName = "Graphenite Kiln";
                this.description = "Big version of legacy Graphenite forge, consumes more resources to produce more Graphenite.";
                this.health = 240;
                this.liquidCapacity = 20;
                this.size = 6;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(2.2f);
                this.consumes.items(ItemStack.with(
                        Items.graphite, 3,
                        Items.silicon, 2,
                        Items.titanium, 1,
                        Items.pyratite, 1
                ));
                this.consumes.liquid(Liquids.water,
                        0.1f);
                this.requirements(this.category, ItemStack.with(
                        Items.lead, 240,
                        ModItems.graphenite, 120,
                        Items.titanium, 50,
                        ModItems.odinum, 70,
                        Items.silicon, 130,
                        Items.graphite, 100
                ));
                this.category = Category.crafting;
                this.outputItem = new ItemStack(ModItems.graphenite, 4);
            }
        };
        odinumExtractor = new GenericSmelter("odinum-extractor") {
            {
                this.localizedName = "Odinum Extractor";
                this.description = "This forge extracts Odinum from Plastanium, Titanium and Thorium.";
                this.health = 250;
                this.liquidCapacity = 0;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.craftTime = 100;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(3f);
                this.consumes.items(ItemStack.with(Items.plastanium, 1, Items.thorium, 3, Items.titanium, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 60, Items.titanium, 100, Items.metaglass, 50, Items.silicon, 150, Items.graphite, 80));
                this.outputItem = new ItemStack(ModItems.odinum, 2);
            }
        };
        phaseAlloySmelter = new GenericSmelter("phase-alloy-smelter") {
            {
                this.localizedName = "Dense Composite Smelter";
                this.description = "Produces universal Dense Composite from Plastanium, Surge alloy and Phase fabric.";
                this.health = 310;
                this.liquidCapacity = 0;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(3f);
                this.consumes.items(ItemStack.with(Items.plastanium, 1, Items.surgeAlloy, 1, Items.phaseFabric, 2));
                this.requirements(Category.crafting, ItemStack.with(Items.phaseFabric, 100, Items.plastanium, 100, Items.thorium, 400, ModItems.exoticAlloy, 270, ModItems.graphenite, 380));
                this.outputItem = new ItemStack(ModItems.phaseAlloy, 2);
            }
        };
        plasticForge = new GenericSmelter("plastic-forge") {
            {
                this.localizedName = "Plastic Forge";
                this.description = "Consumes Plastanium, Oil and Thorium to produce ultra-light Plastic .";
                this.health = 330;
                this.liquidCapacity = 15;
                this.size = 4;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.craftTime = 100;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(2f);
                this.consumes.liquid(Liquids.oil, 0.1f);
                this.consumes.items(ItemStack.with(Items.plastanium, 1, Items.thorium, 4));
                this.requirements(Category.crafting, ItemStack.with(Items.phaseFabric, 100, Items.plastanium, 200, Items.thorium, 300, Items.silicon, 470, ModItems.exoticAlloy, 130, ModItems.graphenite, 380));
                this.outputItem = new ItemStack(ModItems.plastic, 2);
            }
        };
        refrigerantmixer = new GenericSmelter("refrigerantmixer") {
            {
                this.localizedName = "Thorium Refrigerant Mixer";
                this.description = "Makes cool Refrigirant from Cryofluid and crushed Thorium.";
                this.health = 130;
                this.size = 2;
                this.consumes.power(3f);
                this.consumes.liquid(Liquids.cryofluid, 0.1f);
                this.consumes.items(ItemStack.with(Items.thorium, 2));
                this.outputLiquid = new LiquidStack(ModLiquids.thoriumRefrigerant, 16f);
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 200, Items.thorium, 200, Items.titanium, 100, Items.metaglass, 130, ModItems.graphenite, 190));
                this.updateEffect = Fx.purify;
                this.updateEffectChance = 0.02f;
            }
        };
        methaneGasifier = new GasGenericCrafter("methane-gasifier"){
            {
                this.localizedName = "Methane Extractor";
                this.description = "Extract Methane from Oil";
                this.health = 140;
                this.size = 2;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasGas = true;
                this.hasItems = true;
                this.consumes.power(3f);
                this.consumes.liquid(Liquids.oil, 0.3f);
                this.outputGas = new GasStack(Gasses.methane, 1);
                this.requirements(Category.crafting, ItemStack.with( ModItems.odinum, 100, Items.titanium, 100, Items.metaglass, 130, ModItems.chromium, 190));
                this.updateEffect = Fx.purify;
                this.updateEffectChance = 0.02f;
            }
        };
        hyperDenseCompoiteSmelter = new GasGenericCrafter("hyper-dense-composite-smelter") {
            {
                this.localizedName = "Hyper Dense Composite Smelter";
                this.description = "Upgraded version of Dense Composite smelter but consumes gas for more effective production.";
                this.health = 510;
                this.liquidCapacity = 20;
                this.size = 6;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasGas = true;
                this.hasItems = true;
                this.craftTime = 100;
                this.updateEffect = Fx.fuelburn;
                this.consumes.power(5f);
                this.consumes.items(ItemStack.with(Items.plastanium, 3, Items.surgeAlloy, 2, Items.phaseFabric, 2));
                this.consumes.addGas(new ConsumeGasses(Gasses.methane, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.phaseFabric, 100, Items.plastanium, 100, Items.thorium, 400, ModItems.exoticAlloy, 270, ModItems.graphenite, 380));
                this.outputItem = new ItemStack(ModItems.phaseAlloy, 5);
            }
        };
        methaneLiquifier = new GasGenericCrafter("methane-liquifier") {
            {
                this.localizedName = "Methane Liquifier";
                this.description = "Turn Methane into Liquid.";
                this.health = 150;
                this.liquidCapacity = 20;
                this.itemCapacity = 25;
                this.gasCapacity = 15;
                this.size = 2;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasGas = true;
                this.hasItems = true;
                this.craftTime = 100;
                this.updateEffect = ModFx.contritumUpdate;
                this.consumes.power(3f);
                this.consumes.liquid(Liquids.water, 0.1f);
                this.consumes.addGas(new ConsumeGasses(Gasses.methane, 1));
                this.requirements(Category.crafting, ItemStack.with(ModItems.chromium, 150, Items.plastanium, 100, Items.metaglass, 150, ModItems.graphenite, 200));
                this.outputLiquid = new LiquidStack(ModLiquids.liquidMethane, 1f);
            }
        };
    }
}
