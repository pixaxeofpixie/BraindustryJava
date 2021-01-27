package mindustryAddition.gen;

import Gas.gen.Cloud;
import ModVars.modFunc;
import ModVars.modVars;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.entities.Advanced.AdvancedLegsUnit;
import braindustry.entities.Advanced.AdvancedPayloadUnit;
import braindustry.entities.Advanced.AdvancedUnitWaterMove;
import braindustry.entities.AmmoDistributeUnit;
import braindustry.entities.PowerGeneratorUnit;
import braindustry.gen.StealthMechUnit;
import mindustry.gen.*;
import mindustryAddition.versions.ModEntityc;

import java.lang.reflect.Constructor;

public class ModEntityMapping {
    public static Prov[] idMap = new Prov[256];
    public static ObjectMap<String, Prov> nameMap = new ObjectMap();
    private static ObjectMap<Class<?>,Integer> classIdMap=new ObjectMap<>();
    public static Prov map(int id) {
        return idMap[id];
    }

    public static Prov map(String name) {
        return (Prov)nameMap.get(name);
    }
    public static int getId(Class<?> name){
        return classIdMap.get(name,-1);
    }
    static {
        lastClass=0;
        mapClasses(
                AmmoDistributeUnit.class,
                PowerGeneratorUnit.class,
                AdvancedLegsUnit.class,
                AdvancedPayloadUnit.class,
                AdvancedUnitWaterMove.class,
                StealthMechUnit.class,
                Cloud.class,
                null
        );
    }
    public static  <T> void mapClasses(Class<? extends ModEntityc>... objClasses) {
        Seq.with(objClasses).each(ModEntityMapping::mapClass);
    }
    private static int lastClass;
    public static  <T> void mapClass(Class<? extends ModEntityc> objClass) {
        try {
            if (objClass==null)return;
            Constructor<?> cons = objClass.getDeclaredConstructor();
            objClass.getField("classId").setInt(objClass,lastClass);
            Log.info("@ @",lastClass,objClass.getName());
//            classIdMap.put(objClass,lastClass);
            idMap[lastClass++] = () -> {
                try {
                    return cons.newInstance();
                } catch (Exception var3) {
                    throw new RuntimeException(var3);
                }
            };
            nameMap.put(objClass.getName(),() -> {
                try {
                    return cons.newInstance();
                } catch (Exception var3) {
                    throw new RuntimeException(var3);
                }
            });
        } catch (Exception e) {
            modFunc.showException(e);
        }
    }
}
