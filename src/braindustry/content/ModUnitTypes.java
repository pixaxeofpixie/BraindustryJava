package braindustry.content;

import arc.func.Prov;
import arc.graphics.Color;
import braindustry.entities.Advanced.AdvancedLegsUnit;
import braindustry.entities.Advanced.AdvancedUnitType;
import braindustry.entities.Advanced.UnitExtensions;
import braindustry.entities.PowerGeneratorUnit;
import braindustry.entities.PowerUnitType;
import braindustry.type.ModWeapon;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.*;
import mindustry.gen.EntityMapping;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.type.AmmoTypes;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.Block;

import static braindustry.modVars.modFunc.fullName;

public class ModUnitTypes implements ContentList {
    public static UnitType
            aquila, aries, armor, broadsword, capra,
            cenda, chainmail, chestplate, ibis, lacerta,
            lyra, shield, tropsy, venti,
            vyvna;

    private static class Types {
        static Prov<? extends Unit> payload = EntityMapping.map("PayloadUnit");
        static Prov<? extends Unit> naval = EntityMapping.map("UnitWaterMove");
        static Prov<? extends Unit> legs = EntityMapping.map("LegsUnit");
    }

    public ModUnitTypes() {
        UnitTypes.class.isArray();
    }

    @Override
    public void load() {
        vyvna = new PowerUnitType("vyvna") {
            public Block getGeneratorBlock(){
                return ModBlocks.unitGenerator;
            }
            {
                this.range=18;
                this.constructor = ()->new PowerGeneratorUnit();
                this.localizedName = "Atomic";
                this.description = "Power Generator";
                this.health = 5300;
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
                                this.bullet=new MissileBulletType() {
                                    {
                                        this.width = 15;
                                        this.height = 21;
                                        this.shrinkY = 0.3f;
                                        this.speed = 2.9f;
                                        this.drag = -0.01f;
                                        this.splashDamageRadius = 30;
                                        this.splashDamage = 22;
                                        this.hitEffect = Fx.blastExplosion;
                                        this.despawnEffect = Fx.blastExplosion;
                                        this.homingPower = 0.1f;
                                        this.lightningDamage = 6;
                                        this.lightning = 8;
                                        this.lightningLength = 5;
                                        this.makeFire = true;
                                        this.status = StatusEffects.burning;
                                        this.lifetime = 85;
                                        this.trailColor = Color.valueOf("b3bee0");
                                        this.backColor = Color.valueOf("7bafc4");
                                        this.frontColor = Color.valueOf("b5eff4");
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
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet=new ShrapnelBulletType() {
                                    {
                                        this.length = 130;
                                        this.damage = 92;
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
        ibis = new UnitType("ibis") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 60.0F;
                this.localizedName = "Ibis";
                this.description = "Smallest unit with 4 legs and high speed.";
                this.health = 210;
                this.speed = 0.56f;
                this.mechSideSway = 0.25f;
                this.hitSize = 5;
                this.rotateSpeed = 1;
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
                this.visualElevation = 0.4f;
                this.ammoType = AmmoTypes.powerHigh;
                this.mechStepShake = 0.15f;
                this.mechStepParticles = true;
                this.speed = 1;
                this.weapons.add(
                        new ModWeapon("ibis-weapon") {
                            {
                                this.x = 3;
                                this.y = -1.5f;
                                this.shootY = 3;
                                this.reload = 13;
                                this.ejectEffect = Fx.none;
                                this.recoil = 1;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.8f;
                                        this.length = 30;
                                        this.damage = 50;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
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

        aries = new UnitType("aries") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 60.0F;
                this.localizedName = "Aries";
                this.description = "A small 4-leg unit with strong laser guns.";
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
                this.visualElevation = 0.65f;
                this.immunities.addAll(StatusEffects.burning);
                this.weapons.add(
                        new ModWeapon("ibis-weapon") {
                            {
                                this.x = 3;
                                this.y = -1.5f;
                                this.shootY = 3;
                                this.reload = 20;
                                this.shots = 4;
                                this.shotDelay = 3;
                                this.inaccuracy = 9;
                                this.ejectEffect = Fx.none;
                                this.recoil = 1;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.95f;
                                        this.length = 48;
                                        this.damage = 40;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
                                        this.despawnEffect = Fx.none;
                                        this.width = 0.7f;
                                        this.lifetime = 45;
                                        this.knockback = 1.34f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        capra = new UnitType("capra") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 68.0F;
                this.localizedName = "Capra";
                this.itemCapacity = 200;
                this.speed = 0.4f;
                this.drag = 0.4f;
                this.hitSize = 12;
                this.rotateSpeed = 3;
                this.health = 4200;
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
                this.visualElevation = 0.4f;
                this.weapons.add(
                        new ModWeapon("capra-weapon") {
                            {
                                this.reload = 120;
                                this.rotate = true;
                                this.x = 10;
                                this.y = -7f;
                                this.shots = 24;
                                this.shotDelay = 1;
                                this.inaccuracy = 15;
                                this.mirror = true;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.8f;
                                        this.length = 150;
                                        this.damage = 60;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1;
                                        this.lifetime = 30;
                                        this.knockback = -0.65f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        lacerta = new UnitType("lacerta") {
            {
                this.constructor = Types.legs;
                this.groundLayer = 75.0F;
                this.localizedName = "Lacerta";
                this.description = "A giant unit with 6 legs and super-powerful laser with lightnings!";
                this.boostMultiplier = 2.1f;
                this.speed = 0.35f;
                this.drag = 0.4f;
                this.hitSize = 26;
                this.rotateSpeed = 1.6f;
                this.health = 10000;
                this.armor = 7;
                this.hovering = true;
                this.commandLimit = 8;
                this.legCount = 6;
                this.legLength = 19;
                this.legTrns = 1;
                this.legMoveSpace = 3;
                this.legBaseOffset = 2;
                this.allowLegStep = true;
                this.visualElevation = 0.8f;
                this.mechStepShake = 0.15f;
                this.mechStepParticles = true;
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
                                this.shootSound = Sounds.laser;
                                this.mirror = false;
                                this.bullet = new LaserBulletType() {
                                    {
                                        this.length = 500;
                                        this.damage = 300;
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
                                        this.lightningColor=Color.valueOf("d5b2ed");
                                        this.colors = new Color[]{Color.valueOf("d5b2ed"), Color.valueOf("9671b1"), Color.valueOf("a17dcd")};
                                    }
                                };
                            }
                        },
                        new ModWeapon("capra-weapon") {
                            {
                                this.reload = 20;
                                this.rotate = true;
                                this.x = 10;
                                this.y = -7f;
                                this.shots = 2;
                                this.shotDelay = 15;
                                this.mirror = true;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.7f;
                                        this.length = 200;
                                        this.damage = 200;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1.5f;
                                        this.lifetime = 25;
                                        this.knockback = -0.65f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        aquila = new AdvancedUnitType("aquila") {
            {
                this.extensions(UnitExtensions.blink);
                this.constructor = AdvancedLegsUnit::new;
                this.groundLayer = 75.0F;
                this.localizedName = "Aquila";
                this.description = "Fires a Sap bullets and big large laser.";
                this.drag = 0.1f;
                this.speed = 0.5f;
                this.hitSize = 48;
                this.hovering = true;
                this.health = 22000;
                this.rotateSpeed = 2;
                this.legCount = 8;
                this.legMoveSpace = 1.4f;
                this.legPairOffset = 4;
                this.legLength = 61;
                this.legExtension = 10;
                this.legBaseOffset = 8;
                this.landShake = 2.1f;
                this.legSpeed = 0.19f;
                this.legLengthScl = 1.3f;
                this.armor = 14;
                this.rippleScale = 6;
                this.legSplashDamage = 160;
                this.legSplashRange = 50;
                this.weapons.add(
                        new ModWeapon("aquila-equip1") {
                            {
                                this.reload = 60;
                                this.rotate = true;
                                this.x = 25;
                                this.y = 5;
                                this.shots = 6;
                                this.shotDelay = 10;
                                this.inaccuracy = 7;
                                this.mirror = true;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.8f;
                                        this.length = 350;
                                        this.damage = 270;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
                                        this.despawnEffect = Fx.none;
                                        this.width = 2;
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
                                this.x = 13;
                                this.y = -7f;
                                this.shots = 20;
                                this.shotDelay = 0.7f;
                                this.inaccuracy = 13;
                                this.mirror = true;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.7f;
                                        this.length = 350;
                                        this.damage = 90;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("8ef3ff");
                                        this.color = Color.valueOf("8ef3ff");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1;
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
                                this.shootSound = Sounds.laser;
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
                                        this.lightningColor=Color.valueOf("d5b2ed");
                                        this.colors = new Color[]{Color.valueOf("d5b2ed"), Color.valueOf("9671b1"), Color.valueOf("a17dcd")};
                                    }
                                };
                            }
                        }
                );
            }
        };
        //===========================
        armor = new UnitType("armor") {
            {
                this.constructor = Types.payload;
                this.localizedName = "Armor";
                this.speed = 0.9f;
                this.flying = true;
                this.health = 600;
                this.range = 100;
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
                                this.shootSound = Sounds.laser;
                                this.rotate = true;
                                this.mirror = false;
                                this.bullet=new SapBulletType() {
                                    {
                                        this.sapStrength = 0.5f;
                                        this.length = 75;
                                        this.damage = 40;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("D6FF33");
                                        this.color = Color.valueOf("FFE70F");
                                        this.despawnEffect = Fx.none;
                                        this.width = 0.7f;
                                        this.lifetime = 20;
                                        this.knockback = 1.24f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        shield = new UnitType("shield") {
            {
                this.constructor = Types.payload;
                this.localizedName = "Shield";
                this.speed = 0.8f;
                this.flying = true;
                this.health = 1200;
                this.armor = 1;
                this.range = 160;
                this.engineOffset = 6;
                this.rotateSpeed = 3;
                this.weapons.add(
                        new ModWeapon("armor-weapon") {
                            {
                                this.x = 5;
                                this.y = -4f;
                                this.shootY = 4;
                                this.reload = 30;
                                this.shots = 2;
                                this.inaccuracy = 6;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.shootSound = Sounds.laser;
                                this.rotate = true;
                                this.mirror = true;
                                this.bullet = new SapBulletType() {
                                    {
                                        this.sapStrength = 0.8f;
                                        this.length = 90;
                                        this.damage = 90;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("D6FF33");
                                        this.color = Color.valueOf("FFE70F");
                                        this.despawnEffect = Fx.none;
                                        this.width = 0.9f;
                                        this.lifetime = 20;
                                        this.knockback = 1.3f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        chestplate =new UnitType("chestplate") {
            {
                this.localizedName = "Chestplate";
                this.description = "A big T3 defense unit with sap laser guns.";
                this.constructor = Types.payload;
                this.speed = 0.6f;
                this.flying = true;
                this.hitSize = 12;
                this.engineSize = 3.2f;
                this.armor = 2;
                this.health = 3500;
                this.range = 120;
                this.engineOffset = 7;
                this.rotateSpeed = 1.1f;
                this.targetAir = true;
                this.payloadCapacity = 240;
                this.buildSpeed = 1.6f;
                this.weapons.add(
                        new ModWeapon("shield-weapon") {
                            {
                                this.reload = 40;
                                this.mirror = true;
                                this.shots = 2;
                                this.x = 6;
                                this.y = 0;
                                this.rotate = true;
                                this.bullet=Bullets.missileExplosive;
                            }
                        },
                        new ModWeapon("chainmail-weapon" ){
                            {
                                this.x = 0;
                                this.y = -8f;
                                this.shootY = -1f;
                                this.reload = 60;
                                this.ejectEffect = Fx.none;
                                this.recoil = 3;
                                this.shootSound = Sounds.laser;
                                this.rotate = true;
                                this.mirror = false;
                                this.bullet=new SapBulletType() {
                                    {
                                        this.sapStrength = 0.9f;
                                        this.length = 50;
                                        this.damage = 100;
                                        this.shootEffect = Fx.shootSmall;
                                        this.hitColor = Color.valueOf("e88ec9");
                                        this.color = Color.valueOf("e88ec9");
                                        this.despawnEffect = Fx.none;
                                        this.width = 1;
                                        this.lifetime = 40;
                                        this.knockback = 1.3f;
                                    }
                                };
                            }
                        }
                );
            }
        };

        chainmail =new UnitType("chainmail") {
            {
                this.localizedName = "Chainmail";
                this.constructor=Types.payload;
                this.speed = 0.9f;
                this.flying = true;
                this.hitSize = 27;
                this.engineSize = 7;
                this.armor = 12;
                this.health = 10000;
                this.rotateSpeed = 1.2f;
                this.targetAir = true;
                this.payloadCapacity = 300;
                this.buildSpeed = 1.3f;
                this.commandLimit = 6;
                this.weapons.add(
                        new ModWeapon("bomb-weapon") {
                            {
                                this.x = 0;
                                this.y = -12f;
                                this.mirror = false;
                                this.reload = 75;
                                this.minShootVelocity = 0.02f;
                                this.shootSound = Sounds.plasmadrop;
                                this.bullet=new BasicBulletType() {
                                    {
                                        this.width = 60;
                                        this.height = 60;
                                        this.maxRange = 30;
                                        //this.shootCone = 190;
                                        this.despawnShake = 3;
                                        this.collidesAir = false;
                                        this.lifetime = 30;
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
                                        this.splashDamageRadius = 220;
                                    }
                                };
                            }
                        },
                        new ModWeapon("broadsword-weapon") {
                            {
                                this.top = false;
                                this.y = -3f;
                                this.x = 32;
                                this.reload = 40;
                                this.recoil = 2;
                                this.shootSound = Sounds.missile;
                                this.shots = 5;
                                this.inaccuracy = 0.4f;
                                this.velocityRnd = 0.2f;
                                this.alternate = true;
                                this.mirror = true;
                                this.bullet=Bullets.missileExplosive;
                            }
                        }
                );
            }
        };

        broadsword = new UnitType("broadsword") {
            {
                this.constructor = Types.payload;
                this.localizedName = "Helmet";
                this.description = "A colossal unit with ability to bombard, repair, defend, transport other units and shoot lasers.";
                this.armor = 18;
                this.health = 29000;
                this.speed = 0.6f;
                this.rotateSpeed = 1;
                this.accel = 0.07f;
                this.drag = 0.02f;
                this.flying = true;
                this.lowAltitude = true;
                this.commandLimit = 8;
                this.engineOffset = 38;
                this.engineSize = 10;
                this.rotateShooting = true;
                this.hitSize = 64;
                this.payloadCapacity = 380;
                this.buildSpeed = 5;
                this.range = 160;
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
                                this.bullet=new BasicBulletType(){
                                    {
                                        this.width = 45;
                                        this.height = 45;
                                        this.maxRange = 40;
                                        //this.ignoreRotation = true;
                                        this.hitSound = Sounds.plasmaboom;
                                        //this.shootCone = 160;
                                        this.despawnShake = 2.1f;
                                        this.collidesAir = false;
                                        this.lifetime = 20;
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
                                        this.splashDamageRadius = 170;
                                    }
                                };
                            }
                        },
                        new ModWeapon("broadsword-weapon") {
                            {
                                this.top = false;
                                this.y = -4f;
                                this.x = 29;
                                this.reload = 25;
                                this.recoil = 3;
                                this.shootSound = Sounds.missile;
                                this.shots = 7;
                                this.inaccuracy = 1;
                                this.velocityRnd = 0.1f;
                                this.alternate = true;
                                this.mirror = true;
                                this.bullet=Bullets.missileSurge;
                            }
                        }
                );
            }
        };

        //===========================
        venti = new UnitType("venti") {
            {
                this.localizedName = "Vixy";
                this.description = "First naval unit, has powerful shrapnel gun but needs support.";
                this.constructor=Types.naval;
                this.health = 520;
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
                                this.bullet=new ShrapnelBulletType() {
                                    {
                                        this.length = 60;
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

        lyra =new UnitType("lyra") {
            {
                this.localizedName = "Lyra";
                this.description = "A T2 sea unit with shrapnel lasers and rocketguns, good at attacking opponent's base, but needs support.";
                this.constructor = Types.naval;
                this.health = 850;
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
                                this.bullet=new ShrapnelBulletType() {
                                    {
                                        this.length = 60;
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
                                this.reload = 60;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.shots = 1;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.bullet = new MissileBulletType(){
                                    {
                                        this.width = 10;
                                        this.height = 20;
                                        this.shrinkY = 0.1f;
                                        this.drag = -0.1f;
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

        tropsy = new UnitType("tropsy") {
            {
                this.localizedName = "Tropsy";
                this.description = "An enlarged and improved unit created on the basis of Lyra unit.";
                this.constructor=Types.naval;
                this.health = 1120;
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
                this.weapons.add(
                        new ModWeapon("cenda-weapon") {
                            {
                                this.reload = 60;
                                this.x = 0;
                                this.y = -4f;
                                this.shadow = 7;
                                this.rotateSpeed = 0.7f;
                                this.rotate = true;
                                this.shots = 6;
                                this.shotDelay = 15;
                                this.inaccuracy = 4;
                                this.velocityRnd = 0.14f;
                                this.shootSound = Sounds.missile;
                                this.bullet=new MissileBulletType(){
                                    {
                                        this.width = 14;
                                        this.height = 14f;
                                        this.shrinkY = 0.3f;
                                        this.speed = 6.7f;
                                        this.drag = 0.05f;
                                        this.splashDamageRadius = 50;
                                        this.splashDamage = 28;
                                        this.hitEffect = Fx.blastExplosion;
                                        this.despawnEffect = Fx.blastExplosion;
                                        this.lightningDamage = 7;
                                        this.lightning = 5;
                                        this.lightningLength = 7;
                                        this.lifetime = 120;
                                        this.trailColor = Color.valueOf("b3bee0");
                                        this.backColor = Color.valueOf("7bafc4");
                                        this.frontColor = Color.valueOf("b5eff4");
                                        this.weaveScale = 8;
                                        this.weaveMag = 1;
                                    }
                                };
                            }
                        },
                        new ModWeapon("lyra-weapon") {
                            {
                                this.x = 6;
                                this.y = 4;
                                this.shootY = -1f;
                                this.reload = 30;
                                this.ejectEffect = Fx.none;
                                this.recoil = 2;
                                this.rotate = true;
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet=new ShrapnelBulletType() {
                                    {
                                        this.length = 60;
                                        this.damage = 62;
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
        cenda = new UnitType("cenda") {
            {
                this.localizedName = "Cenda";
                this.description = "A large and heavy attacking naval unit built on the proven \"Tropsy\" design, but with improvements in everything.";
                this.constructor=Types.naval;
                this.health = 5300;
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
                                this.bullet=new MissileBulletType() {
                                    {
                                        this.width = 15;
                                        this.height = 21;
                                        this.shrinkY = 0.3f;
                                        this.speed = 2.9f;
                                        this.drag = -0.01f;
                                        this.splashDamageRadius = 30;
                                        this.splashDamage = 22;
                                        this.hitEffect = Fx.blastExplosion;
                                        this.despawnEffect = Fx.blastExplosion;
                                        this.homingPower = 0.1f;
                                        this.lightningDamage = 6;
                                        this.lightning = 8;
                                        this.lightningLength = 5;
                                        this.makeFire = true;
                                        this.status = StatusEffects.burning;
                                        this.lifetime = 85;
                                        this.trailColor = Color.valueOf("b3bee0");
                                        this.backColor = Color.valueOf("7bafc4");
                                        this.frontColor = Color.valueOf("b5eff4");
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
                                this.shootSound = Sounds.flame;
                                this.alternate = true;
                                this.bullet=new ShrapnelBulletType() {
                                    {
                                        this.length = 130;
                                        this.damage = 92;
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
    }
}
