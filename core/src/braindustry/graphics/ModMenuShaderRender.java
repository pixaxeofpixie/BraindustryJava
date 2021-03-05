package braindustry.graphics;

import ModVars.modVars;
import arc.Core;
import arc.files.Fi;
import arc.func.Floatc2;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.Angles;
import arc.math.Mat;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.*;
import arc.util.noise.RidgedPerlin;
import arc.util.noise.Simplex;
import braindustry.content.Blocks.ModBlocks;
import braindustry.content.ModUnitTypes;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.graphics.MenuRenderer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.CachedTile;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.OreBlock;

import java.util.Iterator;

import static ModVars.modFunc.fullName;
import static arc.Core.camera;
import static mindustry.Vars.*;
import static mindustry.Vars.ui;

public class ModMenuShaderRender extends MenuRenderer {
    private static final float darkness = 0.3F;
    private final int width;
    private final int height;
    private int cacheFloor;
    private int cacheWall;
    private Camera camera;
    private Mat mat;
    private FrameBuffer shadows;
    private CacheBatch batch;
    private float time;
    private float flyerRot;
    private int flyers;
    private UnitType flyerType;
    public void takeBackgroundScreenshot(){
        int aFloat = (int) modVars.settings.getFloat("background.screenshot.scl");
        int w = width * tilesize* aFloat, h = height * tilesize*aFloat;
        int memory = w * h * 4 / 1024 / 1024;

        if(memory >= 65){
            ui.showInfo("@screenshot.invalid");
            return;
        }
        FrameBuffer buffer = new FrameBuffer(w, h);
        float lastDelta = Time.delta;
        Time.delta=0;
        render();
        float vpW = camera.width, vpH = camera.height, px = camera.position.x, py = camera.position.y;
        disableUI = true;
        camera.width = w;
        camera.height = h;
        camera.position.x = w / 2f + tilesize / 2f;
        camera.position.y = h / 2f + tilesize / 2f;
        buffer.begin();
        render();
        buffer.end();
        disableUI = false;
        camera.width = vpW;
        camera.height = vpH;
        camera.position.set(px, py);
        buffer.begin();
        byte[] lines = ScreenUtils.getFrameBufferPixels(0, 0, w, h, true);
        for(int i = 0; i < lines.length; i += 4){
            lines[i + 3] = (byte)255;
        }
        buffer.end();
        Pixmap fullPixmap = new Pixmap(w, h, Pixmap.Format.rgba8888);
        Buffers.copy(lines, 0, fullPixmap.getPixels(), lines.length);
        Fi file = screenshotDirectory.child("screenshot-background-" + Time.millis() + ".png");
        PixmapIO.writePNG(file, fullPixmap);
        fullPixmap.dispose();
        ui.showInfoFade(Core.bundle.format("screenshot", file.toString()));

        buffer.dispose();
        Time.delta=lastDelta;
    }
    public ModMenuShaderRender() {
        this.width = !Vars.mobile ? 100 : 60;
        this.height = !Vars.mobile ? 50 : 40;
        buildMain(true);
    }
    public void rebuild(){
        batch.dispose();
        shadows.dispose();
        shadows=null;
        batch=null;
        buildMain(false);
    }
    protected void buildMain(boolean timeMark) {
        this.camera = new Camera();
        this.mat = new Mat();
        Mathf.rand.setSeed(System.nanoTime());
        this.time = 0.0F;
        this.flyerRot = 45.0F;
        this.flyers = Mathf.chance(0.2D) ? Mathf.random(35) : Mathf.random(15);
        this.flyerType = Structs.select(
                UnitTypes.flare, UnitTypes.flare,
                UnitTypes.horizon, UnitTypes.mono,
                UnitTypes.poly, UnitTypes.mega,
                UnitTypes.zenith,
                ModUnitTypes.armor, ModUnitTypes.armor,
                ModUnitTypes.chainmail,ModUnitTypes.chestplate);
      if(timeMark)  Time.mark();
        this.generate();
        this.cache();
        if(timeMark)   Log.info("Time to generate menu: @", Time.elapsed());
    }

    public static Shader createShader() {
        return new Shader("attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nattribute vec4 a_mix_color;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec4 v_mix_color;\nvarying vec2 v_texCoords;\n\nvoid main(){\n   v_color = a_color;\n   v_color.a = v_color.a * (255.0/254.0);\n   v_mix_color = a_mix_color;\n   v_mix_color.a *= (255.0/254.0);\n   v_texCoords = a_texCoord0;\n   gl_Position = u_projTrans * a_position;\n}",
                "\nvarying lowp vec4 v_color;\nvarying lowp vec4 v_mix_color;\nvarying highp vec2 v_texCoords;\nuniform highp sampler2D u_texture;\n\nvoid main(){\n  vec4 c = texture2D(u_texture, v_texCoords);\n  gl_FragColor = v_color * mix(c, vec4(v_mix_color.rgb, c.a), v_mix_color.a);\n}");
    }
    private void generate() {
        Vars.world.beginMapLoad();
        Tiles tiles = Vars.world.resize(this.width, this.height);
        Seq<Block> ores = Vars.content.blocks().select((b) -> {
            return b instanceof OreBlock;
        });
        this.shadows = new FrameBuffer(this.width, this.height);
        int offset = Mathf.random(100000);
        Simplex s1 = new Simplex(offset);
        Simplex s2 = new Simplex((offset + 1));
        Simplex s3 = new Simplex((offset + 2));
        RidgedPerlin rid = new RidgedPerlin(1 + offset, 1);
//        Rand last = Mathf.rand;
//        Mathf.rand=new Rand(System.nanoTime());
        Block[] selected = Structs.select(new Block[][]{
                {Blocks.sand, Blocks.sandWall},
                {Blocks.shale, Blocks.shaleWall},
                {Blocks.ice, Blocks.iceWall},
                {ModBlocks.obsidianFloor,ModBlocks.obsidianBlock},
                {Blocks.sand, Blocks.sandWall},
                {Blocks.shale, Blocks.shaleWall},
                {Blocks.ice, Blocks.iceWall},
                {ModBlocks.obsidianFloor,ModBlocks.obsidianBlock},
                {Blocks.moss, Blocks.sporePine},
        });
        Block[] selected2 = Structs.select(new Block[][]{
                {Blocks.basalt, Blocks.duneWall},
                {Blocks.basalt, Blocks.duneWall},
                {Blocks.stone, Blocks.stoneWall},
                {Blocks.stone, Blocks.stoneWall},
                {Blocks.moss, Blocks.sporeWall},
                {Blocks.salt, Blocks.saltWall},
                {ModBlocks.jungleFloor,ModBlocks.jungleWall},
                {ModBlocks.jungleFloor,ModBlocks.jungleWall},
                {ModBlocks.crimzesFloor,ModBlocks.crimzesWall},
                {Blocks.water, Blocks.sandWall},

        });
        Block ore1 = ores.random();
        ores.remove(ore1);
        Block ore2 = ores.random();
        double tr1 = (double)Mathf.random(0.65F, 0.85F);
        double tr2 = (double)Mathf.random(0.65F, 0.85F);
        boolean doheat = Mathf.chance(0.25D);
        boolean tendrils = Mathf.chance(0.25D);
        boolean tech = Mathf.chance(0.25D);
        int secSize = 10;
        Block floord = selected[0];
        Block walld = selected[1];
        Block floord2 = selected2[0];
        Block walld2 = selected2[1];

        for(int x = 0; x < this.width; ++x) {
            for(int y = 0; y < this.height; ++y) {
                Block floor = floord;
                Block ore = Blocks.air;
                Block wall = Blocks.air;
                if (s1.octaveNoise2D(3.0D, 0.5D, 0.05D, (double)x, (double)y) > 0.5D) {
                    wall = walld;
                }

                if (s3.octaveNoise2D(3.0D, 0.5D, 0.05D, (double)x, (double)y) > 0.5D) {
                    floor = floord2;
                    if (wall != Blocks.air) {
                        wall = walld2;
                    }
                }

                if (s2.octaveNoise2D(3.0D, 0.3D, 0.03333333333333333D, (double)x, (double)y) > tr1) {
                    ore = ore1;
                }

                if (s2.octaveNoise2D(2.0D, 0.2D, 0.06666666666666667D, (double)x, (double)(y + 99999)) > tr2) {
                    ore = ore2;
                }

                if (doheat) {
                    double heat = s3.octaveNoise2D(4.0D, 0.6D, 0.02D, (double)x, (double)(y + 9999));
                    double base = 0.65D;
                    if (heat > base) {
                        ore = Blocks.air;
                        wall = Blocks.air;
                        floor = Blocks.basalt;
                        if (heat > base + 0.1D) {
                            floor = Blocks.hotrock;
                            if (heat > base + 0.15D) {
                                floor = Blocks.magmarock;
                            }
                        }
                    }
                }

                if (tech) {
                    int mx = x % secSize;
                    int my = y % secSize;
                    int sclx = x / secSize;
                    int scly = y / secSize;
                    if (s1.octaveNoise2D(2.0D, 0.10000000149011612D, 0.5D, (double)sclx, (double)scly) > 0.4000000059604645D && (mx == 0 || my == 0 || mx == secSize - 1 || my == secSize - 1)) {
                        floor = Blocks.darkPanel3;
                        if (Mathf.dst((float)mx, (float)my, (float)(secSize / 2), (float)(secSize / 2)) > (float)secSize / 2.0F + 1.0F) {
                            floor = Blocks.darkPanel4;
                        }

                        if (wall != Blocks.air && Mathf.chance(0.7D)) {
                            wall = Blocks.darkMetal;
                        }
                    }
                }

                if (tendrils && rid.getValue((double)x, (double)y, 0.05882353F) > 0.0F) {
                    floor = Mathf.chance(0.2D) ? Blocks.sporeMoss : Blocks.moss;
                    if (wall != Blocks.air) {
                        wall = Blocks.sporeWall;
                    }
                }

                CachedTile tile;
                tiles.set(x, y, tile = new CachedTile());
                tile.x = (short)x;
                tile.y = (short)y;
                tile.setFloor(floor.asFloor());
                tile.setBlock(wall);
                tile.setOverlay(ore);
            }
        }

        Vars.world.endMapLoad();
    }

    private void cache() {
        Draw.proj().setOrtho(0.0F, 0.0F, (float)this.shadows.getWidth(), (float)this.shadows.getHeight());
        this.shadows.begin(Color.clear);
        Draw.color(Color.black);

        for (Tile tile : Vars.world.tiles) {
            if (tile.block() != Blocks.air) {
                Fill.rect((float) tile.x + 0.5F, (float) tile.y + 0.5F, 1.0F, 1.0F);
            }
        }

        Draw.color();
        this.shadows.end();
        Batch prev = Core.batch;
        Core.batch = this.batch = new CacheBatch(new SpriteCache(this.width * this.height * 6, false));
        this.batch.beginCache();
        Iterator<Tile> var5 = Vars.world.tiles.iterator();

        Tile tile;
        while(var5.hasNext()) {
            tile = (Tile)var5.next();
            tile.floor().drawBase(tile);
        }

        var5 = Vars.world.tiles.iterator();

        while(var5.hasNext()) {
            tile = (Tile)var5.next();
            tile.overlay().drawBase(tile);
        }

        this.cacheFloor = this.batch.endCache();
        this.batch.beginCache();
        var5 = Vars.world.tiles.iterator();

        while(var5.hasNext()) {
            tile = (Tile)var5.next();
            tile.block().drawBase(tile);
        }

        this.cacheWall = this.batch.endCache();
        Core.batch = prev;
    }
    boolean errorred =false;
    public void render() {
        this.time += Time.delta;
        float scaling = Math.max(Scl.scl(4.0F), Math.max((float)Core.graphics.getWidth() / (((float)this.width - 1.0F) * 8.0F), (float)Core.graphics.getHeight() / (((float)this.height - 1.0F) * 8.0F)));
        this.camera.position.set((float)(this.width * 8) / 2.0F, (float)(this.height * 8) / 2.0F);
        this.camera.resize((float)Core.graphics.getWidth() / scaling, (float)Core.graphics.getHeight() / scaling);
        this.mat.set(Draw.proj());
        Draw.flush();
        Draw.proj(this.camera);
        this.batch.setProjection(this.camera.mat);
        this.batch.beginDraw();
//        Fill.rect(camera.position.x,camera.position.y,width*8,height*8);
        this.batch.drawCache(this.cacheFloor);
        this.batch.endDraw();
        Draw.color();

        Draw.rect(Draw.wrap((Texture)this.shadows.getTexture()), (float)(this.width * 8) / 2.0F - 4.0F, (float)(this.height * 8) / 2.0F - 4.0F, (float)(this.width * 8), (float)(-this.height * 8));
        Draw.flush();
        this.batch.beginDraw();
        this.batch.drawCache(this.cacheWall);
        this.batch.endDraw();
        this.drawFlyers();
        Draw.shader(ModShaders.menuRender);
        Draw.proj(this.mat);
        Draw.color(0.0F, 0.0F, 0.0F, 0.3F);
        Fill.crect(0.0F, 0.0F, (float)Core.graphics.getWidth(), (float)Core.graphics.getHeight());
        Draw.color();
        Draw.shader();
        /*try {
            ModShaders.logoRender.logo=logoRegion;
        } catch (NullPointerException exception) {
            if (!errorred){
                modFunc.showException(exception);
                errorred=true;
            }
//            exception.printStackTrace();
        }
        Draw.shader(ModShaders.logoRender);
        Draw.rect(logoRegion,Core.graphics.getWidth()-logoRegion.width/2f,logoRegion.height/2f,logoRegion.width,logoRegion.height);
        Draw.shader();*/
    }

    private void drawFlyers() {
        Draw.color(0.0F, 0.0F, 0.0F, 0.4F);
        TextureRegion icon = this.flyerType.icon(Cicon.full);
        float size = (float)Math.max(icon.width, icon.height) * Draw.scl * 1.6F;
        this.flyers((x, y) -> {
            Draw.rect(icon, x - 12.0F, y - 13.0F, this.flyerRot - 90.0F);
        });
        this.flyers((x, y) -> {
            Draw.rect("circle-shadow", x, y, size, size);
        });
        Draw.color();
        this.flyers((x, y) -> {
            float engineOffset = this.flyerType.engineOffset;
            float engineSize = this.flyerType.engineSize;
            float rotation = this.flyerRot;
            Draw.color(Pal.engine);
            Fill.circle(x + Angles.trnsx(rotation + 180.0F, engineOffset), y + Angles.trnsy(rotation + 180.0F, engineOffset), engineSize + Mathf.absin(Time.time, 2.0F, engineSize / 4.0F));
            Draw.color(Color.white);
            Fill.circle(x + Angles.trnsx(rotation + 180.0F, engineOffset - 1.0F), y + Angles.trnsy(rotation + 180.0F, engineOffset - 1.0F), (engineSize + Mathf.absin(Time.time, 2.0F, engineSize / 4.0F)) / 2.0F);
            Draw.color();
            Draw.rect(icon, x, y, this.flyerRot - 90.0F);
        });
    }

    private void flyers(Floatc2 cons) {
        float tw = (float)(this.width * 8) * 1.0F + 8.0F;
        float th = (float)(this.height * 8) * 1.0F + 8.0F;
        float range = 500.0F;
        float offset = -100.0F;

        for(int i = 0; i < this.flyers; ++i) {
            Tmp.v1.trns(this.flyerRot, this.time * (2.0F + this.flyerType.speed));
            cons.get((Mathf.randomSeedRange((long)i, range) + Tmp.v1.x + Mathf.absin(this.time + Mathf.randomSeedRange((long)(i + 2), 500.0F), 10.0F, 3.4F) + offset) % (tw + (float)Mathf.randomSeed((long)(i + 5), 0, 500)), (Mathf.randomSeedRange((long)(i + 1), range) + Tmp.v1.y + Mathf.absin(this.time + Mathf.randomSeedRange((long)(i + 3), 500.0F), 10.0F, 3.4F) + offset) % th);
        }

    }

    public void dispose() {
        this.batch.dispose();
        this.shadows.dispose();
    }
}
