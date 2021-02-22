package Gas.world;

import Gas.GasBuilding;
import Gas.type.Gas;
import Gas.world.GasBlock;
import arc.util.Log;
import mindustry.gen.Building;

public class GasRouter extends GasBlock {
    public GasRouter(String name){
        super(name);

        noUpdateDisabled = true;
        System.out.println(newBuilding());
    }

    public class GasRouterBuild extends GasBuilding {
        @Override
        public void updateTile(){
            if(gasses.total() > 0.01f){
                dumpGas(gasses.current());
            }
        }

        @Override
        public boolean acceptGas(Building source, Gas gas){
            return (gasses.current() == gas || gasses.currentAmount() < 0.2f);
        }
    }
}
