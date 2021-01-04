package Gas;

import Gas.content.BasicBlocks;
import Gas.content.Gasses;

public class GasInit {
    private static boolean init=false;
    public static void init(boolean debug){
        if (init)return;
        init=true;
        new Gasses().load();
        if (debug){
            new BasicBlocks().load();
        }
    }
}
