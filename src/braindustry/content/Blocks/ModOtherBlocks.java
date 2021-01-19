package braindustry.content.Blocks;

import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.world.blocks.production.MultiRotorDrill;
import arc.math.geom.Vec2;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.Drill;

import static braindustry.content.ModBlocks.*;
public class ModOtherBlocks implements ContentList {
    @Override
    public void load() {
        armoredPlastaniumConveyor = new ArmoredConveyor("armored-plastanium-conveyor") {
            {
                this.localizedName = "Armored Plastanium Conveyor";
                this.description = "Wonderful and strong Plastanium Conveyor.";
                this.health = 240;
                this.hasItems = true;
                this.itemCapacity = 6;
                this.requirements(Category.distribution, ItemStack.with(Items.silicon, 2, ModItems.graphenite, 1, Items.metaglass, 1, Items.plastanium, 2));
                this.speed = 0.3f;
            }
        };
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
                this.requirements(Category.liquid, ItemStack.with(ModItems.chromium, 2, Items.metaglass, 2, Items.thorium, 1));
            }
        };
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
                this.requirements(Category.distribution, ItemStack.with(ModItems.phaseAlloy, 5, Items.silicon, 10, ModItems.graphenite, 8, Items.titanium, 10));
                this.consumes.power(0.4f);
            }
        };
        plasticConveyor = new Conveyor("plastic-conveyor") {
            {
                this.localizedName = "Hermetic Plastic Conveyor";
                this.description = "The most fast and durable conveyor!";
                this.health = 168;
                this.requirements(Category.distribution, ItemStack.with(Items.silicon, 1, ModItems.graphenite, 1, ModItems.plastic, 1));
                this.speed = 0.45f;
            }
        };
        surgeConveyor = new StackConveyor("surge-conveyor") {
            {
                this.localizedName = "Surge Conveyor";
                this.requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 1, Items.silicon, 1, ModItems.graphenite, 1));
                this.health = 100;
                this.speed = 0.12f;
                this.itemCapacity = 12;
            }
        };
        surgePayloadConveyor = new PayloadConveyor("surge-payload-conveyor"){
            {
            this.health = 310;
            this.requirements(Category.distribution, ItemStack.with(ModItems.graphenite, 10, Items.surgeAlloy, 5));
            this.canOverdrive = true;
            this.size = 6;
            }
        };
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
        obsidianBlock = new StaticWall("obsidian-block") {
            {
                this.localizedName = "Obsidian Block";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        obsidianFloor = new Floor("obsidian-floor") {
            {
                this.localizedName = "Obsidian Floor";
                this.variants = 3;
            }
        };
        oreChromium = new OreBlock("ore-chromium") {
            {
                this.itemDrop = ModItems.chromium;
            }
        };
        oreOdinum = new OreBlock("ore-odinum") {
            {
                this.itemDrop = ModItems.odinum;
            }
        };
        blackHoleReactor = new ImpactReactor("black-hole-reactor") {
            {
                this.localizedName = "Blackhole Reactor";
                this.description = "Create power from small Blackhole in center.";
                this.size = 14;
                this.hasPower = true;
                this.hasLiquids = true;
                this.hasItems = false;
                //this.itemCapacity = 120;
                this.liquidCapacity = 100;
                this.itemDuration = 240;
                this.powerProduction = 192;
                this.consumes.power(16f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.5f);
                this.requirements(Category.power, ItemStack.with(ModItems.astroAlloy, 300, Items.surgeAlloy, 200, Items.graphite, 500, ModItems.odinum, 100));
            }
        };
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
                this.consumes.items(ItemStack.with(Items.pyratite, 3));
                this.requirements(Category.power, ItemStack.with(ModItems.graphenite, 140, ModItems.chromium, 190, Items.plastanium, 120, Items.titanium, 200, ModItems.odinum, 70));
            }
        };
        grapheniteSolarCollectorLarge = new SolarGenerator("graphenite-solar-collector-large") {
            {
                this.localizedName = "Graphenite Solar Collector Large";
                this.description = "Solar panel with Graphenite elements, better than Large Solar Panel.";
                this.powerProduction = 1.6f;
                this.size = 3;
                this.health = 210;
                this.requirements(Category.power, ItemStack.with(Items.metaglass, 100, Items.silicon, 40, ModItems.graphenite, 70));
            }
        };
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
                this.consumes.items(ItemStack.with(Items.coal, 1));
                this.requirements(Category.power, ItemStack.with(ModItems.graphenite, 50, Items.silicon, 80, Items.plastanium, 120, ModItems.chromium, 160));
            }
        };
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
                this.consumes.items(ItemStack.with(ModItems.odinum, 1));
                this.requirements(Category.power, ItemStack.with(Items.metaglass, 500, ModItems.odinum, 300, Items.silicon, 400, ModItems.graphenite, 200, Items.plastanium, 200));
            }
        };
        phaseAlloySolarPanel = new SolarGenerator("phase-alloy-solar-panel") {
            {
                this.localizedName = "Phase Alloy Solar Panel";
                this.description = "An improved version of the Large Solar Panel. Takes up a lot of space.";
                this.health = 960;
                this.size = 5;
                this.hasPower = true;
                this.powerProduction = 8.4f;
                this.requirements(Category.power, ItemStack.with(Items.thorium, 280, Items.silicon, 400, Items.titanium, 90, ModItems.graphenite, 80, ModItems.phaseAlloy, 115));
                this.buildCostMultiplier = 1.1f;
            }
        };
        phaseTower = new PowerNode("phase-tower") {
            {
                this.localizedName = "Phase Tower";
                this.health = 240;
                this.size = 4;
                this.maxNodes = 12;
                this.laserRange = 45;
                this.requirements(Category.power, ItemStack.with(ModItems.phaseAlloy, 15, Items.silicon, 20, Items.titanium, 10, Items.lead, 10));
            }
        };
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
                this.consumes.items(ItemStack.with(Items.thorium, 3));
                this.requirements(Category.power, ItemStack.with(Items.metaglass, 300, ModItems.odinum, 100, Items.silicon, 210, ModItems.graphenite, 270, Items.thorium, 200, Items.titanium, 140));
            }
        };

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
                this.requirements(Category.production, ItemStack.with(Items.plastanium, 15, Items.silicon, 40, Items.graphite, 45, ModItems.odinum, 75));
                this.ambientSound = Sounds.drill;
                this.ambientSoundVolume = 0.01f;
            }
        };
        geothermicDrill = new MultiRotorDrill("geothermic-drill") {
            {
                this.requirements(Category.production, ItemStack.with(
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
                this.requirements(Category.production, ItemStack.with(ModItems.plastic, 150, ModItems.graphenite, 120, ModItems.odinum, 140, Items.plastanium, 100));
            }
        };
        crimzesBlock = new StaticWall("crimzes-block") {
            {
                this.localizedName = "Crimson Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 3;
            }
        };
        jungleWall = new StaticWall("jungle-wall") {
            {
                this.localizedName = "Jungle Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 3;
            }
        };
        dirtRocksWall = new StaticWall("dirt-rocks-wall") {
            {
                this.localizedName = "Hard Dirt Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 3;
            }
        };
        jungleFloor = new Floor("jungle-floor") {
            {
                this.localizedName = "Jungle Floor";
                this.variants = 5;
            }
        };
        crimzesFloor = new Floor("crimzes-floor") {
            {
                this.localizedName = "crimzes Floor";
                this.variants = 3;
            }
        };
    }
}
