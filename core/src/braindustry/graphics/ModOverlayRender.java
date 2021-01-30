package braindustry.graphics;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.ai.types.LogicAI;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.OverlayRenderer;
import mindustry.graphics.Pal;
import mindustry.input.Binding;
import mindustry.input.InputHandler;
import mindustry.ui.Cicon;
import mindustry.ui.fragments.PlacementFragment;
import mindustry.world.Tile;

import static mindustry.Vars.*;
import static mindustry.Vars.tilesize;

public class ModOverlayRender extends OverlayRenderer {
    private static final float indicatorLength = 14.0F;
    private static final float spawnerMargin = 88.0F;
    private static final Rect rect = new Rect();
    private float buildFade;
    private float unitFade;
    private Unit lastSelect;

    public ModOverlayRender() {
        super();
    }

    @Override
    public void drawBottom() {
        super.drawBottom();
    }

    @Override
    public void drawTop() {

        if(!player.dead() && ui.hudfrag.shown){
            if(Core.settings.getBool("playerindicators")){
                for(Player player : Groups.player){
                    if(Vars.player != player && Vars.player.team() == player.team()){
                        if(!rect.setSize(Core.camera.width * 0.9f, Core.camera.height * 0.9f)
                                .setCenter(Core.camera.position.x, Core.camera.position.y).contains(player.x, player.y)){

                            Tmp.v1.set(player).sub(Vars.player).setLength(indicatorLength);

                            Lines.stroke(2f, Vars.player.team().color);
                            Lines.lineAngle(Vars.player.x + Tmp.v1.x, Vars.player.y + Tmp.v1.y, Tmp.v1.angle(), 4f);
                            Draw.reset();
                        }
                    }
                }
            }

            if(Core.settings.getBool("indicators")){
                Groups.unit.each(unit -> {
                    if(!unit.isLocal() && unit.team != player.team() && !rect.setSize(Core.camera.width * 0.9f, Core.camera.height * 0.9f)
                            .setCenter(Core.camera.position.x, Core.camera.position.y).contains(unit.x, unit.y)){
                        Tmp.v1.set(unit.x, unit.y).sub(player).setLength(indicatorLength);

                        Lines.stroke(1f, unit.team().color);
                        Lines.lineAngle(player.x + Tmp.v1.x, player.y + Tmp.v1.y, Tmp.v1.angle(), 3f);
                        Draw.reset();
                    }
                });
            }
        }

        if(player.dead()) return; //dead players don't draw

        InputHandler input = control.input;

        Unit select = input.selectedUnit();
        if(!Core.input.keyDown(Binding.control)) select = null;
        unitFade = Mathf.lerpDelta(unitFade, Mathf.num(select != null), 0.1f);

        if(select != null) lastSelect = select;
        if(select == null) select = lastSelect;
        if(select != null && select.isAI()){
            Draw.mixcol(Pal.accent, 1f);
            Draw.alpha(unitFade);

            if(select instanceof BlockUnitc){
                //special selection for block "units"
                Fill.square(select.x, select.y, ((BlockUnitc)select).tile().block.size * tilesize/2f);
            }else{
                Draw.rect(select.type.icon(Cicon.full), select.x(), select.y(), select.rotation() - 90);
            }

            for(int i = 0; i < 4; i++){
                float rot = i * 90f + 45f + (-Time.time) % 360f;
                float length = select.hitSize() * 1.5f + (unitFade * 2.5f);
                Draw.rect("select-arrow", select.x + Angles.trnsx(rot, length), select.y + Angles.trnsy(rot, length), length / 1.9f, length / 1.9f, rot - 135f);
            }

            Draw.reset();
        }

        //draw config selected block
        if(input.frag.config.isShown()){
            Building tile = input.frag.config.getSelectedTile();
            tile.drawConfigure();
        }

        input.drawTop();

        buildFade = Mathf.lerpDelta(buildFade, input.isPlacing() || input.isUsingSchematic() ? 1f : 0f, 0.06f);

        Draw.reset();
        Lines.stroke(buildFade * 2f);

        if(buildFade > 0.005f){
            state.teams.eachEnemyCore(player.team(), core -> {
                float dst = core.dst(player);
                if(dst < state.rules.enemyCoreBuildRadius * 2.2f){
                    Draw.color(Color.darkGray);
                    Lines.circle(core.x, core.y - 2, state.rules.enemyCoreBuildRadius);
                    Draw.color(Pal.accent, core.team.color, 0.5f + Mathf.absin(Time.time, 10f, 0.5f));
                    Lines.circle(core.x, core.y, state.rules.enemyCoreBuildRadius);
                }
            });
        }

        Lines.stroke(2f);
        Draw.color(Color.gray, Color.lightGray, Mathf.absin(Time.time, 8f, 1f));

        if(state.hasSpawns()){
            for(Tile tile : spawner.getSpawns()){
                if(tile.within(player.x, player.y, state.rules.dropZoneRadius + spawnerMargin)){
                    Draw.alpha(Mathf.clamp(1f - (player.dst(tile) - state.rules.dropZoneRadius) / spawnerMargin));
                    Lines.dashCircle(tile.worldx(), tile.worldy(), state.rules.dropZoneRadius);
                }
            }
        }

        Draw.reset();

        //draw selected block
        if(input.block == null && !Core.scene.hasMouse()){
            Vec2 vec = Core.input.mouseWorld(input.getMouseX(), input.getMouseY());
            Building build = world.buildWorld(vec.x, vec.y);

            if(build != null && build.team == player.team()){
                build.drawSelect();
                if(!build.enabled && build.block.drawDisabled){
                    build.drawDisabled();
                }

                if(Core.input.keyDown(Binding.rotateplaced) && build.block.rotate && build.block.quickRotate && build.interactable(player.team())){
                    control.input.drawArrow(build.block, build.tileX(), build.tileY(), build.rotation, true);
                    Draw.color(Pal.accent, 0.3f + Mathf.absin(4f, 0.2f));
                    Fill.square(build.x, build.y, build.block.size * tilesize/2f);
                    Draw.color();
                }
            }
        }

        input.drawOverSelect();
        // TODO Put MOD version
        PlacementFragment blockfrag = ui.hudfrag.blockfrag;
        if(blockfrag.hover() instanceof Unit ) {
            Unit unit = (Unit) blockfrag.hover();
            if(unit.controller() instanceof LogicAI) {
                LogicAI ai = (LogicAI) unit.controller();
                if (ai.controller != null) {
                    Building build=ai.controller;
                    if (build.isValid())
                    Drawf.square(build.x, build.y, build.block.size * tilesize / 2f + 2f);
                    if (!unit.within(build, unit.hitSize * 2f)) {
                        Drawf.arrow(unit.x, unit.y, build.x, build.y, unit.hitSize * 2f, 4f);
                    }
                }
            }
        }

        //draw selection overlay when dropping item
        if(input.isDroppingItem()){
            Vec2 v = Core.input.mouseWorld(input.getMouseX(), input.getMouseY());
            float size = 8;
            Draw.rect(player.unit().item().icon(Cicon.medium), v.x, v.y, size, size);
            Draw.color(Pal.accent);
            Lines.circle(v.x, v.y, 6 + Mathf.absin(Time.time, 5f, 1f));
            Draw.reset();

            Building tile = world.buildWorld(v.x, v.y);
            if(input.canDropItem() && tile != null && tile.interactable(player.team()) && tile.acceptStack(player.unit().item(), player.unit().stack.amount, player.unit()) > 0 && player.within(tile, itemTransferRange)){
                Lines.stroke(3f, Pal.gray);
                Lines.square(tile.x, tile.y, tile.block.size * tilesize / 2f + 3 + Mathf.absin(Time.time, 5f, 1f));
                Lines.stroke(1f, Pal.place);
                Lines.square(tile.x, tile.y, tile.block.size * tilesize / 2f + 2 + Mathf.absin(Time.time, 5f, 1f));
                Draw.reset();

            }
        }
    }
}
