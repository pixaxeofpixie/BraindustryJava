package braindustry.graphics;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Position;
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
    private static Vec2 vec2(float x, float y) {
        return new Vec2(x,y);
    }
    private static Vec2 vec2(float x) {
        return new Vec2(x,x);
    }
    public static class TestShader extends ModLoadShader {
        public int offsetId=0;
        public Vec2 pos=new Vec2();

        public TestShader() {
            super("test", "default");
        }
        public void setPos(Position pos){
            this.pos.set(pos);
            Draw.shader(this);
        }
        public void setPos(Vec2 pos){
            this.pos.set(pos);
        }
        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(10);

            setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            Vec2 screenSize = getScreenSize();
            setUniformf("iResolution", screenSize);
            Vec2 cameraOffset=Core.camera.position.cpy().sub(Core.camera.width/2f,Core.camera.height/2f);
            float displayScale = Vars.renderer.getDisplayScale();
            setUniformf("u_pos", pos.cpy().sub(cameraOffset).scl(vec2(displayScale)));
            setUniformf("u_dscl", displayScale);
            setUniformf("u_scl", Vars.renderer.getScale());
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


        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(10);

            setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            Vec2 screenSize = getScreenSize();
            setUniformf("iResolution", screenSize);
            Vec2 bulletPos = new Vec2(bullet.y, bullet.x);
            Vec2 cameraOffset=Core.camera.position.cpy().sub(Core.camera.width/2f,Core.camera.height/2f);
            float displayScale = Vars.renderer.getDisplayScale();
            setUniformf("u_screenPos", bulletPos.cpy().sub(cameraOffset).scl(vec2(displayScale)));
            setUniformf("u_pos", bulletPos);
            setUniformf("u_length", type.length * displayScale);
            setUniformf("u_scl", displayScale);
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
        public float force = 1000;

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
