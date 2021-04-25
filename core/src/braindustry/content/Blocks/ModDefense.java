package braindustry.content.Blocks;

import arc.graphics.Color;
import braindustry.content.*;
import braindustry.entities.bullets.ContinuousRainbowLaserBulletType;
import braindustry.entities.bullets.SpikeCircleOrbonBullet;
import braindustry.gen.ModSounds;
import braindustry.graphics.ModPal;
import braindustry.world.blocks.Wall.ReflectionWall;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.*;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LaserTurret;
import mindustry.world.blocks.defense.turrets.PointDefenseTurret;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret;
import mindustry.world.consumers.ConsumeLiquidFilter;

import static braindustry.content.Blocks.ModBlocks.*;
import static mindustry.content.Bullets.*;

class ModDefense implements ContentList {

    @Override
    public void load() {
        rapier = new ItemTurret("rapier") {{
            this.localizedName = "Rapier";
            this.description = "Shots hedgehod-like frag bullets.";
            this.range = 200;
//                this.recoilAmount = 28;
            this.reloadTime = 60;
            this.size = 4;
            this.shots = 4;
            this.health = 2800;
            this.inaccuracy = 0;
            this.shootSound = Sounds.missile;
            this.rotateSpeed = 0.9f;
            this.targetAir = true;
            this.targetGround = true;
            this.ammo(
                    ModItems.graphenite, new SpikeCircleOrbonBullet() {
                        {
                            this.hitEffect = ModFx.circleSpikeHit;
                            this.ammoMultiplier = 4.0f;
                            this.despawnEffect = ModFx.circleSpikeHit;

                            this.smokeEffect = ModFx.spikeSmoke;
                            this.damage = 1300;
                            this.despawnShake = 2.3f;
                            this.hitShake = 4.8f;
                            this.speed = 2.8f;

                            this.absorbable = false;
                            this.reflectable = false;
                            this.hittable = false;
                            this.hitSize = 6.0f;

                            this.status = ModStatusEffects.speedMul.get(5);
                            this.statusDuration = 70.0f * 3.0f;
                        }
                    }
            );
            this.consumes.liquid(Liquids.cryofluid, 0.2f).optional(false, true);
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 400, Items.plastanium, 350, Items.silicon, 800, Items.titanium, 420, Items.metaglass, 280));
        }};
        axon = new ItemTurret("axon") {{
            this.health = 2890;
            this.size = 3;
            this.rotateSpeed = 0.9f;
            this.shots = 6;
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
                            this.damage = 15;
                            this.shootEffect = Fx.railShoot;
                            this.hitColor = this.frontColor = Color.valueOf("f1fc58");
                            this.despawnEffect = Fx.railHit;
                            this.lifetime = 90;
                            this.knockback = 1;
                            this.lightning = 3;//?????????? ??????
                            this.lightningLength = 5;//????? ??????
                            this.lightningLengthRand = 15;//????????? ????? ?? 0 ?? 50 ????? ???????????? ? ????? ??????, ?? ???? ???????????? ?????
                            this.lightningDamage = 9;//???? ??????
                            this.lightningAngle = 5;//???? ??????????? ?????? ???????????? ???? ????
                            this.lightningCone = 45;//???????????? ???? ??????????? ??????
                            this.lightningColor = Color.valueOf("f1fc58");
                        }
                    }
            );
            this.requirements(Category.turret, ItemStack.with(ModItems.odinum, 125, ModItems.graphenite, 200, Items.silicon, 240, Items.metaglass, 210, ModItems.exoticAlloy, 140));
        }};
        blaze = new ItemTurret("blaze") {{
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
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 300, Items.silicon, 340, Items.plastanium, 250, Items.graphite, 240));
            this.ammo(
                    ModItems.graphenite, new ShrapnelBulletType() {
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
                    Items.plastanium, new ShrapnelBulletType() {
                        {
                            this.length = 160;
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
            this.consumes.liquid(ModLiquids.liquidMethane, 0.04f).optional(true, true);
        }};
        brain = new ItemTurret("brain") {{
            this.localizedName = "Brain";
            this.description = "Fires a beam of death at enemies. Requires Phase Alloy to concentrate energy. Total destruction.";
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 470, Items.silicon, 500, Items.surgeAlloy, 220, Items.thorium, 420, ModItems.phaseAlloy, 230, ModItems.plastic, 250));
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
                            this.lightningColor = Color.valueOf("d5b2ed");
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
                            this.lightningColor = Color.valueOf("d5b2ed");
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
        }};
        mind = new ItemTurret("mind") {{
            this.localizedName = "Mind";
            this.description = "An alternative to Brain. Fires a splash-damage beam. Requires lightweight Plastic to shoot.";
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 340, Items.silicon, 500, Items.surgeAlloy, 200, ModItems.odinum, 115, ModItems.phaseAlloy, 250, ModItems.plastic, 250));
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
                            this.length = 260;
                            this.damage = 420;
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
                            this.length = 160;
                            this.damage = 710;
                            this.width = 25;
                            this.serrationLenScl = 8;
                            this.serrationSpaceOffset = 120;
                            this.serrationFadeOffset = 0;
                            this.serrations = 13;
                            this.serrationWidth = 3;
                            this.fromColor = Color.valueOf("FFF6A3");
                            this.toColor = Color.valueOf("FFE70F");
                            this.lifetime = 35;
                            this.shootEffect = Fx.sparkShoot;
                            this.smokeEffect = Fx.sparkShoot;
                        }
                    }
            );
            this.reloadTime = 70;
            this.shots = 1;
            this.burstSpacing = 3;
            this.inaccuracy = 0.2f;
            this.range = 230;
            this.size = 4;
            this.health = 2350;
        }};
        electron = new LaserTurret("electron") {{
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
                    this.damage = 560;
                    this.shootEffect = Fx.railShoot;
                    this.despawnEffect = Fx.railHit;
                    this.knockback = 1;
                    this.lightning = 4;//?????????? ??????
                    this.lightningLength = 20;//????? ??????
                    this.lightningLengthRand = 20;//????????? ????? ?? 0 ?? 50 ????? ???????????? ? ????? ??????, ?? ???? ???????????? ?????
                    this.lightningDamage = 48;//???? ??????
                    this.lightningAngle = 15;//???? ??????????? ?????? ???????????? ???? ????
                    this.lightningCone = 50;//???????????? ???? ??????????? ??????
                    this.lightningColor = Color.valueOf("f1fc58");
                }
            };
            this.requirements(Category.turret, ItemStack.with(ModItems.phaseAlloy, 550, ModItems.exoticAlloy, 600, Items.surgeAlloy, 450, ModItems.chromium, 420, ModItems.graphenite, 1020, Items.metaglass, 420));
            this.reloadTime = 4;
        }};
        fragment = new PointDefenseTurret("fragment") {{
            this.localizedName = "Fragment";
            this.description = "Upgraded \"Segment\", consumes more power as well Refrigerant for cooling.";
            this.size = 3;
            this.health = 410;
            this.range = 250;
            this.hasPower = true;
            this.shootLength = 14;
            this.bulletDamage = 32;
            this.reloadTime = 5;
            this.consumes.power(4.7f);
            this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.04f).optional(false, false);
            this.requirements(Category.turret, ItemStack.with(ModItems.phaseAlloy, 70, Items.silicon, 180, ModItems.odinum, 150));
        }};
        impulse = new ItemTurret("impulse") {{
            this.localizedName = "Impulse";
            this.health = 840;
            this.size = 1;
            this.rotateSpeed = 1.8f;
            this.shots = 1;
            this.reloadTime = 30;
            this.hasItems = true;
            shootLength = -6;
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
                            this.damage = 19;
                            this.shootEffect = Fx.shockwave;
                            this.hitColor = this.frontColor = Color.valueOf("f1fc58");
                            this.despawnEffect = Fx.hitLancer;
                            this.lifetime = 60;
                            this.knockback = 1;
                            this.lightning = 3;
                            this.lightningLength = 3;
                            this.lightningLengthRand = 7;
                            this.lightningDamage = 9;
                            this.lightningAngle = 3;
                            this.lightningCone = 20;
                            this.lightningColor = Color.valueOf("f1fc58");
                        }
                    }
            );
            this.requirements(Category.turret, ItemStack.with(Items.silicon, 70, ModItems.graphenite, 50, Items.lead, 90, Items.copper, 110));
        }};
        katana = new ItemTurret("katana") {{
            this.localizedName = "Katana";
            this.description = "Strong artillery turret with powerful ammo. Requires a strong alloy to fire. Better in groups.";
            this.range = 300;
            this.recoilAmount = 6;
            this.reloadTime = 17;
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
            this.consumes.liquid(ModLiquids.thoriumRefrigerant, 0.14f).optional(true, true);
            this.requirements(Category.turret, ItemStack.with(ModItems.exoticAlloy, 120, ModItems.graphenite, 330, Items.plastanium, 150, Items.silicon, 200, Items.surgeAlloy, 50));
        }};
        neuron = new ItemTurret("neuron") {{
            this.localizedName = "Neuron";
            this.description = "A small turret that fires lasers that do splash damage. Requires power aswell as Exotic Alloy to shoot.";
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 150, Items.silicon, 150, Items.surgeAlloy, 70, Items.plastanium, 120, Items.thorium, 270));
            this.ammo(
                    ModItems.exoticAlloy, new PointBulletType() {
                        {
                            this.shootEffect = Fx.instShoot;
                            this.hitEffect = Fx.instHit;
                            this.smokeEffect = Fx.smokeCloud;
                            this.trailEffect = Fx.instTrail;
                            this.despawnEffect = Fx.instBomb;
                            this.trailSpacing = 12;
                            this.damage = 400;
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
            this.consumes.power(3f);
            this.consumes.liquid(Liquids.cryofluid, 0.14f).optional(false, false);
            this.reloadTime = 35;
            this.shots = 1;
            this.burstSpacing = 6;
            this.inaccuracy = 1;
            this.range = 190;
            this.size = 2;
            this.health = 1100;
        }};
        perlin = new TractorBeamTurret("perlin") {{
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
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 130, Items.thorium, 160, Items.silicon, 180));
        }};
        soul = new ItemTurret("soul") {{
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
        }};
        stinger = new ItemTurret("stinger") {{
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
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 500, Items.plastanium, 250, Items.silicon, 420, ModItems.odinum, 360, Items.phaseFabric, 260));
        }};
        synaps = new ItemTurret("synaps") {{
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
                            this.length = 75;
                            this.damage = 47;
                            this.shootEffect = Fx.railShoot;
                            this.hitColor = this.color = Color.valueOf("fbff9e");
                            this.despawnEffect = Fx.railHit;
                            this.width = 1.4f;
                            this.lifetime = 35;
                            this.knockback = -1;
                            this.lightning = 6;
                            this.lightningLength = 2;
                            this.lightningLengthRand = 10;
                            this.lightningDamage = 9;
                            this.lightningAngle = 6;
                            this.lightningCone = 12;
//                                this.largeHit = true;
                            this.lightColor = this.lightningColor = Color.valueOf("fbff9e");
                        }
                    }
            );

            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 120, Items.silicon, 140, Items.lead, 190, Items.titanium, 120));
        }};

        gem = new LaserTurret("gem") {{
            localizedName = "Gem";
            requirements(Category.turret, ItemStack.with(ModItems.chloroAlloy, 480, ModItems.phaseAlloy, 960, ModItems.graphenite, 2250, Items.silicon, 2230, Items.phaseFabric, 830));
            size = 14;
            health = 240 * size * size;
            range = 240f;
            reloadTime = 210;
            shootSound = ModSounds.electronShoot;
            loopSound = ModSounds.electronCharge;
            firingMoveFract = 0.4f;
            shootLength = -8;
            this.shootType = new ContinuousRainbowLaserBulletType() {{
                damage = 860f;
                length = 400f;
                lifetime = 120f;
                width = 30f;
                hittable = false;
                absorbable = false;
                hitEffect = Fx.hitMeltHeal;
                //    hitColor = colors[2];
                despawnEffect = ModFx.curseEffect;
                shootEffect = Fx.hitMeltdown;
                smokeEffect = Fx.rocketSmokeLarge;
            }};
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.5f)).update(false);
        }};
        shinigami = new ItemTurret("shinigami") {{
            float brange = range = 880f;
            localizedName = "Shinigami";
            description = "Railgun turret to defense from higher tiers of units, consumes Dense Composite to shoot, don't use this turret to attack buildings. ";
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 3000, Items.metaglass, 800, ModItems.phaseAlloy, 400, Items.plastanium, 900, Items.silicon, 2400));
            ammo(
                    ModItems.phaseAlloy, new PointBulletType() {{
                        shootEffect = Fx.lightningShoot;
                        hitEffect = Fx.instHit;
                        smokeEffect = Fx.reactorsmoke;
                        trailEffect = ModFx.shinigamiTrail;
                        despawnEffect = ModFx.instBomb;
                        trailSpacing = 25f;
                        damage = 20480;
                        buildingDamageMultiplier = 0.2f;
                        speed = brange;
                        hitShake = 9f;
                        ammoMultiplier = 2f;
                    }}
            );
            maxAmmo = 40;
            ammoPerShot = 10;
            rotateSpeed = 1.5f;
            reloadTime = 8000f;
            ammoUseEffect = Fx.casing3Double;
            recoilAmount = 10f;
            restitution = 0.009f;
            cooldown = 0.01f;
            shootShake = 6f;
            shots = 1;
            size = 8;
            shootCone = 3f;
            shootSound = Sounds.railgun;
            unitSort = (u, x, y) -> -u.maxHealth;
            coolantMultiplier = 0.8f;
            health = 260 * size * size;
            coolantUsage = 1.1f;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 10f)).update(false);
            consumes.powerCond(24f, TurretBuild::isActive);
        }};

        voidwave = new ItemTurret("void-wave") {{
            this.localizedName = "Void Wave";
            this.description = "";
            this.range = 660;
//                this.recoilAmount = 28;
            this.reloadTime = 220;
            this.size = 8;
            this.shots = 10;
            this.health = 12000;
            this.inaccuracy = 10f;
            this.shootSound = Sounds.plasmaboom;
            this.rotateSpeed = 0.4f;
            this.targetAir = true;
            this.targetGround = true;
            this.ammo(
                    ModItems.plastic, new BasicBulletType() {
                        {
                            this.damage = 1260;
                            this.width = 15;
                            this.height = 18;
                            this.shrinkY = 0.1f;
                            this.shrinkX = 0.2f;
                            this.speed = 4;
                            this.hitSize = 50;
                            this.lifetime = 180;
                            this.status = StatusEffects.slow;
                            this.statusDuration = 120;
                            //  this.bulletSprite = wave-shell;
                            this.pierce = true;
                            this.buildingDamageMultiplier = 0.6f;
                            //   this.length = 4;
                            this.hittable = true;
                            this.ammoMultiplier = 1;
                            trailChance = 92f;
                            trailEffect = ModFx.fireworkTrail;
                            this.backColor = ModPal.blackHoleLaserBackColor;
                            this.frontColor = ModPal.blackHoleLaserColor;
                            this.hitColor = this.trailColor = this.lightColor = this.lightningColor = Color.violet;
                        }
                    }
            );
            this.consumes.liquid(ModLiquids.liquidMethane, 0.1f).optional(false, true);
            this.requirements(Category.turret, ItemStack.with(ModItems.graphenite, 1000, Items.titanium, 600, ModItems.phaseAlloy, 400, Items.phaseFabric, 100, Items.silicon, 1400));
        }};
        spark = new ItemTurret("spark"){
            {
                localizedName = "Spark";
                description = "Upgraded Salvo with using high-tec Graphenite";
                requirements(Category.turret, ItemStack.with(Items.copper, 130, Items.graphite, 90, Items.silicon, 70, ModItems.graphenite, 80));
                ammo(
                        ModItems.graphenite, new BasicBulletType() {
                            {
                                this.damage = 78;
                                this.width = 10;
                                this.height = 11;
                                this.shrinkY = 0.1f;
                                this.shrinkX = 0.2f;
                                this.speed = 4.1f;
                                this.hitSize = 10;
                                this.lifetime = 190;
                                this.status = StatusEffects.shocked;
                                this.statusDuration = 60;
                                //  this.bulletSprite = wave-shell;
                                this.pierce = true;
                                this.buildingDamageMultiplier = 0.8f;
                                //   this.length = 4;
                                this.hittable = true;
                                this.ammoMultiplier = 1;
                                trailChance = 1.2f;
                                trailEffect = Fx.missileTrail;
                                this.backColor = ModPal.amethyst;
                                this.frontColor = ModPal.amethystLight;
                                this.hitColor = this.trailColor = this.lightColor = this.lightningColor = Color.violet;
                            }
                        },
                        Items.copper, new BasicBulletType() {
                            {
                                this.damage = 27;
                                this.width = 8;
                                this.height = 9;
                                this.shrinkY = 0.1f;
                                this.shrinkX = 0.2f;
                                this.speed = 3.1f;
                                this.hitSize = 11;
                                this.lifetime = 180;
                                this.status = StatusEffects.shocked;
                                this.statusDuration = 40;
                                //  this.bulletSprite = wave-shell;
                                this.pierce = true;
                                this.buildingDamageMultiplier = 0.8f;
                                //   this.length = 4;
                                this.hittable = true;
                                this.ammoMultiplier = 1;
                                trailChance = 1.2f;
                                trailEffect = Fx.missileTrail;
                                this.backColor = ModPal.orangeBackColor;
                                this.frontColor = ModPal.orangeFrontColor;
                                this.hitColor = this.trailColor = this.lightColor = this.lightningColor = Color.orange;
                            }
                        },
                        Items.titanium, new BasicBulletType() {
                            {
                                this.damage = 38;
                                this.width = 9;
                                this.height = 10;
                                this.shrinkY = 0.1f;
                                this.shrinkX = 0.2f;
                                this.speed = 3.6f;
                                this.hitSize = 10;
                                this.lifetime = 190;
                                this.status = StatusEffects.shocked;
                                this.statusDuration = 50;
                                //  this.bulletSprite = wave-shell;
                                this.pierce = true;
                                this.buildingDamageMultiplier = 0.9f;
                                //   this.length = 4;
                                this.hittable = true;
                                this.ammoMultiplier = 1;
                                trailChance = 1.2f;
                                trailEffect = Fx.missileTrail;
                                this.backColor = ModPal.NorthernLightsNoiseColor;
                                this.frontColor = ModPal.NorthernLightsColor;
                                this.hitColor = this.trailColor = this.lightColor = this.lightningColor = Color.cyan;
                            }
                        }
                );

                size = 3;
                range = 180f;
                reloadTime = 42f;
                restitution = 0.04f;
                ammoEjectBack = 3.2f;
                cooldown = 0.08f;
                recoilAmount = 3.2f;
                shootShake = 1f;
                burstSpacing = 2f;
                shots = 5;
                ammoUseEffect = ModFx.napalmShoot;
                health = 360 * size * size;
                shootSound = Sounds.bang;
            }};
        exoticAlloyWallLarge = new Wall("dense-composite-wall-large") {{
            this.localizedName = "Dense Composite Wall Large";
            this.description = "A bigger Dense Composite Wall, creates lightings when shot.";
            this.health = 4060;
            this.size = 2;
            this.requirements(Category.defense, ItemStack.with(ModItems.phaseAlloy, 22));
            this.lightningChance = 0.24f;
        }};
        exoticAlloyWall = new Wall("dense-composite-wall") {{
            this.localizedName = "Dense Composite Wall";
            this.description = "An Dense Composite Wall, creates lightnings when shot.";
            this.health = 1160;
            this.size = 1;
            this.requirements(Category.defense, ItemStack.with(ModItems.phaseAlloy, 6));
            this.lightningChance = 0.2f;
        }};
        grapheniteWallLarge = new Wall("graphenite-wall-large") {{
            this.localizedName = "Large Graphenite Wall";
            this.description = "A bigger version of standard graphenite wall.";
            this.health = 2160;
            this.size = 2;
            this.requirements(Category.defense, ItemStack.with(ModItems.graphenite, 20, Items.silicon, 6));
        }};
        grapheniteWall = new Wall("graphenite-wall") {{
            this.localizedName = "Graphenite Wall";
            this.description = "A purple, medium strength wall.";
            this.size = 1;
            this.health = 620;
            this.requirements(Category.defense, ItemStack.with(ModItems.graphenite, 6, Items.silicon, 2));

        }};
        odinumWallLarge = new Wall("odinum-wall-large") {{
            this.localizedName = "Odinum Wall Large";
            this.description = "Bigger Odinum Wall, can deflect bullets.";
            this.health = 2860;
            this.size = 2;
            this.requirements(Category.defense, ItemStack.with(ModItems.odinum, 28));
            this.chanceDeflect = 14;
            this.flashHit = true;
        }};
        odinumWall = new Wall("odinum-wall") {{
            this.localizedName = "Odinum Wall";
            this.description = "Medium strength wall, a little radioactive.";
            this.size = 1;
            this.health = 620;
            this.requirements(Category.defense, ItemStack.with(ModItems.odinum, 8));
            this.chanceDeflect = 12;
            this.flashHit = true;
        }};
        plasticWallLarge = new Wall("plastic-wall-large") {{
            this.localizedName = "Plastic Wall Large";
            this.description = "A bigger Plastic wall, can deflect some bullets.";
            this.health = 3820;
            this.size = 2;
            this.requirements(Category.defense, ItemStack.with(ModItems.plastic, 24, Items.metaglass, 10));
            this.insulated = true;
            this.absorbLasers = true;
        }};
        plasticWall = new Wall("plastic-wall") {{
            this.localizedName = "Plastic Wall";
            this.description = "A Plastic wall, can deflect some bullets.";
            this.health = 980;
            this.size = 1;
            this.requirements(Category.defense, ItemStack.with(ModItems.plastic, 6, Items.metaglass, 4));
            this.insulated = true;
            this.absorbLasers = true;
        }};
        //please add description :3 /*Zelaux: NO*/ /*PixaxeOfPixie: YEEAS*/
        largeChloroWall = new ReflectionWall("chloro-wall-large") {{
            laserReflect = true;
            lightningReflect = true;
            localizedName = "Large Chloro Wall";
            description = "Big organic wall with ability to reflect enemy lasers.";
            size = 2;
            health = 5450;
            requirements(Category.defense, ItemStack.with(ModItems.chloroAlloy, 60));
        }};

        chloroWall = new ReflectionWall("chloro-wall") {{
            localizedName = "Chloro Wall";
            description = "Strong organic wall with ability to reflect enemy lasers.";
            size = 1;
            laserReflect = true;
            lightningReflect = true;
            health = 2010;
            requirements(Category.defense, ItemStack.with(ModItems.chloroAlloy, 20));
        }};
    }
}
