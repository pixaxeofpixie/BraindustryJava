package Main.ModContent.world.blocks.sandbox;

import Main.ModContent.content.ModFx;
import arc.func.Cons;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Intersector;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.logic.Ranged;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ForceProjector;

public class StrangeBlock extends Block {
    public StrangeBlock(String name) {
        super(name);
    }
    float timeOffset =0;
    Color color=new Color(Color.green).lerp(Color.white,0.3f).lerp(Color.darkGray,0.1f);
    @Override
    public void init() {
        super.init();
        timeOffset = (Time.millis()*1000)%(360*100);
    }

    public class StrangeBlockBuild extends Building implements Ranged{
        private void drawEffect(){
            this.drawEffect(this.x,this.y,this.block.size);
        }
        private void drawEffect(float x,float y,float radius){
            float rad = (float)(StrangeBlock.this.size * 8) / 2.0F * 0.74F;
            float length = StrangeBlock.this.size;
            float time=Time.time+ timeOffset;
            Draw.z(99.9999F);
            Lines.stroke(radius*0.2F,StrangeBlock.this.color);
            Lines.square(x, y, rad * 1.22F, 45.0F);
            Lines.stroke(radius*0.4f, StrangeBlock.this.color);
            Lines.square(x, y, rad, time / length);
            Lines.square(x, y, rad, -time / length);
            Lines.stroke(radius*0.4f,StrangeBlock.this.color);
            Lines.swirl(x,y,radius*1.5f,(time/length)%360.0f,time / length);
        }
        public void drawShield(){
            if (this.enabled){
                drawEffect();
//                ModFx.doctorStrange.at(this.x,this.y,StrangeBlock.this.size, new Color(Color.green).lerp(Color.white,0.3f).lerp(Color.darkGray,0.1f));
            }
        }
        public boolean collide(Bullet other) {
//            other.team=this.team;
            if (other.team==this.team)return false;
            other.time=3;
            Entityc entity= other.owner();
            other.rotation(other.rotation()+180);
            if (entity instanceof Unit){
                Unit unit=(Unit)entity;
                float angle=new Vec2(other.x,other.y).angleTo(new Vec2(unit.x,unit.y));
                other.vel.angle(new Vec2(other.x-unit.x,other.y-unit.y));
            }
            other.lifetime=1000;
            other.team=this.team;
//            other.owner(this);
            return false;
        }
        public void draw(){
            super.draw();
            drawShield();
            if (this.enabled) {
//                Draw.alpha((Time.millis()%1000f)/1000f);
                Draw.blend(Blending.additive);
//                drawEffect(this.x,this.y,radius);
                Draw.color(StrangeBlock.this.color);
                Draw.alpha(0.4f);
                Fill.circle(this.x,this.y,this.radius);
                Draw.alpha(0.7f);
                Lines.circle(this.x,this.y,radius);
                Draw.blend();
                Draw.reset();
            }
        }
        public float phaseUseTime;
        public float phaseRadiusBoost;
        public float phaseShieldBoost;
        public float radius=100,radscl,shieldHealth=100;
        public TextureRegion topRegion;
        private ObjectMap<Bullet, Runnable> resetBullets=new ObjectMap<>();
        public ForceProjector.ForceBuild paramEntity;
        final Cons<Bullet> shieldConsumer = (trait) -> {
            if (trait.type.absorbable && Intersector.isInsideHexagon(this.x, this.y, radius * 2.0F, trait.x(), trait.y())) {
//                trait.drag=0;
                if (trait.team != this.team){
                    if (!resetBullets.containsKey(trait)) {
                        Team team=trait.team;
                        Vec2 vel=trait.vel().cpy();
                        IntSeq collided=new IntSeq(trait.collided);
                        float lifetime=trait.lifetime,time=trait.time;
                        resetBullets.put(trait,()->{
                            trait.team(team);
                            trait.vel=vel;
                            trait.collided=collided;
                            trait.lifetime=lifetime;
                            trait.time=time;
                        });
                        ModFx.absorb.at(trait.getX(), trait.getY(), StrangeBlock.this.color);
                    }
//                    trait.team(this.team);
                    trait.time(10);
                    trait.collided.clear();
                    trait.lifetime(100);
                    trait.vel.set(0f,0f);
                } else if (!resetBullets.containsKey(trait)){
                    trait.vel.add(0.01f,0.01f);
                }

//                paramEntity.hit = 1.0F;
            }

        };

        @Override
        public void remove() {
            super.remove();
            this.startTime();
        }

        @Override
        public void onDestroyed() {
            super.onDestroyed();
            this.startTime();
        }

        public void startTime(){
            for (ObjectMap.Entry<Bullet,Runnable> bullet:resetBullets){
                if (bullet.key.added){
                    bullet.value.run();
                }
            }
            resetBullets.clear();
        }
        public void updateTile() {
            float realRadius=100f;
            if (radius > 0.0F && this.enabled) {
                Groups.bullet.intersect(this.x - realRadius, this.y - realRadius, realRadius * 2.0F, realRadius * 2.0F, shieldConsumer);
            } else if(!this.enabled){
                this.startTime();
            }

        }
        @Override
        public float range() {
            return radius;
        }
    }
}
