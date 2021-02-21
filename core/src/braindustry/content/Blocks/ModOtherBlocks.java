package braindustry.content.Blocks;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.world.blocks.power.BlackHoleReactor;
import braindustry.world.blocks.production.MultiRotorDrill;
import arc.math.geom.Vec2;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.ShallowLiquid;
import mindustry.world.blocks.legacy.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.environment.StaticTree;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.StorageBlock;
import mindustryAddition.world.blocks.distribution.CrossItemBridge;

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
        phaseAlloyConveyor = new CrossItemBridge("phase-alloy-conveyor") {
            {
                this.localizedName = "Phase Alloy Conveyor";
                this.requirements(Category.distribution, ItemStack.with(ModItems.phaseAlloy, 5, Items.silicon, 7, Items.lead, 10, Items.graphite, 10));
                this.range = 12;
                this.canOverdrive = false;
                this.hasPower = true;
                this.consumes.power(0.3F);
                /** custom connect filter*/
                connectFilter = (build) -> {
                    Block block = build.block;
                    return block.acceptsItems || block instanceof StorageBlock;
                };
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
                cacheLayer = CacheLayer.tar;
                this.speedMultiplier = 0.17f;
                status = StatusEffects.blasted;
                this.liquidDrop = ModLiquids.magma;
                this.statusDuration = 240;
                this.drownTime = 90;
                this.walkEffect = Fx.melting;
                this.drownUpdateEffect = Fx.burning;
            }
        };
        obsidianBlock = new StaticWall("obsidian-wall") {
            {
                this.localizedName = "Obsidian Block";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }

            @Override
            public TextureRegion[] editorVariantRegions() {
                Log.info("editorVariantRegions() @",name);
                return super.editorVariantRegions();
            }

            @Override
            public TextureRegion editorIcon() {
                Log.info("editorIcon() @",name);
                return super.editorIcon();
            }

            @Override
            public void drawBase(Tile tile) {
                Log.info("drawBase(@) @",tile.toString(),name);
                super.drawBase(tile);
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
                this.consumes.power(6.8F);
                this.consumes.liquid(ModLiquids.magma, 0.4F);
            }
        };
        quarryDrill = new Drill("quarry-drill") {
            {
                this.localizedName = "Quarry Drill";
                this.description = "A fastest, most efficient gigantic drill.";
                this.size = 5;
                this.health = 260;
                this.drillTime = 210;
                this.tier = 5;
                this.drawRim = true;
                this.consumes.power(2f);
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(false, false);
                this.hasLiquids = true;
                this.liquidCapacity = 60;
                this.rotateSpeed = 5;
                this.requirements(Category.production, ItemStack.with(ModItems.plastic, 150, ModItems.graphenite, 120, ModItems.odinum, 140, Items.plastanium, 100));
            }
        };
        crimzesWall = new StaticWall("crimzes-wall") {
            {
                this.localizedName = "Crimson Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        jungleWall = new StaticWall("jungle-shrubs") {
            {
                this.localizedName = "Jungle Shrubs";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        dirtRocksWall = new StaticWall("dirt-rocks") {
            {
                this.localizedName = "Hard Dirt Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        jungleFloor = new Floor("jungle-grass") {
            {
                this.localizedName = "Jungle Grass";
                this.variants = 3;
            }
        };
        crimzesFloor = new Floor("crimzes-floor") {
            {
                this.localizedName = "Crimzes Floor";
                this.variants = 3;
            }
        };
        graysand = new Floor("graysand"){{
            itemDrop = Items.sand;
            playerUnmineable = true;
        }};
        liquidMethaneFloor = new Floor("liquid-methane-floor") {
            {
                this.localizedName = "Liquid Methane";
                this.isLiquid = true;
                this.variants = 1;
                this.blendGroup = Blocks.water;
                this.cacheLayer = CacheLayer.tar;
                this.speedMultiplier = -0.15f;
                status = StatusEffects.freezing;
                this.liquidDrop = ModLiquids.liquidMethane;
                this.statusDuration = 200;
                this.drownTime = 90;
                this.walkEffect = Fx.freezing;
                this.drownUpdateEffect = Fx.freezing;
            }
        };
        blackIceWall = new StaticWall("black-ice-wall") {
            {
                this.localizedName = "Black Ice Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        blackSnowWall = new StaticWall("black-snow-wall") {
            {
                this.localizedName = "Black Snow Wall";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        darkShrubs = new StaticWall("dark-shrubs") {
            {
                this.localizedName = "Dark Shrubs";
                this.breakable = false;
                this.alwaysReplace = false;
                this.solid = true;
                this.variants = 2;
            }
        };
        blackIce = new Floor("black-ice") {
            {
                this.localizedName = "Black Ice";
                this.variants = 3;
                //attributes.set(Attribute.water, 0.6f);
            }
        };
        blackSnow = new Floor("black-snow") {
            {
                this.localizedName = "Black Snow";
                this.variants = 3;
                //attributes.set(Attribute.water, 0.2f);
            }
        };
        swampSandWater = new ShallowLiquid("swamp-sand-water-floor"){{
            speedMultiplier = 0.7f;
            statusDuration = 60f;
            variants = 1;
            albedo = 0.3f;
        }};
        swampWater = new Floor("swamp-water-floor") {
            {
                this.localizedName = "Swamp Water";
                this.isLiquid = true;
                this.variants = 1;
                this.blendGroup = Blocks.water;
                this.cacheLayer = CacheLayer.water;
                this.speedMultiplier = -0.5f;
                status = StatusEffects.corroded;
                this.liquidDrop = Liquids.water;
                this.statusDuration = 120;
                this.drownTime = 60;
                this.walkEffect = Fx.muddy;
                this.drownUpdateEffect = Fx.muddy;
            }
        };
        greenTree = new TreeBlock("green-tree");

        blackTree = new TreeBlock("black-tree");

        darkPine = new StaticTree("dark-pine"){{
            variants = 0;
        }};
        metallicPine = new StaticTree("metallic-pine"){{
            variants = 0;
        }};
    }
}
