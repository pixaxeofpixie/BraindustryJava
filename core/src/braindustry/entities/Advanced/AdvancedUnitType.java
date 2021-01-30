package braindustry.entities.Advanced;

import arc.func.Func;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.entities.Leg;
import mindustry.gen.Legsc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;

import java.awt.*;

public class AdvancedUnitType extends UnitType implements UnitExtensionsImplements{
    protected static final Vec2 legOffset = new Vec2();
    public AdvancedUnitType(String name) {
        super(name);
    }
    private boolean exen=false;

    @Override
    public void draw(Unit unit) {
        super.draw(unit);
    }
    public <T extends Unit & Legsc> void drawLegs(T unit) {
        this.applyColor(unit);
        Leg[] legs = ((Legsc)unit).legs();
        float ssize = (float)this.footRegion.width * Draw.scl * 1.5F;
        float rotation = ((Legsc)unit).baseRotation();
        Leg[] var5 = legs;
        int i = legs.length;

        for(int var7 = 0; var7 < i; ++var7) {
            Leg leg = var5[var7];
            Drawf.shadow(leg.base.x, leg.base.y, ssize);
        }

        for(int j = legs.length - 1; j >= 0; --j) {
            i = j % 2 == 0 ? j / 2 : legs.length - 1 - j / 2;
            Leg leg = legs[i];
            float angle = ((Legsc)unit).legAngle(rotation, i);
            boolean flip = (float)i >= (float)legs.length / 2.0F;
            int flips = Mathf.sign(flip);
            Vec2 position = legOffset.trns(angle, this.legBaseOffset).add(unit);
            Tmp.v1.set(leg.base).sub(leg.joint).inv().setLength(this.legExtension);
            if (leg.moving && this.visualElevation > 0.0F) {
                float scl = this.visualElevation;
                float elev = Mathf.slope(1.0F - leg.stage) * scl;
                Draw.color(Pal.shadow);
//                this.applyColor(unit);
                Draw.rect(this.footRegion, leg.base.x + -12.0F * elev, leg.base.y + -13.0F * elev, position.angleTo(leg.base));

                Draw.color();
            }

            this.applyColor(unit);
            Draw.rect(this.footRegion, leg.base.x, leg.base.y, position.angleTo(leg.base));
            Lines.stroke((float)this.legRegion.height * Draw.scl * (float)flips);
            Lines.line(this.legRegion, position.x, position.y, leg.joint.x, leg.joint.y, false);
            Lines.stroke((float)this.legBaseRegion.height * Draw.scl * (float)flips);
            Lines.line(this.legBaseRegion, leg.joint.x + Tmp.v1.x, leg.joint.y + Tmp.v1.y, leg.base.x, leg.base.y, false);
            if (this.jointRegion.found()) {
                Draw.rect(this.jointRegion, leg.joint.x, leg.joint.y);
            }

            if (this.baseJointRegion.found()) {
                Draw.rect(this.baseJointRegion, position.x, position.y, rotation);
            }
        }

        if (this.baseRegion.found()) {
            Draw.rect(this.baseRegion, unit.x, unit.y, rotation - 90.0F);
        }

        Draw.reset();
    }

    @Override
    public void applyColor(Unit unit) {
        super.applyColor(unit);
        exen=true;
        unitExtensions.each((extension)->extension.applyColor.get(unit));
        exen=false;
//        super.applyColor(unit);
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);
        unitExtensions.each((extension)->extension.update.get(unit));
    }
}
