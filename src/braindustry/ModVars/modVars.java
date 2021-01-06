package braindustry.ModVars;

import arc.struct.ObjectMap;
import braindustry.ModVars.Classes.ModAtlas;
import braindustry.ModVars.Classes.ModSettings;
import braindustry.entities.Advanced.AdvancedLegsUnit;
import braindustry.entities.Advanced.AdvancedPayloadUnit;
import braindustry.entities.Advanced.AdvancedUnitWaterMove;
import braindustry.entities.AmmoDistributeUnit;
import braindustry.entities.PowerGeneratorUnit;
import braindustry.gen.StealthMechUnit;
import mindustry.gen.EntityMapping;
import mindustry.mod.Mods;

import java.lang.reflect.Constructor;

public class modVars {
    private static final int classOffset = 40;
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static Mods.LoadedMod modInfo;
    private static int lastClass = 0;

    public static void load() {
        mapClass(AmmoDistributeUnit.class);
        mapClass(PowerGeneratorUnit.class);
        mapClass(AdvancedLegsUnit.class);
        mapClass(AdvancedPayloadUnit.class);
        mapClass(AdvancedUnitWaterMove.class);
        mapClass(StealthMechUnit.class);
        settings = new ModSettings();
    }
    private static  <T> void mapClass(Class<?> objClass) {
        try {
            Constructor<?> cons = objClass.getDeclaredConstructor();
            objClass.getField("classId").setInt(objClass,classOffset+lastClass);
            EntityMapping.idMap[classOffset+lastClass++] = () -> {
                try {
                    return cons.newInstance();
                } catch (Exception var3) {
                    throw new RuntimeException(var3);
                }
            };
        } catch (Exception e) {
            modFunc.showException(e);
        }
    }

}
