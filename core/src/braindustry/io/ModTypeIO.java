package braindustry.io;

import arc.struct.Seq;
import arc.util.io.Reads;
import braindustry.gen.StealthUnitc;
import mindustry.Vars;
import mindustry.ai.formations.Formation;
import mindustry.ai.types.FormationAI;
import mindustry.ai.types.GroundAI;
import mindustry.ai.types.LogicAI;
import mindustry.annotations.Annotations;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.AIController;
import mindustry.entities.units.UnitController;
import mindustry.gen.*;
import mindustry.io.TypeIO;
import mindustry.world.blocks.ControlBlock;

@Annotations.TypeIOHandler
public class ModTypeIO extends TypeIO {
    public static UnitController readController(Reads read) {
        return readController(read,null);
    }
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

