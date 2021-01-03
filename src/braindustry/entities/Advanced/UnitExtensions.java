package braindustry.entities.Advanced;

import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Blocks;
import mindustry.gen.Unit;


public class UnitExtensions {
    public static UnitExtensions blink = new UnitExtensions((unit) -> {
        if (unit.isShooting){
            unit.health+=0.01f;
        }
    }, (unit) -> {
        if (unit.isShooting){
            Draw.alpha(unit.healthf());
        } else {
            Draw.alpha(Mathf.absin(Time.time + Mathf.randomSeed(unit.id, 0, 1000000), 1f, 4.0F) / 8f + 0.5f);
        }
    });
    ;
    public Cons<Unit> update;
    public Cons<Unit> applyColor;

    public UnitExtensions(Cons<Unit> update, Cons<Unit> applyColor) {
        this.update = update;
        this.applyColor = applyColor;
    }
}
