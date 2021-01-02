package braindustry.world.blocks;

import arc.Core;
import arc.func.Func;
import arc.func.Prov;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Tile;

import static braindustry.modVars.modFunc.*;

public class TestBlock extends Block {
    public TestBlock(String name) {
        super(name);
        this.rotate = true;
        this.destructible = true;
    }

    @Override
    public void load() {

        Core.atlas.addRegion(this.name,Core.atlas.find(getFullName("testBlock")));
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

    public class TestBlockBuild extends Building {
        private boolean label=false;

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            return super.init(tile, team, shouldAdd, rotation);
        }

        @Override
        public void playerPlaced(Object config) {
//            EntityGroup
//            new Color((Color)null);
            super.playerPlaced(config);
        }

        private void newLabel(Prov<Building> cons, Func<Building,String> name){
            Table table = (new Table(Styles.black3)).margin(4.0F);
            table.touchable = Touchable.disabled;
            Label label=new Label("");

            table.visibility=()->cons.get()!=null;
            table.update(() -> {
                if (Vars.state.isMenu()) {
                    table.remove();
                }
//                label.setText(""+this.rotation);
                Building build=cons.get();
                if (build==null){
                    return;
                }
                label.setText(name.get(build));
                Vec2 v = Core.camera.project(build.tile().worldx(), build.tile().worldy());
                table.setPosition(v.x, v.y, 1);
            });
            Building me=this;
            table.actions(new Action() {
                @Override
                public boolean act(float v) {
                    return !me.isValid();
                }
            }, Actions.remove());
            table.add(label).style(Styles.outlineLabel);
            table.pack();
            table.act(0.0F);
            Core.scene.root.addChildAt(0, table);
            ((Element)table.getChildren().first()).act(0.0F);
        }
        private void createLabel(){
            newLabel(()->this.front(),(build)->{
                return "\n"+build.getDisplayName();
            });

            newLabel(()->this,(build)->{
                return build.rotation+"";
            });
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

        public void draw() {
//            super.draw();
            if (!label){
                createLabel();
                label=true;
            }
            Draw.rect(this.block.region, this.x, this.y,this.block.size*8,this.block.size*8, 0.0F);
            Building front = this.front();
//            if (true)return;
            if (front != null && front.block != null) {
//                Draw.alpha(0.3f);
//                Draw.color(Color.green);
//                Draw.z(40f);

//                print("@, @, @, @, @, @",front.block.region, front.x, front.y, front.block.size, front.block.size, this.block.rotate ? this.rotdeg() : 0.0F);
//                Draw.rect(front.block.region, front.x, front.y, 0.0F);
            }
        }
    }
}
