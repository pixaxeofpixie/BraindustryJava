package braindustry.content;

import braindustry.content.Blocks.ModBlocksUnits;
import braindustry.content.Blocks.ModDefense;
import braindustry.content.Blocks.ModOtherBlocks;
import braindustry.content.Blocks.ModProduction;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class ModBlocks implements ContentList {
    public static Block


    armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor,


    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum, differentialMagmaGenerator,
            grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, phaseAlloySolarPanel, phaseTower,


    refrigerantReactor, chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer,


    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor,


    axon, heartbeat, blaze, brain, electron, fragment, impulse, katana, mind, neuron, perlin, soul, stinger, synaps,


    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge, plasticWall,


    smartRouter, turretSwitcher,unitGenerator;

    public void load() {
        new ModBlocksUnits().load();
        new ModProduction().load();
        new ModOtherBlocks().load();
        new ModDefense().load();
        unitGenerator=new UnitPowerGenerator("unit-generator"){
            {
                this.powerProduction=10f;
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

    }
}
