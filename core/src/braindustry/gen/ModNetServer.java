package braindustry.gen;

import ModVars.ModEnums;
import ModVars.modVars;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Strings;
import braindustry.annotations.ModAnnotations;
import mindustry.annotations.Annotations;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class ModNetServer {
    @ModAnnotations.Remote(called = Annotations.Loc.server)
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
    @ModAnnotations.Remote(called = Annotations.Loc.server)
    public static void setStealthStatus(Unit unit,boolean inStealth,float value){
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

    public void registerCommands(CommandHandler handler) {
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
        });
    }
}
