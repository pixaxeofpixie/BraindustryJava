package braindustry.gen;

import ModVars.modVars;
import arc.Core;
import arc.func.Boolf;
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
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Log;
import arc.util.Structs;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.entities.ModUnits;
import braindustry.input.ModBinding;
import braindustry.type.StealthUnitType;
import mindustry.Vars;
import mindustry.ai.formations.DistanceAssignmentStrategy;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationPattern;
import mindustry.entities.EntityCollisions;
import mindustry.entities.Units;
import mindustry.entities.units.BuildPlan;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Hitboxc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.storage.CoreBlock;
import mindustryAddition.gen.CopyMechUnit;
import mindustryAddition.gen.ModEntityMapping;
import mindustryAddition.versions.ModEntityc;

import java.util.Arrays;

public class StealthMechUnit extends CopyMechUnit implements StealthUnitc, ModEntityc {
    public static int classId = 0;
    public boolean inStealth = false;
    public float cooldownStealth = 0;
    public float durationStealth = 0;
    public boolean healing;
    public boolean longPress = false;
    StealthUnitType stealthType;
    boolean check = false, check2 = false;
    private boolean clicked = false;
    public Interval timer=new Interval(10);
    private static final float stealthCheckDuration=12;

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
    public void setType(UnitType type) {
        if (!(type instanceof StealthUnitType)) return;
        super.setType(type);
        type(type);
    }

    public boolean selectStealth() {
        boolean bool;
        if (isLocal()) {
            bool = modVars.keyBinds.keyTap(ModBinding.stealthBing);
            if (Vars.mobile) {
                if (!check2 && longPress) {
                    check2 = true;
                    longPress = false;
                    return true;
                }
                return false;
            }
            return bool;
        }
        bool = mustHeal();
        if (bool && healing) {
            return inStealth;
        }
        return !inStealth && bool;
    }

    @Override
    public boolean healing() {
        return healing;
    }

    @Override
    public void healing(boolean healing) {
        this.healing = healing;
    }

    public boolean mustHeal() {
        boolean bool1 = health <= stealthType.minHealth;
        boolean bool2 = health > stealthType.maxHealth;
        if (!check && bool1) {
            check = true;
            return true;
        } else if (check && bool2) {
            check = false;
            return bool1;
        }
        return !bool2;
    }

    @Override
    public boolean inStealth() {
        return inStealth;
    }

    @Override
    public void inStealth(boolean inStealth) {
        this.inStealth = inStealth;
    }

    @Override
    public void longPress(boolean longPress) {
        this.longPress = longPress;
    }

    @Override
    public boolean longPress() {
        return longPress;
    }

    public void setStealth() {
        setStealth(0);
    }

    public void setStealth(float time) {
        if (!inStealth && cooldownStealth==0) {
            inStealth = true;
            ModCall.checkStealthStatus(Vars.player,this,inStealth);
            durationStealth = 0;
        }
    }

    @Override
    public float stealthf() {
        if (inStealth){
            return 1f-durationStealth/stealthType.stealthDuration;
        }else {
            return 1f-cooldownStealth/stealthType.stealthCooldown;
        }
    }

    public void removeStealth() {
        removeStealth((durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
    }

    public void removeStealth(float time) {
        if (inStealth) {
            inStealth = false;
            ModCall.checkStealthStatus(Vars.player,this,inStealth);
            cooldownStealth = Math.min(stealthType.stealthCooldown, time);
        }
    }
    public void commandNearby(FormationPattern pattern, Boolf<Unit> include) {

        Formation formation = new Formation(new Vec3(x, y, rotation), pattern);
        formation.slotAssignmentStrategy = new DistanceAssignmentStrategy(pattern);
        units.clear();
        ModUnits.nearby(team, x, y, 150.0F, (u)->{
            if (u.isAI() && include.get(u) && u != this && u.type.flying == type.flying && u.hitSize <= hitSize * 1.1F) {
                units.add(u);
            }
        });
        if (units.isEmpty()) return;
        units.sort(Structs.comps(Structs.comparingFloat((u)->-u.hitSize), Structs.comparingFloat((u)->u.dst2(this))));
        units.truncate(type.commandLimit);
        command(formation, units);
    }
    @Override
    public void updateStealthStatus() {
        if (inStealth) {
            if(timer.get(0,stealthCheckDuration))ModCall.checkStealthStatus(Vars.player,this,true);
            if (durationStealth >= stealthType.stealthDuration || selectStealth()) {
//                removeStealth((durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
                ModCall.setStealthStatus(this,false,(durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
                Seq<Unit> stealthUnitc = controlling().select((u) -> u instanceof StealthUnitc);
                if (stealthUnitc.size > 0) {
                    stealthUnitc.each(unit -> {
                        ModCall.setStealthStatus(unit,false,-1);
                    });
                }
            }
        } else if (cooldownStealth == 0f && selectStealth()) {
            ModCall.setStealthStatus(this,true,-1);
            Seq<Unit> stealthUnitc = controlling().select((u) -> u instanceof StealthUnitc);
            if (stealthUnitc.size > 0) {
                stealthUnitc.each(unit -> {
                    ModCall.setStealthStatus(unit,true,-1);
                });
            }
        }
    }

    @Override
    public void update() {
        if (!Groups.unit.contains(u -> u == this)) {
            updateLastPosition();
            Groups.unit.tree().insert(this);
        }
        super.update();
        cooldownStealth = Math.max(0, cooldownStealth - Time.delta);
        if (inStealth) {
            durationStealth = Math.min(stealthType.stealthDuration, durationStealth + Time.delta);
        }
        updateStealthStatus();

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
        Draw.alpha(getAlpha() * Draw.getColor().a);
    }

    public float getAlpha() {

        try {
            return !inStealth ? Draw.getColor().a : Vars.player.team() == team() ? 0.25f : 0f;
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public void draw() {
        if (stealthType==null){
            if (type instanceof StealthUnitType){
                stealthType=(StealthUnitType)type;
            } else {
                Log.info("@ my type @",toString(),type);
            }
        }
//        if (getAlpha()==0)return;
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
      if (stealthType!=null)  stealthType.alpha = getAlpha();
        this.type.draw(this);

        for (StatusEntry e : this.statuses) {
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

    @Override
    public void write(Writes write) {
        super.write(write);
        write.bool(inStealth);
        write.f(cooldownStealth);
        write.f(durationStealth);
    }

    @Override
    public void writeSync(Writes write) {
//        write.s(modClassId());
        super.writeSync(write);
        write.bool(inStealth);
        write.f(cooldownStealth);
        write.f(durationStealth);
    }

    @Override
    public void type(UnitType type) {
        if (type instanceof StealthUnitType) {
            this.type = type;
            stealthType = (StealthUnitType) type;
        }
    }
    @Override
    public void read(Reads read) {
        super.read(read);
        inStealth = read.bool();
        cooldownStealth = read.f();
        durationStealth = read.f();
    }

    @Override
    public void readSync(Reads read) {
        super.readSync(read);
        inStealth = read.bool();
        cooldownStealth = read.f();
        durationStealth = read.f();
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

    @Override
    public int modClassId() {
        return ModEntityMapping.getId(this.getClass());
    }

    public int classId() {
        return modVars.MOD_CONTENT_ID;
    }


}
