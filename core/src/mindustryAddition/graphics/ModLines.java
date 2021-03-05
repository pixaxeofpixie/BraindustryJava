package mindustryAddition.graphics;

import arc.func.Cons;
import arc.graphics.g2d.Lines;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;


public class ModLines extends Lines {
    static FloatSeq floats = new FloatSeq();

    public static void rect(float x, float y, float width, float height, float offsetx, float offsety, float rot) {
        float stroke = getStroke();
        Vec2[] points1 = ModGeometry.rectPoints(x+offsetx, y+offsety, width, height, rot).toArray(Vec2.class);
        Vec2[] points2 = ModGeometry.rectPoints(x + stroke+offsetx, y + stroke+offsety, width - stroke*2f, height - stroke*2f, rot).toArray(Vec2.class);
        Cons<Integer> side = (index) -> {
            int index2 = (index + 1) % 4;
            floats.clear();
            floats.add(points1[index].x, points1[index].y);
            floats.add(points1[index2].x, points1[index2].y);
            floats.add(points2[index2].x, points2[index2].y);
            floats.add(points2[index].x, points2[index].y);
            ModFill.quad(floats);
        };
        side.get(0);
        side.get(1);
        side.get(2);
        side.get(3);
        floats.clear();
    }

    public static void rect(float x, float y, float width, float height) {
        rect(x, y, width, height, 0);
    }

    public static void rect(float x, float y, float width, float height, float rot) {
        rect(x, y, width, height, 0, 0, rot);
    }

    public static void rect(Rect rect) {
        rect(rect.x, rect.y, rect.width, rect.height, 0);
    }

    public static void rect(float x, float y, float width, float height, int offset) {
        rect(x, y, width, height, (float) offset, (float) offset, 0F);
    }
}
