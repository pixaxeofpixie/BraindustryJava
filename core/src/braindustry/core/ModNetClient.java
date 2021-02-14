package braindustry.core;

import ModVars.ModEnums;
import ModVars.modVars;
import arc.ApplicationListener;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.ReusableByteInStream;
import braindustry.annotations.ModAnnotations;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.gen.EntityMapping;
import mindustry.gen.Groups;
import mindustry.gen.Syncc;
import mindustryAddition.gen.ModEntityMapping;

import java.io.DataInputStream;
import java.io.IOException;

import static mindustry.Vars.net;
import static mindustry.Vars.player;

public class ModNetClient implements ApplicationListener {
    protected ReusableByteInStream byteStream = new ReusableByteInStream();
    protected DataInputStream dataStream = new DataInputStream(byteStream);

    @ModAnnotations.Remote(variants = Annotations.Variant.one, priority = Annotations.PacketPriority.low, unreliable = true, replaceLevel = 18)
    public static void entitySnapshot(short amount, short dataLen, byte[] data) {
        if (false) {
            mindustry.core.NetClient.entitySnapshot(amount, dataLen, data);
            return;
        }
        try {
            modVars.netClient.byteStream.setBytes(Vars.net.decompressSnapshot(data, dataLen));
            DataInputStream input = modVars.netClient.dataStream;

            for (int j = 0; j < amount; ++j) {
                int id = input.readInt();
                byte typeID = input.readByte();
                Syncc entity = (Syncc) Groups.sync.getByID(id);
                boolean add = false;
                boolean created = false;
                if (entity == null && id == Vars.player.id()) {
                    entity = Vars.player;
                    add = true;
                }
                int classId = 255;
                if (typeID == modVars.MOD_CONTENT_ID) {
                    classId = input.readShort();
                }
                if (entity == null) {
                    Log.info("c: @, t: @", classId, typeID);
                    if (typeID == modVars.MOD_CONTENT_ID) {
                        entity = (Syncc) ModEntityMapping.map(classId).get();
                    } else {

                        entity = (Syncc) EntityMapping.map(typeID).get();
                    }
                    ((Syncc) entity).id(id);
                    if (!Vars.netClient.isEntityUsed(((Syncc) entity).id())) {
                        add = true;
                    }

                    created = true;
                }

                (entity).readSync(Reads.get(input));
                if (created) {
                    ((Syncc) entity).snapSync();
                }

                if (add) {
                    (entity).add();
                    Vars.netClient.addRemovedEntity((entity).id());
                }
            }

        } catch (IOException var10) {
            throw new RuntimeException(var10);
        }
    }

    @ModAnnotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.client)
    public static void setServerCheatLevel(int level) {
        ModEnums.CheatLevel[] values = ModEnums.CheatLevel.values();
        modVars.settings.cheatLevelServer(values[level % values.length]);
    }

    public void registerCommands(CommandHandler handler) {
    }

    public boolean showCheatMenu() {
        boolean result = true;
        ModEnums.CheatLevel cheatLevel = modVars.settings.cheating() ? ModEnums.CheatLevel.onlyAdmins : ModEnums.CheatLevel.off;
//        Log.info("@ @ @", net.client(),net.server(),net.active());
        if (net.client()) {
            cheatLevel = modVars.settings.cheatLevelServer();
//            Log.info("on server: @", cheatLevel);
        }else if(net.server() && net.active()){
            cheatLevel=modVars.settings.cheatLevel();
        } else {
            return modVars.settings.cheating();
//            Log.info("c-@-@", net.client(),net.server());
        }
        switch (cheatLevel) {
            case off:
                result = false;
                break;

            case onlyAdmins:
                result = player.admin();
                break;
            case all:
                result = true;
                break;

        }
        return result;
    }

    @Override
    public void update() {

    }
}
