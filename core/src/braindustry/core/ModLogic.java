package braindustry.core;

import arc.ApplicationListener;
import arc.Events;
import arc.struct.Seq;
import braindustry.content.ModBullets;
import braindustry.content.ModUnitTypes;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import static ModVars.modFunc.print;

public class ModLogic  implements ApplicationListener {
    public ModLogic(){
        Events.on(EventType.UnitDestroyEvent.class, (e) -> {
            Seq<UnitType> types = Seq.with(ModUnitTypes.griffon);
            Unit unit = e.unit;
            if (types.contains(unit.type)) {
                float anglePart=360f/5f;
                for (float i = 0; i < 360f/anglePart; i++) {
                    print("i: @,anglePart: @",i,anglePart);
                    Call.createBullet(ModBullets.deathLaser, unit.team, unit.x, unit.y, (360f+unit.rotation+90f+anglePart/2f +anglePart*i)%360f, 1200f, 1f, 10f);
                }
            }
        });
    }
}
