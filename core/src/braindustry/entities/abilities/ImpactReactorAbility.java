package braindustry.entities.abilities;

import ModVars.modVars;
import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import braindustry.type.ModUnitType;
import mindustry.gen.Unit;

public class ImpactReactorAbility extends PowerGeneratorAbility {
    public TextureRegion light;
    public TextureRegion[] plasmaRegions;
    public Color plasma1, plasma2;
    public ImpactReactorAbility(ModUnitType unitType, float range, float powerProduction, int maxNodes, Vec2 reactorOffset) {
        super(unitType, range, powerProduction, maxNodes, reactorOffset);
        this.plasma1 = Color.valueOf("ffd06b");
        this.plasma2 = Color.valueOf("ff361b");
    }
    public ImpactReactorAbility(ModUnitType unitType, float range, float powerProduction, int maxNodes) {
        this(unitType,range,powerProduction,maxNodes,Vec2.ZERO.cpy());
    }

    public void drawReactor(Unit unit) {

        for (int i = 0; i < this.plasmaRegions.length; ++i) {
            float r = (float) (unit.hitSize() / 2f) - 3.0F + Mathf.absin(Time.time, 2.0F + (float) i * 1.0F, 5.0F - (float) i * 0.5F);
            Draw.color(this.plasma1, this.plasma2, (float) i / (float) this.plasmaRegions.length);
            Draw.alpha((0.3F + Mathf.absin(Time.time, 2.0F + (float) i * 2.0F, 0.3F + (float) i * 0.05F)) * 1);
            Draw.blend(Blending.additive);
            Draw.rect(this.plasmaRegions[i], unit.x, unit.y, r, r, Time.time * (12.0F + (float) i * 6.0F) * 1);
            Draw.blend();
        }

    }

    @Override
    public void load() {
        super.load();
        light = Core.atlas.find(unitType.name + "-light");
        Seq<TextureRegion> plasmas = new Seq<>();
        int i = 0;
        for (TextureRegion plasma = Core.atlas.find(unitType.name + "-plasma-" + i); Core.atlas.isFound(plasma); plasma = Core.atlas.find(unitType.name + "-plasma-" + (++i))) {
            if (!modVars.packSprites) plasmas.add(plasma);
        }
        plasmaRegions = new TextureRegion[plasmas.size];
        for (int j = 0; j < plasmaRegions.length; j++) {
            plasmaRegions[j] = plasmas.get(j);
        }
    }
}
