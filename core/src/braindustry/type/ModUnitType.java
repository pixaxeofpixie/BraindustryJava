package braindustry.type;

import ModVars.Interface.InitableAbility;
import ModVars.Interface.LoadableAbility;
import arc.struct.Seq;
import braindustry.ModListener;
import braindustry.content.ModItems;
import braindustry.entities.abilities.ModAbility;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;

import java.util.Objects;

public class ModUnitType extends UnitType {
    public ItemStack[] dropItems=ItemStack.with(ModItems.chloroAlloy, 342);
    public boolean hasAfterDeathLaser=true;
    public int afterDeathLaserCount=5;
    public ModUnitType(String name) {
        super(name);
        ModListener.updaters.add(this::triggerUpdate);
    }

    protected void triggerUpdate() {

    }

    @Override
    public void drawBody(Unit unit) {
        Seq<ModAbility> modAbilities = getModAbilities();
        Seq<ModAbility> select = modAbilities.select(ModAbility::drawBody);
        if (select.size == 0) {
            super.drawBody(unit);
        } else {
            select.each(a -> a.drawBody(unit));
        }
    }

    public ModAbility toModAbility(Ability ability) {
        if (ability instanceof ModAbility) return (ModAbility) ability;
        return null;
    }

    private LoadableAbility getLoadable(Ability ability) {
        if (ability instanceof LoadableAbility) return (LoadableAbility) ability;
        return null;
    }

    private InitableAbility getInitable(Ability ability) {
        if (ability instanceof InitableAbility) return (InitableAbility) ability;
        return null;
    }

    @Override
    public void init() {
        super.init();
        getModAbilities().each(ModAbility::init);
    }

    public Seq<ModAbility> getModAbilities() {
        return abilities.map(this::toModAbility).select(Objects::nonNull);
    }

    @Override
    public void load() {
        super.load();
        getModAbilities().each(ModAbility::load);
    }
}
