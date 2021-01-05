package braindustry.world.blocks;

import arc.Core;
import arc.func.Func;
import arc.func.Prov;
import arc.math.geom.Vec2;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.ui.Styles;

import java.util.concurrent.atomic.AtomicBoolean;

public interface BuildingLabel {
    AtomicBoolean loadLabels=new AtomicBoolean(true);
    default void loadLabels(Runnable runnable){
        if (loadLabels.get()) {
            runnable.run();
            loadLabels.set(false);
        }

    }
    default void newLabel(Prov<Building> cons, Func<Building, String> name){
        newLabel(cons,name,(building)->false);
    }
    default void newLabel(Prov<Building> cons, Func<Building, String> name,Func<Building,Boolean> boolf){
        Table table = (new Table(Styles.black3)).margin(4.0F);
        table.touchable = Touchable.disabled;
        Label label=new Label("");

        table.visibility=()->cons.get()!=null && boolf.get(cons.get());
        table.update(() -> {
            if (Vars.state.isMenu()) {
                table.remove();
            }
//                label.setText(""+this.rotation);
            Building build=cons.get();
            if (build==null){
                return;
            }
            label.setText(name.get(build));
            Vec2 v = Core.camera.project(build.tile().worldx(), build.tile().worldy());
            table.setPosition(v.x, v.y, 1);
        });
        Building me=(Building) this;
        table.actions(new Action() {
            @Override
            public boolean act(float v) {
                return !me.isValid();
            }
        }, Actions.remove());
        table.add(label).style(Styles.outlineLabel);
        table.pack();
        table.act(0.0F);
        Core.scene.root.addChildAt(0, table);
        ((Element)table.getChildren().first()).act(0.0F);
    }
}
