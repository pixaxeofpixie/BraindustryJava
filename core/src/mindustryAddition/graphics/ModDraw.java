package mindustryAddition.graphics;

import ModVars.math.ModMath;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Tmp;
import braindustry.cfunc.Couple;
import braindustry.cfunc.Couple3;
import braindustry.content.ModFx;
import mindustry.ui.Fonts;

public class ModDraw extends Draw{
    public static void drawLabel(Vec2 pos, float textSize, Color color, String text){
        Font font = Fonts.outline;
        boolean ints = font.usesIntegerPositions();
        font.getData().setScale(textSize / Scl.scl(1.0f));
        font.setUseIntegerPositions(false);

        font.setColor(color);

        float z = Draw.z();
        Draw.z(z+1.f);
        font.draw(text, pos.x, pos.y);
        Draw.z(z);

        font.setUseIntegerPositions(ints);
        font.getData().setScale(1);
    }
    public static void teleportCircles(float x, float y, float radius, Color first, Color second, Couple<Float,Float> range){
        Seq<Couple3<Vec2, Float, Color>> couple3s = new Seq<>();
        float lastAngle = 0;
        float maxAngle = 0;
        Vec2 firstO=new Vec2();
        Vec2 lastPos = new Vec2();
        for (int i = 0; true; i++) {
            float mradius = Mathf.random(range.o1, range.o2);
            Color lerp = first.cpy().lerp(second, Mathf.random());
            Vec2 pos = Tmp.v1.trns(0, radius).cpy();
            if (i != 0) {
                float angle = ModMath.atan((mradius + lastAngle) / radius);
                float fangle = angle + lastPos.angle();
                if (fangle > maxAngle) break;
                float pangle =ModMath.atan((mradius) / radius);
                if (pangle + angle > maxAngle) {
                    fangle = maxAngle - (maxAngle - lastAngle) / 2f;
                    pos = Tmp.v1.trns(fangle, radius).cpy();
                    mradius = pos.dst(firstO);
                }
                pos = Tmp.v1.trns(fangle, radius).cpy();
            } else {
                maxAngle = 360f - ModMath.atan((mradius) / radius);
                firstO.trns(maxAngle, radius);
            }
            couple3s.add(Couple3.of(pos, mradius, lerp));
            lastPos = pos.cpy();
            lastAngle = mradius;
        }
        while (couple3s.size>0){
            Couple3<Vec2, Float, Color> c = couple3s.random();
            ModFx.teleportSircle.at(x + c.o1.x, y + c.o1.y, c.o2, c.o3);
            couple3s.remove(c);
        }
    }
    public static void drawLabel(Vec2 pos,float textSize,String text){
        drawLabel(pos,textSize,Color.white,text);
    }
    public static void drawLabel(Vec2 pos,Color color,String text){
        drawLabel(pos,0.23f,color,text);
    }
    public static void drawLabel(Vec2 pos,String text){
        drawLabel(pos,Color.white,text);
    }
    public static void drawLabel(float x,float y, float textSize, Color color, String text){
        drawLabel(new Vec2(x,y),textSize,color,text);
    }
    public static void drawLabel(float x,float y,float textSize,String text){
        drawLabel(x,y,textSize,Color.white,text);
    }
    public static void drawLabel(float x,float y,Color color,String text){
        drawLabel(x,y,0.23f,color,text);
    }
    public static void drawLabel(float x,float y,String text){
        drawLabel(x,y,Color.white,text);
    }
}
