package braindustry.ModVars.Classes.UI.Cheat;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Styles;

import static braindustry.ModVars.modVars.*;

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
//                print("add");
                Table table = new Table(Styles.black3);
                table.touchable = Touchable.enabled;
                table.update(() -> {
                    table.visible=isPlay();
//                    if (!canAdd()) table.remove();
                });
                cons.get(table);
//                    table.table(cons::get);

                table.pack();
                table.act(0.0F);
                Core.scene.root.addChildAt(0, table);
                ((Element) table.getChildren().first()).act(0.0F);
                add=true;
//                    print("add:");
            } else if (add && !isPlay()) {
                add = false;
            }
        });
    }
}
