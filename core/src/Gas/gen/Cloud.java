package Gas.gen;

import Gas.entities.Clouds;
import Gas.type.Gas;
import ModVars.modVars;
import arc.func.Cons;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.gl.Shader;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.math.geom.Rect;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import braindustry.content.ModFx;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.ctype.ContentType;
import mindustry.entities.*;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.io.TypeIO;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.environment.Floor;
import mindustryAddition.versions.ModEntityc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Cloud implements Pool.Poolable,  Drawc, Posc, ModEntityc {
    public static final int maxGeneration = 2;
    public static final Color tmp = new Color();
    public static final Rect rect = new Rect();
    public static final Rect rect2 = new Rect();
    public static int seeds;
    public transient float accepting;
    public transient float updateTime;
    public transient float lastRipple;
    public float amount;
    public int generation;
    public Tile tile;
    public Gas gas;
    public transient boolean added;
    public transient int id = EntityGroup.nextId();
    public float x;
    public float y;
    public static int classId=Byte.MAX_VALUE-1;

    public Cloud() {
    }

    public boolean serialize() {
        return true;
    }

    public String toString() {
        return "Cloud#" + this.id;
    }

    public void trns(float x, float y) {
        this.set(this.x + x, this.y + y);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void draw() {
        Draw.z(110.0F);
        seeds = this.id();
        boolean onLiquid = this.tile.floor().isLiquid;
        float f = Mathf.clamp(this.amount / 46.666668F),a;
        float smag = onLiquid ? 0.8F : 0.0F;
        float sscl = 25.0F;
        Draw.blend(Blending.additive);
        Shader last=Draw.getShader();
//        last
//        Draw.shader(Shaders.PlanetShader);
        Color col=tmp.set(this.gas.color).shiftValue(-0.05F);
        a=(Mathf.clamp(this.amount /250f)*gas.transparency)*0.9f;
        a=Math.max(0.1f,a);
        Draw.color(col);
//        Draw.color(col,col.cpy().a(0),a);
        Draw.alpha(a);
        Fill.circle(this.x + Mathf.sin(Time.time + (float)(seeds * 532), sscl, smag), this.y + Mathf.sin(Time.time + (float)(seeds * 53), sscl, smag), f * 8.0F);
        Angles.randLenVectors((long)this.id(), 3, f * 6.0F, (ex, ey) -> {
            Fill.circle(this.x + ex + Mathf.sin(Time.time + (float)(seeds * 532), sscl, smag), this.y + ey + Mathf.sin(Time.time + (float)(seeds * 53), sscl, smag), f * 5.0F);
            ++seeds;
        });
        Draw.blend();
        Draw.color();
//        Draw.reset();
        if (this.gas.lightColor.a > 0.001F && f > 0.0F) {
            Color color = this.gas.lightColor;
            float opacity = color.a * f;
            Drawf.light(Team.derelict, this.tile.drawx(), this.tile.drawy(), 30.0F * f, color, opacity * 0.8F);
        }

    }

    public float getFlammability() {
        return this.gas.flammability * this.amount;
    }

    public void update() {
//        print("up: amount: @ name: @",amount,toString());
        float addSpeed = this.accepting*2f;
        this.amount -= Time.delta * (1.0F - this.gas.viscosity) / (5.0F + addSpeed);
        this.amount += this.accepting;
        this.accepting = 0.0F;
        if (this.amount >= 46.666668F && this.generation < 2) {
            float deposited = Math.min((this.amount - 46.666668F) / 4.0F, 0.3F) * Time.delta;
            Point2[] var3 = Geometry.d4;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Point2 point = var3[var5];
                Tile other = Vars.world.tile(this.tile.x + point.x, this.tile.y + point.y);
                if (other != null) {
                    Clouds.deposit(other, this.tile, this.gas, deposited, this.generation + 1);
                    this.amount -= deposited / 2.0F;
                }
            }
        }

        this.amount = Mathf.clamp(this.amount, 0.0F, 70.0F);
        if (this.amount <= 0.0F) {
            this.remove();
        }

        if (this.amount >= 35.0F && this.updateTime <= 0.0F) {
            Units.nearby(rect.setSize(Mathf.clamp(this.amount / 46.666668F) * 10.0F).setCenter(this.x, this.y), (unit) -> {
                if (unit.isGrounded() && !unit.hovering) {
                    unit.hitbox(rect2);
                    if (rect.overlaps(rect2)) {
                        unit.apply(this.gas.effect, 120.0F);
                        if ((double)unit.vel.len() > 0.1D) {
                            Fx.ripple.at(unit.x, unit.y, unit.type.rippleScale, this.gas.color);
                        }
                    }
                }

            });
            if (this.gas.temperature > 0.7F && this.tile.build != null && Mathf.chance(0.5D)) {
                Fires.create(this.tile);
            }

            this.updateTime = 40.0F;

        }
        Puddle puddle=Puddles.get(this.tile);
        if (puddle!=null){
            if (puddle.liquid.temperature>=0.7f && this.gas.explosiveness>=0.9f){
                float flammability,explosiveness,radius;
                flammability=this.getFlammability()+puddle.getFlammability();
                explosiveness=this.gas.explosiveness+puddle.liquid.explosiveness;
                radius=(this.amount+puddle.amount)*1.2f;
                Vars.world.tiles.eachTile((tile)->{
                    if (tile==null || tile.build==null)return;
                    Building build=tile.build;
                    AtomicBoolean hasWall=new AtomicBoolean(false);
                    AtomicInteger countBlocks=new AtomicInteger(0);
                    Vars.world.raycastEach(this.tileX(),this.tileY(),build.tileX(),build.tileY(),(x,y)->{
                        Building b=Vars.world.build(x*8,y*8);
                        if (b==null)return true;
                        countBlocks.addAndGet(1);
                        if (b.block instanceof Wall){
                            hasWall.set(true);
                            ModFx.debugSelect.at(build.x,build.y,build.block.size,Color.red,Color.red);
                            return false;
                        }

                        return true;
                    });
                    float distance=tile.build.dst(this);
                    float blockIndex=1;
                    float distanceIndex=1f-((distance+1f)/radius);
                    if (distance<=radius && !hasWall.get())tile.build.damage(distanceIndex*(40/blockIndex));
                });
                Groups.build.each((b)->{
                });
                Damage.dynamicExplosion(this.x, this.y, flammability, explosiveness, 0, Mathf.clamp((radius),0,30), Vars.state.rules.damageExplosions,true,Team.derelict);
                puddle.amount=Math.max(0,puddle.amount-explosiveness-flammability-radius/8f);
                this.amount=Math.max(0,this.amount-explosiveness-flammability-radius/8f);
            }
        }
        this.updateTime -= Time.delta;
    }

    public void remove() {
        if (this.added) {
            Groups.all.remove(this);
            Groups.draw.remove(this);
//            Groups.puddle.remove(this);
            Clouds.remove(this.tile);
            this.added = false;
            Groups.queueFree(this);
        }
    }

    public void trns(Position pos) {
        this.trns(pos.getX(), pos.getY());
    }

    public Floor floorOn() {
        Tile tile = this.tileOn();
        return tile != null && tile.block() == Blocks.air ? tile.floor() : (Floor)Blocks.air;
    }

    public void set(Position pos) {
        this.set(pos.getX(), pos.getY());
    }

    public boolean isRemote() {
        return this instanceof Unitc && ((Unitc)this).isPlayer() && !this.isLocal();
    }

    public boolean isNull() {
        return false;
    }

    public void afterRead() {
        Clouds.register(this);
    }

    public void write(Writes write) {
        write.s(0);
        write.f(this.amount);
        write.i(this.generation);
        write.s(this.gas.id);
        TypeIO.writeTile(write, this.tile);
        write.f(this.x);
        write.f(this.y);
    }

    public void read(Reads read) {
        short REV = read.s();
        if (REV == 0) {
            this.amount = read.f();
            this.generation = read.i();
            this.gas = Vars.content.getByID(ContentType.typeid_UNUSED, read.s());
            this.tile = TypeIO.readTile(read);
            this.x = read.f();
            this.y = read.f();
            this.afterRead();
        } else {
            throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'PuddleComp'");
        }
    }

    public <T> T as() {
        return (T) this;
    }

    public Block blockOn() {
        Tile tile = this.tileOn();
        return tile == null ? Blocks.air : tile.block();
    }

    public void add() {
        if (!this.added) {
            Groups.all.add(this);
            Groups.draw.add(this);
//            Groups.puddle.add(this);
            this.added = true;
        }
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public <T> T with(Cons<T> cons) {
        cons.get((T)this);
        return (T)this;
    }

    public int tileX() {
        return World.toTile(this.x);
    }

    public int tileY() {
        return World.toTile(this.y);
    }

    public Tile tileOn() {
        return Vars.world.tileWorld(this.x, this.y);
    }

    public float clipSize() {
        return 20.0F;
    }

    public <T extends Entityc> T self() {
        return (T) this;
    }

    public boolean isAdded() {
        return this.added;
    }

    public boolean onSolid() {
        Tile tile = this.tileOn();
        return tile == null || tile.solid();
    }

    public boolean isLocal() {
        return this == (Entityc)Vars.player || this instanceof Unitc && ((Unitc)this).controller() == Vars.player;
    }

    public void reset() {
        this.accepting = 0.0F;
        this.updateTime = 0.0F;
        this.lastRipple = 0.0F;
        this.amount = 0.0F;
        this.generation = 0;
        this.tile = null;
        this.gas = null;
        this.added = false;
        this.id = EntityGroup.nextId();
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public static Cloud create() {
        return Pools.obtain(Cloud.class, Cloud::new);
    }

    public int classId() {
        return modVars.MOD_CONTENT_ID;
    }

    @Override
    public int modClassId() {
        return classId;
    }

    public float accepting() {
        return this.accepting;
    }

    public void accepting(float accepting) {
        this.accepting = accepting;
    }

    public float updateTime() {
        return this.updateTime;
    }

    public void updateTime(float updateTime) {
        this.updateTime = updateTime;
    }

    public float lastRipple() {
        return this.lastRipple;
    }

    public void lastRipple(float lastRipple) {
        this.lastRipple = lastRipple;
    }

    public float amount() {
        return this.amount;
    }

    public void amount(float amount) {
        this.amount = amount;
    }

    public int generation() {
        return this.generation;
    }

    public void generation(int generation) {
        this.generation = generation;
    }

    public Tile tile() {
        return this.tile;
    }

    public void tile(Tile tile) {
        this.tile = tile;
    }

    public Gas gas() {
        return this.gas;
    }

    public void gas(Gas gas) {
        this.gas = gas;
    }

    public int id() {
        return this.id;
    }

    public void id(int id) {
        this.id = id;
    }

    public float x() {
        return this.x;
    }

    public void x(float x) {
        this.x = x;
    }

    public float y() {
        return this.y;
    }

    public void y(float y) {
        this.y = y;
    }
}
