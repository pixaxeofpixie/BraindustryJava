package ModVars.Classes.UI.settings;

import ModVars.modVars;
import arc.Core;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

import static ModVars.modFunc.fullName;

public class ModOtherSettingsDialog extends Dialog {
    public ModOtherSettingsDialog() {
        super("@dialogs.other-settings");
        setup();
    }
    private void setup(){

        cont.table(t->{
            t.left().defaults();
            t.slider(1f,5f,1f,modVars.settings.getFloat("background.screenshot.scl"),f->{
                modVars.settings.setFloat("background.screenshot.scl",f);
            }).left().width(180.0F);
            Cell<Label> label = t.label(() -> Strings.format("@: @/@", Core.bundle.get("background.screenshot.scl"), (int) modVars.settings.getFloat("background.screenshot.scl"), 5)).left();
            label.minWidth(label.get().getPrefWidth() / Scl.scl(1.0F) + 50.0F);
            t.row();
        }).padTop(3.0F).row();
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
