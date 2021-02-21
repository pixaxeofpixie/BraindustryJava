package braindustry.type;

import braindustry.entities.abilities.OrbitalPlatformAbility;
import mindustry.gen.Unit;

public abstract class UnitContainer {
    public final Unit unit;

    protected UnitContainer(Unit unit) {
        this.unit = unit;
    }

    public abstract void draw();

    public abstract void update();

    public abstract void remove();
}
