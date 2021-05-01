package braindustry.content.Blocks;

import Gas.world.blocks.distribution.GasRouter;
import arc.math.geom.Vec2;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.world.blocks.distribution.CrossItemBridge;
import braindustry.world.blocks.production.MultiRotorDrill;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.StorageBlock;

import static braindustry.content.Blocks.ModBlocks.*;

class ModOtherBlocks implements ContentList {
    @Override
    public void load() {
        gasRouter = new GasRouter("gas-router"){
            {
                this.localizedName = "Gas Router";
                this.description = "Simple router for gasses.";
                this.health = 50;
                this.hasGas = true;
                this.gasCapacity = 20;
                this.requirements(Category.liquid, ItemStack.with(Items.titanium, 1, ModItems.graphenite, 1, Items.metaglass, 3));
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
        armoredPlastaniumConveyor = new ArmoredConveyor("armored-plastanium-conveyor") {{
            this.localizedName = "Armored Plastanium Conveyor";
            this.description = "Wonderful and strong Plastanium Conveyor.";
            this.health = 240;
            this.hasItems = true;
            this.itemCapacity = 6;
            this.requirements(Category.distribution, ItemStack.with(Items.silicon, 2, ModItems.graphenite, 1, Items.metaglass, 1, Items.plastanium, 2));
            this.speed = 0.3f;
        }};
        chromiumConduit = new Conduit("chromium-conduit") {{
            this.localizedName = "Chromium Conduit";
            this.size = 1;
            this.description = "Heat resistant and fast conduit.";
            this.health = 140;
            this.liquidCapacity = 18;
            this.rotate = true;
            this.solid = false;
            this.floating = true;
            this.requirements(Category.liquid, ItemStack.with(ModItems.chromium, 2, Items.metaglass, 2, Items.thorium, 1));
        }};
        plasticConveyor = new Conveyor("plastic-conveyor") {{
            this.localizedName = "Hermetic Plastic Conveyor";
            this.description = "The most fast and durable conveyor.";
            this.health = 168;
            this.requirements(Category.distribution, ItemStack.with(Items.silicon, 1, ModItems.graphenite, 1, ModItems.plastic, 1));
            this.speed = 0.45f;
        }};
        surgeConveyor = new StackConveyor("surge-conveyor") {{
            this.localizedName = "Surge Conveyor";
            this.requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 1, Items.silicon, 1, ModItems.graphenite, 1));
            this.health = 100;
            this.speed = 0.12f;
            this.itemCapacity = 12;
        }};
        phaseAlloyConveyor = new CrossItemBridge("phase-alloy-conveyor") {{
            this.localizedName = "Dense Composite Conveyor";
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
        }};
        /*surgePayloadConveyor = new PayloadConveyor("surge-payload-conveyor"){
            {
                this.health = 310;
                this.requirements(Category.distribution, ItemStack.with(ModItems.graphenite, 10, Items.surgeAlloy, 5));
                this.canOverdrive = true;
                this.size = 6;
            }};*/


        hydraulicDrill = new Drill("hydraulic-drill") {{
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
        }};
        geothermicDrill = new MultiRotorDrill("geothermic-drill") {{
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
        }};
        quarryDrill = new Drill("quarry-drill") {{
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
        }};
    }
}
