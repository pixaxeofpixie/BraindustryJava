package braindustry.world.blocks.distribution;

import arc.Core;
import arc.func.Boolf;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.Placement;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustryAddition.graphics.ModLines;
import mindustryAddition.world.blocks.BuildingLabel;
import mindustryAddition.world.blocks.BuildingTaskQueue;
import braindustry.world.meta.AStat;
import braindustry.world.meta.AStats;

public class CrossItemBridge extends ItemBridge {
    public Prov<Seq<Block>> connectBlocksGetter = () -> new Seq<>();
    AStats aStats = new AStats();
    Seq<Block> connectibleBlocks = new Seq<>();
    public Boolf<Building> connectFilter = (building) -> connectibleBlocks.contains(building.block());
    byte maxConnections = 10;

    public CrossItemBridge(String name) {
        super(name);
        super.stats = aStats.copy(stats);
    }

    CrossItemBridge cast(Block b) {
        return (CrossItemBridge) b;
    }

    CrossItemBridgeBuild cast(Building b) {
        return (CrossItemBridgeBuild) b;
    }

    @Override
    public void init() {
        super.init();
        Seq<Block> connectibleBlocks = connectBlocksGetter.get();
        if (connectibleBlocks == null) connectibleBlocks = new Seq<>();
        connectibleBlocks.add(this);
        this.connectibleBlocks = connectibleBlocks;
    }

    @Override
    public void checkStats() {
        if (!aStats.intialized) {
            this.setStats();
            this.aStats.intialized = true;
        }
    }

    @Override
    public void setStats() {
        super.setStats();
        aStats.add(Stat.range, this.range, StatUnit.blocks);
        aStats.add(AStat.maxConnections, (float) this.maxConnections, StatUnit.none);
    }

    @Override
    public void setBars() {
        super.setBars();
//        Vars.ui.content
        this.bars.add("connections", (entity) -> {
            return new Bar(() -> {
                //in bundle: Connections: {0}/{1}
                return Core.bundle.format("bar.cross-item-bridge-lines", cast(entity).realConnections(), this.maxConnections);
            }, () -> {
                return Pal.items;
            }, () -> {
                return (float) cast(entity).realConnections() / (float) this.maxConnections;
            });
        });
    }

    @Override
    public void drawBridge(BuildPlan req, float ox, float oy, float flip) {
        Lines.stroke(8.0F);
        Tmp.v1.set(ox, oy).sub(req.drawx(), req.drawy()).setLength(4.0F);
//        Angles.angle(req.drawx(),req.drawy(),ox,oy)
        Lines.line(bridgeRegion, req.drawx() + Tmp.v1.x, req.drawy() + Tmp.v1.y, ox - Tmp.v1.x, oy - Tmp.v1.y, false);
        Draw.rect(arrowRegion, (req.drawx() + ox) / 2.0F, (req.drawy() + oy) / 2.0F, Angles.angle(req.drawx(), req.drawy(), ox, oy) + flip);
    }

    public Tile findLink(int x, int y) {
        return findLink(x,y,true);
    }public Tile findLink(int x, int y,boolean checkBlock) {
        Tile tile = Vars.world.tile(x, y);
        if (checkBlock) {
            if (tile != null && this.lastBuild != null && this.linkValid(tile, this.lastBuild.tile) && this.lastBuild.tile != tile)
                return this.lastBuild.tile;
        } else {
            if (tile != null && this.lastBuild != null && this.linkValid(tile, this.lastBuild.tile,false,true) && this.lastBuild.tile != tile)
                return this.lastBuild.tile;
        }
        return null;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Tile link = this.findLink(x, y,false);
        Lines.stroke(2.0F, Pal.placing);
        Lines.dashCircle(x * 8f, y * 8f, range * 8f);

        Draw.reset();
        Draw.color(Pal.placing);
        Lines.stroke(1.0F);
        if (link != null && (link.x != x || link.y != y)) {
            final float angle = Angles.angle(link.x, link.y, x, y) + 90;
            float w = 8f;
            float h = Mathf.dst(link.x, link.y, x, y) * 8f - 8f;
            Lines.stroke(1.0F);
            float rectX = (float) (x + link.x) / 2.0F * 8.0F - w / 2.0F,
                    rectY = (float) (y + link.y) / 2.0F * 8.0F - h / 2.0F;
//            Lines.poly(ModLines.rotRect(rectX, rectY, w, h, angle).toArray(Vec2.class), 0, 0, 1f);
            ModLines.rect(rectX,rectY,w,h,angle);
            Tmp.v1.set(x, y).sub(link.x, link.y).setLength(4.0F).scl(-1.0F);
            Vec2 arrowOffset = new Vec2(Tmp.v1).scl(1f).setLength(1f);
            Draw.rect("bridge-arrow", link.x * 8f - arrowOffset.x * 8f, link.y * 8f - arrowOffset.y * 8f, angle - 90f);
        }

        Draw.reset();
    }

    @Override
    public TextureRegion[] getGeneratedIcons() {
        return super.getGeneratedIcons();
    }

    @Override
    public boolean linkValid(Tile tile, Tile other) {
        return linkValid(tile, other, true);
    }

    @Override
    public boolean linkValid(Tile tile, Tile other, boolean checkDouble) {
    return linkValid(tile,other,checkDouble,false);
    }
    public boolean linkValid(Tile tile, Tile other, boolean checkDouble,boolean old) {
        old:
        {
            if (!old) break old;
            if (other != null && tile != null && this.positionsValid(tile.x, tile.y, other.x, other.y)) {
                return (other.block() == tile.block() && tile.block() == this || !(tile.block() instanceof ItemBridge) && other.block() == this) && (other.team() == tile.team() || tile.block() != this) && (!checkDouble || ((ItemBridgeBuild) other.build).link != tile.pos());
            } else {
                return false;
            }
        }
        check:
        {
            if (!(other != null && tile != null) || other.build == null || tile.build == null) break check;
            other = other.build.tile;
            tile = tile.build.tile;
            int offset = other.block().isMultiblock() ? Mathf.floor(other.block().size / 2f) : 0;
            boolean b2 = tile.pos() != other.pos();
            if (tile.block() ==this) {
                Vec2 offVec = Tmp.v1.trns(tile.angleTo(other) + 90f, offset, offset);
                if (!positionsValid(tile.x, tile.y, Mathf.ceil(other.x + offVec.x), Mathf.ceil(other.y + offVec.y)))
                    break check;
                CrossItemBridge block = (CrossItemBridge) tile.block();
                boolean connected = false;
                if (other.build instanceof ItemBridgeBuild) {
                    connected = other.build.<ItemBridgeBuild>as().link == tile.pos();
                }
                return ((block.connectFilter.get(other.build)) || !(tile.block() instanceof ItemBridge) && other.block() == this) &&
                        b2 &&
                        (other.team() == tile.team() || other.block() != this) &&

                        (!checkDouble || !connected);
            } else {
                if (!positionsValid(tile.x, tile.y, other.x, other.y)) break check;
                boolean b3 = other.team() == tile.team() || tile.block() != this;
                if (other.block() == this) {
                    CrossItemBridge block = (CrossItemBridge) other.block();
                    boolean b1 = true;
                    boolean b4 = !checkDouble || !(other.build instanceof ItemBridgeBuild && ((ItemBridgeBuild) other.build).link == tile.pos());
                    return b1 &&
                            b2 &&
                            b3 &&
                            b4;
                } else {
                    return (other.block() == tile.block() && tile.block() == this || !(tile.block() instanceof ItemBridge) && other.block() == this)
                            && b3 &&
                            (!checkDouble || ((ItemBridgeBuild) other.build).link != tile.pos());
                }
            }
        }
        return false;
    }

    @Override
    public boolean positionsValid(int x1, int y1, int x2, int y2) {
        return Mathf.within(x1, y1, x2, y2, range + 0.5f);
    }
    public boolean positionsValid(Position pos, Position other) {
        return positionsValid((int)pos.getX(),(int)pos.getY(),(int) other.getX(),(int)other.getY());
    }
    public boolean positionsValid(Point2 pos, Point2 other) {
        return positionsValid(pos.x,pos.y, other.x,other.y);
    }
    public void changePlacementPath(Seq<Point2> points, int rotation) {
        Placement.calculateNodes(points, this, rotation, (point, other) -> {
            return positionsValid(point,other);
        });
    }
    public class CrossItemBridgeBuild extends ItemBridge.ItemBridgeBuild implements BuildingLabel, BuildingTaskQueue {
        public void drawBase() {
            Draw.rect(this.block.region, this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
            this.drawTeamTop();
        }

        public boolean acceptIncoming(int pos) {
            if ((incoming.size + (link == -1 ? 0 : 1) < maxConnections) && !incoming.contains(pos)) incoming.add(pos);
            return incoming.contains(pos);
        }

        @Override
        public void checkIncoming() {
            IntSet.IntSetIterator it = this.incoming.iterator();

            while (true) {
                Tile other;
                do {
                    if (!it.hasNext) {
                        return;
                    }

                    int i = it.next();
                    other = Vars.world.tile(i);
                } while (linkValid(this.tile, other, false) && (other.build instanceof ItemBridgeBuild && ((ItemBridgeBuild) other.build).link == this.tile.pos()));

                it.remove();
            }
        }

        public int realConnections() {
            return incoming.size + (link == -1 ? 0 : 1);
        }

        public boolean canLinked() {
            return (realConnections() < maxConnections);
        }

        public boolean canReLink() {
            return (realConnections() <= maxConnections && link != -1);
        }

        public boolean onConfigureTileTapped(Building other) {
            if (other instanceof ItemBridge.ItemBridgeBuild && ((ItemBridge.ItemBridgeBuild) other).link == this.pos()) {
                incoming.remove(other.pos());
                other.<ItemBridgeBuild>as().incoming.add(this.pos());
                this.configure(other.pos());
                other.configure(-1);
            } else if (linkValid(this.tile, other.tile)) {
                if (this.link == other.pos()) {
                    if (other instanceof ItemBridgeBuild) other.<ItemBridgeBuild>as().incoming.remove(this.pos());
                    incoming.add(other.pos());
                    this.configure(-1);
                } else if (!(other instanceof CrossItemBridgeBuild && !cast(other).canLinked()) && (canLinked() || canReLink())) {
                    if (other instanceof ItemBridgeBuild) other.<ItemBridgeBuild>as().incoming.add(this.pos());
                    incoming.remove(other.pos());
                    this.configure(other.pos());
                }

                return false;
            }

            return true;
        }

        @Override
        public void updateTile() {
            incoming.shrink(maxConnections - (link == -1 ? 0 : 1));
            runUpdateTaskQueue();
            Building linkBuilding = Vars.world.build(link);
            if (linkBuilding != null) {
                configure(linkBuilding.pos());
            } else {
//                configure(-1);
            }
            this.time += this.cycleSpeed * this.delta();
            this.time2 += (this.cycleSpeed - 1.0F) * this.delta();
            this.checkIncoming();
            Tile other = Vars.world.tile(this.link);
            if (!linkValid(this.tile, other)) {
                this.dump();
                this.uptime = 0.0F;
            } else {
                if (other.build instanceof ItemBridgeBuild)
                    if (other.build instanceof CrossItemBridgeBuild && !cast(other.build).acceptIncoming(this.tile.pos())) {
                        configure(-1);
                        return;
                    }
                if (this.consValid() && Mathf.zero(1.0F - this.efficiency())) {
                    this.uptime = Mathf.lerpDelta(this.uptime, 1.0F, 0.04F);
                } else {
                    this.uptime = Mathf.lerpDelta(this.uptime, 0.0F, 0.02F);
                }


                this.updateTransport(other.build);
            }

        }

        public void drawSelect() {
            if (linkValid(tile, Vars.world.tile(link))) {
                drawInput(Vars.world.tile(link));
            }

            incoming.each((pos) -> {
                drawInput(Vars.world.tile(pos));
            });
            Draw.reset();
        }

        protected void drawInput(Tile other) {
            if (linkValid(this.tile, other, false)) {
                boolean linked = other.pos() == this.link;
                final float angle = tile.angleTo(other);
                Tmp.v2.trns(angle, 2.0F);
                float tx = tile.drawx();
                float ty = tile.drawy();
                float ox = other.drawx();
                float oy = other.drawy();
                float alpha = Math.abs((float) (linked ? 100 : 0) - Time.time * 2.0F % 100.0F) / 100.0F;
                float x = Mathf.lerp(ox, tx, alpha);
                float y = Mathf.lerp(oy, ty, alpha);
                Tile otherLink = linked ? other : tile;
                float rel = (linked ? tile : other).angleTo(otherLink);
                Draw.color(Pal.gray);
                Lines.stroke(2.5F);
                Lines.square(ox, oy, 2.0F, 45.0F);
                Lines.stroke(2.5F);
                Lines.line(tx + Tmp.v2.x, ty + Tmp.v2.y, ox - Tmp.v2.x, oy - Tmp.v2.y);
                Draw.color(linked ? Pal.place : Pal.accent);
                Lines.stroke(1.0F);
                Lines.line(tx + Tmp.v2.x, ty + Tmp.v2.y, ox - Tmp.v2.x, oy - Tmp.v2.y);
                Lines.square(ox, oy, 2.0F, 45.0F);
                Draw.mixcol(Draw.getColor(), 1.0F);
                Draw.color();
                Draw.rect(arrowRegion, x, y, rel);
                Draw.mixcol();
            }
        }

        public void drawConfigure() {
            Drawf.select(this.x, this.y, (float) (this.tile.block().size * 8) / 2.0F + 2.0F, Pal.accent);
            Draw.color(Pal.accent);
            Lines.dashCircle(x, y, (range) * 8f);
            Draw.color();
            if (!canReLink() && !canLinked()) return;
            OrderedMap<Building, Boolean> orderedMap = new OrderedMap<>();
            for (int x = -range; x <= range; ++x) {
                for (int y = -range; y <= range; ++y) {
                    Tile other = this.tile.nearby(x, y);
                    if (linkValid(this.tile, other) && !(tile == other)) {
                        if (!orderedMap.containsKey(other.build)) orderedMap.put(other.build, false);
                    }
                }
            }
            Building linkBuilding = Vars.world.build(link);
            if (linkBuilding != null) {
                configure(linkBuilding.pos());
                orderedMap.remove(linkBuilding);
                orderedMap.put(linkBuilding, true);
            } else {
                configure(-1);
            }
            if (orderedMap.containsKey(this)) orderedMap.remove(this);
            orderedMap.each((other, linked) -> {
//                if (!linkValid(tile, other.tile)) return;
                Drawf.select(other.x, other.y, (float) (other.block().size * 8) / 2.0F + 2.0F + (linked ? 0.0F : Mathf.absin(Time.time, 4.0F, 1.0F)), linked ? Pal.place : Pal.breakInvalid);
            });

        }

        public void draw() {
            drawBase();
//            super.draw();
            Draw.z(70.0F);
            Tile other = Vars.world.tile(this.link);
            Building build = Vars.world.build(link);
            if (build == this) build = null;
            if (build != null) other = build.tile;
            if (linkValid(this.tile, other) && build != null) {
                float opacity = (float) Core.settings.getInt("bridgeopacity") / 100.0F;
                if (!Mathf.zero(opacity)) {
                    final float angle = Angles.angle(x, y, build.x, build.y);
                    float vx = Mathf.cosDeg(angle);
                    float vy = Mathf.sinDeg(angle);
                    float len1 = (size * 8f) / 2.0F - 1.5F;
                    float len2 = (build.block.size * 8f) / 2.0F - 1.5F;
                    float x = this.x + vx * len1, y = this.y + vy * len1 ,
                            x2 = build.x - vx * len2, y2 = build.y - vy * len2 ;

                    Tmp.v1.set(x, y).sub(x2, y2).setLength(4.0F).scl(-1.0F);
                    Tmp.v2.set(x, y).sub(x2, y2).setLength(1.0F).scl(1.0F);
                    Tmp.v2.setZero().trns((angle + 135f), 0.5f, 0.5f);
//                    Tmp.v2.set(Tmp.v1.setZero());
                    float
                            d360x = (vx * 8f) / 2.0F,
                            d360y = (vy * 8f) / 2.0F,
                            uptime = Vars.state.isEditor() ? 1.0F : this.uptime;
                    if (!isMultiblock()) {
                        x -=d360x;
                        y -=d360y;
                    }
                    float      ex = x + (x2 - x - d360x) + d360x,
                            ey = y + (y2 - y - d360y) + d360y,
                            bx = x2 - Tmp.v1.x,
                            by = y2 - Tmp.v1.y;
                    by = y2;
                    bx = x2;
                    ex = x2;
                    ey = y2;
                    Draw.color(Color.white);
                    Draw.color(Color.white, Color.black, Mathf.absin(Time.time, 6.0F, 0.07F));
                    Draw.alpha(Math.max(this.uptime, 0.25F) * opacity);
                    Draw.rect(endRegion, x, y, angle + 90);
//                    d4x=d4y=0;
                    Draw.rect(endRegion, ex+d360x, ey+d360y, angle + 270f);
                    Lines.stroke(8.0F);

                    Lines.line(bridgeRegion, x+d360x , y+d360y, bx, by, false);
                    int dist = Math.max((int) Math.abs(x2 - x) / Vars.tilesize, (int) Math.abs(y2 - y) / Vars.tilesize);
                    float time = this.time2 / 1.7F;
                    int arrows = dist * 8 / 4 - 2;
//                    arrows -= arrows == 1 || x == other.worldx() || y == other.worldy() ? 1 : 0;
//                    arrows=dist;
                    Draw.color();
                    Vec2 arrowOffset = new Vec2(Tmp.v1).scl(1f).setLength(1f);
                    arrowOffset.trns(angle - 45f, 1f, 1f);
                    for (float a = 0; a < arrows; ++a) {
                        Draw.alpha(Mathf.absin(a / arrows - time / 100.0F, 0.1F, 1.0F) * uptime * opacity);
                        final float progress = uptime * ((1f / arrows) * (a) + 1f / arrows / 2f);
                        float arrowX = x + Mathf.lerp(arrowOffset.x * 4f, ex - x - arrowOffset.x * 4f + time % 4f, progress);
                        float arrowY = y + Mathf.lerp(arrowOffset.y * 4f, ey - y - arrowOffset.y * 4f + time % 4f, progress);
                        arrowX = x + (float) arrowOffset.x * (4.0F + (float) a * 4.0F + time % 4.0F);
                        arrowY = y + (float) arrowOffset.y * (4.0F + (float) a * 4.0F + time % 4.0F);

                        Draw.rect(arrowRegion, arrowX, arrowY,
                                angle);
                    }
                    Draw.reset();
                }
            }
        }

    }
}
