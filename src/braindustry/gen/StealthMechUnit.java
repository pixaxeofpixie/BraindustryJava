package braindustry.gen;

import arc.Core;
import arc.func.Cons;
import arc.func.Floatc4;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.QuadTree;
import arc.math.geom.Vec2;
import arc.struct.Bits;
import arc.util.Structs;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.entities.EntityCollisions;
import mindustry.entities.units.AIController;
import mindustry.entities.units.BuildPlan;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Hitboxc;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.storage.CoreBlock;
import mindustryAddition.gen.WorkbleMechUnit;

import java.util.Arrays;
import java.util.Iterator;

public class StealthMechUnit extends WorkbleMechUnit {
    public static int classId = 0;
    public boolean inStealth = false;

    public StealthMechUnit() {
        super();

        /*
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.speedMultiplier = 1.0F;
        this.damageMultiplier = 1.0F;
        this.healthMultiplier = 1.0F;
        this.reloadMultiplier = 1.0F;
        this.buildAlpha = 0.0F;*/
    }

    public static StealthMechUnit create() {
        return new StealthMechUnit();
    }

    public String toString() {
        return "StealthMechUnit#" + this.id;
    }

    @Override
    public void update() {
        super.update();
        if (inStealth != this.isShooting) {
            if (inStealth) {
                Groups.unit.add(this);
            } else {
                Groups.unit.remove(this);
            }
            inStealth = this.isShooting;
        }
    }

    public void getCollisions(Cons<QuadTree> consumer) {
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

    public void drawAlpha() {
        Draw.alpha(!inStealth ? Draw.getColor().a : this.isLocal() ? 0.25f : 0f);
    }

    public void draw() {
//        Color color = team.color.cpy();
//        Draw.color(color.cpy());
//        drawRect(Lines::rect, this.x, this.y, this.hitSize);
        Draw.color(isShooting ? Color.red : Color.gray);
        drawRect(Lines::rect, this.x, this.y, this.clipSize());
//        if (true) return;
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

    public boolean isAI() {
        return this.controller instanceof AIController;
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

    public EntityCollisions.SolidPred solidity() {

//        return this.isFlying() ? null : EntityCollisions::solid;
        return this.isFlying() ? null : (x, y) -> !EntityCollisions.solid(x, y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean canPass(int tileX, int tileY) {
        EntityCollisions.SolidPred s = this.solidity();
        return s == null || !s.solid(tileX, tileY);
    }

    public boolean collides(Hitboxc other) {
        return true;
    }

    public int classId() {
        return classId;
    }
}
