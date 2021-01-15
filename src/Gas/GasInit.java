package Gas;

import Gas.content.BasicBlocks;
import Gas.content.DebugBlocks;
import Gas.content.Gasses;
import Gas.gen.Cloud;
import mindustry.gen.EntityMapping;

public class GasInit {
    private static boolean init=false;
    public static void init(boolean debug){
        if (init)return;
        init=true;
        new Gasses().load();
        if (debug){
            new DebugBlocks().load();
        }
        EntityMapping.idMap[Cloud.classId]=Cloud::new;
        EntityMapping.idMap[39]=Cloud::new;
        new BasicBlocks().load();
    }
}
