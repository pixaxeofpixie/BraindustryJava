package braindustry.content;

import braindustry.content.Blocks.ModBlocksUnits;
import braindustry.content.Blocks.ModDefense;
import braindustry.content.Blocks.ModOtherBlocks;
import braindustry.content.Blocks.ModProduction;
import braindustry.world.blocks.distribution.SmartRouter;
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


    axon, Heartbeat, blaze, brain, electron, fragment, impulse, katana, mind, neuron, perlin, soul, stinger, synaps,


    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge, plasticWall,


    smartRouter, turretSwitcher;

    public void load() {
        new ModBlocksUnits().load();
        new ModProduction().load();
        new ModOtherBlocks().load();
        new ModDefense().load();
        smartRouter = new SmartRouter("smart-router") {
            {
                this.size = 1;
                this.requirements(Category.distribution, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                this.buildCostMultiplier = 4.0F;
            }
        };
        turretSwitcher = new braindustry.world.blocks.sandbox.BlockSwitcher("turret-switcher") {
            {
                this.size = 2;
                this.laserRange = 6.0F;
                this.requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                this.buildCostMultiplier = 4.0F;
            }
        };

    }
}
