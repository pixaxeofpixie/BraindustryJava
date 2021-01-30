package mindustryAddition.world.meta;

import arc.func.Func;
import arc.struct.OrderedMap;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.meta.BlockBars;

public class DynamicBlockBars extends BlockBars {
    private OrderedMap<String, Func<Building, Bar>> bars = new OrderedMap();

    public DynamicBlockBars() {
    }

    public <T extends Building> void add(String name, Func<T, Bar> sup) {
        this.bars.put(name, (Func<Building, Bar>)sup);
    }
    public void clear(){
        bars.clear();
    }
    public void remove(String name) {
        if (!this.bars.containsKey(name)) {
            throw new RuntimeException("No bar with name '" + name + "' found; current bars: " + this.bars.keys().toSeq());
        } else {
            this.bars.remove(name);
        }
    }

    public Iterable<Func<Building, Bar>> list() {
        return this.bars.values();
    }
}
