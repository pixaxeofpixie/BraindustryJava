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
    }

    @Override
    public float payloadUsed() {
        return payloadc.payloadUsed();
    }

    @Override
    public boolean canPickup(Unit unit) {
        return payloadc.canPickup(unit) && !(unit instanceof StealthUnitc);
    }

    @Override
    public boolean canPickup(Building building) {
        return payloadc.canPickup(building);
    }

    @Override
    public boolean canPickupPayload(Payload payload) {
        return payloadc.canPickupPayload(payload) && !((payload instanceof UnitPayload) && ((UnitPayload) payload).unit instanceof StealthUnitc);
    }

    @Override
    public boolean hasPayload() {
        return payloadc.hasPayload();
    }

    @Override
    public void addPayload(Payload payload) {
        payloadc.addPayload(payload);
    }

    @Override
    public void pickup(Unit unit) {
        if (!(unit instanceof StealthUnitc)) payloadc.pickup(unit);
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
    }

    @Override
    public void approach(Vec2 vec2) {
        payloadc.approach(vec2);
    }

    @Override
    public void aimLook(Position position) {
        payloadc.aimLook(position);
    }

    @Override
    public void aimLook(float v, float v1) {
        payloadc.aimLook(v, v1);
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
    }

    @Override
    public void set(UnitType unitType, UnitController unitController) {
        payloadc.set(unitType, unitController);
    }

    @Override
    public int pathType() {
        return payloadc.pathType();
    }

    @Override
    public void lookAt(float v) {
        payloadc.lookAt(v);
    }

    @Override
    public void lookAt(Position position) {
        payloadc.lookAt(position);
    }

    @Override
    public void lookAt(float v, float v1) {
        payloadc.lookAt(v, v1);
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
    }

    @Override
    public void setupWeapons(UnitType unitType) {
        payloadc.setupWeapons(unitType);
    }

    @Override
    public void controlWeapons(boolean b) {
        payloadc.controlWeapons(b);
    }

    @Override
    public void controlWeapons(boolean b, boolean b1) {
        payloadc.controlWeapons(b, b1);
    }

    @Override
    public void aim(Position position) {
        payloadc.aim(position);
    }

    @Override
    public void aim(float v, float v1) {
        payloadc.aim(v, v1);
    }

    @Override
    public boolean canShoot() {
        return payloadc.canShoot();
    }

    @Override
    public void apply(StatusEffect statusEffect) {
        payloadc.apply(statusEffect);
    }

    @Override
    public void apply(StatusEffect statusEffect, float v) {
        payloadc.apply(statusEffect, v);
    }

    @Override
    public void unapply(StatusEffect statusEffect) {
        payloadc.unapply(statusEffect);
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
    }

    @Override
    public void writeSyncManual(FloatBuffer floatBuffer) {
        payloadc.writeSyncManual(floatBuffer);
    }

    @Override
    public void afterSync() {
        payloadc.afterSync();
    }

    @Override
    public void interpolate() {
        payloadc.interpolate();
    }

    @Override
    public boolean isAdded() {
        return payloadc.isAdded();
    }

    @Override
    public void update() {
        payloadc.update();
    }

    @Override
    public void remove() {
        payloadc.remove();
    }

    @Override
    public void add() {
        payloadc.add();
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
    }

    @Override
    public void write(Writes writes) {
        payloadc.write(writes);
    }

    @Override
    public void afterRead() {
        payloadc.afterRead();
    }

    @Override
    public int id() {
        return payloadc.id();
    }

    @Override
    public void id(int i) {
        payloadc.id(i);
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
    }

    @Override
    public void hitboxTile(Rect rect) {
        payloadc.hitboxTile(rect);
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
        return false;
    } // TODO Всё, что ниже

    @Override
    public void hovering(boolean b) {

    }

    @Override
    public float drownTime() {
        return 0;
    }

    @Override
    public void drownTime(float v) {

    }

    @Override
    public float splashTimer() {
        return 0;
    }

    @Override
    public void splashTimer(float v) {

    }

    @Override
    public void draw() {

    }

    @Override
    public float mineTimer() {
        return 0;
    }

    @Override
    public void mineTimer(float v) {

    }

    @Override
    public Tile mineTile() {
        return null;
    }

    @Override
    public void mineTile(Tile tile) {

    }

    @Override
    public boolean hasEffect(StatusEffect statusEffect) {
        return false;
    }

    @Override
    public float speedMultiplier() {
        return 0;
    }

    @Override
    public float damageMultiplier() {
        return 0;
    }

    @Override
    public float healthMultiplier() {
        return 0;
    }

    @Override
    public float reloadMultiplier() {
        return 0;
    }

    @Override
    public WeaponMount[] mounts() {
        return new WeaponMount[0];
    }

    @Override
    public void mounts(WeaponMount[] weaponMounts) {

    }

    @Override
    public boolean isRotate() {
        return false;
    }

    @Override
    public float aimX() {
        return 0;
    }

    @Override
    public void aimX(float v) {

    }

    @Override
    public float aimY() {
        return 0;
    }

    @Override
    public void aimY(float v) {

    }

    @Override
    public boolean isShooting() {
        return false;
    }

    @Override
    public void isShooting(boolean b) {

    }

    @Override
    public float ammo() {
        return 0;
    }

    @Override
    public void ammo(float v) {

    }

    @Override
    public void controller(UnitController unitController) {

    }

    @Override
    public void commandNearby(FormationPattern formationPattern) {

    }

    @Override
    public void commandNearby(FormationPattern formationPattern, Boolf<Unit> boolf) {

    }

    @Override
    public void command(Formation formation, Seq<Unit> seq) {

    }

    @Override
    public boolean isCommanding() {
        return false;
    }

    @Override
    public void clearCommand() {

    }

    @Override
    public Formation formation() {
        return null;
    }

    @Override
    public void formation(Formation formation) {

    }

    @Override
    public Seq<Unit> controlling() {
        return null;
    }

    @Override
    public void controlling(Seq<Unit> seq) {

    }

    @Override
    public float minFormationSpeed() {
        return 0;
    }

    @Override
    public void minFormationSpeed(float v) {

    }

    @Override
    public void drawBuildPlans() {

    }

    @Override
    public void drawPlan(BuildPlan buildPlan, float v) {

    }

    @Override
    public void drawPlanTop(BuildPlan buildPlan, float v) {

    }

    @Override
    public boolean shouldSkip(BuildPlan buildPlan, Building building) {
        return false;
    }

    @Override
    public void removeBuild(int i, int i1, boolean b) {

    }

    @Override
    public boolean isBuilding() {
        return false;
    }

    @Override
    public void clearBuilding() {

    }

    @Override
    public void addBuild(BuildPlan buildPlan) {

    }

    @Override
    public void addBuild(BuildPlan buildPlan, boolean b) {

    }

    @Override
    public boolean activelyBuilding() {
        return false;
    }

    @Override
    public BuildPlan buildPlan() {
        return null;
    }

    @Override
    public Queue<BuildPlan> plans() {
        return null;
    }

    @Override
    public void plans(Queue<BuildPlan> queue) {

    }

    @Override
    public boolean updateBuilding() {
        return false;
    }

    @Override
    public void updateBuilding(boolean b) {

    }

    @Override
    public float clipSize() {
        return 0;
    }

    @Override
    public float mass() {
        return 0;
    }

    @Override
    public void impulse(float v, float v1) {

    }

    @Override
    public void impulse(Vec2 vec2) {

    }

    @Override
    public void impulseNet(Vec2 vec2) {

    }

    @Override
    public PhysicsProcess.PhysicRef physref() {
        return null;
    }

    @Override
    public void physref(PhysicsProcess.PhysicRef physicRef) {

    }

    @Override
    public boolean cheating() {
        return false;
    }

    @Override
    public Building core() {
        return null;
    }

    @Override
    public Building closestCore() {
        return null;
    }

    @Override
    public Building closestEnemyCore() {
        return null;
    }

    @Override
    public Team team() {
        return null;
    }

    @Override
    public void team(Team team) {

    }

    @Override
    public void set(float v, float v1) {

    }

    @Override
    public void set(Position position) {

    }

    @Override
    public void trns(float v, float v1) {

    }

    @Override
    public void trns(Position position) {

    }

    @Override
    public int tileX() {
        return 0;
    }

    @Override
    public int tileY() {
        return 0;
    }

    @Override
    public Floor floorOn() {
        return null;
    }

    @Override
    public Block blockOn() {
        return null;
    }

    @Override
    public boolean onSolid() {
        return false;
    }

    @Override
    public Tile tileOn() {
        return null;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float x() {
        return 0;
    }

    @Override
    public void x(float v) {

    }

    @Override
    public float y() {
        return 0;
    }

    @Override
    public void y(float v) {

    }

    @Override
    public float rotation() {
        return 0;
    }

    @Override
    public void rotation(float v) {

    }
}
