package mindustryAddition.versions;

import ModVars.modFunc;
import ModVars.modVars;
import arc.math.geom.Point2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.game.Teams;
import mindustry.gen.EntityMapping;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.io.SaveVersion;
import mindustry.io.TypeIO;
import mindustryAddition.gen.ModEntityMapping;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

public class ModSave4 extends SaveVersion {
    public ModSave4() {
        super(4);
    }

    @Override
    public void writeEntities(DataOutput stream) throws IOException {
        Seq<Teams.TeamData> data = Vars.state.teams.getActive().copy();
        if (!data.contains(Team.sharded.data())) {
            data.add(Team.sharded.data());
        }

        stream.writeInt(data.size);
        Iterator var3 = data.iterator();

        while (var3.hasNext()) {
            Teams.TeamData team = (Teams.TeamData) var3.next();
            stream.writeInt(team.team.id);
            stream.writeInt(team.blocks.size);

            for (Teams.BlockPlan block : team.blocks) {
                stream.writeShort(block.x);
                stream.writeShort(block.y);
                stream.writeShort(block.rotation);
                stream.writeShort(block.block);
                TypeIO.writeObject(Writes.get(stream), block.config);
            }
        }

        stream.writeInt(Groups.all.count(Entityc::serialize));
        var3 = Groups.all.iterator();

        while (var3.hasNext()) {
            Entityc entity = (Entityc) var3.next();
            if (entity.serialize()) {
                this.writeChunk(stream, true, (out) -> {
                    int id = entity.classId();
                    if (id == modVars.MOD_CONTENT_ID && entity instanceof ModEntityc) {
                        out.writeByte(id);
                        out.writeShort(((ModEntityc) entity).modClassId());
                    } else if (id==modVars.MOD_CONTENT_ID) {
                        out.writeByte(Byte.MAX_VALUE - 1);
                    } else {
                        out.writeByte(id);
                    }
                    entity.write(Writes.get(out));
                });
            }
        }
    }

    @Override
    public void readEntities(DataInput stream) throws IOException {
        int teamc = stream.readInt();

        int amount;
        for (amount = 0; amount < teamc; ++amount) {
            Team team = Team.get(stream.readInt());
            Teams.TeamData data = team.data();
            int blocks = stream.readInt();
            data.blocks.clear();
            data.blocks.ensureCapacity(Math.min(blocks, 1000));
            Reads reads = Reads.get(stream);
            IntSet set = new IntSet();

            for (int j = 0; j < blocks; ++j) {
                short x = stream.readShort();
                short y = stream.readShort();
                short rot = stream.readShort();
                short bid = stream.readShort();
                Object obj = TypeIO.readObject(reads);
                if (set.add(Point2.pack(x, y))) {
                    data.blocks.addLast(new Teams.BlockPlan(x, y, rot, Vars.content.block(bid).id, obj));
                }
            }
        }

        amount = stream.readInt();

        for (int j = 0; j < amount; ++j) {
            try {
                this.readChunk(stream, true, (in) -> {
                    byte typeid = in.readByte();
                    if (typeid == modVars.MOD_CONTENT_ID) {
                        short modTypeid=in.readShort();
                        try{
                            if (ModEntityMapping.map(modTypeid) == null) {
                                Log.info("typeid: @",modTypeid);
                                in.skipBytes(this.lastRegionLength - 1);
                            } else {
                                Entityc entity = (Entityc) ModEntityMapping.map(modTypeid).get();
                                entity.read(Reads.get(in));
                                entity.add();
                            }
                        } catch (IndexOutOfBoundsException e){
                            in.skipBytes(this.lastRegionLength - 1);
                        }
                    } else if (EntityMapping.map(typeid) == null) {
                        in.skipBytes(this.lastRegionLength - 1);
                    } else {
                        Entityc entity = (Entityc) EntityMapping.map(typeid).get();
                        entity.read(Reads.get(in));
                        entity.add();
                    }
                });
            } catch (IOException ignored) {
            }
        }
    }
}
