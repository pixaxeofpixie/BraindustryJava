package braindustry.gen;

import ModVars.modFunc;
import arc.util.io.Reads;
import braindustry.core.ModNetClient;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.RemoteReadClient;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;

public class ModRemoteReadClient extends RemoteReadClient {
    public static void readPacket(Reads read, int id) {

        if (id < 0) {
            switch (id){
                case -1:
                case -2:
            }
        } else if (id == 18) {
            try {
                short amount = read.s();
                short dataLen = read.s();
                byte[] data = mindustry.io.TypeIO.readBytes(read);
                ModNetClient.entitySnapshot(amount, dataLen, data);
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException("Failed to read remote method 'entitySnapshot'!", e);
            }
        } else {
            RemoteReadClient.readPacket(read, id);
        }
    }
}
