package braindustry.type;

import arc.struct.Seq;

public class Runners extends Seq<Runnable> {
    public void run(){
        each(Runnable::run);
    }
    public void crun(){
        run();
        clear();
    }
}
