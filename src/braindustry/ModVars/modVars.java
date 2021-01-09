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
        mapClass(AmmoDistributeUnit.class,0);
        mapClass(PowerGeneratorUnit.class,1);
        mapClass(AdvancedLegsUnit.class,2);
        mapClass(AdvancedPayloadUnit.class,3);
        mapClass(AdvancedUnitWaterMove.class,4);
        mapClass(StealthMechUnit.class,5);
        settings = new ModSettings();
    }
    private static  <T> void mapClass(Class<?> objClass) {
        mapClass(objClass,lastClass++);
    }
    private static  <T> void mapClass(Class<?> objClass,int offset) {
        try {
            Constructor<?> cons = objClass.getDeclaredConstructor();
            objClass.getField("classId").setInt(objClass,classOffset+offset);
            EntityMapping.idMap[classOffset+offset] = () -> {
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
