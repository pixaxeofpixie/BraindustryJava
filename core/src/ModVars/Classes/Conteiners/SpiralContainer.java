package ModVars.Classes.Conteiners;

public class SpiralContainer {
    public float partLength,count,angleMul;
    public SpiralContainer() {
        this(2,10,170);
    }
    public SpiralContainer(float partLength, float count,float angleMul) {
        this.partLength = partLength;
        this.count = count;
        this.angleMul=angleMul;
    }
}
