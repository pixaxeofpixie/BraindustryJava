package braindustry.input;

import ModVars.modVars;
import arc.Core;
import arc.KeyBinds;
import arc.input.KeyCode;

public class ModKeyBinds extends KeyBinds {
    public void load(){
        KeyBinds last=Core.keybinds;
        Core.keybinds=this;
        Core.settings.load();
        Core.keybinds=last;
    }
    public void save(){
        KeyBinds last=Core.keybinds;
        Core.keybinds=this;
        Core.settings.forceSave();
        Core.keybinds=last;
    }
    public boolean keyDown(KeyBind key) {
        return modVars.keyBinds.get(key).key != null && Core.input.keyDown(modVars.keyBinds.get(key).key);
    }

    public boolean keyTap(KeyBind key) {
        return modVars.keyBinds.get(key).key != null && Core.input.keyTap(modVars.keyBinds.get(key).key);
    }

    public boolean keyRelease(KeyBind key) {
        return modVars.keyBinds.get(key).key != null && Core.input.keyRelease( modVars.keyBinds.get(key).key);
    }

    public float axis(KeyBind key) {
        Axis axis = modVars.keyBinds.get(key);
        if (axis.key != null) {
            return Core.input.axis(axis.key);
        } else {
            return Core.input.keyDown(axis.min) && Core.input.keyDown(axis.max) ? 0.0F : (Core.input.keyDown(axis.min) ? -1.0F : (Core.input.keyDown(axis.max) ? 1.0F : 0.0F));
        }
    }

    public float axisTap(KeyBind key) {
        Axis axis = modVars.keyBinds.get(key);
        if (axis.key != null) {
            return Core.input.axis(axis.key);
        } else {
            return Core.input.keyTap(axis.min) ? -1.0F : (Core.input.keyTap(axis.max) ? 1.0F : 0.0F);
        }
    }

}
