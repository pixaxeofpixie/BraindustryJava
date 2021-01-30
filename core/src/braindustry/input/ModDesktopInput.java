package braindustry.input;

import arc.Core;
import arc.util.Tmp;
import braindustry.entities.ModUnits;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.input.DesktopInput;
import mindustry.world.blocks.ControlBlock;

public class ModDesktopInput extends DesktopInput {
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
