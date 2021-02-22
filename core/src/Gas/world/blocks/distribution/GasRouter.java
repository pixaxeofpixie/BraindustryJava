package Gas.world.blocks.distribution;

import Gas.annotations.GasAnnotations;
import Gas.gen.GasBuilding;
import Gas.type.Gas;
import Gas.world.GasBlock;
import Gas.world.blocks.gas.GasGasBlock;
import arc.graphics.g2d.TextureRegion;
import mindustry.annotations.Annotations;
import mindustry.gen.Building;
import mindustry.world.meta.BlockGroup;

public class GasRouter extends GasGasBlock {
    public GasRouter(String name){
        super(name);
        noUpdateDisabled = true;
    }

    public class GasRouterBuild extends GasBuild {

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
