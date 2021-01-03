package braindustry.modVars.Classes.UI.Cheat;

import arc.func.Cons;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.struct.Seq;
import mindustry.game.Team;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class TeamChooseDialog extends BaseDialog {
    public TeamChooseDialog(Cons<Team> confirm){
        super("Choose team:");
        this.cont.table(i -> {
            i.table(t -> {
                final int buttonSize = 20;
                for (Team team : Team.all) {
                    if (Seq.with(Team.all).indexOf(team) % 20 == 0) t.row();
                    ImageButton button = new ImageButton(Tex.whitePane, Styles.clearToggleTransi);
                    button.clearChildren();
                    Image image = new Image();
                    button.background(image.getDrawable()).setColor(team.color);
                    Cell<Image> imageCell = button.add(image).color(team.color).size(buttonSize);
                    button.clicked(() -> {
                        confirm.get(team);
                        this.hide();
                    });
                    t.add(button).color(team.color).width(buttonSize).height(buttonSize).pad(6);
                }
            });
        }).width(360).bottom().center();
        this.addCloseButton();
    }
}
