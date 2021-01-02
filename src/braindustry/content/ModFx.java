package braindustry.content;

import braindustry.entities.DebugEffect;
import braindustry.graphics.ModPal;
import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Angles;
import arc.math.Mathf;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;

import static braindustry.modVars.modFunc.*;

public class ModFx {
    private static final float Distance = Core.camera.width + Core.camera.height + 30 * Vars.tilesize;

    private static Effect newSwEff(float lifetime, float startSize, float multiplier, Color color1, Color color2, float finion) {
        Effect effect = new Effect(lifetime, e -> {
            Draw.color(color1, color2, e.fout());
            Lines.stroke(startSize + multiplier * e.fout());
            Lines.swirl(e.x, e.y, startSize + multiplier * e.fin(), finion, 0);
        });
        return effect;
    }

    private static Effect newCiEff(float lifetime, float startSize, float multiplier, Color color1, Color color2) {
        Effect effect = new Effect(lifetime, e -> {
            Draw.color(color1, color2, e.fout());
            Fill.circle(e.x, e.y, startSize + e.fout() * multiplier);
        });
        return effect;
    }

    private static final Color[] gemColors = {ModPal.rubyLight, ModPal.emeraldLight, ModPal.sapphireUnitDecalLight, ModPal.angel, ModPal.topazLight, ModPal.amethystLight};
    private static Color[] gemColorsBack = {ModPal.rubyDark, ModPal.emeraldDark, ModPal.sapphireUnitDecalDark, ModPal.angelDark, ModPal.topazDark, ModPal.amethystDark};
    private static final float[] energyShootsAngle = {-50, -25, 25, 50},
            energyShootsWidth = {7.6f, 9.8f, 9.8f, 7.6f},
            energyShootsHeight = {15.4f, 22.8f, 22.8f, 15.4f},
            spikeTurretShootsAngle = {-55, -30, 30, 55},
            spikeTurretShootsWidth = {7.6f, 9.8f, 9.8f, 7.6f},
            spikeTurretShootsHeight = {16.2f, 26.0f, 26.0f, 16.2f};

    public static final Effect magicTrailSwirl = newSwEff(15, 0.3f, 2.2f, ModPal.magicLight, ModPal.magic, 10);
    public static final Effect magicBulletTrail = new Effect(30, Distance, e -> {
        Draw.color(ModPal.magicLight, ModPal.magic, e.fout());
        Lines.stroke(e.fout() * 2);
        Angles.randLenVectors(e.id, 8, 8, 0, 360, (x, y) -> {
            Fill.circle(e.x, e.y, 0.1f + e.fout() * 2f);
        });
    });

    public static final Effect magicBulletHitTiny = new Effect(15, e -> {
        Draw.color(ModPal.magicLight, ModPal.magicDark, e.fin());
        Lines.stroke(e.fout());
        Angles.randLenVectors(e.id, 4, 1.8f + e.fin() * 4.5f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 1.4f + 0.2f);
        });
        ;
    });

    public static final Effect magicBulletHitSmall = new Effect(15, e -> {
        Draw.color(ModPal.magicLight, ModPal.magicDark, e.fin());
        Lines.stroke(0.1f + e.fout() * 1.1f);
        Angles.randLenVectors(e.id, 5, 2.6f + e.fin() * 6.8f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 2.2f + 0.6f);
        });
        ;
    });

    public static final Effect magicBulletHit = new Effect(20, e -> {
        Draw.color(ModPal.magicLight, ModPal.magicDark, e.fin());
        Lines.stroke(0.15f + e.fout() * 1.4f);
        Angles.randLenVectors(e.id, 7, 3.2f + e.fin() * 10.2f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 2.8f + 0.75f);
        });
        ;
    });

    public static final Effect magicBulletHitBig = new Effect(25, e -> {
        Draw.color(ModPal.magicLight, ModPal.magicDark, e.fin());
        Lines.stroke(0.22f + e.fout() * 1.8f);
        Angles.randLenVectors(e.id, 7, 4.5f + e.fin() * 14.0f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 3.4f + 0.9f);
        });
    });

    public static final Effect magicShootEffectBig = new Effect(30, e -> {
        Angles.randLenVectors(e.id, 9, 2.8f + e.fin() * 26.0f, e.rotation, 22, (x, y) -> {
            Draw.color(ModPal.magicLight, ModPal.magicDark, e.fin());
            Fill.square(e.x + x, e.y + y, 0.3f + e.fout() * 1.2f, 45);
        });
    });

    public static final Effect magicShootEffect = new Effect(25, e -> {
        Angles.randLenVectors(e.id, 6, 2.4f + e.fin() * 20.0f, e.rotation, 15, (x, y) -> {
            Draw.color(ModPal.magicLight, ModPal.magicDark, e.fin());
            Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 1.1f, 45);
        });
    });

    public static final Effect magicShootEffectSmall = new Effect(20, e -> {
        Angles.randLenVectors(e.id, 5, 2.0f + e.fin() * 16.0f, e.rotation, 13, (x, y) -> {
            Draw.color(Color.white, ModPal.magic, e.fin());
            Fill.square(e.x + x, e.y + y, 0.16f + e.fout(), 45);
        });
    });

    public static final Effect yellowBallCharge = new Effect(120, Distance, e -> {
        Draw.color(ModPal.energy);
        Lines.stroke(e.fin() * 5.0f);
        Lines.circle(e.x, e.y, 20.0f + e.fout() * 140.0f);

        Angles.randLenVectors(e.id, 30, 80.0f * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 10.0f);
        });
        ;

        Draw.color(ModPal.energyLight);
        Fill.circle(e.x, e.y, e.fin() * 30.0f);

        Draw.color();
        Fill.circle(e.x, e.y, e.fin() * 15);
    });

    public static final Effect giantYellowBallHitBig = new Effect(24, Distance, e -> {
        Draw.color(ModPal.energy);
        Angles.randLenVectors(e.id * 11, 8, e.fin() * 64.0f, e.rotation, 360.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 8f + 1.5f);
        });
        ;

        Lines.stroke(e.fout() * 4.0f);
        Lines.circle(e.x, e.y, 10.0f + e.fin() * 80.0f);
    });

    public static final Effect giantYellowBallHitLarge = new Effect(38, Distance, e -> {
        Draw.color(ModPal.energy);
        Angles.randLenVectors(e.id * 11, 13, e.fin() * 120.0f, e.rotation, 360.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 12f + 3.0f);
        });
        ;

        Lines.stroke(e.fout() * 8.0f);
        Lines.circle(e.x, e.y, 20.0f + e.fin() * 100.0f);
    });

    public static final Effect cutolCraft = new Effect(25, e -> {
        Angles.randLenVectors(e.id, 8, 8 + e.fin() * 13, (x, y) -> {
            Draw.color(Color.valueOf("#718DDB"), Color.valueOf("#4C5F93"), e.fin());
            Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 3f, 45);
        });
        ;
    });

    public static final Effect photoniteCraft = new Effect(50, e -> {
        Angles.randLenVectors(e.id, 20, 8 + e.fin() * 16, (x, y) -> {
            Draw.color(gemColors[e.id % 6], gemColorsBack[e.id % 6], e.fin());
            Fill.square(e.x + x, e.y + y, 0.25f + e.fout() * 1.8f, 45);
        });
    });

    public static final Effect energyBlastTiny = new Effect(30, e -> {
        Angles.randLenVectors(e.id, 15, 4 + e.fin() * 10, (x, y) -> {
            Draw.color(ModPal.crystalizerDecalLight, ModPal.crystalizerDecal, e.fin());
            Fill.circle(e.x + x, e.y + y, 0.1f + e.fout() * 1.3f);
        });
    });

    public static final Effect orbonCraft = new Effect(25, e -> {
        Angles.randLenVectors(e.id, 15, 4 + e.fin() * 10, (x, y) -> {
            Draw.color(ModPal.orbonLight, ModPal.orbon, e.fin());
            Fill.circle(e.x + x, e.y + y, 0.4f + e.fout() * 1.3f);
        });
    });

    public static final Effect contritumCraft = new Effect(40, e -> {
        Angles.randLenVectors(e.id, 20, 5.0f + e.fin() * 16f, (x, y) -> {
            Draw.color(ModPal.contritum, ModPal.contritumDark, e.fin());
            Fill.square(e.x + x, e.y + y, 0.55f + e.fout() * 1.6f, 90);
        });
    });

    public static final Effect contritumUpdate = new Effect(25, e -> {
        Angles.randLenVectors(e.id, 10, 3.0f + e.fin() * 10.0f, (x, y) -> {
            Draw.color(ModPal.contritum, ModPal.contritumDark, e.fin());
            Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 1.1f, 90);
        });
    });

    public static final Effect laserAdditionalEffect = new Effect(15, e -> {
        Draw.color(ModPal.unitOrangeLight, ModPal.unitOrangeDark, e.fin());
        Fill.square(e.x, e.y, 0.8f + e.fout() * 2.0f, Mathf.randomSeedRange(e.id, 360));
    });

    public static final Effect energyShrapnelShoot = new Effect(8, e -> {
        for (int i = 0; i < 4; i++) {
            Draw.color(ModPal.unitOrangeLight, ModPal.unitOrangeDark, e.fout());
            Drawf.tri(e.x, e.y,
                    energyShootsWidth[i],
                    energyShootsHeight[i],
                    e.rotation + (energyShootsAngle[i])
            );
        }
    });

    public static final Effect rapierShoot = new Effect(7, e -> {
        for (int i = 0; i < 4; i++) {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Drawf.tri(e.x, e.y,
                    energyShootsWidth[i],
                    energyShootsHeight[i],
                    e.rotation + (energyShootsAngle[i])
            );
        }
    });

    public static final Effect rapierSmoke = new Effect(20, e -> {
        Angles.randLenVectors(e.id, 12, 8.0f + e.fin() * 80.0f, e.rotation, 15, (x, y) -> {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 4.0f + 12.0f * e.fout());
        });
    });

    public static final Effect laculisAttraction = new Effect(18, e -> {
        Angles.randLenVectors(e.id, 12, 6.0f + e.fout() * 12.0f, 0.0f, 360.0f, (x, y) -> {
            Draw.color(ModPal.unitOrangeLight, ModPal.unitOrange, e.fout());
            Lines.stroke(e.fin() * 3.0f);
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 2.0f + 8.0f * e.fin());
        });
    });

    public static final Effect spikeHit = new Effect(15, e -> {
        Angles.randLenVectors(e.id, 4, 3.0f + e.fin() * 8.0f, 0.0f, 360.0f, (x, y) -> {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.0f + 2.5f * e.fout());
        });
    });

    public static final Effect spikeSmoke = new Effect(35, e -> {
        Angles.randLenVectors(e.id, 20, 3.0f + e.fin() * 80.0f, e.rotation, 15, (x, y) -> {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Lines.stroke(e.fout() * 4.0f);
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.0f + 3.0f * e.fout());
        });
    });

    public static final Effect spikeTurretShoot = new Effect(6, e -> {
        for (int i = 0; i < 4; i++) {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fin());
            Drawf.tri(e.x, e.y,
                    spikeTurretShootsWidth[i],
                    spikeTurretShootsHeight[i],
                    e.rotation + (spikeTurretShootsAngle[i])
            );
        }
    });

    public static final Effect smallSpikeHit = new Effect(15, e -> {
        Angles.randLenVectors(e.id, 3, 1.5f + e.fin() * 6.0f, e.rotation, 45.0f, (x, y) -> {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Lines.stroke(2.0f * e.fout());
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 0.2f + 2.4f * e.fout());
        });
    });

    public static final Effect energyShrapnelSmoke = new Effect(20, e -> {
        Angles.randLenVectors(e.id, 10, 6 + e.fin() * 200, e.rotation, 10, (x, y) -> {
            Draw.color(ModPal.unitOrangeLight, ModPal.unitOrangeDark, e.fout());
            Fill.square(e.x + x, e.y + y, 0.2f + e.fout() * 1.1f, 45);
        });
    });

    public static final Effect greenTinyHit = new Effect(10, e -> {
        Draw.color(Pal.heal);
        Lines.stroke(1.2f * e.fout());
        Lines.circle(e.x, e.y, 0.8f + e.finpow() * 3.6f);
    });

    public static final Effect redArtilleryHit = new Effect(14, e -> {
        Draw.color(ModPal.unitOrangeLight, ModPal.unitOrangeDark, e.fin());
        Lines.stroke(0.5f + e.fout() * 2.2f);
        Lines.circle(e.x, e.y, e.fin() * 80.0f);

        Angles.randLenVectors(e.id, 8, 5.6f + e.fin() * 25.0f, (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 14.5f + 3.0f);
        });
    });

    public static final Effect leviathanLaserCharge = new Effect(50, 100, e -> {
        Draw.color(ModPal.unitOrangeLight, ModPal.unitOrangeDark,e.fin());
        Angles.randLenVectors(e.id * 11, 30, e.fin() * 200, e.rotation, 360.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 8.0f + 1.5f);
        });
        Lines.stroke(e.fout() * 14.0f);
        Lines.circle(e.x, e.y, 16.0f + e.fin() * 200.0f);
    });

    public static final Effect blueSquare = new Effect(25, e -> {
        Draw.color(Color.valueOf("#00A6FF"));
        Lines.stroke(3.0f * e.fslope());
        Lines.square(e.x, e.y, e.fin() * 120.0f);
    });

    public static final Effect curseEffect = new Effect(33, e -> {
        Draw.color(ModPal.unitOrangeLight);
        Fill.circle(e.x, e.y, 0.5f + e.fout() * (0.5f + Mathf.randomSeedRange(e.id, 3.5f)));
    });

    public static final Effect magicUnitDamage = new Effect(40, e -> {
        Object[] data=(Object[])e.data;
        Draw.color(ModPal.magic);
        Draw.alpha(0.1f + 0.5f * e.fout());
        Draw.rect(Core.atlas.find(data[0].toString(), Core.atlas.find("error")), e.x, e.y, e.rotation - (float)data[1]);
        Draw.alpha(1.0f);
    });

    public static final Effect thunderShoot = new Effect(50, e -> {
        Angles.randLenVectors(e.id, 6, 10 + e.fin() * 80, e.rotation, 20, (x, y) -> {
            Draw.color(Color.white,ModPal.topaz, e.fout());
            Fill.square(e.x + x, e.y + y, 0.5f + e.fout() * 2.5f, 45);
        });
    });

    public static final Effect dischargeShoot = new Effect(35, e -> {
        Angles.randLenVectors(e.id, 6, 4.0f + e.fin() * 30.0f, e.rotation, 10, (x, y) -> {
            Draw.color(Color.white, ModPal.topaz, e.fout());
            Fill.square(e.x + x, e.y + y, 0.4f + e.fout() * 1.5f, 45);
        });
    });

    public static final Effect hitMovingLaser = new Effect(30, e -> {
        Angles.randLenVectors(e.id, 3, 4f + e.fin() * 10.0f, 0, 360.0f, (x, y) -> {
            Draw.color(Color.white, ModPal.topaz, e.fout());
            Fill.square(e.x + x, e.y + y, 0.5f + e.fout() * 1.2f, 45);
        });
    });

    public static final Effect movingLaserOnExtend = new Effect(35, e -> {
        Angles.randLenVectors(e.id, 3, 10.0f + e.fin() * 22.0f, e.rotation, 10, (x, y) -> {
            Draw.color(Color.white, ModPal.topaz, e.fout());
            Fill.square(e.x + x, e.y + y, 0.3f + e.fout(), 45);
        });
    });

/////////

    public static final Effect YellowBeamFlare = new Effect(30, e -> {
        Draw.color(Color.valueOf("FFFFFF44"));
        Draw.alpha(e.fout() * 0.3f);
        Draw.blend(Blending.additive);
        Draw.rect(getFullName("smoke"), e.x, e.y, e.fin() * 800, e.fin() * 800 * Mathf.random(1.5f, 2.0f));
        Draw.blend();
    });

    public static final Effect YellowBeamFlare2 = new Effect(30, e -> {
        Draw.color(Color.valueOf("FFFFFF44"));
        Draw.alpha(e.fout() * 1);
        Draw.blend(Blending.additive);
        Draw.rect(getFullName("smoke"), e.x, e.y, 50, 50);
        Draw.blend();
    });

    public static final Effect YellowBeamFlare3 = new Effect(30, e -> {
        Draw.color(Color.valueOf("FFFFFF44"));
        Draw.alpha(e.fout() * 1);
        Draw.blend(Blending.additive);
        Draw.rect(getFullName("smoke"), e.x, e.y, 800 * e.fin(), 800 * e.fin());
        Draw.blend();
    });

    public static final Effect YellowBeamChargeBegin = new Effect(30, 300, e -> {
        Draw.color(ModPal.topaz, Color.white, e.fin());
        Lines.stroke(e.fin() * 5);
        Lines.circle(e.x, e.y, e.fout() * 60);

        Angles.randLenVectors(e.id, 4, 10 + 80 * e.fout(), e.rotation, 50, (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 24);
        });
        ;
    });

    public static final Effect YellowBeamCharge = new Effect(30, 300, e -> {
        Draw.color(ModPal.topaz, Color.white, e.fin());
        Lines.stroke(e.fin() * 5);
        Lines.circle(e.x, e.y, e.fout() * 60.0f);

        Angles.randLenVectors(e.id, 4, 15.0f + 160.0f * e.fout(), e.rotation, 10, (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 32);
        });
        ;
    });

    public static final Effect circleSpikeHit = new Effect(20, e -> {
        Angles.randLenVectors(e.id, 7, 5.0f + e.fin() * 12.0f, 0.0f, 360.0f, (x, y) -> {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Lines.stroke(e.fout() * 2.0f);
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1.2f + 3.2f * e.fout());
        });
    });

    public static final Effect materializerCraft = new Effect(40, e -> {
        Angles.randLenVectors(e.id, 12, 1.0f + e.fin() * 5.0f, 0.0f, 360.0f, (x, y) -> {
            Draw.color(e.color.cpy().mul(0.9f));
            Fill.square(e.x + x, e.y + y, 0.1f + 0.8f * e.fout(), 45);
        });
    });

    public static final Effect gemLaserHit = new Effect(20, e -> {
        Angles.randLenVectors(e.id, 8, 4.0f + e.fin() * 14.0f, 0, 360.0f, (x, y) -> {
            Draw.color(ModPal.diamond, ModPal.diamondDark, e.fout());
            Fill.square(e.x + x, e.y + y, 4.5f * e.fout(), 270.0f * e.fout());
        });
    });

    public static final Effect angelLight = new Effect(20, e -> {
        Vars.renderer.lights.add(e.x, e.y, 4.0f + 20.0f * e.fout(), e.color, 0.8f);
    });

    public static final Effect fireHit = new Effect(50, e -> {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 3, 4.0f + e.fin() * 14.0f, 0, 360.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.4f + e.fout() * 1.6f);
        });
    });

    public static final Effect fireHitBig = new Effect(50, e -> {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 3, 5.5f + e.fin() * 18.0f, 0, 360.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.55f + e.fout() * 2.0f);
        });
    });

    public static final Effect foxShoot = new Effect(30, e -> {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 4, 4.0f + e.fin() * 40.0f, e.rotation, 15, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.4f + e.fout() * 1.4f);
        });
    });

    public static final Effect napalmShoot = new Effect(40, e -> {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 2, 2.0f + e.fin() * 80.0f, e.rotation, 15f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.5f + e.fout() * 1.8f);
        });
    });

    public static final Effect burningIntensiveEffect = new Effect(55, e -> {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 3, 2.0f + e.fin() * 6.0f, 90.0f, 15.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.4f + e.fout() * 1.6f);
        });
    });

    public static final Effect burningIntensiverEffect = new Effect(55, e -> {
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 3, 3.0f + e.fin() * 8.0f, 90.0f, 25.0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.6f + e.fout() * 2.0f);
        });
    });


    static {
        Fx.class.isArray();
    }

    public static final Effect shootSmall = new Effect(8.0F, (e) -> {
        Draw.color(Color.white, e.color, e.fin());
        float w = 1.0F + 5.0F * e.fout();
        Drawf.tri(e.x, e.y, w, 15.0F * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 3.0F * e.fout(), e.rotation + 180.0F);
    });
    public static final Effect purpleLaserChargeSmall = new Effect(40.0F, 100.0F, (e) -> {
        Draw.color(Color.valueOf("d5b2ed"));
        Lines.stroke(e.fin() * 2.0F);
        Lines.circle(e.x, e.y, e.fout() * 50.0F);
    });
    public static final Effect purpleBomb = new Effect(40.0F, 100.0F, (e) -> {
        Color color = Color.valueOf("d5b2ed");
        Draw.color(color);
        Lines.stroke(e.fout() * 2.0F);
        Lines.circle(e.x, e.y, 4.0F + e.finpow() * 65.0F);
        Draw.color(color);

        int i;
        for (i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 6.0F, 100.0F * e.fout(), (float) (i * 90));
        }

        Draw.color();

        for (i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 3.0F, 35.0F * e.fout(), (float) (i * 90));
        }

    });
    public static final Effect lacertaLaserCharge = new Effect(80.0F, 100.0F, (e) -> {
        print("e.data: @", e.data);
        Color color = Color.valueOf("d5b2ed");
        Draw.color(color);
        Lines.stroke(e.fin() * 2.0F);
        Lines.circle(e.x, e.y, 4.0F + e.fout() * 100.0F);
        Fill.circle(e.x, e.y, e.fin() * 20.0F);
        Angles.randLenVectors((long) e.id, 20, 40.0F * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 5.0F);
        });
        Draw.color();
        Fill.circle(e.x, e.y, e.fin() * 10.0F);
    });
    public static final Effect yellowBomb = new Effect(40.0F, 100.0F, (e) -> {
        Color color = Color.valueOf("FFFD99");
        Draw.color(color);
        Lines.stroke(e.fout() * 2.0F);
        Lines.circle(e.x, e.y, 4.0F + e.finpow() * 65.0F);
        Draw.color(color);

        int i;
        for (i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 6.0F, 100.0F * e.fout(), (float) (i * 90));
        }

        Draw.color();

        for (i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 3.0F, 35.0F * e.fout(), (float) (i * 90));
        }

    });
    ;
    public static final Effect absorb = new Effect(12.0F, (e) -> {
        Draw.color(e.color);
        Lines.stroke(2.0F * e.fout());
        Lines.circle(e.x, e.y, 5.0F * e.fout());
    });
    public static final Effect changeCraft = new Effect(40.0F, (e) -> {
        Color secondColor;
        if (e.data == null) {
            secondColor = Color.white;
        } else {
            if (!(e.data instanceof Color)) return;
            secondColor = (Color) e.data;
        }
        Draw.color(secondColor, e.color, e.fin());
        Draw.alpha(e.fout());
        Fill.square(e.x, e.y, 4.0F * e.rotation);
    });
    public static final Effect debugSelect = new DebugEffect(40.0F, (e) -> {
        Color secondColor;
        if (e.data == null) {
            secondColor = e.color;
        } else {
            if (!(e.data instanceof Color)) return;
            secondColor = (Color) e.data;
        }
        Draw.color(secondColor, e.color, e.fin());
        Draw.alpha(e.fout());
        Fill.square(e.x, e.y, 4.0F * e.rotation);
    });
}