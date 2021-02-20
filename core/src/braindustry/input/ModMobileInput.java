package braindustry.input;

import arc.Core;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.entities.ModUnits;
import braindustry.gen.ModCall;
import braindustry.gen.StealthUnitc;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Predict;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.input.MobileInput;
import mindustry.input.PlaceMode;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.ControlBlock;

import static mindustry.Vars.*;
import static mindustry.Vars.player;

public class ModMobileInput extends MobileInput {

    protected void updateMovement(Unit unit){
        Rect rect = Tmp.r3;

        UnitType type = unit.type;
        if(type == null) return;

        boolean omni = unit.type.omniMovement;
        boolean allowHealing = type.canHeal;
        boolean validHealTarget = allowHealing && target instanceof Building && ((Building)target).isValid() && target.team() == unit.team &&
                ((Building)target).damaged() && target.within(unit, type.range);
        boolean boosted = (unit instanceof Mechc && unit.isFlying());

        //reset target if:
        // - in the editor, or...
        // - it's both an invalid standard target and an invalid heal target
        if((Units.invalidateTarget(target, unit, type.range) && !validHealTarget) || state.isEditor()){
            target = null;
        }

        targetPos.set(Core.camera.position);
        float attractDst = 15f;

        float speed = unit.realSpeed();
        float range = unit.hasWeapons() ? unit.range() : 0f;
        float bulletSpeed = unit.hasWeapons() ? type.weapons.first().bullet.speed : 0f;
        float mouseAngle = unit.angleTo(unit.aimX(), unit.aimY());
        boolean aimCursor = omni && player.shooting && type.hasWeapons() && type.faceTarget && !boosted && type.rotateShooting;

        if(aimCursor){
            unit.lookAt(mouseAngle);
        }else{
            unit.lookAt(unit.prefRotation());
        }
        Payloadc pay;
        if(payloadTarget != null && unit instanceof Payloadc){
            pay=unit.as();
            targetPos.set(payloadTarget);
            attractDst = 0f;

            if(unit.within(payloadTarget, 3f * Time.delta)){
                if(payloadTarget instanceof Vec2 && pay.hasPayload()){
                    //vec -> dropping something
                    tryDropPayload();
                }else if(payloadTarget instanceof Building && pay.canPickup((Building) payloadTarget)){
                    //building -> picking building up
                    Call.requestBuildPayload(player, (Building) payloadTarget);
                }else if(payloadTarget instanceof Unit && !(payloadTarget instanceof StealthUnitc) && pay.canPickup((Unit) payloadTarget)){
                    //unit -> picking unit up
                    ModCall.requestUnitPayload(player, (Unit) payloadTarget);
                }

                payloadTarget = null;
            }
        }else{
            payloadTarget = null;
        }

        movement.set(targetPos).sub(player).limit(speed);
        movement.setAngle(Mathf.slerp(movement.angle(), unit.vel.angle(), 0.05f));

        if(player.within(targetPos, attractDst)){
            movement.setZero();
            unit.vel.approachDelta(Vec2.ZERO, unit.speed() * type.accel / 2f);
        }

        float expansion = 3f;

        unit.hitbox(rect);
        rect.x -= expansion;
        rect.y -= expansion;
        rect.width += expansion * 2f;
        rect.height += expansion * 2f;

        player.boosting = collisions.overlapsTile(rect) || !unit.within(targetPos, 85f);

        if(omni){
            unit.moveAt(movement);
        }else{
            unit.moveAt(Tmp.v2.trns(unit.rotation, movement.len()));
            if(!movement.isZero()){
                unit.vel.rotateTo(movement.angle(), unit.type.rotateSpeed * Math.max(Time.delta, 1));
            }
        }

        //update shooting if not building + not mining
        if(!player.unit().activelyBuilding() && player.unit().mineTile == null){

            //autofire targeting
            if(manualShooting){
                player.shooting = !boosted;
                unit.aim(player.mouseX = Core.input.mouseWorldX(), player.mouseY = Core.input.mouseWorldY());
            }else if(target == null){
                player.shooting = false;
                BlockUnitUnit u;
                if(Core.settings.getBool("autotarget") && !(player.unit() instanceof BlockUnitUnit &&((u=player.as())!=null) && u.tile() instanceof ControlBlock  && !((ControlBlock) u.tile()).shouldAutoTarget())){
                    target = Units.closestTarget(unit.team, unit.x, unit.y, range, u1 -> u1.team != Team.derelict, u1 -> u1.team != Team.derelict);

                    if(allowHealing && target == null){
                        target = Geometry.findClosest(unit.x, unit.y, indexer.getDamaged(Team.sharded));
                        if(target != null && !unit.within(target, range)){
                            target = null;
                        }
                    }
                }

                //when not shooting, aim at mouse cursor
                //this may be a bad idea, aiming for a point far in front could work better, test it out
                unit.aim(Core.input.mouseWorldX(), Core.input.mouseWorldY());
            }else{
                Vec2 intercept = Predict.intercept(unit, target, bulletSpeed);

                player.mouseX = intercept.x;
                player.mouseY = intercept.y;
                player.shooting = !boosted;

                unit.aim(player.mouseX, player.mouseY);
            }
        }

        unit.controlWeapons(player.shooting && !boosted);
    }
    protected int tileX(float cursorX) {
        Vec2 vec = Core.input.mouseWorld(cursorX, 0.0F);
        if (this.selectedBlock()) {
            vec.sub(this.block.offset, this.block.offset);
        }

        return World.toTile(vec.x);
    }

    protected int tileY(float cursorY) {
        Vec2 vec = Core.input.mouseWorld(0.0F, cursorY);
        if (this.selectedBlock()) {
            vec.sub(this.block.offset, this.block.offset);
        }

        return World.toTile(vec.y);
    }

    protected Tile tileAt(float x, float y) {
        return Vars.world.tile(this.tileX(x), this.tileY(y));
    }

    @Override
    public boolean longPress(float x, float y) {
        if (!Vars.state.isMenu() && !Vars.player.dead()) {
            Tile cursor = this.tileAt(x, y);
            if (!Core.scene.hasMouse(x, y) && !this.schematicMode) {
                if (this.mode == PlaceMode.none) {
                    Vec2 pos = Core.input.mouseWorld(x, y);
                    Unit target = Vars.player.unit();
                    Payloadc pay;
                    if (target instanceof StealthUnitc) {
                        ((StealthUnitc)target).longPress(true);
                    } else if (target instanceof Payloadc) {
                        pay = (Payloadc) target;
                        target = Units.closest(Vars.player.team(), pos.x, pos.y, 8.0F, (u) -> {
                            return u.isAI() && u.isGrounded() && pay.canPickup(u) && u.within(pos, u.hitSize + 8.0F);
                        });
                        if (target != null) {
                            this.payloadTarget = target;
                        } else {
                            Building build = Vars.world.buildWorld(pos.x, pos.y);
                            if (build != null && build.team == Vars.player.team() && pay.canPickup(build)) {
                                this.payloadTarget = build;
                            } else if (pay.hasPayload()) {
                                this.payloadTarget = new Vec2(pos);
                            } else {
                                this.manualShooting = true;
                                this.target = null;
                            }
                        }
                    } else {
                        this.manualShooting = true;
                        this.target = null;
                    }

                    if (!Vars.state.isPaused()) {
                        Fx.select.at(pos);
                    }
                } else {
                    if (cursor == null) {
                        return false;
                    }

                    this.lineStartX = cursor.x;
                    this.lineStartY = cursor.y;
                    this.lastLineX = cursor.x;
                    this.lastLineY = cursor.y;
                    this.lineMode = true;
                    if (this.mode == PlaceMode.breaking) {
                        if (!Vars.state.isPaused()) {
                            Fx.tapBlock.at(cursor.worldx(), cursor.worldy(), 1.0F);
                        }
                    } else if (this.block != null) {
                        this.updateLine(this.lineStartX, this.lineStartY, cursor.x, cursor.y);
                        if (!Vars.state.isPaused()) {
                            Fx.tapBlock.at(cursor.worldx() + this.block.offset, cursor.worldy() + this.block.offset, (float) this.block.size);
                        }
                    }
                }

                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public Unit selectedUnit() {
        Unit unit = ModUnits.closest(Vars.player.team(), Core.input.mouseWorld().x, Core.input.mouseWorld().y, 40.0F, Unitc::isAI);
        if (unit != null) {
            unit.hitbox(Tmp.r1);
            Tmp.r1.grow(6.0F);
            if (Tmp.r1.contains(Core.input.mouseWorld())) {
                return unit;
            }
        }

        Building build = Vars.world.buildWorld(Core.input.mouseWorld().x, Core.input.mouseWorld().y);
        ControlBlock cont;
        return build instanceof ControlBlock && (cont = (ControlBlock)build) == build && cont.canControl() && build.team == Vars.player.team() ? cont.unit() : null;
    }
}
