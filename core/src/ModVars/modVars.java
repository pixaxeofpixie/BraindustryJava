package ModVars;

import ModVars.Classes.ModAssets;
import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModSettings;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.ReusableByteInStream;
import braindustry.ModListener;
import braindustry.core.ModLogic;
import braindustry.core.ModNetClient;
import braindustry.core.ModUI;
import braindustry.gen.ModBuilding;
import braindustry.gen.ModNetServer;
import braindustry.input.ModKeyBinds;
import braindustry.net.ModNet;
import braindustry.world.ModSave4;
import mindustry.ClientLauncher;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.gen.EntityMapping;
import mindustry.io.SaveIO;
import mindustry.mod.Mods;

import java.io.DataInputStream;
import java.lang.reflect.Constructor;

import static mindustry.Vars.*;

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
    static Seq<Content> modContent=new Seq<>();
    public static Seq<Content> getModContent(){
        return modContent.copy();
    }
    public static void addContent(Content content){
        if (!modContent.contains(content)) {
            modContent.add(content);
        }
    }
    private static ReusableByteInStream bin;
    private static Reads read = new Reads(new DataInputStream(bin = new ReusableByteInStream()));
    public static void init() {
        ModSave4 save4 = new ModSave4();
        net=new ModNet(platform.getNet(),net);
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
        netClient.loadNetHandler();
        netServer.loadNetHandler();
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
