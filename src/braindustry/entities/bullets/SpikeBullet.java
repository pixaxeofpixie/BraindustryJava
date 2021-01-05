package braindustry.entities.bullets;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Position;
import braindustry.content.ModFx;
import braindustry.content.ModStatusEffects;
import braindustry.graphics.ModPal;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Teamc;
import mindustry.graphics.Drawf;

public class SpikeBullet extends BasicBulletType {
    public SpikeBullet() {
        this.damage = 80.0f;
        this.homingPower = 0.15f;
        this.homingRange = 160.0f;
        this.speed = 5.0f;
        this.knockback = 0.6f;
        this.reflectable = false;
        this.hitEffect = ModFx.spikeHit;
        this.despawnEffect = ModFx.spikeHit;

        this.status = ModStatusEffects.speedMul.get(6);
        this.statusDuration = 60.0f * 3.0f;
        this.splashDamage = 0.0f;
        this.splashDamageRadius = 10.0f;
    }

    @Override
    public void draw(Bullet b) {
        Draw.color(ModPal.diamond, ModPal.diamondDark, b.fout());
        Drawf.tri(b.x, b.y, 4.0f, 8.0f, b.data == null ? b.rotation() : (float) ((Object[]) b.data)[2]);
        Drawf.tri(b.x, b.y, 4.0f, 8.0f, b.data == null ? b.rotation() - 180f : (float) ((Object[]) b.data)[2] - 180f);
    }

    @Override
    public void update(Bullet b) {
        if (this.homingPower > 0.0001 && b.time >= this.homingDelay) {
            Teamc target = Units.closestTarget(b.team, b.x, b.y, this.homingRange, (e -> (e.isGrounded() && this.collidesGround) || (e.isFlying() && this.collidesAir)),
                    t -> this.collidesGround);
            Object[] data = (Object[]) b.data;
            if (target != null && data == null) {
                b.vel.setAngle(Mathf.slerpDelta(b.rotation(), b.angleTo(target), this.homingPower / 5 + this.homingPower * 2 * b.fin()));
            } else if (data != null) {
                if (((boolean) data[1]) && data[0] != null) {
                    data[2] = Mathf.slerpDelta((float) data[2], b.angleTo((Position) data[0]), 0.05f);
                    b.vel.scl(0.988f);

                    if ((float) data[2] > b.angleTo((Position) data[0]) - 3 && (float) data[2] < b.angleTo((Position) data[0]) + 3f) {
                        b.vel.setAngle((float) data[2]);
                        b.vel.scl(30.0f);
                        data[1] = false;
                    }
                }
            }
        }
    }

}
