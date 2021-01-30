package braindustry.ui.dialogs;

import arc.Core;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.event.Touchable;
import arc.util.Align;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.graphics.g3d.ModPlanetRenderer;
import mindustry.content.TechTree;
import mindustry.gen.Iconc;
import mindustry.graphics.g3d.PlanetRenderer;
import mindustry.type.Sector;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.mobile;
import static mindustry.Vars.ui;

public class ModPlanetDialog extends PlanetDialog {

    @Override
    public void act(float delta){
        super.act(delta);
        float outlineRad;
        if(hovered != null && !mobile){
            addChild(hoverLabel);
            hoverLabel.toFront();
            hoverLabel.touchable = Touchable.disabled;
            outlineRad=ModPlanetRenderer.radiusProvider.get(hovered);
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
            outlineRad=ModPlanetRenderer.radiusProvider.get(planets.planet);
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
            hovered = planets.planet.getSector(planets.cam.getMouseRay(), outlineRad);
        }else{
            hovered = selected = null;
        }

        planets.zoom = Mathf.lerpDelta(planets.zoom, zoom, 0.4f);
        selectAlpha = Mathf.lerpDelta(selectAlpha, Mathf.num(planets.zoom < 1.9f), 0.1f);
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
    boolean showing() {
        return this.newPresets.any();
    }
}
