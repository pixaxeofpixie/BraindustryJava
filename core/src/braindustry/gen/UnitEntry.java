package braindustry.gen;

import ModVars.modVars;
import arc.Core;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.math.geom.Position;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.io.ModTypeIO;
import mindustry.core.World;
import mindustry.game.Team;
import mindustry.gen.Drawc;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustryAddition.graphics.ModDraw;
@ModAnnotations.WritableObject
public class UnitEntry implements Drawc, WritableInterface {
    private static final Rect viewport = new Rect();
    private UnitType unitType;
    public int amount;
    public Vec2 pos;
    public Team team;
    public boolean added = false;

    public UnitType unitType() {
        if (unitType==null)throw new IllegalArgumentException("UnitType cannot be null");
        return unitType;
    }

    public void unitType(UnitType unitType) {
        if (unitType==null)throw new IllegalArgumentException("UnitType cannot be null");
        this.unitType = unitType;
    }

    public UnitEntry(UnitType unitType, Team team, int amount, Vec2 pos) {
        if (unitType==null)throw new IllegalArgumentException("UnitType cannot be null");
        unitType(unitType);
        this.amount = amount;
        this.pos = pos;
        this.team = team;
    }
    UnitEntry(Reads reads){
        read(reads);
    }
    UnitEntry(){
    }
    public static UnitEntry readEntry(Reads reads){
        return new UnitEntry(reads);
    }

    public static UnitEntry readEntry(Reads read, byte revision) {
        return readEntry(read);
    }

    @Override
    public boolean equals(Object obj) {
        UnitEntry entry = obj instanceof UnitEntry ? (UnitEntry) obj : null;
        if (entry == null) return super.equals(obj);

        return super.equals(obj) || customEquals(entry);
    }

    private boolean customEquals(UnitEntry entry) {
        return entry.unitType == unitType &&
                entry.amount == amount &&
                entry.pos == pos &&
                entry.team == team
                ;
    }

    public float clipSize() {
        return unitType.hitSize + 3;
    }

    @Override
    public void set(float x, float y) {
        pos.set(x, y);
    }

    @Override
    public void set(Position position) {
        pos.set(position);
    }

    @Override
    public void trns(float v, float v1) {

    }

    @Override
    public void trns(Position position) {

    }

    @Override
    public int tileX() {
        return World.toTile(x());
    }

    @Override
    public int tileY() {
        return World.toTile(y());
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
        return x();
    }

    @Override
    public float getY() {
        return y();
    }

    public float x() {
        return pos.x;
    }

    @Override
    public void x(float x) {
        pos.x = x;
    }

    public float y() {
        return pos.y;
    }

    @Override
    public void y(float y) {
        pos.y = y;
    }

    public void draw() {
        Core.camera.bounds(viewport);
        if (viewport.overlaps(x() - clipSize() / 2f, y() - clipSize() / 2f, clipSize(), clipSize()) && amount > 0) {
            Draw.color(team.color);
            Draw.alpha(0.5f);
            Draw.rect(unitType.region, pos.x, pos.y);
            if (amount > 1) ModDraw.drawLabel(pos, amount + "");
            Draw.reset();
        }
    }

    public void spawn() {
        ModCall.spawnUnits(unitType, pos.x, pos.y, amount, false, team);
    }

    @Override
    public boolean isAdded() {
        return added;
    }

    @Override
    public void update() {

    }

    @Override
    public void remove() {
        if (!added) return;
        added = false;
        Groups.draw.remove(this);
    }

    @Override
    public void add() {
        if (added) return;
        added = true;
        Groups.draw.add(this);
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
        return (T) this;
    }

    @Override
    public <T> T as() {
        return (T) this;
    }

    @Override
    public <T> T with(Cons<T> cons) {
        cons.get((T) this);
        return (T) this;
    }

    @Override
    public int classId() {
        return modVars.MOD_CONTENT_ID;
    }

    @Override
    public boolean serialize() {
        return false;
    }



    @Override
    public void read(Reads reads) {
        unitType=ModTypeIO.readUnitType(reads);
        if (unitType==null)throw new IllegalArgumentException("UnitType cannot be null");
        amount=reads.i();
        pos=TypeIO.readVec2(reads);
        team=TypeIO.readTeam(reads);
    }

    public void write(Writes write) {
        if (unitType==null)throw new IllegalArgumentException("UnitType cannot be null");
        ModTypeIO.writeUnitType(write, unitType);
        write.i(amount);
        TypeIO.writeVec2(write, pos);
        ModTypeIO.writeTeam(write, team);
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
}
