package braindustry.io;

import arc.struct.Seq;
import arc.util.io.Reads;
import braindustry.gen.StealthUnitc;
import mindustry.Vars;
import mindustry.entities.EntityGroup;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Nulls;
import mindustry.gen.Unit;
import mindustry.world.blocks.ControlBlock;

public class ModTypeIO {
    public static Unit readUnit(Reads read) {
        byte type = read.b();
        int id = read.i();
        if (type == 0) {
            return Nulls.unit;
        } else if (type == 2) {
            EntityGroup<Unit> stealthUnits=new EntityGroup<>(Unit.class, true, true);
            Groups.all.each((entityc -> {
                if (entityc instanceof StealthUnitc && ((StealthUnitc) entityc).inStealth()){
                    stealthUnits.add((Unit) entityc);
                }
            }));
            Unit unit = Groups.unit.getByID(id);
            if (unit==null)unit=stealthUnits.getByID(id);
            return unit == null ? Nulls.unit : unit;
        } else if (type != 1) {
            return Nulls.unit;
        } else {
            Building tile = Vars.world.build(id);
            ControlBlock cont;
            return tile instanceof ControlBlock && (cont = (ControlBlock)tile) == (ControlBlock)tile ? cont.unit() : Nulls.unit;
        }
    }
}

