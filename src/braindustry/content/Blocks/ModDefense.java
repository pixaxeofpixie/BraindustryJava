package braindustry.content.Blocks;
import braindustry.content.*;
import braindustry.entities.bullets.RainbowLaserBulletType;
import braindustry.world.blocks.Wall.ReflectionWall;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Tmp;
import braindustry.entities.bullets.ReflectionBullet;
import braindustry.entities.bullets.SpikeCircleOrbonBullet;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.*;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LaserTurret;
import mindustry.world.blocks.defense.turrets.PointDefenseTurret;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret;
import mindustry.world.consumers.ConsumeLiquidFilter;

import static braindustry.content.ModBlocks.*;
import static mindustry.content.Bullets.*;

public class ModDefense implements ContentList {

    @Override
    public void load() {
        heartbeat =new ItemTurret("heartbeat"){
            {
                this.localizedName="Heartbeat";
                this.range = 200;
//                this.recoilAmount = 28;
                this.reloadTime = 70;
                this.size = 5;
                this.shots = 1;
                this.health = 5800;
                this.inaccuracy = 0;
                this.shootSound = Sounds.missile;
                this.rotateSpeed = 0.4f;
                this.targetAir = true;
                this.targetGround = true;
                this.ammo(
                        ModItems.odinum, new BasicBulletType(){public void draw(Bullet b) {
                            float height = this.height * (1.0F - this.shrinkY + this.shrinkY * b.fout());
                            float width = this.width * (1.0F - this.shrinkX + this.shrinkX * b.fout());
                            float offset = -90.0F + (this.spin != 0.0F ? Mathf.randomSeed((long)b.id, 360.0F) + b.time * this.spin : 0.0F);
                            Color mix = Tmp.c1.set(this.mixColorFrom).lerp(this.mixColorTo, b.fin());
                            Draw.mixcol(mix, mix.a);
                            Draw.color(this.backColor);
//                                Draw.rect(this.backRegion, b.x, b.y, width, height, b.rotation() + offset);
                            Draw.color(this.frontColor);
//                            Draw.rect(this.frontRegion, b.x, b.y, width, height, b.rotation() + offset);
                            Draw.color(this.backColor,this.frontColor,b.fin());
                            Lines.swirl(b.x,b.y,width/height,90f/360f,b.rotation()-45f);
                            Draw.reset();
                        }
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
                                this.buildingDamageMultiplier = 0.3f;
//                                this.length = 4;
                                this.hittable = true;
                                this.ammoMultiplier = 1;
                                this.backColor= Pal.lancerLaser;
                                this.frontColor=Color.white;
                                this.hitColor=this.trailColor=this.lightColor=this.lightningColor=Color.gray;
                            }
                        },
                        Items.phaseFabric, new SpikeCircleOrbonBullet()
                );
                this.consumes.liquid(Liquids.cryofluid,0.2f).optional(false,true);
                this.requirements(Category.turret, ItemStack.with(ModItems.odinum,400, Items.plastanium,350, Items.silicon,800, Items.titanium,420, Items.metaglass,280));
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
                                this.lightning = 3;//?????????? ??????
                                this.lightningLength = 5;//????? ??????
                                this.lightningLengthRand = 15;//????????? ????? ?? 0 ?? 50 ????? ???????????? ? ????? ??????, ?? ???? ???????????? ?????
                                this.lightningDamage = 12;//???? ??????
                                this.lightningAngle = 5;//???? ??????????? ?????? ???????????? ???? ????
                                this.lightningCone = 45;//???????????? ???? ??????????? ??????
                                this.lightningColor = Color.valueOf("f1fc58");
                            }
                        }
                );
                this.requirements(Category.turret, ItemStack.with(ModItems.odinum, 100, ModItems.graphenite, 200, Items.silicon, 140, Items.metaglass, 70, ModItems.exoticAlloy, 140));
            }
        };
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
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 1000, Items.silicon, 1800, ModItems.chromium, 450, ModItems.odinum, 400));
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
        brain = new ItemTurret("brain") {
            {
                this.localizedName = "Brain";
                this.description = "Fires a beam of death at enemies. Requires Phase Alloy to concentrate energy. Total destruction.";
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 570, Items.silicon, 1200, Items.surgeAlloy, 100, Items.thorium, 720, ModItems.phaseAlloy, 230, ModItems.plastic, 350));
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
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 100, Items.silicon, 2000, Items.surgeAlloy, 100, ModItems.odinum, 1150, ModItems.phaseAlloy, 350, ModItems.plastic, 350));
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
                        this.lightning = 4;//?????????? ??????
                        this.lightningLength = 30;//????? ??????
                        this.lightningLengthRand = 30;//????????? ????? ?? 0 ?? 50 ????? ???????????? ? ????? ??????, ?? ???? ???????????? ?????
                        this.lightningDamage = 78;//???? ??????
                        this.lightningAngle = 15;//???? ??????????? ?????? ???????????? ???? ????
                        this.lightningCone = 50;//???????????? ???? ??????????? ??????
                        this.lightningColor = Color.valueOf("f1fc58");
                    }
                };
                this.requirements(Category.turret, ItemStack.with(ModItems.phaseAlloy, 250, ModItems.exoticAlloy, 400, Items.surgeAlloy, 300, ModItems.chromium, 200, ModItems.odinum, 300, ModItems.graphenite, 420, Items.metaglass, 120));
                this.reloadTime = 4;
            }
        };
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
                this.requirements(Category.turret, ItemStack.with(ModItems.phaseAlloy, 20, Items.silicon, 200, ModItems.odinum, 120));
            }
        };
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
                                this.lightning = 3;//?????????? ??????
                                this.lightningLength = 3;//????? ??????
                                this.lightningLengthRand = 7;//????????? ?????
                                this.lightningDamage = 20;//???? ??????
                                this.lightningAngle = 3;//???? ??????????? ?????? ???????????? ???? ????
                                this.lightningCone = 20;//???????????? ???? ??????????? ??????
                                this.lightningColor = Color.valueOf("f1fc58");
                            }
                        }
                );
                this.requirements(Category.turret, ItemStack.with(Items.silicon, 30, ModItems.graphenite, 20, Items.lead, 60, Items.copper, 70));
            }
        };
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
                        ModItems.exoticAlloy, artilleryExplosive,
                        Items.surgeAlloy, artilleryIncendiary
                );
                this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(true, true);
                this.requirements(Category.turret, ItemStack.with(ModItems.exoticAlloy, 400, ModItems.graphenite, 400, Items.plastanium, 250, Items.silicon, 700, Items.surgeAlloy, 250, ModItems.phaseAlloy, 200));
            }
        };
        neuron = new ItemTurret("neuron") {
            {
                this.localizedName = "Neuron";
                this.description = "A small turret that fires lasers that do splash damage. Requires power aswell as Exotic Alloy to shoot.";
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 250, Items.silicon, 400, Items.surgeAlloy, 70, Items.plastanium, 120, Items.thorium, 350));
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
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 100, Items.thorium, 100, Items.silicon, 180));
            }
        };
        soul = new ItemTurret("soul") {
            {
                this.localizedName = "Soul";
                this.description = "A small, but powerful turret. Requires expensive Surge Alloy to fire. Can literally reap your soul.";
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 340, Items.silicon, 405, Items.surgeAlloy, 100, ModItems.plastic, 270, ModItems.odinum, 420));
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
                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 500, Items.plastanium, 250, Items.silicon, 500, ModItems.odinum, 460, Items.phaseFabric, 360));
            }
        };
        synaps = new ItemTurret("synaps") {
            {
                this.localizedName = "Synaps";
                this.health = 1260;
                this.size = 2;
                this.hasItems = true;
                this.hasLiquids = true;
                this.description = "Electrical Sap turret, can shoot a sap bullet with mane lightnings.";

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

                this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 120, Items.silicon, 140, Items.lead, 190, Items.titanium, 120));
            }
        };

        gem = new LaserTurret("gem") {
            {
                localizedName = "Gem";
                requirements(Category.turret, ItemStack.with(Items.copper, 10));
                size = 14;
                health = 240 * size * size;
                range = 240f;
                this.shootType = new RainbowLaserBulletType(){
                    {
                        length=100f;
                        lifetime=120f;
                    }
                };
                consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.5f)).update(false);
            }
        };

        gloryTurret = new LaserTurret("glory"){{
                this.localizedName = "Synaps";
                this.requirements(Category.turret, ItemStack.with(Items.plastanium, 340, Items.lead, 350, Items.phaseFabric, 260, Items.surgeAlloy, 360, Items.silicon, 390));
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
        exoticAlloyWallLarge = new Wall("exotic-alloy-wall-large") {
            {
                this.localizedName = "Exotic Alloy Wall Large";
                this.description = "A bigger Exotic Alloy wall, creates lightings when shot.";
                this.health = 4060;
                this.size = 2;
                this.requirements(Category.defense, ItemStack.with(ModItems.exoticAlloy, 22, ModItems.graphenite, 8));
                this.lightningChance = 0.09f;
            }
        };
        exoticAlloyWall = new Wall("exotic-alloy-wall") {
            {
                this.localizedName = "Exotic Alloy Wall";
                this.description = "An Exotic Alloy wall, creates lightnings when shot.";
                this.health = 1160;
                this.size = 1;
                this.requirements(Category.defense, ItemStack.with(ModItems.exoticAlloy, 6, ModItems.graphenite, 2));
                this.lightningChance = 0.08f;
            }
        };
        grapheniteWallLarge = new Wall("graphenite-wall-large") {
            {
                this.localizedName = "Large Graphenite Wall";
                this.description = "A bigger version of standard graphenite wall.";
                this.health = 2160;
                this.size = 2;
                this.requirements(Category.defense, ItemStack.with(ModItems.graphenite, 20, Items.silicon, 6));
            }
        };
        grapheniteWall = new Wall("graphenite-wall") {
            {
                this.localizedName = "Graphenite Wall";
                this.description = "A purple, medium strength wall.";
                this.size = 1;
                this.health = 620;
                this.requirements(Category.defense, ItemStack.with(ModItems.graphenite, 6, Items.silicon, 2));

            }
        };
        odinumWallLarge = new Wall("odinum-wall-large") {
            {
                this.localizedName = "Odinum Wall Large";
                this.description = "Bigger Odinum Wall, can deflect bullets.";
                this.health = 3360;
                this.size = 2;
                this.requirements(Category.defense, ItemStack.with(ModItems.odinum, 28));
                this.chanceDeflect = 14;
                this.flashHit = true;
            }
        };
        odinumWall=new Wall("odinum-wall"){
            {
                this.localizedName = "Odinum Wall";
                this.description = "Medium strength wall, a little radioactive.";
                this.size = 1;
                this.health = 1020;
                this.requirements(Category.defense, ItemStack.with(ModItems.odinum,8));
                this.chanceDeflect = 12;
                this.flashHit = true;
            }
        };
        plasticWallLarge = new Wall("plastic-wall-large") {
            {
                this.localizedName = "Plastic Wall Large";
                this.description = "A bigger Plastic wall, can deflect some bullets.";
                this.health = 3820;
                this.size = 2;
                this.requirements(Category.defense, ItemStack.with(ModItems.plastic, 24, Items.metaglass, 10));
                this.insulated = true;
                this.absorbLasers = true;
            }
        };
        plasticWall = new Wall("plastic-wall") {
            {
                this.localizedName = "Plastic Wall";
                this.description = "A Plastic wall, can deflect some bullets.";
                this.health = 980;
                this.size = 1;
                this.requirements(Category.defense, ItemStack.with(ModItems.plastic, 6, Items.metaglass, 4));
                this.insulated = true;
                this.absorbLasers = true;
            }
        };
        //please add description :3 /*Zelaux: NO*/ /*PixaxeOfPixie: YEEAS*/
        largeAstronomicalWall = new ReflectionWall("astronomical-alloy-wall-large") {
            {
                laserReflect=true;
                lightningReflect=true;
                localizedName = "Large Astronomical Wall";
                size = 2;
                health = 4650;
                requirements(Category.defense, ItemStack.with(ModItems.phaseAlloy, 60));
            }
        };

        astronomicalWall = new ReflectionWall("astronomical-alloy-wall") {
            {
                localizedName = "Astronomical Wall";
                size = 1;
                health = 1550;
                requirements(Category.defense, ItemStack.with(ModItems.astroAlloy, 20));
            }
        };
    }
}
