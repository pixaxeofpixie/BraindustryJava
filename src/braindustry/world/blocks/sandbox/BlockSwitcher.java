package braindustry.world.blocks.sandbox;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.ModVars.modVars;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.io.JsonIO;
import mindustry.logic.LAccess;
import mindustry.world.Block;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.power.PowerGraph;

public class BlockSwitcher extends Block {
    public TextureRegion laser;
    public TextureRegion laserEnd;
    public float laserRange = 6.0F;
    public Color laserColor1;
    public Color laserColor2;
    public Boolf<Building> blockFilter=(build)-> true;
    public Cons<Building> action =(build)-> {
       boolean enable= (build instanceof ControlBlock && ((ControlBlock) build).isControlled() && !(build instanceof BlockSwitcherBuild));
        build.control(LAccess.enabled, enable ? 1 : 0, 0, 0, 0);
        build.enabledControlTime = 30.0F;
    };
    public BlockSwitcher(String name) {
        super(name);
        this.update = true;
        this.solid = true;
        this.laserColor1 = Color.white;
        this.laserColor2 = Pal.powerLight;
        this.configurable = true;
        this.swapDiagonalPlacement = true;
        this.config(Integer.class, (e, value) -> {
            BlockSwitcherBuild entity = (BlockSwitcherBuild) e;
            Building other = Vars.world.build(value);
            boolean contains = entity.links.contains(other);
            boolean valid = other != null && other.power != null;
            boolean good = other.dst(entity) <= BlockSwitcher.this.laserRange;
            entity.handleBuilding(other);

        });
    }

    @Override
    public void load() {
        super.load();
        this.laser = Core.atlas.find("laser");
        this.laserEnd = Core.atlas.find("laser-end");
    }

    protected void drawLaser(Team team, float x1, float y1, float x2, float y2, int size1, int size2, boolean enable) {
        float angle1 = Angles.angle(x1, y1, x2, y2);
        float vx = Mathf.cosDeg(angle1);
        float vy = Mathf.sinDeg(angle1);
        float len1 = (float) (size1 * 8) / 2.0F - 1.5F;
        float len2 = (float) (size2 * 8) / 2.0F - 1.5F;
        Draw.color(enable ? Color.green : Pal.redDust);
        Drawf.laser(team, modVars.modAtlas.laser, modVars.modAtlas.laserEnd, x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, 0.25F);
    }

    public boolean goodBuilding(BlockSwitcherBuild forB, Building other) {
        return other.dst(forB) <= (laserRange + other.block.size+Mathf.ceil(forB.block.size/2f)) * 8f && forB != other && forB.isValid() && other.isValid() && blockFilter.get(other);
    }

    public class BlockSwitcherBuild extends Building {
        Seq<Building> links = new Seq<>();

        public void placed() {
            links.clear();
        }


        public void dropped() {
            this.power.links.clear();
            this.power.graph = new PowerGraph();
            this.power.graph.add(this);
        }
        private Seq<Runnable> onUpdate=new Seq<>();
        public void updateTile() {
            onUpdate.each(Runnable::run);
            onUpdate.clear();
            Seq<Building> catchLinks = links.copy();
            catchLinks.each((link) -> {
                if (!BlockSwitcher.this.goodBuilding(this, link)) links.remove(link);
            });
            links.each(link -> {
                action.get(link);
            });
        }

        public boolean handleBuilding(Building other) {

            if (!this.links.contains(other) && BlockSwitcher.this.goodBuilding(this, other)) {
                this.links.add(other);
                return true;
            } else if (this.links.contains(other) && BlockSwitcher.this.goodBuilding(this, other)) {
                this.links.remove(other);
                return true;
            } else {
                return false;
            }
        }

        public boolean onConfigureTileTapped(Building other) {
            if (handleBuilding(other)) {
//                this.deselect();
                return false;
            }
            return true;
        }

        public void drawSelect() {
            super.drawSelect();
            Lines.stroke(1.0F);
            Draw.color(Pal.accent);
            Drawf.circles(this.x, this.y, (BlockSwitcher.this.laserRange+Mathf.ceil(block.size/2f)) * 8.0F);
            Draw.reset();
        }

        public void drawConfigure() {
            Drawf.circles(this.x, this.y, (float) (this.tile.block().size * 8) / 2.0F + 1.0F + Mathf.absin(Time.time, 4.0F, 1.0F));
            Drawf.circles(this.x, this.y, BlockSwitcher.this.laserRange * 8.0F);

            for (int x = (int) ((float) this.tile.x - BlockSwitcher.this.laserRange - 2.0F); (float) x <= (float) this.tile.x + BlockSwitcher.this.laserRange + 2.0F; ++x) {
                for (int y = (int) ((float) this.tile.y - BlockSwitcher.this.laserRange - 2.0F); (float) y <= (float) this.tile.y + BlockSwitcher.this.laserRange + 2.0F; ++y) {
                    Building link = Vars.world.build(x, y);
                    boolean linked = this.linked(link);
                    if (linked) {
                        Drawf.square(link.x, link.y, (float) (link.block.size * 8) / 2.0F + 1.0F, Pal.place);
                    }

                }
            }

            Draw.reset();
        }

        public void draw() {
            super.draw();
            if (!Mathf.zero(Vars.renderer.laserOpacity)) {
                Draw.z(70.0F);
                this.links.each((link) -> {
                    BlockSwitcher.this.drawLaser(this.team, this.x, this.y, link.x, link.y, BlockSwitcher.this.size, link.block.size, link.enabled);
                });

                Draw.reset();
            }
        }

        private Seq<Integer> getPosses(){
            Seq<Integer> posses = new Seq<>();
            links.each((building -> {
                posses.add(building.pos());
            }));

            return posses;
        }
        public String config() {
            return JsonIO.json().toJson(getPosses());
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.str(config());
        }
        public void handleString(String value) {
            Seq<Integer> posses = JsonIO.json().fromJson(Seq.class, value);

            onUpdate.add(()->{
                links.clear();
                posses.each((pos) -> {
                    Building build = Vars.world.build(pos);
                    if (build != null && !linked(build)) links.add(build);
                });
            });
        }
        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            handleString(read.str());
        }

        protected boolean linked(Building other) {
            return this.links.contains(other);
        }
    }
}
