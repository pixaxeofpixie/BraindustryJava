package braindustry.entities.bullets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import braindustry.graphics.ModShaders;
import braindustry.type.LengthBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;

public class AngelContinuousBulletType extends ContinuousLaserBulletType implements LengthBulletType {
    public Color firstColor=Color.black.cpy();
    public Color secondColor=Color.white.cpy();
    {
        speed=1f;
    }

    @Override
    public Bullet create(Entityc owner, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data) {
        Bullet bullet = super.create(owner, team, x, y, angle, damage, velocityScl, lifetimeScl, data);
        bullet.rotation(angle);
        return bullet;
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