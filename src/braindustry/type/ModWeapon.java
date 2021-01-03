package braindustry.type;

import mindustry.mod.Mod;
import mindustry.type.Weapon;

import static braindustry.modVars.modFunc.fullName;

public class ModWeapon extends Weapon {
    public ModWeapon(String name){
        super(fullName(name));
    }
    private ModWeapon(){

    }
}
