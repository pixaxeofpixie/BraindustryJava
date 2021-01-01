package Main.ModContent.world.blocks.defense;

import mindustry.world.blocks.defense.turrets.LaserTurret;

public class LaserHeatTurret extends LaserTurret {
    public LaserHeatTurret(String name) {
        super(name);
    }
    public class LaserHeatTurretBuild extends LaserTurret.LaserTurretBuild{
        protected float baseReloadSpeed() {
            return this.liquids.currentAmount()>0? this.efficiency():0;
        }
    }
}
