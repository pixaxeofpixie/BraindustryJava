package Gas.world.blocks.distribution;

import Gas.gen.GasBuilding;
import Gas.type.Gas;
import Gas.world.GasBlock;
import mindustry.gen.Building;
import mindustry.world.meta.BlockGroup;

public class GasRouter extends GasBlock {
    public GasRouter(String name){
        super(name);
        update = true;
        solid = true;
        hasGas = true;
        group = BlockGroup.liquids;
        outputsGas = true;
        noUpdateDisabled = true;
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
