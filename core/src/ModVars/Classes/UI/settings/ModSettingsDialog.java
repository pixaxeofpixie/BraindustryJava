package ModVars.Classes.UI.settings;

import ModVars.modVars;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class ModSettingsDialog extends Dialog {
    public ModSettingsDialog(){
        super("@settings");
        setup();
    }
    public Table main;
    private void setup() {
//        otherSettings= modVars.otherSettingsDialog;
        main=new Table(Tex.button);
        main.defaults().size(300f, 60f);
        TextButton.TextButtonStyle style = Styles.cleart;
        main.button("@settings.controls",style, modVars.controls::show).row();
        main.button("@settings.other",style, ()->{
            modVars.otherSettingsDialog.show();
        });
        cont.add(main);
        addCloseButton();
    }

    @Override
    public void addCloseButton(){
        this.buttons.button("@back", Icon.leftOpen, () -> {
            this.hide();
        }).size(230.0F, 64.0F);
    }
}
