package ModVars;

import ModVars.Classes.ModAssets;
import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModSettings;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.ModUI;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.Events;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import braindustry.ModListener;
import braindustry.core.ModLogic;
import braindustry.core.ModNetClient;
import braindustry.gen.ModBuilding;
import braindustry.gen.ModNetServer;
import braindustry.gen.ModRemoteReadClient;
import braindustry.gen.ModRemoteReadServer;
import braindustry.input.ModKeyBinds;
import mindustry.ClientLauncher;
import mindustry.gen.Building;
import mindustry.gen.EntityMapping;
import mindustry.gen.Player;
import mindustry.io.SaveIO;
import mindustry.mod.Mods;
import mindustry.net.Packets;
import mindustry.net.ValidateException;
import braindustry.world.ModSave4;

import java.lang.reflect.Constructor;

import static arc.util.Log.debug;
import static mindustry.Vars.net;
import static mindustry.Vars.ui;

public class modVars {
    public static final byte MOD_CONTENT_ID = 66;
    public static final String successfulMessage = "\n\nEE__EE\n\n";
    private static final int classOffset = 40;
    public static ModSettings settings;
    public static ModAtlas modAtlas;
    public static ModAssets modAssets;
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
    public static ClientLauncher launcher;
    public static boolean loaded = false;
    public static boolean packSprites;
    private static int lastClass = 0;

    public static void init() {
        ModSave4 save4 = new ModSave4();
        Events.on(Object[].class, (array) -> {
            try {
                if (array[0].equals("net.handleClient")) {
                    ModRemoteReadClient.readPacket((Reads) array[1], (byte) array[2]);
                } else if (array[0].equals("net.handleServer")) {
                    ModRemoteReadServer.readPacket((Reads) array[1], (byte) array[2], (Player) array[3]);
                } else {
                    return;
                }
                throw new RuntimeException(successfulMessage);
            } catch (ClassCastException ignored) {
            }
        });
        net.handleClient(Packets.InvokePacket.class, packet -> {
            Events.fire(new Object[]{"net.handleClient", packet.reader(), packet.type});

        });
        net.handleServer(Packets.InvokePacket.class, (con, packet) -> {
            if (con.player == null) return;
            try {
//                ModRemoteReadServer.readPacket(packet.reader(), packet.type, con.player);
                Events.fire(new Object[]{"net.handleServer", packet.reader(), packet.type,con.player});
            } catch (ValidateException e) {
                debug("Validation failed for '@': @", e.player, e.getMessage());
            } catch (RuntimeException e) {
                if (e.getMessage().equals(successfulMessage)) return;
                if (e.getCause() instanceof ValidateException) {
                    debug("Validation failed for '@': @", ((ValidateException) e.getCause()).player, e.getCause().getMessage());
                } else {
                    throw e;
                }
            }
        });
            if (false) {
                for (int i = 0; i < EntityMapping.idMap.length; i++) {
                    Prov prov = EntityMapping.idMap[i];
                    if (prov == null) continue;
                    Object o = prov.get();
                    if (o instanceof Building && !(o instanceof ModBuilding)) {
                        EntityMapping.idMap[i] = ModBuilding::new;
                    }
                }
                Seq<Runnable> runners = new Seq<>();
                for (ObjectMap.Entry<String, Prov> entry : EntityMapping.nameMap) {
                    if (entry.value == null) continue;
                    Object o = entry.value.get();
                    if (o instanceof Building && !(o instanceof ModBuilding)) {
                        runners.add(() -> {
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

        public static void load () {
            modUI = new ModUI();
            settings = new ModSettings();
            modAssets = new ModAssets();

            ModListener.load();
            listener.add(netClient = new ModNetClient());
            listener.add(netServer = new ModNetServer());
            listener.add(logic = new ModLogic());
        }

        private static <T > void mapClasses (Class < ? >...objClasses){
            Seq.with(objClasses).each(modVars::mapClass);
        }

        private static <T > void mapClass (Class < ? > objClass){
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

        public static boolean showCheatMenu () {
//        if (Vars.player.isLocal()) return ui.hudfrag.shown;
            return ui.hudfrag.shown && netClient.showCheatMenu();
        }
    }
