package braindustry.content;

import braindustry.entities.bullets.AngelContinuousBulletType;
import mindustry.core.ContentLoader;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.BulletType;

public class ModBullets implements ContentList {
    public static BulletType deathLaser;
    @Override
    public void load() {
        deathLaser=new AngelContinuousBulletType(){
            {

            }
        };
    }
}
