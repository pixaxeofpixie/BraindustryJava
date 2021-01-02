package braindustry.ModContent.content.Blocks;

import braindustry.ModContent.content.ModItems;
import braindustry.ModContent.content.ModLiquids;
import braindustry.ModContent.world.blocks.production.MultiGenericSmelter;
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

import static braindustry.ModContent.content.ModBlocks.*;

public class ModProduction implements ContentList {
    @Override
    public void load() {
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
                this.consumes.items(ItemStack.with(Items.metaglass, 2, Items.titanium, 2));
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
                this.consumes.items(ItemStack.with(Items.sporePod, 1, Items.thorium, 2, Items.titanium, 1));
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
                this.consumes.liquid(Liquids.water, 0.1f);
                this.consumes.items(ItemStack.with(ModItems.graphenite, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.lead, 50, Items.thorium, 80, Items.silicon, 70, Items.titanium, 50, ModItems.graphenite, 85));
                this.outputLiquid = new LiquidStack(ModLiquids.liquidGraphenite, 32f);
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
                this.consumes.items(ItemStack.with(Items.graphite, 1, Items.silicon, 1, Items.titanium, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.lead, 100, Items.titanium, 30, Items.thorium, 40, Items.silicon, 70, Items.graphite, 80));
                this.outputItem = new ItemStack(ModItems.graphenite, 1);
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
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(1.2f);
                this.consumes.items(ItemStack.with(Items.thorium, 3, Items.silicon, 4, Items.sand, 9));
                this.requirements(Category.crafting, ItemStack.with(Items.metaglass, 200, Items.titanium, 90, Items.phaseFabric, 80, Items.silicon, 200, ModItems.graphenite, 180));
                this.outputItem = new ItemStack(Items.phaseFabric, 3);
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
                this.consumes.liquid(Liquids.slag, 0.1f);
                this.consumes.items(ItemStack.with(Items.copper, 4));
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
                        Items.silicon, 1,
                        Items.titanium, 1,
                        Items.blastCompound, 1
                ));
                this.consumes.liquid(Liquids.oil,
                        0.04f);
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
                this.consumes.items(ItemStack.with(Items.plastanium, 1, Items.thorium, 1, Items.titanium, 1));
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 60, Items.titanium, 100, Items.metaglass, 50, Items.silicon, 150, Items.graphite, 80));
                this.outputItem = new ItemStack(ModItems.odinum, 1);
            }
        };
        phaseAlloySmelter = new GenericSmelter("phase-alloy-smelter") {
            {
                this.localizedName = "Phase Alloy Smelter";
                this.description = "Produces universal Phase alloy from Plastanium, Surge alloy and Phase fabric.";
                this.health = 310;
                this.liquidCapacity = 0;
                this.size = 3;
                this.hasPower = true;
                this.hasLiquids = false;
                this.hasItems = true;
                this.craftTime = 90;
                this.updateEffect = Fx.plasticburn;
                this.consumes.power(2f);
                this.consumes.items(ItemStack.with(Items.plastanium, 2, Items.surgeAlloy, 1, Items.phaseFabric, 1));
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
                this.consumes.items(ItemStack.with(Items.thorium, 1));
                this.outputLiquid = new LiquidStack(ModLiquids.thoriumRefrigerant, 16f);
                this.requirements(Category.crafting, ItemStack.with(Items.plastanium, 200, Items.thorium, 200, Items.titanium, 100, Items.metaglass, 130, ModItems.graphenite, 190));
                this.updateEffect = Fx.purify;
                this.updateEffectChance = 0.02f;
            }
        };
    }
}
