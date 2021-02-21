package ModVars.Classes.UI.Cheat;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Styles;

import static ModVars.modVars.*;

public class ModCheatMenu {

    Cons<Table> cons;
    boolean add = false;

    public ModCheatMenu(Cons<Table> cons) {
//        consSeq.add(cons);

        this.cons = cons;
        loadEvent();
    }

    public boolean isPlay() {
        return Vars.state.isPlaying() || Vars.state.isPaused();
    }

    public boolean canAdd() {
        return isPlay() || settings.cheating();
    }

    private void loadEvent() {
        Events.on(EventType.Trigger.class, (e) -> {
            if (!add && isPlay()) {
                Table table = new Table(Styles.black3);
                table.touchable = Touchable.enabled;
                table.update(() -> {
                    table.visible=isPlay();
                });
                cons.get(table);
                table.pack();
                table.act(0.0F);
                Core.scene.root.addChildAt(0, table);
                (table.getChildren().first()).act(0.0F);
                add=true;
            } else if (add && !isPlay()) {
                add = false;
            }
        });
    }
}
