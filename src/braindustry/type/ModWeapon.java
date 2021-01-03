package braindustry.type;

import mindustry.mod.Mod;
import mindustry.type.Weapon;

import static braindustry.modVars.ModFunc.getFullName;

public class ModWeapon extends Weapon {
    public ModWeapon(String name){
        super(getFullName(name));
    }
    private ModWeapon(){

    }
}
