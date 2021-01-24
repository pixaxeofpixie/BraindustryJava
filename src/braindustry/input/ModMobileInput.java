package braindustry.input;

import arc.Core;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import braindustry.entities.ModUnits;
import braindustry.gen.StealthMechUnit;
import braindustry.gen.StealthUnitc;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Units;
import mindustry.gen.Building;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.input.MobileInput;
import mindustry.input.PlaceMode;
import mindustry.world.Tile;
import mindustry.world.blocks.ControlBlock;

public class ModMobileInput extends MobileInput {
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
