package braindustry.world.blocks.Wall;

import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;

public class LaserReflectionWall extends Wall {
    public LaserReflectionWall(String name) {
        super(name);
        absorbLasers=true;
    }
    public class LaserReflectionWallBuild extends WallBuild {
        @Override
        public boolean collision(Bullet bullet) {

            if (bullet.team==team )return false;
            if (bullet.type instanceof LaserBulletType) {
                float delta = Time.delta;

                LaserBulletType type = (LaserBulletType) bullet.type;
                float length= Damage.findLaserLength(bullet, type.length)*0.75f;
                float angle=bullet.rotation();
                Tmp.v2.trns(bullet.rotation()+135f,length,length);
//                Tmp.v1.set(bullet.x+bullet.vel.x* delta,bullet.y+bullet.vel.y*delta);
                Tmp.v1.set(bullet.x-Tmp.v2.x,bullet.y-Tmp.v2.y);
//                Tmp.v1.set(bullet.x,bullet.y);
                Bullet b = bullet.type.create(
                        this,
                        team,
                        Tmp.v1.x,
                        Tmp.v1.y,
                        0,
                        bullet.damage,
                        1,
                        1,
                         null
                );
                b.lifetime=bullet.lifetime;
                b.time=bullet.time;
                float rotate=(180f-angle)*2f;
                b.vel.set(bullet.vel.cpy()).rotate(rotate);
            }

            /*if (bullet.type instanceof LightningBulletType) {
                bullet.vel.rotate90(bullet.x>x?-1:1);
                return super.collision(bullet);
            }*/

            return super.collision(bullet);
        }
    }
}
