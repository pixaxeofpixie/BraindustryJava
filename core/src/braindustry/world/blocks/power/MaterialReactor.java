package braindustry.world.blocks.power;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.content.ModFx;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Puddles;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.io.TypeIO;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.power.ItemLiquidGenerator;

public class MaterialReactor extends ItemLiquidGenerator {
    public Effect effect1, effect2, effect3;
    public Color lightningColor = Color.valueOf("#00FFF9");
    public Color destroyLightingColor = Color.valueOf("#00FFF9");
    public TextureRegion lightsRegion;

    public MaterialReactor(boolean hasItems, boolean hasLiquids, String name) {
        super(hasItems, hasLiquids, name);
        effect1 = ModFx.electricExplosionPart1;
        effect2 = ModFx.electricExplosionPart1;
        effect3 = ModFx.electricExplosionPart1;
        configurable=true;
        outputsPower=true;
    }

    public MaterialReactor(String name) {
        this(false, false, name);
    }

    @Override
    public void load() {
        super.load();
        lightsRegion = Core.atlas.find(name + "-lights");
    }

    @Override
    public void setBars() {
        super.setBars();
        this.bars.add("efficiency", en -> {
                    MaterialReactorBuild e = (MaterialReactorBuild) en;
                    return new Bar(
                            () -> Core.bundle.get("efficiency") + ": " + e.getEfficiency(),
                            () -> Tmp.c1.set(Color.valueOf("#FFE679")).lerp(Color.valueOf("#78FFFD"), Mathf.sin(Time.time * 0.03f) * e.getEfficiency() / 2f + 0.5f),
                            () -> e.getEfficiency() / 10f
                    );
                }

        );
    }

    public class MaterialReactorBuild extends ItemLiquidGenerator.ItemLiquidGeneratorBuild {
        public boolean work;
        public Item item;
        public Liquid liquid;
        public int efficiency;

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            Building building = super.init(tile, team, shouldAdd, rotation);

            this.setItem(null);
            this.setLiquid(null);
            this.setWork(false);
            this.efficiency(1);

            return this;
        }

        public void buildConfiguration(Table table) {
            Slider slider = table.slider(0, 10, 1, this.getEfficiency(), null).width(240).color(Color.valueOf("FFFFFF")).get();
            slider.changed(() -> {
                this.efficiency(Mathf.round(slider.getValue(), 1));
            });
        }

        public void draw() {
            super.draw();

            Draw.blend(Blending.additive);
            Draw.color(Color.valueOf("#1F74F3"));
            Draw.alpha(0.5f + Mathf.sin(Time.time * this.getEfficiency() * 0.15f) * 0.5f);
            Draw.rect(Core.atlas.find("collos-materia-reactor-lights"), this.x, this.y);
            Draw.alpha(1.0f);
            Draw.color();
            Draw.blend();
        }

        public void updateTile() {
            if (this.items.total() == 0) this.setItem(null);
            if (this.liquids.total() == 0) this.setLiquid(null);
            if (this.items.total() == 0 && this.liquids.total() == 0) this.setWork(false);

            if (item != null && liquid != null && work) {
                int num = efficiency != 0 ? efficiency : 1;
                float time = 60f / num;

                if (this.timer.get(time)) {
                    Lightning.create(Team.get(99),
                            lightningColor,
                            Mathf.pow(3, Mathf.round(efficiency / 2f)),
                            this.x + Mathf.range(2.5f * Vars.tilesize),
                            this.y + Mathf.range(2.5f * Vars.tilesize),
                            Mathf.random(0.0f, 360.0f),
                            3 + Mathf.pow(3, Mathf.round(efficiency / 4f)));

                    this.liquids.remove(liquid, Mathf.round(Mathf.clamp(3, 0, this.liquids.total())));
                    this.items.remove(item, Mathf.round(Mathf.clamp(1, 0, this.items.total())));
                }
                ;
            }
        }

        public void onDestroyed() {
            if (!work) {
                super.onDestroyed();
                return;
            }

            effect1.at(this.x, this.y);
            effect2.at(this.x, this.y);
            effect3.at(this.x, this.y);

            for (int x = 0; x < 30; x++) {
                Lightning.create(Team.get(99),
                        destroyLightingColor,
                        Mathf.pow(4, Mathf.round(this.getEfficiency())), this.x + Mathf.range(2.5f) * Vars.tilesize,
                        this.y + Mathf.range(2.5f) * Vars.tilesize, Mathf.random(0.0f, 360.0f),
                        (35 + Mathf.range(15)));
            }
            ;

            for (int x = 0; x < 40; x++) {
                Vec2 v = new Vec2();
                v.trns(Mathf.random(360.0f), Mathf.random(100.0f));

                Tile tile = Vars.world.tileWorld(this.x + v.x, this.y + v.y);
                Puddles.deposit(tile, Vars.world.tileWorld(this.x, this.y), this.getLiquid(), this.getEfficiency() * 10);
            }
        }

        public float getPowerProduction() {
            if (item != null && liquid != null) {
                this.setWork(true);
                return item.hardness * item.cost * 10.0f * liquid.temperature * this.getEfficiency();
            } else {
                this.setWork(false);
                return 0;
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (
                    this.items == null ||
                            this.items.get(item) >= this.block.itemCapacity ||
                            this.items.total() >= this.block.itemCapacity) return false;

            if (item.hardness >= 0.5 && item.cost >= 0.5) {
                if (this.getItem() != null && this.getItem() != item) {
                    this.items.remove(this.getItem(), this.items.total());
                }
                ;
                this.setItem(item);
                return true;
            }
            return false;

        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            if (
                    this.liquids == null ||
                            this.liquids.get(liquid) >= this.block.liquidCapacity ||
                            this.liquids.total() >= this.block.liquidCapacity) return false;

            if (liquid.temperature >= 0.0 && liquid.heatCapacity >= 0.0) {
                if (this.getLiquid() != null && this.getLiquid() != liquid) {
                    this.liquids.remove(this.getLiquid(), this.liquids.total());
                }
                ;
                this.setLiquid(liquid);
                return true;
            }
            return false;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            TypeIO.writeItem(write, item);
            TypeIO.writeLiquid(write, liquid);
            write.i(efficiency);
            write.bool(work);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            item = TypeIO.readItem(read);
            liquid = TypeIO.readLiquid(read);
            efficiency = read.i();
            work = read.bool();
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public Liquid getLiquid() {
            return this.liquid;
        }

        public void setLiquid(Liquid liquid) {
            this.liquid = liquid;
        }

        public boolean getWork() {
            return this.work;
        }

        public void setWork(boolean work) {
            this.work = work;
        }

        public int getEfficiency() {
            return this.efficiency;
        }

        public void efficiency(int efficiency) {
            this.efficiency = efficiency;
        }

    }
}
