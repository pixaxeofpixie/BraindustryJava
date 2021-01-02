package braindustry.ModContent.content;

import braindustry.ModContent.entities.DebugEffect;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;

import static braindustry.modVars.modFunc.print;

public class ModFx {




    static {
        Fx.class.isArray();
    }
    public static final Effect shootSmall = new Effect(8.0F, (e) -> {
        Draw.color(Color.white, e.color, e.fin());
        float w = 1.0F + 5.0F * e.fout();
        Drawf.tri(e.x, e.y, w, 15.0F * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 3.0F * e.fout(), e.rotation + 180.0F);
    });
    public static Effect purpleLaserChargeSmall =new Effect(40.0F, 100.0F, (e) -> {
        Draw.color(Color.valueOf("d5b2ed"));
        Lines.stroke(e.fin() * 2.0F);
        Lines.circle(e.x, e.y, e.fout() * 50.0F);
    });
    public static Effect purpleBomb =new Effect(40.0F, 100.0F, (e) -> {
        Color color=Color.valueOf("d5b2ed");
        Draw.color(color);
        Lines.stroke(e.fout() * 2.0F);
        Lines.circle(e.x, e.y, 4.0F + e.finpow() * 65.0F);
        Draw.color(color);

        int i;
        for(i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 6.0F, 100.0F * e.fout(), (float)(i * 90));
        }

        Draw.color();

        for(i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 3.0F, 35.0F * e.fout(), (float)(i * 90));
        }

    });
    public static Effect lacertaLaserCharge =new Effect(80.0F, 100.0F, (e) -> {
        print("e.data: @",e.data);
        Color color=Color.valueOf("d5b2ed");
        Draw.color(color);
        Lines.stroke(e.fin() * 2.0F);
        Lines.circle(e.x, e.y, 4.0F + e.fout() * 100.0F);
        Fill.circle(e.x, e.y, e.fin() * 20.0F);
        Angles.randLenVectors((long)e.id, 20, 40.0F * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 5.0F);
        });
        Draw.color();
        Fill.circle(e.x, e.y, e.fin() * 10.0F);
    });
    public static Effect yellowBomb =new Effect(40.0F, 100.0F, (e) -> {
        Color color=Color.valueOf("FFFD99");
        Draw.color(color);
        Lines.stroke(e.fout() * 2.0F);
        Lines.circle(e.x, e.y, 4.0F + e.finpow() * 65.0F);
        Draw.color(color);

        int i;
        for(i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 6.0F, 100.0F * e.fout(), (float)(i * 90));
        }

        Draw.color();

        for(i = 0; i < 4; ++i) {
            Drawf.tri(e.x, e.y, 3.0F, 35.0F * e.fout(), (float)(i * 90));
        }

    });;
    public static final Effect absorb = new Effect(12.0F, (e) -> {
        Draw.color(e.color);
        Lines.stroke(2.0F * e.fout());
        Lines.circle(e.x, e.y, 5.0F * e.fout());
    });
    public static final Effect changeCraft = new Effect(40.0F, (e) -> {
        Color secondColor;
        if (e.data == null) {
            secondColor = Color.white;
        } else {
            if (!(e.data instanceof Color)) return;
            secondColor = (Color) e.data;
        }
        Draw.color(secondColor, e.color, e.fin());
        Draw.alpha(e.fout());
        Fill.square(e.x, e.y, 4.0F * e.rotation);
    });
    public static final Effect debugSelect = new DebugEffect(40.0F, (e) -> {
        Color secondColor;
        if (e.data == null) {
            secondColor = e.color;
        } else {
            if (!(e.data instanceof Color)) return;
            secondColor = (Color) e.data;
        }
        Draw.color(secondColor, e.color, e.fin());
        Draw.alpha(e.fout());
        Fill.square(e.x, e.y, 4.0F * e.rotation);
    });
}
