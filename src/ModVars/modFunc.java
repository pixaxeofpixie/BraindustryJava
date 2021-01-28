package ModVars;


import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Collapser;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.EntityMapping;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

import java.util.Objects;

import static ModVars.modVars.*;

public class modFunc {
    public static void spawnUnit(UnitType type, float x, float y, int amount, Team team){
        if (Vars.net.active() && !Vars.net.server()){
            Call.serverPacketReliable("spawn unit",Strings.format("@ @ @ @ @",type.id,x,y,amount,team.id));
        } else {
            for (int i = 0; i < amount; i++) {
                type.spawn(team,x,y);
            }
        }
    }
    public static int addEntityMappingIdMap(Prov prov) {
        for (int i = 0; i < EntityMapping.idMap.length; i++) {
            if (EntityMapping.idMap[i] == null){
                EntityMapping.idMap[i] = prov;
                return i;
            }
        }
        return -1;
    }

    public static void inTry(Runnable runnable) {
        try{
            runnable.run();
        } catch (Exception ex){
            showException(ex);
        }
    }

    public static void checkTranslate(UnlockableContent content){
        content.localizedName = Core.bundle.get(content.getContentType() + "." + content.name + ".name", content.localizedName);
        content.description = Core.bundle.get(content.getContentType() + "." + content.name + ".description",content.description);
    }

    public static void addResearch(UnlockableContent parentContent, UnlockableContent unlock){
        TechTree.TechNode node = new TechTree.TechNode(TechTree.getNotNull(parentContent), unlock, unlock.researchRequirements());
        TechTree.TechNode parent = TechTree.getNotNull(parentContent);
        if (parent == null) {
            showException(new IllegalArgumentException("Content '" + parentContent.name + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched."));
//            throw new IllegalArgumentException("Content '" + researchName + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched.");
        } else {
            if (!parent.children.contains(node)) {
                parent.children.add(node);
            }

            node.parent = parent;
        }
    }
    public static void inspectBuilding(){

    }
    public static void addResearch(String researchName, UnlockableContent unlock){
        TechTree.TechNode node = new TechTree.TechNode(null, unlock, unlock.researchRequirements());
        TechTree.TechNode parent = TechTree.all.find((t) -> {
            return t.content.name.equals(researchName) || t.content.name.equals(fullName(researchName));
        });

        if (parent == null) {
            showException(new IllegalArgumentException("Content '" + researchName + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched."));
//            throw new IllegalArgumentException("Content '" + researchName + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched.");
        } else {
            if (!parent.children.contains(node)) {
                parent.children.add(node);
            }

            node.parent = parent;
        }
    }

    public static String fullName(String name){
        return Strings.format("@-@",modInfo==null?"braindustry":modInfo.name,name);
    }

    public static Dialog getInfoDialog(String title, String subTitle, String message, Color lineColor){
        return new Dialog(title) {
            {
                this.setFillParent(true);
                this.cont.margin(15.0F);
                this.cont.add(subTitle);
                this.cont.row();
                this.cont.image().width(300.0F).pad(2.0F).height(4.0F).color(lineColor);
                this.cont.row();
                (this.cont.add(message).pad(2.0F).growX().wrap().get()).setAlignment(1);
                this.cont.row();
                this.cont.button("@ok", this::hide).size(120.0F, 50.0F).pad(4.0F);
                this.closeOnBack();
            }
        };
    }

    public static String getTranslateName(String name){
        return Strings.format("@.@", modInfo.name,name);
    }

    private static void showExceptionDialog(Throwable t) {
        showExceptionDialog("", t);
    }

    private static void showExceptionDialog(final String text, final Throwable exc) {
        (new Dialog("") {
            {
                String message = Strings.getFinalMessage(exc);
                this.setFillParent(true);
                this.cont.margin(15.0F);
                this.cont.add("@error.title").colspan(2);
                this.cont.row();
                this.cont.image().width(300.0F).pad(2.0F).colspan(2).height(4.0F).color(Color.scarlet);
                this.cont.row();
                ((Label)this.cont.add((text.startsWith("@") ? Core.bundle.get(text.substring(1)) : text) + (message == null ? "" : "\n[lightgray](" + message + ")")).colspan(2).wrap().growX().center().get()).setAlignment(1);
                this.cont.row();
                Collapser col = new Collapser((base) -> {
                    base.pane((t) -> {
                        t.margin(14.0F).add(Strings.neatError(exc)).color(Color.lightGray).left();
                    });
                }, true);
                Table var10000 = this.cont;
                TextButton.TextButtonStyle var10002 = Styles.togglet;
                Objects.requireNonNull(col);
                var10000.button("@details", var10002, col::toggle).size(180.0F, 50.0F).checked((b) -> {
                    return !col.isCollapsed();
                }).fillX().right();
                this.cont.button("@ok", this::hide).size(110.0F, 50.0F).fillX().left();
                this.cont.row();
                this.cont.add(col).colspan(2).pad(2.0F);
                this.closeOnBack();
            }
        }).show();
    }
    public static void showException(Exception exception){
        Log.err(exception);
        try {
            Vars.ui.showException(Strings.format("@: error", modInfo.meta.displayName), exception);
        } catch (NullPointerException n){
            Events.on(EventType.ClientLoadEvent.class, event -> {
                showExceptionDialog(Strings.format("@: error", modInfo==null?null:modInfo.meta.displayName), exception);
            });
        }
    }

    public static <T> void  EventOn(Class<T> type, Cons<T> listener){
        Events.on(type, (e) -> {
            try{
                listener.get(e);
            } catch (Exception ex){
                showException(ex);
            }
        });
    }

    public static float getDeltaTime() {
        return Time.delta / 60;
    }

    public static <T> Seq<T> addFirst(Seq<T> start, T object) {
        Seq<T> old = start.copy();
        start.clear();
        start.add(object);
        for (T obj : old) {
            start.add(obj);
        }

        return start;
    }

    public static String replaceToLowerStart(String line, String... replaceres) {
        String newLine = line;
        for (String replace : replaceres) {
            if (line.startsWith(replace)) {
                if (line.toLowerCase().equals(replace.toLowerCase())) {
                    line = replace.toLowerCase();
                    continue;
                }

                line = replace.toLowerCase() + line.split(replace)[1];
                break;
            }
        }
        return line;
    }

    public static void print(String text, Object... args) {
        Log.info("[@/@]: @",modInfo.file.name(), modInfo.meta.displayName, Strings.format(text, args));
    }
}