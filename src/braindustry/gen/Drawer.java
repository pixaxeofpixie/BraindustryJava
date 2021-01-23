package braindustry.gen;

import arc.func.Cons;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.entities.EntityGroup;
import mindustry.gen.*;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.environment.Floor;

import static mindustry.Vars.player;
import static mindustry.Vars.world;

public class Drawer implements Drawc, Posc,Entityc {
    public static int classId=0;
    public float x;

    public float y;

    public interface BuilderDrawer extends Buildingc{
        void drawer();
        Vec2 getPos();
    }
    public transient boolean added;
    public BuilderDrawer build;
    public transient int id = EntityGroup.nextId();
    public Runnable runnable;
    protected Drawer(BuilderDrawer build) {
        this.build=build;
    }
    public boolean serialize() {
        return false;
    }

    @Override
    public String toString() {
        return "ForceDraw#" + id;
    }

    public float getX() {

        return x;
    }

    public float getY() {

        return y;
    }

    public void draw() {
        draw: {

        }
        forcedraw: {

            build.drawer();
        }
    }

    public void update() {

    }

    public boolean isNull() {

        return false;
    }

    public Floor floorOn() {

        Tile tile = tileOn();
        return tile == null || tile.block() != Blocks.air ? (Floor)Blocks.air : tile.floor();
    }

    public Block blockOn() {

        Tile tile = tileOn();
        return tile == null ? Blocks.air : tile.block();
    }

    public boolean isRemote() {

        return ((Object)this) instanceof Unitc && ((Unitc)((Object)this)).isPlayer() && !isLocal();
    }

    public void set(Position pos) {

        set(pos.getX(), pos.getY());
    }

    public void afterRead() {

    }

    public void write(Writes write) {

    }

    public <T extends Entityc> T self() {

        return (T)this;
    }

    public void read(Reads read) {

        afterRead();
    }

    public <T> T as() {

        return (T)this;
    }

    public void trns(float x, float y) {
    }

    public void set(float x, float y) {

        this.x = x;
        this.y = y;
    }

    public void add() {
        if(added) return;
//        Groups.all.add(this);
        Groups.draw.add(this);

        added = true;
    }

    public <T> T with(Cons<T> cons) {

        cons.get((T)this);
        return (T)this;
    }

    public int tileX() {

        return World.toTile(x);
    }

    public int tileY() {

        return World.toTile(y);
    }

    public Tile tileOn() {

        return world.tileWorld(x, y);
    }

    public void remove() {
        if(!added) return;
//        Groups.all.remove(this);
        Groups.draw.remove(this);

        added = false;
    }

    public float clipSize() {

        return build.block().size * 8.0F;
    }

    public void trns(Position pos) {

        trns(pos.getX(), pos.getY());
    }

    public boolean isAdded() {

        return added;
    }

    public boolean onSolid() {

        Tile tile = tileOn();
        return tile == null || tile.solid();
    }

    public boolean isLocal() {

        return ((Object)this) == player || ((Object)this) instanceof Unitc && ((Unitc)((Object)this)).controller() == player;
    }

    public static Drawer create(    BuilderDrawer build) {
        return new Drawer(build);
    }

    @Override
    public int classId() {
        return classId;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
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
    public int id() {
        return id;
    }

    @Override
    public void id(int id) {
        this.id = id;
    }
}
