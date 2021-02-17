package ModVars;

import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModSettings;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.ModUI;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.ModListener;
import braindustry.core.ModLogic;
import braindustry.core.ModNetClient;
import braindustry.gen.*;
import braindustry.input.ModKeyBinds;
import mindustry.ClientLauncher;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.EntityMapping;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;
import mindustry.io.SaveIO;
import mindustry.mod.Mods;
import mindustry.net.Packets;
import mindustry.net.ValidateException;
import mindustry.type.UnitType;
import mindustryAddition.versions.ModSave4;

import java.lang.reflect.Constructor;

import static arc.util.Log.debug;
import static mindustry.Vars.net;
import static mindustry.Vars.ui;

public class modVars {
    public static final byte MOD_CONTENT_ID = 66;
    private static final int classOffset = 40;
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static Mods.LoadedMod modInfo;
    public static ModKeyBinds keyBinds;
    public static ModControlsDialog controls;
    public static ModOtherSettingsDialog otherSettingsDialog;
    public static ModSettingsDialog settingsDialog;
    public static ModNetClient netClient;
    public static ModNetServer netServer;
    public static ModUI modUI;
    public static ModLogic logic;
    public static ModListener listener;
    public  static ClientLauncher launcher;
    public static boolean loaded = false;
    public static boolean packSprites;
    private static int lastClass = 0;

    public static void init() {
        ModSave4 save4 = new ModSave4();
        net.handleClient(Packets.InvokePacket.class, packet -> {
            ModRemoteReadClient.readPacket(packet.reader(), packet.type);
        });
        net.handleServer(Packets.InvokePacket.class, (con, packet) -> {
            if (con.player == null) return;
            try {
                ModRemoteReadServer.readPacket(packet.reader(), packet.type, con.player);
            } catch (ValidateException e) {
                debug("Validation failed for '@': @", e.player, e.getMessage());
            } catch (RuntimeException e) {
                if (e.getCause() instanceof ValidateException) {
                    debug("Validation failed for '@': @", ((ValidateException) e.getCause()).player, e.getCause().getMessage());
                } else {
                    throw e;
                }
            }
        });
        if (false){
            for (int i = 0; i < EntityMapping.idMap.length; i++) {
                Prov prov = EntityMapping.idMap[i];
                if (prov==null)continue;
                Object o = prov.get();
                if (o instanceof Building && !(o instanceof ModBuilding)){
                    EntityMapping.idMap[i]=ModBuilding::new;
                }
            }
            Seq<Runnable> runners=new Seq<>();
            for (ObjectMap.Entry<String, Prov> entry : EntityMapping.nameMap) {
                if (entry.value==null)continue;
                Object o = entry.value.get();
                if (o instanceof Building && !(o instanceof ModBuilding)){
                    runners.add(()->{
                        EntityMapping.nameMap.put(entry.key, ModBuilding::new);
                    });
                }
            }
            runners.each(Runnable::run);
            runners.clear();
        }
        SaveIO.versionArray.add(save4);
        SaveIO.versions.remove(save4.version);
        SaveIO.versions.put(save4.version, save4);
        modUI.init();

    }

    public static void load() {
        modUI = new ModUI();
        settings = new ModSettings();

        ModListener.load();
        listener.add(netClient = new ModNetClient());
        listener.add(netServer = new ModNetServer());
        listener.add(logic=new ModLogic());
    }

    private static <T> void mapClasses(Class<?>... objClasses) {
        Seq.with(objClasses).each(modVars::mapClass);
    }

    private static <T> void mapClass(Class<?> objClass) {
        try {
            if (objClass == null) return;
            Constructor<?> cons = objClass.getDeclaredConstructor();
            objClass.getField("classId").setInt(objClass, classOffset + lastClass);
            EntityMapping.idMap[classOffset + lastClass++] = () -> {
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

    public static boolean showCheatMenu() {
//        if (Vars.player.isLocal()) return ui.hudfrag.shown;
        return ui.hudfrag.shown && netClient.showCheatMenu();
    }
}
