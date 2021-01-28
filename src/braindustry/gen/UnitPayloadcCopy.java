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

public class UnitPayloadcCopy  extends Unit implements Drawc, Weaponsc, Payloadc, Hitboxc, Unitc, Velc, Posc, Teamc, Rotc, Entityc, Healthc, Commanderc, Builderc, Boundedc, Minerc, Shieldc, Statusc, Itemsc, Syncc, Flyingc, Physicsc {
    public final Unit parent;
    public final Payloadc payloadc;
    public <T extends Unit> UnitPayloadcCopy(T parent){
        this.parent=parent;
        payloadc =(Payloadc)parent;

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
        if (!(unit instanceof StealthUnitc))payloadc.pickup(unit);
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
        payloadc.contentInfo(table,v,v1);
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
        payloadc.aimLook(v,v1);
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
        return payloadc.speed() ;
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
        return payloadc.range() ;
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
        payloadc.set(unitType,unitController);
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
        payloadc.lookAt(v,v1);
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

    }

    @Override
    public String getControllerName() {
        return null;
    }

    @Override
    public void display(Table table) {

    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public UnitType type() {
        return null;
    }

    @Override
    public void type(UnitType unitType) {

    }

    @Override
    public boolean spawnedByCore() {
        return false;
    }

    @Override
    public void spawnedByCore(boolean b) {

    }

    @Override
    public double flag() {
        return 0;
    }

    @Override
    public void flag(double v) {

    }

    @Override
    public Seq<Ability> abilities() {
        return null;
    }

    @Override
    public void abilities(Seq<Ability> seq) {

    }

    @Override
    public boolean canBuild() {
        return false;
    }

    @Override
    public float ammof() {
        return 0;
    }

    @Override
    public void setWeaponRotation(float v) {

    }

    @Override
    public void setupWeapons(UnitType unitType) {

    }

    @Override
    public void controlWeapons(boolean b) {

    }

    @Override
    public void controlWeapons(boolean b, boolean b1) {

    }

    @Override
    public void aim(Position position) {

    }

    @Override
    public void aim(float v, float v1) {

    }

    @Override
    public boolean canShoot() {
        return false;
    }

    @Override
    public void apply(StatusEffect statusEffect) {

    }

    @Override
    public void apply(StatusEffect statusEffect, float v) {

    }

    @Override
    public void unapply(StatusEffect statusEffect) {

    }

    @Override
    public boolean isBoss() {
        return false;
    }

    @Override
    public boolean isImmune(StatusEffect statusEffect) {
        return false;
    }

    @Override
    public Color statusColor() {
        return null;
    }

    @Override
    public boolean checkTarget(boolean b, boolean b1) {
        return false;
    }

    @Override
    public boolean isGrounded() {
        return false;
    }

    @Override
    public boolean isFlying() {
        return false;
    }

    @Override
    public boolean canDrown() {
        return false;
    }

    @Override
    public void landed() {

    }

    @Override
    public void wobble() {

    }

    @Override
    public void moveAt(Vec2 vec2, float v) {

    }

    @Override
    public float floorSpeedMultiplier() {
        return 0;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public float healthf() {
        return 0;
    }

    @Override
    public boolean canMine(Item item) {
        return false;
    }

    @Override
    public boolean offloadImmediately() {
        return false;
    }

    @Override
    public boolean mining() {
        return false;
    }

    @Override
    public boolean validMine(Tile tile, boolean b) {
        return false;
    }

    @Override
    public boolean validMine(Tile tile) {
        return false;
    }

    @Override
    public boolean canMine() {
        return false;
    }

    @Override
    public int itemCapacity() {
        return 0;
    }

    @Override
    public void snapSync() {

    }

    @Override
    public void snapInterpolation() {

    }

    @Override
    public void readSync(Reads reads) {

    }

    @Override
    public void writeSync(Writes writes) {

    }

    @Override
    public void readSyncManual(FloatBuffer floatBuffer) {

    }

    @Override
    public void writeSyncManual(FloatBuffer floatBuffer) {

    }

    @Override
    public void afterSync() {

    }

    @Override
    public void interpolate() {

    }

    @Override
    public boolean isAdded() {
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void add() {

    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public <T extends Entityc> T self() {
        return null;
    }

    @Override
    public <T> T as() {
        return null;
    }

    @Override
    public <T> T with(Cons<T> cons) {
        return null;
    }

    @Override
    public int classId() {
        return 0;
    }

    @Override
    public boolean serialize() {
        return false;
    }

    @Override
    public void read(Reads reads) {

    }

    @Override
    public void write(Writes writes) {

    }

    @Override
    public void afterRead() {

    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void id(int i) {

    }

    @Override
    public EntityCollisions.SolidPred solidity() {
        return null;
    }

    @Override
    public boolean canPass(int i, int i1) {
        return false;
    }

    @Override
    public boolean canPassOn() {
        return false;
    }

    @Override
    public boolean moving() {
        return false;
    }

    @Override
    public void move(float v, float v1) {

    }

    @Override
    public Vec2 vel() {
        return null;
    }

    @Override
    public float drag() {
        return 0;
    }

    @Override
    public void drag(float v) {

    }

    @Override
    public long lastUpdated() {
        return 0;
    }

    @Override
    public void lastUpdated(long l) {

    }

    @Override
    public long updateSpacing() {
        return 0;
    }

    @Override
    public void updateSpacing(long l) {

    }

    @Override
    public Item item() {
        return null;
    }

    @Override
    public void clearItem() {

    }

    @Override
    public boolean acceptsItem(Item item) {
        return false;
    }

    @Override
    public boolean hasItem() {
        return false;
    }

    @Override
    public void addItem(Item item) {

    }

    @Override
    public void addItem(Item item, int i) {

    }

    @Override
    public int maxAccepted(Item item) {
        return 0;
    }

    @Override
    public ItemStack stack() {
        return null;
    }

    @Override
    public void stack(ItemStack itemStack) {

    }

    @Override
    public float itemTime() {
        return 0;
    }

    @Override
    public void itemTime(float v) {

    }

    @Override
    public void getCollisions(Cons<QuadTree> cons) {

    }

    @Override
    public void updateLastPosition() {

    }

    @Override
    public void collision(Hitboxc hitboxc, float v, float v1) {

    }

    @Override
    public float deltaLen() {
        return 0;
    }

    @Override
    public float deltaAngle() {
        return 0;
    }

    @Override
    public boolean collides(Hitboxc hitboxc) {
        return false;
    }

    @Override
    public void hitbox(Rect rect) {

    }

    @Override
    public void hitboxTile(Rect rect) {

    }

    @Override
    public float lastX() {
        return 0;
    }

    @Override
    public void lastX(float v) {

    }

    @Override
    public float lastY() {
        return 0;
    }

    @Override
    public void lastY(float v) {

    }

    @Override
    public float deltaX() {
        return 0;
    }

    @Override
    public void deltaX(float v) {

    }

    @Override
    public float deltaY() {
        return 0;
    }

    @Override
    public void deltaY(float v) {

    }

    @Override
    public float hitSize() {
        return 0;
    }

    @Override
    public void hitSize(float v) {

    }

    @Override
    public void killed() {

    }

    @Override
    public void kill() {

    }

    @Override
    public void heal() {

    }

    @Override
    public boolean damaged() {
        return false;
    }

    @Override
    public void damagePierce(float v, boolean b) {

    }

    @Override
    public void damagePierce(float v) {

    }

    @Override
    public void damage(float v) {

    }

    @Override
    public void damage(float v, boolean b) {

    }

    @Override
    public void damageContinuous(float v) {

    }

    @Override
    public void damageContinuousPierce(float v) {

    }

    @Override
    public void clampHealth() {

    }

    @Override
    public void heal(float v) {

    }

    @Override
    public void healFract(float v) {

    }

    @Override
    public float health() {
        return 0;
    }

    @Override
    public void health(float v) {

    }

    @Override
    public float hitTime() {
        return 0;
    }

    @Override
    public void hitTime(float v) {

    }

    @Override
    public float maxHealth() {
        return 0;
    }

    @Override
    public void maxHealth(float v) {

    }

    @Override
    public boolean dead() {
        return false;
    }

    @Override
    public void dead(boolean b) {

    }

    @Override
    public float shield() {
        return 0;
    }

    @Override
    public void shield(float v) {

    }

    @Override
    public float armor() {
        return 0;
    }

    @Override
    public void armor(float v) {

    }

    @Override
    public float shieldAlpha() {
        return 0;
    }

    @Override
    public void shieldAlpha(float v) {

    }

    @Override
    public float elevation() {
        return 0;
    }

    @Override
    public void elevation(float v) {

    }

    @Override
    public boolean hovering() {
        return false;
    }

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
