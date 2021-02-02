package braindustry.world.blocks.sandbox;

import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.world.blocks.ModSelection;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.io.TypeIO;
import mindustry.type.Category;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.meta.BlockGroup;

public class PayloadSource extends Block {
    public PayloadSource(String name) {
        super(name);
        this.outputsPayload = true;
        this.update = true;
        this.solid = true;
        this.group = BlockGroup.transportation;
        this.configurable = true;
        this.saveConfig = true;
        this.noUpdateDisabled = true;
        category= Category.distribution;
        this.<Integer, PayloadSourceBuild>config(Integer.class, (tile, item) -> {
            tile.page = item;
        });
        config(UnlockableContent.class, (tile, item) -> {
            ((PayloadSourceBuild)tile).output=item;
        });
        this.<PayloadSourceBuild>configClear((tile) -> {
//            tile.output = null;
        });
    }

    public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list) {

        Object config = req.config;
        TextureRegion region = config instanceof UnitType ? ((UnitType) config).region : config instanceof Block ? ((Block) config).region : null;
        this.drawRequestConfigCenter(req, config, region, true);
    }

    public TextureRegion getRegion(Object config) {
        return config instanceof UnitType ? ((UnitType) config).region : config instanceof Block ? ((Block) config).region : null;
    }

    public void drawRequestConfigCenter(BuildPlan req, Object content, TextureRegion region, boolean cross) {
        if (content == null) {
            if (cross) {
                Draw.rect("cross", req.drawx(), req.drawy());
            }

        } else {
            Draw.rect(region, req.drawx(), req.drawy());
            Draw.color();
        }
    }

    public class PayloadSourceBuild extends Building {
        public UnlockableContent output;
        public int page = -1;

        public void draw() {
            super.draw();
            TextureRegion outputRegion = getRegion(output);
            if (output == null) {
                Draw.rect("cross", this.x, this.y);
            } else {
                if (outputRegion != null) {
                    Draw.rect(outputRegion, this.x, this.y);
                    Draw.color();
                }
            }

        }

        public Payload getPayload() {
            return output instanceof Block ? new BuildPayload((Block) output, team) : output instanceof UnitType ? new UnitPayload(((UnitType) output).create(team)) : null;
        }

        public void updateTile() {
            Payload payload = getPayload();
            if (payload==null)return;
            dumpPayload(payload);
        }

        protected boolean check(float size, float min, float max) {
            return size >= min && size <= max;
        }

        public void buildConfiguration(Table table) {
            table.clear();
            table.clearChildren();
            table.top();
            ModSelection.buildTable(table, Seq.with("1..2","3..4","5..6","7>"), () -> this.page, (page) -> {
                this.page = page;
                buildConfiguration(table);
            },false);
            if (page==-1)return;
            table.row();
            Seq<Boolf<Float>> sizes = Seq.with(
                    (f) -> check(f, 1, 2),
                    (f) -> check(f, 3, 4),
                    (f) -> check(f, 5, 6),
                    (f) -> check(f, 7, Integer.MAX_VALUE)
            );
            Seq<UnlockableContent> content = Seq.<UnlockableContent>withArrays(Vars.content.blocks(), Vars.content.units()).select(con -> {
                float size = -1;
                if (con instanceof Block) {
                    size = ((Block) con).size;
                } else if (con instanceof UnitType) {
                    size = ((UnitType) con).hitSize / Vars.tilesize;
                }
                return sizes.get(page).get(size);
            });
            ItemSelection.buildTable(table, content, () -> output,this::configure);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            page=read.i();
            output=(UnlockableContent) TypeIO.readObject(read);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(page);
            TypeIO.writeObject(write,output);
        }
    }
}
