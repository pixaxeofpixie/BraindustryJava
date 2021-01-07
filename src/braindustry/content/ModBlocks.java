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

            armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor, surgePayloadConveyor,


    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum, differentialMagmaGenerator,
            grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, phaseAlloySolarPanel, phaseTower,


    refrigerantReactor, chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer,


    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor,


    axon, heartbeat, blaze, brain, electron, fragment, impulse, katana, mind, neuron, perlin, soul, stinger, synaps, gloryTurret,


    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge, plasticWall,


    smartRouter, turretSwitcher, dpsMeter, unitGenerator,unitNode, multiCrafter,unitSpawner;

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
                this.update = true;
                this.destructible = true;
                this.hasLiquids=true;
                this.hasShadow = false;

                this.health = 360;
                this.category = Category.units;
                dynamicItem = true;
                dynamicLiquid=true;
//                this.changeTexture = true;
                recipes(Recipe.with(new ItemStack(Items.copper, 1), ModLiquidStack.with(Liquids.water, 1, Liquids.slag, 1),120),
                        Recipe.with(new ItemStack(Items.lead, 1),ItemStack.with(Items.copper,2), ModLiquidStack.with(Liquids.water, 3, Liquids.slag, 3), 120),
                        Recipe.with(new ItemStack(Items.titanium, 1), ModLiquidStack.with(Liquids.water, 20, Liquids.slag, 20), 120),
                        Recipe.with(new ItemStack(Items.thorium, 1), ModLiquidStack.with(Liquids.water, 20, Liquids.slag, 20, Liquids.oil, 20, Liquids.cryofluid, 20), 120)
                );
                this.requirements(this.category, ItemStack.with(Items.lead, 12));
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
