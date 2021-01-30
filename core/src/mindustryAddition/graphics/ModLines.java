package mindustryAddition.graphics;

import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;


public class ModLines {
    private static float stroke;
    private static Vec2 vec2(float x,float y){
        return new Vec2(x,y);
    }
    public static Seq<Vec2> rotRect(float x, float y, float width, float height,float rot){
        float vw = width / 2f;
        float vh = height / 2f;
        Seq<Vec2> points=Seq.with(vec2(-vw,-vh),vec2(vw,-vh),vec2(vw,vh),vec2(-vw,vh),vec2(-vw,-vh));
        Seq<Vec2> cords=new Seq<>();
        points.each((point)->{
            cords.add(point.cpy().rotate(rot).add(x+vw,y+vh));
        });
//        if (cords.size>5)cords.remove(cords.size-1);
        return cords;
    }
    public static void rect(float x, float y, float width, float height, float offsetx, float offsety,float rot) {
        Lines.poly(ModLines.rotRect(x, y, width, height, rot).toArray(Vec2.class), offsetx, offsety, 1f);
    }

    public static void rect(float x, float y, float width, float height) {
        rect(x, y, width, height, 0);
    }
    public static void rect(float x, float y, float width, float height,float rot) {
        rect(x,y,width,height,0,0,rot);
    }

    public static void rect(Rect rect) {
        rect(rect.x, rect.y, rect.width, rect.height, 0);
    }

    public static void rect(float x, float y, float width, float height, int offset) {
        rect(x, y, width, height, (float)offset, (float)offset,0F);
    }
}
