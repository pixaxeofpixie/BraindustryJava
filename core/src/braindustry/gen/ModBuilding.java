package braindustry.gen;

import arc.util.Log;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class ModBuilding extends Building {

    @Override
    public void configure(Object value) {
        block.lastConfig = value;
        ModCall.tileConfig(Vars.player, this, value);
    }

    @Override
    public void configured(Unit builder, Object value) {
//        Log.info("builder: @, value: @",builder,value);
        super.configured(builder, value);
    }

    @Override
    public void configureAny(Object value) {
        ModCall.tileConfig(null, this, value);
    }
}
