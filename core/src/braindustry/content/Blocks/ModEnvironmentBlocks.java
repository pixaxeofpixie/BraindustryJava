package braindustry.content.Blocks;

import arc.graphics.g2d.TextureRegion;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.graphics.CacheLayer;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.*;

import static braindustry.content.Blocks.ModBlocks.*;

class ModEnvironmentBlocks implements ContentList {
    public void load(){
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
                return super.editorVariantRegions();
            }

            @Override
            public TextureRegion editorIcon() {
                return super.editorIcon();
            }

            @Override
            public void drawBase(Tile tile) {
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
        darkShrubsFloor = new Floor("black-grass") {
            {
                this.localizedName = "Dark Grass";
                this.variants = 3;
            }
        };
    }
}
