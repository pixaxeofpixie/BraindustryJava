package mindustryAddition.world.blocks.distribution;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.ItemBuffer;
import mindustry.world.Tile;

public class CrossBufferedItemBridge extends CrossItemBridge {
    public final int timerAccept;
    public float speed;
    public int bufferCapacity;
    public CrossBufferedItemBridge(String name) {
        super(name);
        this.hasItems = true;
        this.timerAccept = this.timers++;
        this.speed = 40.0F;
        this.bufferCapacity = 50;
        this.hasPower = false;
        this.hasItems = true;
        this.canOverdrive = true;
    }
    public class CrossBufferedItemBridgeBuild extends CrossItemBridgeBuild{
        ItemBuffer buffer;
        public CrossBufferedItemBridgeBuild() {
            super();
            this.buffer = new ItemBuffer(bufferCapacity);
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
                    float
                            ex = Mathf.lerp(x+d360x, x2, uptime),
                            ey = Mathf.lerp(y+d360y, y2, uptime),
                            bx = Mathf.lerp(x+d360x, x2 , uptime),
                            by = Mathf.lerp(y+d360y , y2 , uptime);
                    Draw.color(Color.white);
                    Draw.alpha(opacity);
                    Draw.rect(endRegion, x, y, angle + 90);
                    Draw.rect(endRegion, ex+d360x, ey+d360y, angle + 270f);
                    Lines.stroke(8.0F);

                    Lines.line(bridgeRegion, x+d360x , y+d360y, bx, by, false);
                    int dist = Math.max((int) Math.abs(x2 - x) / Vars.tilesize, (int) Math.abs(y2 - y) / Vars.tilesize);
                    float arrows = (int) (dist * 8 / 6) - 1;
                    Draw.color();
                    Vec2 arrowOffset = new Vec2().trns(angle - 45f, 1f, 1f);
                    for (float a = 0; a < arrows; ++a) {
                        Draw.alpha(Mathf.absin(a / arrows - this.time / 100.0F, 0.1F, 1.0F) * uptime * opacity);
                        final float progress = uptime * ((1f / arrows) * (a) + 1f / arrows / 2f);
                        float arrowX = x + Mathf.lerp(arrowOffset.x * 4f, ex - x - arrowOffset.x * 4f, progress);
                        float arrowY = y + Mathf.lerp(arrowOffset.y * 4f, ey - y - arrowOffset.y * 4f, progress);
                        Draw.rect(arrowRegion, arrowX, arrowY,
//                                x + arrowOffset.x * (4.0F + a * 6.0F + 2.0F) * uptime,
//                                y + arrowOffset.y * (4.0F + a * 6.0F + 2.0F) * uptime,
                                angle);
                    }

                    Draw.reset();
                }
            }
        }
        public void updateTransport(Building other) {
            if (this.buffer.accepts() && this.items.total() > 0) {
                this.buffer.accept(this.items.take());
            }

            Item item = this.buffer.poll(speed / this.timeScale);
            if (this.timer(timerAccept, 4.0F / this.timeScale) && item != null && other.acceptItem(this, item)) {
                this.cycleSpeed = Mathf.lerpDelta(this.cycleSpeed, 4.0F, 0.05F);
                other.handleItem(this, item);
                this.buffer.remove();
            } else {
                this.cycleSpeed = Mathf.lerpDelta(this.cycleSpeed, 0.0F, 0.008F);
            }

        }

        public void write(Writes write) {
            super.write(write);
            this.buffer.write(write);
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            this.buffer.read(read);
        }
    }
}
