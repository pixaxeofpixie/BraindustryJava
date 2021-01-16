package braindustry.graphics;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Time;

import static braindustry.ModVars.modVars.modInfo;

public class ModShaders {
    public static RainbowShader rainbow;
    public static MenuRenderShader menuRender;
    public static LogoRenderShader logoRender;
    public static HoloShader holo;
    public static FractalPyramidShader fractalPyramid;
    public static Shader defaultShader;

    public static void init() {
        rainbow = new RainbowShader();
        menuRender = new MenuRenderShader();
        logoRender = new LogoRenderShader();
        fractalPyramid = new FractalPyramidShader();
        holo =new HoloShader();
//        defaultShader=new Shaders.LoadShader("default","default");
    }

    public static class HoloShader extends ModLoadShader {
        public int offsetId = 0;
        public TextureRegion logo;

        public HoloShader() {
            super("holo", "holo");
            logo = Core.atlas.find("");
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / 100f;

            this.setUniformf("u_texsize", new Vec2(Scl.scl(logo.texture.width), Scl.scl(logo.texture.height)));
            this.setUniformf("u_size", new Vec2(Scl.scl(logo.width), Scl.scl(logo.height)));
            this.setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100, 100));
            this.setUniformf("u_timeMul", 10f * u_time);
            this.setUniformf("u_mul1", 100f);
            this.setUniformf("u_scl", Scl.scl(4));
            this.setUniformf("colorFrom", Color.purple);
            this.setUniformf("colorTo", Color.yellow);
            this.setUniformf("iResolution", new Vec2(Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
    }
    public static class LogoRenderShader extends ModLoadShader {
        public int offsetId = 0;
        public TextureRegion logo;

        public LogoRenderShader() {
            super("logoRender", "logoRender");
            logo = Core.atlas.find("");
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / 100f;

            this.setUniformf("u_texsize", new Vec2(Scl.scl(logo.texture.width), Scl.scl(logo.texture.height)));
            this.setUniformf("u_size", new Vec2(Scl.scl(logo.width), Scl.scl(logo.height)));
            this.setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100, 100));
            this.setUniformf("u_timeMul", 10f * u_time);
            this.setUniformf("u_mul1", 0.1f);
            this.setUniformf("u_scl", Scl.scl(4));
            this.setUniformf("colorFrom", Color.purple);
            this.setUniformf("colorTo", Color.yellow);
            this.setUniformf("iResolution", new Vec2(Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
    }

    public static class MenuRenderShader extends ModLoadShader {
        public int offsetId = 0;

        public MenuRenderShader() {
            super(("menuRender"), "default");
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(1f);

            this.setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100, 100));
            this.setUniformf("iResolution", new Vec2(Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
    }

    public static class FractalPyramidShader extends ModLoadShader {
        public int offsetId = 0;

        public FractalPyramidShader() {
            super(("fractalPyramid"), "default");
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(1f);

            this.setUniformf("iTime", u_time + Mathf.randomSeed(offsetId, -100, 100));
            this.setUniformf("iResolution", new Vec2(Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
    }

    public static class RainbowShader extends ModLoadShader {
        public int offsetId = 0;

        public RainbowShader() {
            super(("rainbow"), "default");
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / 100f;

            this.setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            this.setUniformf("iResolution", new Vec2(Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
    }

    public static class ModLoadShader extends Shader {
        public ModLoadShader(String fragment, String vertex) {
            super(loadFile(vertex + ".vert"), loadFile(fragment + ".frag"));
        }

        private static Fi loadFile(String fileName) {
            Seq<Fi> modShaders = Seq.with(modInfo.root.child("shaders").list());
            if (modShaders.find(fi -> fi.name().equals(fileName)) == null) {
                return Core.files.internal("shaders/" + (fileName));
            }
            return modInfo.root.child("shaders").child(fileName);
        }

    }
}
