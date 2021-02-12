package braindustry.graphics;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.scene.ui.layout.Scl;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import braindustry.entities.bullets.AngelContinuousBulletType;
import braindustry.entities.bullets.ContinuousRainbowLaserBulletType;
import braindustry.entities.bullets.RainbowLaserBulletType;
import braindustry.type.LengthBulletType;
import braindustry.world.blocks.TestBlock;
import mindustry.Vars;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.graphics.Shaders;

import static ModVars.modVars.modInfo;

public class ModShaders {
    public static RainbowShader rainbow;
    public static MenuRenderShader menuRender;
    public static LogoRenderShader logoRender;
    public static HoloShader holo;
    public static FractalPyramidShader fractalPyramid;
    public static RainbowLaserShader rainbowLaserShader;
    public static TestShader testShader;
    public static GradientLaserShader gradientLaserShader;
    public static IconBackgroundShader iconBackgroundShader;

    static {
        Shaders.class.getName();
    }

    public static void init() {
        rainbow = new RainbowShader();
        menuRender = new MenuRenderShader();
        logoRender = new LogoRenderShader();
        fractalPyramid = new FractalPyramidShader();
        holo = new HoloShader();
        rainbowLaserShader = new RainbowLaserShader();
        gradientLaserShader = new GradientLaserShader();

        testShader = new TestShader();
        iconBackgroundShader = new IconBackgroundShader();
//        defaultShader=new Shaders.LoadShader("default","default");
    }

    public static Vec2 getScreenSize() {
        Vec2 screenSize = new Vec2(Core.graphics.getWidth(), Core.graphics.getHeight());
        return screenSize;
    }

    private static Vec2 vec2(float x, float y) {
        return new Vec2(x, y);
    }

    private static Vec2 vec2(float x) {
        return vec2(x, x);
    }

    public static class IconBackgroundShader extends ModLoadShader {

        private float length;
        private float fromY;

        public IconBackgroundShader() {
            super("iconBackground", "default");
        }


        public void set(float fromY, float length) {
            this.fromY = fromY;
            this.length = length;
//            Log.info("fromY: @, length: @",fromY,length);
            super.set();
        }

        public void set(float fromY) {
            set(fromY, 0);
        }

        @Override
        public void apply() {
            super.apply();
            setUniformf("toY", fromY);
            setUniformf("length", length);
        }
    }

    public static class TestShader extends ModLoadShader {
        public int offsetId = 0;
        public Vec2 pos = new Vec2();
        private ObjectMap<TextureRegion, Texture> textureMap = new ObjectMap<>();
        private TextureRegion region;
        private Texture selectedTexture;

        public TestShader() {
            super("test", "test");
        }

        @Override
        public void dispose() {
            super.dispose();
            for (ObjectMap.Entry<TextureRegion, Texture> entry : textureMap.entries()) {
                entry.value.dispose();
            }
        }

        public void setPos(Position pos) {
            this.pos.set(pos);
            Draw.shader(this);
        }

        public void setPos(Vec2 pos) {
            this.pos.set(pos);
        }

        public void set(Vec2 pos) {
            setPos(pos);
            set();
        }

        public void set(Position pos) {
            setPos(pos);
            set();
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(10);

            setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            Log.info("Time.delta: @", Time.delta);
            setUniformf("u_delta", Time.delta / 60.f);
            Vec2 cameraOffset = Core.camera.position.cpy().sub(Core.camera.width / 2f, Core.camera.height / 2f);
            float displayScale = Vars.renderer.getDisplayScale();
            setUniformf("u_pos", pos.cpy().sub(cameraOffset).scl(vec2(displayScale)));
            setUniformf("u_dscl", displayScale);
            setUniformf("u_scl", Vars.renderer.getScale());
            if (selectedTexture != null) {
                selectedTexture.bind(3);
                setUniformi("region", 3);
            }
//            this.setUniformf("iResolution", new Vec2().trns(bullet.rotation()-45f,Core.camera.height, Core.camera.width));
//            this.setUniformf("offset", );
        }

        public void set(TestBlock.TestBlockBuild me, TextureRegion region) {
            setPos(me);
            setRegion(region);
            set();
        }

        private void setRegion(TextureRegion region) {
            this.region = region;
            if (textureMap.containsKey(region)){
                selectedTexture=textureMap.get(region);
            } else {
                Pixmap pixmap=new Pixmap(region.width,region.height);

//                pixmap.draw(region);

                textureMap.put(region,new Texture(pixmap));
                setRegion(region);
            }
        }
    }

    public static class GradientLaserShader extends ModLoadShader {
        public int offsetId = 0;
        public Bullet bullet;
        public LengthBulletType type;
        private Color from;
        private Color to;

        public GradientLaserShader() {
            super("gradientLaser", "default");
        }

        public void set(Bullet bullet, LengthBulletType type, Color from, Color to) {
            setBullet(bullet, type, from, to);
            set();
        }

        public Shader setColors(Color from, Color to) {
            this.from = from;
            this.to = to;
            return this;
        }

        public Shader setBullet(Bullet bullet, LengthBulletType type, Color from, Color to) {
            offsetId = bullet.id;
            this.bullet = bullet;
            this.type = type;
            return setColors(from, to);
        }

        private float getLength() {
            return type!=null?type.length():0;
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / Scl.scl(10);

            setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
            Vec2 screenSize = getScreenSize();
            setUniformf("iResolution", screenSize);
            Vec2 bulletPos = new Vec2(bullet.y, bullet.x);
            Vec2 cameraOffset = Core.camera.position.cpy().sub(Core.camera.width / 2f, Core.camera.height / 2f);
            float displayScale = Vars.renderer.getDisplayScale();
            setUniformf("u_screenPos", bulletPos.cpy().sub(cameraOffset).scl(vec2(displayScale)));
            setUniformf("u_pos", bulletPos);
            setUniformf("u_length", getLength() * displayScale);
            setUniformf("u_scl", displayScale);
            setUniformf("u_fromColor", from);
            setUniformf("u_toColor", to);
        }
    }
        public static class RainbowLaserShader extends ModLoadShader {
            public int offsetId = 0;
            public Bullet bullet;
            public LengthBulletType type;
            private int applyCount = 0;

            public RainbowLaserShader() {
                super("rainbowLaser", "default");
            }

            public void set(Bullet bullet, LengthBulletType type) {
                setBullet(bullet, type);
                set();
            }

            public Shader setBullet(Bullet bullet, LengthBulletType type) {
                offsetId = bullet.id;
                this.bullet = bullet;
                this.type = type;
                return this;
            }

            private float getLength() {
                return type == null ? 0:type.length();
            }

            @Override
            public void apply() {
                float u_time = Time.time / Scl.scl(10);
                setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100f, 100f));
                Vec2 screenSize = getScreenSize();
                setUniformf("iResolution", screenSize);
                Vec2 bulletPos = new Vec2(bullet.y, bullet.x);
                Vec2 cameraOffset = Core.camera.position.cpy().sub(Core.camera.width / 2f, Core.camera.height / 2f);
                float displayScale = Vars.renderer.getDisplayScale();
                setUniformf("u_screenPos", bulletPos.cpy().sub(cameraOffset).scl(vec2(displayScale)));
                setUniformf("u_pos", bulletPos);
                setUniformf("u_length", getLength() * displayScale);
                setUniformf("u_scl", displayScale);
                setUniformf("u_vecRot", new Vec2(Mathf.cosDeg(bullet.rotation()), Mathf.sinDeg(bullet.rotation())));
                setUniformf("u_offset", new Vec3(
                        -2, 2, -0));
                Log.info("rot: @", bullet.rotation());
                setUniformf("u_bulletRot", bullet.rotation());

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
        public float force = 10000;

        public LogoRenderShader() {
            super("logoRender", "logoRender");
            logo = Core.atlas.find("");
        }

        @Override
        public void apply() {
            super.apply();
            float u_time = Time.time / 100f;

            float displayScale = Vars.renderer.getDisplayScale();
            this.setUniformf("u_texsize", new Vec2(logo.texture.width, logo.texture.height));
            Vec2 logoSize = vec2(logo.width, logo.height);
            Vec2 logoTexSize = vec2(logo.texture.width, logo.texture.height);
            this.setUniformf("u_uv", vec2(logo.u, logo.v));

            this.setUniformf("u_uv2", vec2(logo.u2, logo.v2));
            setUniformf("u_campos", Core.camera.position);
            this.setUniformf("u_texsize", logo.texture.width, logo.texture.height);
            this.setUniformf("u_time", u_time + Mathf.randomSeed(offsetId, -100, 100));
            this.setUniformf("u_timeMul", Time.time / 10f);
            this.setUniformf("u_yOffset", 2.f);
            this.setUniformf("u_force", force);
            this.setUniformf("u_scl", Scl.scl(4));
            this.setUniformf("u_resolution", getScreenSize());
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
        public long offsetId = 0;

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

        public void set(Entityc entityc) {
            this.offsetId = entityc.id();
            super.set();
        }

        public void set(long id) {
            this.offsetId = id;
            super.set();
        }
    }

    public static class ModLoadShader extends Shader {
        public ModLoadShader(String fragment, String vertex) {
            super(loadFile(vertex + ".vert"), loadFile(fragment + ".frag"));
        }

        private static Fi loadFile(String fileName) {
            Seq<Fi> modShaders = Seq.with(modInfo.root.child("shaders").list());
            Fi foundFile = modShaders.find(fi -> fi.name().equals(fileName));
            if (foundFile == null) {
                return Core.files.internal("shaders/" + (fileName));
            }
            return foundFile;
        }

        public void set() {
            Draw.shader(this);
        }

        @Override
        public void apply() {
            super.apply();
            setUniformf("u_resolution", vec2(Core.graphics.getWidth(), Core.graphics.getHeight()));
        }

    }
}
