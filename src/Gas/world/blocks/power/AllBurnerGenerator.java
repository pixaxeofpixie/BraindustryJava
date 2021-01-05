package Gas.world.blocks.power;

import Gas.AllGenerator;
import Gas.type.Gas;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.graphics.Drawf;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.blocks.power.BurnerGenerator;

public class AllBurnerGenerator extends AllGenerator {
    public TextureRegion[] turbineRegions;
    public TextureRegion capRegion;
    public float turbineSpeed = 2.0F;


    public AllBurnerGenerator(boolean hasItems, boolean hasLiquids, boolean hasGas, String name) {
        super(hasItems, hasLiquids, hasGas, name);
    }

    public AllBurnerGenerator(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        this.turbineRegions = new TextureRegion[2];

        for(int i = 0; i < 2; ++i) {
            this.turbineRegions[i] = Core.atlas.find(this.name + "-turbine" + i + "");
        }

        this.capRegion = Core.atlas.find(this.name + "-cap");
    }

    protected float getLiquidEfficiency(Liquid liquid) {
        return liquid.flammability;
    }

    protected float getItemEfficiency(Item item) {
        return item.flammability;
    }

    @Override
    protected float getGasEfficiency(Gas gas) {
        return gas.flammability;
    }

    public TextureRegion[] icons() {
        return this.turbineRegions[0].found() ? new TextureRegion[]{this.region, this.turbineRegions[0], this.turbineRegions[1], this.capRegion} : super.icons();
    }

    public class AllBurnerGeneratorBuild extends AllGenerator.AllGeneratorBuild {
        public AllBurnerGeneratorBuild() {
            super();
        }

        public void draw() {
            super.draw();
            if (AllBurnerGenerator.this.turbineRegions[0].found()) {
                Draw.rect(AllBurnerGenerator.this.turbineRegions[0], this.x, this.y, this.totalTime * AllBurnerGenerator.this.turbineSpeed);
                Draw.rect(AllBurnerGenerator.this.turbineRegions[1], this.x, this.y, -this.totalTime * AllBurnerGenerator.this.turbineSpeed);
                Draw.rect(AllBurnerGenerator.this.capRegion, this.x, this.y);
                if (AllBurnerGenerator.this.hasLiquids) {
                    Drawf.liquid(AllBurnerGenerator.this.liquidRegion, this.x, this.y, this.liquids.total() / AllBurnerGenerator.this.liquidCapacity, this.liquids.current().color);
                }
                if (AllBurnerGenerator.this.hasGas) {
                    Drawf.liquid(AllBurnerGenerator.this.gasRegion, this.x, this.y, this.gasses.total() / AllBurnerGenerator.this.gasCapacity, this.gasses.current().color);
                }
            }

        }
    }
}