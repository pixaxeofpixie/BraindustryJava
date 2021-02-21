package braindustry.entities.comp;

import ModVars.modVars;
import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import braindustry.annotations.ModAnnotations;
import braindustry.gen.ModCall;
import braindustry.gen.StealthUnitc;
import braindustry.input.ModBinding;
import braindustry.type.StealthUnitType;
import mindustry.Vars;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.UnitController;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustryAddition.versions.ModEntityc;

@ModAnnotations.Component
public abstract class StealthComp implements StealthUnitc, ModEntityc {
    public boolean inStealth = false;
    public float cooldownStealth = 0;
    public float durationStealth = 0;
    public boolean healing;
    public boolean longPress = false;
    StealthUnitType stealthType;
    boolean check = false, check2 = false;
    @ModAnnotations.Import
    UnitType type;
    @ModAnnotations.Import
    float drag, maxHealth, armor, hitSize,health;
    @ModAnnotations.Import
    Seq<Ability> abilities;
    @ModAnnotations.Import
    boolean hovering;
    @ModAnnotations.Import
    UnitController controller;

    public void setOldType(UnitType type) {
        this.type = type;
        this.maxHealth = type.health;
        this.drag = type.drag;
        this.armor = type.armor;
        this.hitSize = type.hitSize;
        this.hovering = type.hovering;
        if (controller == null) controller(type.createController());
        if (mounts().length != type.weapons.size) setupWeapons(type);
        if (abilities.size != type.abilities.size) {
            abilities = type.abilities.map(Ability::copy);
        }
    }

    @Override
    public void setType(UnitType type) {
        if (!(type instanceof StealthUnitType)) {
            return;
        }
        setOldType(stealthType);
        stealthType = (StealthUnitType) type;
    }

    public boolean selectStealth() {
        boolean bool;
        if (isLocal()) {
            bool = modVars.keyBinds.keyTap(ModBinding.stealthBing);
            if (Vars.mobile) {
                if (!check2 && longPress) {
                    check2 = true;
                    longPress = false;
                    return true;
                }
                return false;
            }
            return bool;
        }
        bool = mustHeal();
        if (bool && healing) {
            return inStealth;
        }
        return !inStealth && bool;
    }

    @Override
    public boolean healing() {
        return healing;
    }

    @Override
    public void healing(boolean healing) {
        this.healing = healing;
    }

    public boolean mustHeal() {
        boolean bool1 = health <= stealthType.minHealth;
        boolean bool2 = health > stealthType.maxHealth;
        if (!check && bool1) {
            check = true;
            return true;
        } else if (check && bool2) {
            check = false;
            return bool1;
        }
        return !bool2;
    }

    @Override
    public boolean inStealth() {
        return inStealth;
    }

    @Override
    public void inStealth(boolean inStealth) {
        this.inStealth = inStealth;
    }

    @Override
    public void longPress(boolean longPress) {
        this.longPress = longPress;
    }

    @Override
    public boolean longPress() {
        return longPress;
    }

    public void setStealth() {
        setStealth(0);
    }

    public void setStealth(float time) {
        if (!inStealth && cooldownStealth==0) {
            inStealth = true;
            Groups.unit.remove(this.as());
            durationStealth = 0;
        }
    }

    @Override
    public float stealthf() {
        if (inStealth){
            return 1f-durationStealth/stealthType.stealthDuration;
        }else {
            return 1f-cooldownStealth/stealthType.stealthCooldown;
        }
    }

    public void removeStealth() {
        removeStealth((durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
    }

    public void removeStealth(float time) {
        if (inStealth) {
            inStealth = false;
            Groups.unit.add(this.as());
            cooldownStealth = Math.min(stealthType.stealthCooldown, time);
        }
    }
    public void drawAlpha() {
        Draw.alpha(getAlpha() * Draw.getColor().a);
    }

    public float getAlpha() {

        try {
            return !inStealth ? Draw.getColor().a : Vars.player.team() == team() ? 0.25f : 0f;
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    @Override
    @ModAnnotations.OverrideCallSuper
    public void draw() {
        stealthType.alpha = getAlpha();
    }

    @Override
    public void updateStealthStatus() {
        if (inStealth) {
            while (Groups.unit.contains(u -> u == this.<Unit>as())) {
                Groups.unit.remove(this.as());
            }
            if (durationStealth >= stealthType.stealthDuration || selectStealth()) {
//                removeStealth((durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
                ModCall.setStealthStatus(this.as(),false,(durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
                Seq<Unit> stealthUnitc = controlling().select((u) -> u instanceof StealthUnitc);
                if (stealthUnitc.size > 0) {
                    stealthUnitc.each(unit -> {
                        ModCall.setStealthStatus(unit,false,-1);
                    });
                }
            }
        } else if (cooldownStealth == 0f && selectStealth()) {
            ModCall.setStealthStatus(this.as(),true,-1);
            Seq<Unit> stealthUnitc = controlling().select((u) -> u instanceof StealthUnitc);
            if (stealthUnitc.size > 0) {
                stealthUnitc.each(unit -> {
                    ModCall.setStealthStatus(unit,true,-1);
                });
            }
        }
    }
    @Override
    public void update() {

    }
}
