package braindustry.world.meta;

import arc.Core;
import arc.struct.Seq;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

import java.util.Locale;

public enum  AStat {
    health,
    size,
    displaySize,
    buildTime,
    buildCost,
    memoryCapacity,
    explosiveness,
    flammability,
    radioactivity,
    heatCapacity,
    viscosity,
    temperature,
    flying,
    speed,
    buildSpeed,
    mineSpeed,
    mineTier,
    payloadCapacity,
    commandLimit,
    baseDeflectChance,
    lightningChance,
    lightningDamage,
    abilities,
    canBoost,
    itemCapacity(StatCat.items),
    itemsMoved(StatCat.items),
    launchTime(StatCat.items),
    maxConsecutive(StatCat.items),
    liquidCapacity(StatCat.liquids),
    powerCapacity(StatCat.power),
    powerUse(StatCat.power),
    powerDamage(StatCat.power),
    powerRange(StatCat.power),
    powerConnections(StatCat.power),
    basePowerGeneration(StatCat.power),
    tiles(StatCat.crafting),
    input(StatCat.crafting),
    output(StatCat.crafting),
    productionTime(StatCat.crafting),
    drillTier(StatCat.crafting),
    drillSpeed(StatCat.crafting),
    maxUnits(StatCat.crafting),
    linkRange(StatCat.crafting),
    instructions(StatCat.crafting),
    weapons(StatCat.function),
    bullet(StatCat.function),
    speedIncrease(StatCat.function),
    repairTime(StatCat.function),
    range(StatCat.function),
    shootRange(StatCat.function),
    inaccuracy(StatCat.function),
    shots(StatCat.function),
    reload(StatCat.function),
    powerShot(StatCat.function),
    targetsAir(StatCat.function),
    targetsGround(StatCat.function),
    damage(StatCat.function),
    ammo(StatCat.function),
    ammoUse(StatCat.function),
    shieldHealth(StatCat.function),
    cooldownTime(StatCat.function),
    booster(StatCat.optional),
    boostEffect(StatCat.optional),
    affinities(StatCat.optional),
    maxConnections(StatCat.function),
    recipes(StatCat.function);
    public final StatCat category;
    public static AStat fromExist(Stat stat){
        return Seq.with(values()).find((v)-> v.name().equals(stat.name()));
    }
    private AStat(StatCat category) {
        this.category = category;
    }

    private AStat() {
        this.category = StatCat.general;
    }

    public String localized() {
        return Core.bundle.get("stat." + this.name().toLowerCase(Locale.ROOT));
    }
}
