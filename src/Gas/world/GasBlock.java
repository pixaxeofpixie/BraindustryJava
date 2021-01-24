package Gas.world;

import Gas.content.Gasses;
import Gas.GasBuilding;
import Gas.type.Gas;
import Gas.world.consumers.ConsumeGasses;
import Gas.world.consumers.GasConsumers;
import Gas.GasStats;
import arc.Core;
import arc.func.Cons;
import arc.func.Func;
import arc.graphics.Color;
import arc.math.Mathf;
import mindustry.content.Liquids;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.values.ItemListValue;

import java.util.Objects;

public class GasBlock extends Block {
    public boolean hasGas=false;
    public float gasCapacity ;
    public boolean outputsGas = false;
    public final GasConsumers consumes = new GasConsumers();
    public GasStats stats = new GasStats();

//    4a4b53
    public GasBlock(String name) {
        super(name);
        this.gasCapacity =10;
    }
    public void getDependencies(Cons<UnlockableContent> cons) {
        for (ItemStack stack : this.requirements) {
            cons.get(stack.item);
        }

        this.consumes.each((c) -> {
            if (!c.isOptional()) {
                ConsumeItems i;
                if (c instanceof ConsumeItems && (i = (ConsumeItems)c) == c) {
                    ItemStack[] var4 = i.items;
                    int var5 = var4.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        ItemStack stack = var4[var6];
                        cons.get(stack.item);
                    }
                } else {
                    ConsumeLiquid ix;
                    ConsumeGasses ig;
                    if (c instanceof ConsumeLiquid && (ix = (ConsumeLiquid)c) == c) {
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
        super.init();
        this.consumes.init();
    }

    public void setStats() {
//        super.setStats();
        this.stats.add(Stat.size, "@x@", this.size, this.size);
        this.stats.add(Stat.health, (float)this.health, StatUnit.none);
        if (this.canBeBuilt()) {
            this.stats.add(Stat.buildTime, this.buildCost / 60.0F, StatUnit.seconds);
            this.stats.add(Stat.buildCost, new ItemListValue(false, this.requirements));
        }

        if (this.instantTransfer) {
            this.stats.add(Stat.maxConsecutive, 2.0F, StatUnit.none);
        }

        this.consumes.display(this.stats);
        if (this.hasLiquids) {
            this.stats.add(Stat.liquidCapacity, this.liquidCapacity, StatUnit.liquidUnits);
        }

        if (this.hasItems && this.itemCapacity > 0) {
            this.stats.add(Stat.itemCapacity, (float)this.itemCapacity, StatUnit.items);
        }

    }
    @Override
    public void setBars() {
        this.bars.add("health", (entity) -> {
            return (new Bar("stat.health", Pal.health, entity::healthf)).blink(Color.white);
        });
        if (this.hasLiquids) {
            Func<Building,Liquid> current;
            if (this.consumes.has(ConsumeType.liquid) && this.consumes.get(ConsumeType.liquid) instanceof ConsumeLiquid) {
                Liquid liquid = ((ConsumeLiquid)this.consumes.get(ConsumeType.liquid)).liquid;
                current = (entity) -> {
                    return liquid;
                };
            } else {
                current = (entity) -> {
                    return entity.liquids == null ? Liquids.water : entity.liquids.current();
                };
            }

            this.bars.add("liquid", (entity) -> {
                return new Bar(() -> {
                    return entity.liquids.get((Liquid)current.get(entity)) <= 0.001F ? Core.bundle.get("bar.liquid") : ((Liquid)current.get(entity)).localizedName;
                }, () -> {
                    return ((Liquid)current.get(entity)).barColor();
                }, () -> {
                    return entity != null && entity.liquids != null ? entity.liquids.get((Liquid)current.get(entity)) / this.liquidCapacity : 0.0F;
                });
            });
        }

        if (hasPower && consumes.hasPower()) {
            ConsumePower cons = consumes.getPower();
            boolean buffered = cons.buffered;
            float capacity = cons.capacity;
            bars.add("power", (entity) -> {
                return new Bar(() -> {
                    return buffered ? Core.bundle.format("bar.poweramount", Float.isNaN(entity.power.status * capacity) ? "<ERROR>" : (int)(entity.power.status * capacity)) : Core.bundle.get("bar.power");
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
                    return Core.bundle.format("bar.items", new Object[]{entity.items.total()});
                }, () -> {
                    return Pal.items;
                }, () -> {
                    return (float)entity.items.total() / (float)this.itemCapacity;
                });
            });
        }
        if (this.hasGas) {
            Func<GasBuilding, Gas> current;
            if (consumes.hasGas() && this.consumes.getGas() instanceof ConsumeGasses) {
                Gas gas = ((ConsumeGasses)this.consumes.getGas()).gas;
                current = (entity) -> {
                    return gas;
                };
            } else {
                current = (entity) -> {
                    return entity.gasses == null ? Gasses.oxygen : entity.gasses.current();
                };
            }

            bars.add("gas", (e) -> {
                GasBuilding entity=(GasBuilding)e;
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
