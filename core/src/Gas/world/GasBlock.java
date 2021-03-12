package Gas.world;

import Gas.GasStats;
import Gas.content.Gasses;
import Gas.gen.GasBuilding;
import Gas.gen.GasContentRegions;
import Gas.type.Gas;
import Gas.world.consumers.ConsumeGasses;
import Gas.world.consumers.GasConsumers;
import arc.Core;
import arc.func.Cons;
import arc.func.Func;
import arc.graphics.Color;
import arc.math.Mathf;
import braindustry.world.meta.AStat;
import braindustry.world.meta.AStats;
import mindustry.content.Liquids;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.values.ItemListValue;

import static mindustry.Vars.tilesize;

public class GasBlock extends Block {
    public final GasConsumers consumes = new GasConsumers();
    public boolean hasGas = false;
    public float gasCapacity;
    public boolean outputsGas = false;
    public AStats aStats = new AStats();

    //    4a4b53
    public GasBlock(String name) {
        super(name);
        super.stats=aStats.copy(stats);
        this.gasCapacity = 10;
    }

    public void getDependencies(Cons<UnlockableContent> cons) {
        for (ItemStack stack : this.requirements) {
            cons.get(stack.item);
        }

        this.consumes.each((c) -> {
            if (!c.isOptional()) {
                ConsumeItems i;
                if (c instanceof ConsumeItems && (i = (ConsumeItems) c) == c) {
                    ItemStack[] var4 = i.items;
                    int var5 = var4.length;

                    for (int var6 = 0; var6 < var5; ++var6) {
                        ItemStack stack = var4[var6];
                        cons.get(stack.item);
                    }
                } else {
                    ConsumeLiquid ix;
                    ConsumeGasses ig;
                    if (c instanceof ConsumeLiquid && (ix = (ConsumeLiquid) c) == c) {
                        cons.get(ix.liquid);
                    } else if (c instanceof ConsumeGasses && (ig = (ConsumeGasses) c) == c) {
                        cons.get(ig.gas);
                    }
                }

            }
        });
    }

    @Override
    public void init() {
        for (ConsumeType value : ConsumeType.values()) {
            if (consumes.has(value)) {
                super.consumes.add(consumes.get(value));
            }
        }
        if(health == -1){
            health = size * size * 40;
        }

        if(group == BlockGroup.transportation || consumes.has(ConsumeType.item) || category == Category.distribution){
            acceptsItems = true;
        }

        offset = ((size + 1) % 2) * tilesize / 2f;

        buildCost = 0f;
        for(ItemStack stack : requirements){
            buildCost += stack.amount * stack.item.cost;
        }
        buildCost *= buildCostMultiplier;

        if(consumes.has(ConsumeType.power)) hasPower = true;
        if(consumes.has(ConsumeType.item)) hasItems = true;
        if(consumes.has(ConsumeType.liquid)) hasLiquids = true;
        if(consumes.hasGas()) hasGas = true;

        setBars();

        stats.useCategories = true;

        consumes.init();
        super.consumes.init();

        if(!logicConfigurable){
            configurations.each((key, val) -> {
                if(UnlockableContent.class.isAssignableFrom(key)){
                    logicConfigurable = true;
                }
            });
        }

        if(!outputsPower && consumes.hasPower() && consumes.getPower().buffered){
            throw new IllegalArgumentException("Consumer using buffered power: " + name);
        }
    }

    public void setStats() {
//        super.setStats();
        aStats.add(Stat.size, "@x@", size, size);
        aStats.add(Stat.health, (float) health, StatUnit.none);
        if (canBeBuilt()) {
            aStats.add(Stat.buildTime, buildCost / 60.0F, StatUnit.seconds);
            aStats.add(Stat.buildCost, new ItemListValue(false, requirements));
        }

        if (instantTransfer) {
            aStats.add(Stat.maxConsecutive, 2.0F, StatUnit.none);
        }

        consumes.display(aStats);
        if (hasLiquids) {
            aStats.add(Stat.liquidCapacity, liquidCapacity, StatUnit.liquidUnits);
        }

        if (hasItems && itemCapacity > 0) {
            aStats.add(Stat.itemCapacity, (float) itemCapacity, StatUnit.items);
        }
        if (hasGas && gasCapacity > 0) {
            aStats.add(AStat.gasCapacity, gasCapacity, StatUnit.liquidUnits);
        }

    }

    @Override
    public void load() {
        super.load();
        GasContentRegions.loadRegions(this);
    }

    @Override
    public void setBars() {
        bars.add("health", (entity) -> {
            return (new Bar("stat.health", Pal.health, entity::healthf)).blink(Color.white);
        });
        if (hasLiquids) {
            Func<Building, Liquid> current;
            if (consumes.has(ConsumeType.liquid) && consumes.get(ConsumeType.liquid) instanceof ConsumeLiquid) {
                Liquid liquid = ((ConsumeLiquid) consumes.get(ConsumeType.liquid)).liquid;
                current = (entity) -> {
                    return liquid;
                };
            } else {
                current = (entity) -> {
                    return entity.liquids == null ? Liquids.water : entity.liquids.current();
                };
            }

            bars.add("liquid", (entity) -> {
                return new Bar(() -> {
                    return entity.liquids.get((Liquid) current.get(entity)) <= 0.001F ? Core.bundle.get("bar.liquid") : ((Liquid) current.get(entity)).localizedName;
                }, () -> {
                    return ((Liquid) current.get(entity)).barColor();
                }, () -> {
                    return entity != null && entity.liquids != null ? entity.liquids.get((Liquid) current.get(entity)) / this.liquidCapacity : 0.0F;
                });
            });
        }

        if (hasPower && consumes.hasPower()) {
            ConsumePower cons = consumes.getPower();
            boolean buffered = cons.buffered;
            float capacity = cons.capacity;
            bars.add("power", (entity) -> {
                return new Bar(() -> {
                    return buffered ? Core.bundle.format("bar.poweramount", Float.isNaN(entity.power.status * capacity) ? "<ERROR>" : (int) (entity.power.status * capacity)) : Core.bundle.get("bar.power");
                }, () -> {
                    return Pal.powerBar;
                }, () -> {
                    return Mathf.zero(cons.requestedPower(entity)) && entity.power.graph.getPowerProduced() + entity.power.graph.getBatteryStored() > 0.0F ? 1.0F : entity.power.status;
                });
            });
        }

        if (hasItems && configurable) {
            bars.add("items", (entity) -> {
                return new Bar(() -> {
                    return Core.bundle.format("bar.items", entity.items.total());
                }, () -> {
                    return Pal.items;
                }, () -> {
                    return (float) entity.items.total() / (float) itemCapacity;
                });
            });
        }
        if (hasGas) {
            Func<GasBuilding, Gas> current;
            if (consumes.hasGas() && this.consumes.getGas() instanceof ConsumeGasses) {
                Gas gas = ((ConsumeGasses) this.consumes.getGas()).gas;
                current = (entity) -> {
                    return gas;
                };
            } else {
                current = (entity) -> {
                    return entity.gasses == null ? Gasses.oxygen : entity.gasses.current();
                };
            }

            bars.<GasBuilding>add("gas", (entity) -> {
                return new Bar(() -> {
                    return entity.gasses.get(current.get(entity)) <= 0.001F ? Core.bundle.get("bar.gas") : (current.get(entity)).localizedName;
                }, () -> {
                    return (current.get(entity)).barColor();
                }, () -> {
                    return entity != null && entity.gasses != null ? entity.gasses.get(current.get(entity)) / this.gasCapacity : 0.0F;
                });
            });
        }
    }
}
