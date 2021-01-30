package braindustry.gen;

import arc.util.Nullable;
import mindustry.annotations.Annotations;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class ModNetServer {
    @Annotations.Remote(called = Annotations.Loc.server)
    public static void spawnUnits(UnitType type, float x, float y, int amount, boolean spawnerByCore, @Nullable Team team, @Nullable UnitController controller){
        for (int i = 0; i < amount; i++) {
            Unit unit=type.spawn(team==null?Team.derelict:team,x,y);
            unit.spawnedByCore(spawnerByCore);
            if (controller!=null){
                if (controller instanceof Player) {
                    controller.unit().spawnedByCore(true);
                    ((Player) controller).team(team);
                }

                unit.controller(controller);
            }
        }
    }

}
