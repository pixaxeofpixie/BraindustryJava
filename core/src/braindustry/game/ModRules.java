package braindustry.game;

import arc.scene.ui.layout.Table;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.struct.StringMap;
import braindustry.annotations.ModAnnotations;
import mindustry.annotations.util.Svar;
import mindustry.game.Rules;
import mindustry.type.Sector;

@ModAnnotations.Rules
@ModAnnotations.RulesTable
public class ModRules extends Rules {

    public static boolean check(Object var){
        return !(var instanceof StringMap || var instanceof ObjectSet || var instanceof Seq || var instanceof Sector);
    }
}
