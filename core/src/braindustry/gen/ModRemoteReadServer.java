package braindustry.gen;

import arc.util.io.Reads;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.RemoteReadServer;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;

public class ModRemoteReadServer {
    public static void readPacket(Reads read, byte id, Player player) {
        if (id < 0) {
            switch (id) {
                case -1:
                    try {
                        UnitType type = TypeIO.readUnitType(read);
                        float x = read.f(), y = read.f();
                        int amount = read.i();
                        Team team = TypeIO.readTeam(read);
                        UnitController controller = TypeIO.readController(read, null);
                        boolean spawnedByCore = read.bool();
                        ModNetServer.spawnUnits(type, x, y, amount, spawnedByCore, team, controller);
                    } catch (java.lang.Exception e) {
                        throw new java.lang.RuntimeException("Failed to read remote method 'entitySnapshot'!", e);
                    }
                case -2:
            }
        } else {
            RemoteReadServer.readPacket(read, id, player);
        }
    }
}
