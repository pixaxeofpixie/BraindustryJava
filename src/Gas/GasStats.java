package Gas;

import Gas.type.Gas;
import Gas.world.meta.GasValue;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;
import mindustry.world.meta.Stats;

public class GasStats extends Stats {

    public void add(Stat stat, Gas gas, float amount, boolean perSecond) {
        this.add(stat, (new GasValue(gas, amount, perSecond)));
    }
}
