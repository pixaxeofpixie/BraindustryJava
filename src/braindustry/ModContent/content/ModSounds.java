package braindustry.ModContent.content;

import arc.Core;
import arc.audio.Sound;
import mindustry.ctype.ContentList;

public class ModSounds implements ContentList {
    public static Sound electronShoot = new Sound();
    public static Sound electronCharge = new Sound();

    @Override
    public void load() {
        Core.assets.load("sounds/electronShoot.ogg", Sound.class).loaded = (a) -> {
            electronShoot = (Sound)a;
        };

        Core.assets.load("sounds/electronCharge.ogg", Sound.class).loaded = (a) -> {
            electronCharge = (Sound)a;
        };
    }
}
