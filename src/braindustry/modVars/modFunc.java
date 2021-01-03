package braindustry.modVars;


import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.Cubemap;
import arc.scene.ui.Dialog;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.gen.EntityMapping;

import static braindustry.modVars.modVars.*;

public class modFunc {
    public static int addEntityMappingIdMap(Prov prov){
        for (int i = 0; i < EntityMapping.idMap.length; i++) {
            if (EntityMapping.idMap[i]==null){
                EntityMapping.idMap[i]=prov;
                return i;
            }
        }
        return -1;
    }
    public static void inTry(Runnable runnable){
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
        TechTree.TechNode parent = (TechTree.TechNode)TechTree.getNotNull(parentContent);
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
    public static void addResearch(String researchName, UnlockableContent unlock){
        TechTree.TechNode node = new TechTree.TechNode(null, unlock, unlock.researchRequirements());
        TechTree.TechNode parent = (TechTree.TechNode)TechTree.all.find((t) -> {
            return t.content.name.equals(researchName) || t.content.name.equals(getFullName(researchName));
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
    public static String getFullName(String name){
        return Strings.format("@-@",modInfo.name,name);
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
                ((arc.scene.ui.Label)this.cont.add(message).pad(2.0F).growX().wrap().get()).setAlignment(1);
                this.cont.row();
                this.cont.button("@ok", this::hide).size(120.0F, 50.0F).pad(4.0F);
                this.closeOnBack();
            }
        };
    }
    public static String getTranslateName(String name){
        return Strings.format("@.@",modInfo.name,name);
    }
    public static void showException(Exception exception){
        if (Vars.ui!=null){
            Vars.ui.showException(Strings.format("@: error",modInfo.meta.displayName), exception);
        } else {
            Events.on(EventType.ClientLoadEvent.class, event -> {
                Vars.ui.showException(Strings.format("@: error",modInfo.meta.displayName), exception);
            });
        }
    }
    public static <T> void  EventOn(Class<T> type, Cons<T> listener){
        Events.on(type,(e)->{
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
        Log.info("[@/@]: @",modInfo.file.name(),modInfo.meta.displayName,Strings.format(text, args));
    }
}