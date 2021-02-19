package braindustry.io;

import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.gen.ObjectOperations;
import braindustry.gen.StealthUnitc;
import braindustry.gen.WritableInterface;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.content.TechTree;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitCommand;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Nulls;
import mindustry.gen.Unit;
import mindustry.io.TypeIO;
import mindustry.logic.LAccess;
import mindustry.type.UnitType;
import mindustry.world.blocks.ControlBlock;

@Annotations.TypeIOHandler
public class ModTypeIO extends TypeIO {
    public static void writeTeam(Writes write, Team team) {
        if (team == null) {
            write.b(-1);
        } else {
            TypeIO.writeTeam(write, team);
        }
    }

    public static Team readTeam(Reads reads) {
        int id = reads.b();
        if (id == -1) return null;
        return Team.get(id);
    }

    public static void writeUnitType(Writes write, UnitType unitType) {
        if (unitType == null) {
            write.s(-1);
        } else {
            TypeIO.writeUnitType(write, unitType);
        }
    }

    public static UnitController readController(Reads read) {
        return readController(read, null);
    }

    @ModAnnotations.WritableObjectsConfig(value = {Vec2.class}, offset = 1)
    public static void writeObject(Writes write, Object object) {
        if (object !=null && ObjectOperations.contains(write, object)) {
        } else if (object instanceof Vec2) {
            write.b(-1);
            TypeIO.writeVec2(write, (Vec2) object);
        } else {
            TypeIO.writeObject(write, object);
        }
    }

    @Nullable
    public static Object readObject(Reads read) {
        byte type = read.b();
        int i;
        WritableInterface writable = ObjectOperations.getById(type);
//        Log.info("type=@,obj=@", type, writable);
        if (writable != null) {
            writable.read(read);
            return writable;
        }
        switch (type) {
            case -1:
                return TypeIO.readVec2(read);
            case 0:
                return null;
            case 1:
                return read.i();
            case 2:
                return read.l();
            case 3:
                return read.f();
            case 4:
                return readString(read);
            case 5:
                return Vars.content.getByID(ContentType.all[read.b()], read.s());
            case 6:
                short length = read.s();
                IntSeq arr = new IntSeq();

                for (i = 0; i < length; ++i) {
                    arr.add(read.i());
                }

                return arr;
            case 7:
                return new Point2(read.i(), read.i());
            case 8:
                byte len = read.b();
                Point2[] out = new Point2[len];

                for (i = 0; i < len; ++i) {
                    out[i] = Point2.unpack(read.i());
                }

                return out;
            case 9:
                return TechTree.getNotNull((UnlockableContent) Vars.content.getByID(ContentType.all[read.b()], read.s()));
            case 10:
                return read.bool();
            case 11:
                return read.d();
            case 12:
                return Vars.world.build(read.i());
            case 13:
                return LAccess.all[read.s()];
            case 14:
                i = read.i();
                byte[] bytes = new byte[i];
                read.b(bytes);
                return bytes;
            case 15:
                return UnitCommand.all[read.b()];
            default:
                throw new IllegalArgumentException("Unknown object type: " + type);
        }
    }

    public static Unit readUnit(Reads read) {
        byte type = read.b();
        int id = read.i();
        if (type == 0) {
            return Nulls.unit;
        } else if (type == 2) {
            Unit unit = Groups.unit.getByID(id);
            if (unit == null) {
                EntityGroup<Unit> stealthUnits = new EntityGroup<>(Unit.class, true, true);
                Groups.all.each((entityc -> {
                    if (entityc instanceof StealthUnitc && ((StealthUnitc) entityc).inStealth()) {
                        stealthUnits.add((Unit) entityc);
                    }
                }));
                unit = stealthUnits.getByID(id);
            }
            return unit == null ? Nulls.unit : unit;
        } else if (type != 1) {
            return Nulls.unit;
        } else {
            Building tile = Vars.world.build(id);
            ControlBlock cont;
            return tile instanceof ControlBlock && (cont = (ControlBlock) tile) == (ControlBlock) tile ? cont.unit() : Nulls.unit;
        }
    }
}

