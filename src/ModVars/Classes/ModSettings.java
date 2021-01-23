package ModVars.Classes;

import ModVars.modVars;
import arc.Core;
import arc.Events;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextButton;
import arc.util.Strings;
import braindustry.MainModClass;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import static ModVars.modFunc.fullName;
import static ModVars.modVars.modInfo;

public class ModSettings {
    public ModSettings(){
        addEvent();
    }
    private String full(String name){
        return fullName(name);
    }
    private void put(String name,Object value){
        if (Core.settings.has(full(name))){
            Core.settings.remove(full(name));
        }
        Core.settings.put(full(name),value);
    };
    private Object get(String name,Object def){
        return Core.settings.get(full(name),def);
    }
    public boolean getBool(String name, boolean def){
        return (boolean)get(name,def);
    }
    public boolean getBool(String name){
        return getBool(name,false);
    }
    public void setBool(String name, boolean def){
        put(name,def);
    }
    public float getFloat(String name, float def){
        return (float)get(name,def);
    }
    public float getFloat(String name){
        return getFloat(name,0.0F);
    }
    public void setFloat(String name, float def){
        put(name,def);
    }
    public boolean cheating(){
        return getBool("cheat",false);
    }
    public void cheating(boolean cheating){
        put("cheat",cheating);
    }
    public boolean debug(){
        return getBool("debug",false);
    }
    public void debug(boolean debug){
        put("debug",debug);
    }
    private void addEvent() {
        Events.on(EventType.ClientLoadEvent.class,(e)->{
            Vars.ui.settings.row();
            Vars.ui.settings.button((button) -> {
                        button.image(MainModClass.getIcon()).size(64, 64);
//                        button.setSize(80f,80f);
                        button.label(() -> Strings.format("@", modInfo==null?"":modInfo.meta.displayName));
                    },
                    () -> {
                        modVars.settingsDialog.show();
                    }).height(84).right().row();
        });
    }
}
