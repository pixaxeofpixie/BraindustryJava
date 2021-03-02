package ModVars.math;

import arc.math.Mathf;

public class ModMath  {
    public static float round(double value){
        float scale = Mathf.pow(10f, 8);
        return ((float) Math.ceil(value* scale))/scale;
    }
    public static float atan(float tan){
        return round(Mathf.radiansToDegrees*Math.atan(tan));
    }
}
