package braindustry.entities.bullets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import braindustry.graphics.ModShaders;
import braindustry.type.LengthBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.gen.Bullet;

public class AngelContinuousBulletType extends ContinuousLaserBulletType implements LengthBulletType {
    @Override
    public void draw(Bullet b) {
        Draw.draw(Draw.z(),()->{
            ModShaders.gradientLaserShader.set(b, this, Color.acid,Color.black);
            super.draw(b);
            Draw.shader();
        });
    }

    @Override
    public float length() {
        return length;
    }
}