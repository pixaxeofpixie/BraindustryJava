package Gas.world.consumers;

import Gas.GasBuilding;
import mindustry.gen.Building;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeType;

public abstract class ConsumeGasBase extends Consume {
    public final float amount;

    public ConsumeGasBase(float amount) {
        this.amount = amount;
    }

    public ConsumeType type() {
        return null;
    }

    protected float use(GasBuilding entity) {
        return Math.min(this.amount * entity.edelta(), entity.block.gasCapacity);
    }
}
