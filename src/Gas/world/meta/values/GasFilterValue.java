package Gas.world.meta.values;

import Gas.type.Gas;
import Gas.ui.GasDisplay;
import arc.func.Boolf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.type.Liquid;
import mindustry.ui.LiquidDisplay;
import mindustry.world.meta.StatValue;

import java.util.Iterator;

public class GasFilterValue implements StatValue {
    private final Boolf<Gas> filter;
    private final float amount;
    private final boolean perSecond;

    public GasFilterValue(Boolf<Gas> filter, float amount, boolean perSecond) {
        this.filter = filter;
        this.amount = amount;
        this.perSecond = perSecond;
    }

    public void display(Table table) {
        Seq<Gas> list = new Seq<>();
        Iterator<Gas> var3 = Vars.content.<Gas>getBy(ContentType.typeid_UNUSED).iterator();

        while(var3.hasNext()) {
            Gas gas = var3.next();
            if (!gas.isHidden() && this.filter.get(gas)) {
                list.add(gas);
            }
        }

        for(int i = 0; i < list.size; ++i) {
            table.add(new GasDisplay(list.get(i), this.amount, this.perSecond)).padRight(5.0F);
            if (i != list.size - 1) {
                table.add("/");
            }
        }

    }
}
