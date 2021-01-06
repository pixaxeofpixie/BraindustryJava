package braindustry.entities.bullets;

import arc.graphics.Color;
import braindustry.content.ModFx;
import braindustry.graphics.ModPal;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.MissileBulletType;

public class AdamBulletType extends MissileBulletType {
    public AdamBulletType(){
        super();
        this.width = 15;
        this.height = 15;
        this.shrinkY = 0.1f;
        this.speed = 3.0f;
        this.drag = 0.01f;
        this.splashDamageRadius = 30f;
        this.splashDamage = 52f;
        this.hitEffect =this.despawnEffect = ModFx.adamExplosion;
        this.homingPower = 0.2f;
        this.lightningDamage = 6f;
        this.lightning = 8;
        this.lightningLength = 5;
        this.makeFire = true;
        this.status = StatusEffects.burning;
        this.lifetime = 85f;
        this.trailColor = ModPal.adamTrailColor;
        this.backColor = ModPal.adamBackColor;
        this.frontColor = ModPal.adamFrontColor;
        this.weaveScale = 3f;
        this.weaveMag = 6f;
    }
}
