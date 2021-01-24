package ModVars;

import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModSettings;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.struct.Seq;
import braindustry.entities.Advanced.AdvancedLegsUnit;
import braindustry.entities.Advanced.AdvancedPayloadUnit;
import braindustry.entities.Advanced.AdvancedUnitWaterMove;
import braindustry.entities.AmmoDistributeUnit;
import braindustry.entities.PowerGeneratorUnit;
import braindustry.gen.StealthMechUnit;
import braindustry.input.ModBinding;
import braindustry.input.ModKeyBinds;
import mindustryAddition.versions.ModSave4;
import mindustry.gen.EntityMapping;
import mindustry.io.SaveIO;
import mindustry.mod.Mods;

import java.lang.reflect.Constructor;

public class modVars {
    private static final int classOffset = 40;
    public static final byte MOD_CONTENT_ID= 66;
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static Mods.LoadedMod modInfo;
    public static ModKeyBinds keyBinds;
    public static ModControlsDialog controls;
    public static ModOtherSettingsDialog otherSettingsDialog;
    public static ModSettingsDialog settingsDialog;
    private static int lastClass = 0;
    public static boolean loaded=false;
    public static void init(){
        ModSave4 save4=new ModSave4();
        SaveIO.versionArray.add(save4);
        SaveIO.versions.remove(save4.version);  
        SaveIO.versions.put(save4.version,save4);
        keyBinds=new ModKeyBinds();
        keyBinds.setDefaults(ModBinding.values());
        keyBinds.load();
        controls=new ModControlsDialog();
        otherSettingsDialog=new ModOtherSettingsDialog();
        settingsDialog=new ModSettingsDialog();

    }
    public static void load() {
        settings = new ModSettings();
    }
    private static  <T> void mapClasses(Class<?>... objClasses) {
        Seq.with(objClasses).each(modVars::mapClass);
    }

    private static  <T> void mapClass(Class<?> objClass) {
        try {
            if (objClass==null)return;
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
