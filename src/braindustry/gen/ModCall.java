package braindustry.gen;

import arc.util.Nullable;
import arc.util.io.ReusableByteOutStream;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.core.NetServer;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.io.TypeIO;
import mindustry.net.Packets;
import mindustry.type.UnitType;

import java.io.DataOutputStream;

public class ModCall {
    private static final ReusableByteOutStream OUT = new ReusableByteOutStream(8192);
    private static final Writes write = new Writes(new DataOutputStream(OUT));
    public static void spawnUnits(UnitType type, float x, float y, int amount, boolean spawnerByCore, @Nullable Team team, @Nullable UnitController controller){
        if(mindustry.Vars.net.server() || !mindustry.Vars.net.active()) {
            ModNetServer.spawnUnits(type,x,y,amount,spawnerByCore,team,controller);
        }
        if (Vars.net.client()){
            Packets.InvokePacket packet = Pools.obtain(Packets.InvokePacket.class,Packets.InvokePacket::new);
            packet.priority = (byte)0;
            packet.type = (byte)-1;
            OUT.reset();
            TypeIO.writeUnitType(write,type);
            write.f(x);
            write.f(y);
            write.i(amount);
            TypeIO.writeTeam(write,team==null?Team.derelict:team);
            TypeIO.writeController(write,controller);
            write.bool(spawnerByCore);
            packet.bytes = OUT.getBytes();
            packet.length = OUT.size();
            Vars.net.send(packet, mindustry.net.Net.SendMode.tcp);
        }
    }
}
