package mindustryAddition.gen;


import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
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
import mindustry.ai.Pathfinder;
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
import mindustry.entities.EntityCollisions.SolidPred;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.*;
import mindustry.game.EventType.BuildSelectEvent;
import mindustry.game.EventType.Trigger;
import mindustry.game.EventType.UnitDestroyEvent;
import mindustry.game.EventType.UnitDrownEvent;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.logic.LAccess;
import mindustry.type.*;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.ConstructBlock.ConstructBuild;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.blocks.storage.CoreBlock;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;

import static mindustry.Vars.*;

public class CopyMechUnit extends Unit implements Itemsc, Minerc, ElevationMovec, Flyingc, Entityc, Statusc, Healthc, Commanderc, Hitboxc, Physicsc, Weaponsc, Builderc, Posc, Velc, Rotc, Shieldc, Drawc, Teamc, Boundedc, Mechc, Syncc, Unitc {
    public static final Vec2 tmp1 = new Vec2();
    public static final Vec2 tmp2 = new Vec2();
    public static final float hitDuration = 9.0F;
    public static final Seq<FormationMember> members = new Seq<>();
    public static final Seq<Unit> units = new Seq<>();
    public static final Vec2[] vecs = new Vec2[]{new Vec2(), new Vec2(), new Vec2(), new Vec2()};
    public static final float warpDst = 180.0F;
    public static int sequenceNum = 0;
    public transient boolean wasFlying;
    public transient boolean added;
    public Seq<StatusEntry> statuses = new Seq<>();
    public transient Bits applied = new Bits(content.getBy(ContentType.status).size);
    public transient BuildPlan lastActive;
    public transient int lastSize;
    public transient float buildAlpha = 0.0F;
    public float baseRotation;
    public transient float walkTime;
    public transient float walkExtension;
    public transient boolean walked;
    public UnitController controller;
    public transient float resupplyTime = Mathf.random(10.0F);
    public transient boolean wasPlayer;
    protected transient float speedMultiplier = 1;
    protected transient float damageMultiplier = 1;
    protected transient float healthMultiplier = 1;
    protected transient float reloadMultiplier = 1;
    protected transient boolean isRotate;
    private transient float x_TARGET_;
    private transient float x_LAST_;
    private transient float y_TARGET_;
    private transient float y_LAST_;
    private transient float rotation_TARGET_;
    private transient float rotation_LAST_;
    private transient float baseRotation_TARGET_;
    private transient float baseRotation_LAST_;

    protected CopyMechUnit() {
    }

    public static CopyMechUnit create() {
        return new CopyMechUnit();
    }

    public boolean serialize() {
        return true;
    }

    public void clearStatuses() {
        statuses.clear();
    }
    @Override
    public String toString() {
        return "MechUnit#" + id;
    }

    public boolean canMine() {
        return type.mineSpeed > 0 && type.mineTier >= 0;
    }

    public void drawBuildPlans() {
        for (BuildPlan plan : plans) {
            if (plan.progress > 0.01F || (buildPlan() == plan && plan.initialized && (within(plan.x * tilesize, plan.y * tilesize, buildingRange) || state.isEditor())))
                continue;
            drawPlan(plan, 1.0F);
        }
        Draw.reset();
    }

    public void damagePierce(float amount, boolean withEffect) {
        float pre = hitTime;
        rawDamage(amount);
        if (!withEffect) {
            hitTime = pre;
        }
    }

    public void clearBuilding() {
        plans.clear();
    }

    public float getY() {
        return y;
    }

    public void wobble() {
        x += Mathf.sin(Time.time + (id() % 10) * 12, 25.0F, 0.05F) * Time.delta * elevation;
        y += Mathf.cos(Time.time + (id() % 10) * 12, 25.0F, 0.05F) * Time.delta * elevation;
    }

    public void setupWeapons(UnitType def) {
        mounts = new WeaponMount[def.weapons.size];
        for (int i = 0; i < mounts.length; i++) {
            mounts[i] = new WeaponMount(def.weapons.get(i));
        }
    }

    public boolean isValid() {
        return !dead && isAdded();
    }

    public float realSpeed() {
        return Mathf.lerp(1.0F, type.canBoost ? type.boostMultiplier : 1.0F, elevation) * speed();
    }

    public float healthf() {
        return health / maxHealth;
    }

    public boolean onSolid() {
        Tile tile = tileOn();
        return tile == null || tile.solid();
    }

    public void moveAt(Vec2 vector, float acceleration) {
        flying:
        {
            Vec2 t = tmp1.set(vector);
            tmp2.set(t).sub(vel).limit(acceleration * vector.len() * Time.delta * floorSpeedMultiplier());
            vel.add(tmp2);
        }
        mech:
        {
            if (!vector.isZero()) {
                walked = true;
            }
        }
    }

    public boolean isImmune(StatusEffect effect) {
        return type.immunities.contains(effect);
    }

    public boolean isRemote() {
        return ((Object) this) instanceof Unitc && ((Unitc) ((Object) this)).isPlayer() && !isLocal();
    }

    public void lookAt(Position pos) {
        lookAt(angleTo(pos));
    }

    public void afterRead() {
        entity:
        {
        }
        hitbox:
        {
            updateLastPosition();
        }
        unit:
        {
            afterSync();
            controller(type.createController());
        }
    }

    public Building closestCore() {
        return state.teams.closestCore(x, y, team);
    }

    public void damageContinuous(float amount) {
        damage(amount * Time.delta, hitTime <= -10 + hitDuration);
    }

    public void addBuild(BuildPlan place) {
        addBuild(place, true);
    }

    public boolean isFlying() {
        return elevation >= 0.09F;
    }

    public boolean isLocal() {
        return ((Object) this) == player || ((Object) this) instanceof Unitc && ((Unitc) ((Object) this)).controller() == player;
    }

    public void set(UnitType def, UnitController controller) {
        if (this.type != def) {
            setType(def);
        }
        controller(controller);
    }

    public Building closestEnemyCore() {
        return state.teams.closestEnemyCore(x, y, team);
    }

    public void damage(float amount) {
        amount = Math.max(amount - armor, minArmorDamage * amount);
        amount /= healthMultiplier;
        rawDamage(amount);
    }

    private <T> T cast(Object o) {
        return (T) o;
    }

    public Object senseObject(LAccess sensor) {
        Payloadc pay;
        UnitPayload p1;
        BuildPayload p2;
        Object value=noSensed;
        switch (sensor) {
            case type :value= type;
            case name : value=controller instanceof Player ? ((Player) controller).name : null;
            case firstItem : value=stack().amount == 0 ? null : item();
            case payloadType : value=this instanceof Payloadc && (pay = (Payloadc) this) == this ? (pay.payloads().isEmpty() ? null :
                    pay.payloads().peek() instanceof UnitPayload && (p1 = cast(pay.payloads().peek())) == pay.payloads().peek() ? p1.unit.type :
                            pay.payloads().peek() instanceof BuildPayload && (p2 = cast(pay.payloads().peek())) == pay.payloads().peek() ? p2.block() : null) : null;
        };
        return value;
    }

    public boolean activelyBuilding() {
        if (isBuilding()) {
            if (!state.isEditor() && !within(buildPlan(), state.rules.infiniteResources ? Float.MAX_VALUE : buildingRange)) {
                return false;
            }
        }
        return isBuilding() && updateBuilding;
    }

    public void damage(float amount, boolean withEffect) {
        float pre = hitTime;
        damage(amount);
        if (!withEffect) {
            hitTime = pre;
        }
    }

    public boolean shouldSkip(BuildPlan request, Building core) {
        if (state.rules.infiniteResources || team.rules().infiniteResources || request.breaking || core == null || request.isRotation(team))
            return false;
        return (request.stuck && !core.items.has(request.block.requirements)) || (Structs.contains(request.block.requirements, (i) -> !core.items.has(i.item) && Mathf.round(i.amount * state.rules.buildCostMultiplier) > 0) && !request.initialized);
    }

    public boolean damaged() {
        return health < maxHealth - 0.001F;
    }

    public float clipSize() {
        if (isBuilding()) {
            return state.rules.infiniteResources ? Float.MAX_VALUE : Math.max(type.clipSize, type.region.width) + buildingRange + tilesize * 4.0F;
        }
        return Math.max(type.region.width * 2.0F, type.clipSize);
    }

    public void snapInterpolation() {
        updateSpacing = 16;
        lastUpdated = Time.millis();
        baseRotation_LAST_ = baseRotation;
        baseRotation_TARGET_ = baseRotation;
        rotation_LAST_ = rotation;
        rotation_TARGET_ = rotation;
        x_LAST_ = x;
        x_TARGET_ = x;
        y_LAST_ = y;
        y_TARGET_ = y;

    }

    public void aimLook(Position pos) {
        aim(pos);
        lookAt(pos);
    }

    public float getX() {
        return x;
    }

    public void set(Position pos) {
        set(pos.getX(), pos.getY());
    }

    public double sense(LAccess sensor) {
        double value=0;
        switch (sensor) {
            case totalItems :value= stack().amount;
            case itemCapacity :value= type.itemCapacity;
            case rotation : value=rotation;
            case health :value= health;
            case maxHealth : value=maxHealth;
            case ammo : value=!state.rules.unitAmmo ? type.ammoCapacity : ammo;
            case ammoCapacity :value= type.ammoCapacity;
            case x :value= World.conv(x);
            case y :value= World.conv(y);
            case team :value= team.id;
            case shooting :value= isShooting() ? 1 : 0;
            case shootX :value= World.conv(aimX());
            case shootY : value=World.conv(aimY());
            case mining : value=mining() ? 1 : 0;
            case mineX :value= mining() ? mineTile.x : -1;
            case mineY : value=mining() ? mineTile.y : -1;
            case flag : value=flag;
            case controlled :value= controller instanceof LogicAI || controller instanceof Player ? 1 : 0;
            case commanded : value=controller instanceof FormationAI ? 1 : 0;
            case payloadCount :value= this instanceof Payloadc ? this.<Payloadc>as().payloads().size : 0;
        };
        return value;
    }

    public void clampHealth() {
        health = Mathf.clamp(health, 0, maxHealth);
    }

    public void setWeaponRotation(float rotation) {
        for (WeaponMount mount : mounts) {
            mount.rotation = rotation;
        }
    }

    public TextureRegion icon() {
        return type.icon(Cicon.full);
    }

    public float deltaAngle() {
        return Mathf.angle(deltaX, deltaY);
    }

    public void killed() {
        health:
        {
        }
        commander:
        {
            clearCommand();
        }
        unit:
        {
            wasPlayer = isLocal();
            health = 0;
            dead = true;
            if (!type.flying) {
                destroy();
            }
        }
    }

    public void moveAt(Vec2 vector) {
        moveAt(vector, type.accel);
    }

    public void commandNearby(FormationPattern pattern) {
        commandNearby(pattern, (u) -> true);
    }

    public boolean isCommanding() {
        return formation != null;
    }

    public float deltaLen() {
        return Mathf.len(deltaX, deltaY);
    }

    public void draw() {
        miner:
        {
            if (!mining()) break miner;
            float focusLen = hitSize / 2.0F + Mathf.absin(Time.time, 1.1F, 0.5F);
            float swingScl = 12.0F;
            float swingMag = tilesize / 8.0F;
            float flashScl = 0.3F;
            float px = x + Angles.trnsx(rotation, focusLen);
            float py = y + Angles.trnsy(rotation, focusLen);
            float ex = mineTile.worldx() + Mathf.sin(Time.time + 48, swingScl, swingMag);
            float ey = mineTile.worldy() + Mathf.sin(Time.time + 48, swingScl + 2.0F, swingMag);
            Draw.z(Layer.flyingUnit + 0.1F);
            Draw.color(Color.lightGray, Color.white, 1.0F - flashScl + Mathf.absin(Time.time, 0.5F, flashScl));
            Drawf.laser(team(), Core.atlas.find("minelaser"), Core.atlas.find("minelaser-end"), px, py, ex, ey, 0.75F);
            if (isLocal()) {
                Lines.stroke(1.0F, Pal.accent);
                Lines.poly(mineTile.worldx(), mineTile.worldy(), 4, tilesize / 2.0F * Mathf.sqrt2, Time.time);
            }
            Draw.color();
        }
        status:
        {
            for (StatusEntry e : statuses) {
                e.effect.draw(this);
            }
        }
        builder:
        {
            boolean active = activelyBuilding();
            if (!active && lastActive == null) break builder;
            Draw.z(Layer.flyingUnit);
            BuildPlan plan = active ? buildPlan() : lastActive;
            Tile tile = world.tile(plan.x, plan.y);
            CoreBlock.CoreBuild core = team.core();
            if (tile == null || !within(plan, state.rules.infiniteResources ? Float.MAX_VALUE : buildingRange)) {
                break builder;
            }
            if (core != null && active && !isLocal() && !(tile.block() instanceof ConstructBlock)) {
                Draw.z(Layer.plans - 1.0F);
                drawPlan(plan, 0.5F);
                Draw.z(Layer.flyingUnit);
            }
            int size = plan.breaking ? active ? tile.block().size : lastSize : plan.block.size;
            float tx = plan.drawx();
            float ty = plan.drawy();
            Lines.stroke(1.0F, plan.breaking ? Pal.remove : Pal.accent);
            float focusLen = type.buildBeamOffset + Mathf.absin(Time.time, 3.0F, 0.6F);
            float px = x + Angles.trnsx(rotation, focusLen);
            float py = y + Angles.trnsy(rotation, focusLen);
            float sz = Vars.tilesize * size / 2.0F;
            float ang = angleTo(tx, ty);
            vecs[0].set(tx - sz, ty - sz);
            vecs[1].set(tx + sz, ty - sz);
            vecs[2].set(tx - sz, ty + sz);
            vecs[3].set(tx + sz, ty + sz);
            Arrays.sort(vecs, Structs.comparingFloat((vec) -> -Angles.angleDist(angleTo(vec), ang)));
            Vec2 close = Geometry.findClosest(x, y, vecs);
            float x1 = vecs[0].x;
            float y1 = vecs[0].y;
            float x2 = close.x;
            float y2 = close.y;
            float x3 = vecs[1].x;
            float y3 = vecs[1].y;
            Draw.z(Layer.buildBeam);
            Draw.alpha(buildAlpha);
            if (!active && !(tile.build instanceof ConstructBuild)) {
                Fill.square(plan.drawx(), plan.drawy(), size * tilesize / 2.0F);
            }
            if (renderer.animateShields) {
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
            Fill.square(px, py, 1.8F + Mathf.absin(Time.time, 2.2F, 1.1F), rotation + 45);
            Draw.reset();
            Draw.z(Layer.flyingUnit);
        }
        draw:
        {
        }
        unit:
        {
            type.draw(this);
        }
    }

    public float walkExtend(boolean scaled) {
        float raw = walkTime % (type.mechStride * 4);
        if (scaled) return raw / type.mechStride;
        if (raw > type.mechStride * 3) raw = raw - type.mechStride * 4;
        else if (raw > type.mechStride * 2) raw = type.mechStride * 2 - raw;
        else if (raw > type.mechStride) raw = type.mechStride * 2 - raw;
        return raw;
    }

    public void destroy() {
        if (!isAdded()) return;
        float explosiveness = 2.0F + item().explosiveness * stack().amount * 1.53F;
        float flammability = item().flammability * stack().amount / 1.9F;
        if (!spawnedByCore) {
            Damage.dynamicExplosion(x, y, flammability, explosiveness, 0.0F, bounds() / 2.0F, state.rules.damageExplosions, item().flammability > 1, team);
        }
        float shake = hitSize / 3.0F;
        Effect.scorch(x, y, (int) (hitSize / 5));
        Fx.explosion.at(this);
        Effect.shake(shake, shake, this);
        type.deathSound.at(this);
        Events.fire(new UnitDestroyEvent(this));
        if (explosiveness > 7.0F && (isLocal() || wasPlayer)) {
            Events.fire(Trigger.suicideBomb);
        }
        if (type.flying && !spawnedByCore) {
            Damage.damage(team, x, y, Mathf.pow(hitSize, 0.94F) * 1.25F, Mathf.pow(hitSize, 0.75F) * type.crashDamageMultiplier * 5.0F, true, false, true);
        }
        if (!headless) {
            for (int i = 0; i < type.wreckRegions.length; i++) {
                if (type.wreckRegions[i].found()) {
                    float range = type.hitSize / 4.0F;
                    Tmp.v1.rnd(range);
                    Effect.decal(type.wreckRegions[i], x + Tmp.v1.x, y + Tmp.v1.y, rotation - 90);
                }
            }
        }
        remove();
    }

    public boolean canDrown() {
        return isGrounded() && !hovering && type.canDrown;
    }

    public void landed() {
        flying:
        {
        }
        unit:
        {
            if (type.landShake > 0.0F) {
                Effect.shake(type.landShake, type.landShake, this);
            }
            type.landed(this);
        }
    }

    public Floor floorOn() {
        Tile tile = tileOn();
        return tile == null || tile.block() != Blocks.air ? (Floor) Blocks.air : tile.floor();
    }

    public Block blockOn() {
        Tile tile = tileOn();
        return tile == null ? Blocks.air : tile.block();
    }

    public int count() {
        return team.data().countType(type);
    }

    public boolean mining() {
        return mineTile != null && !this.activelyBuilding();
    }

    public boolean hasEffect(StatusEffect effect) {
        return applied.get(effect.id);
    }

    public boolean isAI() {
        return controller instanceof AIController;
    }

    public void write(Writes write) {
        write.s(5);
        write.f(this.ammo);
        write.f(this.armor);
        write.f(this.baseRotation);
        mindustry.io.TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        mindustry.io.TypeIO.writeTile(write, this.mineTile);
        mindustry.io.TypeIO.writeMounts(write, this.mounts);
        write.i(this.plans.size);
        for (int INDEX = 0; INDEX < this.plans.size; INDEX++) {
            mindustry.io.TypeIO.writeRequest(write, this.plans.get(INDEX));
        }
        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        mindustry.io.TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);
        for (int INDEX = 0; INDEX < this.statuses.size; INDEX++) {
            mindustry.io.TypeIO.writeStatuse(write, this.statuses.get(INDEX));
        }
        mindustry.io.TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        write.f(this.x);
        write.f(this.y);
    }

    public float ammof() {
        return ammo / type.ammoCapacity;
    }

    public void command(Formation formation, Seq<Unit> units) {
        clearCommand();
        float spacing = hitSize * 0.8F;
        minFormationSpeed = type.speed;
        controlling.addAll(units);
        for (Unit unit : units) {
            FormationAI ai;
            unit.controller(ai = new FormationAI(this, formation));
            spacing = Math.max(spacing, ai.formationSize());
            minFormationSpeed = Math.min(minFormationSpeed, unit.type.speed);
        }
        this.formation = formation;
        formation.pattern.spacing = spacing;
        members.clear();
        for (Unitc u : units) {
            members.add((FormationAI) u.controller());
        }
        formation.addMembers(members);
    }

    public void apply(StatusEffect effect, float duration) {
        if (effect == StatusEffects.none || effect == null || isImmune(effect)) return;
        if (statuses.size > 0) {
            for (int i = 0; i < statuses.size; i++) {
                StatusEntry entry = statuses.get(i);
                if (entry.effect == effect) {
                    entry.time = Math.max(entry.time, duration);
                    return;
                } else if (entry.effect.reactsWith(effect)) {
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
        StatusEntry entry = Pools.obtain(StatusEntry.class, StatusEntry::new);
        entry.set(effect, duration);
        statuses.add(entry);
    }

    public void add() {
        if (added == true) return;
        Groups.all.add(this);
        Groups.unit.add(this);
        Groups.sync.add(this);
        Groups.draw.add(this);
        entity:
        {
            added = true;
        }
        hitbox:
        {
            updateLastPosition();
        }
        unit:
        {
            team.data().updateCount(type, 1);
            if (count() > cap() && !spawnedByCore && !dead && !state.rules.editor) {
                Call.unitCapDeath(this);
                team.data().updateCount(type, -1);
            }
        }
    }

    public void interpolate() {
        if (lastUpdated != 0 && updateSpacing != 0) {
            float timeSinceUpdate = Time.timeSinceMillis(lastUpdated);
            float alpha = Math.min(timeSinceUpdate / updateSpacing, 2f);
            baseRotation = (Mathf.slerp(baseRotation_LAST_, baseRotation_TARGET_, alpha));
            rotation = (Mathf.slerp(rotation_LAST_, rotation_TARGET_, alpha));
            x = (Mathf.lerp(x_LAST_, x_TARGET_, alpha));
            y = (Mathf.lerp(y_LAST_, y_TARGET_, alpha));
        } else if (lastUpdated != 0) {
            baseRotation = baseRotation_TARGET_;
            rotation = rotation_TARGET_;
            x = x_TARGET_;
            y = y_TARGET_;
        }

    }

    public EntityCollisions.SolidPred solidity() {
        return isFlying() ? null : EntityCollisions::solid;
    }

    public String getControllerName() {
        if (isPlayer()) return getPlayer().name;
        if (controller instanceof LogicAI && this.<LogicAI>cast(controller).controller != null)
            return this.<LogicAI>cast(controller).controller.lastAccessed;
        FormationAI ai;
        if (controller instanceof FormationAI && (ai = cast(controller)) == controller && ai.leader != null && ai.leader.isPlayer())
            return ai.leader.getPlayer().name;
        return null;
    }

    public void writeSyncManual(FloatBuffer buffer) {
        buffer.put(this.baseRotation);
        buffer.put(this.rotation);
        buffer.put(this.x);
        buffer.put(this.y);
    }

    public void controlWeapons(boolean rotateShoot) {
        controlWeapons(rotateShoot, rotateShoot);
    }

    public int maxAccepted(Item item) {
        return stack.item != item && stack.amount > 0 ? 0 : itemCapacity() - stack.amount;
    }

    public void read(Reads read) {
        short REV = read.s();
        if (REV == 0) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = mindustry.io.TypeIO.readController(read, this.controller);
            read.bool();
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();
            for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
                mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
                if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
            }
            this.team = mindustry.io.TypeIO.readTeam(read);
            this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 1) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = mindustry.io.TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();
            for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
                mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
                if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
            }
            this.team = mindustry.io.TypeIO.readTeam(read);
            this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 2) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = mindustry.io.TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();
            for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
                mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
                if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
            }
            this.team = mindustry.io.TypeIO.readTeam(read);
            this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 3) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = mindustry.io.TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = mindustry.io.TypeIO.readTile(read);
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();
            for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
                mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
                if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
            }
            this.team = mindustry.io.TypeIO.readTeam(read);
            this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 4) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = mindustry.io.TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = mindustry.io.TypeIO.readTile(read);
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
            int plans_LENGTH = read.i();
            this.plans.clear();
            for (int INDEX = 0; INDEX < plans_LENGTH; INDEX++) {
                mindustry.entities.units.BuildPlan plans_ITEM = mindustry.io.TypeIO.readRequest(read);
                if (plans_ITEM != null) this.plans.add(plans_ITEM);
            }
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();
            for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
                mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
                if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
            }
            this.team = mindustry.io.TypeIO.readTeam(read);
            this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 5) {
            this.ammo = read.f();
            this.armor = read.f();
            this.baseRotation = read.f();
            this.controller = mindustry.io.TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = mindustry.io.TypeIO.readTile(read);
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
            int plans_LENGTH = read.i();
            this.plans.clear();
            for (int INDEX = 0; INDEX < plans_LENGTH; INDEX++) {
                mindustry.entities.units.BuildPlan plans_ITEM = mindustry.io.TypeIO.readRequest(read);
                if (plans_ITEM != null) this.plans.add(plans_ITEM);
            }
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();
            for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
                mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
                if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
            }
            this.team = mindustry.io.TypeIO.readTeam(read);
            this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
            this.updateBuilding = read.bool();
            this.x = read.f();
            this.y = read.f();
        } else {
            throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'mace'");
        }

        afterRead();
    }

    public double sense(Content content) {
        if (content == stack().item) return stack().amount;
        return 0;
    }

    public void move(float cx, float cy) {
        SolidPred check = solidity();
        if (check != null) {
            collisions.move(this, cx, cy, check);
        } else {
            x += cx;
            y += cy;
        }
    }

    public boolean canShoot() {
        return !(type.canBoost && isFlying());
    }

    public Color statusColor() {
        if (statuses.size == 0) {
            return Tmp.c1.set(Color.white);
        }
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        float total = 0.0F;
        for (StatusEntry entry : statuses) {
            float intensity = entry.time < 10.0F ? entry.time / 10.0F : 1.0F;
            r += entry.effect.color.r * intensity;
            g += entry.effect.color.g * intensity;
            b += entry.effect.color.b * intensity;
            total += intensity;
        }
        float count = statuses.size + total;
        return Tmp.c1.set(r / count, g / count, b / count, 1.0F);
    }

    public <T extends Entityc> T self() {
        return (T) this;
    }

    public boolean acceptsItem(Item item) {
        return !hasItem() || item == stack.item && stack.amount + 1 <= itemCapacity();
    }

    public float bounds() {
        return hitSize * 2.0F;
    }

    public boolean isAdded() {
        return added;
    }

    public void addItem(Item item) {
        addItem(item, 1);
    }

    public void controlWeapons(boolean rotate, boolean shoot) {
        for (WeaponMount mount : mounts) {
            mount.rotate = rotate;
            mount.shoot = shoot;
        }
        isRotate = rotate;
        isShooting = shoot;
    }

    public void hitboxTile(Rect rect) {
        float size = Math.min(hitSize * 0.66F, 7.9F);
        rect.setCentered(x, y, size, size);
    }

    public int cap() {
        return Units.getCap(team);
    }

    public boolean isBuilding() {
        return plans.size != 0;
    }

    public float mass() {
        return hitSize * hitSize * Mathf.pi;
    }

    public float prefRotation() {
        if (activelyBuilding()) {
            return angleTo(buildPlan());
        } else if (mineTile != null) {
            return angleTo(mineTile);
        } else if (moving()) {
            return vel().angle();
        }
        return rotation;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean cheating() {
        return team.rules().cheat;
    }

    public void remove() {
        if (added == false) return;
        Groups.all.remove(this);
        Groups.unit.remove(this);
        Groups.sync.remove(this);
        Groups.draw.remove(this);
        entity:
        {
            added = false;
        }
        commander:
        {
            clearCommand();
        }
        weapons:
        {
            for (WeaponMount mount : mounts) {
                if (mount.bullet != null) {
                    mount.bullet.time = mount.bullet.lifetime - 10.0F;
                    mount.bullet = null;
                }
                if (mount.sound != null) {
                    mount.sound.stop();
                }
            }
        }
        sync:
        {
            if (Vars.net.client()) {
                Vars.netClient.addRemovedEntity(id());
            }
        }
        unit:
        {
            team.data().updateCount(type, -1);
            controller.removed(this);
        }
    }

    public void getCollisions(Cons<QuadTree> consumer) {
    }

    public float speed() {
        float strafePenalty = isGrounded() || !isPlayer() ? 1.0F : Mathf.lerp(1.0F, type.strafePenalty, Angles.angleDist(vel().angle(), rotation) / 180.0F);
        return (isCommanding() ? minFormationSpeed * 0.98F : type.speed) * strafePenalty;
    }

    public Tile tileOn() {
        return world.tileWorld(x, y);
    }

    public void clearItem() {
        stack.amount = 0;
    }

    public void resetController() {
        controller(type.createController());
    }

    public void heal(float amount) {
        health += amount;
        clampHealth();
    }

    public boolean hasItem() {
        return stack.amount > 0;
    }

    public void apply(StatusEffect effect) {
        apply(effect, 1);
    }

    public void afterSync() {
        sync:
        {
        }
        unit:
        {
            setType(this.type);
            controller.unit(this);
        }
    }

    public boolean isNull() {
        return false;
    }

    public boolean checkTarget(boolean targetAir, boolean targetGround) {
        return (isGrounded() && targetGround) || (isFlying() && targetAir);
    }

    public void collision(Hitboxc other, float x, float y) {
    }

    public void addItem(Item item, int amount) {
        stack.amount = stack.item == item ? stack.amount + amount : amount;
        stack.item = item;
        stack.amount = Mathf.clamp(stack.amount, 0, itemCapacity());
    }

    private void shoot(WeaponMount mount, float x, float y, float aimX, float aimY, float mountX,
                       float mountY, float rotation, int side) {
        Weapon weapon = mount.weapon;
        float baseX = this.x;
        float baseY = this.y;
        boolean delay = weapon.firstShotDelay + weapon.shotDelay > 0.0F;
        (delay ? weapon.chargeSound : weapon.continuous ? Sounds.none : weapon.shootSound).at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
        BulletType ammo = weapon.bullet;
        float lifeScl = ammo.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y, aimX, aimY) / ammo.range()) : 1.0F;
        sequenceNum = 0;
        if (delay) {
            Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> {
                Time.run(sequenceNum * weapon.shotDelay + weapon.firstShotDelay, () -> {
                    if (!isAdded()) return;
                    mount.bullet = bullet(weapon, x + this.x - baseX, y + this.y - baseY, f + Mathf.range(weapon.inaccuracy), lifeScl);
                });
                sequenceNum++;
            });
        } else {
            Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> mount.bullet = bullet(weapon, x, y, f + Mathf.range(weapon.inaccuracy), lifeScl));
        }
        boolean parentize = ammo.keepVelocity;
        if (delay) {
            Time.run(weapon.firstShotDelay, () -> {
                if (!isAdded()) return;
                vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
                Effect.shake(weapon.shake, weapon.shake, x, y);
                mount.heat = 1.0F;
                if (!weapon.continuous) {
                    weapon.shootSound.at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
                }
            });
        } else {
            vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
            Effect.shake(weapon.shake, weapon.shake, x, y);
            mount.heat = 1.0F;
        }
        weapon.ejectEffect.at(mountX, mountY, rotation * side);
        ammo.shootEffect.at(x, y, rotation, parentize ? this : null);
        ammo.smokeEffect.at(x, y, rotation, parentize ? this : null);
        apply(weapon.shootStatus, weapon.shootStatusDuration);
    }

    public void approach(Vec2 vector) {
        mech:
        {
            if (!vector.isZero(0.09F)) {
                walked = true;
            }
        }
        unit:
        {
            vel.approachDelta(vector, type.accel * realSpeed() * floorSpeedMultiplier());
        }
    }

    public UnitController controller() {
        return controller;
    }

    public boolean hasWeapons() {
        return type.hasWeapons();
    }

    public void aim(float x, float y) {
        Tmp.v1.set(x, y).sub(this.x, this.y);
        if (Tmp.v1.len() < type.aimDst) Tmp.v1.setLength(type.aimDst);
        x = Tmp.v1.x + this.x;
        y = Tmp.v1.y + this.y;
        for (WeaponMount mount : mounts) {
            mount.aimX = x;
            mount.aimY = y;
        }
        aimX = x;
        aimY = y;
    }

    public void snapSync() {
        updateSpacing = 16;
        lastUpdated = Time.millis();
        baseRotation_LAST_ = baseRotation_TARGET_;
        baseRotation = baseRotation_TARGET_;
        rotation_LAST_ = rotation_TARGET_;
        rotation = rotation_TARGET_;
        x_LAST_ = x_TARGET_;
        x = x_TARGET_;
        y_LAST_ = y_TARGET_;
        y = y_TARGET_;

    }

    private Bullet bullet(Weapon weapon, float x, float y, float angle, float lifescl) {
        float xr = Mathf.range(weapon.xRand);
        return weapon.bullet.create(this, team(), x + Angles.trnsx(angle, 0, xr), y + Angles.trnsy(angle, 0, xr), angle, (1.0F - weapon.velocityRnd) + Mathf.random(weapon.velocityRnd), lifescl);
    }

    public boolean validMine(Tile tile) {
        return validMine(tile, true);
    }

    public void addBuild(BuildPlan place, boolean tail) {
        if (!canBuild()) return;
        BuildPlan replace = null;
        for (BuildPlan request : plans) {
            if (request.x == place.x && request.y == place.y) {
                replace = request;
                break;
            }
        }
        if (replace != null) {
            plans.remove(replace);
        }
        Tile tile = world.tile(place.x, place.y);
        if (tile != null && tile.build instanceof ConstructBuild) {
            place.progress = this.<ConstructBuild>cast(tile.build).progress;
        }
        if (tail) {
            plans.addLast(place);
        } else {
            plans.addFirst(place);
        }
    }

    public <T> T with(Cons<T> cons) {
        cons.get((T) this);
        return (T) this;
    }

    public boolean validMine(Tile tile, boolean checkDst) {
        return !(tile == null || tile.block() != Blocks.air || (!within(tile.worldx(), tile.worldy(), miningRange) && checkDst) || tile.drop() == null || !canMine(tile.drop()));
    }

    public boolean offloadImmediately() {
        return this.isPlayer();
    }

    public int tileY() {
        return World.toTile(y);
    }

    public void hitbox(Rect rect) {
        rect.setCentered(x, y, hitSize, hitSize);
    }

    public int itemCapacity() {
        return type.itemCapacity;
    }

    public void lookAt(float angle) {
        rotation = Angles.moveToward(rotation, angle, type.rotateSpeed * Time.delta * speedMultiplier());
    }

    public boolean isGrounded() {
        return elevation < 0.001F;
    }

    public Item item() {
        return stack.item;
    }

    public int pathType() {
        return Pathfinder.costGround;
    }

    public boolean isBoss() {
        return hasEffect(StatusEffects.boss);
    }

    public void healFract(float amount) {
        heal(amount * maxHealth);
    }

    public boolean canPass(int tileX, int tileY) {
        SolidPred s = solidity();
        return s == null || !s.solid(tileX, tileY);
    }

    public float floorSpeedMultiplier() {
        Floor on = isFlying() || hovering ? Blocks.air.asFloor() : floorOn();
        return on.speedMultiplier * speedMultiplier;
    }

    public void clearCommand() {
        for (Unit unit : controlling) {
            if (unit.controller().isBeingControlled(this)) {
                unit.controller(unit.type.createController());
            }
        }
        controlling.clear();
        formation = null;
    }

    public boolean canPassOn() {

        return canPass(tileX(), tileY());
    }

    public void trns(Position pos) {

        trns(pos.getX(), pos.getY());
    }

    public void impulseNet(Vec2 v) {

        impulse(v.x, v.y);
        if (isRemote()) {
            float mass = mass();
            move(v.x / mass, v.y / mass);
        }
    }

    public boolean inRange(Position other) {
        return within(other, type.range);
    }

    public void setType(UnitType type) {
        this.type = type;
        this.maxHealth = type.health;
        this.drag = type.drag;
        this.armor = type.armor;
        this.hitSize = type.hitSize;
        this.hovering = type.hovering;
        if (controller == null) controller(type.createController());
        if (mounts().length != type.weapons.size) setupWeapons(type);
        if (abilities.size != type.abilities.size) {
            abilities = type.abilities.map(Ability::copy);
        }
    }

    public void writeSync(Writes write) {
        write.f(this.ammo);
        write.f(this.armor);
        write.f(this.baseRotation);
        mindustry.io.TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        mindustry.io.TypeIO.writeTile(write, this.mineTile);
        mindustry.io.TypeIO.writeMounts(write, this.mounts);
        write.i(this.plans.size);
        for (int INDEX = 0; INDEX < this.plans.size; INDEX++) {
            mindustry.io.TypeIO.writeRequest(write, this.plans.get(INDEX));
        }
        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        mindustry.io.TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);
        for (int INDEX = 0; INDEX < this.statuses.size; INDEX++) {
            mindustry.io.TypeIO.writeStatuse(write, this.statuses.get(INDEX));
        }
        mindustry.io.TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        write.f(this.x);
        write.f(this.y);

    }

    public boolean moving() {
        return !vel.isZero(0.01F);
    }

    public void impulse(Vec2 v) {
        impulse(v.x, v.y);
    }

    public void update() {
        vel:
        {
            move(vel.x * Time.delta, vel.y * Time.delta);
            vel.scl(Mathf.clamp(1.0F - drag * Time.delta));
        }
        items:
        {
            stack.amount = Mathf.clamp(stack.amount, 0, itemCapacity());
            itemTime = Mathf.lerpDelta(itemTime, Mathf.num(hasItem()), 0.05F);
        }
        miner:
        {
            Building core = closestCore();
            if (core != null && mineTile != null && mineTile.drop() != null && !acceptsItem(mineTile.drop()) && within(core, mineTransferRange) && !offloadImmediately()) {
                int accepted = core.acceptStack(item(), stack().amount, this);
                if (accepted > 0) {
                    Call.transferItemTo(this, item(), accepted, mineTile.worldx() + Mathf.range(tilesize / 2.0F), mineTile.worldy() + Mathf.range(tilesize / 2.0F), core);
                    clearItem();
                }
            }
            if (!validMine(mineTile)) {
                mineTile = null;
                mineTimer = 0.0F;
            } else if (mining()) {
                Item item = mineTile.drop();
                mineTimer += Time.delta * type.mineSpeed;
                if (Mathf.chance(0.06 * Time.delta)) {
                    Fx.pulverizeSmall.at(mineTile.worldx() + Mathf.range(tilesize / 2.0F), mineTile.worldy() + Mathf.range(tilesize / 2.0F), 0.0F, item.color);
                }
                if (mineTimer >= 50.0F + item.hardness * 15.0F) {
                    mineTimer = 0;
                    if (state.rules.sector != null && team() == state.rules.defaultTeam)
                        state.rules.sector.info.handleProduction(item, 1);
                    if (core != null && within(core, mineTransferRange) && core.acceptStack(item, 1, this) == 1 && offloadImmediately()) {
                        if (item() == item && !net.client()) addItem(item);
                        Call.transferItemTo(this, item, 1, mineTile.worldx() + Mathf.range(tilesize / 2.0F), mineTile.worldy() + Mathf.range(tilesize / 2.0F), core);
                    } else if (acceptsItem(item)) {
                        InputHandler.transferItemToUnit(item, mineTile.worldx() + Mathf.range(tilesize / 2.0F), mineTile.worldy() + Mathf.range(tilesize / 2.0F), this);
                    } else {
                        mineTile = null;
                        mineTimer = 0.0F;
                    }
                }
                if (!headless) {
                    control.sound.loop(type.mineSound, this, type.mineSoundVolume);
                }
            }
        }
        flying:
        {
            Floor floor = floorOn();
            if (isFlying() != wasFlying) {
                if (wasFlying) {
                    if (tileOn() != null) {
                        Fx.unitLand.at(x, y, floorOn().isLiquid ? 1.0F : 0.5F, tileOn().floor().mapColor);
                    }
                }
                wasFlying = isFlying();
            }
            if (!hovering && isGrounded()) {
                if ((splashTimer += Mathf.dst(deltaX(), deltaY())) >= (7.0F + hitSize() / 8.0F)) {
                    floor.walkEffect.at(x, y, hitSize() / 8.0F, floor.mapColor);
                    splashTimer = 0.0F;
                    if (!(this instanceof WaterMovec)) {
                        floor.walkSound.at(x, y, Mathf.random(floor.walkSoundPitchMin, floor.walkSoundPitchMax), floor.walkSoundVolume);
                    }
                }
            }
            if (canDrown() && floor.isLiquid && floor.drownTime > 0) {
                drownTime += Time.delta / floor.drownTime;
                drownTime = Mathf.clamp(drownTime);
                if (Mathf.chanceDelta(0.05F)) {
                    floor.drownUpdateEffect.at(x, y, 1.0F, floor.mapColor);
                }
                if (drownTime >= 0.999F && !net.client()) {
                    kill();
                    Events.fire(new UnitDrownEvent(this));
                }
            } else {
                drownTime = Mathf.lerpDelta(drownTime, 0.0F, 0.03F);
            }
        }
        entity:
        {
        }
        status:
        {
            Floor floor = floorOn();
            if (isGrounded() && !type.hovering) {
                apply(floor.status, floor.statusDuration);
            }
            applied.clear();
            speedMultiplier = damageMultiplier = healthMultiplier = reloadMultiplier = 1.0F;
            if (statuses.isEmpty()) break status;
            int index = 0;
            while (index < statuses.size) {
                StatusEntry entry = statuses.get(index++);
                entry.time = Math.max(entry.time - Time.delta, 0);
                if (entry.effect == null || (entry.time <= 0 && !entry.effect.permanent)) {
                    Pools.free(entry);
                    index--;
                    statuses.remove(index);
                } else {
                    applied.set(entry.effect.id);
                    speedMultiplier *= entry.effect.speedMultiplier;
                    healthMultiplier *= entry.effect.healthMultiplier;
                    damageMultiplier *= entry.effect.damageMultiplier;
                    reloadMultiplier *= entry.effect.reloadMultiplier;
                    entry.effect.update(this, entry.time);
                }
            }
        }
        health:
        {
            hitTime -= Time.delta / hitDuration;
        }
        commander:
        {
            if (controlling.isEmpty()) {
                formation = null;
            }
            if (formation != null) {
                formation.anchor.set(x, y, 0);
                formation.updateSlots();
                controlling.removeAll((u) -> u.dead || !(u.controller() instanceof FormationAI && this.<FormationAI>cast(u.controller()).leader == this));
            }
        }
        hitbox:
        {
        }
        weapons:
        {
            boolean can = canShoot();
            for (WeaponMount mount : mounts) {
                Weapon weapon = mount.weapon;
                mount.reload = Math.max(mount.reload - Time.delta * reloadMultiplier, 0);
                float weaponRotation = this.rotation - 90 + (weapon.rotate ? mount.rotation : 0);
                float mountX = this.x + Angles.trnsx(this.rotation - 90, weapon.x, weapon.y);
                float mountY = this.y + Angles.trnsy(this.rotation - 90, weapon.x, weapon.y);
                float shootX = mountX + Angles.trnsx(weaponRotation, weapon.shootX, weapon.shootY);
                float shootY = mountY + Angles.trnsy(weaponRotation, weapon.shootX, weapon.shootY);
                float shootAngle = weapon.rotate ? weaponRotation + 90 : Angles.angle(shootX, shootY, mount.aimX, mount.aimY) + (this.rotation - angleTo(mount.aimX, mount.aimY));
                if (weapon.continuous && mount.bullet != null) {
                    if (!mount.bullet.isAdded() || mount.bullet.time >= mount.bullet.lifetime || mount.bullet.type != weapon.bullet) {
                        mount.bullet = null;
                    } else {
                        mount.bullet.rotation(weaponRotation + 90);
                        mount.bullet.set(shootX, shootY);
                        mount.reload = weapon.reload;
                        vel.add(Tmp.v1.trns(rotation + 180.0F, mount.bullet.type.recoil));
                        if (weapon.shootSound != Sounds.none && !headless) {
                            if (mount.sound == null) mount.sound = new SoundLoop(weapon.shootSound, 1.0F);
                            mount.sound.update(x, y, true);
                        }
                    }
                } else {
                    mount.heat = Math.max(mount.heat - Time.delta * reloadMultiplier / mount.weapon.cooldownTime, 0);
                    if (mount.sound != null) {
                        mount.sound.update(x, y, false);
                    }
                }
                if (weapon.otherSide != -1 && weapon.alternate && mount.side == weapon.flipSprite && mount.reload + Time.delta * reloadMultiplier > weapon.reload / 2.0F && mount.reload <= weapon.reload / 2.0F) {
                    mounts[weapon.otherSide].side = !mounts[weapon.otherSide].side;
                    mount.side = !mount.side;
                }
                if (weapon.rotate && (mount.rotate || mount.shoot) && can) {
                    float axisX = this.x + Angles.trnsx(this.rotation - 90, weapon.x, weapon.y);
                    float axisY = this.y + Angles.trnsy(this.rotation - 90, weapon.x, weapon.y);
                    mount.targetRotation = Angles.angle(axisX, axisY, mount.aimX, mount.aimY) - this.rotation;
                    mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, weapon.rotateSpeed * Time.delta);
                } else if (!weapon.rotate) {
                    mount.rotation = 0;
                    mount.targetRotation = angleTo(mount.aimX, mount.aimY);
                }
                if (mount.shoot && can && (ammo > 0 || !state.rules.unitAmmo || team().rules().infiniteAmmo) && (!weapon.alternate || mount.side == weapon.flipSprite) && (vel.len() >= mount.weapon.minShootVelocity || (net.active() && !isLocal())) && mount.reload <= 1.0E-4F && Angles.within(weapon.rotate ? mount.rotation : this.rotation, mount.targetRotation, mount.weapon.shootCone)) {
                    shoot(mount, shootX, shootY, mount.aimX, mount.aimY, mountX, mountY, shootAngle, Mathf.sign(weapon.x));
                    mount.reload = weapon.reload;
                    ammo--;
                    if (ammo < 0) ammo = 0;
                }
            }
        }
        builder:
        {

            if (!headless) {
                if (lastActive != null && buildAlpha <= 0.01F) {
                    lastActive = null;
                }
                buildAlpha = Mathf.lerpDelta(buildAlpha, activelyBuilding() ? 1.0F : 0.0F, 0.15F);
            }
            if (!updateBuilding || !canBuild()) break builder;
            float finalPlaceDst = state.rules.infiniteResources ? Float.MAX_VALUE : buildingRange;
            boolean infinite = state.rules.infiniteResources || team().rules().infiniteResources;
            Iterator<BuildPlan> it = plans.iterator();
            while (it.hasNext()) {
                BuildPlan req = it.next();
                Tile tile = world.tile(req.x, req.y);
                if (tile == null || (req.breaking && tile.block() == Blocks.air) || (!req.breaking && ((tile.build != null && tile.build.rotation == req.rotation) || !req.block.rotate) && tile.block() == req.block)) {
                    it.remove();
                }
            }
            Building core = core();
            if (buildPlan() == null) break builder;
            if (plans.size > 1) {
                int total = 0;
                BuildPlan req;
                while ((!within((req = buildPlan()).tile(), finalPlaceDst) || shouldSkip(req, core)) && total < plans.size) {
                    plans.removeFirst();
                    plans.addLast(req);
                    total++;
                }
            }
            BuildPlan current = buildPlan();
            Tile tile = current.tile();
            lastActive = current;
            buildAlpha = 1.0F;
            if (current.breaking) lastSize = tile.block().size;
            if (!within(tile, finalPlaceDst)) break builder;
            ConstructBuild cb;
            if (!(tile.build instanceof ConstructBuild)) {
                if (!current.initialized && !current.breaking && Build.validPlace(current.block, team, current.x, current.y, current.rotation)) {
                    boolean hasAll = infinite || current.isRotation(team) || !Structs.contains(current.block.requirements, (i) -> core != null && !core.items.has(i.item));
                    if (hasAll) {
                        Call.beginPlace(this, current.block, team, current.x, current.y, current.rotation);
                    } else {
                        current.stuck = true;
                    }
                } else if (!current.initialized && current.breaking && Build.validBreak(team, current.x, current.y)) {
                    Call.beginBreak(this, team, current.x, current.y);
                } else {
                    plans.removeFirst();
                    break builder;
                }
            } else if ((cb = cast(tile.build)) == tile.build && (tile.team() != team && tile.team() != Team.derelict) || (!current.breaking && (cb.cblock != current.block || cb.tile != current.tile()))) {
                plans.removeFirst();
                break builder;
            }
            if (tile.build instanceof ConstructBuild && !current.initialized) {
                Core.app.post(() -> Events.fire(new BuildSelectEvent(tile, team, this, current.breaking)));
                current.initialized = true;
            }
            if ((core == null && !infinite) || !(tile.build instanceof ConstructBuild)) {
                break builder;
            }
            ConstructBuild entity=(ConstructBuild)tile.build;
            if (current.breaking) {
                entity.deconstruct(this, core, 1.0F / entity.buildCost * Time.delta * type.buildSpeed * state.rules.buildSpeedMultiplier);
            } else {
                entity.construct(this, core, 1.0F / entity.buildCost * Time.delta * type.buildSpeed * state.rules.buildSpeedMultiplier, current.config);
            }
            current.stuck = Mathf.equal(current.progress, entity.progress);
            current.progress = entity.progress;
        }
        shield:
        {

            shieldAlpha -= Time.delta / 15.0F;
            if (shieldAlpha < 0) shieldAlpha = 0.0F;
        }
        bounded:
        {

            if (!net.client() || isLocal()) {
                if (x < 0) vel.x += (-x / warpDst);
                if (y < 0) vel.y += (-y / warpDst);
                if (x > world.unitWidth()) vel.x -= (x - world.unitWidth()) / warpDst;
                if (y > world.unitHeight()) vel.y -= (y - world.unitHeight()) / warpDst;
            }
            if (isGrounded()) {
                x = Mathf.clamp(x, 0, world.width() * tilesize - tilesize);
                y = Mathf.clamp(y, 0, world.height() * tilesize - tilesize);
            }
            if (x < -finalWorldBounds || y < -finalWorldBounds || x >= world.width() * tilesize + finalWorldBounds || y >= world.height() * tilesize + finalWorldBounds) {
                kill();
            }
        }
        mech:
        {

            if (walked || net.client()) {
                float len = deltaLen();
                baseRotation = Angles.moveToward(baseRotation, deltaAngle(), type().baseRotateSpeed * Mathf.clamp(len / type().speed / Time.delta) * Time.delta);
                walkTime += len;
                walked = false;
            }
            float extend = walkExtend(false);
            float base = walkExtend(true);
            float extendScl = base % 1.0F;
            float lastExtend = walkExtension;
            if (extendScl < lastExtend && base % 2.0F > 1.0F && !isFlying()) {
                int side = -Mathf.sign(extend);
                float width = hitSize / 2.0F * side;
                float length = type.mechStride * 1.35F;
                float cx = x + Angles.trnsx(baseRotation, length, width);
                float cy = y + Angles.trnsy(baseRotation, length, width);
                if (type.mechStepShake > 0) {
                    Effect.shake(type.mechStepShake, type.mechStepShake, cx, cy);
                }
                if (type.mechStepParticles) {
                    Tile tile = world.tileWorld(cx, cy);
                    if (tile != null) {
                        Color color = tile.floor().mapColor;
                        Fx.unitLand.at(cx, cy, hitSize / 8.0F, color);
                    }
                }
            }
            walkExtension = extendScl;
        }
        sync:
        {

            if ((Vars.net.client() && !isLocal()) || isRemote()) {
                interpolate();
            }
        }
        unit:
        {

            type.update(this);
            if (state.rules.unitAmmo && ammo < type.ammoCapacity - 1.0E-4F) {
                resupplyTime += Time.delta;
                if (resupplyTime > 10.0F) {
                    type.ammoType.resupply(this);
                    resupplyTime = 0.0F;
                }
            }
            if (abilities.size > 0) {
                for (Ability a : abilities) {
                    a.update(this);
                }
            }
            drag = type.drag * (isGrounded() ? (floorOn().dragMultiplier) : 1.0F);
            if (team != state.rules.waveTeam && state.hasSpawns() && (!net.client() || isLocal())) {
                float relativeSize = state.rules.dropZoneRadius + hitSize / 2.0F + 1.0F;
                for (Tile spawn : spawner.getSpawns()) {
                    if (within(spawn.worldx(), spawn.worldy(), relativeSize)) {
                        vel().add(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(0.1F + 1.0F - dst(spawn) / relativeSize).scl(0.45F * Time.delta));
                    }
                }
            }
            if (dead || health <= 0) {
                drag = 0.01F;
                if (Mathf.chanceDelta(0.1)) {
                    Tmp.v1.setToRandomDirection().scl(hitSize);
                    type.fallEffect.at(x + Tmp.v1.x, y + Tmp.v1.y);
                }
                if (Mathf.chanceDelta(0.2)) {
                    float offset = type.engineOffset / 2.0F + type.engineOffset / 2.0F * elevation;
                    float range = Mathf.range(type.engineSize);
                    type.fallThrusterEffect.at(x + Angles.trnsx(rotation + 180, offset) + Mathf.range(range), y + Angles.trnsy(rotation + 180, offset) + Mathf.range(range), Mathf.random());
                }
                elevation -= type.fallSpeed * Time.delta;
                if (isGrounded()) {
                    destroy();
                }
            }
            Tile tile = tileOn();
            Floor floor = floorOn();
            if (tile != null && isGrounded() && !type.hovering) {
                if (tile.build != null) {
                    tile.build.unitOn(this);
                }
                if (floor.damageTaken > 0.0F) {
                    damageContinuous(floor.damageTaken);
                }
            }
            if (tile != null && !canPassOn()) {
                if (type.canBoost) {
                    elevation = 1.0F;
                } else if (!net.client()) {
                    kill();
                }
            }
            if (!net.client() && !dead) {
                controller.updateUnit();
            }
            if (!controller.isValidController()) {
                resetController();
            }
            if (spawnedByCore && !isPlayer() && !dead) {
                Call.unitDespawn(this);
            }
        }
    }

    public boolean collides(Hitboxc other) {

        return true;
    }

    public void lookAt(float x, float y) {

        lookAt(angleTo(x, y));
    }

    public void updateLastPosition() {

        deltaX = x - lastX;
        deltaY = y - lastY;
        lastX = x;
        lastY = y;
    }

    public void commandNearby(FormationPattern pattern, Boolf<Unit> include) {

        Formation formation = new Formation(new Vec3(x, y, rotation), pattern);
        formation.slotAssignmentStrategy = new DistanceAssignmentStrategy(pattern);
        units.clear();
        Units.nearby(team, x, y, 150.0F, (u) -> {
            if (u.isAI() && include.get(u) && u != this && u.type.flying == type.flying && u.hitSize <= hitSize * 1.1F) {
                units.add(u);
            }
        });
        if (units.isEmpty()) return;
        units.sort(Structs.comps(Structs.comparingFloat((u) -> -u.hitSize), Structs.comparingFloat((u) -> u.dst2(this))));
        units.truncate(type.commandLimit);
        command(formation, units);
    }

    public void readSyncManual(FloatBuffer buffer) {
        if (lastUpdated != 0) updateSpacing = Time.timeSinceMillis(lastUpdated);
        lastUpdated = Time.millis();
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

        int idx = plans.indexOf((req) -> req.breaking == breaking && req.x == x && req.y == y);
        if (idx != -1) {
            plans.removeIndex(idx);
        }
    }

    public Player getPlayer() {

        return isPlayer() ? (Player) controller : null;
    }

    public void display(Table table) {

        type.display(this, table);
    }

    private void rawDamage(float amount) {

        boolean hadShields = shield > 1.0E-4F;
        if (hadShields) {
            shieldAlpha = 1.0F;
        }
        float shieldDamage = Math.min(Math.max(shield, 0), amount);
        shield -= shieldDamage;
        hitTime = 1.0F;
        amount -= shieldDamage;
        if (amount > 0) {
            health -= amount;
            if (health <= 0 && !dead) {
                kill();
            }
            if (hadShields && shield <= 1.0E-4F) {
                Fx.unitShieldBreak.at(x, y, 0, this);
            }
        }
    }

    public void kill() {

        if (dead || net.client()) return;
        Call.unitDeath(id);
    }

    public Building core() {

        return team.core();
    }

    public void trns(float x, float y) {

        set(this.x + x, this.y + y);
    }

    public void readSync(Reads read) {
        if (lastUpdated != 0) updateSpacing = Time.timeSinceMillis(lastUpdated);
        lastUpdated = Time.millis();
        boolean islocal = isLocal();
        this.ammo = read.f();
        this.armor = read.f();
        if (!islocal) {
            baseRotation_LAST_ = this.baseRotation;
            this.baseRotation_TARGET_ = read.f();
        } else {
            read.f();
            baseRotation_LAST_ = this.baseRotation;
            baseRotation_TARGET_ = this.baseRotation;
        }
        this.controller = mindustry.io.TypeIO.readController(read, this.controller);
        if (!islocal) {
            this.elevation = read.f();
        } else {
            read.f();
        }
        this.flag = read.d();
        this.health = read.f();
        this.isShooting = read.bool();
        if (!islocal) {
            this.mineTile = mindustry.io.TypeIO.readTile(read);
        } else {
            mindustry.io.TypeIO.readTile(read);
        }
        if (!islocal) {
            this.mounts = mindustry.io.TypeIO.readMounts(read, this.mounts);
        } else {
            mindustry.io.TypeIO.readMounts(read);
        }
        if (!islocal) {
            int plans_LENGTH = read.i();
            this.plans.clear();
            for (int INDEX = 0; INDEX < plans_LENGTH; INDEX++) {
                mindustry.entities.units.BuildPlan plans_ITEM = mindustry.io.TypeIO.readRequest(read);
                if (plans_ITEM != null) this.plans.add(plans_ITEM);
            }
        } else {
            int _LENGTH = read.i();
            for (int INDEX = 0; INDEX < _LENGTH; INDEX++) {
                mindustry.io.TypeIO.readRequest(read);
            }
        }
        if (!islocal) {
            rotation_LAST_ = this.rotation;
            this.rotation_TARGET_ = read.f();
        } else {
            read.f();
            rotation_LAST_ = this.rotation;
            rotation_TARGET_ = this.rotation;
        }
        this.shield = read.f();
        this.spawnedByCore = read.bool();
        this.stack = mindustry.io.TypeIO.readItems(read, this.stack);
        int statuses_LENGTH = read.i();
        this.statuses.clear();
        for (int INDEX = 0; INDEX < statuses_LENGTH; INDEX++) {
            mindustry.entities.units.StatusEntry statuses_ITEM = mindustry.io.TypeIO.readStatuse(read);
            if (statuses_ITEM != null) this.statuses.add(statuses_ITEM);
        }
        this.team = mindustry.io.TypeIO.readTeam(read);
        this.type = mindustry.Vars.content.getByID(mindustry.ctype.ContentType.unit, read.s());
        if (!islocal) {
            this.updateBuilding = read.bool();
        } else {
            read.bool();
        }
        if (!islocal) {
            x_LAST_ = this.x;
            this.x_TARGET_ = read.f();
        } else {
            read.f();
            x_LAST_ = this.x;
            x_TARGET_ = this.x;
        }
        if (!islocal) {
            y_LAST_ = this.y;
            this.y_TARGET_ = read.f();
        } else {
            read.f();
            y_LAST_ = this.y;
            y_TARGET_ = this.y;
        }
        afterSync();

    }

    public <T> T as() {

        return (T) this;
    }

    public void aimLook(float x, float y) {

        aim(x, y);
        lookAt(x, y);
    }

    public void unapply(StatusEffect effect) {

        statuses.remove((e) -> {
            if (e.effect == effect) {
                Pools.free(e);
                return true;
            }
            return false;
        });
    }

    public void heal() {

        dead = false;
        health = maxHealth;
    }

    public boolean canMine(Item item) {

        return type.mineTier >= item.hardness;
    }

    public int tileX() {

        return World.toTile(x);
    }

    public void controller(UnitController next) {
        commander:
        {

            clearCommand();
        }
        unit:
        {

            this.controller = next;
            if (controller.unit() != this) controller.unit(this);
        }
    }

    public void impulse(float x, float y) {

        float mass = mass();
        vel.add(x / mass, y / mass);
    }

    public void damageContinuousPierce(float amount) {

        damagePierce(amount * Time.delta, hitTime <= -20 + hitDuration);
    }

    public void drawPlan(BuildPlan request, float alpha) {

        request.animScale = 1.0F;
        if (request.breaking) {
            control.input.drawBreaking(request);
        } else {
            request.block.drawPlan(request, control.input.allRequests(), Build.validPlace(request.block, team, request.x, request.y, request.rotation) || control.input.requestMatches(request), alpha);
            Draw.reset();
            Draw.mixcol(Color.white, 0.24F + Mathf.absin(Time.globalTime, 6.0F, 0.28F));
            Draw.alpha(alpha);
            request.block.drawRequestConfigTop(request, plans);
        }
    }

    @Override
    public void drawPlanTop(BuildPlan request, float alpha) {
        if (!request.breaking) {
            Draw.reset();
            Draw.mixcol(Color.white, 0.24F + Mathf.absin(Time.globalTime, 6.0F, 0.28F));
            Draw.alpha(alpha);
            request.block.drawRequestConfigTop(request, plans);
        }
    }

    public boolean isPlayer() {

        return controller instanceof Player;
    }

    public void aim(Position pos) {

        aim(pos.getX(), pos.getY());
    }

    public void eachGroup(Cons<Unit> cons) {

        cons.get(this);
        controlling().each(cons);
    }

    public BuildPlan buildPlan() {

        return plans.size == 0 ? null : plans.first();
    }

    public float range() {

        return type.range;
    }

    public void damagePierce(float amount) {

        damagePierce(amount, true);
    }

    public boolean canBuild() {

        return type.buildSpeed > 0;
    }

    @Override
    public int classId() {
        return 4;
    }

    @Override
    public ItemStack stack() {
        return stack;
    }

    @Override
    public void stack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public float itemTime() {
        return itemTime;
    }

    @Override
    public void itemTime(float itemTime) {
        this.itemTime = itemTime;
    }

    @Override
    public float mineTimer() {
        return mineTimer;
    }

    @Override
    public void mineTimer(float mineTimer) {
        this.mineTimer = mineTimer;
    }

    @Override
    public Tile mineTile() {
        return mineTile;
    }

    @Override
    public void mineTile(Tile mineTile) {
        this.mineTile = mineTile;
    }

    @Override
    public float elevation() {
        return elevation;
    }

    @Override
    public void elevation(float elevation) {
        this.elevation = elevation;
    }

    @Override
    public boolean hovering() {
        return hovering;
    }

    @Override
    public void hovering(boolean hovering) {
        this.hovering = hovering;
    }

    @Override
    public float drownTime() {
        return drownTime;
    }

    @Override
    public void drownTime(float drownTime) {
        this.drownTime = drownTime;
    }

    @Override
    public float splashTimer() {
        return splashTimer;
    }

    @Override
    public void splashTimer(float splashTimer) {
        this.splashTimer = splashTimer;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void id(int id) {
        this.id = id;
    }

    @Override
    public float speedMultiplier() {
        return speedMultiplier;
    }

    @Override
    public float damageMultiplier() {
        return damageMultiplier;
    }

    @Override
    public float healthMultiplier() {
        return healthMultiplier;
    }

    @Override
    public float reloadMultiplier() {
        return reloadMultiplier;
    }

    @Override
    public float health() {
        return health;
    }

    @Override
    public void health(float health) {
        this.health = health;
    }

    @Override
    public float hitTime() {
        return hitTime;
    }

    @Override
    public void hitTime(float hitTime) {
        this.hitTime = hitTime;
    }

    @Override
    public float maxHealth() {
        return maxHealth;
    }

    @Override
    public void maxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public void dead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public Formation formation() {
        return formation;
    }

    @Override
    public void formation(Formation formation) {
        this.formation = formation;
    }

    @Override
    public Seq<Unit> controlling() {
        return controlling;
    }

    @Override
    public void controlling(Seq<Unit> controlling) {
        this.controlling = controlling;
    }

    @Override
    public float minFormationSpeed() {
        return minFormationSpeed;
    }

    @Override
    public void minFormationSpeed(float minFormationSpeed) {
        this.minFormationSpeed = minFormationSpeed;
    }

    @Override
    public float lastX() {
        return lastX;
    }

    @Override
    public void lastX(float lastX) {
        this.lastX = lastX;
    }

    @Override
    public float lastY() {
        return lastY;
    }

    @Override
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    @Override
    public float deltaX() {
        return deltaX;
    }

    @Override
    public void deltaX(float deltaX) {
        this.deltaX = deltaX;
    }

    @Override
    public float deltaY() {
        return deltaY;
    }

    @Override
    public void deltaY(float deltaY) {
        this.deltaY = deltaY;
    }

    @Override
    public float hitSize() {
        return hitSize;
    }

    @Override
    public void hitSize(float hitSize) {
        this.hitSize = hitSize;
    }

    @Override
    public PhysicsProcess.PhysicRef physref() {
        return physref;
    }

    @Override
    public void physref(PhysicsProcess.PhysicRef physref) {
        this.physref = physref;
    }

    @Override
    public WeaponMount[] mounts() {
        return mounts;
    }

    @Override
    public void mounts(WeaponMount[] mounts) {
        this.mounts = mounts;
    }

    @Override
    public boolean isRotate() {
        return isRotate;
    }

    @Override
    public float aimX() {
        return aimX;
    }

    @Override
    public void aimX(float aimX) {
        this.aimX = aimX;
    }

    @Override
    public float aimY() {
        return aimY;
    }

    @Override
    public void aimY(float aimY) {
        this.aimY = aimY;
    }

    @Override
    public boolean isShooting() {
        return isShooting;
    }

    @Override
    public void isShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    @Override
    public float ammo() {
        return ammo;
    }

    @Override
    public void ammo(float ammo) {
        this.ammo = ammo;
    }

    @Override
    public Queue<BuildPlan> plans() {
        return plans;
    }

    @Override
    public void plans(Queue<BuildPlan> plans) {
        this.plans = plans;
    }

    @Override
    public boolean updateBuilding() {
        return updateBuilding;
    }

    @Override
    public void updateBuilding(boolean updateBuilding) {
        this.updateBuilding = updateBuilding;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public void x(float x) {
        this.x = x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public void y(float y) {
        this.y = y;
    }

    @Override
    public Vec2 vel() {
        return vel;
    }

    @Override
    public float drag() {
        return drag;
    }

    @Override
    public void drag(float drag) {
        this.drag = drag;
    }

    @Override
    public float rotation() {
        return rotation;
    }

    @Override
    public void rotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public float shield() {
        return shield;
    }

    @Override
    public void shield(float shield) {
        this.shield = shield;
    }

    @Override
    public float armor() {
        return armor;
    }

    @Override
    public void armor(float armor) {
        this.armor = armor;
    }

    @Override
    public float shieldAlpha() {
        return shieldAlpha;
    }

    @Override
    public void shieldAlpha(float shieldAlpha) {
        this.shieldAlpha = shieldAlpha;
    }

    @Override
    public Team team() {
        return team;
    }

    @Override
    public void team(Team team) {
        this.team = team;
    }

    @Override
    public float baseRotation() {
        return baseRotation;
    }

    @Override
    public void baseRotation(float baseRotation) {
        this.baseRotation = baseRotation;
    }

    @Override
    public float walkTime() {
        return walkTime;
    }

    @Override
    public void walkTime(float walkTime) {
        this.walkTime = walkTime;
    }

    @Override
    public float walkExtension() {
        return walkExtension;
    }

    @Override
    public void walkExtension(float walkExtension) {
        this.walkExtension = walkExtension;
    }

    @Override
    public long lastUpdated() {
        return lastUpdated;
    }

    @Override
    public void lastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public long updateSpacing() {
        return updateSpacing;
    }

    @Override
    public void updateSpacing(long updateSpacing) {
        this.updateSpacing = updateSpacing;
    }

    @Override
    public UnitType type() {
        return type;
    }

    @Override
    public void type(UnitType type) {
        this.type = type;
    }

    @Override
    public boolean spawnedByCore() {
        return spawnedByCore;
    }

    @Override
    public void spawnedByCore(boolean spawnedByCore) {
        this.spawnedByCore = spawnedByCore;
    }

    @Override
    public double flag() {
        return flag;
    }

    @Override
    public void flag(double flag) {
        this.flag = flag;
    }

    @Override
    public Seq<Ability> abilities() {
        return abilities;
    }

    @Override
    public void abilities(Seq<Ability> abilities) {
        this.abilities = abilities;
    }
}
