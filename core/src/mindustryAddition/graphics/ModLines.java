package mindustryAddition.graphics;

import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;


public class ModLines  extends Lines{

    public static void rect(float x, float y, float width, float height, float offsetx, float offsety,float rot) {
        poly(ModGeometry.getRectPoints(x, y, width, height, rot).toArray(Vec2.class), offsetx, offsety, 1f);
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
