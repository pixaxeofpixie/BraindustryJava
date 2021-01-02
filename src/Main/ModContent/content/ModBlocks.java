package Main.ModContent.content;

import Main.ModContent.world.blocks.production.MultiGenericSmelter;
import Main.ModContent.world.blocks.production.MultiRotorDrill;
import Main.ModContent.world.blocks.distribution.SmartRouter;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.*;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LaserTurret;
import mindustry.world.blocks.defense.turrets.PointDefenseTurret;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.ConsumeLiquidFilter;

import static Main.modVars.modFunc.addResearch;
import static mindustry.content.Bullets.*;
import static mindustry.type.Category.turret;
import static mindustry.type.ItemStack.with;

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
    public static Block glory;
    public static Block exoticAlloyWallLarge;
    public static Block exoticAlloyWall;
    public static Block grapheniteWallLarge;
    public static Block grapheniteWall;
    public static Block odinumWallLarge;
    public static Block odinumWall;
    public static Block plasticWallLarge;
    public static Block plasticWall;

    public void load() {
//start----
//way content\blocks/distribution/armored-plastanium-conveyor.hjson
        armoredPlastaniumConveyor = new ArmoredConveyor("armored-plastanium-conveyor") {
            {
                this.localizedName = "Armored Plastanium Conveyor";
                this.description = "Wonderful and strong Plastanium Conveyor.";
                this.health = 240;
                this.hasItems = true;
                this.itemCapacity = 6;
                this.requirements(Category.distribution, with(Items.silicon, 2, ModItems.graphenite, 1, Items.metaglass, 1, Items.plastanium, 2));
                this.speed = 0.3f;
            }
        };
//----end
//start----
//way content\blocks/distribution/chromium-conduit.hjson
        chromiumConduit = new Conduit("chromium-conduit") {
            {
                this.localizedName = "Chromium Conduit";
                this.size = 1;
                this.description = "Heat resistant and fast conduit.";
                this.health = 140;
                this.liquidCapacity = 18;
                this.rotate = true;
                this.solid = false;
                this.floating = true;
                this.requirements(Category.liquid, with(ModItems.chromium, 2, Items.metaglass, 2, Items.thorium, 1));
            }
        };
//----end
//start----
//way content\blocks/distribution/phase-alloy-conveyor.hjson
        phaseAlloyConveyor = new ItemBridge("phase-alloy-conveyor") {
            {
                this.localizedName = "Phase Alloy Conveyor";
                this.description = "A powerful and fast conveyor that can transport items over several blocks at once.Consumes energy.";
                this.health = 70;
                this.size = 1;
                this.update = true;
                this.solid = true;
                this.range = 24;
                this.configurable = true;
                this.unloadable = false;
                this.expanded = true;
                this.hasItems = true;
                this.itemCapacity = 16;
                this.hasPower = true;
                this.requirements(Category.distribution, with(ModItems.phaseAlloy, 5, Items.silicon, 10, ModItems.graphenite, 8, Items.titanium, 10));
                this.consumes.power(0.4f);
            }
        };
//----end
//start----
//way content\blocks/distribution/plastic-conveyor.hjson
        plasticConveyor = new Conveyor("plastic-conveyor") {
            {
                this.localizedName = "Hermetic Plastic Conveyor";
                this.description = "The most fast and durable conveyor!";
                this.health = 168;
                this.requirements(Category.distribution, with(Items.silicon, 1, ModItems.graphenite, 1, ModItems.plastic, 1));
                this.speed = 0.45f;
            }
        };
//----end
//start----
//way content\blocks/distribution/surge-conveyor.hjson
        surgeConveyor = new StackConveyor("surge-conveyor") {
            {
                this.localizedName = "Surge Conveyor";
                this.requirements(Category.distribution, with(Items.surgeAlloy, 1, Items.silicon, 1, ModItems.graphenite, 1));
                this.health = 100;
                this.speed = 0.12f;
                this.itemCapacity = 12;
            }
        };
//----end
//start----
//way content\blocks/environment/magma-floor.hjson
        magmaFloor = new Floor("magma-floor") {
            {
                this.localizedName = "Magma";
                this.isLiquid = true;
                this.variants = 1;
                this.blendGroup = Blocks.water;
                this.cacheLayer = CacheLayer.tar;
                this.speedMultiplier = 0.17f;
                this.liquidDrop = ModLiquids.magma;
                this.statusDuration = 240;
                this.drownTime = 90;
                this.walkEffect = Fx.melting;
                this.drownUpdateEffect = Fx.burning;
            }
        };
//----end
//start----
//way content\blocks/environment/obsidian-block.hjson
        obsidianBlock = new StaticWall("obsidian-block") {
            {
                this.localizedName = "Obsidian Block";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
//----end
//start----
//way content\blocks/environment/obsidian-floor.hjson
        obsidianFloor = new Floor("obsidian-floor") {
            {
                this.localizedName = "Obsidian Floor";
                this.variants = 3;
            }
        };
//----end
//start----
//way content\blocks/environment/ore-chromium.hjson
        oreChromium = new OreBlock("ore-chromium") {
            {
                this.itemDrop = ModItems.chromium;
            }
        };
//----end
//start----
//way content\blocks/environment/ore-odinum.hjson
        oreOdinum = new OreBlock("ore-odinum") {
            {
                this.itemDrop = ModItems.odinum;
            }
        };
//----end
//start----
//way content\blocks/power/differential-magma-generator.hjson
        differentialMagmaGenerator = new SingleTypeGenerator("differential-magma-generator") {
            {
                this.localizedName = "Hyper Magma Generator";
                this.description = "Consumes Magma and Pyratite to produce much heat power.";
                this.size = 3;
                this.hasPower = true;
                this.health = 130;
                this.hasItems = true;
                this.itemCapacity = 30;
                this.hasLiquids = true;
                this.liquidCapacity = 40;
                this.itemDuration = 100;
                this.powerProduction = 20.6f;
                this.consumes.liquid(ModLiquids.magma, 0.2f);
                this.consumes.items(with(Items.pyratite, 3));
                this.requirements(Category.power, with(ModItems.graphenite, 140, ModItems.chromium, 190, Items.plastanium, 120, Items.titanium, 200, ModItems.odinum, 70));
            }
        };
//----end
//start----
//way content\blocks/power/graphenite-solar-collector-large.hjson
        grapheniteSolarCollectorLarge = new SolarGenerator("graphenite-solar-collector-large") {
            {
                this.localizedName = "Graphenite Solar Collector Large";
                this.description = "Solar panel with Graphenite elements, better than Large Solar Panel.";
                this.powerProduction = 1.6f;
                this.size = 3;
                this.health = 210;
                this.requirements(Category.power, with(Items.metaglass, 100, Items.silicon, 40, ModItems.graphenite, 70));
            }
        };
//----end
//start----
//way content\blocks/power/magma-generator.hjson
        magmaGenerator = new BurnerGenerator("magma-generator") {
            {
                this.localizedName = "Magma Generator";
                this.description = "Consumes Magma and Coal to produce a lot of power.";
                this.size = 2;
                this.hasPower = true;
                this.hasItems = true;
                this.itemCapacity = 10;
                this.hasLiquids = true;
                this.liquidCapacity = 15;
                this.itemDuration = 90;
                this.powerProduction = 8;
                this.consumes.liquid(ModLiquids.magma, 0.04f);
                this.consumes.items(with(Items.coal, 1));
                this.requirements(Category.power, with(ModItems.graphenite, 50, Items.silicon, 80, Items.plastanium, 120, ModItems.chromium, 160));
            }
        };
//----end
//start----
//way content\blocks/power/odinum-reactor.hjson
        odinumReactor = new ImpactReactor("odinum-reactor") {
            {
                this.localizedName = "Odinum Reactor";
                this.description = "Atomic fission and collision reactor.Consumes Odinum and Liquid Graphenite.";
                this.size = 4;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.itemCapacity = 20;
                this.liquidCapacity = 20;
                this.itemDuration = 90;
                this.powerProduction = 126;
                this.consumes.power(9f);
                this.consumes.liquid(ModLiquids.liquidGraphenite, 0.1f);
                this.consumes.items(with(ModItems.odinum, 1));
                this.requirements(Category.power, with(Items.metaglass, 500, ModItems.odinum, 300, Items.silicon, 400, ModItems.graphenite, 200, Items.plastanium, 200));
            }
        };
//----end
//start----
//way content\blocks/power/phase-alloy-solar-panel.hjson
        phaseAlloySolarPanel = new SolarGenerator("phase-alloy-solar-panel") {
            {
                this.localizedName = "Phase Alloy Solar Panel";
                this.description = "An improved version of the Large Solar Panel. Takes up a lot of space.";
                this.health = 960;
                this.size = 5;
                this.hasPower = true;
                this.powerProduction = 8.4f;
                this.requirements(Category.power, with(Items.thorium, 280, Items.silicon, 400, Items.titanium, 90, ModItems.graphenite, 80, ModItems.phaseAlloy, 115));
                this.buildCostMultiplier = 1.1f;
            }
        };
//----end
//start----
//way content\blocks/power/phase-tower.hjson
        phaseTower = new PowerNode("phase-tower") {
            {
                this.localizedName = "Phase Tower";
                this.health = 240;
                this.size = 4;
                this.maxNodes = 12;
                this.laserRange = 45;
                this.requirements(Category.power, with(ModItems.phaseAlloy, 15, Items.silicon, 20, Items.titanium, 10, Items.lead, 10));
            }
        };
//----end
//start----
//way content\blocks/power/refrigerant-reactor.hjson
        refrigerantReactor = new NuclearReactor("refrigerant-reactor") {
            {
                this.localizedName = "Refrigerant Reactor";
                this.description = "Upgraded Thorium Reactor. Now it uses the Refrigerant.";
                this.size = 4;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = true;
                this.itemCapacity = 10;
                this.liquidCapacity = 10;
                this.itemDuration = 120;
                this.powerProduction = 78;
                this.heating = 0.07f;
                this.smokeThreshold = 0.48f;
                this.explosionRadius = 90;
                this.explosionDamage = 1900;
                this.flashThreshold = 0.34f;
                this.coolantPower = 0.9f;
                this.consumes.power(3f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f);
                this.consumes.items(with(Items.thorium, 3));
                this.requirements(Category.power, with(Items.metaglass, 300, ModItems.odinum, 100, Items.silicon, 210, ModItems.graphenite, 270, Items.thorium, 200, Items.titanium, 140));
            }
        };
//----end
//start----
//way content\blocks/production/chromium-forge.hjson
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
                this.consumes.items(with(Items.metaglass, 2, Items.titanium, 2));
                this.requirements(Category.crafting, with(Items.plastanium, 80, Items.titanium, 100, Items.metaglass, 120, Items.silicon, 200, ModItems.graphenite, 200));
                this.outputItem = new ItemStack(ModItems.chromium, 1);
            }
        };
//----end
//start----
//way content\blocks/production/exotic-alloy-smelter.hjson
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
                this.consumes.items(with(Items.sporePod, 1, Items.thorium, 2, Items.titanium, 1));
                this.requirements(Category.crafting, with(Items.plastanium, 40, Items.lead, 100, Items.graphite, 80, ModItems.graphenite, 100, Items.metaglass, 80));
                this.outputItem = new ItemStack(ModItems.exoticAlloy, 2);
            }
        };
//----end
//start----
//way content\blocks/production/geothermic-drill.hjson
//----end
//start----
//way content\blocks/production/graphenite-fluidizer.hjson
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
                this.consumes.items(with(ModItems.graphenite, 1));
                this.requirements(Category.crafting, with(Items.lead, 50, Items.thorium, 80, Items.silicon, 70, Items.titanium, 50, ModItems.graphenite, 85));
                this.outputLiquid = new LiquidStack(ModLiquids.liquidGraphenite, 32f);
            }
        };
//----end
//start----
//way content\blocks/production/graphenite-forge.hjson
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
                this.consumes.items(with(Items.graphite, 1, Items.silicon, 1, Items.titanium, 1));
                this.requirements(Category.crafting, with(Items.lead, 100, Items.titanium, 30, Items.thorium, 40, Items.silicon, 70, Items.graphite, 80));
                this.outputItem = new ItemStack(ModItems.graphenite, 1);
            }
        };
//----end
//start----
//way content\blocks/production/graphenite-kiln.hjson
//----end
//start----
//way content\blocks/production/hydraulic-drill.hjson
        hydraulicDrill = new Drill("hydraulic-drill") {
            {
                this.localizedName = "Hydraulic Drill";
                this.description = "This drill uses hydraulics for more effective work. Consumes water.";
                this.size = 3;
                this.tier = 4;
                this.consumes.liquid(Liquids.water, 0.1f).optional(false, false);
                this.hasLiquids = true;
                this.rotateSpeed = 2;
                this.drillTime = 245;
                this.requirements(Category.production, with(Items.plastanium, 15, Items.silicon, 40, Items.graphite, 45, ModItems.odinum, 75));
                this.ambientSound = Sounds.drill;
                this.ambientSoundVolume = 0.01f;
            }
        };
//----end
//start----
//way content\blocks/production/hyper-alloy-smelter.hjson
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
                this.consumes.items(with(Items.copper, 4, Items.titanium, 4, Items.lead, 5, Items.silicon, 5));
                this.requirements(Category.crafting, with(Items.plastanium, 120, Items.titanium, 150, Items.metaglass, 100, Items.silicon, 300, ModItems.graphenite, 170, Items.surgeAlloy, 100));
                this.outputItem = new ItemStack(Items.surgeAlloy, 4);
            }
        };
//----end
//start----
//way content\blocks/production/hyper-phase-weaver.hjson
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
                this.consumes.items(with(Items.thorium, 3, Items.silicon, 4, Items.sand, 9));
                this.requirements(Category.crafting, with(Items.metaglass, 200, Items.titanium, 90, Items.phaseFabric, 80, Items.silicon, 200, ModItems.graphenite, 180));
                this.outputItem = new ItemStack(Items.phaseFabric, 3);
            }
        };
//----end
//start----
//way content\blocks/production/magma-mixer.hjson
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
                this.consumes.items(with(Items.copper, 4));
                this.requirements(Category.crafting, with(Items.lead, 150, ModItems.chromium, 200, Items.silicon, 90, Items.metaglass, 100, ModItems.graphenite, 85));
                this.outputLiquid = new LiquidStack(ModLiquids.magma, 26f);
            }
        };
//----end
//start----
//way content\blocks/production/odinum-extractor.hjson
        geothermicDrill = new MultiRotorDrill("geothermic-drill") {
            {
                this.requirements(Category.production, with(
                        ModItems.chromium, 290, Items.titanium, 400,
                        Items.silicon, 300, ModItems.graphenite, 575,
                        ModItems.odinum, 190, Items.plastanium, 120));
                this.size = 9;
                this.health = 1460;
                this.hasLiquids = true;
                this.liquidCapacity = 20;
                this.drawMineItem = false;
                this.tier = 8;
                rotatorPoints.add(new Vec2(2, 2), new Vec2(2, 6), new Vec2(6, 2), new Vec2(6, 6));
                this.rotateSpeed = 3.2f;
                this.consumes.power(5.8F);
                this.consumes.liquid(ModLiquids.magma, 0.2F);
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
                this.consumes.items(with(
                        Items.graphite, 3,
                        Items.silicon, 1,
                        Items.titanium, 1,
                        Items.blastCompound, 1
                ));
                this.consumes.liquid(Liquids.oil,
                        0.04f);
                this.requirements(this.category, with(
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
                this.consumes.items(with(Items.plastanium, 1, Items.thorium, 1, Items.titanium, 1));
                this.requirements(Category.crafting, with(Items.plastanium, 60, Items.titanium, 100, Items.metaglass, 50, Items.silicon, 150, Items.graphite, 80));
                this.outputItem = new ItemStack(ModItems.odinum, 1);
            }
        };
//----end
//start----
//way content\blocks/production/phase-alloy-smelter.hjson
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
                this.consumes.items(with(Items.plastanium, 2, Items.surgeAlloy, 1, Items.phaseFabric, 1));
                this.requirements(Category.crafting, with(Items.phaseFabric, 100, Items.plastanium, 100, Items.thorium, 400, ModItems.exoticAlloy, 270, ModItems.graphenite, 380));
                this.outputItem = new ItemStack(ModItems.phaseAlloy, 2);
            }
        };
//----end
//start----
//way content\blocks/production/plastic-forge.hjson
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
                this.consumes.items(with(Items.plastanium, 1, Items.thorium, 4));
                this.requirements(Category.crafting, with(Items.phaseFabric, 100, Items.plastanium, 200, Items.thorium, 300, Items.silicon, 470, ModItems.exoticAlloy, 130, ModItems.graphenite, 380));
                this.outputItem = new ItemStack(ModItems.plastic, 2);
            }
        };
//----end
//start----
//way content\blocks/production/quarry-drill.hjson
        quarryDrill = new Drill("quarry-drill") {
            {
                this.localizedName = "Quarry Drill";
                this.description = "A fastest, most efficient gigantic drill.";
                this.size = 5;
                this.health = 260;
                this.drillTime = 225;
                this.tier = 5;
                this.drawRim = true;
                this.consumes.power(2f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.12f).optional(false, false);
                this.hasLiquids = true;
                this.liquidCapacity = 60;
                this.rotateSpeed = 5;
                this.requirements(Category.production, with(ModItems.plastic, 150, ModItems.graphenite, 120, ModItems.odinum, 140, Items.plastanium, 100));
            }
        };
//----end
//start----
//way content\blocks/production/refrigerantmixer.hjson
        refrigerantmixer = new GenericSmelter("refrigerantmixer") {
            {
                this.localizedName = "Thorium Refrigerant Mixer";
                this.description = "Makes cool Refrigirant from Cryofluid and crushed Thorium.";
                this.health = 130;
                this.size = 2;
                this.consumes.power(3f);
                this.consumes.liquid(Liquids.cryofluid, 0.1f);
                this.consumes.items(with(Items.thorium, 1));
                this.outputLiquid = new LiquidStack(ModLiquids.thoriumRefrigerant, 16f);
                this.requirements(Category.crafting, with(Items.plastanium, 200, Items.thorium, 200, Items.titanium, 100, Items.metaglass, 130, ModItems.graphenite, 190));
                this.updateEffect = Fx.purify;
                this.updateEffectChance = 0.02f;
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-additive-reconstructor.hjson
        hyperAdditiveReconstructor = new Reconstructor("hyper-additive-reconstructor") {
            {
                this.localizedName = "Hyper Additive Reconstructor";
                this.size = 3;
                this.consumes.power(3.2f);
                this.consumes.items(with(Items.silicon, 35, ModItems.graphenite, 15, Items.thorium, 20, Items.titanium, 20));
                this.constructTime = 900;
                this.requirements(Category.units, with(Items.copper, 200, ModItems.odinum, 100, Items.titanium, 120, Items.silicon, 90, Items.plastanium, 80));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.ibis, ModUnitTypes.aries},
                        new UnitType[]{ModUnitTypes.armor, ModUnitTypes.shield},
                        new UnitType[]{ModUnitTypes.venti, ModUnitTypes.lyra}
                );
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-air-factory.hjson
        hyperAirFactory = new UnitFactory("hyper-air-factory") {
            {
                this.localizedName = "Hyper Air Factory";
                this.size = 3;
                this.consumes.power(2.2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.armor, 600, with(Items.silicon, 15, ModItems.graphenite, 5, Items.lead, 5))
                );
                this.requirements(Category.units, with(Items.copper, 50, Items.lead, 150, Items.silicon, 70, Items.plastanium, 30, ModItems.odinum, 60));
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-exponential-reconstructor.hjson
        hyperExponentialReconstructor = new Reconstructor("hyper-exponential-reconstructor") {
            {
                this.localizedName = "Hyper Exponential Reconstructor";
                this.size = 7;
                this.consumes.power(7f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(false, false);
                this.consumes.items(with(Items.silicon, 100, ModItems.graphenite, 100, Items.titanium, 125, Items.plastanium, 75, ModItems.exoticAlloy, 40));
                this.constructTime = 2100;
                this.requirements(Category.units, with(ModItems.graphenite, 1200, Items.titanium, 900, ModItems.odinum, 500, Items.plastanium, 200, Items.phaseFabric, 300, Items.silicon, 750, ModItems.exoticAlloy, 130));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.capra, ModUnitTypes.lacerta},
                        new UnitType[]{ModUnitTypes.chestplate, ModUnitTypes.chainmail},
                        new UnitType[]{ModUnitTypes.tropsy, ModUnitTypes.cenda}
                );
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-ground-factory.hjson
        hyperGroundFactory = new UnitFactory("hyper-ground-factory") {
            {
                this.localizedName = "Hyper Ground Factory";
                this.size = 3;
                this.consumes.power(2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.ibis, 550, with(Items.silicon, 10, ModItems.graphenite, 5, ModItems.odinum, 5))
                );
                this.requirements(Category.units, with(Items.copper, 50, Items.lead, 140, Items.silicon, 70, Items.plastanium, 60));
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-multiplicative-reconstructor.hjson
        hyperMultiplicativeReconstructor = new Reconstructor("hyper-multiplicative-reconstructor") {
            {
                this.localizedName = "Hyper Multiplicative Reconstructor";
                this.size = 5;
                this.consumes.power(6.2f);
                this.consumes.items(with(Items.silicon, 60, ModItems.graphenite, 50, Items.metaglass, 30, Items.titanium, 30, ModItems.odinum, 30));
                this.constructTime = 1620;
                this.requirements(Category.units, with(ModItems.graphenite, 600, Items.titanium, 300, Items.silicon, 300, ModItems.odinum, 500, Items.plastanium, 150));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.aries, ModUnitTypes.capra},
                        new UnitType[]{ModUnitTypes.shield, ModUnitTypes.chestplate},
                        new UnitType[]{ModUnitTypes.lyra, ModUnitTypes.tropsy}
                );
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-naval-factory.hjson
        hyperNavalFactory = new UnitFactory("hyper-naval-factory") {
            {
                this.localizedName = "Hyper Naval Factory";
                this.size = 3;
                this.consumes.power(2.2f);
                this.plans = Seq.with(
                        new UnitPlan(ModUnitTypes.venti, 630, with(Items.silicon, 5, ModItems.graphenite, 10, Items.metaglass, 10))
                );
                this.requirements(Category.units, with(Items.lead, 150, Items.silicon, 80, Items.plastanium, 25, Items.metaglass, 85, ModItems.odinum, 55));
            }
        };
//----end
//start----
//way content\blocks/production/unit fabrics/hyper-tetrative-reconstructor.hjson
        hyperTetrativeReconstructor = new Reconstructor("hyper-tetrative-reconstructor") {
            {
                this.localizedName = "Hyper Tetrative Reconstructor";
                this.size = 9;
                this.consumes.power(9f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.3f).optional(false, false);
                this.consumes.items(with(ModItems.graphenite, 300, Items.surgeAlloy, 300, ModItems.odinum, 200, Items.plastanium, 400, ModItems.exoticAlloy, 200));
                this.constructTime = 3600;
                this.requirements(Category.units, with(ModItems.graphenite, 3100, Items.surgeAlloy, 500, ModItems.odinum, 500, Items.plastanium, 400, Items.phaseFabric, 600, Items.silicon, 1500, ModItems.exoticAlloy, 1000));
                this.upgrades = Seq.with(
                        new UnitType[]{ModUnitTypes.lacerta, ModUnitTypes.aquila},
                        new UnitType[]{ModUnitTypes.chainmail, ModUnitTypes.broadsword}
                );
            }
        };
//----end
//start----
//way content\blocks/turrets/axon.hjson
        Heartbeat=new ItemTurret("heartbeat"){
            {

                this.range = 400;
//                this.recoilAmount = 28;
                this.reloadTime = 15;
                this.size = 3;
                this.shots = 7;
                this.health = 5800;
                this.inaccuracy = 0;
                this.shootSound = Sounds.missile;
                this.rotateSpeed = 0.4f;
                this.targetAir = true;
                this.targetGround = true;
                this.ammo(
                        ModItems.odinum, new BasicBulletType(){
                            {
                                this.damage = 55;
                                this.speed = 4;
                                this.hitSize = 50;
                                this.lifetime = 180;
                                this.status = StatusEffects.unmoving;
                                this.statusDuration = 38;
//                                this.bulletSprite = wave;
                                this.pierce = true;
                                this.width = 120;
//                                this.length = 4;
                                this.hittable = true;
                                this.ammoMultiplier = 1;
                                this.hitColor=this.trailColor=this.backColor=this.frontColor=this.lightColor=this.lightningColor=Color.gray;
                            }
                        },
                        Items.surgeAlloy, new SapBulletType(){
                            {
                                this.sapStrength = 1.2f;
                                this.length = 240;
                                this.damage = 40;
                                this.shootEffect = ModFx.shootSmall;
                                this.hitColor = Color.valueOf("e88ec9");
                                this.color = Color.valueOf("e88ec9");
                                this.hitColor=this.color=this.trailColor=this.lightColor=this.lightningColor=Color.gray;
                                this.despawnEffect = Fx.none;
                                this.width = 1;
                                this.lifetime = 80;
                                this.knockback = 0.4f;
                                this.ammoMultiplier = 1;
                            }
                        },
                        ModItems.exoticAlloy, new PointBulletType(){
                            {
                                this.shootEffect = Fx.instShoot;
                                this.hitEffect = Fx.instHit;
                                this.smokeEffect = Fx.smokeCloud;
                                this.trailEffect = Fx.instTrail;
                                this.despawnEffect = Fx.instBomb;
                                this.trailSpacing = 12;
                                this.damage = 60;
                                this.buildingDamageMultiplier = 0.3f;
                                this.speed = 50;
                                this.hitShake = 0.2f;
                                this.ammoMultiplier = 2;
                                this.hitColor=this.trailColor=this.lightColor=this.lightningColor=Color.gray;
                            }
                        }
                );
                this.consumes.liquid(Liquids.cryofluid,0.2f).optional(false,true);
                this.requirements(Category.turret, with(ModItems.odinum,400, Items.plastanium,350, Items.silicon,800, Items.titanium,420, Items.metaglass,280));
            }
        };
        axon = new ItemTurret("axon") {
            {
                this.health = 2890;
                this.size = 3;
                this.rotateSpeed = 0.9f;
                this.shots = 2;
                this.reloadTime = 40;
                this.hasItems = true;
                this.hasLiquids = true;
                this.range = 180;
                this.localizedName = "Axon";
                this.description = "Powerful Electric shotgun.";
                this.ammo(
                        ModItems.exoticAlloy, new BasicBulletType() {
                            public void update(Bullet b) {
                                SubBullets.addLightning(b, this);
                                super.update(b);
                            }

                            {
                                this.backColor = Color.valueOf("c2cc37");
                                this.width = 14;
                                this.height = 14;
                                this.shrinkY = 0.1f;
                                this.shrinkX = 0.2f;
                                this.spin = 3.5f;
                                this.speed = 4.1f;
                                this.damage = 35;
                                this.shootEffect = Fx.railShoot;
                                this.hitColor = this.frontColor = Color.valueOf("f1fc58");
                                this.despawnEffect = Fx.railHit;
                                this.lifetime = 90;
                                this.knockback = 1;
                                this.lightning = 3;//êîëè÷åñòâî ìîëíèé
                                this.lightningLength = 5;//äëèíà ìîëíèè
                                this.lightningLengthRand = 15;//ðàíäîìíîå ÷èñëî îò 0 äî 50 áóäåò ïðèáàâëÿòüñÿ ê äëèíå ìîëíèè, òî åñòü ðàíäîìèçàöèÿ äëèíû
                                this.lightningDamage = 12;//óðîí ìîëíèè
                                this.lightningAngle = 5;//óãîë íàïðàâëåíèÿ ìîëíèé îòíîñèòåëüíî óãëà ïóëè
                                this.lightningCone = 45;//ðàíäîìèçàöèÿ óãëà íàïðàâëåíèÿ ìîëíèé
                                this.lightningColor = Color.valueOf("f1fc58");
                            }
                        }
                );
                this.requirements(Category.turret, with(ModItems.odinum, 100, ModItems.graphenite, 200, Items.silicon, 140, Items.metaglass, 70, ModItems.exoticAlloy, 140));
            }
        };
//----end
//start----
//way content\blocks/turrets/blaze.hjson
        blaze = new ItemTurret("blaze") {
            {
                this.localizedName = "Blaze";
                this.description = "Fires four powerful beams at enemies. Every beam has 1/4 of the damage. Requires high-tech ammo to fire.";
                this.reloadTime = 90;
                this.shootShake = 5;
                this.range = 130;
                this.recoilAmount = 6;
                this.spread = 20;
                this.shootCone = 35;
                this.size = 4;
                this.health = 2600;
                this.shots = 4;
                this.rotate = true;
                this.shootSound = Sounds.laser;
                this.requirements(Category.turret, with(ModItems.graphenite, 1000, Items.silicon, 1800, ModItems.chromium, 450, ModItems.odinum, 400));
                this.ammo(
                        ModItems.chromium, new ShrapnelBulletType() {
                            {
                                this.length = 200;
                                this.damage = 180;
                                this.width = 20;
                                this.serrationLenScl = 3;
                                this.serrationSpaceOffset = 40;
                                this.serrationFadeOffset = 0;
                                this.serrations = 12;
                                this.serrationWidth = 10;
                                this.ammoMultiplier = 6;
                                this.lifetime = 40;
                                this.shootEffect = Fx.thoriumShoot;
                                this.smokeEffect = Fx.sparkShoot;
                            }
                        },
                        ModItems.odinum, new ShrapnelBulletType() {
                            {
                                this.length = 100;
                                this.damage = 280;
                                this.width = 42;
                                this.serrationLenScl = 6;
                                this.serrationSpaceOffset = 40;
                                this.serrationFadeOffset = 0;
                                this.serrations = 8;
                                this.serrationWidth = 5;
                                this.ammoMultiplier = 7;
                                this.toColor = Color.valueOf("c2f9ff");
                                this.fromColor = Color.valueOf("9ee1e8");
                                this.lifetime = 60;
                                this.shootEffect = Fx.thoriumShoot;
                                this.smokeEffect = Fx.sparkShoot;
                            }
                        }
                );
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.04f).optional(true, true);
            }
        };
//----end

//start----
//way content\blocks/turrets/electron.hjson
        brain = new ItemTurret("brain") {
            {
                this.localizedName = "Brain";
                this.description = "Fires a beam of death at enemies. Requires Phase Alloy to concentrate energy. Total destruction.";
                this.requirements(Category.turret, with(ModItems.graphenite, 570, Items.silicon, 1200, Items.surgeAlloy, 100, Items.thorium, 720, ModItems.phaseAlloy, 230, ModItems.plastic, 350));
                this.ammo(
                        ModItems.phaseAlloy, new LaserBulletType() {
                            {
                                this.length = 240;
                                this.damage = 300;
                                this.width = 60;
                                this.lifetime = 30;
                                this.lightningSpacing = 34;
                                this.lightningLength = 6;
                                this.lightningDelay = 1;
                                this.lightningLengthRand = 21;
                                this.lightningDamage = 45;
                                this.lightningAngleRand = 30;
                                this.largeHit = true;
                                this.shootEffect = ModFx.purpleBomb;
                                this.collidesTeam = true;
                                this.sideAngle = 15;
                                this.sideWidth = 0;
                                this.sideLength = 0;
                                this.lightningColor=Color.valueOf("d5b2ed");
                                this.colors = new Color[]{Color.valueOf("d5b2ed"), Color.valueOf("9671b1"), Color.valueOf("a17dcd")};
                            }
                        },
                        ModItems.plastic, new LaserBulletType() {
                            {
                                this.length = 240;
                                this.damage = 180;
                                this.width = 60;
                                this.lifetime = 30;
                                this.largeHit = true;
                                this.shootEffect = ModFx.purpleLaserChargeSmall;
                                this.collidesTeam = true;
                                this.sideAngle = 15;
                                this.sideWidth = 0;
                                this.sideLength = 0;
                                this.lightningColor=Color.valueOf("d5b2ed");
                                this.colors = new Color[]{Color.valueOf("d5b2ed"), Color.valueOf("9671b1"), Color.valueOf("a17dcd")};
                            }
                        }
                );
                this.reloadTime = 120;
                this.shots = 3;
//                this.shotDelay = 10;
                this.burstSpacing = 3;
                this.inaccuracy = 7;
                this.range = 240;
                this.size = 4;
                this.health = 2500;
            }
        };
        mind = new ItemTurret("mind") {
            {
                this.localizedName = "Mind";
                this.description = "An alternative to Brain. Fires a splash-damage beam. Requires lightweight Plastic to shoot.";
                this.requirements(Category.turret, with(ModItems.graphenite, 100, Items.silicon, 2000, Items.surgeAlloy, 100, ModItems.odinum, 1150, ModItems.phaseAlloy, 350, ModItems.plastic, 350));
                this.ammo(
                        ModItems.plastic, new ShrapnelBulletType() {
                            {
                                this.length = 220;
                                this.damage = 650;
                                this.width = 35;
                                this.serrationLenScl = 7;
                                this.serrationSpaceOffset = 60;
                                this.serrationFadeOffset = 0;
                                this.serrations = 11;
                                this.serrationWidth = 6;
                                this.fromColor = Color.valueOf("d5b2ed");
                                this.toColor = Color.valueOf("a17dcd");
                                this.lifetime = 45;
                                this.shootEffect = Fx.sparkShoot;
                                this.smokeEffect = Fx.sparkShoot;
                            }
                        },
                        ModItems.odinum, new LaserBulletType() {
                            {
                                this.length = 340;
                                this.damage = 320;
                                this.width = 40;
                                this.lifetime = 30;
                                this.lightningSpacing = 34;
                                this.lightningLength = 2;
                                this.lightningDelay = 1;
                                this.lightningLengthRand = 7;
                                this.lightningDamage = 45;
                                this.lightningAngleRand = 30;
                                this.largeHit = true;
                                this.shootEffect = Fx.greenLaserChargeSmall;
                                this.collidesTeam = true;
                                this.sideAngle = 15;
                                this.sideWidth = 0;
                                this.sideLength = 0;
                                this.colors = new Color[]{Color.valueOf("ffffff"), Color.valueOf("EDEDED"), Color.valueOf("A4A4A4")};
                            }
                        },
                        ModItems.exoticAlloy, new ShrapnelBulletType() {
                            {
                                this.length = 170;
                                this.damage = 720;
                                this.width = 35;
                                this.serrationLenScl = 8;
                                this.serrationSpaceOffset = 120;
                                this.serrationFadeOffset = 0;
                                this.serrations = 17;
                                this.serrationWidth = 6;
                                this.fromColor = Color.valueOf("FFF6A3");
                                this.toColor = Color.valueOf("FFE70F");
                                this.lifetime = 35;
                                this.shootEffect = Fx.sparkShoot;
                                this.smokeEffect = Fx.sparkShoot;
                            }
                        }
                );
                this.reloadTime = 65;
                this.shots = 1;
                this.burstSpacing = 3;
                this.inaccuracy = 0.2f;
                this.range = 230;
                this.size = 4;
                this.health = 2350;
            }
        };
        electron = new LaserTurret("electron") {
            {
                this.localizedName = "Dendrite";
                this.shootSound = ModSounds.electronShoot;
                this.loopSound = ModSounds.electronCharge;
                this.health = 8600;
                this.size = 10;
                this.recoilAmount = 11;
                this.shootShake = 4;
                this.shootCone = 15;
                this.rotateSpeed = 0.9f;
                this.shots = 1;
                this.hasItems = true;
                this.hasLiquids = true;
                this.rotate = true;
                this.shootDuration = 170;
                this.powerUse = 162;
                this.range = 400;
                this.firingMoveFract = 0.4f;
                this.localizedName = "Dendrite";
                this.description = "Monstruous turret with Electric Laser.";
                this.shootType = new ContinuousLaserBulletType() {
                    public void update(Bullet b) {
                        SubBullets.addLightning(b, this);
                        super.update(b);
                    }

                    {
                        this.hitSize = 14;
                        this.drawSize = 520;
                        this.width = 34;
                        this.length = 390;
                        this.largeHit = true;
                        this.hitColor = Color.valueOf("f1fc58");
                        this.incendAmount = 4;
                        this.incendSpread = 10;
                        this.incendChance = 0.7f;
                        this.lightColor = Color.valueOf("fbffcc");
                        this.keepVelocity = true;
                        this.collides = true;
                        this.pierce = true;
                        this.hittable = true;
                        this.absorbable = false;
                        this.damage = 82;
                        this.shootEffect = Fx.railShoot;
                        this.despawnEffect = Fx.railHit;
                        this.knockback = 1;
                        this.lightning = 4;//êîëè÷åñòâî ìîëíèé
                        this.lightningLength = 30;//äëèíà ìîëíèè
                        this.lightningLengthRand = 30;//ðàíäîìíîå ÷èñëî îò 0 äî 50 áóäåò ïðèáàâëÿòüñÿ ê äëèíå ìîëíèè, òî åñòü ðàíäîìèçàöèÿ äëèíû
                        this.lightningDamage = 78;//óðîí ìîëíèè
                        this.lightningAngle = 15;//óãîë íàïðàâëåíèÿ ìîëíèé îòíîñèòåëüíî óãëà ïóëè
                        this.lightningCone = 50;//ðàíäîìèçàöèÿ óãëà íàïðàâëåíèÿ ìîëíèé
                        this.lightningColor = Color.valueOf("f1fc58");
                    }
                };
                this.requirements(Category.turret, with(ModItems.phaseAlloy, 250, ModItems.exoticAlloy, 400, Items.surgeAlloy, 300, ModItems.chromium, 200, ModItems.odinum, 300, ModItems.graphenite, 420, Items.metaglass, 120));
                this.reloadTime = 4;
            }
        };
//----end
//start----
//way content\blocks/turrets/fragment.hjson
        fragment = new PointDefenseTurret("fragment") {
            {
                this.localizedName = "Fragment";
                this.description = "Upgraded \"Segment\", consumes more power aswell Refrigerant for cooling.";
                this.size = 3;
                this.health = 410;
                this.range = 250;
                this.hasPower = true;
                this.shootLength = 14;
                this.bulletDamage = 32;
                this.reloadTime = 10;
                this.consumes.power(4.7f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.04f).optional(false, false);
                this.requirements(Category.turret, with(ModItems.phaseAlloy, 20, Items.silicon, 200, ModItems.odinum, 120));
            }
        };
//----end
//start----
//way content\blocks/turrets/impulse.hjson
        impulse = new ItemTurret("impulse") {
            {
                this.localizedName = "Impulse";
                this.health = 840;
                this.size = 1;
                this.rotateSpeed = 1.8f;
                this.shots = 1;
                this.reloadTime = 30;
                this.hasItems = true;
                this.hasLiquids = true;
                this.range = 130;
                this.localizedName = "Impulse";
                this.description = "Arc upgraded version, can make more lightnings and shot a one basic bullet.";
                this.ammo(
                        Items.silicon, new BasicBulletType() {
                            public void update(Bullet b) {
                                SubBullets.addLightning(b, this);
                                super.update(b);
                            }

                            {
                                this.backColor = Color.valueOf("c2cc37");
                                this.width = 4;
                                this.height = 6;
                                this.shrinkY = 0.1f;
                                this.shrinkX = 0.2f;
                                this.spin = 1.2f;
                                this.speed = 2.1f;
                                this.damage = 8;
                                this.shootEffect = Fx.shockwave;
                                this.hitColor = this.frontColor = Color.valueOf("f1fc58");
                                this.despawnEffect = Fx.hitLancer;
                                this.lifetime = 60;
                                this.knockback = 1;
                                this.lightning = 3;//êîëè÷åñòâî ìîëíèé
                                this.lightningLength = 3;//äëèíà ìîëíèè
                                this.lightningLengthRand = 7;//ðíàäîìíàÿ äëèíà
                                this.lightningDamage = 20;//óðîí ìîëíèè
                                this.lightningAngle = 3;//óãîë íàïðàâëåíèÿ ìîëíèé îòíîñèòåëüíî óãëà ïóëè
                                this.lightningCone = 20;//ðàíäîìèçàöèÿ óãëà íàïðàâëåíèÿ ìîëíèé
                                this.lightningColor = Color.valueOf("f1fc58");
                            }
                        }
                );
                this.requirements(Category.turret, with(Items.silicon, 30, ModItems.graphenite, 20, Items.lead, 60, Items.copper, 70));
            }
        };
//----end
//start----
//way content\blocks/turrets/katana.hjson
        katana = new ItemTurret("katana") {
            {
                this.localizedName = "Katana";
                this.description = "Strong artillery turret with powerful ammo. Requires a strong alloy to fire. Better in groups.";
                this.range = 300;
                this.recoilAmount = 6;
                this.reloadTime = 15;
                this.size = 4;
                this.shots = 4;
                this.health = 3000;
                this.inaccuracy = 0.1f;
                this.rotateSpeed = 1.4f;
                this.targetAir = false;
                this.targetGround = true;
                this.ammo(
                ModItems.graphenite, Bullets.artilleryDense,
                ModItems.phaseAlloy, Bullets.artilleryHoming,
                ModItems.exoticAlloy, Bullets.artilleryExplosive,
                ModItems.plastic, Bullets.artilleryPlastic
                );
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(true, true);
                this.requirements(Category.turret, with(ModItems.exoticAlloy, 400, ModItems.graphenite, 400, Items.plastanium, 250, Items.silicon, 700, Items.surgeAlloy, 250, ModItems.phaseAlloy, 200));
            }
        };
//----end

//start----
//way content\blocks/turrets/neuron.hjson
        neuron = new ItemTurret("neuron") {
            {
                this.localizedName = "Neuron";
                this.description = "A small turret that fires lasers that do splash damage. Requires power aswell as Exotic Alloy to shoot.";
                this.requirements(Category.turret, with(ModItems.graphenite, 250, Items.silicon, 400, Items.surgeAlloy, 70, Items.plastanium, 120, Items.thorium, 350));
                this.ammo(
                        ModItems.exoticAlloy, new PointBulletType() {
                            {
                                this.shootEffect = Fx.instShoot;
                                this.hitEffect = Fx.instHit;
                                this.smokeEffect = Fx.smokeCloud;
                                this.trailEffect = Fx.instTrail;
                                this.despawnEffect = Fx.instBomb;
                                this.trailSpacing = 12;
                                this.damage = 360;
                                this.buildingDamageMultiplier = 0.3f;
                                this.speed = 50;
                                this.hitShake = 1;
                                this.ammoMultiplier = 2;
                            }
                        },
                        ModItems.plastic, new ShrapnelBulletType() {
                            {
                                this.length = 170;
                                this.damage = 250;
                                this.width = 15;
                                this.serrationLenScl = 3;
                                this.serrationSpaceOffset = 20;
                                this.serrationFadeOffset = 0;
                                this.serrations = 9;
                                this.serrationWidth = 4;
                                this.fromColor = Color.valueOf("AD5CFF");
                                this.toColor = Color.valueOf("870FFF");
                                this.lifetime = 45;
                                this.shootEffect = Fx.sparkShoot;
                                this.smokeEffect = Fx.sparkShoot;
                            }
                        }
                );
                this.consumes.power(4f);
                this.consumes.liquid(Liquids.cryofluid, 0.1f).optional(false, false);
                this.reloadTime = 35;
                this.shots = 1;
                this.burstSpacing = 6;
                this.inaccuracy = 1;
                this.range = 190;
                this.size = 2;
                this.health = 800;
            }
        };
//----end
//start----
//way content\blocks/turrets/perlin.hjson
        perlin = new TractorBeamTurret("perlin") {
            {
                this.localizedName = "Perlin";
                this.description = "Upgraded \"Parallax\". Does more damage to targets at cost of power and cooling.";
                this.size = 3;
                this.force = 14;
                this.scaledForce = 9;
                this.health = 390;
                this.range = 220;
                this.damage = 5;
                this.rotateSpeed = 10;
                this.hasPower = true;
                this.consumes.power(4.2f);
                this.consumes.liquid(Liquids.cryofluid, 0.1f).optional(false, false);
                this.requirements(Category.turret, with(ModItems.graphenite, 100, Items.thorium, 100, Items.silicon, 180));
            }
        };
//----end
//start----
//way content\blocks/turrets/soul.hjson
        soul = new ItemTurret("soul") {
            {
                this.localizedName = "Soul";
                this.description = "A small, but powerful turret. Requires expensive Surge Alloy to fire. Can literally reap your soul.";
                this.requirements(Category.turret, with(ModItems.graphenite, 340, Items.silicon, 405, Items.surgeAlloy, 100, ModItems.plastic, 270, ModItems.odinum, 420));
                this.ammo(
                        Items.surgeAlloy, new SapBulletType() {
                            {
                                this.sapStrength = 1.2f;
                                this.length = 160;
                                this.damage = 400;
                                this.shootEffect = Fx.shootSmall;
                                this.hitColor = Color.valueOf("e88ec9");
                                this.color = Color.valueOf("e88ec9");
                                this.despawnEffect = Fx.none;
                                this.width = 2;
                                this.lifetime = 60;
                                this.knockback = 0.4f;
                            }
                        }
                );
                this.reloadTime = 60;
                this.shots = 1;
                this.burstSpacing = 6;
                this.inaccuracy = 1.1f;
                this.range = 140;
                this.size = 2;
                this.health = 1300;
            }
        };
//----end
//start----
//way content\blocks/turrets/stinger.hjson
        stinger = new ItemTurret("stinger") {
            {
                this.localizedName = "Stinger";
                this.description = "A huge, powerful rocket launcher. Doesn't require expensive ammo to fire. May be a better Ripple.";
                this.range = 280;
//                this.recoil = 18;

                this.reloadTime = 60;
                this.size = 4;
                this.shots = 4;
                this.health = 1400;
                this.inaccuracy = 0.4f;
//                this.rotatespeed = 1.3f;
                this.targetAir = true;
                this.targetGround = true;
                this.ammo(
                        Items.plastanium, missileSurge,
                        ModItems.graphenite, missileIncendiary,
                        ModItems.odinum, missileExplosive
                );
                this.consumes.liquid(Liquids.cryofluid, 0.1f).optional(false, true);
                this.requirements(Category.turret, with(ModItems.graphenite, 500, Items.plastanium, 250, Items.silicon, 500, ModItems.odinum, 460, Items.phaseFabric, 360));
            }
        };
//----end
//start----
//way content\blocks/turrets/synaps.hjson
        synaps = new ItemTurret("synaps") {
            {
                this.localizedName = "Synaps";
                this.health = 1260;
                this.size = 2;
                this.hasItems = true;
                this.hasLiquids = true;
                this.localizedName = "Synaps";
                this.description = "Electrical Sap turret, can shoot a sap bullet with mane ligthnings.";

                this.ammo(
                        Items.plastanium, new SapBulletType() {
                            public void update(Bullet b) {
                                SubBullets.addLightning(b, this);
                                super.update(b);
                            }

                            {
                                this.sapStrength = 0.48f;
                                this.length = 55;
                                this.damage = 37;
                                this.shootEffect = Fx.railShoot;
                                this.hitColor = this.color = Color.valueOf("fbff9e");
                                this.despawnEffect = Fx.railHit;
                                this.width = 3;
                                this.lifetime = 45;
                                this.knockback = -1;
                                this.lightning = 4;
                                this.lightningLength = 2;
                                this.lightningLengthRand = 10;
                                this.lightningDamage = 50;
                                this.lightningAngle = 6;
                                this.lightningCone = 12;
//                                this.largeHit = true;
                                this.lightColor = this.lightningColor = Color.valueOf("fbff9e");
                            }
                        }
                );

                this.requirements(Category.turret, with(ModItems.graphenite, 120, Items.silicon, 140, Items.lead, 190, Items.titanium, 120));
            }
        };
//----end
//start----
        glory = new LaserTurret("glory"){{
                this.localizedName = "Synaps";
                this.requirements(Category.turret, with(Items.plastanium, 340, Items.lead, 350, Items.phaseFabric, 260, Items.surgeAlloy, 360, Items.silicon, 390));
                this.shootEffect = Fx.greenBomb;
                this.shootCone = 21f;
                this.recoilAmount = 6f;
                this.size = 4;
                this.shootShake = 3f;
                this.range = 250f;
                this.reloadTime = 100f;
                this.firingMoveFract = 0.4f;
                this.shootDuration = 280f;
                this.powerUse = 23f;
                this.shootSound = Sounds.laserblast;
                this.loopSound = Sounds.lasercharge2;
                this.loopSoundVolume = 2.4f;

                this.shootType = new ContinuousLaserBulletType(80){{
                    this.length = 260f;
                    this.hitEffect = Fx.hitMeltdown;
                    this.drawSize = 400f;

                    this.incendChance = 0.7f;
                    this.incendSpread = 9f;
                    this.incendAmount = 3;
                }};

                this.health = 240 * size * size;
                this.consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.5f)).update(false);
            }};
//----end
//start----
//way content\blocks/walls/exotic-alloy-wall-large.hjson
        exoticAlloyWallLarge = new Wall("exotic-alloy-wall-large") {
            {
                this.localizedName = "Exotic Alloy Wall Large";
                this.description = "A bigger Exotic Alloy wall, creates lightings when shot.";
                this.health = 4060;
                this.size = 2;
                this.requirements(Category.defense, with(ModItems.exoticAlloy, 22, ModItems.graphenite, 8));
                this.lightningChance = 0.09f;
            }
        };
//----end
//start----
//way content\blocks/walls/exotic-alloy-wall.hjson
        exoticAlloyWall = new Wall("exotic-alloy-wall") {
            {
                this.localizedName = "Exotic Alloy Wall";
                this.description = "An Exotic Alloy wall, creates lightnings when shot.";
                this.health = 1160;
                this.size = 1;
                this.requirements(Category.defense, with(ModItems.exoticAlloy, 6, ModItems.graphenite, 2));
                this.lightningChance = 0.08f;
            }
        };
//----end
//start----
//way content\blocks/walls/graphenite-wall-large.hjson
        grapheniteWallLarge = new Wall("graphenite-wall-large") {
            {
                this.localizedName = "Large Graphenite Wall";
                this.description = "A bigger version of standard graphenite wall.";
                this.health = 2160;
                this.size = 2;
                this.requirements(Category.defense, with(ModItems.graphenite, 20, Items.silicon, 6));
            }
        };
//----end
        grapheniteWall = new Wall("graphenite-wall") {
            {
                this.localizedName = "Graphenite Wall";
                this.description = "A purple, medium strength wall.";
                this.size = 1;
                this.health = 620;
                this.requirements(Category.defense, with(ModItems.graphenite, 6, Items.silicon, 2));

            }
        };
//start----
//way content\blocks/walls/odinum-wall-large.hjson
        odinumWallLarge = new Wall("odinum-wall-large") {
            {
                this.localizedName = "Odinum Wall Large";
                this.description = "Bigger Odinum Wall, can deflect bullets.";
                this.health = 3360;
                this.size = 2;
                this.requirements(Category.defense, with(ModItems.odinum, 28));
                this.chanceDeflect = 14;
                this.flashHit = true;
            }
        };
//----end
        odinumWall=new Wall("odinum-wall"){
            {
                this.localizedName = "Odinum Wall";
                this.description = "Medium strength wall, a little radioactive.";
                this.size = 1;
                this.health = 1020;
                this.requirements(Category.defense, with(ModItems.odinum,8));
                this.chanceDeflect = 12;
                this.flashHit = true;
            }
        };
//start----
//way content\blocks/walls/plastic-wall-large.hjson
        plasticWallLarge = new Wall("plastic-wall-large") {
            {
                this.localizedName = "Plastic Wall Large";
                this.description = "A bigger Plastic wall, can deflect some bullets.";
                this.health = 3820;
                this.size = 2;
                this.requirements(Category.defense, with(ModItems.plastic, 24, Items.metaglass, 10));
                this.insulated = true;
                this.absorbLasers = true;
            }
        };
//----end
//start----
//way content\blocks/walls/plastic-wall.hjson
        plasticWall = new Wall("plastic-wall") {
            {
                this.localizedName = "Plastic Wall";
                this.description = "A Plastic wall, can deflect some bullets.";
                this.health = 980;
                this.size = 1;
                this.requirements(Category.defense, with(ModItems.plastic, 6, Items.metaglass, 4));
                this.insulated = true;
                this.absorbLasers = true;
            }
        };
//----end

//----end


        smartRouter = new SmartRouter("smart-router") {
            {
                this.size = 1;
                this.requirements(Category.distribution, with(Items.copper, 3, Items.silicon, 10));
                this.buildCostMultiplier = 4.0F;
            }
        };

    }
}
