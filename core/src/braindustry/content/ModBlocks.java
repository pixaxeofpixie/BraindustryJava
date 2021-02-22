package braindustry.content;

import Gas.world.blocks.distribution.GasRouter;
import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import braindustry.content.Blocks.*;
import braindustry.world.blocks.TestBlock;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import braindustry.world.blocks.sandbox.DpsMeter;
import braindustry.world.blocks.sandbox.UnitSpawner;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BuildVisibility;
import mindustryAddition.world.blocks.distribution.CrossBufferedItemBridge;
import mindustryAddition.world.blocks.distribution.CrossItemBridge;

public class ModBlocks implements ContentList {
    public static Block

//transportation
            armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor, surgePayloadConveyor,

    //environment
    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum,
            crimzesWall, crimzesFloor, jungleWall, jungleFloor, graysand, dirtRocksWall, liquidMethaneFloor, blackIce, blackIceWall,blackSnow,blackSnowWall,blackTree,darkPine,darkShrubs,greenTree,metallicPine,swampWater,swampSandWater,

    //power
    differentialMagmaGenerator, grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, blackHoleReactor, phaseAlloySolarPanel,
            phaseTower,materialReactor,

    //production
    refrigerantReactor, chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer, methaneGasifier,

    //units
    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor, ultraReconstructor,

    //turrets
    axon, blaze, brain, electron, fragment, impulse, katana, mind, neuron, perlin, soul, stinger, synaps, gem, rapier, shinigami,

    //walls
    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge,
            plasticWall, chloroWall, largeChloroWall,
    //gas
    gasTank,

    //logic
    advancedSwitcher,

    //sandbox
    payloadSource,payloadVoid,

    //experimental
    smartRouter, turretSwitcher, blockHealer, dpsMeter, unitGenerator, unitNode, multiCrafter, largeMultiCrafter, unitSpawner,
            exampleCrossItemBridge, exampleCrossPhaseBridge, testBlock;


    public void load() {
        new ModUnitsBlocks().load();
        new ModProduction().load();
        new ModPowerBlocks().load();
        new ModOtherBlocks().load();
        new ModDefense().load();
        new ModLogicBlocks().load();
        new ModSandBox().load();
        testBlock = new TestBlock("test-block") {
            {
                this.size = 2;
                this.requirements(Category.logic, ItemStack.with(), true);
            }
        };
       exampleCrossPhaseBridge = new CrossItemBridge("example-cross-phase-bridge-conveyor") {
            {
                this.localizedName = "Phase Alloy Conveyor";
                this.requirements(Category.distribution, ItemStack.with(ModItems.phaseAlloy, 5, Items.silicon, 7, Items.lead, 10, Items.graphite, 10));
                this.range = 12;
                this.canOverdrive = false;
                this.hasPower = true;
                this.consumes.power(0.3F);
              //   custom connect filter
                connectFilter = (build) -> {
                    Block block = build.block;
                    return block.acceptsItems || block instanceof StorageBlock;
                };
                //end of block
                ItemBridge bridge = (ItemBridge) Blocks.phaseConveyor;
                range = bridge.range;
            }

            @Override
            public void load() {
                ItemBridge bridge = (ItemBridge) Blocks.phaseConveyor;
                Core.atlas.addRegion(this.name, Core.atlas.find(bridge.name));
                super.load();
                this.arrowRegion = bridge.arrowRegion;
                this.bridgeRegion = bridge.bridgeRegion;
                this.endRegion = bridge.endRegion;
                this.region = bridge.region;
            }
        };
        exampleCrossItemBridge = new CrossBufferedItemBridge("example-cross-bridge-conveyor") {
            {
                this.requirements(Category.distribution, ItemStack.with(Items.lead, 6, Items.copper, 6));
                this.range = 4;
                this.speed = 74.0F;
                this.bufferCapacity = 14;
                //* custom connect filter
                connectFilter = (build) -> {
                    Block block = build.block;
                    return block.acceptsItems || block instanceof StorageBlock;
                };
                // default filter check blocks from connectBlocksGetter
                connectBlocksGetter = () -> {
                    return Seq.with(Blocks.titaniumConveyor);
                };
                //end of block
                BufferedItemBridge bridge = (BufferedItemBridge) Blocks.itemBridge;
                range = bridge.range;
            }

            @Override
            public void load() {
                BufferedItemBridge bridge = (BufferedItemBridge) Blocks.itemBridge;
                Core.atlas.addRegion(this.name, Core.atlas.find(bridge.name));
                super.load();
                this.arrowRegion = bridge.arrowRegion;
                this.bridgeRegion = bridge.bridgeRegion;
                this.endRegion = bridge.endRegion;
                this.region = bridge.region;
            }
        };
        unitSpawner = new UnitSpawner("unit-spawner") {
            {
                localizedName = "Unit Spawner";
                description = "Powerful sandbox block, can spawn and control any unit from game and mods.";
                size = 2;

                requirements(Category.effect,BuildVisibility.sandboxOnly, ItemStack.empty);
            }
        };
        unitGenerator = new UnitPowerGenerator("unit-generator") {
            {
                powerProduction = 10f;
                buildVisibility=BuildVisibility.debugOnly;
            }
        };
        unitNode = new UnitPowerNode("unit-node") {
            {
                maxNodes = Integer.MAX_VALUE;
                buildVisibility=BuildVisibility.debugOnly;
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
                requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                buildCostMultiplier = 4.0F;
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
                requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                buildCostMultiplier = 4.0F;
            }
        };
        dpsMeter = new DpsMeter("dps-meter") {
            {
                category = Category.effect;
//                alwaysUnlocked=true;
                buildVisibility = BuildVisibility.sandboxOnly;
                health = Integer.MAX_VALUE;
                size = 3;
//                requirements();
            }
        };

        gasTank = new GasRouter("gas-tank") {
            {
                localizedName = "Gas Tank";
                description = "Storage gas";
                size = 3;
                gasCapacity = 1500f;
                health = 500;
                requirements(Category.liquid, ItemStack.with(Items.titanium, 25, Items.metaglass, 25));
            }
        };
    }
}
