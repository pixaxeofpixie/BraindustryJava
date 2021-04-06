package braindustry.content;

import arc.func.Prov;
import arc.graphics.Color;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.ai.types.StealthGroundAI;
import braindustry.entities.abilities.BlackHoleReactorAbility;
import braindustry.entities.abilities.ImpactReactorAbility;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import braindustry.entities.bullets.AdamBulletType;
import braindustry.entities.bullets.EveBulletType;
import braindustry.entities.bullets.LilithBulletType;
import braindustry.gen.ModSounds;
import braindustry.gen.StealthMechUnit;
import braindustry.graphics.ModPal;
import braindustry.type.ModUnitType;
import braindustry.type.ModWeapon;
import braindustry.type.StealthUnitType;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.ctype.ContentList;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.abilities.UnitSpawnAbility;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.AmmoTypes;
import mindustry.world.meta.BlockFlag;


public class ModUnitTypes implements ContentList {
    public static ModUnitType
            //spider
            aquila, aries, ibis, lacerta, capra,
            //air
            shield, chainmail, chestplate, broadsword, armor,
            //naval
            lyra, cenda, tropsy, venti, vyvna,
            //stealth
            tyzen, kryox, intelix, nemesis, maverix,
            //tx
            griffon, moray, litix, penumbra;


    public ModUnitTypes() {
        UnitTypes.class.isArray();
    }

    @Override
    public void load() {
        //====spiders
        ibis = new ModUnitType("ibis") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 60.0F;
                this.localizedName = "Blast";
                this.description = "Small spider unit with high speed and firework guns.";
                this.health = 210;
                this.speed = 0.56f;
                this.mechSideSway = 0.25f;
                this.hitSize = 5;
                this.rotateSpeed = 4;
                this.armor = 0.5f;
                this.hovering = true;
                this.landShake = 0.1f;
                this.commandLimit = 8;
                this.legCount = 4;
                this.legLength = 8;
                this.legTrns = 0.8f;
                this.legMoveSpace = 1.5f;
                this.legBaseOffset = 2;
                this.buildSpeed = 0.8f;
                this.allowLegStep = true;
                this.visualElevation = 75f;
                this.ammoType = AmmoTypes.powerHigh;
                this.mechStepShake = 0.15f;
                this.mechStepParticles = true;
                this.speed = 1;
                this.weapons.add(
                        new ModWeapon("ibis-weapon") {
                            {
                                this.x = 5;
                                this.y = -1.5f;
                                this.shootY = 3;
                                this.reload = 13;
                                this.ejectEffect = Fx.none;
                                this.recoil = 1;
                                this.shots = 4;
                                this.shotDelay = 2;
                                this.rotate = false;
                                this.shootSound = Sounds.laser;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.8f;
                                        this.length = 30;
                                        this.damage = 27;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("8efff0");
                                        this.color = Color.valueOf("8efff0");
                                        this.despawnEffect = Fx.none;
                                        this.width = 0.54f;
                                        this.lifetime = 15;
                                        this.knockback = 1.24f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        aries = new ModUnitType("aries") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 60.0F;
                this.localizedName = "Firecracker";
                this.description = "A small 4-leg unit with strong firework sap guns.";
                this.speed = 0.6f;
                this.drag = 0.3f;
                this.hitSize = 8;
                this.targetAir = false;
                this.hovering = true;
                this.health = 800;
                this.legCount = 4;
                this.legMoveSpace = 1;
                this.legPairOffset = 2;
                this.legLength = 7;
                this.legExtension = -4f;
                this.legBaseOffset = 5;
                this.landShake = 0.1f;
                this.legSpeed = 0.2f;
                this.legLengthScl = 1;
                this.rippleScale = 2;
                this.legSplashDamage = 32;
                this.legSplashRange = 30;
                this.allowLegStep = true;
                this.visualElevation = 75f;
                this.immunities.addAll(StatusEffects.burning);
                this.weapons.add(
                        new ModWeapon("ibis-weapon") {
                            {
                                this.x = 4;
                                this.y = -1.5f;
                                this.shootY = 3;
                                this.reload = 30;
                                this.shots = 15;
                                this.shotDelay = 2;
                                this.inaccuracy = 9;
                                this.ejectEffect = Fx.none;
                                this.recoil = 1;
                                this.rotate = true;
                                this.shootSound = Sounds.laser;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.95f;
                                        this.length = 50;
                                        this.damage = 35;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("8efff0");
                                        this.color = Color.valueOf("8efff0");
                                        this.despawnEffect = Fx.none;
                                        this.width = 0.7f;
                                        this.lifetime = 35;
                                        this.knockback = 1.34f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        capra = new ModUnitType("capra") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 68.0F;
                this.localizedName = "Irascibility";
                this.itemCapacity = 200;
                this.speed = 0.4f;
                this.drag = 0.4f;
                this.hitSize = 12;
                this.rotateSpeed = 3;
                this.health = 3600;
                this.hovering = true;
                this.immunities.addAll(StatusEffects.burning, StatusEffects.melting);
                this.legCount = 4;
                this.legLength = 15;
                this.legTrns = 0.8f;
                this.legMoveSpace = 1.5f;
                this.legBaseOffset = 2;
                this.armor = 5;
                this.buildSpeed = 0.8f;
                this.allowLegStep = true;
                this.visualElevation = 75f;
                this.weapons.add(
                        new ModWeapon("capra-weapon") {
                            {
                                this.reload = 90;
                                this.rotate = true;
                                this.rotateSpeed = 1.2f;
                                this.x = 13;
                                this.y = -7f;
                                this.shots = 22;
                                this.shotDelay = 2;
                                this.inaccuracy = 14;
                                this.mirror = true;
                                this.shootSound = Sounds.laser;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.6f;
                                        this.length = 110;
                                        this.damage = 68;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("8efff0");
                                        this.color = Color.valueOf("8efff0");
                                        this.despawnEffect = Fx.none;
                                        this.width = 0.7f;
                                        this.lifetime = 20;
                                        this.knockback = -0.65f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        lacerta = new ModUnitType("lacerta") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 75.0F;
                this.localizedName = "Salute";
                this.description = "A giant unit with 6 legs and super-powerful laser with lightnings!";
                this.boostMultiplier = 2.1f;
                this.speed = 0.35f;
                this.drag = 0.4f;
                this.hitSize = 26;
                this.rotateSpeed = 1.6f;
                this.health = 9000;
                this.armor = 7;
                this.hovering = true;
                this.commandLimit = 8;
                this.legCount = 4;
                this.legLength = 19;
                this.legTrns = 1;
                this.legMoveSpace = 3;
                this.legBaseOffset = 2;
                this.allowLegStep = true;
                this.visualElevation = 75f;
                this.mechStepShake = 0.15f;
                this.mechStepParticles = true;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                this.weapons.add(
                        new ModWeapon("lacerta-weapon") {
                            {
                                this.x = 0;
                                this.y = -5f;
                                this.shootY = 6;
                                this.reload = 600;
                                this.shots = 5;
                                this.shotDelay = 20;
                                this.inaccuracy = 10;
                                this.ejectEffect = Fx.none;
                                this.recoil = 20;
                                this.shootStatus = StatusEffects.unmoving;
                                this.shootStatusDuration = 300;
                                this.firstShotDelay = 70;
                                this.shootSound = Sounds.explosionbig;
                                this.mirror = false;
                                this.bullet = new LaserBulletType() {
                                    {
                                        this.length = 430;
                                        this.damage = 340;
                                        this.width = 60;
                                        this.lifetime = 60;
                                        this.lightningSpacing = 40;
                                        this.lightningLength = 12;
                                        this.lightningDelay = 1;
                                        this.lightningLengthRand = 21;
                                        this.lightningDamage = 65;
                                        this.lightningAngleRand = 30;
                                        this.largeHit = true;
                                        this.shootEffect = ModFx.lacertaLaserCharge;
                                        this.healPercent = 20;
                                        this.collidesTeam = true;
                                        this.sideAngle = 15;
                                        this.sideWidth = 0;
                                        this.sideLength = 0;
                                        this.lightningColor = Color.valueOf("8cffcd");
                                        this.colors = new Color[]{Color.valueOf("f78cff"), Color.valueOf("f78cff"), Color.valueOf("f78cff")};
                                    }
                                };
                            }
                        },
                        new ModWeapon("capra-weapon") {
                            {
                                this.reload = 160;
                                this.rotate = true;
                                this.rotateSpeed = 1.6f;
                                this.x = 12;
                                this.y = -7f;
                                this.shots = 16;
                                this.inaccuracy = 8;
                                this.shotDelay = 2;
                                this.mirror = true;
                                this.shootSound = Sounds.laserblast;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.7f;
                                        this.length = 135;
                                        this.damage = 40;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("8efff0");
                                        this.color = Color.valueOf("8efff0");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1.0f;
                                        this.lifetime = 25;
                                        this.knockback = -0.65f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        aquila = new ModUnitType("aquila") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 75.0F;
                this.localizedName = "Firework";
                this.description = "Fires a fireworks and big laser.";
                this.drag = 0.1f;
                this.speed = 0.5f;
                this.hitSize = 48;
                this.hovering = true;
                this.health = 17000;
                this.rotateSpeed = 2;
                this.legCount = 8;
                this.legMoveSpace = 0.8f;
                this.legPairOffset = 3;
                this.legLength = 75f;
                this.legExtension = -20;
                this.legBaseOffset = 8f;
                this.landShake = 1f;
                this.legSpeed = 0.1f;
                this.rippleScale = 3;
                this.legSplashDamage = 340;
                this.legSplashRange = 40;
                this.allowLegStep = true;
                this.visualElevation = 75f;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                this.weapons.add(
                        new ModWeapon("aquila-equip1") {
                            {
                                this.reload = 60;
                                this.rotate = true;
                                this.x = 25;
                                this.y = 5;
                                this.rotateSpeed = 1.5f;
                                this.shots = 6;
                                this.shotDelay = 7;
                                this.inaccuracy = 12;
                                this.mirror = true;
                                this.shootSound = Sounds.beam;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.8f;
                                        this.length = 180;
                                        this.damage = 270;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1.25f;
                                        this.lifetime = 20;
                                        this.knockback = -0.65f;
                                    }
                                };
                            }
                        },
                        new ModWeapon("aquila-equip2") {
                            {
                                this.reload = 75;
                                this.rotate = true;
                                this.rotateSpeed = 0.9f;
                                this.x = 13;
                                this.y = -7f;
                                this.shots = 20;
                                this.shotDelay = 0.7f;
                                this.inaccuracy = 13;
                                this.mirror = true;
                                this.shootSound = Sounds.beam;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.7f;
                                        this.length = 180;
                                        this.damage = 90;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("8efff0");
                                        this.color = Color.valueOf("8efff0");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1.15f;
                                        this.lifetime = 20;
                                        this.knockback = -1f;
                                    }
                                };
                            }
                        },
                        new ModWeapon("lacerta-weapon") {
                            {
                                this.x = 0;
                                this.y = 2;
                                this.shootY = -1f;
                                this.reload = 500;
                                this.ejectEffect = Fx.none;
                                this.recoil = 0;
                                this.shots = 3;
                                this.shotDelay = 5;
                                this.inaccuracy = 5;
                                this.shootStatus = StatusEffects.unmoving;
                                this.shootStatusDuration = 130;
                                this.firstShotDelay = 20;
                                this.shootSound = Sounds.lasershoot;
                                this.mirror = false;
                                this.bullet = new LaserBulletType() {
                                    {
                                        this.length = 380;
                                        this.damage = 400;
                                        this.width = 90;
                                        this.lifetime = 60;
                                        this.lightningSpacing = 30;
                                        this.lightningLength = 10;
                                        this.lightningDelay = 1.2f;
                                        this.lightningLengthRand = 16;
                                        this.lightningDamage = 55;
                                        this.lightningAngleRand = 24;
                                        this.largeHit = true;
                                        this.shootEffect = ModFx.lacertaLaserCharge;
                                        this.healPercent = 12;
                                        this.collidesTeam = true;
                                        this.sideAngle = 18;
                                        this.sideWidth = 0;
                                        this.sideLength = 0;
                                        this.lightningColor = Color.valueOf("8cffcd");
                                        this.colors = new Color[]{Color.valueOf("f78cff"), Color.valueOf("f78cff"), Color.valueOf("f78cff")};
                                    }
                                };
                            }
                        }
                );
            }
        };
        //TODO: fly
        armor = new ModUnitType("armor") {
            {
                this.constructor = Types.payload;
                this.localizedName = "Myphros";
                this.speed = 0.9f;
                this.flying = true;
                this.health = 210;
                this.range = 70;
                this.armor = 6;
                this.engineOffset = 3;
                this.engineSize = 2;
                this.rotateSpeed = 10;
                this.targetAir = true;
                this.weapons.add(
                        new ModWeapon("armor-weapon") {
                            {
                                this.x = 0;
                                this.y = -3f;
                                this.shootY = 6;
                                this.reload = 20;
                                this.ejectEffect = Fx.none;
                                this.recoil = 1;
                                this.shootSound = Sounds.laserblast;
                                this.rotate = true;
                                this.mirror = false;
                                this.bullet = new BasicBulletType(){{
                                    this.damage = 20;
                                    this.speed = 14.3f;
                                    this.width = 9f;
                                    this.height = 18f;
                                    this.lifetime = 15f;
                                    this.shootEffect = Fx.shootBig;
                                    this.lightning = 2;
                                    this.lightningLength = 6;
                                    this.lightningColor = Pal.surge;
                                    this.lightningDamage = 6;
                                    }
                                };
                            }
                        }
                );
            }
        };
        shield = new ModUnitType("shield") {
            {
                this.constructor = Types.payload;
                this.localizedName = "Nocta";
                this.speed = 0.8f;
                this.flying = true;
                this.health = 660;
                this.armor = 10;
                this.range = 90;
                this.engineOffset = 6;
                this.rotateSpeed = 3;
                abilities.add(new RepairFieldAbility(4f, 60f * 5, 40f), new ForceFieldAbility(40f, 5f, 200f, 60f * 8));
                this.weapons.add(
                        new ModWeapon("armor-weapon") {
                            {
                                this.top = true;
                                this.x = 5;
                                this.y = -4f;
                                this.shootY = 4;
                                this.reload = 40;
                                this.shots = 2;
                                this.inaccuracy = 1;
                                this.shotDelay = 5;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.shootSound = Sounds.laserblast;
                                this.rotate = true;
                                this.mirror = true;
                                this.bullet = new BasicBulletType(){{
                                        this.damage = 60;
                                        this.speed = 13.4f;
                                        this.pierce = true;
                                        this.pierceCap = 2;
                                        this.width = 14f;
                                        this.height = 33f;
                                        this.lifetime = 25f;
                                        this.shootEffect = Fx.shootBig;
                                    }
                                };
                            }
                        }
                );
            }
        };
        chestplate = new ModUnitType("chestplate") {
            {
                this.localizedName = "Zenum";
                this.description = "A big defense unit with sap laser guns.";
                this.constructor = Types.payload;
                this.speed = 0.6f;
                this.flying = true;
                this.hitSize = 12;
                this.engineSize = 3.2f;
                this.armor = 18;
                this.health = 1600;
                this.range = 110;
                this.engineOffset = 7;
                this.rotateSpeed = 1.1f;
                this.targetAir = true;
                this.payloadCapacity = 240;
                this.buildSpeed = 1.6f;
                abilities.add(new RepairFieldAbility(5f, 60f * 5, 45f), new ForceFieldAbility(65f, 5f, 480f, 60f * 7));
                this.weapons.add(
                        new ModWeapon("shield-weapon") {
                            {
                                this.reload = 40;
                                this.mirror = true;
                                this.shots = 2;
                                this.inaccuracy = 5;
                                this.x = 7;
                                this.y = 1;
                                this.rotate = false;
                                this.shootSound = Sounds.boom;
                                this.bullet = Bullets.missileExplosive;
                            }
                        },
                        new ModWeapon("chainmail-weapon") {
                            {
                                this.top = true;
                                this.x = 0;
                                this.y = -8f;
                                this.shootY = -1f;
                                this.reload = 120;
                                this.shots = 5;
                                this.shotDelay = 8;
                                this.inaccuracy = 7;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.shootSound = Sounds.laserblast;
                                this.rotate = true;
                                this.mirror = false;
                                 this.bullet = new BasicBulletType(){{
                                    this.damage = 60;
                                    this.speed = 12.6f;
                                    this.pierce = true;
                                    this.pierceCap = 3;
                                    this.width = 10f;
                                    this.height = 17f;
                                    this.lifetime = 30f;
                                    this.shootEffect = Fx.shootBig;
                                    this.lightning = 4;
                                    this.lightningLength = 6;
                                    this.lightningColor = Pal.surge;
                                    this.lightningDamage = 8;
                                    }};
                                    }
                                });
            }
        };
        chainmail = new ModUnitType("chainmail") {
            {
                this.localizedName = "Dent";
                this.constructor = Types.payload;
                this.speed = 0.8f;
                this.flying = true;
                this.hitSize = 27;
                this.engineSize = 7;
                this.armor = 29;
                this.health = 6000;
                this.rotateSpeed = 1.2f;
                this.targetAir = true;
                this.payloadCapacity = 300;
                this.buildSpeed = 1.3f;
                this.rotateShooting = false;
                this.commandLimit = 6;
                abilities.add(new RepairFieldAbility(6f, 60f * 5, 60f), new ForceFieldAbility(90f, 5f, 1300f, 60f * 7));
                this.weapons.add(
                        new ModWeapon("bomb-weapon") {
                            {
                                this.x = 0;
                                this.y = -12f;
                                this.mirror = false;
                                this.reload = 75;
                                this.minShootVelocity = 0.02f;
                                this.shootSound = Sounds.plasmadrop;
                                this.bullet = new BasicBulletType() {
                                    {
                                        this.width = 60;
                                        this.height = 60;
                                        this.maxRange = 30;
                                        //this.shootCone = 190;
                                        this.despawnShake = 3;
                                        this.collidesAir = false;
                                        this.lifetime = 0;
                                        this.despawnEffect = ModFx.yellowBomb;
                                        this.hitEffect = Fx.massiveExplosion;
                                        this.hitSound = Sounds.plasmaboom;
                                        this.keepVelocity = false;
                                        this.spin = 1;
                                        this.shrinkX = 0.6f;
                                        this.shrinkY = 0.6f;
                                        this.speed = 0.002f;
                                        this.collides = false;
                                        this.healPercent = 15;
                                        this.splashDamage = 450;
                                        this.splashDamageRadius = 110;
                                    }
                                };
                            }
                        },
                        new ModWeapon("chainmail-weapon") {
                            {
                                this.top = true;
                                this.y = -3f;
                                this.x = 14;
                                this.reload = 120;
                                this.recoil = 4;
                                this.shotDelay = 9;
                                this.shootSound = Sounds.sap;
                                this.shots = 7;
                                this.inaccuracy = 6.5f;
                                this.velocityRnd = 0.2f;
                                this.alternate = true;
                                this.mirror = true;
                                this.rotate = true;
                                this.rotateSpeed = 1f;
                                this.bullet = new BasicBulletType(){{
                                        this.damage = 90;
                                        this.speed = 13.8f;
                                        this.pierce = true;
                                        this.pierceCap = 6;
                                        this.lightning = 3;
                                        this.lightningLength = 4;
                                        this.lightningColor = Pal.surge;
                                        this.lightningDamage = 11;
                                        this.width = 14f;
                                        this.height = 33f;
                                        this.lifetime = 35f;
                                        this.shootEffect = Fx.shootBig;
                                    }
                                };
                            }
                        }
                );
            }
        };
        broadsword = new ModUnitType("broadsword") {
            {
                this.constructor = Types.payload;
                this.localizedName = "Quix";
                this.description = "A colossal unit with ability to bombard, repair, defend, transport other units and shoot lasers.";
                this.armor = 38;
                this.health = 11000;
                this.speed = 0.5f;
                this.rotateSpeed = 1;
                this.accel = 0.07f;
                this.drag = 0.02f;
                this.flying = true;
                this.lowAltitude = true;
                this.commandLimit = 8;
                this.engineOffset = 38;
                this.engineSize = 10;
                this.rotateShooting = false;
                this.hitSize = 64;
                this.payloadCapacity = 380;
                this.buildSpeed = 5;
                this.range = 140;
                abilities.add(new RepairFieldAbility(7f, 60f * 4, 50f), new ForceFieldAbility(150f, 4f, 3000f, 60f * 7));
                this.weapons.add(
                         new ModWeapon("bomb-weapon") {
                            {
                                this.x = 12;
                                this.y = -30f;
                                this.mirror = true;
                                this.reload = 50;
                                this.minShootVelocity = 0.02f;
                                this.soundPitchMin = 1;
                                this.shootSound = Sounds.plasmadrop;
                                this.bullet = new BasicBulletType() {
                                    {
                                        this.width = 45;
                                        this.height = 45;
                                        this.maxRange = 40;
                                        //this.ignoreRotation = true;
                                        this.hitSound = Sounds.plasmaboom;
                                        //this.shootCone = 160;
                                        this.despawnShake = 2.1f;
                                        this.collidesAir = false;
                                        this.lifetime = 0;
                                        this.despawnEffect = ModFx.yellowBomb;
                                        this.hitEffect = Fx.massiveExplosion;
                                        this.keepVelocity = false;
                                        this.spin = 1;
                                        this.shrinkY = 0.5f;
                                        this.shrinkX = 0.5f;
                                        this.speed = 0.004f;
                                        this.collides = false;
                                        this.healPercent = 10;
                                        this.splashDamage = 400;
                                        this.splashDamageRadius = 110;
                                    }
                                };
                            }
                        },
                        new ModWeapon("broadsword-weapon") {
                            {
                                this.y = -1f;
                                this.x = 25;
                                this.reload = 40;                                
                                this.recoil = 3;
                                this.rotate = true;
                                this.shootSound = Sounds.sap;
                                this.shots = 7;
                                this.inaccuracy = 5;
                                this.velocityRnd = 0.1f;
                                this.alternate = true;
                                this.mirror = true;
                                this.bullet = new BasicBulletType(){{
                                        this.damage = 102;
                                        this.speed = 11.4f;
                                        this.pierce = true;
                                        this.pierceCap = 16;
                                        this.width = 14f;
                                        this.height = 33f;
                                        this.lifetime = 25f;
                                        this.shootEffect = Fx.shootBig;
                                        this.lightning = 3;
                                        this.lightningLength = 4;
                                        this.lightningColor = Pal.surge;
                                        this.lightningDamage = 11;
                                        this.width = 14f;
                                        this.height = 33f;
                                        this.lifetime = 40f;
                                        this.shootEffect = Fx.shootBig;
                                    }
                                };
                            }
                        }
                );
            }
        };

        //TODO: water
        venti = new ModUnitType("venti") {
            {
                this.localizedName = "Stenella";
                this.description = "First naval unit, has powerful shrapnel gun but needs support.";
                this.constructor = Types.naval;
                this.health = 270;
                this.speed = 1.2f;
                this.drag = 0.17f;
                this.hitSize = 12;
                this.armor = 4;
                this.accel = 0.3f;
                this.rotateSpeed = 2.3f;
                this.rotateShooting = true;
                this.trailLength = 25;
                this.trailX = 6;
                this.trailY = -5f;
                this.trailScl = 2.1f;
                this.weapons.add(
                        new ModWeapon("lyra-weapon") {
                            {
                                this.x = 0;
                                this.y = 0;
                                this.shootY = -1f;
                                this.reload = 32;
                                this.ejectEffect = Fx.none;
                                this.recoil = 0;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.bullet = new ShrapnelBulletType() {
                                    {
                                        this.length = 90;
                                        this.damage = 50;
                                        this.width = 28;
                                        this.lifetime = 10;
                                        this.serrationLenScl = 1;
                                        this.serrationSpaceOffset = 1;
                                        this.serrationFadeOffset = 0;
                                        this.serrations = 3;
                                        this.serrationWidth = 5;
                                        this.fromColor = Color.valueOf("dbf3ff");
                                        this.toColor = Color.valueOf("00ffff");
                                        this.shootEffect = Fx.sparkShoot;
                                        this.smokeEffect = Fx.sparkShoot;
                                    }
                                };
                            }
                        }
                );
            }
        };
        lyra = new ModUnitType("lyra") {
            {
                this.localizedName = "Marginata";
                this.description = "T2 sea unit with shrapnel lasers and rocketguns, good at attacking opponent's base, but needs support.";
                this.constructor = Types.naval;
                this.health = 750;
                this.speed = 0.8f;
                this.drag = 0.18f;
                this.hitSize = 17;
                this.armor = 8;
                this.accel = 0.3f;
                this.rotateSpeed = 2;
                this.rotateShooting = true;
                this.trailLength = 24;
                this.trailX = 7;
                this.trailY = -9f;
                this.trailScl = 1.6f;
                this.weapons.add(
                        new ModWeapon("venti-weapon") {
                            {
                                this.x = 0;
                                this.y = 0;
                                this.shootY = -1f;
                                this.reload = 35;
                                this.ejectEffect = Fx.none;
                                this.recoil = 0;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.bullet = new ShrapnelBulletType() {
                                    {
                                        this.length = 70;
                                        this.damage = 100;
                                        this.width = 24;
                                        this.serrationLenScl = 6;
                                        this.serrationSpaceOffset = 4;
                                        this.serrationFadeOffset = 0;
                                        this.serrations = 6;
                                        this.serrationWidth = 4;
                                        this.lifetime = 40;
                                        this.fromColor = Color.valueOf("dbf3ff");
                                        this.toColor = Color.valueOf("00ffff");
                                        this.shootEffect = Fx.sapExplosion;
                                        this.smokeEffect = Fx.melting;
                                    }
                                };
                            }
                        },
                        new ModWeapon("lyra-weapon") {
                            {
                                this.x = 5;
                                this.y = -1f;
                                this.shootY = -1f;
                                this.reload = 30;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.shots = 1;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.bullet = new MissileBulletType() {
                                    {
                                        this.width = 10;
                                        this.height = 20;
                                        this.shrinkY = 0.1f;
                                        this.speed = 2.0f;
                                        this.splashDamageRadius = 20;
                                        this.splashDamage = 45;
                                        this.lifetime = 45;
                                        this.hitEffect = Fx.blastExplosion;
                                        //this.fromColor = Color.valueOf("dbf3ff");
                                        //this.toColor = Color.valueOf("00ffff");
                                        this.shootEffect = Fx.plasticExplosion;
                                        this.smokeEffect = Fx.melting;
                                    }
                                };
                            }
                        }
                );
            }
        };
        tropsy = new ModUnitType("tropsy") {
            {
                this.localizedName = "Glacialis";
                this.description = "An enlarged and improved unit created on the basis of Lyra unit.";
                this.constructor = Types.naval;
                this.health = 3920;
                this.speed = 0.51f;
                this.accel = 0.16f;
                this.rotateSpeed = 1.6f;
                this.drag = 0.18f;
                this.hitSize = 19;
                this.armor = 8;
                this.rotateShooting = false;
                this.trailLength = 26;
                this.trailX = 8;
                this.trailY = 11;
                this.trailScl = 1.8f;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                this.weapons.add(
                        new ModWeapon("cenda-weapon3") {
                            {
                                this.reload = 60;
                                this.x = 0;
                                this.y = -6f;
                                this.shadow = 7;
                                this.rotateSpeed = 0.7f;
                                this.rotate = true;
                                this.shots = 6;
                                this.shotDelay = 15;
                                this.inaccuracy = 4;
                                this.velocityRnd = 0.14f;
                                this.shootSound = Sounds.missile;
                                this.bullet = new AdamBulletType() {
                                    {
                                        this.width = 14;
                                        this.height = 14f;
                                        this.shrinkY = 0.3f;
                                        this.speed = 6.7f;
                                        this.drag = 0.05f;
                                        this.splashDamageRadius = 50;
                                        this.splashDamage = 36;
                                        this.makeFire = false;
                                        this.lightningDamage = 9;
                                        this.lightning = 5;
                                        this.lightningLength = 7;
                                        this.lifetime = 120;
                                        this.weaveScale = 8;
                                        this.weaveMag = 1;
                                    }
                                };
                            }
                        },
                        new ModWeapon("lyra-weapon") {
                            {
                                this.x = 7;
                                this.y = 4;
                                this.shootY = -1f;
                                this.reload = 30;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet = new ShrapnelBulletType() {
                                    {
                                        this.length = 80;
                                        this.damage = 142;
                                        this.width = 32;
                                        this.lifetime = 35;
                                        this.serrationLenScl = 1;
                                        this.serrationSpaceOffset = 1;
                                        this.serrationFadeOffset = 0;
                                        this.serrations = 6;
                                        this.serrationWidth = 32;
                                        this.fromColor = Color.valueOf("995ce8");
                                        this.toColor = Color.valueOf("00ffff");
                                        this.shootEffect = Fx.sparkShoot;
                                        this.smokeEffect = Fx.sparkShoot;
                                    }
                                };
                            }
                        }
                );
            }
        };
        cenda = new ModUnitType("cenda") {
            {
                this.localizedName = "Orca";
                this.description = "A large and heavy attacking naval unit built on the proven \"Glacialis\" design, but with improvements in everything.";
                this.constructor = Types.naval;
                this.health = 9300;
                this.speed = 0.6f;
                this.accel = 0.13f;
                this.rotateSpeed = 1.3f;
                this.drag = 0.23f;
                this.hitSize = 38;
                this.armor = 10;
                this.rotateShooting = false;
                this.trailLength = 43;
                this.trailX = 12;
                this.trailY = 18;
                this.trailScl = 2.8f;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                this.weapons.add(
                        new ModWeapon("cenda-weapon") {
                            {
                                this.reload = 35;
                                this.x = 14;
                                this.y = -17f;
                                this.shadow = 8;
                                this.rotateSpeed = 0.5f;
                                this.rotate = true;
                                this.shots = 9;
                                this.shotDelay = 15;
                                this.inaccuracy = 1;
                                this.velocityRnd = 0.1f;
                                this.shootSound = Sounds.missile;
                                this.bullet = new AdamBulletType() {
                                    {
                                        this.width = 15;
                                        this.height = 21;
                                        this.shrinkY = 0.3f;
                                        this.speed = 2.9f;
                                        this.drag = -0.01f;
                                        this.splashDamageRadius = 30;
                                        this.splashDamage = 42;
                                        this.homingPower = 0.1f;
                                        this.lightningDamage = 6;
                                        this.lightning = 6;
                                        this.lightningLength = 5;
                                        this.makeFire = true;
                                        this.lifetime = 85;
                                        this.weaveScale = 3;
                                        this.weaveMag = 6;
                                    }
                                };
                            }
                        },
                        new ModWeapon("cenda-weapon2") {
                            {
                                this.x = 9;
                                this.y = 7;
                                this.shootY = -1f;
                                this.reload = 50;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.rotate = true;
                                this.rotateSpeed = 1.8f;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet = new ShrapnelBulletType() {
                                    {
                                        this.length = 130;
                                        this.damage = 222;
                                        this.width = 58;
                                        this.lifetime = 20;
                                        this.serrationLenScl = 2;
                                        this.serrationSpaceOffset = 1;
                                        this.serrationFadeOffset = 0;
                                        this.serrations = 16;
                                        this.serrationWidth = 51;
                                        this.fromColor = Color.valueOf("995ce8");
                                        this.toColor = Color.valueOf("00ffff");
                                        this.shootEffect = Fx.sparkShoot;
                                        this.smokeEffect = Fx.sparkShoot;
                                    }
                                };
                            }
                        }
                );
            }
        };
        vyvna = new ModUnitType("vyvna") {

            {
                this.range = 18;
                this.constructor = Types.naval;
                this.localizedName = "Physala";
                this.description = "Giant atomic cruiser, produce energy and fires from railguns and rocket launchers.";
                this.health = 21900;
                this.speed = 0.6f;
                this.accel = 0.1f;
                this.rotateSpeed = 1.0f;
                this.drag = 0.23f;
                this.hitSize = 72.0F;
                this.armor = 10;
                this.rotateShooting = false;
                this.trailLength = 180;
                this.trailX = 28;
                this.trailY = 23;
                this.trailScl = 2f;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                int spawnTime = 1550;
                abilities.addAll(new ImpactReactorAbility(this, 18f, 20, Integer.MAX_VALUE), new UnitSpawnAbility(ModUnitTypes.shield, spawnTime, 19.25f, -31.75f), new UnitSpawnAbility(ModUnitTypes.shield, spawnTime, -19.25f, -31.75f));
                int brange = 1;


                this.weapons.add(
                        new ModWeapon("cenda-weapon") {
                            {
                                this.reload = 60;
                                this.x = 27;
                                this.y = 5f;
                                this.shadow = 15;
                                this.rotateSpeed = 0.4f;
                                this.rotate = true;
                                this.mirror = true;
                                this.shots = 6;
                                this.shotDelay = 10;
                                this.inaccuracy = 1.5f;
                                this.velocityRnd = 0.5f;
                                this.shootSound = Sounds.missile;
                                this.bullet = new EveBulletType() {
                                    {
                                        this.width = 15;
                                        this.height = 25;
                                        this.shrinkY = 0.1f;
                                        this.speed = 2.5f;
                                        this.drag = 0f;
                                        this.splashDamageRadius = 20;
                                        this.splashDamage = 110;
                                        this.homingPower = 0.5f;
                                        this.lightningDamage = 10;
                                        this.lightning = 4;
                                        this.lightningLength = 10;
                                        this.lifetime = 100;
                                        this.weaveScale = 1;
                                        this.weaveMag = 3;
                                    }
                                };
                            }
                        },
                        new ModWeapon("cenda-weapon3") {
                            {
                                this.reload = 80;
                                this.x = 14;
                                this.y = -4f;
                                this.shadow = 12;
                                this.rotateSpeed = 1.3f;
                                this.rotate = true;
                                this.shots = 4;
                                this.shotDelay = 5;
                                this.inaccuracy = 0.7f;
                                this.velocityRnd = 0.1f;
                                this.shootSound = Sounds.missile;
                                this.bullet = new AdamBulletType() {
                                    {
                                        this.width = 15;
                                        this.height = 15;
                                        this.shrinkY = 0.1f;
                                        this.speed = 3.0f;
                                        this.drag = 0.01f;
                                        this.splashDamageRadius = 10f;
                                        this.splashDamage = 60f;
                                        this.homingPower = 0.2f;
                                        this.lightningDamage = 6f;
                                        this.lightning = 8;
                                        this.lightningLength = 5;
                                        this.lifetime = 85f;
                                        this.weaveScale = 3f;
                                        this.weaveMag = 6f;
                                    }
                                };
                            }
                        },
                        new ModWeapon("vyvna-weapon") {
                            {
                                this.x = 0f;
                                this.y = -30f;
                                this.shootY = -3f;
                                this.reload = 125f;
                                this.ejectEffect = Fx.none;
                                this.recoil = 5f;
                                this.rotate = true;
                                this.shadow = 50;
                                this.mirror = false;
                                this.rotateSpeed = 0.5f;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                /*this.bullet = new PointBulletType() {
                                    {
                                        this.shootEffect = ModFx.instShoot;
                                        this.hitEffect = ModFx.instHit;
                                        this.smokeEffect = Fx.smokeCloud;
                                        this.trailEffect = ModFx.instTrail;
                                        this.despawnEffect = ModFx.instBomb;
                                        this.trailSpacing = 15f;
                                        this.damage = 1240;
                                        this.buildingDamageMultiplier = 0.4f;
                                        this.speed = brange;
                                        this.hitShake = 7f;
                                    }
                                };*/
                                this.bullet = new RailBulletType() {
                                    {
                                        this.shootEffect = ModFx.instShoot;
                                        this.length = 420.0F;
                                        this.updateEffectSeg = 60.0F;
                                        this.pierceEffect = this.hitEffect = ModFx.instHit;
                                        this.updateEffect = this.trailEffect = ModFx.instTrail;
                                        this.hitEffect = Fx.massiveExplosion;
                                        this.smokeEffect = Fx.shootBig2;
                                        this.damage = 1250.0F;
                                        this.pierceDamageFactor = 0.5F;
                                        this.despawnEffect = ModFx.instBomb;
                                        this.buildingDamageMultiplier = 0.4f;
                                        this.speed = brange;
                                        this.hitShake = 7f;
                                    }
                                };
                            }
                        }
                );
            }
        };
        //TODO: stealthMech
        tyzen = new StealthUnitType("tyzen") {
            {
                this.mineSpeed = 8.0F;
                this.mineTier = 2;
                stealthDuration = 2f * 60f;
                stealthCooldown = 2f * 60f;
                this.constructor = Types.stealthMech;
                this.defaultController = StealthGroundAI::new;
                this.speed = 0.9f;
                this.hitSize = 8;
                this.armor = 4;
                this.buildSpeed = 1.0F;
                this.health = 310;
                this.localizedName = "Tyzen";
                this.description = " Weak unit with Stealth ability.";

                this.weapons.add(
                        new ModWeapon("tyzen-weapon") {
                            {
                                this.x = 8;
                                this.y = 0;
                                this.shootY = -1f;
                                this.reload = 20f;
                                this.ejectEffect = Fx.burning;
                                this.recoil = 1;
                                this.shots = 2;
                                this.rotate = false;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet = new ShrapnelBulletType() {
                                    {
                                        this.length = 58.0F;
                                        this.damage = 50.0F;
                                        this.width = 17.0F;
                                        this.lifetime = 20.0F;
                                        this.serrationLenScl = 2.0F;
                                        this.serrationSpaceOffset = 1.0F;
                                        this.serrationFadeOffset = 0.0F;
                                        this.serrations = 3;
                                        this.serrationWidth = 51.0F;
                                        this.fromColor = Color.valueOf("625b82");
                                        this.toColor = Color.valueOf("d4c7ea");
                                        this.shootEffect = Fx.sparkShoot;
                                        this.smokeEffect = Fx.sparkShoot;
                                    }
                                };
                            }
                        }
                );
            }
        };
        kryox = new StealthUnitType("kryox") {
            {
                this.mineSpeed = 9.0F;
                this.mineTier = 2;
                stealthDuration = 3f * 60f;
                stealthCooldown = 2.5f * 60f;
                this.constructor = Types.stealthMech;
                this.defaultController = StealthGroundAI::new;
                this.speed = 0.63f;
                this.rotateSpeed = 4;
                this.hitSize = 14;
                this.armor = 6;
                this.buildSpeed = 1.4F;
                this.health = 1300;
                this.localizedName = "Kryox";
                this.description = "Upgraded Tyzen with magma guns and Stealth ability.";

                this.weapons.add(
                        new ModWeapon("kryox-weapon") {
                            {
                                this.x = 11;
                                this.y = 0;
                                this.shootY = -1f;
                                this.reload = 34;
                                this.ejectEffect = Fx.fireballsmoke;
                                this.recoil = 2f;
                                this.shots = 4;
                                this.inaccuracy = 9.0f;
                                this.rotate = false;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet = new LiquidBulletType(ModLiquids.magma) {
                                    {
                                        damage = 57;
                                        speed = 2.1f;
                                        drag = -0.01f;
                                        shootEffect = Fx.lightningShoot;
                                        lifetime = 85f;
                                        collidesAir = false;
                                    }
                                };
                            }
                        }
                );
            }
        };
        intelix = new StealthUnitType("intelix") {
            {
                this.mineSpeed = 8.5F;
                this.mineTier = 3;
                stealthDuration = 4f * 60f;
                stealthCooldown = 5f * 60f;
                this.constructor = Types.stealthMech;
                this.defaultController = StealthGroundAI::new;
                this.speed = 0.55f;
                this.rotateSpeed = 3;
                this.hitSize = 20;
                this.armor = 8;
                this.buildSpeed = 1.5F;
                this.health = 4100;
                this.canDrown = false;
                int brange = 1;
                this.localizedName = "Intelix";
                this.description = "Heavy stealth unit with dangerous railgun.";

                this.weapons.add(
                        new ModWeapon("troplex-grinder2") {
                            {
                                this.x = 0;
                                this.y = -5;
                                this.shootY = -1f;
                                this.reload = 90;
                                this.ejectEffect = ModFx.magicShootEffect;
                                this.recoil = 4;
                                this.shootSound = Sounds.laser;
                                this.rotate = true;
                                this.rotateSpeed = 0.8f;
                                this.mirror = false;
                                this.bullet = new RailBulletType() {
                                    {
                                        this.shootEffect = ModFx.energyShrapnelShoot;
                                        this.length = 240.0F;
                                        this.updateEffectSeg = 40.0F;
                                        this.pierceEffect = this.hitEffect = ModFx.instHit;
                                        this.updateEffect = this.trailEffect = ModFx.instTrail;
                                        this.hitEffect = Fx.blastExplosion;
                                        this.smokeEffect = Fx.shootBig2;
                                        this.damage = 590.0F;
                                        this.pierceDamageFactor = 0.45F;
                                        this.despawnEffect = ModFx.instBomb;
                                        this.buildingDamageMultiplier = 0.65f;
                                        this.speed = brange;
                                        this.hitShake = 3f;
                                    }
                                };
                            }
                        }
                );
            }
        };
        nemesis = new StealthUnitType("nemesis") {
            {
                this.mineSpeed = 9F;
                this.mineTier = 3;
                stealthDuration = 5f * 60f;
                stealthCooldown = 6f * 60f;
                this.constructor = Types.stealthMech;
                this.defaultController = StealthGroundAI::new;
                this.speed = 0.45f;
                this.rotateSpeed = 3;
                this.hitSize = 32;
                this.armor = 11;
                this.buildSpeed = 1.8F;
                this.health = 12400;
                this.canDrown = false;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                int brange = 1;
                this.localizedName = "Nemesis";
                this.description = "Slow and dangerous sniper unit with railguns.";

                this.weapons.add(
                        new ModWeapon("troplex-grinder") {
                            {
                                this.x = -17;
                                this.y = 4;
                                this.shootY = -1f;
                                this.reload = 90;
                                this.ejectEffect = ModFx.magicShootEffectBig;
                                this.recoil = 3;
                                this.shootSound = Sounds.laser;
                                this.rotate = false;
                                this.mirror = true;
                                this.bullet = new RailBulletType() {
                                    {
                                        this.shootEffect = ModFx.energyShrapnelShoot;
                                        this.length = 390.0F;
                                        this.updateEffectSeg = 65.0F;
                                        this.pierceEffect = this.hitEffect = ModFx.instHit;
                                        this.updateEffect = this.trailEffect = ModFx.instTrail;
                                        this.hitEffect = Fx.blastExplosion;
                                        this.smokeEffect = Fx.shootBig2;
                                        this.damage = 1100.0F;
                                        this.pierceDamageFactor = 0.85F;
                                        this.despawnEffect = ModFx.instBomb;
                                        this.buildingDamageMultiplier = 1.3f;
                                        this.speed = brange;
                                        this.hitShake = 6f;
                                    }
                                };
                            }
                        }
                );
            }
        };
        maverix = new StealthUnitType("maverix") {
            {
                this.mineSpeed = 9F;
                this.mineTier = 4;
                stealthDuration = 7f * 60f;
                stealthCooldown = 3f * 60f;
                this.constructor = Types.stealthMech;
                this.defaultController = StealthGroundAI::new;
                this.speed = 0.62f;
                this.rotateSpeed = 3;
                this.hitSize = 38;
                this.armor = 14;
                this.buildSpeed = 2F;
                this.health = 18200;
                this.canDrown = false;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                int brange = 1;
                this.localizedName = "Maverix";
                this.description = "Heavy attack unit with magma guns and rampage railgun that mostly effective to buildings.";

                this.weapons.add(
                        new ModWeapon("troplex-grinder-3") {
                            {
                                this.x = 0;
                                this.y = -8;
                                this.shootY = -1f;
                                this.reload = 90;
                                this.ejectEffect = ModFx.magicShootEffectBig;
                                this.recoil = 6;
                                this.shootSound = Sounds.laserblast;
                                this.rotate = true;
                                this.rotateSpeed = 0.7f;
                                this.mirror = false;
                                this.bullet = new RailBulletType() {
                                    {
                                        this.shootEffect = ModFx.energyShrapnelShoot;
                                        this.length = 370.0F;
                                        this.updateEffectSeg = 70.0F;
                                        this.pierceEffect = this.hitEffect = ModFx.gemLaserHit;
                                        this.updateEffect = this.trailEffect = ModFx.instTrail;
                                        this.hitEffect = Fx.blastExplosion;
                                        this.smokeEffect = Fx.shootBig2;
                                        this.damage = 1790.0F;
                                        this.pierceDamageFactor = 1.2F;
                                        this.despawnEffect = ModFx.instBomb;
                                        this.buildingDamageMultiplier = 1.6f;
                                        this.speed = brange;
                                        this.hitShake = 5f;
                                    }
                                };
                            }
                        },
                        new ModWeapon("maverix-weapon") {
                            {
                                this.x = 22;
                                this.y = 0;
                                this.shootY = -1f;
                                this.reload = 50;
                                this.ejectEffect = Fx.fireballsmoke;
                                this.recoil = 3f;
                                this.shots = 7;
                                this.inaccuracy = 16.0f;
                                this.rotate = false;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet = new LiquidBulletType(ModLiquids.magma) {
                                    {
                                        damage = 98;
                                        speed = 1.9f;
                                        drag = -0.01f;
                                        shootEffect = Fx.lightningShoot;
                                        lifetime = 95f;
                                        collidesAir = true;
                                    }
                                };
                            }
                        }
                );
            }
        };
        //TODO: TX
        griffon = new ModUnitType("griffon") {
            {

                this.constructor = Types.legs;
                this.groundLayer = 60.0F;
                this.localizedName = "Griffon";
                this.description = "Ground unit with high characteristics of armor and damage, shoot an electric laser and frag bullets.";
                this.health = 46000;
                this.speed = 0.4f;
                this.mechSideSway = 0.25f;
                this.hitSize = 108;
                this.rotateSpeed = 0.7f;
                this.armor = 14f;
                this.hovering = true;
                this.commandLimit = 6;
                this.legCount = 4;
                this.legMoveSpace = 1.1f;
                this.legPairOffset = 4;
                this.legLength = 48;
                this.legExtension = 8;
                this.legBaseOffset = 2;
                this.landShake = 2.1f;
                this.legSpeed = 0.8f;
                this.legLengthScl = 1f;
                // this.buildSpeed = 0.8f;
                this.allowLegStep = true;
                this.visualElevation = 75f;
                this.ammoType = AmmoTypes.powerHigh;
                this.mechStepShake = 0.25f;
                this.mechStepParticles = true;
//                this.mechStepParticles = true;
                this.immunities = ObjectSet.with(StatusEffects.burning);
                this.weapons.add(
                        new ModWeapon("griffon-weapon") {
                            {
                                mirror = false;
                                top = false;
                                shake = 5.5f;
                                shootY = 15f;
                                x = y = 0f;

                                firstShotDelay = ModFx.yellowLaserCharge.lifetime - 1f;

                                reload = 440f;
                                recoil = 0f;
                                chargeSound = ModSounds.electronCharge;
                                shootSound = ModSounds.electronShoot;
                                continuous = true;
                                cooldownTime = 280f;
                                this.bullet = new ContinuousLaserBulletType() {
                                    public void update(Bullet b) {
                                        SubBullets.addLightning(b, this);
                                        super.update(b);
                                    }

                                    {
                                        this.hitSize = 14;
                                        this.drawSize = 480;
                                        this.width = 17;
                                        this.length = 380;
                                        this.lifetime = 220;
                                        this.hitEffect = Fx.hitMeltHeal;
                                        this.largeHit = true;
                                        //this.hitColor = Color.valueOf("f1fc58");
                                        this.incendAmount = 4;
                                        this.incendSpread = 10;
                                        this.incendChance = 0.7f;
                                        //this.lightColor = Color.valueOf("fbffcc");
                                        this.keepVelocity = true;
                                        this.collides = true;
                                        this.pierce = true;
                                        this.hittable = true;
                                        this.absorbable = true;
                                        this.damage = 248;
                                        this.shootEffect = ModFx.yellowLaserCharge;
                                        this.despawnEffect = ModFx.energyShrapnelSmoke;
                                        this.knockback = 1;
                                        this.healPercent = 18f;
                                        this.collidesTeam = true;
                                        this.lightning = 4;
                                        this.lightningLength = 20;
                                        this.lightningLengthRand = 20;
                                        this.lightningDamage = 42;
                                        this.lightningAngle = 15;
                                        this.lightningCone = 30;
                                        this.lightningColor = Color.valueOf("f1fc58");
                                        colors = new Color[]{ModPal.dendriteYellow.cpy().a(2.5f), ModPal.dendriteYellow.cpy().a(.4f), ModPal.dendriteYellow.cpy().mul(2f), Color.white};
                                    }
                                };
                            }
                        },
                        new ModWeapon("griffon-cannon") {
                            {
                                y = -14f;
                                x = 18f;
                                shootY = 22f;
                                mirror = true;
                                reload = 40;
                                shake = 10f;
                                recoil = 10f;
                                rotateSpeed = 1.2f;
                                ejectEffect = Fx.casing3;
                                shootSound = Sounds.artillery;
                                rotate = true;
                                shadow = 30f;

                                bullet = new ArtilleryBulletType(3.5f, 90) {{
                                    hitEffect = Fx.blastExplosion;
                                    knockback = 0.9f;
                                    lifetime = 100f;
                                    width = height = 25f;
                                    collidesTiles = collides = true;
                                    ammoMultiplier = 4f;
                                    splashDamageRadius = 55f;
                                    splashDamage = 220f;
                                    backColor = Pal.plastaniumBack;
                                    frontColor = lightningColor = Pal.plastanium;
                                    lightning = 18;
                                    lightningLength = 9;
                                    smokeEffect = Fx.shootBigSmoke2;
                                    hitShake = 10f;

                                    status = StatusEffects.shocked;
                                    statusDuration = 70f * 10;

                                    fragLifeMin = 0.6f;
                                    fragBullets = 9;

                                    fragBullet = new ArtilleryBulletType(2.3f, 30) {{
                                        hitEffect = Fx.railHit;
                                        knockback = 0.7f;
                                        lifetime = 85f;
                                        width = height = 17f;
                                        collidesTiles = false;
                                        splashDamageRadius = 30f;
                                        splashDamage = 110f;
                                        backColor = Pal.plastaniumFront;
                                        frontColor = lightningColor = Pal.bulletYellow;
                                        lightning = 9;
                                        lightningLength = 4;
                                        smokeEffect = Fx.shootBigSmoke2;
                                        hitShake = 6f;

                                        status = StatusEffects.shocked;
                                        statusDuration = 60f * 7;
                                    }};
                                }};
                            }
                        }
                );
            }
        };
        moray = new ModUnitType("moray") {
            {
                this.range = 620;
                this.constructor = Types.naval;
                this.localizedName = "Moray";
                this.description = "Naval terror with Black Hole Reactor, built-in unit factories, lasers and rocket launchers.";
                this.health = 62000;
                this.speed = 0.6f;
                this.accel = 0.12f;
                this.rotateSpeed = 0.9f;
                this.drag = 0.35f;
                this.hitSize = 124.0F;
                this.armor = 12;
                this.rotateShooting = false;
                this.trailLength = 240;
                this.trailX = 36;
                this.trailY = 28;
                this.trailScl = 0.9f;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting, StatusEffects.freezing, StatusEffects.corroded);
                float spawnTime = 2000;

                abilities.addAll(
                        new BlackHoleReactorAbility(this, 30f, 35, Integer.MAX_VALUE, 14.0f, new Vec2(-16.25f, 0)),
                        new UnitSpawnAbility(ModUnitTypes.armor, spawnTime, 22.25f, -45.75f),
                        new UnitSpawnAbility(ModUnitTypes.armor, spawnTime, -22.25f, -45.75f),
                        new UnitSpawnAbility(ModUnitTypes.venti, spawnTime, 36.25f, -48.75f),
                        new UnitSpawnAbility(ModUnitTypes.venti, spawnTime, -36.25f, -48.75f)
                );
                int brange = 1;


                this.weapons.add(
                        new ModWeapon("moray-rocket-launcher") {
                            {
                                this.reload = 70;
                                this.x = 14;
                                this.y = -18f;
                                this.shadow = 9;
                                this.rotateSpeed = 0.6f;
                                this.rotate = true;
                                this.shots = 2;
                                this.shotDelay = 20;
                                this.inaccuracy = 0.3f;
                                this.velocityRnd = 0.1f;
                                this.shootSound = Sounds.missile;
                                this.bullet = new LilithBulletType() {
                                };
                            }
                        },

                        new ModWeapon("moray-artillery") {{
                            top = true;
                            y = 4f;
                            x = 29f;
                            reload = 20f;
                            recoil = 7f;
                            shots = 3;
                            inaccuracy = 10.0f;
                            shake = 3;
                            rotate = true;
                            rotateSpeed = 1.3f;
                            mirror = true;
                            ejectEffect = ModFx.foxShoot;
                            shootSound = Sounds.artillery;
                            bullet = new ArtilleryBulletType(3f, 28, "shell") {
                                {
                                    hitEffect = ModFx.adamExplosion;
                                    knockback = 1.2f;
                                    lifetime = 110f;
                                    width = 19f;
                                    height = 21f;
                                    collides = true;
                                    collidesTiles = true;
                                    splashDamageRadius = 28f;
                                    splashDamage = 120f;
                                    backColor = ModPal.unitOrange;
                                    frontColor = ModPal.unitOrangeLight;
                                }
                            };
                        }},
                        new ModWeapon("moray-laser-weapon") {{
                            mirror = false;
                            top = true;
                            shake = 6f;
                            shootY = 28f;
                            x = 0f;
                            y = -9f;
                            rotate = true;
                            rotateSpeed = 0.9f;

                            firstShotDelay = ModFx.blackHoleLaserCharge.lifetime - 1f;

                            reload = 440f;
                            recoil = 0f;
                            chargeSound = ModSounds.electronCharge;
                            shootSound = ModSounds.electronShoot;
                            continuous = true;
                            cooldownTime = 210f;

                            bullet = new ContinuousLaserBulletType() {
                                {
                                    damage = 238f;
                                    length = 410f;
                                    hitEffect = Fx.hitMeltHeal;
                                    drawSize = 450f;
                                    lifetime = 880f;
                                    shake = 1f;
                                    despawnEffect = Fx.smokeCloud;
                                    smokeEffect = Fx.none;

                                    shootEffect = ModFx.thunderShoot;

                                    incendChance = 0.08f;
                                    incendSpread = 5f;
                                    incendAmount = 1;

                                    //constant healing
                                    healPercent = 15f;
                                    collidesTeam = true;

                                    colors = new Color[]{ModPal.blackHoleLaserColor.cpy().a(2.4f), ModPal.blackHoleLaserColor.cpy().a(.4f), ModPal.blackHoleLaserColor.cpy().mul(1.9f), Color.white};
                                }
                            };
                        }},
                        new ModWeapon("moray-voidwave") {{
                            top = true;
                            y = 7f;
                            x = 23f;
                            reload = 10f;
                            recoil = 6f;
                            shots = 1;
                            inaccuracy = 1.0f;
                            shake = 2;
                            rotate = true;
                            rotateSpeed = 0.9f;
                            mirror = true;
                            ejectEffect = ModFx.litixShoot;
                            shootSound = Sounds.missile;
                            bullet = new BasicBulletType(){
                                {
                                    this.damage = 260;
                                    this.width = 15;
                                    this.height = 16;
                                    //this.shrinkY = 0.1f;
                                    this.shrinkX = 0.3f;
                                    this.speed = 4;
                                    this.hitSize = 15;
                                    this.lifetime = 180;
                                    this.status = StatusEffects.slow;
                                    this.statusDuration = 120;
                                    //  this.bulletSprite = wave-shell;
                                    this.pierce = true;
                                    this.width = 1;
                                    this.buildingDamageMultiplier = 0.6f;
                                    //   this.length = 4;
                                    this.hittable = true;
                                    this.ammoMultiplier = 1;
                                    trailChance=100f;
                                    trailEffect=ModFx.fireworkTrail;
                                    this.backColor = ModPal.blackHoleLaserBackColor;
                                    this.frontColor = ModPal.blackHoleLaserColor;
                                    this.hitColor = this.trailColor=this.lightColor=this.lightningColor=Color.violet;
                                }
                            };
                        }}
                );
            }
        };
        litix = new StealthUnitType("litix") {
            {
                this.mineSpeed = 13F;
                this.mineTier = 5;
                stealthDuration = 15f * 60f;
                stealthCooldown = 10f * 60f;
                abilities.add(new OrbitalPlatformAbility(4, 3f,
                                new ModWeapon("litix-methane-shooter") {
                                    {
                                        this.x = 0;
                                        this.y = 0;
                                        this.shootY = -1f;
                                        this.reload = 50;
                                        this.ejectEffect = Fx.fireballsmoke;
                                        this.recoil = 3f;
                                        this.shots = 7;
                                        this.inaccuracy = 10.0f;
                                        this.rotate = true;
                                        this.shootSound = Sounds.flame;
                                        this.alternate = true;
                                        this.bullet = new LiquidBulletType(ModLiquids.liquidMethane) {
                                            {
                                                damage = 98;
                                                speed = 1.9f;
                                                drag = -0.01f;
                                                shootEffect = ModFx.stealthShoot;
                                                lifetime = 95f;
                                                collidesAir = true;
                                            }
                                        };
                                    }
                                },

                                new ModWeapon("litix-grinder") {
                                    {
                                        this.x = 0;
                                        this.y = 0;
                                        this.shootY = -1f;
                                        this.reload = 80;
                                        this.ejectEffect = ModFx.instShoot;
                                        this.recoil = 6;
                                        this.shootSound = Sounds.laserblast;
                                        this.rotate = true;
                                        this.rotateSpeed = 0.7f;
                                        this.mirror = false;
                                        this.bullet = new RailBulletType() {
                                            {
                                                this.shootEffect = ModFx.energyShrapnelShoot;
                                                this.length = 210.0F;
                                                this.updateEffectSeg = 60.0F;
                                                this.pierceEffect = this.hitEffect = ModFx.instHit;
                                                this.updateEffect = this.trailEffect = ModFx.instTrail;
                                                this.hitEffect = Fx.blastExplosion;
                                                this.smokeEffect = Fx.shootBig2;
                                                this.damage = 990.0F;
                                                this.pierceDamageFactor = 1.2F;
                                                this.despawnEffect = ModFx.instBomb;
                                                this.buildingDamageMultiplier = 1.2f;
                                                this.speed = 1f;
                                                this.hitShake = 4f;
                                            }
                                        };
                                    }
                                },
                                new ModWeapon("litix-methane-shooter") {
                                    {
                                        this.x = 0;
                                        this.y = 0;
                                        this.shootY = -1f;
                                        this.reload = 40;
                                        this.ejectEffect = Fx.blastsmoke;
                                        this.recoil = 2f;
                                        this.shots = 6;
                                        this.inaccuracy = 3.0f;
                                        this.rotate = true;
                                        this.shootSound = Sounds.beam;
                                        this.alternate = true;
                                        this.bullet = new LiquidBulletType(ModLiquids.liquidMethane) {
                                            {
                                                damage = 75;
                                                speed = 2.2f;
                                                drag = 0.004f;
                                                shootEffect = ModFx.stealthShoot;
                                                lifetime = 95f;
                                                collidesAir = true;
                                            }
                                        };
                                    }
                                },
                                new ModWeapon("litix-grinder") {
                                    {
                                        this.x = 0;
                                        this.y = 0;
                                        this.shootY = -1f;
                                        this.reload = 60;
                                        this.ejectEffect = Fx.instShoot;
                                        this.recoil = 5;
                                        this.shootSound = Sounds.laserblast;
                                        this.rotate = true;
                                        this.rotateSpeed = 0.5f;
                                        this.mirror = false;
                                        this.bullet = new RailBulletType() {
                                            {
                                                this.shootEffect = ModFx.energyShrapnelShoot;
                                                this.length = 294.0F;
                                                this.updateEffectSeg = 70.0F;
                                                this.pierceEffect = this.hitEffect = Fx.instHit;
                                                this.updateEffect = this.trailEffect = ModFx.shinigamiTrail;
                                                this.smokeEffect = Fx.shootBigSmoke;
                                                this.damage = 860.0F;
                                                this.pierceDamageFactor = 1.5F;
                                                this.despawnEffect = Fx.instBomb;
                                                this.buildingDamageMultiplier = 1.8f;
                                                this.speed = 1.5f;
                                                this.hitShake = 3f;
                                            }
                                        };
                                    }
                                }

                        ).enginePosses(() -> {
                            Seq<Vec2> posses = new Seq<>();
                            for (Point2 point2 : Geometry.d4) {
                                    posses.add(new Vec2(point2.x,point2.y).scl(7.5f));
                            }

                    Log.info("posses: @",posses.toString(", "));
                            return posses;
                        }).engineSize(6)
                );
                this.constructor = Types.stealthMech;
                this.defaultController = StealthGroundAI::new;
                this.speed = 0.62f;
                this.rotateSpeed = 2;
                this.hitSize = 38;
                this.armor = 14;
                this.buildSpeed = 2F;
                this.health = 48200;
                this.canDrown = false;
                this.immunities = ObjectSet.with(StatusEffects.burning, StatusEffects.melting);
                int brange = 1;
                this.localizedName = "Litix";
                this.description = "Heavy attack unit with orbital magma guns,railguns and sap guns.";
                 this.weapons.add(
                        new ModWeapon("litix-striker") {
                            {
                                this.x = 0;
                                this.y = 0;
                                this.shootY = -1f;
                                this.reload = 220;
                                this.ejectEffect = ModFx.magicShootEffectBig;
                                this.recoil = 6;
                                this.shootSound = Sounds.laserblast;
                                this.rotate = true;
                                this.rotateSpeed = 0.5f;
                                this.mirror = false;
                                this.bullet = new RailBulletType() {
                                    {
                                        this.shootEffect = ModFx.litixShoot;
                                        this.length = 425.0F;
                                        this.updateEffectSeg = 70.0F;
                                        this.pierceEffect = this.hitEffect = ModFx.litixHit;
                                        this.updateEffect = this.trailEffect = ModFx.litixTrail;
                                        this.smokeEffect = Fx.reactorsmoke;
                                        this.damage = 2620.0F;
                                        this.pierceDamageFactor = 1.5F;
                                        this.despawnEffect = ModFx.litixBomb;
                                        this.buildingDamageMultiplier = 1.3f;
                                        this.speed = 1.4f;
                                        this.hitShake = 5f;
                                    }
                                };
                            }
                        },
                        new ModWeapon("maverix-weapon") {
                            {
                                this.x = 34;
                                this.y = 0;
                                this.shootY = 5f;
                                this.reload = 50;
                                this.ejectEffect = Fx.fireballsmoke;
                                this.recoil = 3f;
                                this.shots = 7;
                                this.inaccuracy = 14.0f;
                                this.rotate = false;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet = new LiquidBulletType(ModLiquids.liquidMethane) {
                                    {
                                        damage = 98;
                                        speed = 1.9f;
                                        drag = -0.01f;
                                        shootEffect = Fx.lightningShoot;
                                        lifetime = 95f;
                                        collidesAir = true;
                                    }
                                };
                            }
                        }
                );
            }
        };
         penumbra = new ModUnitType("penumbra"){{
            speed = 0.52f;
            accel = 0.04f;
            drag = 0.04f;
            rotateSpeed = 1f;
            flying = true;
            lowAltitude = true;
            health = 15000;
            engineOffset = 38;
            engineSize = 7.3f;
            hitSize = 58f;
            destructibleWreck = false;
            armor = 83f;
            weapons.add(
                    new ModWeapon("penumbra-laser-mount"){{
                        shake = 4f;
                        shootY = 9f;
                        x = 18f;
                        y = 5f;
                        rotateSpeed = 2f;
                        reload = 65f;
                        recoil = 4f;
                        shootSound = Sounds.laser;
                        shadow = 20f;
                        rotate = true;

                        bullet = new LaserBulletType(){{
                            damage = 600f;
                            sideAngle = 20f;
                            sideWidth = 1.5f;
                            sideLength = 80f;
                            width = 25f;
                            length = 270f;
                            shootEffect = Fx.shockwave;
                            colors = new Color[]{Color.valueOf("72E4A9"), Color.valueOf("5BB36C"), Color.white};
                        }};
                    new ModWeapon("penumbra-shotgun"){{
                        x = 17f;
                        y = 32f;
                        rotateSpeed = 2.2f;
                        reload = 20f;
                        shots = 4;
                        inaccuracy = 5f;
                        shootSound = Sounds.shoot;
                        shadow = 7f;
                        rotate = true;
                        recoil = 0.5f;

                        bullet = new BasicBulletType(){{
                            damage = 82;
                            speed = 11.4f;
                            pierce = true;
                            pierceCap = 16;
                            width = 14f;
                            height = 33f;
                            lifetime = 25f;
                            shootEffect = Fx.shootBig;
                            lightning = 4;
                            lightningLength = 4;
                            lightningColor = Pal.surge;
                            lightningDamage = 22;
                            width = 14f;
                            height = 33f;
                            lifetime = 40f;
                            shootEffect = Fx.shootBig;
                        }};
                    }},
                    new ModWeapon("penumbra-shotgun"){{
                        y = -13f;
                        x = 18f;
                        reload = 12f;
                        ejectEffect = Fx.casing1;
                        rotateSpeed = 7f;
                        shake = 1f;
                        shootSound = Sounds.shoot;
                        rotate = true;
                        shadow = 12f;
                        bullet = new BasicBulletType(){{
                            damage = 12;
                            speed = 11.4f;
                            pierce = true;
                            pierceCap = 18;
                            width = 14f;
                            height = 33f;
                            lifetime = 25f;
                            shootEffect = Fx.shootBig;
                            lightning = 8;
                            lightningLength = 4;
                            lightningColor = Pal.surge;
                            lightningDamage = 31;
                            width = 14f;
                            height = 33f;
                            lifetime = 40f;
                            shootEffect = Fx.shootBig;
                        }});
                    };
    private static class Types {
        static Prov<? extends Unit> payload = PayloadUnit::create;
        static Prov<? extends Unit> naval = UnitWaterMove::create;
        static Prov<? extends Unit> legs = LegsUnit::create;
        static Prov<? extends Unit> mech = MechUnit::create;
        //static Prov<? extends Unit> air = FlyingUnit::create; //че так сложно
        static Prov<? extends Unit> stealthMech = StealthMechUnit::new;
//        static Prov<? extends Unit> powerNaval=PowerGeneratorUnit::new;
    }
}
