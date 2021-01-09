package braindustry.world.blocks.Wall;

import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Time;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.entities.Lightning;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.world.blocks.defense.Wall;

public class LaserReflectionWall extends Wall {
    public LaserReflectionWall(String name) {
        super(name);
    }

    public class LaserReflectionWallBuild extends WallBuild {
        @Override
        public boolean collision(Bullet bullet) {
            if (bullet.type instanceof LaserBulletType) {
                Bullet bullet1 = bullet.type.create(
                        bullet.owner,
                        team,
                        x,
                        y,
                        0,
                        bullet.damage,
                        1,
                        1,
                        (Object) null
                );

                bullet1.timer = bullet.timer;
                bullet1.vel.set(bullet.vel.inv());
                return super.collision(bullet);
            }

//            if (bullet.type instanceof LightningBulletType) {
//                bullet.vel.inv();
//                return super.collision(bullet);
//            }

            return super.collision(bullet);
        }
    }
}
