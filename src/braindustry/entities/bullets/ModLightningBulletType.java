package braindustry.entities.bullets;

import arc.math.Mathf;
import braindustry.entities.ModLightning;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.gen.Bullet;

public class ModLightningBulletType extends LightningBulletType {
    @Override
    public void init(Bullet b) {
        ModLightning.create(b, this.lightningColor, this.damage, b.x, b.y, b.rotation(), this.lightningLength + Mathf.random(this.lightningLengthRand));
    }
}
