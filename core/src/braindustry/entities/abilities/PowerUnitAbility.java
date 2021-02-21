package braindustry.entities.abilities;

import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class PowerUnitAbility extends Ability {
    public final UnitType unitType;

    public PowerUnitAbility(UnitType unitType) {
        this.unitType = unitType;
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
    }

    @Override
    public void draw(Unit unit) {
        super.draw(unit);
    }
}
