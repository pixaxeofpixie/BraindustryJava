package Gas.world.blocks.power;

import Gas.world.GasBlock;

public class GasPowerDistributor extends GasBlock {
    public GasPowerDistributor(String name) {
        super(name);
        this.consumesPower = false;
        this.outputsPower = true;
    }
}
