package braindustry.entities.bullets;

import arc.graphics.g2d.Draw;
import braindustry.graphics.ModShaders;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.gen.Bullet;

public class AngelContinuousBulletType extends ContinuousLaserBulletType {
    @Override
    public void draw(Bullet b) {
        Draw.draw(Draw.z(),()->{
            ModShaders.gradientLaserShader.set(b, this);
            super.draw(b);
            Draw.shader();
        });
    }
}