package braindustry.modVars.Classes.UI;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Styles;

import static braindustry.modVars.ModFunc.*;

public class ModGameMenu {
    static Seq<Cons<Table>> conses = new Seq<>();
    static boolean event = false;
    static boolean add = false;

    public ModGameMenu(Cons<Table> cons) {
//        consSeq.add(cons);
        conses.add(cons);
        loadEvent();
    }
    public static boolean isPlay(){
        return Vars.state.isPlaying() || Vars.state.isPaused();
    }
    private static void loadEvent() {
        print("loadEvent:");
        if (!event) {
            print("event add:");
            event = true;
            Events.on(EventType.Trigger.class, (e) -> {
                if (!add && isPlay()){

                    Table table=new Table(Styles.black3);
                    table.touchable = Touchable.enabled;
                    table.update(() -> {
                        table.visible=isPlay();
                    });
                    conses.each((c)->table.table(c::get));

                    table.pack();
                    table.act(0.0F);
                    Core.scene.root.addChildAt(0, table);
                    ((Element)table.getChildren().first()).act(0.0F);
                    print("add:");
                    add=true;
                } else if (add && !isPlay()){
                    add=false;
                }
            });
        }
    }
}
