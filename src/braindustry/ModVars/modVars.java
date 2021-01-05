package braindustry.ModVars;

import arc.struct.ObjectMap;
import braindustry.entities.Advanced.AdvancedLegsUnit;
import braindustry.entities.Advanced.AdvancedPayloadUnit;
import braindustry.entities.Advanced.AdvancedUnitWaterMove;
import braindustry.entities.AmmoDistributeUnit;
import braindustry.entities.PowerGeneratorUnit;
import braindustry.ModVars.Classes.ModAtlas;
import braindustry.ModVars.Classes.ModSettings;
import mindustry.gen.EntityMapping;
import mindustry.mod.Mods;

public class modVars {
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static Mods.LoadedMod modInfo;
    public static ObjectMap<Class,Integer> classMap = new ObjectMap<>();
    public static void load(){
        EntityMapping.idMap[AmmoDistributeUnit.classId] = (AmmoDistributeUnit::new);
        EntityMapping.idMap[PowerGeneratorUnit.classId] = (PowerGeneratorUnit::new);
        EntityMapping.idMap[AdvancedLegsUnit.classId] = (AdvancedLegsUnit::new);
        EntityMapping.idMap[AdvancedPayloadUnit.classId] = (AdvancedPayloadUnit::new);
        EntityMapping.idMap[AdvancedUnitWaterMove.classId] = (AdvancedUnitWaterMove::new);
        settings = new ModSettings();
    }
}
