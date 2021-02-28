package braindustry.entities.abilities;

import arc.math.geom.Vec2;
import braindustry.entities.ContainersForUnits.BlackHoleReactorUnitContainer;
import braindustry.type.ModUnitType;
import braindustry.type.PowerUnitContainer;
import mindustry.gen.Unit;

public class BlackHoleReactorAbility extends PowerGeneratorAbility {
    public float blackHoleHitSize;

    public BlackHoleReactorAbility(ModUnitType unitType, float range, float powerProduction, int maxNodes, float blackHoleHitSize, Vec2 reactorOffset) {
        super(unitType, range, powerProduction, maxNodes, reactorOffset);
        this.blackHoleHitSize=blackHoleHitSize;
    }
    public BlackHoleReactorAbility(ModUnitType unitType, float range, float powerProduction, int maxNodes, float blackHoleHitSize) {
        this(unitType,range,powerProduction,maxNodes,blackHoleHitSize,Vec2.ZERO.cpy());
    }
    @Override
    protected PowerUnitContainer powerUnitContainer(Unit unit) {
        return unitMap.get(unit, () -> new BlackHoleReactorUnitContainer(unit, this));
    }

    @Override
    public void drawReactor(Unit unit) {

    }
}
