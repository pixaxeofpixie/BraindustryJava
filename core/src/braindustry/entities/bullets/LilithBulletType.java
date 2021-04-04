package braindustry.entities.bullets;

import arc.graphics.Color;
import braindustry.content.ModFx;
import braindustry.graphics.ModPal;
import mindustry.content.StatusEffects;
import mindustry.entities.bullet.MissileBulletType;

public class LilithBulletType extends MissileBulletType {
    public LilithBulletType(){
        super();
        this.width = 17;
        this.height = 18;
        this.shrinkY = 0.1f;
        this.speed = 2.3f;
        this.drag = 0f;
        this.splashDamageRadius = 40f;
        this.splashDamage = 62f;
        this.hitEffect =this.despawnEffect = ModFx.lilithExplosion;
        this.homingPower = 0.3f;
        this.lightningDamage = 10f;
        this.lightning = 7;
        this.lightningLength = 7;
        this.makeFire = true;
        this.status = StatusEffects.slow;
        this.lifetime = 90f;
        this.trailColor = ModPal.lilithTrailColor;
        this.backColor = ModPal.lilithBackColor;
        this.frontColor = ModPal.lilithFrontColor;
        this.lightningColor=this.backColor;
        this.weaveScale = 2f;
        this.weaveMag = 5f;
    }
}