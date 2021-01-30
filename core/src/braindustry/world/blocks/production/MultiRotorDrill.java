package braindustry.world.blocks.production;

import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import mindustry.entities.units.BuildPlan;
import mindustry.world.blocks.production.Drill;

public class MultiRotorDrill extends Drill {
    public Seq<Vec2> rotatorPoints=new Seq<>();
    public boolean drawRotator=true;
    public MultiRotorDrill(String name) {
        super(name);
    }
    @Override
    public void init() {
        super.init();
    }
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        Draw.rect(this.region, req.drawx(), req.drawy(), !this.rotate ? 0.0F : (float)(req.rotation * 90));
        if (drawRotator){
            float x=req.drawx()-Mathf.floor(this.size/2f)*8,
                    y=req.drawy()-Mathf.floor(this.size/2f)*8;
            rotatorPoints.each((vec)->{
                Draw.rect(MultiRotorDrill.this.rotatorRegion, x+vec.x*8, y+vec.y*8);
                Draw.rect(MultiRotorDrill.this.topRegion, x+vec.x*8, y+vec.y*8);
            });
        }
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        }

    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{this.region};
    }

    public class MultiRotorDrillBuild extends DrillBuild{

        public void draw() {
            float s = 0.3F;
            float ts = 0.6F;
            Draw.rect(MultiRotorDrill.this.region, this.x, this.y);
            super.drawCracks();
            if (MultiRotorDrill.this.drawRim) {
                Draw.color(MultiRotorDrill.this.heatColor);
                Draw.alpha(this.warmup * ts * (1.0F - s + Mathf.absin(Time.time, 3.0F, s)));
                Draw.blend(Blending.additive);
                Draw.rect(MultiRotorDrill.this.rimRegion, this.x, this.y);
                Draw.blend();
                Draw.color();
            }
            if (rotatorPoints.size>0) {
                float x=this.x-Mathf.floor(this.block.size/2f)*8,
                        y=this.y-Mathf.floor(this.block.size/2f)*8;
                rotatorPoints.each((vec)->{
                    Draw.rect(MultiRotorDrill.this.rotatorRegion, x+vec.x*8, y+vec.y*8, this.timeDrilled * MultiRotorDrill.this.rotateSpeed);
                    Draw.rect(MultiRotorDrill.this.topRegion, x+vec.x*8, y+vec.y*8);
                });
            }
            if (this.dominantItem != null && MultiRotorDrill.this.drawMineItem) {
                Draw.color(this.dominantItem.color);
                Draw.rect(MultiRotorDrill.this.itemRegion, this.x, this.y);
                Draw.color();
            }

        }
    }
}
