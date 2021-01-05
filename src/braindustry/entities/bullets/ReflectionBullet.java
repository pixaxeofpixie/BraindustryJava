package braindustry.entities.bullets;

import braindustry.content.ModFx;
import braindustry.graphics.ModPal;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Call;

public class ReflectionBullet extends BasicBulletType {
    public Color color1 = ModPal.magicLight, color2 = ModPal.magicDark;

    public ReflectionBullet() {
        super();
        ammoMultiplier = 4.0f;
        damage = 120;
        speed = 7.0f;
        hitEffect = ModFx.magicBulletHit;
        despawnEffect = ModFx.magicBulletHit;
        hitColor = ModPal.magicLight.cpy();
        shootEffect = ModFx.magicShootEffectBig;
    }

    @Override
    public void draw(Bullet b) {
        Draw.color(color1, color2, b.fin());
        Fill.circle(b.x, b.y, 2.8f + b.fslope());
//        super.draw(b);
    }

    @Override
    public void hit(Bullet b, float x, float y) {
        this.hitEffect.at(b.x, b.y, b.rotation(), this.hitColor);
        this.hitSound.at(b.x, b.y);
        Vec2 v = new Vec2();
        v.trns(Mathf.random(360.0f), Mathf.random(48.0f, 120.0f));
        Call.createBullet(new ReflectionFragBullet(), b.team, b.x + v.x, b.y + v.y, v.angle() - 180, 70, 1, 1);
        super.hit(b, x, y);
    }

    public class ReflectionFragBullet extends BasicBulletType {
        public ReflectionFragBullet() {
            super();
            damage = 180;
            speed = 10.0f;
            lifetime = 20;
            hitEffect = ModFx.magicBulletHit;
            despawnEffect = ModFx.magicBulletHit;
            hitColor = ModPal.magicLight.cpy();
            shootEffect = ModFx.magicShootEffect;
        }

        @Override
        public void draw(Bullet b) {
            Draw.color(ReflectionBullet.this.color1);
            Fill.circle(b.x, b.y, 3.0f);
        }
    }
}
