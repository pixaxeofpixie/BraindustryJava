package braindustry.type;

import ModVars.Interface.InitableAbility;
import ModVars.Interface.LoadableAbility;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import braindustry.ModListener;
import braindustry.entities.abilities.ModAbility;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.mod.Mod;
import mindustry.type.UnitType;

import java.util.Objects;

public class ModUnitType extends UnitType {
    public ModUnitType(String name) {
        super(name);
        ModListener.updaters.add(this::triggerUpdate);
    }
    protected void triggerUpdate(){

    }

    @Override
    public void drawBody(Unit unit) {
        Seq<ModAbility> modAbilities=abilities.map(this::getModAbil).select(Objects::nonNull);
        Seq<ModAbility> select = modAbilities.select(ModAbility::drawBody);
        if (select.size==0){
            super.drawBody(unit);
        } else {
            select.each(a->a.drawBody(unit));
        }
    }
private ModAbility getModAbil(Ability ability){
        if (ability instanceof ModAbility)return (ModAbility) ability;
        return null;
}
    private LoadableAbility getLoadable(Ability ability){
        if (ability instanceof LoadableAbility)return (LoadableAbility)ability;
        return null;
    }
    private InitableAbility getInitable(Ability ability){
        if (ability instanceof InitableAbility)return (InitableAbility)ability;
        return null;
    }

    @Override
    public void init() {
        super.init();
        abilities.map(this::getInitable).select(Objects::nonNull).each(InitableAbility::init);
    }

    @Override
    public void load() {
        super.load();
        abilities.map(this::getLoadable).select(Objects::nonNull).each(LoadableAbility::load);
    }
}
