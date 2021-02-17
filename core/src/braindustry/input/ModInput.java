package braindustry.input;

import arc.Core;
import arc.Events;
import arc.util.Log;
import arc.util.Nullable;
import braindustry.annotations.ModAnnotations;
import braindustry.gen.ModCall;
import braindustry.gen.StealthUnitc;
import mindustry.annotations.Annotations;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Payloadc;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.net.Administration;
import mindustry.net.ValidateException;

import static mindustry.Vars.net;
import static mindustry.Vars.netServer;

public class ModInput {
    @ModAnnotations.Remote(targets = Annotations.Loc.both, called = Annotations.Loc.both, forward = true)
    public static void tileConfig(Player player, Building build, @Nullable Object value) {
//        Log.info("tileConfig(player: @,build: @,value: @)",player,build,value);
        if (build == null) return;
        if (net.server() && (!Units.canInteract(player, build) ||
                !netServer.admins.allowAction(player, Administration.ActionType.configure, build.tile, action -> action.config = value)))
            throw new ValidateException(player, "Player cannot configure a tile.");
        build.configured(player == null || player.dead() ? null : player.unit(), value);
        Core.app.post(() -> Events.fire(new EventType.ConfigEvent(build, player, value)));
    }

    @ModAnnotations.Remote(targets = Annotations.Loc.both, called = Annotations.Loc.server)
    public static void requestUnitPayload(Player player, Unit target) {
        if (player == null || target instanceof StealthUnitc) return;

        Unit unit = player.unit();
        Payloadc pay = (Payloadc) unit;

        if (target.isAI() && target.isGrounded() && pay.canPickup(target)
                && target.within(unit, unit.type.hitSize * 2f + target.type.hitSize * 2f)) {
            ModCall.pickedUnitPayload(unit, target);
        }
    }

    @ModAnnotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.server)
    public static void pickedUnitPayload(Unit unit, Unit target) {
        if (target != null && unit instanceof Payloadc && !(target instanceof StealthUnitc)) {
            ((Payloadc) unit).pickup(target);
        } else if (target != null && !(target instanceof StealthUnitc)) {
            target.remove();
        }
    }

}
