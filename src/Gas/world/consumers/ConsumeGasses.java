package Gas.world.consumers;

import Gas.type.Gas;
import Gas.GasBuilding;
import Gas.GasStats;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import mindustry.gen.Building;
import mindustry.ui.Cicon;
import mindustry.ui.ReqImage;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.Stat;
import mindustry.world.meta.Stats;

public class ConsumeGasses extends GasConsume {
    public final Gas gas;
    public final float amount;

    public ConsumeType type() {
        return ConsumeType.liquid;
    }

    protected float use(Building entity) {
        return Math.min(this.amount * entity.edelta(), entity.block.liquidCapacity);
    }
    public ConsumeGasses(Gas gas, float amount) {
        this.amount = amount;
        this.gas = gas;
    }

    protected ConsumeGasses() {
        this((Gas) null, 0.0F);
    }

    public void applyLiquidFilter(Bits filter) {
        filter.set(this.gas.id);
    }

    public void build(Building tile, Table table) {
        table.add(new ReqImage(this.gas.icon(Cicon.medium), () -> {
            return this.valid(tile);
        })).size(32.0F);
    }

    public String getIcon() {
        return "icon-liquid-consume";
    }

    public void update(Building b) {
        if (b==null || !(b instanceof GasBuilding))return;
        GasBuilding entity=(GasBuilding)b;
        entity.gasses.remove(this.gas, Math.min(this.use(entity), entity.gasses.get(this.gas)));
    }

    public boolean valid(Building b) {
        if (b==null || !(b instanceof GasBuilding))return false;
        GasBuilding entity=(GasBuilding)b;
        return entity.gasses != null && entity.gasses.get(this.gas) >= this.use(entity);
    }

    public void display(Stats stat) {
        GasStats stats=(GasStats)stat;
        stats.add(this.booster ? Stat.booster : Stat.input, this.gas, this.amount * 60.0F, true);
    }
}
