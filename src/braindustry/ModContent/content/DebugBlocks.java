package braindustry.ModContent.content;

import braindustry.ModContent.world.blocks.TestBlock;
import braindustry.ModContent.world.blocks.defense.PowerHeatTurret;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

import static braindustry.modVars.modFunc.addResearch;

public class DebugBlocks implements ContentList {
    public static Block testBlock1, testBlock2, testBlock3, testBlock4;
    public static Block advancedLancer;
    @Override
    public void load() {

        advancedLancer= new PowerHeatTurret("advanced-lancer") {
            {
                this.requirements(Category.turret, ItemStack.with(Items.copper, 60, Items.lead, 70, Items.silicon, 50));
                this.range = 165.0F;
                this.chargeTime = 40.0F;
                this.chargeMaxDelay = 30.0F;
                this.chargeEffects = 7;
                this.recoilAmount = 2.0F;
                this.reloadTime = 80.0F;
                this.cooldown = 0.03F;
                this.powerUse = 7.0F;
                this.shootShake = 2.0F;
                this.shootEffect = Fx.lancerLaserShoot;
                this.smokeEffect = Fx.none;
                this.chargeEffect = Fx.lancerLaserCharge;
                this.chargeBeginEffect = Fx.lancerLaserChargeBegin;
                this.heatColor = Color.red;
                this.size = 2;
                this.health = 280 * this.size * this.size;
                this.targetAir = false;
                this.shootSound = Sounds.laser;
                this.shootType = new LaserBulletType(140.0F) {
                    {
                        this.colors = new Color[]{Pal.lancerLaser.cpy().mul(1.0F, 1.0F, 1.0F, 0.4F), Pal.lancerLaser, Color.white};
                        this.hitEffect = Fx.hitLancer;
                        this.damage*=2;
                        this.despawnEffect = Fx.none;
                        this.hitSize = 4.0F;
                        this.lifetime = 16.0F;
                        this.drawSize = 400.0F;
                        this.collidesAir = false;
                        this.length = 173.0F;
                    }
                };
            }
        };
        testBlock1 = new TestBlock("testBlock1") {
            {
                this.size = 1;
                this.requirements(Category.logic, ItemStack.with(), true);
                addResearch(Blocks.tetrativeReconstructor, this);
            }
        };
        testBlock2 = new TestBlock("testBlock2") {
            {
                this.size = 2;
                this.requirements(Category.logic, ItemStack.with(), true);
                addResearch(Blocks.tetrativeReconstructor, this);
            }
        };
        testBlock3 = new TestBlock("testBlock3") {
            {
                this.size = 3;
                this.requirements(Category.logic, ItemStack.with(), true);
                addResearch(Blocks.tetrativeReconstructor, this);
            }
        };
        testBlock4 = new TestBlock("testBlock4") {
            {
                this.size = 4;
                this.requirements(Category.logic, ItemStack.with(), true);
                addResearch(Blocks.tetrativeReconstructor, this);
            }
        };
    }
}
