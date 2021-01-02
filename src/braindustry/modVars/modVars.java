package braindustry.modVars;

import braindustry.modVars.Classes.ModSettings;
import mindustry.mod.Mods;

public class modVars {
    public static ModSettings settings;
    public static Mods.LoadedMod modInfo;
    public static void load(){
        settings=new ModSettings();
    }
}
