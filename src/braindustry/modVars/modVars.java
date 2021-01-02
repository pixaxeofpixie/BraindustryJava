package braindustry.modVars;

import braindustry.modVars.Classes.ModAtlas;
import braindustry.modVars.Classes.ModSettings;
import braindustry.modVars.Classes.TechTreeManager;
import mindustry.mod.Mods;

public class modVars {
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static Mods.LoadedMod modInfo;
    public static void load(){
        settings=new ModSettings();
    }
}
