package ModVars;

import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModSettings;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.ModUI;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.math.Mathf;
import arc.struct.Seq;
import braindustry.core.ModNetClient;
import braindustry.gen.ModNetServer;
import braindustry.gen.ModRemoteReadClient;
import braindustry.gen.ModRemoteReadServer;
import braindustry.input.ModBinding;
import braindustry.input.ModKeyBinds;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.game.Team;
import mindustry.gen.EntityMapping;
import mindustry.io.SaveIO;
import mindustry.mod.Mods;
import mindustry.net.Packets;
import mindustry.net.ValidateException;
import mindustry.type.UnitType;
import mindustryAddition.versions.ModSave4;

import java.lang.reflect.Constructor;

import static arc.util.Log.debug;
import static mindustry.Vars.net;

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
    public static boolean loaded = false;
    private static int lastClass = 0;

    public static void init() {
        ModSave4 save4 = new ModSave4();
        net.handleClient(Packets.InvokePacket.class, packet -> {
            ModRemoteReadClient.readPacket(packet.reader(), packet.type);
        });
        net.handleServer(Packets.InvokePacket.class, (con, packet) -> {
            if(con.player == null) return;
            try{
                ModRemoteReadServer.readPacket(packet.reader(), packet.type, con.player);
            }catch(ValidateException e){
                debug("Validation failed for '@': @", e.player, e.getMessage());
            }catch(RuntimeException e){
                if(e.getCause() instanceof ValidateException){
                    debug("Validation failed for '@': @", ((ValidateException) e.getCause()).player, e.getCause().getMessage());
                }else{
                    throw e;
                }
            }
        });
        Vars.netServer.addPacketHandler("spawn unit", (player, line) -> {
            try {
                String[] args = line.split(" ");
//                print("args: @", line, Strings.join("-", args));
                if (args.length == 5) {
                    UnitType type = Vars.content.getByID(ContentType.unit, Integer.parseInt(args[0]));
                    float x = Float.parseFloat(args[1]);
                    float y = Float.parseFloat(args[2]);
                    int amount = Integer.parseInt(args[3]);
                    Team team = Team.get(Integer.parseInt(args[4]));
                    for (int i = 0; i < amount; i++) {
                        type.spawn(team, x, y);
                    }
                }
            } catch (Exception e) {

            }
        });
        for (int i = 0; i < EntityMapping.idMap.length; i++) {

        }
        SaveIO.versionArray.add(save4);
        SaveIO.versions.remove(save4.version);
        SaveIO.versions.put(save4.version, save4);
        modUI.init();

    }

    public static void load() {
        modUI=new ModUI();
        netClient = new ModNetClient();
        netServer=new ModNetServer();
        settings = new ModSettings();
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

}
