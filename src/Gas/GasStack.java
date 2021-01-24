package Gas;

import Gas.content.Gasses;
import Gas.type.Gas;
import mindustry.content.Liquids;
import mindustry.type.Liquid;

public class GasStack {
    public Gas gas;
    public float amount;

    public GasStack(Gas gas, float amount) {
        this.gas = gas;
        this.amount = amount;
    }

    protected GasStack() {
        gas = Gasses.oxygen;
    }

    public String toString() {
        return "GasStack{gas=" + gas + ", amount=" + amount + '}';
    }
}
