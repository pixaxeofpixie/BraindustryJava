package braindustry.audio;

import arc.*;
import arc.audio.*;
import arc.audio.Filters.*;
import arc.files.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.UnitType;
import mindustry.audio.SoundControl;


import static mindustry.Vars.*;

public class ModSoundControl extends SoundControl {

    //arc.Core.assets.load("music/frozenIslands.mp3", arc.audio.Music.class).loaded = a -> frozenIslands = (arc.audio.Music)a;

    protected static final float musicChance = 0.9f;

    @Override

    protected void reload(){
        current = null;
        fade = 0f;
        ambientMusic = Seq.with(Musics.game1, Musics.game3, Musics.game6, Musics.game8, Musics.game9);
        darkMusic = Seq.with(Musics.game2, Musics.game5, Musics.game7, Musics.game4 /*frozenIslands*/);
        bossMusic = Seq.with(Musics.boss1, Musics.boss2, Musics.game2, Musics.game5);

        //setup UI bus for all sounds that are in the UI folder
        for(var sound : Core.assets.getAll(Sound.class, new Seq<>())){
            var file = Fi.get(Core.assets.getAssetFileName(sound));
            if(file.parent().name().equals("ui")){
                sound.setBus(uiBus);
            }
        }
    }

}
