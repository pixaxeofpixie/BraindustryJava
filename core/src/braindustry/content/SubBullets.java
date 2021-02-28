package braindustry.content;

import arc.math.Mathf;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.entities.bullet.SapBulletType;

public class SubBullets {
    public static void addLightning(Bullet b, BulletType bulletType){
        if(b.fin()<0.001){
            for(int i = 0; i < bulletType.lightning; i++){
                Lightning.create(b, bulletType.lightningColor, bulletType.lightningDamage < 0 ? bulletType.damage : bulletType.lightningDamage, b.x, b.y + 2, b.rotation() + Mathf.range(bulletType.lightningCone) + bulletType.lightningAngle, bulletType.lightningLength + Mathf.random(bulletType.lightningLengthRand));
            }
        }
    }
}
