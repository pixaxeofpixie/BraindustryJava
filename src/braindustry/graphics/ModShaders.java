package braindustry.graphics;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Time;
import braindustry.entities.bullets.RainbowLaserBulletType;
import mindustry.Vars;
import mindustry.gen.Bullet;

import static ModVars.modVars.modInfo;

public class ModShaders {
    public static RainbowShader rainbow;
    public static MenuRenderShader menuRender;
    public static LogoRenderShader logoRender;
    public static HoloShader holo;
    public static FractalPyramidShader fractalPyramid;
    public static RainbowLaserShader rainbowLaserShader;
    public static TestShader testShader;

    public static void init() {
        rainbow = new RainbowShader();
        menuRender = new MenuRenderShader();
        logoRender = new LogoRenderShader();
        fractalPyramid = new FractalPyramidShader();
        holo = new HoloShader();
        rainbowLaserShader = new RainbowLaserShader();
        testShader=new TestShader();
//        defaultShader=new Shaders.LoadShader("default","default");
    }
    public static Vec2 getScreenSize(){
        Vec2 screenSize = new Vec2(Core.graphics.getWidth(), Core.graphics.getHeight());
        return screenSize;
    }
    public static class TestShader extends ModLoadShader {
        public int offsetId=0;

        public TestShader() {
            super("test", "default");
        }

        private Vec3 vec2(Vec2 vec2, float val) {
            return new Vec3(vec2.x, val, vec2.y);
        }

        private Vec3 vec2(Vec2 vec2) {
            return vec2(vec2, 0);
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(10);

            setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            Vec2 screenSize = getScreenSize();
            setUniformf("iResolution", screenSize);
//            this.setUniformf("iResolution", new Vec2().trns(bullet.rotation()-45f,Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
    }
    public static class RainbowLaserShader extends ModLoadShader {
        public int offsetId = 0;
        public Bullet bullet;
        public RainbowLaserBulletType type;

        public RainbowLaserShader() {
            super("rainbowLaser", "default");
        }

        public Shader setBullet(Bullet bullet, RainbowLaserBulletType type) {
            offsetId = bullet.id;
            this.bullet = bullet;
            this.type = type;
            return this;
        }

        private Vec3 vec2(Vec2 vec2, float val) {
            return new Vec3(vec2.x, val, vec2.y);
        }

        private Vec3 vec2(Vec2 vec2) {
            return vec2(vec2, 0);
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(10);

            setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            Vec2 screenSize = getScreenSize();
            setUniformf("iResolution", screenSize);
            Vec2 bulletPos = new Vec2(bullet.y, bullet.x);
            Vec2 bulletScreenPos = bulletPos.cpy().sub(Core.camera.position.cpy()
                    .sub(Core.camera.width / 2f, Core.camera.height / 2f))
                    .div(new Vec2(Core.camera.width, Core.camera.height)
                    );
            setUniformf("u_pos", bulletPos);
            /** а
             * Сделать нормальную подачу длинны
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * Да
             * */
            setUniformf("u_length", type.length);
            setUniformf("u_scl", Vars.renderer.getScale());
            setUniformf("u_screenPos", bulletScreenPos);
            setUniformf("u_vecRot", new Vec2(Mathf.cosDeg(bullet.rotation()), Mathf.sinDeg(bullet.rotation())));
            setUniformf("u_offset", new Vec3(-2, 2, -0));

            setUniformf("u_grow", new Vec2(900, 900));
//            this.setUniformf("iResolution", new Vec2().trns(bullet.rotation()-45f,Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }
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
            this.setUniformf("iResolution", getScreenSize());
//            this.setUniformf("offset", );
        }
    }

    public static class LogoRenderShader extends ModLoadShader {
        public int offsetId = 0;
        public TextureRegion logo;
        public float force = 10;

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
            this.setUniformf("u_force", force);
            this.setUniformf("u_scl", Scl.scl(4));
            this.setUniformf("colorFrom", Color.purple);
            this.setUniformf("colorTo", Color.yellow);
            this.setUniformf("iResolution", getScreenSize());
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
            this.setUniformf("iResolution", getScreenSize());
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
            this.setUniformf("iResolution", getScreenSize());
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
            this.setUniformf("iResolution", getScreenSize());
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
