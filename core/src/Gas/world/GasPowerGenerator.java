package Gas.world;

import Gas.GasBuilding;
import arc.Core;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class GasPowerGenerator extends GasDistributor {
    public float powerProduction;
    public Stat generationType;

    public GasPowerGenerator(String name) {
        super(name);
        this.generationType = Stat.basePowerGeneration;
        this.sync = true;
        this.baseExplosiveness = 5.0F;
        this.flags = EnumSet.of(new BlockFlag[]{BlockFlag.generator});
    }

    public void setStats() {
        super.setStats();
        this.stats.add(this.generationType, this.powerProduction * 60.0F, StatUnit.powerSecond);
    }

    public void setBars() {
        super.setBars();
        if (this.hasPower && this.outputsPower && !this.consumes.hasPower()) {
            this.bars.add("power", (entity) -> {
                return new Bar(() -> {
                    return Core.bundle.format("bar.poweroutput", new Object[]{Strings.fixed(entity.getPowerProduction() * 60.0F * entity.timeScale(), 1)});
                }, () -> {
                    return Pal.powerBar;
                }, entity::getPowerProduction);
            });
        }

    }

    public boolean outputsItems() {
        return false;
    }

    public class GasGeneratorBuild extends GasBuilding {
        public float generateTime;
        public float productionEfficiency = 0.0F;

        public GasGeneratorBuild() {
        }

        public float ambientVolume() {
            return Mathf.clamp(this.productionEfficiency);
        }

        public float getPowerProduction() {
            return GasPowerGenerator.this.powerProduction * this.productionEfficiency;
        }

        public void write(Writes write) {
            super.write(write);
            write.f(this.productionEfficiency);
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            this.productionEfficiency = read.f();
        }
    }
}
