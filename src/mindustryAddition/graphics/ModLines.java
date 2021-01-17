package mindustryAddition.graphics;

import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class ModLines {
    private static float stroke;
    public static Seq<Vec2> rotRect(float x, float y, float width, float height,float rot){
        Rectangle2D r = new Rectangle().getBounds2D();
        r.setRect(x, y, width, height);


        AffineTransform at = AffineTransform.getRotateInstance(
                Math.toRadians(rot), r.getCenterX(), r.getCenterY());

        PathIterator i = r.getPathIterator(at);
        Seq<Vec2> cords=new Seq<>();
        while (!i.isDone()) {
            float[] xy = new float[2];
            i.currentSegment(xy);
            cords.add(new Vec2(xy[0], xy[1]));
            i.next();
        }
//        cords.reverse();
        cords.remove(cords.size-1);
        return cords;
    }
    public static void rect(float x, float y, float width, float height, float xspace, float yspace,float rot) {
        stroke= Lines.getStroke();
        x -= xspace;
        y -= yspace;
        width += xspace * 2.0F;
        height += yspace * 2.0F;
        Fill.rect(x, y, width, stroke,rot);
        Fill.rect(x, y + height, width, -stroke,rot);
        Fill.rect(x + width*2f, y, -stroke, height,rot);
        Fill.rect(x, y, stroke, height,rot);
    }

    public static void rect(float x, float y, float width, float height) {
        rect(x, y, width, height, 0);
    }
    public static void rect(float x, float y, float width, float height,float rot) {
        rect(x, y, width, height, 0,0,rot);
    }

    public static void rect(Rect rect) {
        rect(rect.x, rect.y, rect.width, rect.height, 0);
    }

    public static void rect(float x, float y, float width, float height, int space) {
        rect(x, y, width, height, (float)space, (float)space,0F);
    }
}
