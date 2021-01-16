package braindustry.type;

import braindustry.gen.StealthMechUnit;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class StealthUnitType extends UnitType {
    public float stealthCooldown=60.f;
    public float stealthDuration=60.f;
    public StealthUnitType(String name) {
        super(name);
    }

    @Override
    public void applyColor(Unit unit) {

        super.applyColor(unit);
        if (unit instanceof StealthMechUnit){
            ((StealthMechUnit)unit).drawAlpha();
        }
    }
}
