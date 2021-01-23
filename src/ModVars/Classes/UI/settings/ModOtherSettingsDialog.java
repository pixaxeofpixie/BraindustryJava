package ModVars.Classes.UI.settings;

import ModVars.modVars;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

import static ModVars.modFunc.fullName;

public class ModOtherSettingsDialog extends Dialog {
    public ModOtherSettingsDialog() {
        super("@dialogs.other-settings");
        setup();
    }
    private void setup(){
        cont.table((t)->{
            t.check("@cheat", modVars.settings.cheating(),(b)->{
                modVars.settings.cheating(b);
            }).row();
            t.check("@debug",modVars.settings.debug(),(b)->{
                modVars.settings.debug(b);
            }).row();
            t.slider(0,360,0.1f,modVars.settings.getFloat("angle"),(b)->{
                modVars.settings. setFloat("angle",b);
            }).row();

        }).row();

        closeOnBack();
        cont.button("@back", Icon.leftOpen, () -> {
            hide();
        }).size(230.0F, 64.0F).bottom();
    }
}
