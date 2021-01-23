package braindustry.input;

import arc.Core;
import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;
import arc.struct.Seq;
import braindustry.MainModClass;

public enum ModBinding implements KeyBinds.KeyBind {

    teamDialogBing(KeyCode.b, MainModClass::openTeamChooseDialog),
    unitDialogBing(KeyCode.u,MainModClass::openUnitChooseDialog),
    itemManagerDialogBing(KeyCode.unknown,MainModClass::openModCheatItemsMenu),
    unlockDialogBing(KeyCode.unknown,MainModClass::openUnlockContentDialog),
    stealthBing(KeyCode.h),
    ;

    private final KeyBinds.KeybindValue defaultValue;
    private final KeyCode defaultKey;
    private final String category;
    private final Runnable event;

    private ModBinding(KeyBinds.KeybindValue defaultValue, String category,Runnable runnable) {
        this.defaultValue = defaultValue;
        this.category = category;
        if (defaultValue instanceof KeyCode){
            defaultKey=(KeyCode)defaultValue;
        } else {
            defaultKey=null;
        }
        ;
        if (runnable==null) {
            runnable = () -> {
            };
        }
        event=runnable;
    }

    private ModBinding(KeyBinds.KeybindValue defaultValue) {
        this(defaultValue, null,null);
    }
    private ModBinding(KeyBinds.KeybindValue defaultValue,Runnable runnable) {
        this(defaultValue, null,runnable);
    }
    public KeyBinds.KeybindValue defaultValue(InputDevice.DeviceType type) {
        return this.defaultValue;
    }

    public String category() {
        return this.category;
    }
}
