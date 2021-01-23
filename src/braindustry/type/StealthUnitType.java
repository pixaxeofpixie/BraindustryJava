package braindustry.type;

import braindustry.gen.StealthMechUnit;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class StealthUnitType extends UnitType {
    public float stealthCooldown=60.f;
    public float stealthDuration=60.f;
    public float minHealth=-1,maxHealth=-1;
    public StealthUnitType(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
        if (minHealth==-1){
            minHealth=health*0.25f;
        }
        if (maxHealth==-1){
            maxHealth=health*0.90f;
        }
    }

    @Override
    public void applyColor(Unit unit) {

        super.applyColor(unit);
        if (unit instanceof StealthMechUnit){
            ((StealthMechUnit)unit).drawAlpha();
        }
    }
}
