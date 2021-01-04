package braindustry.world.blocks.sandbox;

import braindustry.ModVars.Classes.UI.ModColorPicker;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BlockGroup;

import java.util.Objects;

public class MultyColorBlock extends Block {
    public TextureRegion colorPart;
    public TextureRegion constPart;
    public Color defaultColor=new Color(Color.white);
    protected float layer=-1;

    public MultyColorBlock(String name) {
        super(name);
        this.configurable = true;
        this.update = true;
        this.group = BlockGroup.none;
        this.config(Color.class, (build, color) -> {
            MultyColorBlockBuild tile = (MultyColorBlockBuild) build;
            tile.currentColor = new Color(color);
        });
        this.config(String.class, (build, hex) -> {
            MultyColorBlockBuild tile = (MultyColorBlockBuild) build;
            tile.currentColor = Color.valueOf(hex);
        });
    }

    @Override
    public void load() {
        super.load();
        this.lastConfig=Color.white;
    }

    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        TextureRegion reg = this.getRequestRegion(req, list);
        Draw.rect(reg, req.drawx(), req.drawy(), !this.rotate ? 0.0F : (float) (req.rotation * 90));
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        } else {
            this.drawRequestConfigColor(req, req.block.lastConfig);
        }

    }

    public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list) {
        this.drawRequestConfigColor(req, req.config);
    }
    public void drawRequestConfigColor(BuildPlan req, Object config) {
        if (config==null)config=req.block.lastConfig;
        Color color = Color.valueOf(config + "");
        if (color != null) {
            Draw.color(color);
            Draw.rect(((MultyColorBlock) req.block).colorPart, req.drawx(), req.drawy(), (req.rotation * 90));
            Draw.color();
        }
    }

    public void init() {
        super.init();
        colorPart = Core.atlas.find(this.name+"-recolor");
        constPart = Core.atlas.find(this.name+"-const",this.region);
    }

    public void setBars() {
        super.setBars();
        this.bars.add("Color", (entity) -> {
            Objects.requireNonNull(entity);
            return (new Bar("#"+((MultyColorBlockBuild) entity).currentColor.toString(), ((MultyColorBlockBuild) entity).currentColor, () -> 1f));
        });
//        this.lastConfig=defaultColor;
//        this.bars.add
    }

    public class MultyColorBlockBuild extends Building {
        public Color currentColor;
        ModColorPicker picker=new ModColorPicker();

        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
//            ConstructBlock.constructFinish
            Building build = super.init(tile, team, shouldAdd, rotation);
//            getInfoDialog("","Color", Strings.format("@ @",build.block.lastConfig,build.block.lastConfig.getClass()),Color.gray).show();
            build.configure(build.block.lastConfig);

//            Object lastConfig=this.block.lastConfig;
//            this.playerPlaced(lastConfig.toString());
            return build;
        }

        public void playerPlaced(Object config) {
            if (config == null) return;
//            Color color=new Color((Color)null);
            this.configure(config);
        }

        public void draw() {
//            Color color=new Color((Color)null);
            if (MultyColorBlock.this.layer!=-1){
                Draw.z(MultyColorBlock.this.layer);
            }
            Draw.rect(MultyColorBlock.this.constPart, this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
            this.drawTeamTop();
            Draw.color(currentColor);
            Draw.z(Draw.z()+1);
            Draw.rect(MultyColorBlock.this.colorPart, this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
        }

        public void display(Table table) {
            super.display(table);
            table.row();
//            ((Label) table.label(() -> "" + this.block.lastConfig).style(Styles.outlineLabel).center().growX().get()).setAlignment(1);
        }

        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            Table buttons = new Table();
            buttons.button(Icon.pick, () -> {
                picker.show(currentColor, (color -> {
                    this.configure(color);
                }));
            }).size(44.0F);

            table.add(buttons);
            table.row();
            ((Label) table.label(() -> "@pickcolor").style(Styles.outlineLabel).center().growX().get()).setAlignment(1);

        }

        /*public void configure(Object value) {
            this.block.lastConfig = value;
            Call.tileConfig(Vars.player, this, value);
        }
        public void configureAny(Object value) {
            this.block.lastConfig = value;
            Call.tileConfig(null, this, value);
        }*/
        public Object config() {
            return this.currentColor.toString();
        }

        public void write(Writes write) {
            super.write(write);
            write.str(this.currentColor.toString());
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            this.currentColor = Color.valueOf(read.str());
        }

    }
}
