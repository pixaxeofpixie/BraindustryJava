package Main.ModContent.entities;

import Main.modVars.modVars;
import arc.func.Cons;
import arc.graphics.Color;
import arc.math.geom.Position;
import mindustry.entities.Effect;

public class DebugEffect extends Effect {
    public DebugEffect(float life, float clipsize, Cons<EffectContainer> renderer) {
        super(life,clipsize,renderer);
    }

    public DebugEffect(float life, Cons<Effect.EffectContainer> renderer) {
        this(life, 50.0F, renderer);
    }

    public DebugEffect() {
        super();
    }
    @Override
    public void at(float x, float y, float rotation, Color color, Object data) {
        if(modVars.settings.debug()) super.at(x, y, rotation, color, data);
    }

    @Override
    public void at(Position pos) {
        if(modVars.settings.debug()) super.at(pos);
    }

    @Override
    public void at(float x, float y) {
        if(modVars.settings.debug()) super.at(x, y);
    }

    @Override
    public void at(float x, float y, float rotation) {
        if(modVars.settings.debug()) super.at(x, y, rotation);
    }

    @Override
    public void at(Position pos, float rotation) {
        if(modVars.settings.debug()) super.at(pos, rotation);
    }

    @Override
    public void at(float x, float y, Color color) {
        if(modVars.settings.debug()) super.at(x, y, color);
    }

    @Override
    public void at(float x, float y, float rotation, Color color) {
        if(modVars.settings.debug()) super.at(x, y, rotation, color);
    }

    @Override
    public void at(float x, float y, float rotation, Object data) {
        if(modVars.settings.debug()) super.at(x, y, rotation, data);
    }
}
