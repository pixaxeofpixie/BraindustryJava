package Gas.world.consumers;

import arc.func.Cons;
import arc.struct.Bits;
import arc.util.Nullable;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.consumers.Consumers;
import mindustry.world.meta.Stats;

import java.util.Objects;

public class GasConsumers extends Consumers {
    protected Consume[] map = new GasConsume[ConsumeType.values().length+1];
    protected Consume[] results;
    protected Consume[] optionalResults;
    public final Bits gasFilter;
    public GasConsumers() {
        super();
        this.gasFilter=new Bits(Vars.content.getBy(ContentType.typeid_UNUSED).size);
    }public void each(Cons<Consume> c) {
        Consume[] var2 = this.map;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Consume cons = var2[var4];
            if (cons != null) {
                c.get(cons);
            }
        }

    }
    public void init() {
        this.results =  Structs.filter(Consume.class, this.map, Objects::nonNull);
        this.optionalResults = Structs.filter(Consume.class, this.map, (m) -> {
            return m != null && m.isOptional();
        });
        for(Consume cons:this.results) {
            cons.applyItemFilter(this.itemFilters);
            cons.applyLiquidFilter(this.liquidfilters);
            if (cons instanceof GasConsume) {
                ((GasConsume) cons).applyGasFilter(this.gasFilter);
            } else {
            }
        }
    }
    @Nullable
    public Consume[] all() {
        return this.results;
    }
    public Consume[] optionals() {
        return this.optionalResults;
    }
    public <T extends Consume> T add(T consume) {
        if (consume instanceof GasConsume){
            return (T) addGas(((GasConsume)consume));
        }
        this.map[consume.type().ordinal()] = consume;
        return consume;
    }

    public void remove(ConsumeType type) {
        this.map[type.ordinal()] = null;
    }

    public boolean has(ConsumeType type) {
        return this.map[type.ordinal()] != null;
    }

    public <T extends Consume> T get(ConsumeType type) {
        if (this.map[type.ordinal()] == null) {
            throw new IllegalArgumentException("Block does not contain consumer of type '" + type + "'!");
        } else {
            return (T) this.map[type.ordinal()];
        }
    }

    public boolean hasGas() {
        return this.map[3] != null;
    }
    public Consume getGas() {
        return (Consume) this.get(3);
    }

    public Object get(int type) {
        if (this.map[type] == null) {
            throw new IllegalArgumentException("Block does not contain consumer of type '" + type + "'!");
        } else {
            return this.map[type];
        }
    }

    public <T extends GasConsume> T addGas(T consume) {
        this.map[3] = consume;
        return consume;
    }public void display(Stats stats) {
        Consume[] var2 = this.map;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Consume c = var2[var4];
            if (c != null) {
                c.display(stats);
            }
        }

    }
}
