package Gas.world.blocks.gas;

import Gas.annotations.GasAnnotations;
import Gas.gen.GasBuilding;
import Gas.world.GasBlock;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.meta.BlockGroup;

public class GasGasBlock extends GasBlock {

    public @GasAnnotations.Load("@-gas")
    TextureRegion gasRegion;
    public @GasAnnotations.Load("@-top") TextureRegion topRegion;
    public @GasAnnotations.Load("@-bottom") TextureRegion bottomRegion;
    public GasGasBlock(String name) {
        super(name);
        update = true;
        solid = true;
        hasGas = true;
        group = BlockGroup.liquids;
        outputsGas = true;
    }
    public class GasBuild extends GasBuilding {
        @Override
        public void draw(){
            float rotation = rotate ? rotdeg() : 0;
            Draw.rect(bottomRegion, x, y, rotation);

            if(gasses.total() > 0.001f){
                Drawf.liquid(gasRegion, x, y, gasses.total() / gasCapacity, gasses.current().color);
            }

            Draw.rect(topRegion, x, y, rotation);
        }
    }
}
