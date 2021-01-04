package Gas;

import Gas.type.Gas;
import Gas.world.GasBlock;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.Tile;

public class GasBuilding extends Building {
    public GasModule gasses;
    public GasBlock block;
    public void handleGas(Building source, Gas gas, float amount) {
        this.gasses.add(gas, amount);
    }
    public boolean acceptGas(Building source, Gas gas) {
        return this.block.hasGas && this.block.consumes.gasFilter.get(gas.id);
    }

    public GasModule gasses() {
        return this.gasses;
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
        if (!(n instanceof GasBuilding))return;
        GasBuilding next=(GasBuilding)n;
        float flow = Math.min(next.block.gasCapacity - next.gasses.get(gas), amount);
        if (next.acceptGas(this, gas)) {
            next.handleGas(this, gas, flow);
            this.gasses.remove(gas, flow);
        }
    }

    @Override
    public GasBuilding create(Block block, Team team) {
        GasBuilding out= (GasBuilding) super.create(block, team);
        this.block=(GasBlock) this.block();
        if (this.block.hasGas) {
            this.gasses = new GasModule();
        }
        return out;
    }

    public void dumpGas(Gas gas) {
        int dump = this.cdump;
        if (this.gasses.get(gas) > 1.0E-4F) {
            if (!Vars.net.client() && Vars.state.isCampaign() && this.team == Vars.state.rules.defaultTeam) {
                gas.unlock();
            }

            for(int i = 0; i < this.proximity.size; ++i) {
                this.incrementDump(this.proximity.size);
                Building o = this.proximity.get((i + dump) % this.proximity.size);
                if (!(o instanceof GasBuilding))continue;
                GasBuilding other=(GasBuilding)o;
                other = other.getGasDestination(this, gas);
                if (other != null && other.team == this.team && other.block.hasGas && this.canDumpGas(other, gas) && other.gasses != null) {
                    float ofract = other.gasses.get(gas) / other.block.gasCapacity;
                    float fract = this.gasses.get(gas) / this.block.gasCapacity;
                    if (ofract < fract) {
                        this.transferGas(other, (fract - ofract) * this.block.gasCapacity / 2.0F, gas);
                    }
                }
            }

        }
    }
    public float moveGas(Building n, Gas gas) {
        if (n == null || !(n instanceof GasBuilding)) {
            return 0.0F;
        } else {
            GasBuilding next=(GasBuilding)n;
            next = next.getGasDestination(this, gas);
            if (next.team == this.team && next.block.hasGas && this.gasses.get(gas) > 0.0F) {
                float ofract = next.gasses.get(gas) / next.block.gasCapacity;
                float fract = this.gasses.get(gas) / this.block.gasCapacity;
                float flow = Math.min(Mathf.clamp((fract - ofract) * 1.0F) * this.block.gasCapacity, this.gasses.get(gas));
                flow = Math.min(flow, next.block.gasCapacity - next.gasses.get(gas));
                if (flow > 0.0F && ofract <= fract && next.acceptGas(this, gas)) {
                    next.handleGas(this, gas, flow);
                    this.gasses.remove(gas, flow);
                    return flow;
                }

                if (next.gasses.currentAmount() / next.block.gasCapacity > 0.1F && fract > 0.1F) {
                    float fx = (this.x + next.x) / 2.0F;
                    float fy = (this.y + next.y) / 2.0F;
                    Gas other = next.gasses.current();
                    if (other.flammability > 0.3F && gas.temperature > 0.7F || gas.flammability > 0.3F && other.temperature > 0.7F) {
                        this.damage(1.0F * Time.delta);
                        next.damage(1.0F * Time.delta);
                        if (Mathf.chance(0.1D * (double)Time.delta)) {
                            Fx.fire.at(fx, fy);
                        }
                    } else if (gas.temperature > 0.7F && other.temperature < 0.55F || other.temperature > 0.7F && gas.temperature < 0.55F) {
                        this.gasses.remove(gas, Math.min(this.gasses.get(gas), 0.7F * Time.delta));
                        if (Mathf.chance((double)(0.2F * Time.delta))) {
                            Fx.steam.at(fx, fy);
                        }
                    }
                }
            }

            return 0.0F;
        }
    }
    public float moveGasForward(boolean leaks, Gas gas) {
        Tile next = this.tile.nearby(this.rotation);
        if (next == null) {
            return 0.0F;
        } else if (next.build != null) {
            return this.moveGas(next.build, gas);
        } else {
            if (leaks && !next.block().solid && !next.block().hasLiquids) {
                float leakAmount = this.gasses.get(gas) / 1.5F;
//                Puddles.deposit(next, this.tile, gas, leakAmount);
                this.gasses.remove(gas, leakAmount);
            }

            return 0.0F;
        }
    }
}
