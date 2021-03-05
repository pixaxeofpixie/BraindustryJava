package mindustryAddition.graphics;

import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;

public class ModGeometry {
    private static Vec2 vec2(float x,float y){
        return new Vec2(x,y);
    }
    public static Seq<Vec2> rectPoints(float x, float y, float width, float height, float rot){
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
    public static Seq<Vec2> rectPoints(Rect rect, float rot){
        return rectPoints(rect.x,rect.y,rect.width,rect.height,rot);
    }
}
