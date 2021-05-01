package braindustry.content.Blocks;

import Gas.world.blocks.distribution.GasRouter;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import braindustry.world.blocks.TestBlock;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import braindustry.world.blocks.distribution.BufferedPayloadBridge;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.power.ReceivingPowerNode;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import braindustry.world.blocks.sandbox.DpsMeter;
import braindustry.world.blocks.sandbox.UnitSpawner;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class ModBlocks implements ContentList {
    public static Block

//transportation
            armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor,

    //environment
    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum,
            crimzesWall, crimzesFloor, jungleWall, jungleFloor, graysand, dirtRocksWall, liquidMethaneFloor, blackIce,
            blackIceWall, blackSnow, blackSnowWall, blackTree, darkPine, darkShrubs, greenTree, metallicPine, swampWater, swampSandWater, darkShrubsFloor,

    //power
    differentialMagmaGenerator, grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, blackHoleReactor, phaseAlloySolarPanel,
            phaseTower, materialReactor,

    //production
    refrigerantReactor, chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer, methaneGasifier, methaneLiquifier,
            hyperDenseCompositeSmelter, hyperExoAlloySmelter,

    //units
    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor, ultraReconstructor,

    //turrets
    axon, blaze, brain, electron, fragment, impulse, katana, mind, neuron,
    perlin, soul, stinger, synaps, gem, rapier, shinigami, voidwave, spark, archer,

    //walls
    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge,
            plasticWall, chloroWall, largeChloroWall,
    //gas
    gasTank, gasRouter,

    //logic
    advancedSwitcher,

    //sandbox
    payloadSource, payloadVoid,

    //experimental
    smartRouter, turretSwitcher, blockHealer, dpsMeter, unitGenerator, unitNode, multiCrafter, largeMultiCrafter, unitSpawner,
            examplePayloadBridge, testBlock, node1, node2;

    public static Block methaneBurner, hyperMethaneBurner;
    private ContentList[] blocksContent = {
            new ModEnvironmentBlocks(),
            new ModUnitsBlocks(),
            new ModProduction(),
            new ModPowerBlocks(),
            new ModOtherBlocks(),
            new ModDefense(),
            new ModLogicBlocks(),
            new ModSandBox(),
    };

    public void load() {
        for (ContentList contentList : blocksContent) {
            contentList.load();
        }
        examplePayloadBridge = new BufferedPayloadBridge("payload-bridge-conveyor") {{
            range = 10;

            requirements(Category.distribution, with(Items.graphite, 10, Items.copper, 20));
            size = 3;
            canOverdrive = false;
            buildVisibility = BuildVisibility.debugOnly;
        }};
        testBlock = new TestBlock("test-block") {{
            this.size = 2;
            this.requirements(Category.logic, ItemStack.with(), true);
        }};
        node1 = new ReceivingPowerNode("unit-power-node") {{
            size = 3;
            this.requirements(Category.logic, ItemStack.with(), true);
            buildVisibility = BuildVisibility.debugOnly;
        }};
        node2 = new ReceivingPowerNode("unit-power-node2") {{
            size = 3;
            this.requirements(Category.logic, ItemStack.with(), true);
            buildVisibility = BuildVisibility.debugOnly;
        }};
        
        unitSpawner = new UnitSpawner("unit-spawner") {{
            localizedName = "Unit Spawner";
            description = "Powerful sandbox block, can spawn and control any unit from game and mods.";
            size = 2;

            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.empty);
        }};
        unitGenerator = new UnitPowerGenerator("unit-generator") {
            {
                powerProduction = 10f;
                buildVisibility = BuildVisibility.debugOnly;
            }
        };
        unitNode = new UnitPowerNode("unit-node") {
            {
                maxNodes = Integer.MAX_VALUE;
                buildVisibility = BuildVisibility.debugOnly;
            }
        };

        smartRouter = new SmartRouter("smart-router") {
            {
                localizedName = "Smart Router";
                size = 1;
                requirements(Category.distribution, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                buildCostMultiplier = 4.0F;
            }
        };
        turretSwitcher = new BlockSwitcher("turret-switcher") {
            {
                /** custom block filter*/
                blockFilter = (build) -> {
                    return build.block instanceof Turret;
                };
                colorFunc = (b) -> Color.orange.cpy().lerp(Pal.accent, 0.1f);
                /** default action*/
                action = (b) -> {
                    Turret.TurretBuild build = (Turret.TurretBuild) b;
                    boolean enable = (build instanceof ControlBlock && ((ControlBlock) build).isControlled());
                    build.control(LAccess.enabled, enable ? 1 : 0, 0, 0, 0);
                    build.enabledControlTime = 30.0F;
                    if (enable) {
                        build.enabledControlTime = 0f;
                        build.charging = false;
                    }
                };
                size = 2;
                laserRange = 6.0F;
                health = 10000;
                requirements(Category.distribution, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                buildCostMultiplier = 4.0F;
                buildVisibility = BuildVisibility.debugOnly;
            }
        };
        blockHealer = new BlockSwitcher("block-healer") {
            {
                blockFilter = (build) -> {
                    return true;
                };
                action = (build) -> {
                    build.heal(build.delta() * build.maxHealth);
                };
                colorFunc = (b) -> {
                    float t = Mathf.absin(Time.time + Mathf.randomSeed(b.id, 0, 1000000), 1f, 1F) * 0.9f + 0.1f;
                    return Pal.heal.cpy().lerp(Color.black, t * (1f - b.healthf()));
                };
                size = 2;
                health = 10000;
                laserRange = 6.0F;
                requirements(Category.distribution,  ItemStack.with(Items.copper, 3, Items.silicon, 10));
                buildCostMultiplier = 4.0F;
                buildVisibility = BuildVisibility.debugOnly;
            }
        };
        dpsMeter = new DpsMeter("dps-meter") {
            {
                category = Category.effect;
                buildVisibility = BuildVisibility.debugOnly;
                health = Integer.MAX_VALUE;
                size = 3;
            }
        };
    }
}
