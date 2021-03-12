package braindustry.entities.abilities;

import ModVars.Interface.InitableAbility;
import ModVars.Interface.LoadableAbility;
import arc.struct.Seq;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public abstract class ModAbility extends Ability implements LoadableAbility, InitableAbility {
    public boolean drawBody=false;
    public boolean drawBody(){
        return drawBody;
    }
    public void drawBody(Unit unit){

    }

    @Override
    public void init() {

    }

    @Override
    public void load() {

    }

    public Seq<Weapon> weapons() {
        return new Seq<>();
    }
}
