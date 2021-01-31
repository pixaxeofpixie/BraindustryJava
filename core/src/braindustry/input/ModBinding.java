package braindustry.input;

import arc.Core;
import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;
import arc.struct.Seq;
import braindustry.MainModClass;

public enum ModBinding implements KeyBinds.KeyBind {

    teamDialogBing(KeyCode.b),
    unitDialogBing(KeyCode.u),
    rulesEditDialogBing(KeyCode.unknown),
    itemManagerDialogBing(KeyCode.unknown),
    unlockDialogBing(KeyCode.unknown),
    stealthBing(KeyCode.h),
    ;

    private final KeyBinds.KeybindValue defaultValue;
    private final String category;

    private ModBinding(KeyBinds.KeybindValue defaultValue, String category) {
        this.defaultValue = defaultValue;
        this.category = category;
    }

    private ModBinding(KeyBinds.KeybindValue defaultValue) {
        this(defaultValue, null);
    }
    public KeyBinds.KeybindValue defaultValue(InputDevice.DeviceType type) {
        return this.defaultValue;
    }

    public String category() {
        return this.category;
    }
}
