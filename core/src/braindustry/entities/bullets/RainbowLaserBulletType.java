package braindustry.entities.bullets;

import arc.graphics.g2d.Draw;
import braindustry.graphics.ModShaders;
import braindustry.type.LengthBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.gen.Bullet;

public class RainbowLaserBulletType extends LaserBulletType implements LengthBulletType {
    @Override
    public void draw(Bullet b) {
        Draw.draw(Draw.z(),()->{
            ModShaders.rainbowLaserShader.set(b,this);
            super.draw(b);
            Draw.shader();
        });
    }

    @Override
    public float length() {
        return length;
    }
}