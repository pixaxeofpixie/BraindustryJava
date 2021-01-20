package braindustry.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import braindustry.content.Blocks.ModUnitsBlocks;
import braindustry.content.Blocks.ModDefense;
import braindustry.content.Blocks.ModOtherBlocks;
import braindustry.content.Blocks.ModProduction;
import braindustry.type.Recipe;
import braindustry.world.blocks.TestBlock;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.production.MultiCrafter;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import braindustry.world.blocks.sandbox.DpsMeter;
import braindustry.world.blocks.sandbox.UnitSpawner;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
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
import mindustryAddition.type.ModLiquidStack;
import mindustryAddition.world.blocks.distribution.CrossBufferedItemBridge;
import mindustryAddition.world.blocks.distribution.CrossItemBridge;

public class ModBlocks implements ContentList {
    public static Block

//transportation
            armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor, surgePayloadConveyor,

    //environment and power
    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum, differentialMagmaGenerator,
            grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, blackHoleReactor, phaseAlloySolarPanel, phaseTower, crimzesWall, crimzesFloor, jungleWall, jungleFloor, jungleWater, dirtRocksWall,

    //production
    refrigerantReactor, chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer, astroSmelter,

    //units
    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor,

    //turrets
    axon, heartbeat, blaze, brain, electron, fragment, impulse, katana, mind, neuron, perlin, soul, stinger, synaps, gem,

    //walls
    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge,
            plasticWall, astronomicalWall, largeAstronomicalWall,

    //experimental
    smartRouter, turretSwitcher,blockHealer, dpsMeter, unitGenerator,unitNode, multiCrafter, largeMultiCrafter, unitSpawner,
            exampleCrossItemBridge, exampleCrossPhaseBridge,testBlock;


    public void load() {
        new ModUnitsBlocks().load();
        new ModProduction().load();
        new ModOtherBlocks().load();
        new ModDefense().load();
        testBlock=new TestBlock("test-block"){
            {
                this.size = 2;
                this.requirements(Category.logic, ItemStack.with(), true);
            }
        };
        exampleCrossPhaseBridge =new CrossItemBridge("cross-phase-conveyor"){
            {
                this.requirements(Category.distribution, ItemStack.with(Items.phaseFabric, 5, Items.silicon, 7, Items.lead, 10, Items.graphite, 10));
                this.range = 12;
                this.canOverdrive = false;
                this.hasPower = true;
                this.consumes.power(0.3F);
                /** custom connect filter*/
                connectFilter=(build)->{
                    Block block=build.block;
                    return block.acceptsItems || block instanceof StorageBlock;
                };
                //end of block
                ItemBridge bridge=(ItemBridge)Blocks.phaseConveyor;
                range=bridge.range;
            }
            @Override
            public void load() {
                ItemBridge bridge=(ItemBridge)Blocks.phaseConveyor;
                Core.atlas.addRegion(this.name,Core.atlas.find(bridge.name));
                super.load();
                this.arrowRegion=bridge.arrowRegion;
                this.bridgeRegion=bridge.bridgeRegion;
                this.endRegion=bridge.endRegion;
                this.region=bridge.region;
            }
        };
        exampleCrossItemBridge =new CrossBufferedItemBridge("cross-item-bridge"){
            {
                this.requirements(Category.distribution, ItemStack.with(Items.lead, 6, Items.copper, 6));
                this.range = 4;
                this.speed = 74.0F;
                this.bufferCapacity = 14;
                /** custom connect filter*/
                connectFilter=(build)->{
                    Block block=build.block;
                    return block.acceptsItems || block instanceof StorageBlock;
                };
                /** default filter check blocks from connectBlocksGetter*/
                connectBlocksGetter=()->{
                    return Seq.with(Blocks.titaniumConveyor);
                };
                //end of block
                BufferedItemBridge bridge=(BufferedItemBridge)Blocks.itemBridge;
                range=bridge.range;
            }

            @Override
            public void load() {
                BufferedItemBridge bridge=(BufferedItemBridge)Blocks.itemBridge;
                Core.atlas.addRegion(this.name,Core.atlas.find(bridge.name));
                super.load();
                this.arrowRegion=bridge.arrowRegion;
                this.bridgeRegion=bridge.bridgeRegion;
                this.endRegion=bridge.endRegion;
                this.region=bridge.region;
            }
        };
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
                this.hasShadow = true;

                this.health = 360;
                this.category = Category.production;
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
        largeMultiCrafter = new MultiCrafter("large-multi-crafter") {
            {
                this.size = 6;
                this.localizedName = "Multi Kiln";
                this.update = true;
                this.destructible = true;
                this.hasLiquids = true;
                this.hasShadow = true;

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
                /** custom block filter*/
                blockFilter=(build)->{
                    return build.block instanceof Turret;
                };
                colorFunc=(b)-> Color.orange.cpy().lerp(Pal.accent,0.1f);
                /** default action*/
                action =(b)-> {
                    Turret.TurretBuild build =(Turret.TurretBuild)b;
                    boolean enable= (build instanceof ControlBlock && ((ControlBlock) build).isControlled());
                    build.control(LAccess.enabled, enable ? 1 : 0, 0, 0, 0);
                    build.enabledControlTime = 30.0F;
                    if (enable){
                        build.enabledControlTime=0f;
                        build.charging=false;
                    }
                };
                this.size = 2;
                this.laserRange = 6.0F;
                this.health=10000;
                this.requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(Items.copper, 3, Items.silicon, 10));
                this.buildCostMultiplier = 4.0F;
            }
        };
        blockHealer = new BlockSwitcher("block-healer") {
            {
                blockFilter=(build)->{
                    return true;
                };
                action =(build)-> {
                    build.heal(build.delta()*build.maxHealth);
                };
                colorFunc=(b)-> {
                    float t=Mathf.absin(Time.time + Mathf.randomSeed(b.id, 0, 1000000), 1f, 1F)* 0.9f+0.1f;
                   return Pal.heal.cpy().lerp(Color.black, t * (1f - b.healthf()));
                };
                this.size = 2;
                this.health=10000;
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
