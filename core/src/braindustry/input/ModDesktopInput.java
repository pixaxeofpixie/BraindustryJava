package braindustry.input;

import arc.Core;
import arc.scene.Group;
import arc.util.Tmp;
import braindustry.entities.ModUnits;
import braindustry.gen.ModCall;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.gen.*;
import mindustry.input.DesktopInput;
import mindustry.world.blocks.ControlBlock;

import static mindustry.Vars.player;
import static mindustry.Vars.world;

public class ModDesktopInput extends DesktopInput {
    @Override
    public void buildUI(Group group) {
        super.buildUI(group);
    }
    public void tryPickupPayload(){
        Unit unit = player.unit();
        if(!(unit instanceof Payloadc)) return;
        Payloadc pay=unit.as();
        Unit target = Units.closest(player.team(), pay.x(), pay.y(), unit.type.hitSize * 2f, u -> u.isAI() && u.isGrounded() && pay.canPickup(u) && u.within(unit, u.hitSize + unit.hitSize));
        if(target != null){
           ModCall.requestUnitPayload(player, target);
        }else{
            Building build = world.buildWorld(pay.x(), pay.y());

            if(build != null && build.team == unit.team){
                Call.requestBuildPayload(player, build);
            }
        }
    }
    @Override
    public Unit selectedUnit() {
        Unit unit = ModUnits.closest(Vars.player.team(), Core.input.mouseWorld().x, Core.input.mouseWorld().y, 40.0F, Unitc::isAI);
        if (unit != null) {
            unit.hitbox(Tmp.r1);
            Tmp.r1.grow(6.0F);
            if (Tmp.r1.contains(Core.input.mouseWorld())) {
                return unit;
            }
        }

        Building build = Vars.world.buildWorld(Core.input.mouseWorld().x, Core.input.mouseWorld().y);
        ControlBlock cont;
        return build instanceof ControlBlock && (cont = (ControlBlock)build) == build && cont.canControl() && build.team == Vars.player.team() ? cont.unit() : null;
    }
}
