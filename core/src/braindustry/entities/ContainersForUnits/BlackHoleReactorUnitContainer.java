package braindustry.entities.ContainersForUnits;

import arc.math.geom.Vec2;
import braindustry.entities.abilities.BlackHoleReactorAbility;
import braindustry.graphics.BlackHoleDrawer;
import braindustry.type.PowerUnitContainer;
import mindustry.gen.Unit;

public class BlackHoleReactorUnitContainer extends PowerUnitContainer<BlackHoleReactorAbility> {
    BlackHoleDrawer drawer;
    public BlackHoleReactorUnitContainer(Unit unit, BlackHoleReactorAbility ability) {
        super(unit, ability);
        drawer=new BlackHoleDrawer(unit);
    }

    @Override
    public void update() {
        super.update();
        drawer.update();
    }

    @Override
    public void draw() {
        super.draw();
    }
    public void drawReactor(){
        float rotation = unit.rotation;
        Vec2 reactorPos=new Vec2().set(ability.reactorOffset).rotate(rotation).add(unit);
        drawer.drawBlackHole(reactorPos.x,reactorPos.y,0,1,ability.blackHoleHitSize);
    }
}
