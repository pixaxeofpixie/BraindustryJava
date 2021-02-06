package braindustry;

import arc.ApplicationCore;
import arc.ApplicationListener;
import arc.Events;
import arc.files.Fi;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.ClientLauncher;
import mindustry.Vars;
import mindustry.game.EventType;
import static ModVars.modVars.*;
import static mindustry.Vars.ui;

public class ModListener implements ApplicationListener {
    public static Seq<Runnable> updaters=new Seq<>();
    protected class Editor extends ApplicationCore {

        @Override
        public void setup() {

        }
    }
    public static void load(){
        Log.info("\n @",ui);
        listener=new ModListener();
        if (Vars.platform instanceof ClientLauncher){
            launcher=(ClientLauncher) Vars.platform;
            launcher.add(listener);

        } else {
            Events.on(EventType.Trigger.update.getClass(),(e)->{
                if (e== EventType.Trigger.update){
                    listener.update();
                }
            });
        }
    }

    @Override
    public void dispose() {
        if (!loaded)return;
        Log.info("");
    }

    @Override
    public void init() {
        if (!loaded)return;
    }

    public void update() {
        updaters.each(Runnable::run);
    }

}
