package braindustry.gen;

import ModVars.ModEnums;
import ModVars.modVars;
import arc.ApplicationListener;
import arc.Events;
import arc.func.Cons;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.*;
import arc.util.io.ReusableByteOutStream;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import mindustry.annotations.Annotations;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.game.Teams;
import mindustry.gen.*;
import mindustry.net.NetConnection;
import mindustry.type.UnitType;
import mindustry.world.blocks.storage.CoreBlock;
import mindustryAddition.gen.ModEntityMapping;
import mindustryAddition.versions.ModEntityc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.FloatBuffer;

import static ModVars.modFunc.*;
import static mindustry.Vars.*;

public class ModNetServer implements ApplicationListener {
    /** note that snapshots are compressed, so the max snapshot size here is above the typical UDP safe limit */
    private static final int maxSnapshotSize = 800, timerBlockSync = 0;
    private static final Rect viewport = new Rect();
    /** If a player goes away of their server-side coordinates by this distance, they get teleported back. */
    private ReusableByteOutStream writeBuffer = new ReusableByteOutStream(127);
    private Writes outputBuffer = new Writes(new DataOutputStream(writeBuffer));
    /** Stream for writing player sync data to. */
    private ReusableByteOutStream syncStream = new ReusableByteOutStream();
    /** Data stream for writing player sync data to. */
    private DataOutputStream dataStream = new DataOutputStream(syncStream);

    protected static final float serverSyncTime=12;
    private static boolean cheating(Player player){
        if (!net.active())return modVars.settings.cheating();
        ModEnums.CheatLevel cheatLevel = modVars.settings.cheatLevel();
        return (cheatLevel== ModEnums.CheatLevel.onlyAdmins && player.admin()) || ModEnums.CheatLevel.all==cheatLevel;
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void setTeam(Player player,Team team){
        if (!cheating(player))return;
        player.team(team);
    }
    private static void stealthAction(Cons<Unit> cons,boolean inStealth){
        Groups.all.each(entityc -> {
            if (entityc instanceof StealthUnitc && (!inStealth || ((StealthUnitc) entityc).inStealth())){
                cons.get((Unit)entityc);
            }
        });
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void damageAllUnits(Player player){
        Groups.unit.each(unit -> unit.damage(unit.health - 1));
        stealthAction((unit)->{
            unit.damage(unit.health - 1);
        },true);
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void killAllUnits(Player player){
        Groups.unit.each(unit -> unit.kill());
        stealthAction((unit)->{
            unit.kill();
        },true);
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void healAllUnits(Player player){
        Groups.unit.each(unit -> unit.heal());
        stealthAction((unit)->{
            unit.heal();
        },true);
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void tpAllUnits(Player player,Vec2 pos){
        Groups.unit.each(unit -> unit.set(pos));
        stealthAction((unit)->{
            unit.set(pos);
        },true);
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void setUnit(Player player,Unit unit){
        if (!cheating(player))return;
        player.unit(unit);
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void setNewUnit(Player player,UnitType type){
//        Log.info("try to create unit: @ for--- @",type,player);
        if (!cheating(player))return;
//        Log.info("try to create unit: @ for @",type,player.name());
        Unit unit=type.spawn(player.team(),player.x,player.y);
        unit.spawnedByCore=true;
        player.unit().spawnedByCore=true;
        player.unit(unit);
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void spawnUnits(Player player,UnitType type, float x, float y, int amount, boolean spawnerByCore, Team team){
        for (int i = 0; i < amount; i++) {
            Unit unit=type.spawn(team==null?Team.derelict:team,x,y);
            unit.spawnedByCore(spawnerByCore);

        }
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.server)
    public static void setStealthStatus(Player player,Unit unit,boolean inStealth,float value){
        if (unit instanceof StealthUnitc){
            StealthUnitc stealthUnit=(StealthUnitc)unit;
            if (inStealth){
                if (value==-1) {
                    stealthUnit.setStealth();
                } else {
                    stealthUnit.setStealth(value);
                }
            } else {
                if (value==-1) {
                    stealthUnit.removeStealth();
                } else {
                    stealthUnit.removeStealth(value);
                }
            }
//            stealthUnit.inStealth(inStealth);
        }
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.client,called = Annotations.Loc.both)
    public static void checkStealthStatus(Player player,Unit unit,boolean inStealth){
        if (unit instanceof StealthUnitc){
            if (inStealth){
                while (Groups.unit.contains(u -> u == unit)) {
                    Groups.unit.remove(unit);
                }
            } else if (!Groups.unit.contains(u -> u == unit)) {
                Groups.unit.add(unit);
            }
//            stealthUnit.inStealth(inStealth);
        }
    }

    public void registerCommands(CommandHandler handler) {
        Events.on(EventType.PlayerConnect.class,e->{
            ModCall.setServerCheatLevel(modVars.settings.cheatLevel().ordinal());
        });
        handler.register("cheats","[value]","Set cheat level or return it.",(args)->{
            if (args.length==0){
                Log.info("Cheat level: @",modVars.settings.cheatLevel());
                return;
            }
            String value=args[0];
           if (!ModEnums.contains(ModEnums.CheatLevel.class,value)){
               Log.info("Cheat levels: @", Seq.with(ModEnums.CheatLevel.values()).toString(", "));
               return;
           }
            modVars.settings.cheatLevel(ModEnums.CheatLevel.valueOf(value));
            Log.info("Cheat level updated to @",modVars.settings.cheatLevel());
            ModCall.setServerCheatLevel(modVars.settings.cheatLevel().ordinal());
        });
    }

    @Override
    public void update() {
        if (state.isGame() && net.server()){
            sync();

        }
    }
    protected void sync(){
        Groups.player.each(p -> !p.isLocal(),player->{
//            Log.info("sync: @",player.name());
            if(player.con == null || !player.con.isConnected()){
                return;
            }
            player.timer().getTimes()[0]= Time.time;
            NetConnection connection = player.con;
            if(!player.timer(1, serverSyncTime) || !connection.hasConnected) return;

            try{
                writeEntitySnapshot(player);
            }catch(IOException e){
                e.printStackTrace();
            }
        });
    }
    public void writeEntitySnapshot(Player player) throws IOException{
        syncStream.reset();
        int sum = state.teams.present.sum(t -> t.cores.size);

        dataStream.writeInt(sum);

        for(Teams.TeamData data : state.teams.present){
            for(CoreBlock.CoreBuild entity : data.cores){
                dataStream.writeInt(entity.tile.pos());
                entity.items.write(Writes.get(dataStream));
            }
        }

        dataStream.close();
        byte[] stateBytes = syncStream.toByteArray();

        //write basic state data.
        Call.stateSnapshot(player.con, state.wavetime, state.wave, state.enemies, state.serverPaused, state.gameOver, universe.seconds(), (short)stateBytes.length, net.compressSnapshot(stateBytes));

        viewport.setSize(player.con.viewWidth, player.con.viewHeight).setCenter(player.con.viewX, player.con.viewY);

        syncStream.reset();

        int sent = 0;

        for(Syncc entity : Groups.sync){
            //write all entities now
            dataStream.writeInt(entity.id()); //write id
            dataStream.writeByte(entity.classId()); //write type ID
            if(entity instanceof ModEntityc){
                dataStream.writeShort(ModEntityMapping.getId(entity.getClass()));
            }
            entity.writeSync(Writes.get(dataStream)); //write entity

            sent++;

            if(syncStream.size() > maxSnapshotSize){
                dataStream.close();
                byte[] syncBytes = syncStream.toByteArray();
                Call.entitySnapshot(player.con, (short)sent, (short)syncBytes.length, net.compressSnapshot(syncBytes));
                sent = 0;
                syncStream.reset();
            }
        }

        if(sent > 0){
            dataStream.close();

            byte[] syncBytes = syncStream.toByteArray();
            Call.entitySnapshot(player.con, (short)sent, (short)syncBytes.length, net.compressSnapshot(syncBytes));
        }

    }
}
