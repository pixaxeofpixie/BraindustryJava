package braindustry;

import arc.ApplicationListener;
import arc.Events;
import arc.files.Fi;
import arc.struct.Seq;
import mindustry.game.EventType;

public class ModListener {
    public static Seq<Runnable> updaters=new Seq<>();
    public static void load(){
        ModListener listener=new ModListener();
        Events.on(EventType.Trigger.update.getClass(),(e)->{
            if (e== EventType.Trigger.update){
                listener.update();
            }
        });
    }
    public void update() {
        updaters.each(Runnable::run);
    }

}
