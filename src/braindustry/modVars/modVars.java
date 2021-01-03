package braindustry.modVars;

import arc.struct.ObjectMap;
import braindustry.entities.AmmoDistributeUnit;
import braindustry.entities.PowerGeneratorUnit;
import braindustry.modVars.Classes.ModAtlas;
import braindustry.modVars.Classes.ModSettings;
import braindustry.modVars.Classes.TechTreeManager;
import mindustry.gen.EntityMapping;
import mindustry.mod.Mods;

public class modVars {
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static Mods.LoadedMod modInfo;
    public static ObjectMap<Class,Integer> classMap=new ObjectMap<>();
    public static void load(){
        AmmoDistributeUnit.classId=modFunc.addEntityMappingIdMap(AmmoDistributeUnit::new);
        PowerGeneratorUnit.classId=modFunc.addEntityMappingIdMap(PowerGeneratorUnit::new);
        settings=new ModSettings();
    }
}
