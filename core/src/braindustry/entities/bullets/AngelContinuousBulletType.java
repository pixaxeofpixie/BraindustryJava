package braindustry.entities.bullets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Time;
import braindustry.graphics.ModShaders;
import braindustry.type.LengthBulletType;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Velc;

public class AngelContinuousBulletType extends ContinuousLaserBulletType implements LengthBulletType {
    public Color firstColor=Color.black.cpy();
    public Color secondColor=Color.white.cpy();
    {
        speed=1f;
    }

    @Override
    public Bullet create(Entityc owner, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data) {
        Bullet bullet =new Bullet(){
            @Override
            public void update() {
                type.update(this);
            }

        };
        bullet.type = this;
        bullet.owner = owner;
        bullet.team = team;
        bullet.time = 0.0F;
        bullet.vel.trns(angle, this.speed * velocityScl);
        if (this.backMove) {
            bullet.set(x - bullet.vel.x * Time.delta, y - bullet.vel.y * Time.delta);
        } else {
            bullet.set(x, y);
        }

        bullet.lifetime = this.lifetime * lifetimeScl;
        bullet.data = data;
        bullet.drag = this.drag;
        bullet.hitSize = this.hitSize;
        bullet.damage = (damage < 0.0F ? this.damage : damage) * bullet.damageMultiplier();
        bullet.add();
        Velc v;
        if (this.keepVelocity && owner instanceof Velc && (v = (Velc)owner) == (Velc)owner) {
            bullet.vel.add(v.vel().x, v.vel().y);
        }

        return bullet;
    }
    protected void classicUpdate(Bullet b) {
//       b.move(b.vel.x * Time.delta,b.vel.y * Time.delta);
//       b.vel.scl(Mathf.clamp(1.0F -b.drag * Time.delta));
        if (b.type.collidesTiles &&b.type.collides &&b.type.collidesGround) {
            Vars.world.raycastEach(World.toTile(b.lastX()), World.toTile(b.lastY()),b.tileX(),b.tileY(), (x, y) -> {
                Building tile = Vars.world.build(x, y);
                if (tile != null &&b.isAdded()) {
                    if (!tile.collide(b) || !b.type.testCollision(b, tile) || tile.dead() || !b.type.collidesTeam && tile.team ==b.team ||b.type.pierceBuilding &&b.collided.contains(tile.id)) {
                        return false;
                    } else {
                        boolean remove = false;
                        float health = tile.health;
                        if (tile.team !=b.team) {
                            remove = tile.collision(b);
                        }

                        if (remove ||b.type.collidesTeam) {
                            if (!b.type.pierceBuilding) {
                               b.remove();
                            } else {
                               b.collided.add(tile.id);
                            }
                        }

                       b.type.hitTile(b, tile, health, true);
                        return !b.type.pierceBuilding;
                    }
                } else {
                    return false;
                }
            });
        }

        if (b.type.pierceCap != -1 &&b.collided.size >=b.type.pierceCap) {
           b.remove();
        }

       b.time = Math.min(b.time + Time.delta,b.lifetime);
        if (b.time >=b.lifetime) {
           b.remove();
        }

    }
    @Override
    public void update(Bullet b) {
        classicUpdate(b);
        if (b.timer(1, 5.0F)) {
            Damage.collideLine(b, b.team, this.hitEffect, b.x, b.y, b.rotation(), this.length, this.largeHit);
        }

        if (this.shake > 0.0F) {
            Effect.shake(this.shake, this.shake, b);
        }
    }

    @Override
    public void draw(Bullet b) {
        Draw.draw(Draw.z(),()->{
            ModShaders.gradientLaserShader.set(b, this, firstColor,secondColor);
            super.draw(b);
            Draw.shader();
        });
    }

    @Override
    public float length() {
        return length;
    }
}