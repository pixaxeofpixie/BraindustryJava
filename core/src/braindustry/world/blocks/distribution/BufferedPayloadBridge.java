package braindustry.world.blocks.distribution;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.world.PayloadBuffer;
import mindustry.core.Renderer;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.payloads.Payload;

import static mindustry.Vars.*;

public class BufferedPayloadBridge extends PayloadBridge {
    static Payload toTransport = null;
    public final int timerAccept = timers++;
    public float moveTime = 40f, moveForce = 201f;
    public int bufferCapacity = 50;

    public BufferedPayloadBridge(String name) {
        super(name);
        hasPower = false;
        canOverdrive = true;
    }

    @Override
    public void init() {
        super.init();
        bufferCapacity = (int) ((float) range / payloadLimit);
        payloadCapacity = 1;
    }

    @Override
    public void setBars() {
        super.setBars();

        bars.add("count", (BufferedPayloadBridgeBuild build) -> new Bar(() -> "stat.count", () -> Pal.bar, () -> {
            return (float) build.buffer.size() / (float) bufferCapacity;
        }).blink(Color.white));
    }

    public class BufferedPayloadBridgeBuild extends PayloadBridgeBuild {
        public Interp interp = Interp.pow5;
        PayloadBuffer buffer = new PayloadBuffer(bufferCapacity);

        @Override
        protected int realPayloadCapacity() {
            Building build = world.build(link);
            return build == null ? 1 : (int) ((dst(build) / (float) tilesize) / (float) payloadLimit);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            Draw.z(Layer.power);

            Tile other = world.tile(link);
            if (!linkValid(tile, other)) return;

            int i = tile.absoluteRelativeTo(other.x, other.y);

            float ex = other.worldx() - x - Geometry.d4(i).x * tilesize / 2f,
                    ey = other.worldy() - y - Geometry.d4(i).y * tilesize / 2f;

            float uptime = state.isEditor() ? 1f : this.uptime;

            ex *= uptime;
            ey *= uptime;

            if (Mathf.zero(Renderer.bridgeOpacity)) return;
            Draw.alpha(Renderer.bridgeOpacity);

            Lines.stroke(8f);
            Lines.line(bridgeRegion,
                    x + Geometry.d4(i).x * tilesize / 2f,
                    y + Geometry.d4(i).y * tilesize / 2f,
                    x + ex,
                    y + ey, false);

            Draw.rect(endRegion, x, y, i * 90 + 90);
            Draw.rect(endRegion,
                    x + ex + Geometry.d4(i).x * tilesize / 2f,
                    y + ey + Geometry.d4(i).y * tilesize / 2f, i * 90 + 270);

            int dist = Math.max(Math.abs(other.x - tile.x), Math.abs(other.y - tile.y));

            int arrows = (dist) * tilesize / 6 - 1;

            Draw.color();

            for (int a = 0; a < arrows; a++) {
                Draw.alpha(Mathf.absin(a / (float) arrows - time / 100f, 0.1f, 1f) * uptime * Renderer.bridgeOpacity);
                Draw.rect(arrowRegion,
                        x + Geometry.d4(i).x * (tilesize / 2f + a * 6f + 2) * uptime,
                        y + Geometry.d4(i).y * (tilesize / 2f + a * 6f + 2) * uptime,
                        i * 90f);
            }
            Draw.reset();
            buffer.each((payload, time) -> {
                payload.draw();
            });

        }

        protected float realMoveSpeed() {
            return moveTime / timeScale;
        }

        @Override
        public void updateTile() {
            super.updateTile();
        }

        @Override
        public void updateTransport(Building other) {
            if (buffer.accepts() && payloads.total() > 0) {
                buffer.accept(payloads.take());
            }
            buffer.update();
            toTransport = null;
            Vec2 from = new Vec2(x, y), to = new Vec2(other.x, other.y), d = from.cpy().sub(to);
            buffer.each((payload, time) -> {
                float moveSpeed = realMoveSpeed(),payloadCapacity = realPayloadCapacity();
                int fullc = (int) (time / moveSpeed);
                float residue = time % moveSpeed;
                float scalar = (float) interp.apply(residue / moveSpeed) / payloadCapacity+fullc/payloadCapacity;
                Vec2 pos = from.cpy().sub(d.cpy().scl(Math.min(1f, scalar)));
                payload.set(pos.x, pos.y, from.angle(to));
                if (time / (moveSpeed * payloadCapacity) >= 1f) toTransport = payload;
            });
            if (toTransport != null && other.acceptPayload(this, toTransport)) {
                cycleSpeed = Mathf.lerpDelta(cycleSpeed, 4f, 0.05f);
                other.handlePayload(this, toTransport);
                buffer.remove(toTransport);
            } else {
                if (other == null && toTransport != null) {

                }
                cycleSpeed = Mathf.lerpDelta(cycleSpeed, 0f, 0.008f);
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            buffer.write(write);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            buffer.read(read);
        }
    }
}
