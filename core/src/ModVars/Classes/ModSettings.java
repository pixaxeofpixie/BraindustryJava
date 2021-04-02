package ModVars.Classes;

import ModVars.ModEnums;
import ModVars.modVars;
import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.util.Strings;
import braindustry.BraindustryMod;
import mindustry.Vars;
import mindustry.game.EventType;

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
    public int getInt(String name,int def) {
        Object o = get(name, def);
        if (o instanceof Boolean){
            put(name,o==(Object) true?0:1);
            return getInt(name,def);
        }
        return (int) o;
    }
    public int getInt(String name) {
        return getInt(name,0);
    }
    public void setInt(String name,int value){
        put(name,value);
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
    public  ModEnums.CheatLevel cheatLevel(){
        ModEnums.CheatLevel[] values = ModEnums.CheatLevel.values();
        return values[Mathf.mod(getInt("cheatLevel"), values.length)];
    }
    public void cheatLevel(ModEnums.CheatLevel value){
        setInt("cheatLevel",value.ordinal());
    }
    private void addEvent() {
        Events.on(EventType.ClientLoadEvent.class,(e)->{
            Vars.ui.settings.row();
            Vars.ui.settings.button((button) -> {
                        button.image(BraindustryMod.getIcon()).size(64, 64);
//                        button.setSize(80f,80f);
                        button.label(() -> Strings.format("@", modInfo==null?"":modInfo.meta.displayName));
                    },
                    () -> {
                        modVars.settingsDialog.show();
                    }).height(84).right().row();
        });
    }


    public void cheatLevelServer( ModEnums.CheatLevel level) {
        setInt("server-var-cheatLevel",level.ordinal());
    }

    public ModEnums.CheatLevel cheatLevelServer() {
        ModEnums.CheatLevel[] values = ModEnums.CheatLevel.values();
        return values[Mathf.mod(getInt("server-var-cheatLevel"), values.length)];
    }
}
