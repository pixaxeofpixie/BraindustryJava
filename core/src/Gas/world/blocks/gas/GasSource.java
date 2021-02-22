package Gas.world.blocks.gas;


import Gas.gen.GasBuilding;
import Gas.type.Gas;
import Gas.world.GasBlock;
import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.meta.BlockGroup;

public class GasSource extends GasBlock {
    public GasSource(String name) {
        super(name);
        this.hasGas = true;
        this.outputsGas=true;
        this.update = true;
        this.solid = true;
        this.group = BlockGroup.transportation;
        this.configurable = true;
        this.saveConfig = true;
        this.noUpdateDisabled = true;
        this.displayFlow = false;
        this.config(Gas.class, (t, gas) -> {
            GasSourceBuild tile=(GasSourceBuild)t;
            tile.source = gas;
        });
        this.configClear((t) -> {
            GasSourceBuild tile=(GasSourceBuild)t;
            tile.source = null;
        });
    }
    public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list) {
        this.drawRequestConfigCenter(req, req.config, "center");
    }
    public void setBars() {
        super.setBars();
        this.bars.remove("gas");
    }
    public class GasSourceBuild extends GasBuilding{
        @Nullable
        Gas source =null;
        public void updateTile() {
            if (this.source == null) {
                this.gasses.clear();
            } else {
                this.gasses.add(this.source, GasSource.this.gasCapacity);
                this.dumpGas(this.source);
            }

        }

        public void draw() {
            super.draw();
            if (this.source == null) {
                Draw.rect("cross", this.x, this.y);
            } else {
                Draw.color(this.source.color);
                Draw.rect("center", this.x, this.y);
                Draw.color();
            }

        }

        public void buildConfiguration(Table table) {
            ItemSelection.buildTable(table, Vars.content.getBy(ContentType.typeid_UNUSED), () -> {
                return this.source;
            }, this::configure);
        }

        public boolean onConfigureTileTapped(Building other) {
            if (this == other) {
                this.deselect();
                this.configure((Object)null);
                return false;
            } else {
                return true;
            }
        }

        public Gas config() {
            return this.source;
        }

        public byte version() {
            return 1;
        }

        public void write(Writes write) {
            super.write(write);
            write.s(this.source == null ? -1 : this.source.id);
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int id = revision == 1 ? read.s() : read.b();
            this.source = id == -1 ? null : Vars.content.getByID(ContentType.typeid_UNUSED,id);
        }

    }
}
