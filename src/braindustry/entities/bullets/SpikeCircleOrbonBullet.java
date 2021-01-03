package braindustry.entities.bullets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import braindustry.content.ModFx;
import braindustry.content.ModStatusEffects;
import braindustry.graphics.ModPal;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Call;
import mindustry.gen.Teamc;
import mindustry.graphics.Drawf;

public class SpikeCircleOrbonBullet extends BasicBulletType {
    public Color color1=ModPal.diamond,color2=ModPal.diamondDark;
    public SpikeCircleOrbonBullet(){
        this.hitEffect = ModFx.circleSpikeHit;
        this.ammoMultiplier = 4.0f;
        this.despawnEffect = ModFx.circleSpikeHit;

        this.smokeEffect = ModFx.spikeSmoke;
        this.damage = 1200;
        this.despawnShake = 2.0f;
        this.hitShake = 4.5f;
        this.speed = 2.5f;

        this.absorbable = false;
        this.reflectable = false;
        this.hittable = false;
        this.hitSize = 10.0f;

        this.status = ModStatusEffects.speedMul.get(5);
        this.statusDuration = 60.0f * 3.0f;
    }
    @Override
    public void draw(Bullet b) {
        Draw.color(color1, color2, b.fout());
        Fill.circle(b.x, b.y, 5.5f);

        Vec2 v = new Vec2();
        for(int i = 0; i < 5; i++) {
            v.trns(Time.time + i*72, 5.0f);

            Drawf.tri(b.x + v.x, b.y + v.y, 4.0f, 8.0f, v.angle());
            Drawf.tri(b.x + v.x, b.y + v.y, 4.0f, 8.0f, 180 + v.angle());

            v.set(0, 0);
            v.trns(32 + Time.time + i*72, 5.0f);
            Drawf.tri(b.x + v.x, b.y + v.y, 2.5f, 6.0f, v.angle());
            Drawf.tri(b.x + v.x, b.y + v.y, 2.5f, 6.0f, v.angle() + 180);
        }
    }

    @Override
    public void despawned(Bullet b) {
        this.despawnEffect.at(b.x, b.y, b.rotation(), this.hitColor);
        this.hitSound.at(b);

        Effect.shake(this.despawnShake, this.despawnShake, b);

        this.hit(b, b.x, b.y);
    }

    @Override
    public void hit(Bullet b, float x, float y) {
        this.hitEffect.at(b.x, b.y, b.rotation(), this.hitColor);
        this.hitSound.at(b);

        Effect.shake(this.hitShake, this.hitShake, b);

        Seq<Bullet> spikes = new Seq<>();
        Teamc target = Units.closestTarget(b.team, b.x, b.y, 120.0f,
                e -> (e.isGrounded() && this.collidesGround) || (e.isFlying() && this.collidesAir),
                t -> this.collidesGround);

        Vec2 v = new Vec2();
        for(int i = 0; i < 5; i++) {
            v.trns(i*72, 10.0f);

            Call.createBullet(new SpikeBullet(), b.team, b.x + v.x, b.y + v.y, v.angle(), 160.0f, 0.1f, 2.5f);

            v.set(0, 0);
            v.trns(36+i*72, 10.0f);
            Bullet sp = new SpikeBullet().create(null, b.team, b.x + v.x, b.y + v.y, v.angle(), 240.0f, 0.05f, 6.0f, null);

            spikes.add(sp);
        };

        for(int i = 0; i < spikes.size; i++) {
            spikes.get(i).data = new Object[]{target, true, spikes.get(i).rotation()};
        }
    }
}
