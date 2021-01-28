package braindustry.type;

import ModVars.modFunc;
import ModVars.modVars;
import arc.Core;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.math.geom.Position;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.gen.ModCall;
import mindustry.core.World;
import mindustry.game.Team;
import mindustry.gen.Drawc;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustryAddition.graphics.ModDraw;

public class UnitEntry implements Drawc {
    private static final Rect viewport = new Rect();
    public UnitType unitType;
    public int amount;
    public Vec2 pos;
    public Team team;
    public boolean added = false;

    public UnitEntry(UnitType unitType, Team team, int amount, Vec2 pos) {
        this.unitType = unitType;
        this.amount = amount;
        this.pos = pos;
        this.team = team;
    }

    public UnitEntry() {
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
        ModCall.spawnUnits(unitType, pos.x, pos.y, amount,false, team, null);
    }

    public void read(Reads read, byte revision) {
        unitType = TypeIO.readUnitType(read);
        amount = read.i();
        pos = TypeIO.readVec2(read);
        team = TypeIO.readTeam(read);
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

    }

    public void write(Writes write) {
        TypeIO.writeUnitType(write, unitType);
        write.i(amount);
        TypeIO.writeVec2(write, pos);
        TypeIO.writeTeam(write, team);
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
