package braindustry.gen;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Floatc4;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Structs;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.ai.formations.DistanceAssignmentStrategy;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationMember;
import mindustry.ai.formations.FormationPattern;
import mindustry.ai.types.FormationAI;
import mindustry.ai.types.LogicAI;
import mindustry.async.PhysicsProcess;
import mindustry.audio.SoundLoop;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.core.World;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.EntityCollisions;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.*;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.io.TypeIO;
import mindustry.logic.LAccess;
import mindustry.type.*;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.blocks.storage.CoreBlock;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;

public class StealthMechUnit extends Unit implements Teamc, Minerc, Boundedc, Rotc, ElevationMovec, Mechc, Posc, Syncc, Healthc, Itemsc, Unitc, Hitboxc, Statusc, Physicsc, Velc, Builderc, Shieldc, Flyingc, Weaponsc, Commanderc, Drawc, Entityc {
    public static int classId = 0;
    public static final float warpDst = 180.0F;
    public static final float hitDuration = 9.0F;
    public static final Vec2[] vecs = new Vec2[]{new Vec2(), new Vec2(), new Vec2(), new Vec2()};
    public static final Vec2 tmp1 = new Vec2();
    public static final Vec2 tmp2 = new Vec2();
    public static int sequenceNum = 0;
    public static final Seq<FormationMember> members = new Seq();
    public static final Seq<Unit> units = new Seq();
    private transient float rotation_TARGET_;
    private transient float rotation_LAST_;
    public float baseRotation;
    private transient float baseRotation_TARGET_;
    private transient float baseRotation_LAST_;
    public transient float walkTime;
    public transient float walkExtension;
    public transient boolean walked;
    private transient float x_TARGET_;
    private transient float x_LAST_;
    private transient float y_TARGET_;
    private transient float y_LAST_;
    public UnitController controller;
    public transient float resupplyTime = Mathf.random(10.0F);
    public Seq<StatusEntry> statuses = new Seq();
    public transient Bits applied;
    protected transient float speedMultiplier;
    protected transient float damageMultiplier;
    protected transient float healthMultiplier;
    protected transient float reloadMultiplier;
    public transient BuildPlan lastActive;
    public transient int lastSize;
    public transient float buildAlpha;
    public transient boolean wasFlying;
    protected transient boolean isRotate;
    public transient boolean added;

    public StealthMechUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.speedMultiplier = 1.0F;
        this.damageMultiplier = 1.0F;
        this.healthMultiplier = 1.0F;
        this.reloadMultiplier = 1.0F;
        this.buildAlpha = 0.0F;
    }

    public boolean serialize() {
        return true;
    }

    public String toString() {
        return "MechUnit#" + this.id;
    }

    public void clearItem() {
        this.stack.amount = 0;
    }

    public void damagePierce(float amount, boolean withEffect) {
        float pre = this.hitTime;
        this.rawDamage(amount);
        if (!withEffect) {
            this.hitTime = pre;
        }

    }

    public boolean cheating() {
        return this.team.rules().cheat;
    }

    public float getY() {
        return this.y;
    }

    public void wobble() {
        this.x += Mathf.sin(Time.time + (float) (this.id() % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
        this.y += Mathf.cos(Time.time + (float) (this.id() % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
    }

    public void write(Writes write) {
        write.s(5);
        write.f(this.ammo);
        write.f(this.armor);
        write.f(this.baseRotation);
        TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        TypeIO.writeTile(write, this.mineTile);
        TypeIO.writeMounts(write, this.mounts);
        write.i(this.plans.size);

        int INDEX;
        for (INDEX = 0; INDEX < this.plans.size; ++INDEX) {
            TypeIO.writeRequest(write, (BuildPlan) this.plans.get(INDEX));
        }

        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);

        for (INDEX = 0; INDEX < this.statuses.size; ++INDEX) {
            TypeIO.writeStatuse(write, (StatusEntry) this.statuses.get(INDEX));
        }

        TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        write.f(this.x);
        write.f(this.y);
    }

    public void display(Table table) {
        this.type.display(this, table);
    }

    public float realSpeed() {
        return Mathf.lerp(1.0F, this.type.canBoost ? this.type.boostMultiplier : 1.0F, this.elevation) * this.speed();
    }

    public BuildPlan buildPlan() {
        return this.plans.size == 0 ? null : (BuildPlan) this.plans.first();
    }

    public boolean onSolid() {
        Tile tile = this.tileOn();
        return tile == null || tile.solid();
    }

    public boolean isBuilding() {
        return this.plans.size != 0;
    }

    public void moveAt(Vec2 vector, float acceleration) {
        if (!vector.isZero()) {
            this.walked = true;
        }

        Vec2 t = tmp1.set(vector);
        tmp2.set(t).sub(this.vel).limit(acceleration * vector.len() * Time.delta * this.floorSpeedMultiplier());
        this.vel.add(tmp2);
    }

    public void clearBuilding() {
        this.plans.clear();
    }

    public void controller(UnitController next) {
        this.controller = next;
        if (this.controller.unit() != this) {
            this.controller.unit(this);
        }

        this.clearCommand();
    }

    public void set(Position pos) {
        this.set(pos.getX(), pos.getY());
    }

    public void afterRead() {
        this.afterSync();
        this.controller(this.type.createController());
        this.updateLastPosition();
    }

    public <T> T as() {
        return (T) this;
    }

    private void rawDamage(float amount) {
        boolean hadShields = this.shield > 1.0E-4F;
        if (hadShields) {
            this.shieldAlpha = 1.0F;
        }

        float shieldDamage = Math.min(Math.max(this.shield, 0.0F), amount);
        this.shield -= shieldDamage;
        this.hitTime = 1.0F;
        amount -= shieldDamage;
        if (amount > 0.0F) {
            this.health -= amount;
            if (this.health <= 0.0F && !this.dead) {
                this.kill();
            }

            if (hadShields && this.shield <= 1.0E-4F) {
                Fx.unitShieldBreak.at(this.x, this.y, 0.0F, this);
            }
        }

    }

    public Player getPlayer() {
        return this.isPlayer() ? (Player) this.controller : null;
    }

    public boolean isFlying() {
        return this.elevation >= 0.09F;
    }

    public void getCollisions(Cons<QuadTree> consumer) {
    }

    public boolean isLocal() {
        return this == (Entityc) Vars.player || this instanceof Unitc && ((Unitc) this).controller() == Vars.player;
    }

    public void set(UnitType def, UnitController controller) {
        if (this.type != def) {
            this.setType(def);
        }

        this.controller(controller);
    }

    public void damage(float amount) {
        amount = Math.max(amount - this.armor, 0.1F * amount);
        amount /= this.healthMultiplier;
        this.rawDamage(amount);
    }

    public Object senseObject(LAccess sensor) {
        Object var10000;
        switch (sensor) {
            case type:
                var10000 = this.type;
                break;
            case name:
                UnitController var7 = this.controller;
                Player p;
                var10000 = var7 instanceof Player && (p = (Player) var7) == (Player) var7 ? p.name : null;
                break;
            case firstItem:
                var10000 = this.stack().amount == 0 ? null : this.item();
                break;
            case payloadType:
                Payloadc pay;
                if (this instanceof Payloadc && (pay = (Payloadc) this) == (Payloadc) this) {
                    if (pay.payloads().isEmpty()) {
                        var10000 = null;
                    } else {
                        Object var5 = pay.payloads().peek();
                        UnitPayload p1;
                        if (var5 instanceof UnitPayload && (p1 = (UnitPayload) var5) == (UnitPayload) var5) {
                            var10000 = p1.unit.type;
                        } else {
                            var5 = pay.payloads().peek();
                            BuildPayload p2;
                            var10000 = var5 instanceof BuildPayload && (p2 = (BuildPayload) var5) == (BuildPayload) var5 ? p2.block() : null;
                        }
                    }
                } else {
                    var10000 = null;
                }
                break;
            default:
                var10000 = noSensed;
        }

        return var10000;
    }

    public boolean activelyBuilding() {
        if (this.isBuilding() && !Vars.state.isEditor() && !this.within(this.buildPlan(), Vars.state.rules.infiniteResources ? 3.4028235E38F : 220.0F)) {
            return false;
        } else {
            return this.isBuilding() && this.updateBuilding;
        }
    }

    public void damage(float amount, boolean withEffect) {
        float pre = this.hitTime;
        this.damage(amount);
        if (!withEffect) {
            this.hitTime = pre;
        }

    }

    public boolean canShoot() {
        return !this.type.canBoost || !this.isFlying();
    }

    public boolean shouldSkip(BuildPlan request, Building core) {
        if (!Vars.state.rules.infiniteResources && !this.team.rules().infiniteResources && !request.breaking && core != null && !request.isRotation(this.team)) {
            return request.stuck && !core.items.has(request.block.requirements) || Structs.contains(request.block.requirements, (i) -> {
                return !core.items.has(i.item) && Mathf.round((float) i.amount * Vars.state.rules.buildCostMultiplier) > 0;
            }) && !request.initialized;
        } else {
            return false;
        }
    }

    public boolean damaged() {
        return this.health < this.maxHealth - 0.001F;
    }

    public float clipSize() {
        if (this.isBuilding()) {
            return Vars.state.rules.infiniteResources ? 3.4028235E38F : Math.max(this.type.clipSize, (float) this.type.region.width) + 220.0F + 32.0F;
        } else {
            return Math.max((float) this.type.region.width * 2.0F, this.type.clipSize);
        }
    }

    public boolean validMine(Tile tile, boolean checkDst) {
        return tile != null && tile.block() == Blocks.air && (this.within(tile.worldx(), tile.worldy(), 70.0F) || !checkDst) && tile.drop() != null && this.canMine(tile.drop());
    }

    public void aimLook(Position pos) {
        this.aim(pos);
        this.lookAt(pos);
    }

    public double sense(LAccess sensor) {
        double var10000;
        switch (sensor) {
            case totalItems:
                var10000 = (double) this.stack().amount;
                break;
            case itemCapacity:
                var10000 = (double) this.type.itemCapacity;
                break;
            case rotation:
                var10000 = (double) this.rotation;
                break;
            case health:
                var10000 = (double) this.health;
                break;
            case maxHealth:
                var10000 = (double) this.maxHealth;
                break;
            case ammo:
                var10000 = !Vars.state.rules.unitAmmo ? (double) this.type.ammoCapacity : (double) this.ammo;
                break;
            case ammoCapacity:
                var10000 = (double) this.type.ammoCapacity;
                break;
            case x:
                var10000 = (double) World.conv(this.x);
                break;
            case y:
                var10000 = (double) World.conv(this.y);
                break;
            case team:
                var10000 = (double) this.team.id;
                break;
            case shooting:
                var10000 = this.isShooting() ? 1.0D : 0.0D;
                break;
            case shootX:
                var10000 = (double) World.conv(this.aimX());
                break;
            case shootY:
                var10000 = (double) World.conv(this.aimY());
                break;
            case mining:
                var10000 = this.mining() ? 1.0D : 0.0D;
                break;
            case mineX:
                var10000 = this.mining() ? (double) this.mineTile.x : -1.0D;
                break;
            case mineY:
                var10000 = this.mining() ? (double) this.mineTile.y : -1.0D;
                break;
            case flag:
                var10000 = this.flag;
                break;
            case controlled:
                var10000 = !(this.controller instanceof LogicAI) && !(this.controller instanceof Player) ? 0.0D : 1.0D;
                break;
            case commanded:
                var10000 = this.controller instanceof FormationAI ? 1.0D : 0.0D;
                break;
            case payloadCount:
                Payloadc pay;
                var10000 = (double) (this instanceof Payloadc && (pay = (Payloadc) this) == (Payloadc) this ? pay.payloads().size : 0);
                break;
            default:
                var10000 = 0.0D;
        }

        return var10000;
    }

    public void clampHealth() {
        this.health = Mathf.clamp(this.health, 0.0F, this.maxHealth);
    }

    public void setWeaponRotation(float rotation) {
        WeaponMount[] var2 = this.mounts;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            WeaponMount mount = var2[var4];
            mount.rotation = rotation;
        }

    }

    public TextureRegion icon() {
        return this.type.icon(Cicon.full);
    }

    public float deltaAngle() {
        return Mathf.angle(this.deltaX, this.deltaY);
    }

    public boolean hasEffect(StatusEffect effect) {
        return this.applied.get(effect.id);
    }

    public void killed() {
        this.health = 0.0F;
        this.dead = true;
        if (!this.type.flying) {
            this.destroy();
        }

        this.clearCommand();
    }

    public void moveAt(Vec2 vector) {
        this.moveAt(vector, this.type.accel);
    }

    public int pathType() {
        return 0;
    }

    public boolean isCommanding() {
        return this.formation != null;
    }

    public float getX() {
        return this.x;
    }

    private void drawRect(Floatc4 floatc4, float x, float y, float size) {
        floatc4.get(x - size / 2f, y - size / 2f, size, size);
    }

    public void draw() {
        Color color = team.color.cpy();
        Draw.color(color.cpy());
        drawRect(Lines::rect, this.x, this.y, this.hitSize);
        Draw.color(color.cpy().lerp(Color.black, 0.1f));
        drawRect(Lines::rect, this.x, this.y, this.clipSize());
        if (true) return;
        float tx;
        float ty;
        float focusLen;
        if (this.mining()) {
            focusLen = this.hitSize / 2.0F + Mathf.absin(Time.time, 1.1F, 0.5F);
            float swingScl = 12.0F;
            float swingMag = 1.0F;
            float flashScl = 0.3F;
            float px = this.x + Angles.trnsx(this.rotation, focusLen);
            tx = this.y + Angles.trnsy(this.rotation, focusLen);
            ty = this.mineTile.worldx() + Mathf.sin(Time.time + 48.0F, swingScl, swingMag);
            focusLen = this.mineTile.worldy() + Mathf.sin(Time.time + 48.0F, swingScl + 2.0F, swingMag);
            Draw.z(115.1F);
            Draw.color(Color.lightGray, Color.white, 1.0F - flashScl + Mathf.absin(Time.time, 0.5F, flashScl));
            Drawf.laser(this.team(), Core.atlas.find("minelaser"), Core.atlas.find("minelaser-end"), px, tx, ty, focusLen, 0.75F);
            if (this.isLocal()) {
                Lines.stroke(1.0F, Pal.accent);
                Lines.poly(this.mineTile.worldx(), this.mineTile.worldy(), 4, 4.0F * Mathf.sqrt2, Time.time);
            }

            Draw.color();
        }

        this.type.draw(this);
        Iterator var20 = this.statuses.iterator();

        while (var20.hasNext()) {
            StatusEntry e = (StatusEntry) var20.next();
            e.effect.draw(this);
        }

        boolean active = this.activelyBuilding();
        if (active || this.lastActive != null) {
            Draw.z(115.0F);
            BuildPlan plan = active ? this.buildPlan() : this.lastActive;
            Tile tile = Vars.world.tile(plan.x, plan.y);
            CoreBlock.CoreBuild core = this.team.core();
            if (tile != null && this.within(plan, Vars.state.rules.infiniteResources ? 3.4028235E38F : 220.0F)) {
                if (core != null && active && !this.isLocal() && !(tile.block() instanceof ConstructBlock)) {
                    Draw.z(84.0F);
                    this.drawPlan(plan, 0.5F);
                    Draw.z(115.0F);
                }

                int size = plan.breaking ? (active ? tile.block().size : this.lastSize) : plan.block.size;
                tx = plan.drawx();
                ty = plan.drawy();
                Lines.stroke(1.0F, plan.breaking ? Pal.remove : Pal.accent);
                focusLen = this.type.buildBeamOffset + Mathf.absin(Time.time, 3.0F, 0.6F);
                float px = this.x + Angles.trnsx(this.rotation, focusLen);
                float py = this.y + Angles.trnsy(this.rotation, focusLen);
                float sz = (float) (8 * size) / 2.0F;
                float ang = this.angleTo(tx, ty);
                vecs[0].set(tx - sz, ty - sz);
                vecs[1].set(tx + sz, ty - sz);
                vecs[2].set(tx - sz, ty + sz);
                vecs[3].set(tx + sz, ty + sz);
                Arrays.sort(vecs, Structs.comparingFloat((vec) -> {
                    return -Angles.angleDist(this.angleTo(vec), ang);
                }));
                Vec2 close = (Vec2) Geometry.findClosest(this.x, this.y, vecs);
                float x1 = vecs[0].x;
                float y1 = vecs[0].y;
                float x2 = close.x;
                float y2 = close.y;
                float x3 = vecs[1].x;
                float y3 = vecs[1].y;
                Draw.z(122.0F);
                Draw.alpha(this.buildAlpha);
                if (!active && !(tile.build instanceof ConstructBlock.ConstructBuild)) {
                    Fill.square(plan.drawx(), plan.drawy(), (float) (size * 8) / 2.0F);
                }

                if (Vars.renderer.animateShields) {
                    if (close != vecs[0] && close != vecs[1]) {
                        Fill.tri(px, py, x1, y1, x2, y2);
                        Fill.tri(px, py, x3, y3, x2, y2);
                    } else {
                        Fill.tri(px, py, x1, y1, x3, y3);
                    }
                } else {
                    Lines.line(px, py, x1, y1);
                    Lines.line(px, py, x3, y3);
                }

                Fill.square(px, py, 1.8F + Mathf.absin(Time.time, 2.2F, 1.1F), this.rotation + 45.0F);
                Draw.reset();
                Draw.z(115.0F);
            }
        }

    }

    public float walkExtend(boolean scaled) {
        float raw = this.walkTime % (this.type.mechStride * 4.0F);
        if (scaled) {
            return raw / this.type.mechStride;
        } else {
            if (raw > this.type.mechStride * 3.0F) {
                raw -= this.type.mechStride * 4.0F;
            } else if (raw > this.type.mechStride * 2.0F) {
                raw = this.type.mechStride * 2.0F - raw;
            } else if (raw > this.type.mechStride) {
                raw = this.type.mechStride * 2.0F - raw;
            }

            return raw;
        }
    }

    public void destroy() {
        if (this.isAdded()) {
            float explosiveness = 2.0F + this.item().explosiveness * (float) this.stack().amount * 1.53F;
            float flammability = this.item().flammability * (float) this.stack().amount / 1.9F;
            if (!this.spawnedByCore) {
                Damage.dynamicExplosion(this.x, this.y, flammability, explosiveness, 0.0F, this.bounds() / 2.0F, Vars.state.rules.damageExplosions, this.item().flammability > 1.0F, this.team);
            }

            float shake = this.hitSize / 3.0F;
            Effect.scorch(this.x, this.y, (int) (this.hitSize / 5.0F));
            Fx.explosion.at(this);
            Effect.shake(shake, shake, this);
            this.type.deathSound.at(this);
            Events.fire(new EventType.UnitDestroyEvent(this));
            if (explosiveness > 7.0F && this.isLocal()) {
                Events.fire(EventType.Trigger.suicideBomb);
            }

            if (this.type.flying && !this.spawnedByCore) {
                Damage.damage(this.team, this.x, this.y, Mathf.pow(this.hitSize, 0.94F) * 1.25F, Mathf.pow(this.hitSize, 0.75F) * this.type.crashDamageMultiplier * 5.0F, true, false, true);
            }

            if (!Vars.headless) {
                for (int i = 0; i < this.type.wreckRegions.length; ++i) {
                    if (this.type.wreckRegions[i].found()) {
                        float range = this.type.hitSize / 4.0F;
                        Tmp.v1.rnd(range);
                        Effect.decal(this.type.wreckRegions[i], this.x + Tmp.v1.x, this.y + Tmp.v1.y, this.rotation - 90.0F);
                    }
                }
            }

            this.remove();
        }
    }

    public void updateLastPosition() {
        this.deltaX = this.x - this.lastX;
        this.deltaY = this.y - this.lastY;
        this.lastX = this.x;
        this.lastY = this.y;
    }

    public void landed() {
        if (this.type.landShake > 0.0F) {
            Effect.shake(this.type.landShake, this.type.landShake, this);
        }

        this.type.landed(this);
    }

    public Floor floorOn() {
        Tile tile = this.tileOn();
        return tile != null && tile.block() == Blocks.air ? tile.floor() : (Floor) Blocks.air;
    }

    public Block blockOn() {
        Tile tile = this.tileOn();
        return tile == null ? Blocks.air : tile.block();
    }

    public int count() {
        return this.team.data().countType(this.type);
    }

    public boolean mining() {
        return this.mineTile != null && !this.activelyBuilding();
    }

    public Color statusColor() {
        if (this.statuses.size == 0) {
            return Tmp.c1.set(Color.white);
        } else {
            float r = 1.0F;
            float g = 1.0F;
            float b = 1.0F;
            float total = 0.0F;

            float intensity;
            for (Iterator<StatusEntry> var5 = this.statuses.iterator(); var5.hasNext(); total += intensity) {
                StatusEntry entry = var5.next();
                intensity = entry.time < 10.0F ? entry.time / 10.0F : 1.0F;
                r += entry.effect.color.r * intensity;
                g += entry.effect.color.g * intensity;
                b += entry.effect.color.b * intensity;
            }

            float count = (float) this.statuses.size + total;
            return Tmp.c1.set(r / count, g / count, b / count, 1.0F);
        }
    }

    public int cap() {
        return Units.getCap(this.team);
    }

    public Item item() {
        return this.stack.item;
    }

    public boolean isAI() {
        return this.controller instanceof AIController;
    }

    public float ammof() {
        return this.ammo / (float) this.type.ammoCapacity;
    }

    public void commandNearby(FormationPattern pattern, Boolf<Unit> include) {
        Formation formation = new Formation(new Vec3(this.x, this.y, this.rotation), pattern);
        formation.slotAssignmentStrategy = new DistanceAssignmentStrategy(pattern);
        units.clear();
        Units.nearby(this.team, this.x, this.y, 150.0F, (u) -> {
            if (u.isAI() && include.get(u) && u != this && u.type.flying == this.type.flying && u.hitSize <= this.hitSize * 1.1F) {
                units.add(u);
            }

        });
        if (!units.isEmpty()) {
            units.sort(Structs.comps(Structs.comparingFloat((u) -> {
                return -u.hitSize;
            }), Structs.comparingFloat((u) -> {
                return u.dst2(this);
            })));
            units.truncate(this.type.commandLimit);
            this.command(formation, units);
        }
    }

    public boolean canPassOn() {
        return this.canPass(this.tileX(), this.tileY());
    }

    public void apply(StatusEffect effect, float duration) {
        if (effect != StatusEffects.none && effect != null && !this.isImmune(effect)) {
            if (this.statuses.size > 0) {
                for (int i = 0; i < this.statuses.size; ++i) {
                    StatusEntry entry = (StatusEntry) this.statuses.get(i);
                    if (entry.effect == effect) {
                        entry.time = Math.max(entry.time, duration);
                        return;
                    }

                    if (entry.effect.reactsWith(effect)) {
                        StatusEntry.tmp.effect = entry.effect;
                        entry.effect.getTransition(this, effect, entry.time, duration, StatusEntry.tmp);
                        entry.time = StatusEntry.tmp.time;
                        if (StatusEntry.tmp.effect != entry.effect) {
                            entry.effect = StatusEntry.tmp.effect;
                        }

                        return;
                    }
                }
            }

            StatusEntry entry = (StatusEntry) Pools.obtain(StatusEntry.class, StatusEntry::new);
            entry.set(effect, duration);
            this.statuses.add(entry);
        }
    }

    public void add() {
        if (!this.added) {
            Groups.all.add(this);
            Groups.unit.add(this);
            Groups.sync.add(this);
            Groups.draw.add(this);
            this.team.data().updateCount(this.type, 1);
            if (this.count() > this.cap() && !this.spawnedByCore && !this.dead) {
                Call.unitCapDeath(this);
                this.team.data().updateCount(this.type, -1);
            }

            this.updateLastPosition();
            this.added = true;
        }
    }

    public void interpolate() {
        if (this.lastUpdated != 0L && this.updateSpacing != 0L) {
            float timeSinceUpdate = (float) Time.timeSinceMillis(this.lastUpdated);
            float alpha = Math.min(timeSinceUpdate / (float) this.updateSpacing, 2.0F);
            this.baseRotation = Mathf.slerp(this.baseRotation_LAST_, this.baseRotation_TARGET_, alpha);
            this.rotation = Mathf.slerp(this.rotation_LAST_, this.rotation_TARGET_, alpha);
            this.x = Mathf.lerp(this.x_LAST_, this.x_TARGET_, alpha);
            this.y = Mathf.lerp(this.y_LAST_, this.y_TARGET_, alpha);
        } else if (this.lastUpdated != 0L) {
            this.baseRotation = this.baseRotation_TARGET_;
            this.rotation = this.rotation_TARGET_;
            this.x = this.x_TARGET_;
            this.y = this.y_TARGET_;
        }

    }

    public EntityCollisions.SolidPred solidity() {

//        return this.isFlying() ? null : EntityCollisions::solid;
        return this.isFlying() ? null : (x, y) -> !EntityCollisions.solid(x, y);
    }

    public void writeSyncManual(FloatBuffer buffer) {
        buffer.put(this.baseRotation);
        buffer.put(this.rotation);
        buffer.put(this.x);
        buffer.put(this.y);
    }

    public int maxAccepted(Item item) {
        return this.stack.item != item && this.stack.amount > 0 ? 0 : this.itemCapacity() - this.stack.amount;
    }

    public boolean checkTarget(boolean targetAir, boolean targetGround) {
        return this.isGrounded() && targetGround || this.isFlying() && targetAir;
    }

    public void read(Reads read) {
        short REV = read.s();
        int plans_LENGTH;
        int statuses_LENGTH;
        StatusEntry statuses_ITEM;
        if (REV == 0) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            read.bool();
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = TypeIO.readItems(read, this.stack);
            plans_LENGTH = read.i();
            this.statuses.clear();

            for (statuses_LENGTH = 0; statuses_LENGTH < plans_LENGTH; ++statuses_LENGTH) {
                statuses_ITEM = TypeIO.readStatuse(read);
                if (statuses_ITEM != null) {
                    this.statuses.add(statuses_ITEM);
                }
            }

            this.team = TypeIO.readTeam(read);
            this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 1) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = TypeIO.readItems(read, this.stack);
            plans_LENGTH = read.i();
            this.statuses.clear();

            for (statuses_LENGTH = 0; statuses_LENGTH < plans_LENGTH; ++statuses_LENGTH) {
                statuses_ITEM = TypeIO.readStatuse(read);
                if (statuses_ITEM != null) {
                    this.statuses.add(statuses_ITEM);
                }
            }

            this.team = TypeIO.readTeam(read);
            this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 2) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = TypeIO.readItems(read, this.stack);
            plans_LENGTH = read.i();
            this.statuses.clear();

            for (statuses_LENGTH = 0; statuses_LENGTH < plans_LENGTH; ++statuses_LENGTH) {
                statuses_ITEM = TypeIO.readStatuse(read);
                if (statuses_ITEM != null) {
                    this.statuses.add(statuses_ITEM);
                }
            }

            this.team = TypeIO.readTeam(read);
            this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 3) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = TypeIO.readTile(read);
            this.mounts = TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = TypeIO.readItems(read, this.stack);
            plans_LENGTH = read.i();
            this.statuses.clear();

            for (statuses_LENGTH = 0; statuses_LENGTH < plans_LENGTH; ++statuses_LENGTH) {
                statuses_ITEM = TypeIO.readStatuse(read);
                if (statuses_ITEM != null) {
                    this.statuses.add(statuses_ITEM);
                }
            }

            this.team = TypeIO.readTeam(read);
            this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else {
            BuildPlan plans_ITEM;
            int INDEX;
            if (REV == 4) {
                this.ammo = read.f();
                this.armor = read.f();
                this.baseRotation = read.f();
                this.controller = TypeIO.readController(read, this.controller);
                this.elevation = read.f();
                this.flag = read.d();
                this.health = read.f();
                this.isShooting = read.bool();
                this.mineTile = TypeIO.readTile(read);
                this.mounts = TypeIO.readMounts(read, this.mounts);
                plans_LENGTH = read.i();
                this.plans.clear();

                for (statuses_LENGTH = 0; statuses_LENGTH < plans_LENGTH; ++statuses_LENGTH) {
                    plans_ITEM = TypeIO.readRequest(read);
                    if (plans_ITEM != null) {
                        this.plans.add(plans_ITEM);
                    }
                }

                this.rotation = read.f();
                this.shield = read.f();
                this.spawnedByCore = read.bool();
                this.stack = TypeIO.readItems(read, this.stack);
                statuses_LENGTH = read.i();
                this.statuses.clear();

                for (INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
                    statuses_ITEM = TypeIO.readStatuse(read);
                    if (statuses_ITEM != null) {
                        this.statuses.add(statuses_ITEM);
                    }
                }

                this.team = TypeIO.readTeam(read);
                this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
                this.x = read.f();
                this.y = read.f();
            } else {
                if (REV != 5) {
                    throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'mace'");
                }

                this.ammo = read.f();
                this.armor = read.f();
                this.baseRotation = read.f();
                this.controller = TypeIO.readController(read, this.controller);
                this.elevation = read.f();
                this.flag = read.d();
                this.health = read.f();
                this.isShooting = read.bool();
                this.mineTile = TypeIO.readTile(read);
                this.mounts = TypeIO.readMounts(read, this.mounts);
                plans_LENGTH = read.i();
                this.plans.clear();

                for (statuses_LENGTH = 0; statuses_LENGTH < plans_LENGTH; ++statuses_LENGTH) {
                    plans_ITEM = TypeIO.readRequest(read);
                    if (plans_ITEM != null) {
                        this.plans.add(plans_ITEM);
                    }
                }

                this.rotation = read.f();
                this.shield = read.f();
                this.spawnedByCore = read.bool();
                this.stack = TypeIO.readItems(read, this.stack);
                statuses_LENGTH = read.i();
                this.statuses.clear();

                for (INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
                    statuses_ITEM = TypeIO.readStatuse(read);
                    if (statuses_ITEM != null) {
                        this.statuses.add(statuses_ITEM);
                    }
                }

                this.team = TypeIO.readTeam(read);
                this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
                this.updateBuilding = read.bool();
                this.x = read.f();
                this.y = read.f();
            }
        }

        this.afterRead();
    }

    public boolean isRemote() {
        return this instanceof Unitc && ((Unitc) this).isPlayer() && !this.isLocal();
    }

    public Building closestEnemyCore() {
        return Vars.state.teams.closestEnemyCore(this.x, this.y, this.team);
    }

    public void commandNearby(FormationPattern pattern) {
        this.commandNearby(pattern, (u) -> {
            return true;
        });
    }

    public boolean isValid() {
        return !this.dead && this.isAdded();
    }

    public boolean acceptsItem(Item item) {
        return !this.hasItem() || item == this.stack.item && this.stack.amount + 1 <= this.itemCapacity();
    }

    public float bounds() {
        return this.hitSize * 2.0F;
    }

    public boolean isAdded() {
        return this.added;
    }

    public void addItem(Item item) {
        this.addItem(item, 1);
    }

    public void controlWeapons(boolean rotate, boolean shoot) {
        WeaponMount[] var3 = this.mounts;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            WeaponMount mount = var3[var5];
            mount.rotate = rotate;
            mount.shoot = shoot;
        }

        this.isRotate = rotate;
        this.isShooting = shoot;
    }

    public void snapInterpolation() {
        this.updateSpacing = 16L;
        this.lastUpdated = Time.millis();
        this.baseRotation_LAST_ = this.baseRotation;
        this.baseRotation_TARGET_ = this.baseRotation;
        this.rotation_LAST_ = this.rotation;
        this.rotation_TARGET_ = this.rotation;
        this.x_LAST_ = this.x;
        this.x_TARGET_ = this.x;
        this.y_LAST_ = this.y;
        this.y_TARGET_ = this.y;
    }

    public void trns(float x, float y) {
        this.set(this.x + x, this.y + y);
    }

    public float prefRotation() {
        if (this.activelyBuilding()) {
            return this.angleTo(this.buildPlan());
        } else if (this.mineTile != null) {
            return this.angleTo(this.mineTile);
        } else {
            return this.moving() ? this.vel().angle() : this.rotation;
        }
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean canMine() {
        return this.type.mineSpeed > 0.0F && this.type.mineTier >= 0;
    }

    public void remove() {
        if (this.added) {
            Groups.all.remove(this);
            Groups.unit.remove(this);
            Groups.sync.remove(this);
            Groups.draw.remove(this);
            if (Vars.net.client()) {
                Vars.netClient.addRemovedEntity(this.id());
            }

            this.team.data().updateCount(this.type, -1);
            this.controller.removed(this);
            WeaponMount[] var1 = this.mounts;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                WeaponMount mount = var1[var3];
                if (mount.bullet != null) {
                    mount.bullet.time = mount.bullet.lifetime - 10.0F;
                    mount.bullet = null;
                }

                if (mount.sound != null) {
                    mount.sound.stop();
                }
            }

            this.clearCommand();
            this.added = false;
        }
    }

    public float deltaLen() {
        return Mathf.len(this.deltaX, this.deltaY);
    }

    public float speed() {
        float strafePenalty = !this.isGrounded() && this.isPlayer() ? Mathf.lerp(1.0F, this.type.strafePenalty, Angles.angleDist(this.vel().angle(), this.rotation) / 180.0F) : 1.0F;
        return (this.isCommanding() ? this.minFormationSpeed * 0.98F : this.type.speed) * strafePenalty;
    }

    public void update() {
        this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
        this.vel.scl(Mathf.clamp(1.0F - this.drag * Time.delta));
        Building core = this.closestCore();
        int index;
        if (core != null && this.mineTile != null && this.mineTile.drop() != null && !this.acceptsItem(this.mineTile.drop()) && this.within(core, 220.0F) && !this.offloadImmediately()) {
            index = core.acceptStack(this.item(), this.stack().amount, this);
            if (index > 0) {
                Call.transferItemTo(this, this.item(), index, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
                this.clearItem();
            }
        }

        if (!this.validMine(this.mineTile)) {
            this.mineTile = null;
            this.mineTimer = 0.0F;
        } else if (this.mining()) {
            Item item = this.mineTile.drop();
            this.mineTimer += Time.delta * this.type.mineSpeed;
            if (Mathf.chance(0.06D * (double) Time.delta)) {
                Fx.pulverizeSmall.at(this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), 0.0F, item.color);
            }

            if (this.mineTimer >= 50.0F + (float) item.hardness * 15.0F) {
                this.mineTimer = 0.0F;
                if (Vars.state.rules.sector != null && this.team() == Vars.state.rules.defaultTeam) {
                    Vars.state.rules.sector.info.handleProduction(item, 1);
                }

                if (core != null && this.within(core, 220.0F) && core.acceptStack(item, 1, this) == 1 && this.offloadImmediately()) {
                    if (this.item() == item && !Vars.net.client()) {
                        this.addItem(item);
                    }

                    Call.transferItemTo(this, item, 1, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
                } else if (this.acceptsItem(item)) {
                    InputHandler.transferItemToUnit(item, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), this);
                } else {
                    this.mineTile = null;
                    this.mineTimer = 0.0F;
                }
            }

            if (!Vars.headless) {
                Vars.control.sound.loop(this.type.mineSound, this, this.type.mineSoundVolume);
            }
        }

        if (!Vars.net.client() || this.isLocal()) {
            Vec2 var10000;
            if (this.x < 0.0F) {
                var10000 = this.vel;
                var10000.x += -this.x / 180.0F;
            }

            if (this.y < 0.0F) {
                var10000 = this.vel;
                var10000.y += -this.y / 180.0F;
            }

            if (this.x > (float) Vars.world.unitWidth()) {
                var10000 = this.vel;
                var10000.x -= (this.x - (float) Vars.world.unitWidth()) / 180.0F;
            }

            if (this.y > (float) Vars.world.unitHeight()) {
                var10000 = this.vel;
                var10000.y -= (this.y - (float) Vars.world.unitHeight()) / 180.0F;
            }
        }

        if (this.isGrounded()) {
            this.x = Mathf.clamp(this.x, 0.0F, (float) (Vars.world.width() * 8 - 8));
            this.y = Mathf.clamp(this.y, 0.0F, (float) (Vars.world.height() * 8 - 8));
        }

        if (this.x < -500.0F || this.y < -500.0F || this.x >= (float) (Vars.world.width() * 8) + 500.0F || this.y >= (float) (Vars.world.height() * 8) + 500.0F) {
            this.kill();
        }

        float finalPlaceDst;
        if (this.walked || Vars.net.client()) {
            finalPlaceDst = this.deltaLen();
            this.baseRotation = Angles.moveToward(this.baseRotation, this.deltaAngle(), this.type().baseRotateSpeed * Mathf.clamp(finalPlaceDst / this.type().speed / Time.delta) * Time.delta);
            this.walkTime += finalPlaceDst;
            this.walked = false;
        }

        finalPlaceDst = this.walkExtend(false);
        float range = this.walkExtend(true);
        float extendScl = range % 1.0F;
        float lastExtend = this.walkExtension;
        int total;
        float weaponRotation;
        float mountX;
        float mountY;
        if (extendScl < lastExtend && range % 2.0F > 1.0F && !this.isFlying()) {
            total = -Mathf.sign(finalPlaceDst);
            float width = this.hitSize / 2.0F * (float) total;
            weaponRotation = this.type.mechStride * 1.35F;
            mountX = this.x + Angles.trnsx(this.baseRotation, weaponRotation, width);
            mountY = this.y + Angles.trnsy(this.baseRotation, weaponRotation, width);
            if (this.type.mechStepShake > 0.0F) {
                Effect.shake(this.type.mechStepShake, this.type.mechStepShake, mountX, mountY);
            }

            if (this.type.mechStepParticles) {
                Tile tile = Vars.world.tileWorld(mountX, mountY);
                if (tile != null) {
                    Color color = tile.floor().mapColor;
                    Fx.unitLand.at(mountX, mountY, this.hitSize / 8.0F, color);
                }
            }
        }

        this.walkExtension = extendScl;
        if (Vars.net.client() && !this.isLocal() || this.isRemote()) {
            this.interpolate();
        }

        this.hitTime -= Time.delta / 9.0F;
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
        this.itemTime = Mathf.lerpDelta(this.itemTime, (float) Mathf.num(this.hasItem()), 0.05F);
        this.type.update(this);
        if (Vars.state.rules.unitAmmo && this.ammo < (float) this.type.ammoCapacity - 1.0E-4F) {
            this.resupplyTime += Time.delta;
            if (this.resupplyTime > 10.0F) {
                this.type.ammoType.resupply(this);
                this.resupplyTime = 0.0F;
            }
        }

        if (this.abilities.size > 0) {
            Iterator var16 = this.abilities.iterator();

            while (var16.hasNext()) {
                Ability a = (Ability) var16.next();
                a.update(this);
            }
        }

        this.drag = this.type.drag * (this.isGrounded() ? this.floorOn().dragMultiplier : 1.0F);
        if (this.team != Vars.state.rules.waveTeam && Vars.state.hasSpawns() && (!Vars.net.client() || this.isLocal())) {
            finalPlaceDst = Vars.state.rules.dropZoneRadius + this.hitSize / 2.0F + 1.0F;
            Iterator var21 = Vars.spawner.getSpawns().iterator();

            while (var21.hasNext()) {
                Tile spawn = (Tile) var21.next();
                if (this.within(spawn.worldx(), spawn.worldy(), finalPlaceDst)) {
                    this.vel().add(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(1.1F - this.dst(spawn) / finalPlaceDst).scl(0.45F * Time.delta));
                }
            }
        }

        if (this.dead || this.health <= 0.0F) {
            this.drag = 0.01F;
            if (Mathf.chanceDelta(0.1D)) {
                Tmp.v1.setToRandomDirection().scl(this.hitSize);
                this.type.fallEffect.at(this.x + Tmp.v1.x, this.y + Tmp.v1.y);
            }

            if (Mathf.chanceDelta(0.2D)) {
                finalPlaceDst = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * this.elevation;
                range = Mathf.range(this.type.engineSize);
                this.type.fallThrusterEffect.at(this.x + Angles.trnsx(this.rotation + 180.0F, finalPlaceDst) + Mathf.range(range), this.y + Angles.trnsy(this.rotation + 180.0F, finalPlaceDst) + Mathf.range(range), Mathf.random());
            }

            this.elevation -= this.type.fallSpeed * Time.delta;
            if (this.isGrounded()) {
                this.destroy();
            }
        }

        Tile tile = this.tileOn();
        Floor floor = this.floorOn();
        if (tile != null && this.isGrounded() && !this.type.hovering) {
            if (tile.build != null) {
                tile.build.unitOn(this);
            }

            if (floor.damageTaken > 0.0F) {
                this.damageContinuous(floor.damageTaken);
            }
        }

        if (tile != null && !this.canPassOn()) {
            if (this.type.canBoost) {
                this.elevation = 1.0F;
            } else if (!Vars.net.client()) {
                this.kill();
            }
        }

        if (!Vars.net.client() && !this.dead) {
            this.controller.updateUnit();
        }

        if (!this.controller.isValidController()) {
            this.resetController();
        }

        if (this.spawnedByCore && !this.isPlayer() && !this.dead) {
            Call.unitDespawn(this);
        }

        floor = this.floorOn();
        if (this.isGrounded() && !this.type.hovering) {
            this.apply(floor.status, floor.statusDuration);
        }

        this.applied.clear();
        this.speedMultiplier = this.damageMultiplier = this.healthMultiplier = this.reloadMultiplier = 1.0F;
        if (!this.statuses.isEmpty()) {
            index = 0;

            label552:
            while (true) {
                while (true) {
                    if (index >= this.statuses.size) {
                        break label552;
                    }

                    StatusEntry entry = (StatusEntry) this.statuses.get(index++);
                    entry.time = Math.max(entry.time - Time.delta, 0.0F);
                    if (entry.effect != null && (entry.time > 0.0F || entry.effect.permanent)) {
                        this.applied.set(entry.effect.id);
                        this.speedMultiplier *= entry.effect.speedMultiplier;
                        this.healthMultiplier *= entry.effect.healthMultiplier;
                        this.damageMultiplier *= entry.effect.damageMultiplier;
                        this.reloadMultiplier *= entry.effect.reloadMultiplier;
                        entry.effect.update(this, entry.time);
                    } else {
                        Pools.free(entry);
                        --index;
                        this.statuses.remove(index);
                    }
                }
            }
        }

        if (!Vars.headless) {
            if (this.lastActive != null && this.buildAlpha <= 0.01F) {
                this.lastActive = null;
            }

            this.buildAlpha = Mathf.lerpDelta(this.buildAlpha, this.activelyBuilding() ? 1.0F : 0.0F, 0.15F);
        }

        if (this.updateBuilding && this.canBuild()) {
            finalPlaceDst = Vars.state.rules.infiniteResources ? 3.4028235E38F : 220.0F;
            boolean infinite = Vars.state.rules.infiniteResources || this.team().rules().infiniteResources;
            Iterator it = this.plans.iterator();

            label528:
            while (true) {
                BuildPlan req;
                Tile tileR;
                do {
                    if (!it.hasNext()) {
                        Building coreR = this.core();
                        if (this.buildPlan() != null) {
                            BuildPlan reqR;
                            if (this.plans.size > 1) {
                                for (total = 0; (!this.within((reqR = this.buildPlan()).tile(), finalPlaceDst) || this.shouldSkip(reqR, coreR)) && total < this.plans.size; ++total) {
                                    this.plans.removeFirst();
                                    this.plans.addLast(reqR);
                                }
                            }

                            BuildPlan current = this.buildPlan();
                            Tile tile2R = current.tile();
                            this.lastActive = current;
                            this.buildAlpha = 1.0F;
                            if (current.breaking) {
                                this.lastSize = tile2R.block().size;
                            }

                            if (this.within(tile2R, finalPlaceDst)) {
                                Building var41 = tile2R.build;
                                ConstructBlock.ConstructBuild entity;
                                if (var41 instanceof ConstructBlock.ConstructBuild && (entity = (ConstructBlock.ConstructBuild) var41) == (ConstructBlock.ConstructBuild) var41) {
                                    if (tile2R.team() != this.team && tile2R.team() != Team.derelict || !current.breaking && entity.cblock != current.block) {
                                        this.plans.removeFirst();
                                        break label528;
                                    }
                                } else if (!current.initialized && !current.breaking && Build.validPlace(current.block, this.team, current.x, current.y, current.rotation)) {
                                    boolean hasAll = infinite || current.isRotation(this.team) || !Structs.contains(current.block.requirements, (i) -> {
                                        return coreR != null && !coreR.items.has(i.item);
                                    });
                                    if (hasAll) {
                                        Call.beginPlace(this, current.block, this.team, current.x, current.y, current.rotation);
                                    } else {
                                        current.stuck = true;
                                    }
                                } else {
                                    if (current.initialized || !current.breaking || !Build.validBreak(this.team, current.x, current.y)) {
                                        this.plans.removeFirst();
                                        break label528;
                                    }

                                    Call.beginBreak(this, this.team, current.x, current.y);
                                }

                                if (tile2R.build instanceof ConstructBlock.ConstructBuild && !current.initialized) {
                                    Core.app.post(() -> {
                                        Events.fire(new EventType.BuildSelectEvent(tile2R, this.team, this, current.breaking));
                                    });
                                    current.initialized = true;
                                }

                                if (coreR != null || infinite) {
                                    var41 = tile2R.build;
                                    if (var41 instanceof ConstructBlock.ConstructBuild && (entity = (ConstructBlock.ConstructBuild) var41) == (ConstructBlock.ConstructBuild) var41) {
                                        if (current.breaking) {
                                            entity.deconstruct(this, coreR, 1.0F / entity.buildCost * Time.delta * this.type.buildSpeed * Vars.state.rules.buildSpeedMultiplier);
                                        } else {
                                            entity.construct(this, coreR, 1.0F / entity.buildCost * Time.delta * this.type.buildSpeed * Vars.state.rules.buildSpeedMultiplier, current.config);
                                        }

                                        current.stuck = Mathf.equal(current.progress, entity.progress);
                                        current.progress = entity.progress;
                                    }
                                }
                            }
                        }
                        break label528;
                    }

                    req = (BuildPlan) it.next();
                    tileR = Vars.world.tile(req.x, req.y);
                } while (tileR != null && (!req.breaking || tileR.block() != Blocks.air) && (req.breaking || (tileR.build == null || tileR.build.rotation != req.rotation) && req.block.rotate || tileR.block() != req.block));

                it.remove();
            }
        }

        this.shieldAlpha -= Time.delta / 15.0F;
        if (this.shieldAlpha < 0.0F) {
            this.shieldAlpha = 0.0F;
        }

        floor = this.floorOn();
        if (this.isFlying() != this.wasFlying) {
            if (this.wasFlying && this.tileOn() != null) {
                Fx.unitLand.at(this.x, this.y, this.floorOn().isLiquid ? 1.0F : 0.5F, this.tileOn().floor().mapColor);
            }

            this.wasFlying = this.isFlying();
        }

        if (!this.hovering && this.isGrounded() && (this.splashTimer += Mathf.dst(this.deltaX(), this.deltaY())) >= 7.0F + this.hitSize() / 8.0F) {
            floor.walkEffect.at(this.x, this.y, this.hitSize() / 8.0F, floor.mapColor);
            this.splashTimer = 0.0F;
            if (!(this instanceof WaterMovec)) {
                floor.walkSound.at(this.x, this.y, Mathf.random(floor.walkSoundPitchMin, floor.walkSoundPitchMax), floor.walkSoundVolume);
            }
        }

        if (this.canDrown() && floor.isLiquid && floor.drownTime > 0.0F) {
            this.drownTime += Time.delta / floor.drownTime;
            this.drownTime = Mathf.clamp(this.drownTime);
            if (Mathf.chanceDelta(0.05000000074505806D)) {
                floor.drownUpdateEffect.at(this.x, this.y, 1.0F, floor.mapColor);
            }

            if (this.drownTime >= 0.999F && !Vars.net.client()) {
                this.kill();
                Events.fire(new EventType.UnitDrownEvent(this));
            }
        } else {
            this.drownTime = Mathf.lerpDelta(this.drownTime, 0.0F, 0.03F);
        }

        boolean can = this.canShoot();
        WeaponMount[] var34 = this.mounts;
        int var29 = var34.length;

        for (int var31 = 0; var31 < var29; ++var31) {
            WeaponMount mount = var34[var31];
            Weapon weapon = mount.weapon;
            mount.reload = Math.max(mount.reload - Time.delta * this.reloadMultiplier, 0.0F);
            weaponRotation = this.rotation - 90.0F + (weapon.rotate ? mount.rotation : 0.0F);
            mountX = this.x + Angles.trnsx(this.rotation - 90.0F, weapon.x, weapon.y);
            mountY = this.y + Angles.trnsy(this.rotation - 90.0F, weapon.x, weapon.y);
            float shootX = mountX + Angles.trnsx(weaponRotation, weapon.shootX, weapon.shootY);
            float shootY = mountY + Angles.trnsy(weaponRotation, weapon.shootX, weapon.shootY);
            float shootAngle = weapon.rotate ? weaponRotation + 90.0F : Angles.angle(shootX, shootY, mount.aimX, mount.aimY) + (this.rotation - this.angleTo(mount.aimX, mount.aimY));
            if (weapon.continuous && mount.bullet != null) {
                if (mount.bullet.isAdded() && mount.bullet.time < mount.bullet.lifetime && mount.bullet.type == weapon.bullet) {
                    mount.bullet.rotation(weaponRotation + 90.0F);
                    mount.bullet.set(shootX, shootY);
                    mount.reload = weapon.reload;
                    this.vel.add(Tmp.v1.trns(this.rotation + 180.0F, mount.bullet.type.recoil));
                    if (weapon.shootSound != Sounds.none && !Vars.headless) {
                        if (mount.sound == null) {
                            mount.sound = new SoundLoop(weapon.shootSound, 1.0F);
                        }

                        mount.sound.update(this.x, this.y, true);
                    }
                } else {
                    mount.bullet = null;
                }
            } else {
                mount.heat = Math.max(mount.heat - Time.delta * this.reloadMultiplier / mount.weapon.cooldownTime, 0.0F);
                if (mount.sound != null) {
                    mount.sound.update(this.x, this.y, false);
                }
            }

            if (weapon.otherSide != -1 && weapon.alternate && mount.side == weapon.flipSprite && mount.reload + Time.delta * this.reloadMultiplier > weapon.reload / 2.0F && mount.reload <= weapon.reload / 2.0F) {
                this.mounts[weapon.otherSide].side = !this.mounts[weapon.otherSide].side;
                mount.side = !mount.side;
            }

            if (weapon.rotate && (mount.rotate || mount.shoot) && can) {
                float axisX = this.x + Angles.trnsx(this.rotation - 90.0F, weapon.x, weapon.y);
                float axisY = this.y + Angles.trnsy(this.rotation - 90.0F, weapon.x, weapon.y);
                mount.targetRotation = Angles.angle(axisX, axisY, mount.aimX, mount.aimY) - this.rotation;
                mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, weapon.rotateSpeed * Time.delta);
            } else if (!weapon.rotate) {
                mount.rotation = 0.0F;
                mount.targetRotation = this.angleTo(mount.aimX, mount.aimY);
            }

            if (mount.shoot && can && (this.ammo > 0.0F || !Vars.state.rules.unitAmmo || this.team().rules().infiniteAmmo) && (!weapon.alternate || mount.side == weapon.flipSprite) && (this.vel.len() >= mount.weapon.minShootVelocity || Vars.net.active() && !this.isLocal()) && mount.reload <= 1.0E-4F && Angles.within(weapon.rotate ? mount.rotation : this.rotation, mount.targetRotation, mount.weapon.shootCone)) {
                this.shoot(mount, shootX, shootY, mount.aimX, mount.aimY, mountX, mountY, shootAngle, Mathf.sign(weapon.x));
                mount.reload = weapon.reload;
                --this.ammo;
                if (this.ammo < 0.0F) {
                    this.ammo = 0.0F;
                }
            }
        }

        if (this.controlling.isEmpty()) {
            this.formation = null;
        }

        if (this.formation != null) {
            this.formation.anchor.set(this.x, this.y, 0.0F);
            this.formation.updateSlots();
            this.controlling.removeAll((u) -> {
                boolean var10000;
                if (!u.dead) {
                    UnitController ai$temp = u.controller();
                    FormationAI ai;
                    if (ai$temp instanceof FormationAI && (ai = (FormationAI) ai$temp) == (FormationAI) ai$temp && ai.leader == this) {
                        var10000 = false;
                        return var10000;
                    }
                }

                var10000 = true;
                return var10000;
            });
        }

    }

    public void resetController() {
        this.controller(this.type.createController());
    }

    public void heal(float amount) {
        this.health += amount;
        this.clampHealth();
    }

    public boolean hasItem() {
        return this.stack.amount > 0;
    }

    public void apply(StatusEffect effect) {
        this.apply(effect, 1.0F);
    }

    public void afterSync() {
        this.setType(this.type);
        this.controller.unit(this);
    }

    private void shoot(WeaponMount mount, float x, float y, float aimX, float aimY, float mountX, float mountY, float rotation, int side) {
        Weapon weapon = mount.weapon;
        float baseX = this.x;
        float baseY = this.y;
        boolean delay = weapon.firstShotDelay + weapon.shotDelay > 0.0F;
        (delay ? weapon.chargeSound : (weapon.continuous ? Sounds.none : weapon.shootSound)).at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
        BulletType ammo = weapon.bullet;
        float lifeScl = ammo.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y, aimX, aimY) / ammo.range()) : 1.0F;
        sequenceNum = 0;
        if (delay) {
            Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> {
                Time.run((float) sequenceNum * weapon.shotDelay + weapon.firstShotDelay, () -> {
                    if (this.isAdded()) {
                        mount.bullet = this.bullet(weapon, x + this.x - baseX, y + this.y - baseY, f + Mathf.range(weapon.inaccuracy), lifeScl);
                    }
                });
                ++sequenceNum;
            });
        } else {
            Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> {
                mount.bullet = this.bullet(weapon, x, y, f + Mathf.range(weapon.inaccuracy), lifeScl);
            });
        }

        boolean parentize = ammo.keepVelocity;
        if (delay) {
            Time.run(weapon.firstShotDelay, () -> {
                if (this.isAdded()) {
                    this.vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
                    Effect.shake(weapon.shake, weapon.shake, x, y);
                    mount.heat = 1.0F;
                    if (!weapon.continuous) {
                        weapon.shootSound.at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
                    }

                }
            });
        } else {
            this.vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
            Effect.shake(weapon.shake, weapon.shake, x, y);
            mount.heat = 1.0F;
        }

        weapon.ejectEffect.at(mountX, mountY, rotation * (float) side);
        ammo.shootEffect.at(x, y, rotation, parentize ? this : null);
        ammo.smokeEffect.at(x, y, rotation, parentize ? this : null);
        this.apply(weapon.shootStatus, weapon.shootStatusDuration);
    }

    public void move(float cx, float cy) {
        EntityCollisions.SolidPred check = this.solidity();
        if (check != null) {
            Vars.collisions.move(this, cx, cy, check);
        } else {
            this.x += cx;
            this.y += cy;
        }

    }

    public void collision(Hitboxc other, float x, float y) {
    }

    public void addItem(Item item, int amount) {
        this.stack.amount = this.stack.item == item ? this.stack.amount + amount : amount;
        this.stack.item = item;
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
    }

    public boolean isImmune(StatusEffect effect) {
        return this.type.immunities.contains(effect);
    }

    public Building closestCore() {
        return Vars.state.teams.closestCore(this.x, this.y, this.team);
    }

    public void setupWeapons(UnitType def) {
        this.mounts = new WeaponMount[def.weapons.size];

        for (int i = 0; i < this.mounts.length; ++i) {
            this.mounts[i] = new WeaponMount((Weapon) def.weapons.get(i));
        }

    }

    public boolean canMine(Item item) {
        return this.type.mineTier >= item.hardness;
    }

    public void approach(Vec2 vector) {
        if (!vector.isZero(0.09F)) {
            this.walked = true;
        }

        this.vel.approachDelta(vector, this.type.accel * this.realSpeed() * this.floorSpeedMultiplier());
    }

    public UnitController controller() {
        return this.controller;
    }

    public boolean hasWeapons() {
        return this.type.hasWeapons();
    }

    public double sense(Content content) {
        return content == this.stack().item ? (double) this.stack().amount : 0.0D;
    }

    public void aim(float x, float y) {
        Tmp.v1.set(x, y).sub(this.x, this.y);
        if (Tmp.v1.len() < this.type.aimDst) {
            Tmp.v1.setLength(this.type.aimDst);
        }

        x = Tmp.v1.x + this.x;
        y = Tmp.v1.y + this.y;
        WeaponMount[] var3 = this.mounts;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            WeaponMount mount = var3[var5];
            mount.aimX = x;
            mount.aimY = y;
        }

        this.aimX = x;
        this.aimY = y;
    }

    public <T extends Entityc> T self() {
        return (T) this;
    }

    private Bullet bullet(Weapon weapon, float x, float y, float angle, float lifescl) {
        float xr = Mathf.range(weapon.xRand);
        return weapon.bullet.create(this, this.team(), x + Angles.trnsx(angle, 0.0F, xr), y + Angles.trnsy(angle, 0.0F, xr), angle, 1.0F - weapon.velocityRnd + Mathf.random(weapon.velocityRnd), lifescl);
    }

    public boolean validMine(Tile tile) {
        return this.validMine(tile, true);
    }

    public void addBuild(BuildPlan place, boolean tail) {
        if (this.canBuild()) {
            BuildPlan replace = null;
            Iterator var4 = this.plans.iterator();

            while (var4.hasNext()) {
                BuildPlan request = (BuildPlan) var4.next();
                if (request.x == place.x && request.y == place.y) {
                    replace = request;
                    break;
                }
            }

            if (replace != null) {
                this.plans.remove(replace);
            }

            Tile tile = Vars.world.tile(place.x, place.y);
            if (tile != null) {
                Building var6 = tile.build;
                ConstructBlock.ConstructBuild cons;
                if (var6 instanceof ConstructBlock.ConstructBuild && (cons = (ConstructBlock.ConstructBuild) var6) == (ConstructBlock.ConstructBuild) var6) {
                    place.progress = cons.progress;
                }
            }

            if (tail) {
                this.plans.addLast(place);
            } else {
                this.plans.addFirst(place);
            }

        }
    }

    public boolean isNull() {
        return false;
    }

    public <T> T with(Cons<T> cons) {
        cons.get((T) this);
        return (T) this;
    }

    public void addBuild(BuildPlan place) {
        this.addBuild(place, true);
    }

    public void drawBuildPlans() {
        Iterator var1 = this.plans.iterator();

        while (true) {
            BuildPlan plan;
            do {
                do {
                    if (!var1.hasNext()) {
                        Draw.reset();
                        return;
                    }

                    plan = (BuildPlan) var1.next();
                } while (plan.progress > 0.01F);
            } while (this.buildPlan() == plan && plan.initialized && (this.within((float) (plan.x * 8), (float) (plan.y * 8), 220.0F) || Vars.state.isEditor()));

            this.drawPlan(plan, 1.0F);
        }
    }

    public void hitboxTile(Rect rect) {
        float size = Math.min(this.hitSize * 0.66F, 7.9F);
        rect.setCentered(this.x, this.y, size, size);
    }

    public int tileY() {
        return World.toTile(this.y);
    }

    public void hitbox(Rect rect) {
        rect.setCentered(this.x, this.y, this.hitSize, this.hitSize);
    }

    public int itemCapacity() {
        return this.type.itemCapacity;
    }

    public void controlWeapons(boolean rotateShoot) {
        this.controlWeapons(rotateShoot, rotateShoot);
    }

    public boolean isGrounded() {
        return this.elevation < 0.001F;
    }

    public void snapSync() {
        this.updateSpacing = 16L;
        this.lastUpdated = Time.millis();
        this.baseRotation_LAST_ = this.baseRotation_TARGET_;
        this.baseRotation = this.baseRotation_TARGET_;
        this.rotation_LAST_ = this.rotation_TARGET_;
        this.rotation = this.rotation_TARGET_;
        this.x_LAST_ = this.x_TARGET_;
        this.x = this.x_TARGET_;
        this.y_LAST_ = this.y_TARGET_;
        this.y = this.y_TARGET_;
    }

    public void healFract(float amount) {
        this.heal(amount * this.maxHealth);
    }

    public boolean canPass(int tileX, int tileY) {
        EntityCollisions.SolidPred s = this.solidity();
        return s == null || !s.solid(tileX, tileY);
    }

    public float floorSpeedMultiplier() {
        Floor on = !this.isFlying() && !this.hovering ? this.floorOn() : Blocks.air.asFloor();
        on = Blocks.air.asFloor();
        return on.speedMultiplier * this.speedMultiplier;
    }

    public void clearCommand() {
        Iterator var1 = this.controlling.iterator();

        while (var1.hasNext()) {
            Unit unit = (Unit) var1.next();
            if (unit.controller().isBeingControlled(this)) {
                unit.controller(unit.type.createController());
            }
        }

        this.controlling.clear();
        this.formation = null;
    }

    public void aimLook(float x, float y) {
        this.aim(x, y);
        this.lookAt(x, y);
    }

    public void trns(Position pos) {
        this.trns(pos.getX(), pos.getY());
    }

    public void impulseNet(Vec2 v) {
        this.impulse(v.x, v.y);
        if (this.isRemote()) {
            float mass = this.mass();
            this.move(v.x / mass, v.y / mass);
        }

    }

    public boolean canDrown() {
        return this.isGrounded() && !this.hovering && this.type.canDrown;
    }

    public boolean inRange(Position other) {
        return this.within(other, this.type.range);
    }

    public float healthf() {
        return this.health / this.maxHealth;
    }

    public void setType(UnitType type) {
        this.type = type;
        this.maxHealth = type.health;
        this.drag = type.drag;
        this.armor = type.armor;
        this.hitSize = type.hitSize;
        this.hovering = type.hovering;
        if (this.controller == null) {
            this.controller(type.createController());
        }

        if (this.mounts().length != type.weapons.size) {
            this.setupWeapons(type);
        }

        if (this.abilities.size != type.abilities.size) {
            this.abilities = type.abilities.map(Ability::copy);
        }

    }

    public void writeSync(Writes write) {
        write.f(this.ammo);
        write.f(this.armor);
        write.f(this.baseRotation);
        TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        TypeIO.writeTile(write, this.mineTile);
        TypeIO.writeMounts(write, this.mounts);
        write.i(this.plans.size);

        int INDEX;
        for (INDEX = 0; INDEX < this.plans.size; ++INDEX) {
            TypeIO.writeRequest(write, (BuildPlan) this.plans.get(INDEX));
        }

        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);

        for (INDEX = 0; INDEX < this.statuses.size; ++INDEX) {
            TypeIO.writeStatuse(write, (StatusEntry) this.statuses.get(INDEX));
        }

        TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        write.f(this.x);
        write.f(this.y);
    }

    public boolean moving() {
        return !this.vel.isZero(0.01F);
    }

    public void impulse(Vec2 v) {
        this.impulse(v.x, v.y);
    }

    public boolean collides(Hitboxc other) {
        return true;
    }

    public void lookAt(float x, float y) {
        this.lookAt(this.angleTo(x, y));
    }

    public void damageContinuous(float amount) {
        this.damage(amount * Time.delta, this.hitTime <= -1.0F);
    }

    public void readSyncManual(FloatBuffer buffer) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
        this.baseRotation_LAST_ = this.baseRotation;
        this.baseRotation_TARGET_ = buffer.get();
        this.rotation_LAST_ = this.rotation;
        this.rotation_TARGET_ = buffer.get();
        this.x_LAST_ = this.x;
        this.x_TARGET_ = buffer.get();
        this.y_LAST_ = this.y;
        this.y_TARGET_ = buffer.get();
    }

    public void removeBuild(int x, int y, boolean breaking) {
        int idx = this.plans.indexOf((req) -> {
            return req.breaking == breaking && req.x == x && req.y == y;
        });
        if (idx != -1) {
            this.plans.removeIndex(idx);
        }

    }

    public void command(Formation formation, Seq<Unit> units) {
        this.clearCommand();
        float spacing = this.hitSize * 0.8F;
        this.minFormationSpeed = this.type.speed;
        this.controlling.addAll(units);

        Iterator var4;
        Unit unit;
        for (var4 = units.iterator(); var4.hasNext(); this.minFormationSpeed = Math.min(this.minFormationSpeed, unit.type.speed)) {
            unit = (Unit) var4.next();
            FormationAI ai;
            unit.controller(ai = new FormationAI(this, formation));
            spacing = Math.max(spacing, ai.formationSize());
        }

        this.formation = formation;
        formation.pattern.spacing = spacing;
        members.clear();
        var4 = units.iterator();

        while (var4.hasNext()) {
            Unitc u = (Unitc) var4.next();
            members.add((FormationAI) u.controller());
        }

        formation.addMembers(members);
    }

    public void aim(Position pos) {
        this.aim(pos.getX(), pos.getY());
    }

    public boolean offloadImmediately() {
        return this.isPlayer();
    }

    public void kill() {
        if (!this.dead && !Vars.net.client()) {
            Call.unitDeath(this.id);
        }
    }

    public Building core() {
        return this.team.core();
    }

    public void lookAt(Position pos) {
        this.lookAt(this.angleTo(pos));
    }

    public void readSync(Reads read) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
        boolean islocal = this.isLocal();
        this.ammo = read.f();
        this.armor = read.f();
        if (!islocal) {
            this.baseRotation_LAST_ = this.baseRotation;
            this.baseRotation_TARGET_ = read.f();
        } else {
            read.f();
            this.baseRotation_LAST_ = this.baseRotation;
            this.baseRotation_TARGET_ = this.baseRotation;
        }

        this.controller = TypeIO.readController(read, this.controller);
        if (!islocal) {
            this.elevation = read.f();
        } else {
            read.f();
        }

        this.flag = read.d();
        this.health = read.f();
        this.isShooting = read.bool();
        if (!islocal) {
            this.mineTile = TypeIO.readTile(read);
        } else {
            TypeIO.readTile(read);
        }

        if (!islocal) {
            this.mounts = TypeIO.readMounts(read, this.mounts);
        } else {
            TypeIO.readMounts(read);
        }

        int statuses_LENGTH;
        int INDEX;
        if (!islocal) {
            statuses_LENGTH = read.i();
            this.plans.clear();

            for (INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
                BuildPlan plans_ITEM = TypeIO.readRequest(read);
                if (plans_ITEM != null) {
                    this.plans.add(plans_ITEM);
                }
            }
        } else {
            statuses_LENGTH = read.i();

            for (INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
                TypeIO.readRequest(read);
            }
        }

        if (!islocal) {
            this.rotation_LAST_ = this.rotation;
            this.rotation_TARGET_ = read.f();
        } else {
            read.f();
            this.rotation_LAST_ = this.rotation;
            this.rotation_TARGET_ = this.rotation;
        }

        this.shield = read.f();
        this.spawnedByCore = read.bool();
        this.stack = TypeIO.readItems(read, this.stack);
        statuses_LENGTH = read.i();
        this.statuses.clear();

        for (INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
            StatusEntry statuses_ITEM = TypeIO.readStatuse(read);
            if (statuses_ITEM != null) {
                this.statuses.add(statuses_ITEM);
            }
        }

        this.team = TypeIO.readTeam(read);
        this.type = (UnitType) Vars.content.getByID(ContentType.unit, read.s());
        if (!islocal) {
            this.updateBuilding = read.bool();
        } else {
            read.bool();
        }

        if (!islocal) {
            this.x_LAST_ = this.x;
            this.x_TARGET_ = read.f();
        } else {
            read.f();
            this.x_LAST_ = this.x;
            this.x_TARGET_ = this.x;
        }

        if (!islocal) {
            this.y_LAST_ = this.y;
            this.y_TARGET_ = read.f();
        } else {
            read.f();
            this.y_LAST_ = this.y;
            this.y_TARGET_ = this.y;
        }

        this.afterSync();
    }

    public void damagePierce(float amount) {
        this.damagePierce(amount, true);
    }

    public void unapply(StatusEffect effect) {
        this.statuses.remove((e) -> {
            if (e.effect == effect) {
                Pools.free(e);
                return true;
            } else {
                return false;
            }
        });
    }

    public void heal() {
        this.dead = false;
        this.health = this.maxHealth;
    }

    public void lookAt(float angle) {
        this.rotation = Angles.moveToward(this.rotation, angle, this.type.rotateSpeed * Time.delta * this.speedMultiplier());
    }

    public int tileX() {
        return World.toTile(this.x);
    }

    public boolean isPlayer() {
        return this.controller instanceof Player;
    }

    public Tile tileOn() {
        return Vars.world.tileWorld(this.x, this.y);
    }

    public void damageContinuousPierce(float amount) {
        this.damagePierce(amount * Time.delta, this.hitTime <= -11.0F);
    }

    public void impulse(float x, float y) {
        float mass = this.mass();
        this.vel.add(x / mass, y / mass);
    }

    public void drawPlan(BuildPlan request, float alpha) {
        request.animScale = 1.0F;
        if (request.breaking) {
            Vars.control.input.drawBreaking(request);
        } else {
            request.block.drawPlan(request, Vars.control.input.allRequests(), Build.validPlace(request.block, this.team, request.x, request.y, request.rotation) || Vars.control.input.requestMatches(request), alpha);
            Draw.reset();
            Draw.mixcol(Color.white, 0.24F + Mathf.absin(Time.globalTime, 6.0F, 0.28F));
            Draw.alpha(alpha);
            request.block.drawRequestConfigTop(request, this.plans);
        }

    }

    public float mass() {
        return this.hitSize * this.hitSize * 3.1415927F;
    }

    public void eachGroup(Cons<Unit> cons) {
        cons.get(this);
        this.controlling().each(cons);
    }

    public boolean isBoss() {
        return this.hasEffect(StatusEffects.boss);
    }

    public float range() {
        return this.type.range;
    }

    public boolean canBuild() {
        return this.type.buildSpeed > 0.0F;
    }

    public static StealthMechUnit create() {
        return new StealthMechUnit();
    }

    public int classId() {
        return classId;
    }

    public Team team() {
        return this.team;
    }

    public void team(Team team) {
        this.team = team;
    }

    public float mineTimer() {
        return this.mineTimer;
    }

    public void mineTimer(float mineTimer) {
        this.mineTimer = mineTimer;
    }

    public Tile mineTile() {
        return this.mineTile;
    }

    public void mineTile(Tile mineTile) {
        this.mineTile = mineTile;
    }

    public float rotation() {
        return this.rotation;
    }

    public void rotation(float rotation) {
        this.rotation = rotation;
    }

    public float baseRotation() {
        return this.baseRotation;
    }

    public void baseRotation(float baseRotation) {
        this.baseRotation = baseRotation;
    }

    public float walkTime() {
        return this.walkTime;
    }

    public void walkTime(float walkTime) {
        this.walkTime = walkTime;
    }

    public float walkExtension() {
        return this.walkExtension;
    }

    public void walkExtension(float walkExtension) {
        this.walkExtension = walkExtension;
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

    public long lastUpdated() {
        return this.lastUpdated;
    }

    public void lastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long updateSpacing() {
        return this.updateSpacing;
    }

    public void updateSpacing(long updateSpacing) {
        this.updateSpacing = updateSpacing;
    }

    public float health() {
        return this.health;
    }

    public void health(float health) {
        this.health = health;
    }

    public float hitTime() {
        return this.hitTime;
    }

    public void hitTime(float hitTime) {
        this.hitTime = hitTime;
    }

    public float maxHealth() {
        return this.maxHealth;
    }

    public void maxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean dead() {
        return this.dead;
    }

    public void dead(boolean dead) {
        this.dead = dead;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public void stack(ItemStack stack) {
        this.stack = stack;
    }

    public float itemTime() {
        return this.itemTime;
    }

    public void itemTime(float itemTime) {
        this.itemTime = itemTime;
    }

    public UnitType type() {
        return this.type;
    }

    public void type(UnitType type) {
        this.type = type;
    }

    public boolean spawnedByCore() {
        return this.spawnedByCore;
    }

    public void spawnedByCore(boolean spawnedByCore) {
        this.spawnedByCore = spawnedByCore;
    }

    public double flag() {
        return this.flag;
    }

    public void flag(double flag) {
        this.flag = flag;
    }

    public Seq<Ability> abilities() {
        return this.abilities;
    }

    public void abilities(Seq<Ability> abilities) {
        this.abilities = abilities;
    }

    public float lastX() {
        return this.lastX;
    }

    public void lastX(float lastX) {
        this.lastX = lastX;
    }

    public float lastY() {
        return this.lastY;
    }

    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public float deltaX() {
        return this.deltaX;
    }

    public void deltaX(float deltaX) {
        this.deltaX = deltaX;
    }

    public float deltaY() {
        return this.deltaY;
    }

    public void deltaY(float deltaY) {
        this.deltaY = deltaY;
    }

    public float hitSize() {
        return this.hitSize;
    }

    public void hitSize(float hitSize) {
        this.hitSize = hitSize;
    }

    public float speedMultiplier() {
        return this.speedMultiplier;
    }

    public float damageMultiplier() {
        return this.damageMultiplier;
    }

    public float healthMultiplier() {
        return this.healthMultiplier;
    }

    public float reloadMultiplier() {
        return this.reloadMultiplier;
    }

    public PhysicsProcess.PhysicRef physref() {
        return this.physref;
    }

    public void physref(PhysicsProcess.PhysicRef physref) {
        this.physref = physref;
    }

    public Vec2 vel() {
        return this.vel;
    }

    public float drag() {
        return this.drag;
    }

    public void drag(float drag) {
        this.drag = drag;
    }

    public Queue<BuildPlan> plans() {
        return this.plans;
    }

    public void plans(Queue<BuildPlan> plans) {
        this.plans = plans;
    }

    public boolean updateBuilding() {
        return this.updateBuilding;
    }

    public void updateBuilding(boolean updateBuilding) {
        this.updateBuilding = updateBuilding;
    }

    public float shield() {
        return this.shield;
    }

    public void shield(float shield) {
        this.shield = shield;
    }

    public float armor() {
        return this.armor;
    }

    public void armor(float armor) {
        this.armor = armor;
    }

    public float shieldAlpha() {
        return this.shieldAlpha;
    }

    public void shieldAlpha(float shieldAlpha) {
        this.shieldAlpha = shieldAlpha;
    }

    public float elevation() {
        return this.elevation;
    }

    public void elevation(float elevation) {
        this.elevation = elevation;
    }

    public boolean hovering() {
        return this.hovering;
    }

    public void hovering(boolean hovering) {
        this.hovering = hovering;
    }

    public float drownTime() {
        return this.drownTime;
    }

    public void drownTime(float drownTime) {
        this.drownTime = drownTime;
    }

    public float splashTimer() {
        return this.splashTimer;
    }

    public void splashTimer(float splashTimer) {
        this.splashTimer = splashTimer;
    }

    public WeaponMount[] mounts() {
        return this.mounts;
    }

    public void mounts(WeaponMount[] mounts) {
        this.mounts = mounts;
    }

    public boolean isRotate() {
        return this.isRotate;
    }

    public float aimX() {
        return this.aimX;
    }

    public void aimX(float aimX) {
        this.aimX = aimX;
    }

    public float aimY() {
        return this.aimY;
    }

    public void aimY(float aimY) {
        this.aimY = aimY;
    }

    public boolean isShooting() {
        return this.isShooting;
    }

    public void isShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public float ammo() {
        return this.ammo;
    }

    public void ammo(float ammo) {
        this.ammo = ammo;
    }

    public Formation formation() {
        return this.formation;
    }

    public void formation(Formation formation) {
        this.formation = formation;
    }

    public Seq<Unit> controlling() {
        return this.controlling;
    }

    public void controlling(Seq<Unit> controlling) {
        this.controlling = controlling;
    }

    public float minFormationSpeed() {
        return this.minFormationSpeed;
    }

    public void minFormationSpeed(float minFormationSpeed) {
        this.minFormationSpeed = minFormationSpeed;
    }

    public int id() {
        return this.id;
    }

    public void id(int id) {
        this.id = id;
    }
}
