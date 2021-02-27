package braindustry.world.blocks;

import ModVars.modVars;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Strings;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.content.ModFx;
import braindustry.graphics.ModShaders;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustryAddition.graphics.ModFill;
import mindustryAddition.world.blocks.BuildingLabel;

import static ModVars.modFunc.fullName;

public class TestBlock extends Block {
    public final int timerAny;
    private final static int doubleLength=4,triableLength=1;
    public @ModAnnotations.Load(value = "@-2-#", length = doubleLength,fallback = "@")
    TextureRegion[] doubleSize;
    public @ModAnnotations.Load(value = "@-3-#", length = triableLength,fallback = "@")
    TextureRegion[] triableSize;
    public @ModAnnotations.Load(value = "@-2-top")
    TextureRegion doubleTop;

    public TestBlock(String name) {
        super(name);
        timerAny = timers++;
        this.rotate = true;
        this.destructible = true;
        this.update = true;
        configurable = true;
        saveConfig=true;
        this.<Integer,TestBlockBuild>config(Integer.class,(build,value)->{
            build.selectedSprite=value%build.getSizeSprites().length;
        });
        this.<TestBlockBuild>configClear(build->{
            build.selectedSprite=1;
        });
    }

    @Override
    public void load() {
        if (modVars.packSprites) {
            super.load();
            return;
        }
        Core.atlas.addRegion(this.name, Core.atlas.find(fullName("testBlock")));
        for (int i = 0; i < doubleLength; i++) {
            Core.atlas.addRegion(Strings.format("@-2-@",name,i), Core.atlas.find(fullName(Strings.format("testBlock-2-@",i+1))));
        }
        for (int i = 0; i < triableLength; i++) {
            Core.atlas.addRegion(Strings.format("@-3-@",name,i), Core.atlas.find(fullName(Strings.format("testBlock-3-@",i+1))));
        }
        super.load();
//        this.region=Core.atlas.find(getFullName("testBlock"));
    }

    public TextureRegion editorIcon() {
        if (this.editorIcon == null) {
            this.editorIcon = Core.atlas.find(this.name + "-icon-editor");
        }
        return editorIcon;
    }

    @Override
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        TextureRegion reg = this.getRequestRegion(req, list);
        Draw.rect(reg, req.drawx(), req.drawy(), this.size * 8, this.size * 8, 0f);
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        }
    }

    public class TestBlockBuild extends Building implements BuildingLabel {
        private float time = 0;
        private boolean spriteListing = false, move = true;
        private int spriteIndex = 1;
        private int selectedSprite;
        private float spikeOffset=0.5f;

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            TestBlockBuild me = this;
            table.table((t) -> {
                t.button(Icon.up, () -> {
                    spriteListing = !spriteListing;
                });
                t.button(Icon.terminal, () -> {
                    ModFx.Spirals.at(x, y, size, Pal.lancerLaser);
                });
                t.button(Icon.edit, () -> {
                    BaseDialog dialog = new BaseDialog("") {
                        @Override
                        public void draw() {
                            Draw.draw(Draw.z(), () -> {
                                move = true;
                                Tmp.v1.set(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f);
                                if (spriteListing) {
                                    Seq<TextureAtlas.AtlasRegion> regions = Core.atlas.getRegions();
                                    spriteIndex = Mathf.mod(spriteIndex, regions.size);
                                    TextureAtlas.AtlasRegion drawRegion = regions.get(spriteIndex);
                                    ModShaders.testShader.set(me, drawRegion);
//                                super.draw();
                                    Draw.rect(drawRegion, Tmp.v1.x, Tmp.v1.y, Core.graphics.getWidth(), Core.graphics.getHeight());
                                    Draw.shader();
                                } else {
                                    ModShaders.testShader.set(me, region);
                                    Vec2 pos = ModShaders.worldToScreen(TestBlockBuild.this);
                                    float scl = Vars.renderer.getDisplayScale();
                                    float size = TestBlock.this.size * 8 * scl;
                                    ModShaders.waveShader.rect(getRegion(), pos.x, pos.y, size, size).forcePercent(5f / region.width).otherAxisMul(50);
                                    Draw.shader();
                                }

                            });
                        }
                    };
                    dialog.addCloseListener();
                    dialog.show();
                });
            }).row();
            table.table(Tex.button, t->{
                t.slider(0, 360, .001f, modVars.settings.getFloat("angle"), (f) -> {
                    modVars.settings.setFloat("angle", Mathf.round(f, 0.001f));
                }).row();
                t.label(() -> {
                    String angle = Mathf.round(modVars.settings.getFloat("angle"), 1f) + "";
                    StringBuilder builder = new StringBuilder(angle);
                    while (builder.length() < 3) {
                        builder.insert(0, "0");
                    }
                    return builder.toString();
                });
            }).row();
            table.table(Tex.button,t->{
                TextureRegion[] sizeSprites = getSizeSprites();
                int max = sizeSprites.length;
                t.slider(-1, 1, 0.000001f, spikeOffset, (f) -> {
                    spikeOffset=f;
                }).row();
                t.label(() -> {
                    return Strings.format("@",spikeOffset);
                }).right();
            }).row();
            table.table(Tex.button,t->{
                TextureRegion[] sizeSprites = getSizeSprites();
                int max = sizeSprites.length;
                t.slider(1, max, 1, selectedSprite+1, (f) -> {
                    configure(Mathf.mod(Mathf.round(f, 1)-1,max));
                }).row();/*
                t.label(() -> {
                    return Strings.format("@/@",selectedSprite+1,max);
                });*/
            }).row();
        }
        public TextureRegion[] getSizeSprites(){
            return  size == 2 ? doubleSize : size == 3 ? triableSize : new TextureRegion[0];
        }
        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {

            Building building = super.init(tile, team, shouldAdd, rotation);
            return building;
        }

        @Override
        public void playerPlaced(Object config) {
            configure(config);
        }

        public Building nearby(int rotation) {
            if (rotation == 0) {
                return Vars.world.build(this.tile.x + (int) (this.block.size / 2 + 1), this.tile.y);
            } else if (rotation == 1) {
                return Vars.world.build(this.tile.x, this.tile.y + (int) (this.block.size / 2 + 1));
            } else if (rotation == 2) {
                return Vars.world.build(this.tile.x - Mathf.ceil(this.block.size / 2f), this.tile.y);
            } else {
                return rotation == 3 ? Vars.world.build(this.tile.x, this.tile.y - Mathf.ceil(this.block.size / 2f)) : null;
            }
        }

        @Override
        public byte version() {
            return 1;
        }

        public Building front() {
            return this.nearby(this.rotation);
        }

        @Override
        public void updateTile() {
            super.updateTile();
            time += this.delta() / 60f;

            if (move && spriteListing && timer.get(timerAny, 1)) {
                spriteIndex += spriteListing ? 1 : -1;
                Seq<TextureAtlas.AtlasRegion> regions = Core.atlas.getRegions();
                spriteIndex = Mathf.mod(spriteIndex, regions.size);
                move = false;
//                Fx.upgradeCore.at(this.x,this.y,this.block.size, Color.white);
            }
        }

        public void draw() {
            TextureRegion region = getRegion();
            Draw.rect(region, this.x, this.y, this.block.size * 8, this.block.size * 8, 0.0F);
//            Draw.rect(editorIcon(), x, y + size * 8, size * 8, size * 8, 0f);
            Draw.alpha(0.5f);
            ModFill.spikesSwirl(x, y,(size) * 8, 8, modVars.settings.getFloat("angle") / 360f, rotdeg(),spikeOffset);
//            Lines.stroke(40f);
//            Lines.swirl(x+(size+2)*8,y,(size+1)*8, modVars.settings.getFloat("angle")/360f,rotdeg());
            Draw.reset();
            Building front = this.front();
//            if (true)return;
            if (front != null && front.block != null) {
                float size = front.block.size * 8f;
                Draw.z(Layer.blockBuilding + 1f);
                Draw.color(Color.green);
                Draw.alpha(0.25f);
                float offset = Mathf.ceil(size / 2f);
                Lines.rect(front.x - offset, front.y - offset, size, size);
            }
        }

        protected TextureRegion getRegion() {
            TextureRegion[] sizeSprites = getSizeSprites();
            TextureRegion region = this.block.region;
            if (sizeSprites.length!=0) {
                region=sizeSprites[selectedSprite];
            }
            return region;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(selectedSprite);
        }

        @Override
        public void read(Reads read, byte revision) {
            if (revision>0){
                selectedSprite=read.i()%getSizeSprites().length;
            }
        }
    }
}
