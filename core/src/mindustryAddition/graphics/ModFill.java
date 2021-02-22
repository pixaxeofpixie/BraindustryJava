package mindustryAddition.graphics;

import arc.func.Cons;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.struct.Seq;

public class ModFill extends Fill {
    private static float stroke = 1.0F;
    private static Vec2 vector = new Vec2();
    private static Vec2 u = new Vec2();
    private static Vec2 v = new Vec2();
    private static Vec2 inner = new Vec2();
    private static Vec2 outer = new Vec2();
    private static FloatSeq floats = new FloatSeq(20);
    private static FloatSeq floatBuilder = new FloatSeq(20);
    private static boolean building;
    private static float circlePrecision = 0.4F;
    public static void swirl(float x, float y, float radius, float finion, float angle) {
       final float sides = 50;
        int max = (int)(sides * (finion + 0.001F));
        vector.set(0.0F, 0.0F);
        floats.clear();

        floats.add(x,y);
        Cons<Float> cons=(i)->{
            vector.set(radius, 0.0F).setAngle(360.0F /sides * i + angle);
            floats.add(vector.x + x, vector.y + y);
        };
        int startI=0;
        int maxOffset=0;
        if (max%2!=0){
            startI=1;
            cons.get(0f);
            cons.get(1f);
            cons.get(1f);
//            cons.get(2f);
        }
        for(float i = startI; i < (max-maxOffset); i+=2f) {
            cons.get(i);
            cons.get(i+1f);
            cons.get(i+2f);
        }
       poly(floats);
    }
}
