package braindustry.entities.bullets;

import arc.graphics.Color;
import braindustry.content.ModFx;
import braindustry.graphics.ModPal;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.MissileBulletType;

public class EveBulletType extends MissileBulletType {
    public EveBulletType() {
        super();
        this.width = 15;
        this.height = 25;
        this.shrinkY = 0.1f;
        this.speed = 2.5f;
        this.drag = 0f;
        this.splashDamageRadius = 50;
        this.splashDamage = 30;
        this.hitEffect = this.despawnEffect = ModFx.eveExplosion;
        this.homingPower = 0.5f;
        this.lightningDamage = 10;
        this.lightning = 4;
        this.lightningLength = 10;
        this.makeFire = true;
        this.status = StatusEffects.burning;
        this.lifetime = 100;
        this.trailColor = ModPal.eveTrailColor;
        this.backColor = ModPal.eveBackColor;
        this.frontColor = ModPal.eveFrontColor;
        this.lightningColor=this.backColor;
        this.weaveScale = 1;
        this.weaveMag = 3;
    }
}
