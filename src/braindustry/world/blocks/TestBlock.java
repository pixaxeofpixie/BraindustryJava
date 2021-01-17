package braindustry.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import ModVars.modVars;
import braindustry.content.ModFx;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustryAddition.world.blocks.BuildingLabel;

import static ModVars.modFunc.*;

public class TestBlock extends Block {
    public final int timerAny;
    public TestBlock(String name) {
        super(name);
        timerAny=timers++;
        this.rotate = true;
        this.destructible = true;
        this.update=true;
        configurable=true;
    }

    @Override
    public void load() {

        Core.atlas.addRegion(this.name,Core.atlas.find(fullName("testBlock")));
        super.load();
//        this.region=Core.atlas.find(getFullName("testBlock"));
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        TextureRegion reg = this.getRequestRegion(req, list);
        Draw.rect(reg, req.drawx(), req.drawy(),this.size*8,this.size*8, 0f);
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        }
    }

    public class TestBlockBuild extends Building implements BuildingLabel {
        private float time=0;

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.table((t)->{
               t.button(Icon.terminal,()->{
                   ModFx.Spirals.at(this.x,this.y,size, Pal.lancerLaser);
               });
            });
            table.slider(0,360,0.1f, modVars.settings.getFloat("angle"),(f)->{
                modVars.settings.setFloat("angle",f);
            });
        }

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {

            Building building = super.init(tile, team, shouldAdd, rotation);
            newLabel(()->building,(build)->{
                float angle=new Vec2(tileX(),tileY()).angleTo(tile.nearby(build.rotation));
                Tile other=tile.nearby(Geometry.d4x(build.rotation),Geometry.d4y(build.rotation));
                angle= Angles.angle(tile.getX(), tile.getY(), other.getX(), other.getY());
                angle= modVars.settings.getFloat("angle");
                if (true)return ""+angle;
                if (true)return build.rotation+"\n"+angle;
                return "time: "+(int)time;
            });
            return building;
        }

        @Override
        public void playerPlaced(Object config) {
            super.playerPlaced(config);
        }

        public Building nearby(int rotation) {
            if (rotation == 0) {
                return Vars.world.build(this.tile.x + (int)(this.block.size/2+1), this.tile.y);
            } else if (rotation == 1) {
                return Vars.world.build(this.tile.x, this.tile.y + (int)(this.block.size/2+1));
            } else if (rotation == 2) {
                return Vars.world.build(this.tile.x - Mathf.ceil(this.block.size/2f), this.tile.y);
            } else {
                return rotation == 3 ? Vars.world.build(this.tile.x, this.tile.y - Mathf.ceil(this.block.size/2f)) : null;
            }
        }
        public Building front() {
            return this.nearby(this.rotation);
        }

        @Override
        public void updateTile() {
            super.updateTile();
            time+=this.delta()/60f;
            if (this.timer.get(timerAny,5*60)){
//                Fx.upgradeCore.at(this.x,this.y,this.block.size, Color.white);
            }
        }

        public void draw() {
            Draw.rect(this.block.region, this.x, this.y,this.block.size*8,this.block.size*8, 0.0F);
            Building front = this.front();
//            if (true)return;
            if (front != null && front.block != null) {
                float size = front.block.size*8f;
                Draw.z(Layer.blockBuilding+1f);
                Draw.color(Color.green);
                Draw.alpha(0.25f);
                float offset =  Mathf.ceil(size / 2f);
                Lines.rect(front.x-offset,front.y-offset, size,size);
            }
        }
    }
}
