package braindustry.content;

import braindustry.content.Blocks.ModUnitsBlocks;
import braindustry.content.Blocks.ModDefense;
import braindustry.content.Blocks.ModOtherBlocks;
import braindustry.content.Blocks.ModProduction;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.production.MultiCrafter;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import braindustry.world.blocks.sandbox.DpsMeter;
import braindustry.world.blocks.sandbox.UnitSpawner;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;
import mindustryAddition.type.ModLiquidStack;

public class ModBlocks implements ContentList {
    public static Block

//transportation
            armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor, surgePayloadConveyor,

    //environment and power
    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum, differentialMagmaGenerator,
            grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, blackHoleReactor, phaseAlloySolarPanel, phaseTower, crimzesBlock, crimzesFloor, jungleWall, jungleFloor, jungleWater, dirtRocksWall,

    //production
    refrigerantReactor, chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer, astroSmelter,

    //units
    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor,

    //turrets
    axon, heartbeat, blaze, brain, electron, fragment, impulse, katana, mind, neuron, perlin, soul, stinger, synaps, gloryTurret,

    //walls
    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge,
            plasticWall, astronomicalWall, largeAstronomicalWall,

    //experimental
    smartRouter, turretSwitcher, dpsMeter, unitGenerator,unitNode, multiCrafter, largeMultiCrafter, unitSpawner;


    public void load() {
        new ModUnitsBlocks().load();
        new ModProduction().load();
        new ModOtherBlocks().load();
        new ModDefense().load();
        unitSpawner=new UnitSpawner("unit-spawner"){
            {
                this.size = 2;
                this.requirements(Category.effect, ItemStack.empty);
            }
        };
        multiCrafter = new MultiCrafter("multi-crafter") {
            {
                this.size = 3;
                this.localizedName = "Universal Smelter";
                this.update = true;
                this.destructible = true;
                this.hasLiquids = true;
                this.hasShadow = false;

                this.health = 360;
                this.category = Category.production;
                dynamicItem = true;
                dynamicLiquid=true;
//                this.changeTexture = true;
                //1st itemStack = выход, вход писать во втором itemStack
                recipes(Recipe.with(new ItemStack(ModItems.graphenite, 1),ItemStack.with(Items.graphite, 2, Items.silicon, 3, Items.titanium, 2, Items.lead, 2), ModLiquidStack.with(Liquids.slag, 1), 150),
                        Recipe.with(new ItemStack(ModItems.exoticAlloy, 1),ItemStack.with(Items.sporePod, 3, Items.titanium, 4, Items.thorium, 3), ModLiquidStack.with(Liquids.water, 2), 140)
                );
                this.requirements(this.category, ItemStack.with(Items.thorium, 200, ModItems.graphenite, 230, Items.lead, 500, Items.plastanium, 120));
//                addResearch(Blocks.mechanicalDrill, this);
            }
        };
        largeMultiCrafter = new MultiCrafter("large-multi-crafter") {
            {
                this.size = 6;
                this.localizedName = "Multi Kiln";
                this.update = true;
                this.destructible = true;
                this.hasLiquids = true;
                this.hasShadow = false;

                this.health = 980;
                this.category = Category.production;
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
        unitGenerator = new UnitPowerGenerator("unit-generator") {
            {
                this.powerProduction = 10f;
            }
        };
        unitNode = new UnitPowerNode("unit-node") {
            {
                this.maxNodes = Integer.MAX_VALUE;

            }
        };

        smartRouter = new SmartRouter("smart-router") {
            {
                this.size = 1;
                this.requirements(Category.distribution, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                this.buildCostMultiplier = 4.0F;
            }
        };
        turretSwitcher = new BlockSwitcher("turret-switcher") {
            {
                this.size = 2;
                this.laserRange = 6.0F;
                this.requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                this.buildCostMultiplier = 4.0F;
            }
        };
        dpsMeter = new DpsMeter("dps-meter") {
            {
                this.category = Category.effect;
//                this.alwaysUnlocked=true;
                this.buildVisibility = BuildVisibility.sandboxOnly;
                this.health = Integer.MAX_VALUE;
                this.size = 3;
//                this.requirements();
            }
        };


    }
}
