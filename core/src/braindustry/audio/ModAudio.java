package braindustry.audio;

import arc.audio.Music;
import braindustry.gen.ModMusics;
import mindustry.Vars;

public class ModAudio {
    public static void load() {
       //arc.Core.assets.load("music/frozenIslands.mp3", arc.audio.Music.class).loaded = a -> frozenIslands = (arc.audio.Music)a;
//    SomeMusicLoading
    }

    public static void reload() {
        Vars.control.sound.ambientMusic.add(ModMusics.darknessHarmony);
        Vars.control.sound.bossMusic.add(ModMusics.stellarSonata);
        Vars.control.sound.darkMusic.add(ModMusics.frozenIslands);
    }

}
