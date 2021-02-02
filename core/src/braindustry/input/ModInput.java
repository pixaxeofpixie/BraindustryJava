package braindustry.input;

import braindustry.annotations.ModAnnotations;
import braindustry.gen.ModCall;
import braindustry.gen.StealthUnitc;
import mindustry.annotations.Annotations;
import mindustry.gen.Call;
import mindustry.gen.Payloadc;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class ModInput {
    @ModAnnotations.Remote(targets = Annotations.Loc.both, called = Annotations.Loc.server)
    public static void requestUnitPayload(Player player, Unit target){
        if(player == null || target instanceof StealthUnitc) return;

        Unit unit = player.unit();
        Payloadc pay = (Payloadc)unit;

        if(target.isAI() && target.isGrounded() && pay.canPickup(target)
                && target.within(unit, unit.type.hitSize * 2f + target.type.hitSize * 2f)){
            ModCall.pickedUnitPayload(unit, target);
        }
    }
    @ModAnnotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.server)
    public static void pickedUnitPayload(Unit unit, Unit target){
        if(target != null && unit instanceof Payloadc && !(target instanceof StealthUnitc)){
            ((Payloadc) unit) .pickup(target);
        }else if(target != null && !(target instanceof StealthUnitc)){
            target.remove();
        }
    }

}
