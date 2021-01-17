package braindustry.world.blocks.Wall;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

import static ModVars.modFunc.EventOn;

public class DynamicWallSecond extends Block {
    TextureRegion regionLight0, regionDark0,
            regionLight1, regionDark1,
            regionLight2, regionDark2;

    public DynamicWallSecond(String name) {
        super(name);
//        this.configurable = true;
        this.solid = true;
        this.destructible = true;
        this.group = BlockGroup.none;
        EventOn(EventType.ClientLoadEvent.class,(e)->{
            regionLight0 = Core.atlas.find(this.name + "-light-0");
            regionDark0 = Core.atlas.find(this.name + "-dark-0");
            regionLight1 = Core.atlas.find(this.name + "-light-1");
            regionDark1 = Core.atlas.find(this.name + "-dark-1");
            regionLight2 = Core.atlas.find(this.name + "-light-2");
            regionDark2 = Core.atlas.find(this.name + "-dark-2");
        });
    }

    public boolean canReplace(Block block){
        return block instanceof DynamicWallSecond || block== Blocks.air;
    }

    public class DynamicWallSecondBuild extends Building {

        public Seq<ToDrawObject> toDraw = new Seq<>();

        public void updateProximity() {
            Building[] globalNear = {
                    this.nearby(-1,0),
                    this.nearby(1,0),
                    this.nearby(0,-1),
                    this.nearby(0,1),
                    this.nearby(-1,-1),
                    this.nearby(1,-1),
                    this.nearby(-1,1),
                    this.nearby(1,1),
                    this.nearby(0,0),
            };
            Building[] localNear = {
                    this.nearby(-1, 0),
                    this.nearby(1, 0),
                    this.nearby(0, -1),
                    this.nearby(0, 1),
            };
            for (Building other : globalNear) {
                if (other == null || other.block() != this.block()) continue;
                if (!other.proximity.contains(this, true)) {
                    other.proximity.add(this);
                }
                other.onProximityUpdate();
            }
            for (Building other : localNear) {
                if (other == null || other.block() == this.block()) continue;
                if (!other.proximity.contains(this, true)) {
                    other.proximity.add(this);
                }
                other.onProximityUpdate();
            }
        }

        private boolean isNull(Building build) {
            return (build == null || build.block != this.block);
        }

        public void onProximityUpdate() {
            this.toDraw.clear();
            boolean
                    b_1f0=isNull(this.nearby(-1,0)),
                    b_1f_1=isNull(this.nearby(-1,-1)),
                    b0f_1=isNull(this.nearby(0,-1)),
                    b1f_1=isNull(this.nearby(1,-1)),
                    b1f0=isNull(this.nearby(1,0)),
                    b1f1=isNull(this.nearby(1,1)),
                    b0f1=isNull(this.nearby(0,1)),
                    b_1f1=isNull(this.nearby(-1,1))
                            ;
            if (b_1f0) {
                if (b0f1 && b0f_1){
                    this.toDraw.add(new ToDrawObject(regionDark0,false,false,0));
                } else if(!b0f1 && !b0f_1 && b_1f1 && b_1f_1) {
                    this.toDraw.add(new ToDrawObject(regionDark2,false,false,0));
                } else if(!b0f1 && b_1f1) {
                    this.toDraw.add(new ToDrawObject(regionDark1,false,false,0));
                } else if(!b0f_1 && b_1f_1) {
                    this.toDraw.add(new ToDrawObject(regionDark1,false,true,0));
                }else {
                    this.toDraw.add(new ToDrawObject(regionDark0,false,false,0));
                }
            }
            if (b0f_1){
                if (b1f0 && b_1f0){
                    this.toDraw.add(new ToDrawObject(regionDark0,true,false,3*90));
                } else if(!b1f0 && !b_1f0 && b1f_1 && b_1f_1) {
                    this.toDraw.add(new ToDrawObject(regionDark2,true,false,3*90));
                } else if(!b1f0 && b1f_1) {
                    this.toDraw.add(new ToDrawObject(regionDark1,true,false,3*90));
                } else if(!b_1f0 && b_1f_1) {
                    this.toDraw.add(new ToDrawObject(regionDark1,true,true,3*90));
                }else {
                    this.toDraw.add(new ToDrawObject(regionDark0,true,false,3*90));
                }
            }
            if (b1f0) {
                if (b0f_1 && b0f1){
                    this.toDraw.add(new ToDrawObject(regionLight0,false,false,0));
                } else if(!b0f1 && !b0f_1 && b1f1 && b1f_1) {
                    this.toDraw.add(new ToDrawObject(regionLight2,false,false,0));
                } else if(!b0f1 && b1f1) {
                    this.toDraw.add(new ToDrawObject(regionLight1,false,false,0));
                } else if(!b0f_1 && b1f_1) {
                    this.toDraw.add(new ToDrawObject(regionLight1,false,true,0));
                }else {
                    this.toDraw.add(new ToDrawObject(regionLight0,false,false,0));
                }
            };
            if (b0f1){
                if (b1f0 && b_1f0){
                    this.toDraw.add(new ToDrawObject(regionLight0,true,false,3*90));

                } else if(!b1f0 && !b_1f0 && b1f1 && b_1f1) {
                    this.toDraw.add(new ToDrawObject(regionLight2,true,false,3*90));

                } else if(!b1f0 && b1f1) {
                    this.toDraw.add(new ToDrawObject(regionLight1,true,false,3*90));

                } else if(!b_1f0 && b_1f1) {
                    this.toDraw.add(new ToDrawObject(regionLight1,true,true,90*3));

                }else {
                    this.toDraw.add(new ToDrawObject(regionLight0,true,false,3*90));
                }
            }

        }

        private class ToDrawObject {
            TextureRegion region;
            boolean flipX, flipY;
            float rotation;

            public void flip() {
                this.region.flip(flipX, flipY);
            }

            public ToDrawObject(TextureRegion region, boolean flipX, boolean flipY, float rotation) {
                this.region = region;
                this.flipX = flipX;
                this.flipY = flipY;
                this.rotation = rotation;
            }
        }

        public void draw() {
            super.draw();
            Draw.reset();
            for (ToDrawObject obj : toDraw) {
                obj.flip();
                Draw.rect(obj.region, this.x, this.y, obj.rotation);
                obj.flip();
            }
        }
    }
}
