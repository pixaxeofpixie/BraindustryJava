package braindustry.entities;

import ModVars.modVars;
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
import mindustry.entities.*;
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
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.ResupplyPoint;
import mindustryAddition.versions.ModEntityc;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;

public class AmmoDistributeUnit extends Unit implements ModEntityc,  Itemsc, Builderc, Weaponsc, Commanderc, Boundedc, Statusc, Syncc, Minerc, Healthc, Shieldc, Entityc, Teamc, Physicsc, Drawc, Flyingc, Hitboxc, Velc, Rotc, Unitc, Posc, AmmoDistributec {

    public static int classId = 40;
    public static final Vec2[] vecs = new Vec2[]{new Vec2(), new Vec2(), new Vec2(), new Vec2()};
    public static final Seq<FormationMember> members = new Seq();
    public static final Seq<Unit> units = new Seq();
    public static final float warpDst = 180.0F;
    public static final float hitDuration = 9.0F;
    public static final Vec2 tmp1 = new Vec2();
    public static final Vec2 tmp2 = new Vec2();
    public static int sequenceNum = 0;
    public Seq<StatusEntry> statuses = new Seq();
    public transient Bits applied;
    public transient boolean added;
    public transient boolean wasFlying;
    public UnitController controller;
    public transient float resupplyTime;
    public Seq<Payload> payloads;
    public transient float ammoCooldown;
    protected transient boolean isRotate;
    protected transient float speedMultiplier;
    protected transient float damageMultiplier;
    protected transient float healthMultiplier;
    protected transient float reloadMultiplier;
    private transient float rotation_TARGET_;
    private transient float rotation_LAST_;
    private transient float x_TARGET_;
    private transient float x_LAST_;
    private transient float y_TARGET_;
    private transient float y_LAST_;

    public AmmoDistributeUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.speedMultiplier = 1.0F;
        this.damageMultiplier = 1.0F;
        this.healthMultiplier = 1.0F;
        this.reloadMultiplier = 1.0F;
        this.resupplyTime = Mathf.random(10.0F);
        this.payloads = new Seq();
    }

    public static AmmoDistributeUnit create() {
        return new AmmoDistributeUnit();
    }

    public boolean serialize() {
        return true;
    }

    public String toString() {
        return "AmmoDistributeUnit#" + this.id;
    }
    public void drawPlanTop(BuildPlan request, float alpha) {

        if (!request.breaking) {
            Draw.reset();
            Draw.mixcol(Color.white, 0.24F + Mathf.absin(Time.globalTime, 6.0F, 0.28F));
            Draw.alpha(alpha);
            request.block.drawRequestConfigTop(request, plans);
        }
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

    public void damagePierce(float amount, boolean withEffect) {
        float pre = this.hitTime;
        this.rawDamage(amount);
        if (!withEffect) {
            this.hitTime = pre;
        }

    }

    public void lookAt(float angle) {
        this.rotation = Angles.moveToward(this.rotation, angle, this.type.rotateSpeed * Time.delta * this.speedMultiplier());
    }

    public float getY() {
        return this.y;
    }

    public void wobble() {
        this.x += Mathf.sin(Time.time + (float) (this.id() % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
        this.y += Mathf.cos(Time.time + (float) (this.id() % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
    }

    public void aimLook(float x, float y) {
        this.aim(x, y);
        this.lookAt(x, y);
    }

    public void setupWeapons(UnitType def) {
        this.mounts = new WeaponMount[def.weapons.size];

        for (int i = 0; i < this.mounts.length; ++i) {
            this.mounts[i] = new WeaponMount(def.weapons.get(i));
        }

    }

    public boolean isValid() {
        return !this.dead && this.isAdded();
    }

    public float realSpeed() {
        return Mathf.lerp(1.0F, this.type.canBoost ? this.type.boostMultiplier : 1.0F, this.elevation) * this.speed();
    }

    public float healthf() {
        return this.health / this.maxHealth;
    }

    public boolean onSolid() {
        Tile tile = this.tileOn();
        return tile == null || tile.solid();
    }

    public boolean isBuilding() {
        return this.plans.size != 0;
    }

    public void writeSync(Writes write) {
        write.f(this.ammo);
        write.f(this.armor);
        TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        TypeIO.writeTile(write, this.mineTile);
        TypeIO.writeMounts(write, this.mounts);
        write.i(this.payloads.size);

        int INDEX;
        for (INDEX = 0; INDEX < this.payloads.size; ++INDEX) {
            TypeIO.writePayload(write, this.payloads.get(INDEX));
        }

        write.i(this.plans.size);

        for (INDEX = 0; INDEX < this.plans.size; ++INDEX) {
            TypeIO.writeRequest(write, this.plans.get(INDEX));
        }

        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);

        for (INDEX = 0; INDEX < this.statuses.size; ++INDEX) {
            TypeIO.writeStatuse(write, this.statuses.get(INDEX));
        }

        TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        write.f(this.x);
        write.f(this.y);
    }

    public boolean isImmune(StatusEffect effect) {
        return this.type.immunities.contains(effect);
    }

    public boolean isRemote() {
        return ((Unitc) this).isPlayer() && !this.isLocal();
    }

    public void lookAt(Position pos) {
        this.lookAt(this.angleTo(pos));
    }

    public void afterRead() {
        this.updateLastPosition();
        this.afterSync();
        this.controller(this.type.createController());
    }

    public void killed() {
        this.clearCommand();
        this.health = 0.0F;
        this.dead = true;
        if (!this.type.flying) {
            this.destroy();
        }

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

    public void addBuild(BuildPlan place) {
        this.addBuild(place, true);
    }

    public boolean canPickup(Building build) {
        return this.payloadUsed() + (float) (build.block.size * build.block.size * 8 * 8) <= this.type.payloadCapacity + 0.001F && build.canPickup();
    }

    public boolean isFlying() {
        return this.elevation >= 0.09F;
    }

    public void approach(Vec2 vector) {
        this.vel.approachDelta(vector, this.type.accel * this.realSpeed() * this.floorSpeedMultiplier());
    }

    public void getCollisions(Cons<QuadTree> consumer) {
    }

    public boolean isLocal() {
        return this.getPlayer() == Vars.player || this instanceof Unitc && ((Unitc) this).controller() == Vars.player;
    }

    public void set(UnitType def, UnitController controller) {
        if (this.type != def) {
            this.setType(def);
        }

        this.controller(controller);
    }

    public int itemCapacity() {
        return this.type.itemCapacity;
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
                UnitController var6 = this.controller;
                Player p;
                var10000 = var6 instanceof Player && (p = (Player) var6) == var6 ? p.name : null;
                break;
            case firstItem:
                var10000 = this.stack().amount == 0 ? null : this.item();
                break;
            case payloadType:
                if (this instanceof Payloadc && this == this) {
                    if (this.payloads().isEmpty()) {
                        var10000 = null;
                    } else {
                        Object var5 = this.payloads().peek();
                        UnitPayload p1;
                        if (var5 instanceof UnitPayload && (p1 = (UnitPayload) var5) == var5) {
                            var10000 = p1.unit.type;
                        } else {
                            var5 = this.payloads().peek();
                            BuildPayload p2;
                            var10000 = var5 instanceof BuildPayload && (p2 = (BuildPayload) var5) == var5 ? p2.block() : null;
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
        if (this.isBuilding() && !this.within(this.buildPlan(), Vars.state.rules.infiniteResources ? 3.4028235E38F : 220.0F)) {
            return false;
        } else {
            return this.isBuilding() && this.updateBuilding;
        }
    }

    public void pickup(Building tile) {
        tile.pickedUp();
        tile.tile.remove();
        this.payloads.add(new BuildPayload(tile));
        Fx.unitPickup.at(tile);
        Events.fire(new EventType.PickupEvent(this, tile));
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
        return Math.max((float) this.type.region.width * 2.0F, this.type.clipSize);
    }

    public void aimLook(Position pos) {
        this.aim(pos);
        this.lookAt(pos);
    }

    public float getX() {
        return this.x;
    }

    public void set(Position pos) {
        this.set(pos.getX(), pos.getY());
    }

    public double sense(LAccess sensor) {
        double var10000;
        switch (sensor) {
            case totalItems:
                var10000 = this.stack().amount;
                break;
            case itemCapacity:
                var10000 = this.type.itemCapacity;
                break;
            case rotation:
                var10000 = this.rotation;
                break;
            case health:
                var10000 = this.health;
                break;
            case maxHealth:
                var10000 = this.maxHealth;
                break;
            case ammo:
                var10000 = !Vars.state.rules.unitAmmo ? (double) this.type.ammoCapacity : (double) this.ammo;
                break;
            case ammoCapacity:
                var10000 = this.type.ammoCapacity;
                break;
            case x:
                var10000 = World.conv(this.x);
                break;
            case y:
                var10000 = World.conv(this.y);
                break;
            case team:
                var10000 = this.team.id;
                break;
            case shooting:
                var10000 = this.isShooting() ? 1.0D : 0.0D;
                break;
            case shootX:
                var10000 = World.conv(this.aimX());
                break;
            case shootY:
                var10000 = World.conv(this.aimY());
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
                var10000 = this instanceof Payloadc && this == this ? this.payloads().size : 0;
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

    public void moveAt(Vec2 vector) {
        this.moveAt(vector, this.type.accel);
    }

    public void commandNearby(FormationPattern pattern) {
        this.commandNearby(pattern, (u) -> {
            return true;
        });
    }

    public boolean isCommanding() {
        return this.formation != null;
    }

    public float deltaLen() {
        return Mathf.len(this.deltaX, this.deltaY);
    }

    public void draw() {
        float px;
        float py;
        float ex;
        float ey;
        if (this.activelyBuilding()) {
            Draw.z(115.0F);
            BuildPlan plan = this.buildPlan();
            Tile tile = Vars.world.tile(plan.x, plan.y);
            CoreBlock.CoreBuild core = this.team.core();
            if (tile != null && (this.within(tile, 220.0F) || Vars.state.isEditor())) {
                if (core != null && !this.isLocal() && !(tile.block() instanceof ConstructBlock)) {
                    Draw.z(84.0F);
                    this.drawPlan(plan, 0.5F);
                    Draw.z(115.0F);
                }

                int size = plan.breaking ? tile.block().size : plan.block.size;
                px = plan.drawx();
                py = plan.drawy();
                Lines.stroke(1.0F, Pal.accent);
                ex = 3.8F + Mathf.absin(Time.time, 1.1F, 0.6F);
                ey = this.x + Angles.trnsx(this.rotation, ex);
                py = this.y + Angles.trnsy(this.rotation, ex);
                float sz = (float) (8 * size) / 2.0F;
                float ang = this.angleTo(px, py);
                vecs[0].set(px - sz, py - sz);
                vecs[1].set(px + sz, py - sz);
                vecs[2].set(px - sz, py + sz);
                vecs[3].set(px + sz, py + sz);
                Arrays.sort(vecs, Structs.comparingFloat((vec) -> {
                    return -Angles.angleDist(this.angleTo(vec), ang);
                }));
                float x1 = vecs[0].x;
                float y1 = vecs[0].y;
                float x3 = vecs[1].x;
                float y3 = vecs[1].y;
                Draw.alpha(1.0F);
                Lines.line(ey, py, x1, y1);
                Lines.line(ey, py, x3, y3);
                Fill.circle(ey, py, 1.6F + Mathf.absin(Time.time, 0.8F, 1.5F));
                Draw.color();
            }
        }

        Iterator var16 = this.statuses.iterator();

        while (var16.hasNext()) {
            StatusEntry e = (StatusEntry) var16.next();
            e.effect.draw(this);
        }

        if (this.mining()) {
            float focusLen = this.hitSize / 2.0F + Mathf.absin(Time.time, 1.1F, 0.5F);
            float swingScl = 12.0F;
            float swingMag = 1.0F;
            float flashScl = 0.3F;
            px = this.x + Angles.trnsx(this.rotation, focusLen);
            py = this.y + Angles.trnsy(this.rotation, focusLen);
            ex = this.mineTile.worldx() + Mathf.sin(Time.time + 48.0F, swingScl, swingMag);
            ey = this.mineTile.worldy() + Mathf.sin(Time.time + 48.0F, swingScl + 2.0F, swingMag);
            Draw.z(115.1F);
            Draw.color(Color.lightGray, Color.white, 1.0F - flashScl + Mathf.absin(Time.time, 0.5F, flashScl));
            Drawf.laser(this.team(), Core.atlas.find("minelaser"), Core.atlas.find("minelaser-end"), px, py, ex, ey, 0.75F);
            if (this.isLocal()) {
                Lines.stroke(1.0F, Pal.accent);
                Lines.poly(this.mineTile.worldx(), this.mineTile.worldy(), 4, 4.0F * Mathf.sqrt2, Time.time);
            }

            Draw.color();
        }

        this.type.draw(this);
    }

    public void snapSync() {
        this.updateSpacing = 16L;
        this.lastUpdated = Time.millis();
        this.rotation_LAST_ = this.rotation_TARGET_;
        this.rotation = this.rotation_TARGET_;
        this.x_LAST_ = this.x_TARGET_;
        this.x = this.x_TARGET_;
        this.y_LAST_ = this.y_TARGET_;
        this.y = this.y_TARGET_;
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

    @Override
    public String getControllerName() {

        if (isPlayer()) return getPlayer().name;
        LogicAI ai;
        FormationAI fai;
        if (controller instanceof LogicAI && (ai=(LogicAI)controller)==controller && ai.controller != null) return ai.controller.lastAccessed;
        if (controller instanceof FormationAI && (fai=(FormationAI)controller)==controller && fai.leader != null && fai.leader.isPlayer()) return fai.leader.getPlayer().name;
        return null;
    }

    public boolean canDrown() {
        return this.isGrounded() && !this.hovering && this.type.canDrown;
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

    public boolean hasEffect(StatusEffect effect) {
        return this.applied.get(effect.id);
    }

    public boolean isAI() {
        return this.controller instanceof AIController;
    }

    public Item item() {
        return this.stack.item;
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

    public void apply(StatusEffect effect, float duration) {
        if (effect != StatusEffects.none && effect != null && !this.isImmune(effect)) {
            if (this.statuses.size > 0) {
                for (int i = 0; i < this.statuses.size; ++i) {
                    StatusEntry entry = this.statuses.get(i);
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

            StatusEntry entry = Pools.obtain(StatusEntry.class, StatusEntry::new);
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
            this.added = true;
            this.updateLastPosition();
            this.team.data().updateCount(this.type, 1);
            if (this.count() > this.cap() && !this.spawnedByCore && !this.dead) {
                Call.unitCapDeath(this);
                this.team.data().updateCount(this.type, -1);
            }

        }
    }

    public EntityCollisions.SolidPred solidity() {
        return null;
    }

    public boolean tryDropPayload(Payload payload) {
        Tile on = this.tileOn();
        if (Vars.net.client() && payload instanceof UnitPayload) {
            Vars.netClient.clearRemovedEntity(((UnitPayload) payload).unit.id);
        }

        if (on != null && on.build != null && on.build.acceptPayload(on.build, payload)) {
            Fx.unitDrop.at(on.build);
            on.build.handlePayload(on.build, payload);
            return true;
        } else {
            BuildPayload b;
            if (payload instanceof BuildPayload && (b = (BuildPayload) payload) == payload) {
                return this.dropBlock(b);
            } else {
                UnitPayload p;
                return payload instanceof UnitPayload && (p = (UnitPayload) payload) == payload && this.dropUnit(p);
            }
        }
    }

    public void writeSyncManual(FloatBuffer buffer) {
        buffer.put(this.rotation);
        buffer.put(this.x);
        buffer.put(this.y);
    }

    public int maxAccepted(Item item) {
        return this.stack.item != item && this.stack.amount > 0 ? 0 : this.itemCapacity() - this.stack.amount;
    }

    public boolean dropLastPayload() {
        if (this.payloads.isEmpty()) {
            return false;
        } else {
            Payload load = this.payloads.peek();
            if (this.tryDropPayload(load)) {
                this.payloads.pop();
                return true;
            } else {
                return false;
            }
        }
    }

    public void read(Reads read) {
        short REV = read.s();
        int payloads_LENGTH;
        int plans_LENGTH;
        Payload payloads_ITEM;
        BuildPlan plans_ITEM;
        StatusEntry statuses_ITEM;
        int statuses_LENGTH;
        int INDEX;
        if (REV == 0) {
            this.ammo = read.f();
            this.armor = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = TypeIO.readMounts(read, this.mounts);
            payloads_LENGTH = read.i();
            this.payloads.clear();

            for (plans_LENGTH = 0; plans_LENGTH < payloads_LENGTH; ++plans_LENGTH) {
                payloads_ITEM = TypeIO.readPayload(read);
                if (payloads_ITEM != null) {
                    this.payloads.add(payloads_ITEM);
                }
            }

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
            this.type = Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 1) {
            this.ammo = read.f();
            this.armor = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mounts = TypeIO.readMounts(read, this.mounts);
            payloads_LENGTH = read.i();
            this.payloads.clear();

            for (plans_LENGTH = 0; plans_LENGTH < payloads_LENGTH; ++plans_LENGTH) {
                payloads_ITEM = TypeIO.readPayload(read);
                if (payloads_ITEM != null) {
                    this.payloads.add(payloads_ITEM);
                }
            }

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
            this.type = Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 2) {
            this.ammo = read.f();
            this.armor = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = TypeIO.readTile(read);
            this.mounts = TypeIO.readMounts(read, this.mounts);
            payloads_LENGTH = read.i();
            this.payloads.clear();

            for (plans_LENGTH = 0; plans_LENGTH < payloads_LENGTH; ++plans_LENGTH) {
                payloads_ITEM = TypeIO.readPayload(read);
                if (payloads_ITEM != null) {
                    this.payloads.add(payloads_ITEM);
                }
            }

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
            this.type = Vars.content.getByID(ContentType.unit, read.s());
            this.x = read.f();
            this.y = read.f();
        } else {
            if (REV != 3) {
                throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'oct'");
            }

            this.ammo = read.f();
            this.armor = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = TypeIO.readTile(read);
            this.mounts = TypeIO.readMounts(read, this.mounts);
            payloads_LENGTH = read.i();
            this.payloads.clear();

            for (plans_LENGTH = 0; plans_LENGTH < payloads_LENGTH; ++plans_LENGTH) {
                payloads_ITEM = TypeIO.readPayload(read);
                if (payloads_ITEM != null) {
                    this.payloads.add(payloads_ITEM);
                }
            }

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
            this.type = Vars.content.getByID(ContentType.unit, read.s());
            this.updateBuilding = read.bool();
            this.x = read.f();
            this.y = read.f();
        }

        this.afterRead();
    }

    public double sense(Content content) {
        return content == this.stack().item ? (double) this.stack().amount : 0.0D;
    }

    public Building closestEnemyCore() {
        return Vars.state.teams.closestEnemyCore(this.x, this.y, this.team);
    }

    public boolean cheating() {
        return this.team.rules().cheat;
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
            for (Iterator var5 = this.statuses.iterator(); var5.hasNext(); total += intensity) {
                StatusEntry entry = (StatusEntry) var5.next();
                intensity = entry.time < 10.0F ? entry.time / 10.0F : 1.0F;
                r += entry.effect.color.r * intensity;
                g += entry.effect.color.g * intensity;
                b += entry.effect.color.b * intensity;
            }

            float count = (float) this.statuses.size + total;
            return Tmp.c1.set(r / count, g / count, b / count, 1.0F);
        }
    }

    public void trns(Position pos) {
        this.trns(pos.getX(), pos.getY());
    }

    public boolean acceptsItem(Item item) {
        return !this.hasItem() || item == this.stack.item && this.stack.amount + 1 <= this.itemCapacity();
    }

    public float bounds() {
        return this.hitSize * 2.0F;
    }

    public void healFract(float amount) {
        this.heal(amount * this.maxHealth);
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
        this.rotation_LAST_ = this.rotation;
        this.rotation_TARGET_ = this.rotation;
        this.x_LAST_ = this.x;
        this.x_TARGET_ = this.x;
        this.y_LAST_ = this.y;
        this.y_TARGET_ = this.y;
    }

    public int cap() {
        return Units.getCap(this.team);
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

    public void write(Writes write) {
        write.s(3);
        write.f(this.ammo);
        write.f(this.armor);
        TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        TypeIO.writeTile(write, this.mineTile);
        TypeIO.writeMounts(write, this.mounts);
        write.i(this.payloads.size);

        int INDEX;
        for (INDEX = 0; INDEX < this.payloads.size; ++INDEX) {
            TypeIO.writePayload(write, this.payloads.get(INDEX));
        }

        write.i(this.plans.size);

        for (INDEX = 0; INDEX < this.plans.size; ++INDEX) {
            TypeIO.writeRequest(write, this.plans.get(INDEX));
        }

        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);

        for (INDEX = 0; INDEX < this.statuses.size; ++INDEX) {
            TypeIO.writeStatuse(write, this.statuses.get(INDEX));
        }

        TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        write.f(this.x);
        write.f(this.y);
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
            if (Vars.net.client()) {
                Vars.netClient.addRemovedEntity(this.id());
            }

            this.added = false;
            this.team.data().updateCount(this.type, -1);
            this.controller.removed(this);
        }
    }

    public void moveAt(Vec2 vector, float acceleration) {
        Vec2 t = tmp1.set(vector);
        tmp2.set(t).sub(this.vel).limit(acceleration * vector.len() * Time.delta * this.floorSpeedMultiplier());
        this.vel.add(tmp2);
    }

    public float speed() {
        float strafePenalty = !this.isGrounded() && this.isPlayer() ? Mathf.lerp(1.0F, this.type.strafePenalty, Angles.angleDist(this.vel().angle(), this.rotation) / 180.0F) : 1.0F;
        return (this.isCommanding() ? this.minFormationSpeed * 0.98F : this.type.speed) * strafePenalty;
    }

    public Tile tileOn() {
        return Vars.world.tileWorld(this.x, this.y);
    }

    public void clearItem() {
        this.stack.amount = 0;
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

    public boolean isNull() {
        return false;
    }

    public boolean checkTarget(boolean targetAir, boolean targetGround) {
        return this.isGrounded() && targetGround || this.isFlying() && targetAir;
    }

    public void collision(Hitboxc other, float x, float y) {
    }

    public void addItem(Item item, int amount) {
        this.stack.amount = this.stack.item == item ? this.stack.amount + amount : amount;
        this.stack.item = item;
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
    }

    public Building closestCore() {
        return Vars.state.teams.closestCore(this.x, this.y, this.team);
    }

    public boolean canMine(Item item) {
        return this.type.mineTier >= item.hardness;
    }

    public boolean isAdded() {
        return this.added;
    }

    public UnitController controller() {
        return this.controller;
    }

    public boolean hasWeapons() {
        return this.type.hasWeapons();
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

    public void addPayload(Payload load) {
        this.payloads.add(load);
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
                if (var6 instanceof ConstructBlock.ConstructBuild && (cons = (ConstructBlock.ConstructBuild) var6) == var6) {
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

    public void hitboxTile(Rect rect) {
        float size = Math.min(this.hitSize * 0.66F, 7.9F);
        rect.setCentered(this.x, this.y, size, size);
    }

    public <T> T with(Cons<T> cons) {
        cons.get((T) this);
        return (T) this;
    }

    public boolean validMine(Tile tile, boolean checkDst) {
        return tile != null && tile.block() == Blocks.air && (this.within(tile.worldx(), tile.worldy(), 70.0F) || !checkDst) && tile.drop() != null && this.canMine(tile.drop());
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

    public boolean offloadImmediately() {
        return this.isPlayer();
    }

    public float payloadUsed() {
        return this.payloads.sumf((p) -> {
            return p.size() * p.size();
        });
    }

    public void hitbox(Rect rect) {
        rect.setCentered(this.x, this.y, this.hitSize, this.hitSize);
    }

    public boolean canPickupPayload(Payload pay) {
        return this.payloadUsed() + pay.size() * pay.size() <= this.type.payloadCapacity + 0.001F;
    }

    public void controlWeapons(boolean rotateShoot) {
        this.controlWeapons(rotateShoot, rotateShoot);
    }

    public boolean isGrounded() {
        return this.elevation < 0.001F;
    }

    public void pickup(Unit unit) {
        unit.remove();
        this.payloads.add(new UnitPayload(unit));
        Fx.unitPickup.at(unit);
        if (Vars.net.client()) {
            Vars.netClient.clearRemovedEntity(unit.id);
        }

        Events.fire(new EventType.PickupEvent(this, unit));
    }

    public int pathType() {
        return 0;
    }

    public boolean dropUnit(UnitPayload payload) {
        Unit u = payload.unit;
        if (!u.canPass(this.tileX(), this.tileY())) {
            return false;
        } else {
            Fx.unitDrop.at(this);
            if (Vars.net.client()) {
                return true;
            } else {
                u.set(this);
                u.trns(Tmp.v1.rnd(Mathf.random(2.0F)));
                u.rotation(this.rotation);
                u.id = EntityGroup.nextId();
                if (!u.isAdded()) {
                    u.team.data().updateCount(u.type, -1);
                }

                u.add();
                return true;
            }
        }
    }

    public boolean isBoss() {
        return this.hasEffect(StatusEffects.boss);
    }

    public boolean dropBlock(BuildPayload payload) {
        Building tile = payload.build;
        int tx = World.toTile(this.x - tile.block.offset);
        int ty = World.toTile(this.y - tile.block.offset);
        Tile on = Vars.world.tile(tx, ty);
        if (on != null && Build.validPlace(tile.block, tile.team, tx, ty, tile.rotation, false)) {
            int rot = (int) ((this.rotation + 45.0F) / 90.0F) % 4;
            payload.place(on, rot);
            if (this.isPlayer()) {
                payload.build.lastAccessed = this.getPlayer().name;
            }

            Fx.unitDrop.at(tile);
            Fx.placeBlock.at(on.drawx(), on.drawy(), (float) on.block().size);
            return true;
        } else {
            return false;
        }
    }

    public boolean canPass(int tileX, int tileY) {
        EntityCollisions.SolidPred s = this.solidity();
        return s == null || !s.solid(tileX, tileY);
    }

    public boolean validMine(Tile tile) {
        return this.validMine(tile, true);
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

    public boolean hasPayload() {
        return this.payloads.size > 0;
    }

    public boolean canPassOn() {
        return this.canPass(this.tileX(), this.tileY());
    }

    public void interpolate() {
        if (this.lastUpdated != 0L && this.updateSpacing != 0L) {
            float timeSinceUpdate = (float) Time.timeSinceMillis(this.lastUpdated);
            float alpha = Math.min(timeSinceUpdate / (float) this.updateSpacing, 2.0F);
            this.rotation = Mathf.slerp(this.rotation_LAST_, this.rotation_TARGET_, alpha);
            this.x = Mathf.lerp(this.x_LAST_, this.x_TARGET_, alpha);
            this.y = Mathf.lerp(this.y_LAST_, this.y_TARGET_, alpha);
        } else if (this.lastUpdated != 0L) {
            this.rotation = this.rotation_TARGET_;
            this.x = this.x_TARGET_;
            this.y = this.y_TARGET_;
        }

    }

    public void impulseNet(Vec2 v) {
        this.impulse(v.x, v.y);
        if (this.isRemote()) {
            float mass = this.mass();
            this.move(v.x / mass, v.y / mass);
        }

    }

    public boolean inRange(Position other) {
        return this.within(other, this.type.range);
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

    public boolean moving() {
        return !this.vel.isZero(0.01F);
    }

    public void impulse(Vec2 v) {
        this.impulse(v.x, v.y);
    }

    public void update() {
        this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
        this.vel.scl(Mathf.clamp(1.0F - this.drag * Time.delta));
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
        this.itemTime = Mathf.lerpDelta(this.itemTime, (float) Mathf.num(this.hasItem()), 0.05F);
        float offset;
        if (this.updateBuilding && this.canBuild()) {
            offset = Vars.state.rules.infiniteResources ? 3.4028235E38F : 220.0F;
            boolean infinite = Vars.state.rules.infiniteResources || this.team().rules().infiniteResources;
            Iterator it = this.plans.iterator();

            label590:
            while (true) {
                BuildPlan req;
                Tile tileR;
                do {
                    if (!it.hasNext()) {
                        Building core = this.core();
                        if (this.buildPlan() != null) {
                            if (this.plans.size > 1) {
                                for (int total = 0; (this.dst((req = this.buildPlan()).tile()) > offset || this.shouldSkip(req, core)) && total < this.plans.size; ++total) {
                                    this.plans.removeFirst();
                                    this.plans.addLast(req);
                                }
                            }

                            BuildPlan current = this.buildPlan();
                            if (this.within(current.tile(), offset)) {
                                Tile tile = Vars.world.tile(current.x, current.y);
                                Building var8 = tile.build;
                                ConstructBlock.ConstructBuild entity;
                                if (var8 instanceof ConstructBlock.ConstructBuild && (entity = (ConstructBlock.ConstructBuild) var8) == var8) {
                                    if (tile.team() != this.team && tile.team() != Team.derelict || !current.breaking && entity.cblock != current.block) {
                                        this.plans.removeFirst();
                                        break label590;
                                    }
                                } else if (!current.initialized && !current.breaking && Build.validPlace(current.block, this.team, current.x, current.y, current.rotation)) {
                                    boolean hasAll = infinite || current.isRotation(this.team) || !Structs.contains(current.block.requirements, (i) -> {
                                        return core != null && !core.items.has(i.item);
                                    });
                                    if (hasAll) {
                                        Call.beginPlace(this, current.block, this.team, current.x, current.y, current.rotation);
                                    } else {
                                        current.stuck = true;
                                    }
                                } else {
                                    if (current.initialized || !current.breaking || !Build.validBreak(this.team, current.x, current.y)) {
                                        this.plans.removeFirst();
                                        break label590;
                                    }

                                    Call.beginBreak(this, this.team, current.x, current.y);
                                }

                                if (tile.build instanceof ConstructBlock.ConstructBuild && !current.initialized) {
                                    Core.app.post(() -> {
                                        Events.fire(new EventType.BuildSelectEvent(tile, this.team, this, current.breaking));
                                    });
                                    current.initialized = true;
                                }

                                if (core != null || infinite) {
                                    var8 = tile.build;
                                    if (var8 instanceof ConstructBlock.ConstructBuild && (entity = (ConstructBlock.ConstructBuild) var8) == var8) {
                                        if (current.breaking) {
                                            entity.deconstruct(this, core, 1.0F / entity.buildCost * Time.delta * this.type.buildSpeed * Vars.state.rules.buildSpeedMultiplier);
                                        } else {
                                            entity.construct(this, core, 1.0F / entity.buildCost * Time.delta * this.type.buildSpeed * Vars.state.rules.buildSpeedMultiplier, current.config);
                                        }

                                        current.stuck = Mathf.equal(current.progress, entity.progress);
                                        current.progress = entity.progress;
                                    }
                                }
                            }
                        }
                        break label590;
                    }

                    req = (BuildPlan) it.next();
                    tileR = Vars.world.tile(req.x, req.y);
                } while (tileR != null && (!req.breaking || tileR.block() != Blocks.air) && (req.breaking || (tileR.build == null || tileR.build.rotation != req.rotation) && req.block.rotate || tileR.block() != req.block));

                it.remove();
            }
        }

        boolean can = this.canShoot();
        WeaponMount[] var18 = this.mounts;
        int var21 = var18.length;

        for (int var28 = 0; var28 < var21; ++var28) {
            WeaponMount mount = var18[var28];
            Weapon weapon = mount.weapon;
            mount.reload = Math.max(mount.reload - Time.delta * this.reloadMultiplier, 0.0F);
            float weaponRotation = this.rotation - 90.0F + (weapon.rotate ? mount.rotation : 0.0F);
            float mountX = this.x + Angles.trnsx(this.rotation - 90.0F, weapon.x, weapon.y);
            float mountY = this.y + Angles.trnsy(this.rotation - 90.0F, weapon.x, weapon.y);
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
                    if (ai$temp instanceof FormationAI && (ai = (FormationAI) ai$temp) == ai$temp && ai.leader == this) {
                        var10000 = false;
                        return var10000;
                    }
                }

                var10000 = true;
                return var10000;
            });
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

        Floor floor = this.floorOn();
        if (this.isGrounded() && !this.type.hovering) {
            this.apply(floor.status, floor.statusDuration);
        }

        this.applied.clear();
        this.speedMultiplier = this.damageMultiplier = this.healthMultiplier = this.reloadMultiplier = 1.0F;
        int accepted;
        if (!this.statuses.isEmpty()) {
            accepted = 0;

            label455:
            while (true) {
                while (true) {
                    if (accepted >= this.statuses.size) {
                        break label455;
                    }

                    StatusEntry entry = this.statuses.get(accepted++);
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
                        --accepted;
                        this.statuses.remove(accepted);
                    }
                }
            }
        }

        if (Vars.net.client() && !this.isLocal() || this.isRemote()) {
            this.interpolate();
        }

        Building core = this.closestCore();
        if (core != null && this.mineTile != null && this.mineTile.drop() != null && !this.acceptsItem(this.mineTile.drop()) && this.within(core, 220.0F) && !this.offloadImmediately()) {
            accepted = core.acceptStack(this.item(), this.stack().amount, this);
            if (accepted > 0) {
                Call.transferItemTo(this, this.item(), accepted, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
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
                    if (this.item() == item) {
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

        this.hitTime -= Time.delta / 9.0F;
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

        this.type.update(this);
        if (Vars.state.rules.unitAmmo && this.ammo < (float) this.type.ammoCapacity - 1.0E-4F) {
            this.resupplyTime += Time.delta;
            if (this.resupplyTime > 10.0F) {
                this.type.ammoType.resupply(this);
                this.resupplyTime = 0.0F;
            }
        }

        if (this.abilities.size > 0) {
            Iterator var20 = this.abilities.iterator();

            while (var20.hasNext()) {
                Ability a = (Ability) var20.next();
                a.update(this);
            }
        }

        this.drag = this.type.drag * (this.isGrounded() ? this.floorOn().dragMultiplier : 1.0F);
        if (this.team != Vars.state.rules.waveTeam && Vars.state.hasSpawns() && (!Vars.net.client() || this.isLocal())) {
            offset = Vars.state.rules.dropZoneRadius + this.hitSize / 2.0F + 1.0F;
            Iterator var29 = Vars.spawner.getSpawns().iterator();

            while (var29.hasNext()) {
                Tile spawn = (Tile) var29.next();
                if (this.within(spawn.worldx(), spawn.worldy(), offset)) {
                    this.vel().add(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(1.1F - this.dst(spawn) / offset).scl(0.45F * Time.delta));
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
                offset = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * this.elevation;
                float range = Mathf.range(this.type.engineSize);
                this.type.fallThrusterEffect.at(this.x + Angles.trnsx(this.rotation + 180.0F, offset) + Mathf.range(range), this.y + Angles.trnsy(this.rotation + 180.0F, offset) + Mathf.range(range), Mathf.random());
            }

            this.elevation -= this.type.fallSpeed * Time.delta;
            if (this.isGrounded()) {
                this.destroy();
            }
        }

        Tile tile = this.tileOn();
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

        if (this.ammoCooldown > 0.0F) {
            this.ammoCooldown -= Time.delta;
        }

        if (this.ammo > 0.0F && this.ammoCooldown <= 0.0F && ResupplyPoint.resupply(this.team, this.x, this.y, this.type.ammoResupplyRange, Math.min((float) this.type.ammoResupplyAmount, this.ammo), this.type.ammoType.color, (u) -> {
            return u != this;
        })) {
            this.ammo -= Math.min((float) this.type.ammoResupplyAmount, this.ammo);
            this.ammoCooldown = 5.0F;
        }
    }

    public boolean collides(Hitboxc other) {
        return true;
    }

    public void lookAt(float x, float y) {
        this.lookAt(this.angleTo(x, y));
    }

    public void updateLastPosition() {
        this.deltaX = this.x - this.lastX;
        this.deltaY = this.y - this.lastY;
        this.lastX = this.x;
        this.lastY = this.y;
    }

    public void damageContinuous(float amount) {
        this.damage(amount * Time.delta, this.hitTime <= -1.0F);
    }

    public void readSyncManual(FloatBuffer buffer) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
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

    public Player getPlayer() {
        return this.isPlayer() ? (Player) this.controller : null;
    }

    public void display(Table table) {
        this.type.display(this, table);
    }

    public void aim(Position pos) {
        this.aim(pos.getX(), pos.getY());
    }

    public void afterSync() {
        this.setType(this.type);
        this.controller.unit(this);
    }

    public void kill() {
        if (!this.dead && !Vars.net.client()) {
            Call.unitDeath(this.id);
        }
    }

    public Building core() {
        return this.team.core();
    }

    public void clearBuilding() {
        this.plans.clear();
    }

    public int tileY() {
        return World.toTile(this.y);
    }

    public float floorSpeedMultiplier() {
        Floor on = !this.isFlying() && !this.hovering ? this.floorOn() : Blocks.air.asFloor();
        return on.speedMultiplier * this.speedMultiplier;
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

    public void readSync(Reads read) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
        boolean islocal = this.isLocal();
        this.ammo = read.f();
        this.armor = read.f();
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

        int payloads_LENGTH = read.i();
        this.payloads.clear();

        int statuses_LENGTH;
        for (statuses_LENGTH = 0; statuses_LENGTH < payloads_LENGTH; ++statuses_LENGTH) {
            Payload payloads_ITEM = TypeIO.readPayload(read);
            if (payloads_ITEM != null) {
                this.payloads.add(payloads_ITEM);
            }
        }

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
        this.type = Vars.content.getByID(ContentType.unit, read.s());
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

    public int tileX() {
        return World.toTile(this.x);
    }

    public void controller(UnitController next) {
        this.clearCommand();
        this.controller = next;
        if (this.controller.unit() != this) {
            this.controller.unit(this);
        }

    }

    public void impulse(float x, float y) {
        float mass = this.mass();
        this.vel.add(x / mass, y / mass);
    }

    public void damageContinuousPierce(float amount) {
        this.damagePierce(amount * Time.delta, this.hitTime <= -11.0F);
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

    public boolean isPlayer() {
        return this.controller instanceof Player;
    }

    public boolean canPickup(Unit unit) {
        return this.payloadUsed() + unit.hitSize * unit.hitSize <= this.type.payloadCapacity + 0.001F && unit.team == this.team() && unit.isAI();
    }

    public float mass() {
        return this.hitSize * this.hitSize * 3.1415927F;
    }

    public void eachGroup(Cons<Unit> cons) {
        cons.get(this);
        this.controlling().each(cons);
    }

    public BuildPlan buildPlan() {
        return this.plans.size == 0 ? null : this.plans.first();
    }

    public float range() {
        return this.type.range;
    }

    public void damagePierce(float amount) {
        this.damagePierce(amount, true);
    }

    public boolean canBuild() {
        return this.type.buildSpeed > 0.0F;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int classId() {
        return modVars.MOD_CONTENT_ID;
    }

    @Override
    public int modClassId() {
        return classId;
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

    public int id() {
        return this.id;
    }

    public void id(int id) {
        this.id = id;
    }

    public Team team() {
        return this.team;
    }

    public void team(Team team) {
        this.team = team;
    }

    public PhysicsProcess.PhysicRef physref() {
        return this.physref;
    }

    public void physref(PhysicsProcess.PhysicRef physref) {
        this.physref = physref;
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

    public Vec2 vel() {
        return this.vel;
    }

    public float drag() {
        return this.drag;
    }

    public void drag(float drag) {
        this.drag = drag;
    }

    public float rotation() {
        return this.rotation;
    }

    public void rotation(float rotation) {
        this.rotation = rotation;
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

    public Seq<Payload> payloads() {
        return this.payloads;
    }

    public void payloads(Seq<Payload> payloads) {
        this.payloads = payloads;
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
