package Gas;

import Gas.type.Gas;
import Gas.world.GasPowerGenerator;
import Gas.world.consumers.ConsumeGasFilter;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.ctype.ContentType;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.blocks.power.ItemLiquidGenerator;
import mindustry.world.consumers.ConsumeItemFilter;
import mindustry.world.consumers.ConsumeLiquidFilter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class AllGenerator extends GasPowerGenerator {
    public float minItemEfficiency;
    public float itemDuration;
    public float minLiquidEfficiency;
    public float maxLiquidGenerate;
    public float minGasEfficiency;
    public float maxGasGenerate;
    public Effect generateEffect;
    public Effect explodeEffect;
    public Color heatColor;
    public TextureRegion topRegion;
    public TextureRegion liquidRegion;
    public TextureRegion gasRegion;
    public boolean randomlyExplode;
    public boolean defaults;

    public AllGenerator(boolean hasItems, boolean hasLiquids, boolean hasGas, String name) {
        this(name);
        this.update = true;
        this.hasItems = hasItems;
        this.hasLiquids = hasLiquids;
        this.hasGas = hasGas;
        this.setDefaults();
    }

    public AllGenerator(String name) {
        super(name);
        this.minItemEfficiency = 0.2F;
        this.itemDuration = 70.0F;
        this.minLiquidEfficiency = 0.2F;
        this.minGasEfficiency = 0.2F;
        this.maxLiquidGenerate = 0.4F;
        this.maxGasGenerate = 0.4F;
        this.generateEffect = Fx.generatespark;
        this.explodeEffect = Fx.generatespark;
        this.heatColor = Color.valueOf("ff9b59");
        this.randomlyExplode = true;
        this.setDefaults();
    }

    @Override
    public void load() {
        super.load();
        this.gasRegion = Core.atlas.find(this.name+"-gas");
    }

    protected void setDefaults() {
        if (defaults) return;
        if (this.hasItems) {
            (this.consumes.add(new ConsumeItemFilter((item) -> {
                return this.getItemEfficiency(item) >= this.minItemEfficiency;
            }))).update(false).optional(true, false);
        }

        if (this.hasLiquids) {
            (this.consumes.add(new ConsumeLiquidFilter((liquid) -> {
                return this.getLiquidEfficiency(liquid) >= this.minLiquidEfficiency;
            }, this.maxLiquidGenerate))).update(false).optional(true, false);
        }

        if (this.hasGas) {
            this.consumes.addGas(new ConsumeGasFilter((gas) -> {
                return this.getGasEfficiency(gas) >= this.minGasEfficiency;
            }, this.maxLiquidGenerate)).update(false).optional(true, false);
        }

        this.defaults = true;
    }

    public void init() {
        this.setDefaults();

        super.init();
    }

    public void setStats() {
        super.setStats();
        if (this.hasItems) {
            this.stats.add(Stat.productionTime, this.itemDuration / 60.0F, StatUnit.seconds);
        }

    }

    protected float getItemEfficiency(Item item) {
        return 0.0F;
    }

    protected float getLiquidEfficiency(Liquid liquid) {
        return 0.0F;
    }

    protected float getGasEfficiency(Gas gas) {
        return 0.0F;
    }

    public class AllGeneratorBuild extends GasPowerGenerator.GasGeneratorBuild {
        public float explosiveness;
        public float heat;
        public float totalTime;

        public AllGeneratorBuild() {
            super();
        }

        public boolean productionValid() {
            return this.generateTime > 0.0F;
        }

        public void updateTile() {
            float calculationDelta = this.delta();
            this.heat = Mathf.lerpDelta(this.heat, this.generateTime >= 0.001F ? 1.0F : 0.0F, 0.05F);
            if (!this.consValid()) {
                this.productionEfficiency = 0.0F;
            } else {
                Gas gas = null;

                Liquid liquid = null;

                for (Liquid other : Vars.content.liquids()) {
                    if (AllGenerator.this.hasLiquids && this.liquids.get(other) >= 0.001F && AllGenerator.this.getLiquidEfficiency(other) >= AllGenerator.this.minLiquidEfficiency) {
                        liquid = other;
                        break;
                    }
                }

                for (Gas other : Vars.content.<Gas>getBy(ContentType.typeid_UNUSED)) {
                    if (AllGenerator.this.hasGas && this.gasses.get(other) >= 0.001F && AllGenerator.this.getGasEfficiency(other) >= AllGenerator.this.minGasEfficiency) {
                        gas = other;
                        break;
                    }
                }

                this.totalTime += this.heat * Time.delta;
                if (AllGenerator.this.hasGas && gas != null && this.gasses.get(gas) >= 0.001F) {
                    float baseLiquidEfficiency = AllGenerator.this.getGasEfficiency(gas);
                    float maximumPossible = AllGenerator.this.maxGasGenerate * calculationDelta;
                    float used = Math.min(this.gasses.get(gas) * calculationDelta, maximumPossible);
                    this.gasses.remove(gas, used * this.power.graph.getUsageFraction());
                    this.productionEfficiency = baseLiquidEfficiency * used / maximumPossible;
                    if (used > 0.001F && Mathf.chance(0.05D * (double) this.delta())) {
                        AllGenerator.this.generateEffect.at(this.x + Mathf.range(3.0F), this.y + Mathf.range(3.0F));
                    }
                } else if (AllGenerator.this.hasLiquids && liquid != null && this.liquids.get(liquid) >= 0.001F) {
                    float baseLiquidEfficiency = AllGenerator.this.getLiquidEfficiency(liquid);
                    float maximumPossible = AllGenerator.this.maxLiquidGenerate * calculationDelta;
                    float used = Math.min(this.liquids.get(liquid) * calculationDelta, maximumPossible);
                    this.liquids.remove(liquid, used * this.power.graph.getUsageFraction());
                    this.productionEfficiency = baseLiquidEfficiency * used / maximumPossible;
                    if (used > 0.001F && Mathf.chance(0.05D * (double) this.delta())) {
                        AllGenerator.this.generateEffect.at(this.x + Mathf.range(3.0F), this.y + Mathf.range(3.0F));
                    }
                } else if (AllGenerator.this.hasItems) {
                    if (this.generateTime <= 0.0F && this.items.total() > 0) {
                        AllGenerator.this.generateEffect.at(this.x + Mathf.range(3.0F), this.y + Mathf.range(3.0F));
                        Item item = this.items.take();
                        this.productionEfficiency = AllGenerator.this.getItemEfficiency(item);
                        this.explosiveness = item.explosiveness;
                        this.generateTime = 1.0F;
                    }

                    if (this.generateTime > 0.0F) {
                        this.generateTime -= Math.min(1.0F / AllGenerator.this.itemDuration * this.delta() * this.power.graph.getUsageFraction(), this.generateTime);
                        if (AllGenerator.this.randomlyExplode && Vars.state.rules.reactorExplosions && Mathf.chance((double) this.delta() * 0.06D * (double) Mathf.clamp(this.explosiveness - 0.5F))) {
                            Core.app.post(() -> {
                                this.damage(Mathf.random(11.0F));
                                AllGenerator.this.explodeEffect.at(this.x + Mathf.range((float) (AllGenerator.this.size * 8) / 2.0F), this.y + Mathf.range((float) (AllGenerator.this.size * 8) / 2.0F));
                            });
                        }
                    } else {
                        this.productionEfficiency = 0.0F;
                    }
                }
            }
        }


        public void drawLight() {
            Drawf.light(this.team, this.x, this.y, (60.0F + Mathf.absin(10.0F, 5.0F)) * (float) AllGenerator.this.size, Color.orange, 0.5F * this.heat);
        }

        public void draw() {
            super.draw();
            if (AllGenerator.this.hasItems) {
                Draw.color(AllGenerator.this.heatColor);
                Draw.alpha(this.heat * 0.4F + Mathf.absin(Time.time, 8.0F, 0.6F) * this.heat);
                Draw.rect(AllGenerator.this.topRegion, this.x, this.y);
                Draw.reset();
            }

            if (AllGenerator.this.hasLiquids) {
                Drawf.liquid(AllGenerator.this.liquidRegion, this.x, this.y, this.liquids.total() / AllGenerator.this.liquidCapacity, this.liquids.current().color);
            }

            if (AllGenerator.this.hasGas) {
                Drawf.liquid(AllGenerator.this.gasRegion, this.x, this.y, this.gasses.total() / AllGenerator.this.gasCapacity, this.gasses.current().color);
            }
        }
    }
}