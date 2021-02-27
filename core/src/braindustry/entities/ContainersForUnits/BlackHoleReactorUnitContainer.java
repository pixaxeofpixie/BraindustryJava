package braindustry.entities.ContainersForUnits;

import arc.math.geom.Vec2;
import braindustry.entities.abilities.BlackHoleReactorUnitAbility;
import braindustry.entities.abilities.PowerUnitAbility;
import braindustry.graphics.BlackHoleDrawer;
import braindustry.type.PowerUnitContainer;
import mindustry.gen.Unit;

public class BlackHoleReactorUnitContainer extends PowerUnitContainer<BlackHoleReactorUnitAbility> {
    BlackHoleDrawer drawer;
    public BlackHoleReactorUnitContainer(Unit unit, BlackHoleReactorUnitAbility ability) {
        super(unit, ability);
        drawer=new BlackHoleDrawer(unit);
    }

    @Override
    public void update() {
        drawer.update();
    }

    @Override
    public void draw() {
        super.draw();
        float rotation = unit.rotation - 90;
        Vec2 reactorPos=new Vec2().set(ability.reactorOffset).rotate(rotation).add(unit);
        drawer.drawBlackHole(reactorPos.x,reactorPos.y,rotation,ability.blackHoleHitSize,1);
    }
}
