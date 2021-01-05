package braindustry.type;

import mindustry.type.Weapon;

import static braindustry.ModVars.modFunc.fullName;

public class ModWeapon extends Weapon {
    public ModWeapon(String name){
        super(fullName(name));
    }
    private ModWeapon(){

    }
}
