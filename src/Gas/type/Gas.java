package Gas.type;

import arc.graphics.Color;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.type.StatusEffect;
import mindustry.world.meta.Stat;

public class Gas  extends UnlockableContent {
    public Color color;
    public float explosiveness;
    public float flammability;
    public float radioactivity;
    public Color barColor;
    public Color lightColor;
    public float temperature;
    public float viscosity;
    public StatusEffect effect;
    public Gas(String name) {
        super(name);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.typeid_UNUSED;
    }
    public void setStats() {
        this.stats.addPercent(Stat.explosiveness, this.explosiveness);
        this.stats.addPercent(Stat.flammability, this.flammability);
        this.stats.addPercent(Stat.radioactivity, this.radioactivity);
    }

    public Color barColor() {
        return this.barColor == null ? this.color : this.barColor;
    }
}
