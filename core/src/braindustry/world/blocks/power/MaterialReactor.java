package braindustry.world.blocks.power;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.content.ModFx;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Puddles;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Tex;
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
    public int maxEfficiency = 10;
    public float lightningRange = 2.5f;
    public @ModAnnotations.Load("@-lights")
    TextureRegion lightsRegion;
    public Color lightsColor = Color.valueOf("#1F74F3");

    public MaterialReactor(boolean hasItems, boolean hasLiquids, String name) {
        super(hasItems, hasLiquids, name);
        effect1 = ModFx.electricExplosionPart1;
        effect2 = ModFx.electricExplosionPart1;
        effect3 = ModFx.electricExplosionPart1;
        configurable = true;
        outputsPower = true;
    }

    public MaterialReactor(String name) {
        this(false, false, name);
    }

    @Override
    public void setBars() {
        super.setBars();
        bars.<MaterialReactorBuild>add("efficiency", e -> {
                    return new Bar(
                            () -> Core.bundle.get("efficiency") + ": " + e.efficiency,
                            () -> Tmp.c1.set(Color.valueOf("#FFE679")).lerp(Color.valueOf("#78FFFD"), Mathf.sin(Time.time * 0.03f) * e.realEfficiency / 2f + 0.5f),
                            e::realEfficiencyf
                    );
                }

        );
    }

    public class MaterialReactorBuild extends ItemLiquidGenerator.ItemLiquidGeneratorBuild {
        //        public boolean work;
        public Item item;
        public Liquid liquid;
        public int efficiency;
        public float realEfficiency = -1;

        public float realEfficiencyf() {
            return realEfficiency / (float) maxEfficiency;
        }

        public float efficiencyf() {
            return (float) efficiency / (float) maxEfficiency;
        }

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            Building building = super.init(tile, team, shouldAdd, rotation);

            item = null;
            liquid = null;
//            work=false;
            efficiency = 1;

            return building;
        }

        public void buildConfiguration(Table table) {
            table.table(Tex.button, t -> {
                t.slider(0, (float) maxEfficiency, 1, efficiency, (value) -> {
                    efficiency = (Mathf.round(value, 1));
                }).width(240).color(Color.valueOf("FFFFFF"));
            });
        }

        public void draw() {
            super.draw();

            if (realEfficiency>0f) {
                Draw.blend(Blending.additive);
                Draw.color(lightsColor);
                if (realEfficiency > 1.0f) {
                    Draw.alpha(0.5f + Mathf.sin(Time.time * 0.5f) * 0.5f * (realEfficiencyf()));
                } else {
                    Draw.alpha(realEfficiency);
                }
                Draw.rect(lightsRegion, x, y);
                Draw.alpha(1.0f);
                Draw.color();
                Draw.blend();
            }
        }

        public void updateTile() {
            if (!items.any()) item = null;
            if (this.liquids.total() == 0) liquid = null;
//            if (this.items.total() == 0 && liquids.total() == 0) work=false;
            if (realEfficiency >= 0.1f) {
                realEfficiency = Mathf.lerpDelta(realEfficiency, efficiency, 0.01f);
            } else {
                realEfficiency = Mathf.approachDelta(realEfficiency, efficiency, 0.01f);
            }
            int efficiency = (int) realEfficiency;
            if (realEfficiency > this.efficiency) efficiency++;
            if (item != null && liquid != null) {
                int num = efficiency != 0 ? efficiency : 1;
                float time = 60f / num;

                if (timer.get(time)) {
                    Lightning.create(Team.get(99),
                            lightningColor,
                            Mathf.pow(3, Mathf.round(efficiency / 2f)),
                            x + Mathf.range(lightningRange * Vars.tilesize),
                            y + Mathf.range(lightningRange * Vars.tilesize),
                            Mathf.random(0.0f, 360.0f),
                            3 + Mathf.pow(3, Mathf.round(efficiency / 4f)));

                    liquids.remove(liquid, Mathf.round(Mathf.clamp(3, 0, liquids.total())));
                    items.remove(item, Mathf.round(Mathf.clamp(1, 0, items.total())));
                }
                ;
            }
        }

        @Override
        public byte version() {
            return 2;
        }

        public void onDestroyed() {
            if (item == null || liquid == null) {
                super.onDestroyed();
                return;
            }

            effect1.at(this.x, this.y);
            effect2.at(this.x, this.y);
            effect3.at(this.x, this.y);

            for (int x = 0; x < 30; x++) {
                Lightning.create(Team.get(99),
                        destroyLightingColor,
                        Mathf.pow(4, Mathf.round(this.efficiency)), this.x + Mathf.range(lightningRange) * Vars.tilesize,
                        this.y + Mathf.range(lightningRange) * Vars.tilesize, Mathf.random(0.0f, 360.0f),
                        (35 + Mathf.range(15)));
            }
            ;

            for (int x = 0; x < 40; x++) {
                Vec2 v = new Vec2();
                v.trns(Mathf.random(360.0f), Mathf.random(100.0f));

                Tile tile = Vars.world.tileWorld(this.x + v.x, this.y + v.y);
                Puddles.deposit(tile, Vars.world.tileWorld(this.x, this.y), this.liquid, this.efficiency * 10);
            }
        }

        public float getPowerProduction() {
            return (item != null && liquid != null) ? item.hardness * item.cost * 10.0f * liquid.temperature * this.efficiency : 0;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            if (
                    this.items == null ||
                            this.items.get(item) >= this.block.itemCapacity ||
                            this.items.total() >= this.block.itemCapacity) return false;

            if (item.hardness >= 0.5 && item.cost >= 0.5) {
                if (this.item != null && this.item != item) {
                    this.items.remove(this.item, this.items.total());
                }
                this.item = item;
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
                if (this.liquid != null && this.liquid != liquid) {
                    this.liquids.remove(this.liquid, this.liquids.total());
                }
                ;
                this.liquid = liquid;
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
//            write.bool(work);
            write.f(realEfficiency);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            item = TypeIO.readItem(read);
            liquid = TypeIO.readLiquid(read);
            realEfficiency = efficiency = read.i();
            if (revision<2) {
                read.bool();
            }
            if (revision > 0) {
                realEfficiency = read.f();
            }
        }


    }
}
