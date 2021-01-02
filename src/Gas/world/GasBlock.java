package Gas.world;

import Gas.content.Gasses;
import Gas.GasBuilding;
import Gas.type.Gas;
import Gas.world.consumers.GasConsumers;
import arc.Core;
import arc.func.Func;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeType;

public class GasBlock extends Block {
    public boolean hasGas=false;
    public float gasCapacity =0;
    public boolean outputsGas = false;
    public boolean placeableGas = false;
    public final GasConsumers consumes = new GasConsumers();
//    4a4b53
    public GasBlock(String name) {
        super(name);
        this.gasCapacity =10;
    }

    @Override
    public void setBars() {
        super.setBars();
        if (this.hasGas) {
            Func<GasBuilding, Gas> current;
            if (this.consumes.has(ConsumeType.liquid) && this.consumes.get(ConsumeType.liquid) instanceof ConsumeLiquid) {
                Gas gas = (this.consumes.getGas()).gas;
                current = (entity) -> {
                    return gas;
                };
            } else {
                current = (entity) -> {
                    return entity.gasses == null ? Gasses.none : entity.gasses.current();
                };
            }

            this.bars.add("gas", (e) -> {
                GasBuilding entity=(GasBuilding)e;
                return new Bar(() -> {
                    return entity.gasses.get(current.get(entity)) <= 0.001F ? Core.bundle.get("bar.gas") : (current.get(entity)).localizedName;
                }, () -> {
                    return (current.get(entity)).barColor();
                }, () -> {
                    return entity != null && entity.gasses != null ? entity.gasses.get(current.get(entity)) / this.gasCapacity : 0.0F;
                });
            });
        }
    }
}
