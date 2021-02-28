package braindustry.world.blocks.power;

import Gas.world.blocks.power.GasImpactReactor;
import arc.graphics.g2d.Draw;
import mindustry.world.blocks.power.ImpactReactor;
import braindustry.graphics.BlackHoleDrawer;

public class BlackHoleReactor extends GasImpactReactor {
    public float blackHoleHitSize = 14f;

    public BlackHoleReactor(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
    }

    public class BlackHoleReactorBuild extends GasImpactReactor.GasImpactReactorBuild {
        BlackHoleDrawer drawer;
        @Override
        public void created() {
            super.created();
            drawer=new BlackHoleDrawer(this);
        }

        @Override
        public void updateTile() {
            drawer.update();
            super.updateTile();
        }

        @Override
        public void draw() {
            Draw.rect(bottomRegion, this.x, this.y);


            if(warmup>0.05f)drawer.drawBlackHole(this,blackHoleHitSize*warmup,warmup);

            Draw.reset();
            Draw.rect(region, this.x, this.y);
            Draw.color();
        }
    }
}
