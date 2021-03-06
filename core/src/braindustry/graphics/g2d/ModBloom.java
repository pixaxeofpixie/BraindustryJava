package braindustry.graphics.g2d;

import ModVars.modVars;
import arc.graphics.Color;
import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import mindustry.Vars;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Layer;
import mindustry.graphics.Shaders;

import static ModVars.modVars.floorRenderer;
import static arc.Core.camera;
import static arc.Core.graphics;
import static mindustry.Vars.enableDarkness;
import static mindustry.Vars.state;

public class ModBloom extends Bloom {
    Bloom parent;

    public ModBloom parent(Bloom parent) {
        this.parent = parent;
        return this;
    }

    private Color clearColor=new Color(0f, 0f, 0f, 1f)  ;

    public ModBloom(Bloom parent) {
//        parent(true);
        this.parent=parent;
    }

    @Override
    public void resize(int width, int height) {
        if (modVars.renderUpdate){
//            Draw.draw(Layer.background, Vars.renderer::drawBackground);
            graphics.clear(clearColor);
            Draw.reset();
            Draw.proj(camera);

            floorRenderer.checkChanges();
            Draw.draw(Layer.floor, floorRenderer::drawFloor);
            Draw.draw(Layer.block - 1, Vars.renderer.blocks::drawShadows);
            Draw.draw(Layer.block - 0.09f, () -> {
                Vars.renderer.blocks.floor.beginDraw();
                Vars.renderer.blocks.floor.drawLayer(CacheLayer.walls);
                Vars.renderer.blocks.floor.endDraw();
            });

            Draw.drawRange(Layer.blockBuilding, () -> Draw.shader(Shaders.blockbuild, true), Draw::shader);

            if(state.rules.lighting){
                Draw.draw(Layer.light, Vars.renderer.lights::draw);
            }

            if(enableDarkness){
                Draw.draw(Layer.darkness, Vars.renderer.blocks::drawDarkness);
            }
        }
        if (notnull())parent.resize(width, height);
    }

    @Override
    public void resume() {
        if (notnull())parent.resume();
    }

    @Override
    public void setClearColor(float r, float g, float b, float a) {
        if (notnull())parent.setClearColor(r, g, b, a);
    }

    @Override
    public void capture() {
        if (notnull())parent.capture();
    }

    @Override
    public void capturePause() {
        if (notnull())parent.capturePause();
    }

    @Override
    public void captureContinue() {
        if (notnull())parent.captureContinue();
    }

    @Override
    public void render() {
        if (notnull())parent.render();
    }

    @Override
    public void setBloomIntesity(float intensity) {
        if (notnull())parent.setBloomIntesity(intensity);
    }

    @Override
    public void setOriginalIntesity(float intensity) {
        if (notnull())parent.setOriginalIntesity(intensity);
    }

    @Override
    public void setThreshold(float threshold) {
        if (notnull())parent.setThreshold(threshold);
    }

    @Override
    public void dispose() {
        if (notnull())parent.dispose();
    }

    protected boolean notnull() {
        return parent != null;
    }
}
