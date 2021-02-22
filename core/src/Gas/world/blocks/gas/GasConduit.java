package Gas.world.blocks.gas;

import Gas.gen.GasBuilding;
import Gas.type.Gas;
import Gas.world.GasBlock;
import arc.Core;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.ContentRegions;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.Autotiler;

public class GasConduit extends GasBlock implements Autotiler {
    public final int timerFlow;
    public Color botColor;
    public boolean drawBottom = false;
    public TextureRegion[] topRegions;
    public TextureRegion[] botRegions;
    private boolean isLoadRegions=false;
    public boolean leaks;

    public GasConduit(String name) {
        super(name);
        this.timerFlow = this.timers++;
        this.botColor = Color.valueOf("565656");
        this.hasGas = true;
        this.gasCapacity = 100f;
        this.leaks = true;
        this.outputsGas = true;
        this.rotate = true;
        this.update = true;
        this.solid = false;
        this.floating = true;
        this.conveyorPlacement = true;
//        this.noUpdateDisabled = true;
    }
    public void loadRegions(){
        if(!isLoadRegions) {
            if (topRegions == null) topRegions = new TextureRegion[5];
            if (botRegions == null) botRegions = new TextureRegion[5];
            for (int i = 0; i < topRegions.length; i++) {
                topRegions[i] = Core.atlas.find(this.name + "-top-" + i);
            }
            for (int i = 0; i < botRegions.length; i++) {
                botRegions[i] = Core.atlas.find(this.name + "-bottom-" + i);
            }
            if (topRegions.length>0)this.region=topRegions[0];
            isLoadRegions=true;
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void load() {
        loadRegions();
//        this.region = Core.atlas.find(this.name);
        ContentRegions.loadRegions(this);
        this.teamRegions = new TextureRegion[Team.all.length];
        Team[] var1 = Team.all;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Team team = var1[var3];
            this.teamRegions[team.id] = this.teamRegion.found() ? Core.atlas.find(this.name + "-team-" + team.name, this.teamRegion) : this.teamRegion;
        }
    }

    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        int[] bits = this.getTiling(req, list);
        if (bits != null) {
            Draw.scl((float) bits[1], (float) bits[2]);
            if (drawBottom) {
                Draw.color(this.botColor);
                Draw.alpha(0.5F);
                Draw.rect(this.botRegions[bits[0]], req.drawx(), req.drawy(), (float) (req.rotation * 90));
            }
            Draw.color();
            Draw.rect(this.topRegions[bits[0]], req.drawx(), req.drawy(), (float) (req.rotation * 90));
            Draw.scl();
        }
    }

    public Block getReplacement(BuildPlan req, Seq<BuildPlan> requests) {
        Boolf<Point2> cont = (p) -> {
            return requests.contains((o) -> {
                return o.x == req.x + p.x && o.y == req.y + p.y && o.rotation == req.rotation &&
                        (req.block instanceof GasConduit);
            });
        };
        return this;
//        return (Block)(cont.get(Geometry.d4(req.rotation)) && cont.get(Geometry.d4(req.rotation - 2)) && req.tile() != null &&
//                req.tile().block() instanceof Conduit && Mathf.mod(req.build().rotation - req.rotation, 2) == 1 ? Blocks.liquidJunction : this);
    }
    public TextureRegion[] icons() {
//        new Color((Color)null);
        if (!this.drawBottom) return super.icons();
        return new TextureRegion[]{Core.atlas.find(name+"-bottom"), this.topRegions[0]};
    }

    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block o) {
        if (!(o instanceof GasBlock)) return false;
        GasBlock otherblock = (GasBlock) o;
        return otherblock.hasGas &&
                (otherblock.outputsGas || this.lookingAt(tile, rotation, otherx, othery, otherblock)) &&
                this.lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
    }

    public class GasConduitBuild extends GasBuilding {
        public float smoothGas;
        public int blendbits;
        public int xscl;
        public int yscl;
        public int blending;

        public void draw() {
            float rotation = this.rotdeg();
            int r = this.rotation;
            Draw.z(29.5F);

            for (int i = 0; i < 4; ++i) {
                if ((this.blending & 1 << i) != 0) {
                    int dir = r - i;
                    float rot = i == 0 ? rotation : (float) (dir * 90);
                    this.drawAt(this.x + (float) (Geometry.d4x(dir) * 8) * 0.75F, this.y + (float) (Geometry.d4y(dir) * 8) * 0.75F, 0, rot, i != 0 ? SliceMode.bottom : SliceMode.top);
                }
            }

            Draw.z(30.0F);
            Draw.scl((float) this.xscl, (float) this.yscl);
            this.drawAt(this.x, this.y, this.blendbits, rotation, SliceMode.none);
            Draw.reset();
        }

        protected void drawAt(float x, float y, int bits, float rotation, SliceMode slice) {
            if (GasConduit.this.drawBottom) {
                Draw.color(GasConduit.this.botColor);
                Draw.rect(GasConduit.this.sliced(GasConduit.this.botRegions[bits], slice), x, y, rotation);
                Drawf.liquid(GasConduit.this.sliced(GasConduit.this.botRegions[bits], slice), x, y, this.smoothGas, this.gasses.current().color, rotation);
            }
            Draw.rect(GasConduit.this.sliced(GasConduit.this.topRegions[bits], slice), x, y, rotation);
        }

        public void onProximityUpdate() {
            super.onProximityUpdate();
            int[] bits = GasConduit.this.buildBlending(this.tile, this.rotation, (BuildPlan[]) null, true);
            this.blendbits = bits[0];
            this.xscl = bits[1];
            this.yscl = bits[2];
            this.blending = bits[4];
        }

        public boolean acceptGas(Building source, Gas gas) {
            this.noSleep();
            return (this.gasses.current() == gas || this.gasses.currentAmount() < 0.2F) && (source.relativeTo(this.tile.x, this.tile.y) + 2) % 4 != this.rotation;
        }

        public void updateTile() {
            this.smoothGas = Mathf.lerpDelta(this.smoothGas, this.gasses.currentAmount() / GasConduit.this.gasCapacity, 0.05F);
            if (this.gasses.total() > 0.001F && this.timer(GasConduit.this.timerFlow, 1.0F)) {
                this.moveGasForward(GasConduit.this.leaks, this.gasses.current());
                this.noSleep();
            } else {
                this.sleep();
            }

        }
    }
}
