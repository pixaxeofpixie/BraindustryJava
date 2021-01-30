package braindustry.core;

import ModVars.modVars;
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

import static mindustry.Vars.*;
import static mindustry.Vars.netClient;

public class ModNetClient {
    protected ReusableByteInStream byteStream = new ReusableByteInStream();
    protected DataInputStream dataStream = new DataInputStream(byteStream);

    @ModAnnotations.Remote(variants = Annotations.Variant.one, priority = Annotations.PacketPriority.low, unreliable = true,replaceLevel = 18)
    public static void entitySnapshot(short amount, short dataLen, byte[] data) {
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
                int classId=255;
                if (typeID == modVars.MOD_CONTENT_ID) {
                    classId = input.readShort();
                }
                if (entity == null) {
                    Log.info("c: @, t: @",classId,typeID);
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

    public void registerCommands(CommandHandler handler) {
    }

    public boolean showCheatMenu() {
        return true;
    }
}
