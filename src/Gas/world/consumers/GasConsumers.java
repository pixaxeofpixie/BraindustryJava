package Gas.world.consumers;

import arc.struct.Bits;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.consumers.Consumers;

import java.util.Objects;

public class GasConsumers extends Consumers {
    protected ConsumeGasses[] map = new ConsumeGasses[ConsumeType.values().length];
    protected ConsumeGasses[] results;
    protected ConsumeGasses[] optionalResults;
    public final Bits gasFilter;
    public GasConsumers() {
        super();
        this.gasFilter=new Bits(Vars.content.getBy(ContentType.typeid_UNUSED).size);
    }
    public void init() {
        this.results =  Structs.filter(ConsumeGasses.class, this.map, Objects::nonNull);
        this.optionalResults = Structs.filter(ConsumeGasses.class, this.map, (m) -> {
            return m != null && m.isOptional();
        });
        GasConsumerBase[] var1 = this.results;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            GasConsumerBase cons = var1[var3];
            cons.applyItemFilter(this.itemFilters);
            cons.applyLiquidFilter(this.liquidfilters);
            cons.applyGasFilter(this.gasFilter);
        }
    }

    public ConsumeGasses getGas() {
        return (ConsumeGasses)this.get(2);
    }

    public Object get(int type) {
        if (this.map[type] == null) {
            throw new IllegalArgumentException("Block does not contain consumer of type '" + type + "'!");
        } else {
            return this.map[type];
        }
    }
}
