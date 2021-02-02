package braindustry.gen;

import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Position;
import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationPattern;
import mindustry.async.PhysicsProcess;
import mindustry.ctype.Content;
import mindustry.entities.EntityCollisions;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.BuildPlan;
import mindustry.entities.units.UnitController;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.UnitPayload;

import java.nio.FloatBuffer;

public class UnitPayloadcCopy extends Unit implements Drawc, Weaponsc, Payloadc, Hitboxc, Unitc, Velc, Posc, Teamc, Rotc, Entityc, Healthc, Commanderc, Builderc, Boundedc, Minerc, Shieldc, Statusc, Itemsc, Syncc, Flyingc, Physicsc {
    public final Unit parent;
    public final Payloadc payloadc;

    public <T extends Unit> UnitPayloadcCopy(T parent) {
        this.parent = parent;
        payloadc = (Payloadc) parent;

        Log.info("NNNNNNNNNNN");
    }

    @Override
    public float payloadUsed() {
        return payloadc.payloadUsed();
    }

    @Override
    public boolean canPickup(Unit unit) {
        Log.info("c unit: @",unit.toString());
        return payloadc.canPickup(unit) && !isStealth(unit);
    }

    @Override
    public boolean canPickup(Building building) {
        return payloadc.canPickup(building);
    }
    protected boolean isStealth(Object o){
        Log.info(o.toString());
        if (o instanceof StealthUnitc){
            return true;
        } else if (o instanceof UnitPayload){
            return isStealth(((UnitPayload) o).unit);
        }
        return false;
    }
    protected void check(){
        x=parent.x;
        y=parent.y;
        mounts=parent.mounts;
        aimX=parent.aimX;
        aimY=parent.aimY;
        isShooting=parent.isShooting;
        ammo=parent.ammo;
        elevation=parent.elevation;
        hovering=parent.hovering;
        drownTime=parent.drownTime;
        splashTimer=parent.splashTimer;
        physref=parent.physref;
        plans=parent.plans;
        hitSize=parent.hitSize;
        type=parent.type;
        abilities=parent.abilities;
        controlling=parent.controlling;
        armor=parent.armor;
        id=parent.id;
        parent.controller().unit(this);
        if(parent.isPlayer()){
            parent.getPlayer().unit(this);
        }
    }
    @Override
    public boolean canPickupPayload(Payload payload) {
        Log.info("c payload: @",payload.toString());
        return payloadc.canPickupPayload(payload) && !isStealth(payload);
    }

    @Override
    public boolean hasPayload() {
        return payloadc.hasPayload();
    }

    @Override
    public void addPayload(Payload payload) {
        Log.info("payload: @",payload.toString());
         if(!isStealth(payload))payloadc.addPayload(payload);
    }

    @Override
    public void pickup(Unit unit) {
        Log.info("unit: @",unit.toString());
        if (!isStealth(unit)) payloadc.pickup(unit);
    }

    @Override
    public void pickup(Building building) {
        payloadc.pickup(building);
    }

    @Override
    public boolean dropLastPayload() {
        return payloadc.dropLastPayload();
    }

    @Override
    public boolean tryDropPayload(Payload payload) {
        return payloadc.tryDropPayload(payload);
    }

    @Override
    public boolean dropUnit(UnitPayload unitPayload) {
        return payloadc.dropUnit(unitPayload);
    }

    @Override
    public boolean dropBlock(BuildPayload buildPayload) {
        return payloadc.dropBlock(buildPayload);
    }

    @Override
    public void contentInfo(Table table, float v, float v1) {
        payloadc.contentInfo(table, v, v1);
    }

    @Override
    public Seq<Payload> payloads() {
        return payloadc.payloads();
    }

    @Override
    public void payloads(Seq<Payload> seq) {
        payloadc.payloads(seq);
    }

    @Override
    public void moveAt(Vec2 vec2) {
        payloadc.moveAt(vec2);
        check();

    }

    @Override
    public void approach(Vec2 vec2) {
        payloadc.approach(vec2);
        check();
    }

    @Override
    public void aimLook(Position position) {
        payloadc.aimLook(position);
        check();
    }

    @Override
    public void aimLook(float v, float v1) {
        payloadc.aimLook(v, v1);
        check();
    }

    @Override
    public boolean inRange(Position position) {
        return payloadc.inRange(position);
    }

    @Override
    public boolean hasWeapons() {
        return payloadc.hasWeapons();
    }

    @Override
    public float speed() {
        return payloadc.speed();
    }

    @Override
    public float realSpeed() {
        return payloadc.realSpeed();
    }

    @Override
    public void eachGroup(Cons<Unit> cons) {
        payloadc.eachGroup(cons);
        check();
    }

    @Override
    public float prefRotation() {
        return payloadc.prefRotation();
    }

    @Override
    public float range() {
        return payloadc.range();
    }

    @Override
    public double sense(LAccess lAccess) {
        return payloadc.sense(lAccess);
    }

    @Override
    public Object senseObject(LAccess lAccess) {
        return payloadc.senseObject(lAccess);
    }

    @Override
    public double sense(Content content) {
        return payloadc.sense(content);
    }

    @Override
    public float bounds() {
        return payloadc.bounds();
    }

    @Override
    public UnitController controller() {
        return payloadc.controller();
    }

    @Override
    public void resetController() {
        payloadc.resetController();
        check();
    }

    @Override
    public void set(UnitType unitType, UnitController unitController) {
        payloadc.set(unitType, unitController);
        unitController.unit(this);
        check();
    }

    @Override
    public int pathType() {
        return payloadc.pathType();
    }

    @Override
    public void lookAt(float v) {
        payloadc.lookAt(v);
        check();
    }

    @Override
    public void lookAt(Position position) {
        payloadc.lookAt(position);
        check();
    }

    @Override
    public void lookAt(float v, float v1) {
        payloadc.lookAt(v, v1);
        check();
    }

    @Override
    public boolean isAI() {
        return payloadc.isAI();
    }

    @Override
    public int count() {
        return payloadc.count();
    }

    @Override
    public int cap() {
        return payloadc.cap();
    }

    @Override
    public void setType(UnitType unitType) {
        payloadc.setType(unitType);
        check();
    }

    @Override
    public TextureRegion icon() {
        return payloadc.icon();
    }

    @Override
    public void destroy() {
        payloadc.destroy();
    }

    @Override
    public String getControllerName() {
        return payloadc.getControllerName();
    }

    @Override
    public void display(Table table) {
        payloadc.display(table);
    }

    @Override
    public boolean isPlayer() {
        return payloadc.isPlayer();
    }

    @Override
    public Player getPlayer() {
        return payloadc.getPlayer();
    }

    @Override
    public UnitType type() {
        return payloadc.type();
    }

    @Override
    public void type(UnitType unitType) {
        payloadc.type(unitType);
        check();
    }

    @Override
    public boolean spawnedByCore() {
        return payloadc.spawnedByCore();
    }

    @Override
    public void spawnedByCore(boolean b) {
        payloadc.spawnedByCore(b);
    }

    @Override
    public double flag() {
        return payloadc.flag();
    }

    @Override
    public void flag(double v) {
        payloadc.flag(v);
    }

    @Override
    public Seq<Ability> abilities() {
        return payloadc.abilities();
    }

    @Override
    public void abilities(Seq<Ability> seq) {
        payloadc.abilities(seq);
        check();
    }

    @Override
    public boolean canBuild() {
        return payloadc.canBuild();
    }

    @Override
    public float ammof() {
        return payloadc.ammof();
    }

    @Override
    public void setWeaponRotation(float v) {
        payloadc.setWeaponRotation(v);
        check();
    }

    @Override
    public void setupWeapons(UnitType unitType) {
        payloadc.setupWeapons(unitType);
        check();
    }

    @Override
    public void controlWeapons(boolean b) {
        payloadc.controlWeapons(b);
        check();
    }

    @Override
    public void controlWeapons(boolean b, boolean b1) {
        payloadc.controlWeapons(b, b1);
        check();
    }

    @Override
    public void aim(Position position) {
        payloadc.aim(position);
        check();
    }

    @Override
    public void aim(float v, float v1) {
        payloadc.aim(v, v1);
        check();
    }

    @Override
    public boolean canShoot() {
        return payloadc.canShoot();
    }

    @Override
    public void apply(StatusEffect statusEffect) {
        payloadc.apply(statusEffect);
        check();
    }

    @Override
    public void apply(StatusEffect statusEffect, float v) {
        payloadc.apply(statusEffect, v);
        check();
    }

    @Override
    public void unapply(StatusEffect statusEffect) {
        payloadc.unapply(statusEffect);
        check();
    }

    @Override
    public boolean isBoss() {
        return payloadc.isBoss();
    }

    @Override
    public boolean isImmune(StatusEffect statusEffect) {
        return payloadc.isImmune(statusEffect);
    }

    @Override
    public Color statusColor() {
        return payloadc.statusColor();
    }

    @Override
    public boolean checkTarget(boolean b, boolean b1) {
        return payloadc.checkTarget(b, b1);
    }

    @Override
    public boolean isGrounded() {
        return payloadc.isGrounded();
    }

    @Override
    public boolean isFlying() {
        return payloadc.isFlying();
    }

    @Override
    public boolean canDrown() {
        return payloadc.canDrown();
    }

    @Override
    public void landed() {
        payloadc.landed();
    }

    @Override
    public void wobble() {
        payloadc.wobble();
    }

    @Override
    public void moveAt(Vec2 vec2, float v) {
        payloadc.moveAt(vec2, v);
        check();
    }

    @Override
    public float floorSpeedMultiplier() {
        return payloadc.floorSpeedMultiplier();
    }

    @Override
    public boolean isValid() {
        return payloadc.isValid();
    }

    @Override
    public float healthf() {
        return payloadc.healthf();
    }

    @Override
    public boolean canMine(Item item) {
        return payloadc.canMine(item);
    }

    @Override
    public boolean offloadImmediately() {
        return payloadc.offloadImmediately();
    }

    @Override
    public boolean mining() {
        return payloadc.mining();
    }

    @Override
    public boolean validMine(Tile tile, boolean b) {
        return payloadc.validMine(tile, b);
    }

    @Override
    public boolean validMine(Tile tile) {
        return payloadc.validMine(tile);
    }

    @Override
    public boolean canMine() {
        return payloadc.canMine();
    }

    @Override
    public int itemCapacity() {
        return payloadc.itemCapacity();
    }

    @Override
    public void snapSync() {
        payloadc.snapSync();
    }

    @Override
    public void snapInterpolation() {
        payloadc.snapInterpolation();
    }

    @Override
    public void readSync(Reads reads) {
        payloadc.readSync(reads);
    }

    @Override
    public void writeSync(Writes writes) {
        payloadc.writeSync(writes);
    }

    @Override
    public void readSyncManual(FloatBuffer floatBuffer) {
        payloadc.readSyncManual(floatBuffer);
        check();
    }

    @Override
    public void writeSyncManual(FloatBuffer floatBuffer) {
        payloadc.writeSyncManual(floatBuffer);
        check();
    }

    @Override
    public void afterSync() {
        payloadc.afterSync();
        check();
    }

    @Override
    public void interpolate() {
        payloadc.interpolate();
        check();
    }

    @Override
    public boolean isAdded() {
        return payloadc.isAdded();
    }

    @Override
    public void update() {
        payloadc.update();
        if (added && !parent.isValid()){
            remove();
        }
        check();
    }

    @Override
    public void remove() {
        if (added) {
            added=false;
            Unit parent=(Unit)payloadc;
            parent.remove();
            Groups.unit.remove(this);
            Groups.all.remove(this);
            Groups.sync.remove(this);
        }
    }
    public boolean added=false;
    @Override
    public void add() {

        if (!added) {
            added=true;
            Unit parent=(Unit)payloadc;
            parent.add();
            Groups.all.remove(parent);
            Groups.unit.remove(parent);
            if (Groups.sync.contains(b->b==parent)){
                Groups.sync.remove(parent);
                Groups.sync.add(this);
            }
            Groups.unit.add(this);
            Groups.all.add(this);
        }
    }

    @Override
    public boolean isLocal() {
        return payloadc.isLocal();
    }

    @Override
    public boolean isRemote() {
        return payloadc.isRemote();
    }

    @Override
    public boolean isNull() {
        return payloadc.isNull();
    }

    @Override
    public <T extends Entityc> T self() {
        return payloadc.self();
    }

    @Override
    public <T> T as() {
        return payloadc.as();
    }

    @Override
    public <T> T with(Cons<T> cons) {
        return payloadc.with(cons);
    }

    @Override
    public int classId() {
        return payloadc.classId();
    }

    @Override
    public boolean serialize() {
        return payloadc.serialize();
    }

    @Override
    public void read(Reads reads) {
        payloadc.read(reads);
        check();
    }

    @Override
    public void write(Writes writes) {
        payloadc.write(writes);
        check();
    }

    @Override
    public void afterRead() {
        payloadc.afterRead();
        check();
    }

    @Override
    public int id() {
        return payloadc.id();
    }

    @Override
    public void id(int i) {
        payloadc.id(i);
        check();
    }

    @Override
    public EntityCollisions.SolidPred solidity() {
        return payloadc.solidity();
    }

    @Override
    public boolean canPass(int i, int i1) {
        return payloadc.canPass(i, i1);
    }

    @Override
    public boolean canPassOn() {
        return payloadc.canPassOn();
    }

    @Override
    public boolean moving() {
        return payloadc.moving();
    }

    @Override
    public void move(float v, float v1) {
        payloadc.move(v, v1);
        check();
    }

    @Override
    public Vec2 vel() {
        return payloadc.vel();
    }

    @Override
    public float drag() {
        return payloadc.drag();
    }

    @Override
    public void drag(float v) {
        payloadc.drag(v);
        check();
    }

    @Override
    public long lastUpdated() {
        return payloadc.lastUpdated();
    }

    @Override
    public void lastUpdated(long l) {
        payloadc.lastUpdated(l);
    }

    @Override
    public long updateSpacing() {
        return payloadc.updateSpacing();
    }

    @Override
    public void updateSpacing(long l) {
        payloadc.updateSpacing(l);
    }

    @Override
    public Item item() {
        return payloadc.item();
    }

    @Override
    public void clearItem() {
        payloadc.clearItem();
    }

    @Override
    public boolean acceptsItem(Item item) {
        return payloadc.acceptsItem(item);
    }

    @Override
    public boolean hasItem() {
        return payloadc.hasItem();
    }

    @Override
    public void addItem(Item item) {
        payloadc.addItem(item);
    }

    @Override
    public void addItem(Item item, int i) {
        payloadc.addItem(item, i);
    }

    @Override
    public int maxAccepted(Item item) {
        return payloadc.maxAccepted(item);
    }

    @Override
    public ItemStack stack() {
        return payloadc.stack();
    }

    @Override
    public void stack(ItemStack itemStack) {
        payloadc.stack(itemStack);
    }

    @Override
    public float itemTime() {
        return payloadc.itemTime();
    }

    @Override
    public void itemTime(float v) {
        payloadc.itemTime(v);
    }

    @Override
    public void getCollisions(Cons<QuadTree> cons) {
        payloadc.getCollisions(cons);
    }

    @Override
    public void updateLastPosition() {
        payloadc.updateLastPosition();
    }

    @Override
    public void collision(Hitboxc hitboxc, float v, float v1) {
        payloadc.collision(hitboxc, v, v1);
    }

    @Override
    public float deltaLen() {
        return payloadc.deltaLen();
    }

    @Override
    public float deltaAngle() {
        return payloadc.deltaAngle();
    }

    @Override
    public boolean collides(Hitboxc hitboxc) {
        return payloadc.collides(hitboxc);
    }

    @Override
    public void hitbox(Rect rect) {
        payloadc.hitbox(rect);
        check();
    }

    @Override
    public void hitboxTile(Rect rect) {
        payloadc.hitboxTile(rect);
        check();
    }

    @Override
    public float lastX() {
        return payloadc.lastX();
    }

    @Override
    public void lastX(float v) {
        payloadc.lastX(v);
    }

    @Override
    public float lastY() {
        return payloadc.lastY();
    }

    @Override
    public void lastY(float v) {
        payloadc.lastY(v);
    }

    @Override
    public float deltaX() {
        return payloadc.deltaX();
    }

    @Override
    public void deltaX(float v) {
        payloadc.deltaX(v);
    }

    @Override
    public float deltaY() {
        return payloadc.deltaY();
    }

    @Override
    public void deltaY(float v) {
        payloadc.deltaY(v);
    }

    @Override
    public float hitSize() {
        return payloadc.hitSize();
    }

    @Override
    public void hitSize(float v) {
        payloadc.hitSize(v);
    }

    @Override
    public void killed() {
        payloadc.killed();
    }

    @Override
    public void kill() {
        payloadc.kill();
    }

    @Override
    public void heal() {
        payloadc.heal();
    }

    @Override
    public boolean damaged() {
        return payloadc.damaged();
    }

    @Override
    public void damagePierce(float v, boolean b) {
        payloadc.damagePierce(v, b);
    }

    @Override
    public void damagePierce(float v) {
        payloadc.damagePierce(v);
    }

    @Override
    public void damage(float v) {
        payloadc.damage(v);
    }

    @Override
    public void damage(float v, boolean b) {
        payloadc.damage(v, b);
    }

    @Override
    public void damageContinuous(float v) {
        payloadc.damageContinuous(v);
    }

    @Override
    public void damageContinuousPierce(float v) {
        payloadc.damageContinuousPierce(v);
    }

    @Override
    public void clampHealth() {
        payloadc.clampHealth();
    }

    @Override
    public void heal(float v) {
        payloadc.heal(v);
    }

    @Override
    public void healFract(float v) {
        payloadc.healFract(v);
    }

    @Override
    public float health() {
        return payloadc.health();
    }

    @Override
    public void health(float v) {
        payloadc.health(v);
    }

    @Override
    public float hitTime() {
        return payloadc.hitTime();
    }

    @Override
    public void hitTime(float v) {
        payloadc.hitTime(v);
    }

    @Override
    public float maxHealth() {
        return payloadc.maxHealth();
    }

    @Override
    public void maxHealth(float v) {
        payloadc.maxHealth(v);
    }

    @Override
    public boolean dead() {
        return payloadc.dead();
    }

    @Override
    public void dead(boolean b) {
        payloadc.dead(b);
    }

    @Override
    public float shield() {
        return payloadc.shield();
    }

    @Override
    public void shield(float v) {
        payloadc.shield(v);
    }

    @Override
    public float armor() {
        return payloadc.armor();
    }

    @Override
    public void armor(float v) {
        payloadc.armor(v);
    }

    @Override
    public float shieldAlpha() {
        return payloadc.shieldAlpha();
    }

    @Override
    public void shieldAlpha(float v) {
        payloadc.shield(v);
    }

    @Override
    public float elevation() {
        return payloadc.elevation();
    }

    @Override
    public void elevation(float v) {
        payloadc.elevation(v);
    }

    @Override
    public boolean hovering() {
        return payloadc.hovering();
    }

    @Override
    public void hovering(boolean b) {
        payloadc.hovering(b);
    }

    @Override
    public float drownTime() {
        return payloadc.drownTime();
    }

    @Override
    public void drownTime(float v) {
        payloadc.drownTime(v);
    }

    @Override
    public float splashTimer() {
        return payloadc.splashTimer();
    }

    @Override
    public void splashTimer(float v) {
        payloadc.splashTimer(v);
    }

    @Override
    public void draw() {
        payloadc.draw();
    }

    @Override
    public float mineTimer() {
        return payloadc.mineTimer();
    }

    @Override
    public void mineTimer(float v) {
        payloadc.mineTimer(v);
    }

    @Override
    public Tile mineTile() {
        return payloadc.mineTile();
    }

    @Override
    public void mineTile(Tile tile) {
        payloadc.mineTile(tile);
    }

    @Override
    public boolean hasEffect(StatusEffect statusEffect) {
        return payloadc.hasEffect(statusEffect);
    }

    @Override
    public float speedMultiplier() {
        return payloadc.speedMultiplier();
    }

    @Override
    public float damageMultiplier() {
        return payloadc.damageMultiplier();
    }

    @Override
    public float healthMultiplier() {
        return payloadc.healthMultiplier();
    }

    @Override
    public float reloadMultiplier() {
        return payloadc.reloadMultiplier();
    }

    @Override
    public WeaponMount[] mounts() {
        return payloadc.mounts();
    }

    @Override
    public void mounts(WeaponMount[] weaponMounts) {
        payloadc.mounts(weaponMounts);
    }

    @Override
    public boolean isRotate() {
        return payloadc.isRotate();
    }

    @Override
    public float aimX() {
        return payloadc.aimX();
    }

    @Override
    public void aimX(float v) {
        payloadc.aimX(v);
    }

    @Override
    public float aimY() {
        return payloadc.aimY();
    }

    @Override
    public void aimY(float v) {
        payloadc.aimY(v);
    }

    @Override
    public boolean isShooting() {
        return payloadc.isShooting();
    }

    @Override
    public void isShooting(boolean b) {
        payloadc.isShooting(b);
    }

    @Override
    public float ammo() {
        return payloadc.ammo();
    }

    @Override
    public void ammo(float v) {
        payloadc.ammo(v);
    }

    @Override
    public void controller(UnitController unitController) {
        payloadc.controller(unitController);
    }

    @Override
    public void commandNearby(FormationPattern formationPattern) {
        payloadc.commandNearby(formationPattern);
    }

    @Override
    public void commandNearby(FormationPattern formationPattern, Boolf<Unit> boolf) {
        payloadc.commandNearby(formationPattern, boolf);
    }

    @Override
    public void command(Formation formation, Seq<Unit> seq) {
        payloadc.command(formation, seq);
    }

    @Override
    public boolean isCommanding() {
        return payloadc.isCommanding();
    }

    @Override
    public void clearCommand() {
        payloadc.clearCommand();
    }

    @Override
    public Formation formation() {
        return payloadc.formation();
    }

    @Override
    public void formation(Formation formation) {
        payloadc.formation(formation);
    }

    @Override
    public Seq<Unit> controlling() {
        return payloadc.controlling();
    }

    @Override
    public void controlling(Seq<Unit> seq) {
        payloadc.controlling(seq);
    }

    @Override
    public float minFormationSpeed() {
        return payloadc.minFormationSpeed();
    }

    @Override
    public void minFormationSpeed(float v) {
        payloadc.minFormationSpeed(v);
    }

    @Override
    public void drawBuildPlans() {
        payloadc.drawBuildPlans();
    }

    @Override
    public void drawPlan(BuildPlan buildPlan, float v) {
        payloadc.drawPlan(buildPlan, v);
    }

    @Override
    public void drawPlanTop(BuildPlan buildPlan, float v) {
        payloadc.drawPlanTop(buildPlan, v);
    }

    @Override
    public boolean shouldSkip(BuildPlan buildPlan, Building building) {
        return payloadc.shouldSkip(buildPlan, building);
    }

    @Override
    public void removeBuild(int i, int i1, boolean b) {
        payloadc.removeBuild(i, i1, b);
    }

    @Override
    public boolean isBuilding() {
        return payloadc.isBuilding();
    }

    @Override
    public void clearBuilding() {
        payloadc.clearBuilding();
    }

    @Override
    public void addBuild(BuildPlan buildPlan) {
        payloadc.addBuild(buildPlan);
    }

    @Override
    public void addBuild(BuildPlan buildPlan, boolean b) {
        payloadc.addBuild(buildPlan, b);
    }

    @Override
    public boolean activelyBuilding() {
        return payloadc.activelyBuilding();
    }

    @Override
    public BuildPlan buildPlan() {
        return payloadc.buildPlan();
    }

    @Override
    public Queue<BuildPlan> plans() {
        return payloadc.plans();
    }

    @Override
    public void plans(Queue<BuildPlan> queue) {
        payloadc.plans(queue);
    }

    @Override
    public boolean updateBuilding() {
        return payloadc.updateBuilding();
    }

    @Override
    public void updateBuilding(boolean b) {
        payloadc.updateBuilding(b);
    }

    @Override
    public float clipSize() {
        return payloadc.clipSize();
    }

    @Override
    public float mass() {
        return payloadc.mass();
    }

    @Override
    public void impulse(float v, float v1) {
        payloadc.impulse(v, v1);
    }

    @Override
    public void impulse(Vec2 vec2) {
        payloadc.impulse(vec2);
    }

    @Override
    public void impulseNet(Vec2 vec2) {
        payloadc.impulseNet(vec2);
    }

    @Override
    public PhysicsProcess.PhysicRef physref() {
        return payloadc.physref();
    }

    @Override
    public void physref(PhysicsProcess.PhysicRef physicRef) {
        payloadc.physref(physicRef);
    }

    @Override
    public boolean cheating() {
        return payloadc.cheating();
    }

    @Override
    public Building core() {
        return payloadc.core();
    }

    @Override
    public Building closestCore() {
        return payloadc.closestCore();
    }

    @Override
    public Building closestEnemyCore() {
        return payloadc.closestEnemyCore();
    }

    @Override
    public Team team() {
        return payloadc.team();
    }

    @Override
    public void team(Team team) {
        payloadc.team(team);
    }

    @Override
    public void set(float v, float v1) {
        payloadc.set(v, v1);
    }

    @Override
    public void set(Position position) {
        payloadc.set(position);
    }

    @Override
    public void trns(float v, float v1) {
        payloadc.trns(v, v1);
    }

    @Override
    public void trns(Position position) {
        payloadc.trns(position);
    }

    @Override
    public int tileX() {
        return payloadc.tileX();
    }

    @Override
    public int tileY() {
        return payloadc.tileY();
    }

    @Override
    public Floor floorOn() {
        return payloadc.floorOn();
    }

    @Override
    public Block blockOn() {
        return payloadc.blockOn();
    }

    @Override
    public boolean onSolid() {
        return payloadc.onSolid();
    }

    @Override
    public Tile tileOn() {
        return payloadc.tileOn();
    }

    @Override
    public float getX() {
        return payloadc.getX();
    }

    @Override
    public float getY() {
        return payloadc.getY();
    }

    @Override
    public float x() {
        return payloadc.x();
    }

    @Override
    public void x(float v) {
        payloadc.x(v);
    }

    @Override
    public float y() {
        return payloadc.y();
    }

    @Override
    public void y(float v) {
        payloadc.y(v);
    }

    @Override
    public float rotation() {
        return payloadc.rotation();
    }

    @Override
    public void rotation(float v) {
        payloadc.rotation(v);
    }
}
