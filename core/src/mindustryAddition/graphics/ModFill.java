package mindustryAddition.graphics;

import arc.func.Cons;
import arc.func.Cons2;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;

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
        if (max%2!=0){
            startI=1;
            cons.get(0f);
            cons.get(1f);
            cons.get(1f);
//            cons.get(2f);
        }
        for(float i = startI; i < (max); i+=2f) {
            cons.get(i);
            cons.get(i+1f);
            cons.get(i+2f);
        }
       poly(floats);
    }
public static void circleRect(float x,float y,float radius){
    swirl(x,y,radius,1f,0f);
}
    public static void spikesSwirl(float x, float y, float radius1,float length, float finion, float angle){
        spikesSwirl(x,y,radius1,length,finion,angle,0.5f);
    }
    public static void spikesSwirl(float x, float y, float radius1,float length, float finion, float angle,float spikeOffset) {
        final float sides = 50;
        final float radius2=radius1+length;
        int max = (int)(sides * (finion + 0.001F));
        vector.set(0.0F, 0.0F);
        floats.clear();
        Cons2<Float,Float> point=(i,radius)->{
            vector.set(radius, 0.0F).setAngle(360.0F /sides * i + angle);
            floats.add(vector.x + x, vector.y + y);
        } ;
        Runnable flush=()->{
            poly(floats);
            floats.clear();
        };
        Cons<Float> spike=(i)->{
            point.get(i,radius1);
            point.get(i+1f,radius1);
            point.get(i+spikeOffset,radius2);
            point.get(i+spikeOffset,radius2);
            flush.run();
        };
        int startI=0;
        if (max%2!=0){
            startI=1;
            spike.get(0f);
        }

        for(float i = startI; i < max; i++) {
            spike.get(i);
        }
    }
    public static void doubleSwirl(float x, float y, float radius1,float radius2, float finion, float angle) {
        final float sides = 50;
        int max = (int)(sides * (finion + 0.001F));
        vector.set(0.0F, 0.0F);
        floats.clear();
//        floats.add(x,y);
        Cons<Float> cons=(i)->{
            vector.set(radius1, 0.0F).setAngle(360.0F /sides * i + angle);
            floats.add(vector.x + x, vector.y + y);
            vector.set(radius2, 0.0F).setAngle(360.0F /sides * i + angle);
            floats.add(vector.x + x, vector.y + y);
        };
        Cons<Float> undoCons=(i)->{
            vector.set(radius2, 0.0F).setAngle(360.0F /sides * i + angle);
            floats.add(vector.x + x, vector.y + y);
            vector.set(radius1, 0.0F).setAngle(360.0F /sides * i + angle);
            floats.add(vector.x + x, vector.y + y);
        };
        Runnable flush=()->{
            poly(floats);
            floats.clear();
        };
        int startI=0;
        if (max%2!=0){
            startI=1;
            cons.get(0f);
            undoCons.get(1f);
            flush.run();
            cons.get(1f);
            undoCons.get(1f);
            flush.run();
//            cons.get(1f);
//            cons.get(2f);
        }

        for(float i = startI; i < (max); i+=2f) {
            cons.get(i);
            undoCons.get(i+1f);
            flush.run();
            cons.get(i+1f);
            undoCons.get(i+2f);
            flush.run();
        }
//        poly(floats);
    }
}
