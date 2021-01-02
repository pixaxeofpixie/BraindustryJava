package braindustry.ModContent.world.blocks.defense;

import braindustry.ModContent.world.blocks.BuildingLabel;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.consumers.ConsumeLiquidBase;
import mindustry.world.consumers.ConsumeType;

public class PowerHeatTurret extends PowerTurret {
    public float shootHeat = 1f;

    public PowerHeatTurret(String name) {
        super(name);
    }

    public class PowerHeatTurretBuild extends PowerTurret.PowerTurretBuild implements BuildingLabel {
        protected void updateCooling() {
            if (reload >= PowerHeatTurret.this.reloadTime) {
                return;
            }
            float maxUsed = ((ConsumeLiquidBase) PowerHeatTurret.this.consumes.get(ConsumeType.liquid)).amount;
            Liquid liquid = this.liquids.current();
            float used = Math.min(Math.min(this.liquids.get(liquid), maxUsed * Time.delta), Math.max(0.0F, (PowerHeatTurret.this.reloadTime - this.reload) / PowerHeatTurret.this.coolantMultiplier / liquid.heatCapacity)) * this.baseReloadSpeed();
            this.reload += used * liquid.heatCapacity * PowerHeatTurret.this.coolantMultiplier;

            this.liquids.remove(liquid, used);
            if (Mathf.chance(0.06D * (double) used)) {
                PowerHeatTurret.this.coolEffect.at(this.x + Mathf.range((float) (PowerHeatTurret.this.size * 8) / 2.0F), this.y + Mathf.range((float) (PowerHeatTurret.this.size * 8) / 2.0F));
            }

        }

        @Override
        protected float baseReloadSpeed() {
            return Mathf.round(liquids.currentAmount(), 0.1f) > 0.1f ? this.efficiency() : 0;
        }

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            Building ret = super.init(tile, team, shouldAdd, rotation);
            return ret;
        }

        private boolean labels = false;

        public void updateTile() {
            if (!this.validateTarget()) {
                this.target = null;
            }

            this.wasShooting = false;
            this.recoil = Mathf.lerpDelta(this.recoil, 0.0F, PowerHeatTurret.this.restitution);
            this.heat = getNewHeat();
            this.unit.health(this.health);
            this.unit.rotation(this.rotation);
            this.unit.team(this.team);
            this.unit.set(this.x, this.y);
            if (this.logicControlTime > 0.0F) {
                this.logicControlTime -= Time.delta;
            }

            if (this.hasAmmo()) {
                if (this.timer(PowerHeatTurret.this.timerTarget, (float) PowerHeatTurret.this.targetInterval)) {
                    this.findTarget();
                }

                if (this.validateTarget()) {
                    boolean canShoot = true;
                    if (this.isControlled()) {
                        this.targetPos.set(this.unit.aimX(), this.unit.aimY());
                        canShoot = this.unit.isShooting();
                    } else if (this.logicControlled()) {
                        canShoot = this.logicShooting;
                    } else {
                        this.targetPosition(this.target);
                        if (Float.isNaN(this.rotation)) {
                            this.rotation = 0.0F;
                        }
                    }

                    float targetRot = this.angleTo(this.targetPos);
                    if (this.shouldTurn()) {
                        this.turnToTarget(targetRot);
                    }

                    if (Angles.angleDist(this.rotation, targetRot) < PowerHeatTurret.this.shootCone && canShoot) {
                        this.wasShooting = true;
                        this.updateShooting();
                    }
                }
            }

            if (PowerHeatTurret.this.acceptCoolant) {
                this.updateCooling();
            }

        }

        private float getNewHeat() {
            return Mathf.round(liquids.currentAmount(), 0.1f) > 0.1f ?
                    ((int) (this.heat * 100f)) > 5 ?
                            Mathf.lerpDelta(this.heat, 0.0F, PowerHeatTurret.this.cooldown) :
                            0 
                    : this.heat;
        }

        @Override
        public void draw() {
            if (!labels) {
                labels = true;
                newLabel(() -> this, (b) -> {
                    return "heat: " + Mathf.round(this.heat, 0.1f);
                });
            }
            super.draw();
        }

        @Override
        protected void shoot(BulletType type) {
            int ix;
            if (PowerHeatTurret.this.chargeTime > 0.0F) {
                this.useAmmo();
                PowerHeatTurret.this.tr.trns(this.rotation, (float) (PowerHeatTurret.this.size * 8) / 2.0F);
                PowerHeatTurret.this.chargeBeginEffect.at(this.x + PowerHeatTurret.this.tr.x, this.y + PowerHeatTurret.this.tr.y, this.rotation);
                PowerHeatTurret.this.chargeSound.at(this.x + PowerHeatTurret.this.tr.x, this.y + PowerHeatTurret.this.tr.y, 1.0F);

                for (ix = 0; ix < PowerHeatTurret.this.chargeEffects; ++ix) {
                    Time.run(Mathf.random(PowerHeatTurret.this.chargeMaxDelay), () -> {
                        if (this.isValid()) {
                            PowerHeatTurret.this.tr.trns(this.rotation, (float) (PowerHeatTurret.this.size * 8) / 2.0F);
                            PowerHeatTurret.this.chargeEffect.at(this.x + PowerHeatTurret.this.tr.x, this.y + PowerHeatTurret.this.tr.y, this.rotation);
                        }
                    });
                }

                this.charging = true;
                Time.run(PowerHeatTurret.this.chargeTime, () -> {
                    if (this.isValid()) {
                        PowerHeatTurret.this.tr.trns(this.rotation, (float) (PowerHeatTurret.this.size * 8) / 2.0F);
                        this.recoil = PowerHeatTurret.this.recoilAmount;
                        this.heat += shootHeat;
                        this.bullet(type, this.rotation + Mathf.range(PowerHeatTurret.this.inaccuracy));
                        this.effects();
                        this.charging = false;
                    }
                });
            } else if (PowerHeatTurret.this.burstSpacing > 1.0E-4F) {
                for (ix = 0; ix < PowerHeatTurret.this.shots; ++ix) {
                    Time.run(PowerHeatTurret.this.burstSpacing * (float) ix, () -> {
                        if (this.isValid() && this.hasAmmo()) {
                            this.recoil = PowerHeatTurret.this.recoilAmount;
                            PowerHeatTurret.this.tr.trns(this.rotation, (float) (PowerHeatTurret.this.size * 8) / 2.0F, Mathf.range(PowerHeatTurret.this.xRand));
                            this.bullet(type, this.rotation + Mathf.range(PowerHeatTurret.this.inaccuracy));
                            this.effects();
                            this.useAmmo();
                            this.recoil = PowerHeatTurret.this.recoilAmount;
                            this.heat += shootHeat;
                        }
                    });
                }
            } else {
                if (PowerHeatTurret.this.alternate) {
                    float i = (float) (this.shotCounter % PowerHeatTurret.this.shots) - (float) (PowerHeatTurret.this.shots - 1) / 2.0F;
                    PowerHeatTurret.this.tr.trns(this.rotation - 90.0F, PowerHeatTurret.this.spread * i + Mathf.range(PowerHeatTurret.this.xRand), (float) (PowerHeatTurret.this.size * 8) / 2.0F);
                    this.bullet(type, this.rotation + Mathf.range(PowerHeatTurret.this.inaccuracy));
                } else {
                    PowerHeatTurret.this.tr.trns(this.rotation, (float) (PowerHeatTurret.this.size * 8) / 2.0F, Mathf.range(PowerHeatTurret.this.xRand));

                    for (ix = 0; ix < PowerHeatTurret.this.shots; ++ix) {
                        this.bullet(type, this.rotation + Mathf.range(PowerHeatTurret.this.inaccuracy + type.inaccuracy) + (float) (ix - (int) ((float) PowerHeatTurret.this.shots / 2.0F)) * PowerHeatTurret.this.spread);
                    }
                }

                ++this.shotCounter;
                this.recoil = PowerHeatTurret.this.recoilAmount;
                this.heat += shootHeat;
                this.effects();
                this.useAmmo();
            }

        }

        @Override
        protected void updateShooting() {
            if (this.reload >= PowerHeatTurret.this.reloadTime && heat <= 0) {
                BulletType type = this.peekAmmo();
                this.shoot(type);
                this.reload = 0.0F;
            } else if (!charging) {
                this.reload += this.delta() * this.peekAmmo().reloadMultiplier * this.baseReloadSpeed();
            }

        }
    }
}
