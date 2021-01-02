package braindustry.content;

import braindustry.content.Blocks.ModBlocksUnits;
import braindustry.content.Blocks.ModDefense;
import braindustry.content.Blocks.ModOtherBlocks;
import braindustry.content.Blocks.ModProduction;
import braindustry.world.blocks.distribution.SmartRouter;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

public class ModBlocks implements ContentList {
    public static Block smartRouter;
    public static Block armoredPlastaniumConveyor;
    public static Block chromiumConduit;
    public static Block phaseAlloyConveyor;
    public static Block plasticConveyor;
    public static Block surgeConveyor;
    public static Block magmaFloor;
    public static Block obsidianBlock;
    public static Block obsidianFloor;
    public static Block oreChromium;
    public static Block oreOdinum;
    public static Block differentialMagmaGenerator;
    public static Block grapheniteSolarCollectorLarge;
    public static Block magmaGenerator;
    public static Block odinumReactor;
    public static Block phaseAlloySolarPanel;
    public static Block phaseTower;
    public static Block refrigerantReactor;
    public static Block chromiumForge;
    public static Block exoticAlloySmelter;
    public static Block grapheniteFluidizer;
    public static Block grapheniteForge;
    public static Block hydraulicDrill;
    public static Block hyperAlloySmelter;
    public static Block hyperPhaseWeaver;
    public static Block magmaMixer;
    public static Block odinumExtractor;
    public static Block phaseAlloySmelter;
    public static Block plasticForge;
    public static Block quarryDrill;
    public static Block geothermicDrill;
    public static Block grapheniteKiln;
    public static Block refrigerantmixer;
    public static Block hyperAdditiveReconstructor;
    public static Block hyperAirFactory;
    public static Block hyperExponentialReconstructor;
    public static Block hyperGroundFactory;
    public static Block hyperMultiplicativeReconstructor;
    public static Block hyperNavalFactory;
    public static Block hyperTetrativeReconstructor;
    public static Block axon,Heartbeat;
    public static Block blaze;
    public static Block brain;
    public static Block electron;
    public static Block fragment;
    public static Block impulse;
    public static Block katana;
    public static Block mind;
    public static Block neuron;
    public static Block perlin;
    public static Block soul;
    public static Block stinger;
    public static Block synaps;
    public static Block exoticAlloyWallLarge;
    public static Block exoticAlloyWall;
    public static Block grapheniteWallLarge;
    public static Block grapheniteWall;
    public static Block odinumWallLarge;
    public static Block odinumWall;
    public static Block plasticWallLarge;
    public static Block plasticWall;
    public static Block turretSwitcher;

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

    }
}
