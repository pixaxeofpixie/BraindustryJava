package Gas.gen;

import Gas.entities.Clouds;
import Gas.type.Gas;
import Gas.world.GasBlock;
import Gas.world.modules.GasConsumeModule;
import Gas.world.modules.GasModule;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import braindustry.content.ModFx;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.consumers.ConsumeType;

import java.util.Iterator;

public class GasBuilding extends Building {
    public GasModule gasses;
    public GasBlock block;

    public void handleGas(Building source, Gas gas, float amount) {
        gasses.add(gas, amount);
    }

    public boolean acceptGas(Building source, Gas gas) {
        return block.hasGas && block.consumes.gasFilter.get(gas.id);
    }

    @Override
    public boolean acceptLiquid(Building source, Liquid liquid) {
        return block.hasLiquids && block.consumes.liquidfilters.get(liquid.id);
    }

    public double sense(LAccess sensor) {
        double var10000;
        switch(sensor) {
            case x:
                var10000 =  World.conv(x);
                break;
            case y:
                var10000 = World.conv(y);
                break;
            case team:
                var10000 = team.id;
                break;
            case health:
                var10000 = health;
                break;
            case maxHealth:
                var10000 = maxHealth;
                break;
            case efficiency:
                var10000 = efficiency();
                break;
            case rotation:
                var10000 = rotation;
                break;
            case totalItems:
                var10000 = items == null ? 0.0D : (double)items.total();
                break;
            case totalLiquids:
                var10000 = liquids == null ? 0.0D : (double)liquids.total();
                break;
            case totalPower:
                var10000 = power != null && block.consumes.hasPower() ? (double)(power.status * (block.consumes.getPower().buffered ? block.consumes.getPower().capacity : 1.0F)) : 0.0D;
                break;
            case itemCapacity:
                var10000 = block.hasItems ? (double)block.itemCapacity : 0.0D;
                break;
            case liquidCapacity:
                var10000 = block.hasLiquids ? (double)block.liquidCapacity : 0.0D;
                break;
            case powerCapacity:
                var10000 = block.consumes.hasPower() ? (double)block.consumes.getPower().capacity : 0.0D;
                break;
            case powerNetIn:
                var10000 = power == null ? 0.0D : (double)(power.graph.getLastScaledPowerIn() * 60.0F);
                break;
            case powerNetOut:
                var10000 = power == null ? 0.0D : (double)(power.graph.getLastScaledPowerOut() * 60.0F);
                break;
            case powerNetStored:
                var10000 = power == null ? 0.0D : (double)power.graph.getLastPowerStored();
                break;
            case powerNetCapacity:
                var10000 = power == null ? 0.0D : (double)power.graph.getLastCapacity();
                break;
            case enabled:
                var10000 = enabled ? 1.0D : 0.0D;
                break;
            case controlled:
                ControlBlock c;
                var10000 = (this instanceof ControlBlock && (c = (ControlBlock)this) == (ControlBlock)this ? (c.isControlled() ? 1 : 0) : 0);
                break;
            case payloadCount:
                var10000 = getPayload() != null ? 1.0D : 0.0D;
                break;
            default:
                var10000 = 0.0D;
        }

        return var10000;
    }
    @Override
    public boolean acceptItem(Building source, Item item) {
        return block.consumes.itemFilters.get(item.id) && items.get(item) < getMaximumAccepted(item);
    }
    @Override
    public void onDestroyed() {
        float explosiveness = block.baseExplosiveness;
        float flammability = 0.0F;
        float power = 0.0F;
        Item item;
        int amount;
        if (block.hasItems) {
            for(Iterator<Item> var4 = Vars.content.items().iterator(); var4.hasNext(); flammability += item.flammability * (float)amount) {
                item = var4.next();
                amount = items.get(item);
                explosiveness += item.explosiveness * (float)amount;
            }
        }

        if (block.hasLiquids) {
            flammability += liquids.sum((liquid, amountx) -> {
                return liquid.flammability * amountx / 2.0F;
            });
            explosiveness += liquids.sum((liquid, amountx) -> {
                return liquid.explosiveness * amountx / 2.0F;
            });
        }

        if (block.hasGas) {
            flammability += gasses.sum((gas, amountx) -> {
                return gas.flammability * amountx / 2.0F;
            });
            explosiveness += gasses.sum((gas, amountx) -> {
                return gas.explosiveness * amountx / 2.0F;
            });
        }

        if (block.consumes.hasPower() && block.consumes.getPower().buffered) {
            power += this.power.status * block.consumes.getPower().capacity;
        }

        if (block.hasLiquids && Vars.state.rules.damageExplosions) {
            liquids.each((liquid, amountx) -> {
                float splash = Mathf.clamp(amountx / 4.0F, 0.0F, 10.0F);
                for(int i = 0; (float)i < Mathf.clamp(amountx / 5.0F, 0.0F, 30.0F); ++i) {
                    Time.run((float)i / 2.0F, () -> {
                        Tile other = Vars.world.tile(tileX() + Mathf.range(block.size / 2), tileY() + Mathf.range(block.size / 2));
                        if (other != null) {
                            Puddles.deposit(other, liquid, splash);
                        }

                    });
                }

            });
        }
        if (block.hasGas && Vars.state.rules.damageExplosions) {
            gasses.each((gas, amountx) -> {
                float splash = Mathf.clamp(amountx / 4.0F, 0.0F, 10.0F);
                for(int i = 0; (float)i < Mathf.clamp(amountx / 5.0F, 0.0F, 30.0F); ++i) {
                    Time.run((float)i / 2.0F, () -> {
                        Tile other = Vars.world.tile(tileX() + Mathf.range(block.size / 2), tileY() + Mathf.range(block.size / 2));
                        if (other != null) {
                            Clouds.deposit(other, gas, splash);
                        }

                    });
                }

            });
        }

        Damage.dynamicExplosion(x, y, flammability, explosiveness * 3.5F, power, (float)(8 * block.size) / 2.0F, Vars.state.rules.damageExplosions);
        if (!floor().solid && !floor().isLiquid) {
            Effect.rubble(x, y, block.size);
        }

    }
    public float efficiency() {
        if (!enabled) {
            return 0.0F;
        } else {
            return power != null && block.consumes.has(ConsumeType.power) && !block.consumes.getPower().buffered ? power.status : 1.0F;
        }
    }

    public GasModule gasses() {
        return gasses;
    }

    public void gasses(GasModule gasses) {
        this.gasses = gasses;
    }

    public GasBuilding getGasDestination(Building from, Gas gas) {
        return this;
    }

    public boolean canDumpGas(Building to, Gas gas) {
        return true;
    }

    public void transferGas(Building n, float amount, Gas gas) {
        if (!(n instanceof GasBuilding)) return;
        GasBuilding next = (GasBuilding) n;
        float flow = Math.min(next.block.gasCapacity - next.gasses.get(gas), amount);
        if (next.acceptGas(this, gas)) {
            next.handleGas(this, gas, flow);
            gasses.remove(gas, flow);
        }
    }

    @Override
    public GasBlock block(){
        return block;
    }
    @Override
    public GasBuilding create(Block block, Team team) {
        GasBuilding out = (GasBuilding) super.create(block, team);
        this.block = (GasBlock)super.block();
        if (block().hasGas) {
            gasses = new GasModule();
        }
        cons = new GasConsumeModule(out);
        return out;
    }

    public void dumpGas(Gas gas) {
        int dump = cdump;
        if (gasses.get(gas) > 1.0E-4F) {
            if (!Vars.net.client() && Vars.state.isCampaign() && team == Vars.state.rules.defaultTeam) {
                gas.unlock();
            }

            for (int i = 0; i < proximity.size; ++i) {
                incrementDump(proximity.size);
                Building o = proximity.get((i + dump) % proximity.size);
                if (!(o instanceof GasBuilding)) continue;
                GasBuilding other = (GasBuilding) o;
                other = other.getGasDestination(this, gas);
                if (other != null && other.team == team && other.block.hasGas && canDumpGas(other, gas) && other.gasses != null) {
                    float ofract = other.gasses.get(gas) / other.block.gasCapacity;
                    float fract = gasses.get(gas) / block.gasCapacity;
                    if (ofract < fract) {
                        transferGas(other, (fract - ofract) * block.gasCapacity / 2.0F, gas);
                    }
                }
            }

        }
    }
    @Override
    public String toString() {
        return "GasBuilding#" + id;
    }

    public float moveGas(Building n, Gas gas) {
        if (n instanceof GasBuilding) {
            GasBuilding next = (GasBuilding) n;
            next = next.getGasDestination(this, gas);
            if (next.team == team && next.block.hasGas && gasses.get(gas) > 0.0F) {
                float ofract = next.gasses.get(gas) / next.block.gasCapacity;
                float fract = gasses.get(gas) / block.gasCapacity;
                float flow = Math.min(Mathf.clamp((fract - ofract) * 1.0F) * block.gasCapacity, gasses.get(gas));
                flow = Math.min(flow, next.block.gasCapacity - next.gasses.get(gas));
                if (flow > 0.0F && ofract <= fract && next.acceptGas(this, gas)) {
                    next.handleGas(this, gas, flow);
                    gasses.remove(gas, flow);
                    ModFx.debugSelectSecond.at(n.x, n.y, n.block.size, Color.green.cpy().a(0.1f));
                    return flow;
                }
                ModFx.debugSelectSecond.at(n.x, n.y, n.block.size, Color.red.cpy().a(0.1f));
                if (next.gasses.currentAmount() / next.block.gasCapacity > 0.1F && fract > 0.1F) {
                    float fx = (x + next.x) / 2.0F;
                    float fy = (y + next.y) / 2.0F;
                    Gas other = next.gasses.current();
                    if (other.flammability > 0.3F && gas.temperature > 0.7F || gas.flammability > 0.3F && other.temperature > 0.7F) {
                        damage(1.0F * Time.delta);
                        next.damage(1.0F * Time.delta);
                        if (Mathf.chance(0.1D * (double) Time.delta)) {
                            Fx.fire.at(fx, fy);
                        }
                    } else if (gas.temperature > 0.7F && other.temperature < 0.55F || other.temperature > 0.7F && gas.temperature < 0.55F) {
                        gasses.remove(gas, Math.min(gasses.get(gas), 0.7F * Time.delta));
                        if (Mathf.chance((double) (0.2F * Time.delta))) {
                            Fx.steam.at(fx, fy);
                        }
                    }
                }
            }

        }
        return 0.0F;
    }

    @Override
    public void updateTile() {
    }

    public float moveGasForward(boolean leaks, Gas gas) {
        Building next = front();
        Tile nextTile = tile.nearby(rotation);
        if ((next instanceof GasBuilding)) {
            return moveGas(next, gas);
        }
        if (nextTile != null && leaks && !nextTile.block().solid && !nextTile.block().hasLiquids) {
            float leakAmount = gasses.get(gas) / 1.5F;

            Clouds.deposit(nextTile, tile, gas, leakAmount);
            gasses.remove(gas, leakAmount);
        }
        return 0.0F;
    }
}
