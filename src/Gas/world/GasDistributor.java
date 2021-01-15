package Gas.world;

public class GasDistributor extends GasBlock {
    public GasDistributor(String name) {
        super(name);
        this.consumesPower = false;
        this.outputsPower = true;
    }
}
