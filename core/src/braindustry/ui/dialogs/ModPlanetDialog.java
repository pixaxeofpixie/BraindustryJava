package braindustry.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.event.Touchable;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.graphics.g3d.ModPlanetRenderer;
import mindustry.content.TechTree;
import mindustry.game.Team;
import mindustry.gen.Iconc;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.PlanetRenderer;
import mindustry.type.Planet;
import mindustry.type.Sector;
import mindustry.ui.Fonts;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.mobile;
import static mindustry.Vars.ui;

public class ModPlanetDialog extends PlanetDialog {
    private Runnable update;

    boolean keepWithinStage=true;
    void keepWithinStage() {
        if (this.keepWithinStage) {
            this.keepInStage();
        }
    }

    @Override
    public void setKeepWithinStage(boolean keepWithinStage) {
        super.setKeepWithinStage(keepWithinStage);
        this.keepWithinStage=keepWithinStage;
    }

    public void superAct(float delta){
       element: {
            Seq<Action> actions = getActions();
            if (actions.size > 0) {
                if (getScene() != null && getScene().getActionsRequestRendering()) {
                    Core.graphics.requestRendering();
                }

                for(int i = 0; i < actions.size; ++i) {
                    Action action = (Action)actions.get(i);
                    if (action.act(delta) && i < actions.size) {
                        Action current = (Action)actions.get(i);
                        int actionIndex = current == action ? i : actions.indexOf(action, true);
                        if (actionIndex != -1) {
                            actions.remove(actionIndex);
                            action.setActor((Element)null);
                            --i;
                        }
                    }
                }
            }

            if (this.touchablility != null) {
                this.touchable = (Touchable)this.touchablility.get();
            }

            if (update != null) {
                update.run();
            }

        }
        group:{
            Element[] actors = this.children.begin();
            int i = 0;

            for(int n = this.children.size; i < n; ++i) {
                if (actors[i].visible) {
                    actors[i].act(delta);
                }

                actors[i].updateVisibility();
            }

            this.children.end();
        }
        dialog:{
            if (this.getScene() != null) {
                this.keepWithinStage();
                if (this.isCentered() && !this.isMovable() && this.getActions().size == 0) {
                    this.centerWindow();
                }
            }

        }
    }
    @Override
    public Element update(Runnable r) {
        update = r;
        return this;
    }
    @Override
    public void act(float delta){
        superAct(delta);
        float outlineRad;
        if(hovered != null && !mobile){
            addChild(hoverLabel);
            hoverLabel.toFront();
            hoverLabel.touchable = Touchable.disabled;
            outlineRad=ModPlanetRenderer.radiusProvider(hovered);
            Vec3 pos = planets.cam.project(Tmp.v31.set(hovered.tile.v).setLength(outlineRad).rotate(Vec3.Y, -planets.planet.getRotation()).add(planets.planet.position));
            hoverLabel.setPosition(pos.x - Core.scene.marginLeft, pos.y - Core.scene.marginBottom, Align.center);

            hoverLabel.getText().setLength(0);
            if(hovered != null){
                StringBuilder tx = hoverLabel.getText();
                if(!canSelect(hovered)){
                    tx.append("[gray]").append(Iconc.lock).append(" ").append(Core.bundle.get("locked"));
                }else{
                    tx.append("[accent][[ [white]").append(hovered.name()).append("[accent] ]");
                }
            }
            hoverLabel.invalidateHierarchy();
        }else{
            hoverLabel.remove();
        }

        if(launching && selected != null){
            lookAt(selected, 0.1f);
        }

        if(showing()){
            Sector to = newPresets.peek();

            presetShow += Time.delta;

            lookAt(to, 0.11f);
            zoom = 0.75f;

            if(presetShow >= 20f && !showed && newPresets.size > 1){
                showed = true;
                ui.announce(Iconc.lockOpen + " [accent]" + to.name(), 2f);
            }

            if(presetShow > sectorShowDuration){
                newPresets.pop();
                showed = false;
                presetShow = 0f;
            }
        }

        if(planets.planet.isLandable()){
            hovered = planets.planet.getSector(planets.cam.getMouseRay(), ModPlanetRenderer.radiusProvider(planets.planet));
        }else{
            hovered = selected = null;
        }

        planets.zoom = Mathf.lerpDelta(planets.zoom, zoom, 0.4f);
        selectAlpha = Mathf.lerpDelta(selectAlpha, Mathf.num(planets.zoom < 0.73f + planets.planet.radius), 0.1f);
    }
    boolean canSelect(Sector sector) {
        if (this.mode == PlanetDialog.Mode.select) {
            return sector.hasBase();
        } else if (sector.hasBase()) {
            return true;
        } else if (sector.preset != null) {
            TechTree.TechNode node = sector.preset.node();
            return node == null || node.parent == null || node.parent.content.unlocked();
        } else {
            return sector.hasBase() || sector.near().contains(Sector::hasBase);
        }
    }

    @Override
    public void renderProjections(Planet planet){
        float iw = 48f/4f;

        for(Sector sec : planet.sectors){
            if(sec != hovered){
                TextureRegion preficon = sec.icon();
                TextureRegion icon =
                        sec.isAttacked() ? Fonts.getLargeIcon("warning") :
                                !sec.hasBase() && sec.preset != null && sec.preset.unlocked() && preficon == null ?
                                        Fonts.getLargeIcon("terrain") :
                                        sec.preset != null && sec.preset.locked() && sec.preset.node() != null && !sec.preset.node().parent.content.locked() ? Fonts.getLargeIcon("lock") :
                                                preficon;
                Color color = sec.preset != null && !sec.hasBase() ? Team.derelict.color : Team.sharded.color;

                if(icon != null){
                    planets.drawPlane(sec, () -> {
                        Draw.color(color, selectAlpha);
                        Draw.rect(icon, 0, 0, iw, iw * icon.height / icon.width);
                    });
                }
            }
        }

        Draw.reset();

        if(hovered != null){
            planets.drawPlane(hovered, () -> {
                Draw.color(hovered.isAttacked() ? Pal.remove : Color.white, Pal.accent, Mathf.absin(5f, 1f));

                TextureRegion icon = hovered.locked() && !canSelect(hovered) ? Fonts.getLargeIcon("lock") : hovered.isAttacked() ? Fonts.getLargeIcon("warning") : hovered.icon();

                if(icon != null){
//                    Draw.rect(icon, 0, 0, iw, iw * icon.height / icon.width);
                }

                Draw.reset();
            });
        }

        Draw.reset();
    }
    boolean showing() {
        return this.newPresets.any();
    }
}
