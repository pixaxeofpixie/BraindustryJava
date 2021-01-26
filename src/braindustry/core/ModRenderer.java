package braindustry.core;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.Renderer;
import mindustry.game.EventType;
import mindustry.gen.Drawc;
import mindustry.gen.Groups;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.PlanetRenderer;
import mindustry.ui.Cicon;
import mindustry.world.blocks.storage.CoreBlock;

import static arc.Core.*;
import static mindustry.Vars.*;

public class ModRenderer extends Renderer {


    private Color clearColor = new Color(0.0F, 0.0F, 0.0F, 1.0F);
    private @Nullable
    CoreBlock.CoreBuild landCore;
    private float landscale = 0f, landTime, weatherAlpha;
    private float targetscale = Scl.scl(4.0F);
    private float camerascale = Scl.scl(4.0F);
    private float minZoomScl = Scl.scl(0.01f);
    private float shakeIntensity, shaketime;

    @Override
    public void shake(float intensity, float duration) {
        shakeIntensity = Math.max(shakeIntensity, intensity);
        shaketime = Math.max(shaketime, duration);
    }

    @Override
    public void update() {
        targetscale = getScale();
        camerascale = getDisplayScale();
        Color.white.set(1f, 1f, 1f, 1f);
        Gl.clear(Gl.stencilBufferBit);

        float dest = Mathf.round(targetscale, 0.5f);
        camerascale = Mathf.lerpDelta(camerascale, dest, 0.1f);
        if (Mathf.equal(camerascale, dest, 0.001f)) camerascale = dest;
        laserOpacity = settings.getInt("lasersopacity") / 100f;
        bridgeOpacity = settings.getInt("bridgeopacity") / 100f;
        animateShields = settings.getBool("animatedshields");
        drawStatus = Core.settings.getBool("blockstatus");

        if (landTime > 0) {
            landTime -= Time.delta;
            landscale = Interp.pow5In.apply(minZoomScl, Scl.scl(4f), 1f - landTime / Fx.coreLand.lifetime);
            camerascale = landscale;
            weatherAlpha = 0f;
        } else {
            weatherAlpha = Mathf.lerpDelta(weatherAlpha, 1f, 0.08f);
        }

        camera.width = graphics.getWidth() / camerascale;
        camera.height = graphics.getHeight() / camerascale;

        if (state.isMenu()) {
            landTime = 0f;
            graphics.clear(Color.black);
        } else {
            updateShake(0.75f);
            if (pixelator.enabled()) {
                pixelator.drawPixelate();
            } else {
                draw();
            }
        }
    }

    void updateShake(float scale) {
        if (this.shaketime > 0.0F) {
            float intensity = this.shakeIntensity * ((float) Core.settings.getInt("screenshake", 4) / 4.0F) * scale;
            Core.camera.position.add(Mathf.range(intensity), Mathf.range(intensity));
            this.shakeIntensity -= 0.25F * Time.delta;
            this.shaketime -= Time.delta;
            this.shakeIntensity = Mathf.clamp(this.shakeIntensity, 0.0F, 100.0F);
        } else {
            this.shakeIntensity = 0.0F;
        }

    }

    @Override
    public void init() {
        planets = new PlanetRenderer();

        if (settings.getBool("bloom", !ios)) {
            setupBloom();
        }

        Events.on(EventType.WorldLoadEvent.class, e -> {
            landCore = player.bestCore();
        });
    }

    void setupBloom() {
        try {
            if (this.bloom != null) {
                this.bloom.dispose();
                this.bloom = null;
            }

            this.bloom = new Bloom(true);
        } catch (Throwable var2) {
            Core.settings.put("bloom", false);
            Vars.ui.showErrorMessage("@error.bloom");
            Log.err(var2);
        }

    }

    @Override
    public void draw() {
        Events.fire(EventType.Trigger.preDraw);

        camera.update();

        if (Float.isNaN(camera.position.x) || Float.isNaN(camera.position.y)) {
            camera.position.set(player);
        }

        graphics.clear(clearColor);
        Draw.reset();

        if (Core.settings.getBool("animatedwater") || animateShields) {
            effectBuffer.resize(graphics.getWidth(), graphics.getHeight());
        }

        Draw.proj(camera);

        blocks.floor.checkChanges();
        blocks.processBlocks();

        Draw.sort(true);

        Events.fire(EventType.Trigger.draw);

        if (pixelator.enabled()) {
            pixelator.register();
        }

        Draw.draw(Layer.background, this::drawBackground);
        Draw.draw(Layer.floor, blocks.floor::drawFloor);
        Draw.draw(Layer.block - 1, blocks::drawShadows);
        Draw.draw(Layer.block, () -> {
            blocks.floor.beginDraw();
            blocks.floor.drawLayer(CacheLayer.walls);
            blocks.floor.endDraw();
        });

        Draw.drawRange(Layer.blockBuilding, () -> Draw.shader(Shaders.blockbuild, true), Draw::shader);

        if (state.rules.lighting) {
            Draw.draw(Layer.light, lights::draw);
        }

        if (enableDarkness) {
            Draw.draw(Layer.darkness, blocks::drawDarkness);
        }

        if (bloom != null) {
            bloom.resize(graphics.getWidth() / 4, graphics.getHeight() / 4);
            Draw.draw(Layer.bullet - 0.01f, bloom::capture);
            Draw.draw(Layer.effect + 0.01f, bloom::render);
        }

        Draw.draw(Layer.plans, overlays::drawBottom);

        if (animateShields && Shaders.shield != null) {
            Draw.drawRange(Layer.shields, 1f, () -> effectBuffer.begin(Color.clear), () -> {
                effectBuffer.end();
                effectBuffer.blit(Shaders.shield);
            });

            Draw.drawRange(Layer.buildBeam, 1f, () -> effectBuffer.begin(Color.clear), () -> {
                effectBuffer.end();
                effectBuffer.blit(Shaders.buildBeam);
            });
        }

//        Draw.draw(Layer.overlayUI, overlays::drawTop);
        Draw.draw(Layer.space, this::drawLanding);

        blocks.drawBlocks();

        Groups.draw.draw(Drawc::draw);

        Draw.reset();
        Draw.flush();
        Draw.sort(false);

        Events.fire(EventType.Trigger.postDraw);
    }

    private void drawBackground() {
    }

    public boolean isLanding() {
        return this.landTime > 0.0F;
    }

    public float landScale() {
        return this.landTime > 0.0F ? this.landscale : 1.0F;
    }

    private void drawLanding() {
        CoreBlock.CoreBuild entity = landCore == null ? player.bestCore() : landCore;
        if (landTime > 0 && entity != null) {
            float fract = landTime / Fx.coreLand.lifetime;

            TextureRegion reg = entity.block.icon(Cicon.full);
            float scl = Scl.scl(4f) / camerascale;
            float s = reg.width * Draw.scl * scl * 4f * fract;

            Draw.color(Pal.lightTrail);
            Draw.rect("circle-shadow", entity.x, entity.y, s, s);

            Angles.randLenVectors(1, (1f - fract), 100, 1000f * scl * (1f - fract), (x, y, fin, fout) -> {
                Lines.stroke(scl * fin);
                Lines.lineAngle(entity.x + x, entity.y + y, Mathf.angle(x, y), (fin * 20 + 1f) * scl);
            });

            Draw.color();
            Draw.mixcol(Color.white, fract);
            Draw.rect(reg, entity.x, entity.y, reg.width * Draw.scl * scl, reg.height * Draw.scl * scl, fract * 135f);

            Draw.reset();
        }
    }
}
