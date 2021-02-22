package ModVars.Classes.UI.Cheat;

import arc.func.Boolf;
import arc.graphics.Color;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

import static ModVars. modFunc.getInfoDialog;

public class UnitChooseDialog extends BaseDialog {
    public UnitChooseDialog(Boolf<UnitType> check) {
        super("Choose unit:");
        Table table=new Table();
        final int buttonSize=100;
        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(true, false);
        int[] index={0};
        for (UnitType unitType : Vars.content.units()) {
            if (unitType == UnitTypes.block)continue;
            if (index[0]%5==0)table.row();
            index[0]++;
            Button button = new Button();
            button.clearChildren();
            Image image = new Image(unitType.region);
            Cell<Image> imageCell=button.add(image);
            float imageSize=buttonSize / 100f * 70f;
            if (image.getWidth()==image.getHeight()) {
                imageCell.size(imageSize);
            } else{
                float width=image.getWidth(),height=image.getHeight();

                imageCell.size(imageSize*(width/height),imageSize);
            }

            if (unitType != UnitTypes.block) {
                button.clicked(() -> {
                    if (check.get(unitType))this.hide();

                });
            } else {
                button.clicked(() -> {
                    getInfoDialog("","Don't use Block unit","",Color.scarlet).show();
                });
            }
            table.add(button).width(buttonSize).height(buttonSize).pad(6);
        }
        this.cont.add(pane).growY().growX().bottom().center();

        this.addCloseButton();
    }


}
