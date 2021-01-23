package ModVars.Classes.UI;

import ModVars.Classes.UI.settings.ModKeybindDialog;
import arc.input.KeyCode;
import arc.scene.ui.Image;
import arc.util.Align;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;

public class ModControlsDialog extends ModKeybindDialog {
    public ModControlsDialog(){
        setFillParent(true);
        title.setAlignment(Align.center);
        titleTable.row();
        titleTable.add(new Image()).growX().height(3f).pad(4f).get().setColor(Pal.accent);
    }

    @Override
    public void addCloseButton(){
        buttons.button("@back", Icon.left, this::hide).size(230f, 64f);

        keyDown(key -> {
            if(key == KeyCode.escape || key == KeyCode.back) hide();
        });
    }
}
