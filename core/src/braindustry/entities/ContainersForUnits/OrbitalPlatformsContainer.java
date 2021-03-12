package braindustry.entities.ContainersForUnits;

import ModVars.math.ModAngles;
import ModVars.math.ModMath;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import braindustry.type.OrbitalPlatform;
import braindustry.type.UnitContainer;
import mindustry.audio.SoundLoop;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Sounds;
import mindustry.gen.Trailc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.graphics.Trail;
import mindustry.type.Weapon;

import static mindustry.Vars.*;

public class OrbitalPlatformsContainer extends UnitContainer {
    public static final float shadowTX = -5, shadowTY = -6, outlineSpace = 0.01f;
    protected final float z = 85f;
    final OrbitalPlatformAbility ability;
    final Seq<OrbitalPlatform> orbitalPlatforms = new Seq<>();
//    private float rotation;

    public OrbitalPlatformsContainer(Unit unit, OrbitalPlatformAbility ability) {
        super(unit);
        this.ability = ability;
        for (int i = 0; i < ability.platformsCount(); i++) {
            orbitalPlatforms.add(new OrbitalPlatform(ability, unit, ability.weapons.get(i)).id(i));
        }
    }

    protected void drawShadow(OrbitalPlatform platform) {

        Draw.color(Pal.shadow);
        float e = ability.visualElevation;
        Draw.rect(ability.region(), platform.x + shadowTX * e, platform.y + shadowTY * e, platform.rotation - 90);
        Draw.color();
    }
    public static class NullAbilityException extends RuntimeException{

        public NullAbilityException(String message) {
            super(message);
        }
    }

    public void drawEngines(OrbitalPlatform platform){

        float scale = 0.5f;
//        float offset = ability.engineOffset/2f + ability.engineOffset/2f*scale;

        float engineSize = ability.engineSize;
        Seq<Vec2> enginePosses = ability.enginePosses;
        enginePosses.each(enginePos->{

            int index = enginePosses.indexOf(enginePos);
            float rotation = platform.rotation/*+ 360f/ enginePosses.size*index*/;

            Draw.color(unit.team.color);
            Vec2 engineOffset=enginePos.cpy().rotate(rotation);
            Fill.circle(
                    platform.x + engineOffset.x,
                    platform.y + engineOffset.y,
                    (engineSize + Mathf.absin(Time.time, 2f, engineSize / 4f)) * scale
            );
            Draw.color(Color.white);
            Vec2 one=new Vec2(Angles.trnsx(enginePos.angle(),  1f),Angles.trnsy(enginePos.angle(), 1f));
            engineOffset.sub(one);
            Fill.circle(
                    platform.x  + engineOffset.x,
                    platform.y  + engineOffset.y,
                    (engineSize + Mathf.absin(Time.time, 2f, engineSize / 4f)) / 2f  * scale
            );
            Draw.color();
        });
    }
    @Override
    public void draw() {
        float unitZ = unit.elevation > 0.5f ? (unit.type.lowAltitude ? Layer.flyingUnitLow : Layer.flyingUnit) : unit.type.groundLayer + Mathf.clamp(unit.type.hitSize / 4000f, 0, 0.01f);

        int platformsCount = orbitalPlatforms.size;
        float oneAngle = 360f / (float) platformsCount;
        float hitSize = unit.hitSize;
        for (OrbitalPlatform platform : orbitalPlatforms) {

            Vec2 platformPos = Tmp.v1.set(unit).add(Tmp.v2.trns(platform.orbitRotation, hitSize, hitSize));
            float platformRot = Tmp.v2.set(unit).angleTo(platformPos);
            platformRot = 0f;
            platform.set(platformPos);
            Draw.z(z);
            drawEngines(platform);
            Draw.rect(ability.outlineRegion(), platformPos.x, platformPos.y, platformRot - 90);
            Draw.rect(ability.region(), platformPos.x, platformPos.y, platformRot - 90);
            drawWeapon(platform);
            Draw.z(Math.min(Layer.darkness, z - 1f));

            drawShadow(platform);
        }
        Draw.reset();
    }

    public void updateWeapon(OrbitalPlatform platform) {
        WeaponMount mount = platform.mount;
        if (mount == null) return;
//        mount.rotate;
        mount.rotate=false;
        Seq.with(unit.mounts).each(m->{
            mount.rotate|=m.rotate;
        });
        mount.aimX=unit.aimX();
        mount.aimY=unit.aimY();
        mount.rotate=unit.isRotate();
        mount.shoot= unit.isShooting;
        boolean can = unit.canShoot();
        Weapon weapon = mount.weapon;
        mount.reload = Math.max(mount.reload - Time.delta * unit.reloadMultiplier(), 0);
        float weaponRotation = platform.rotation - 90 + (weapon.rotate ? mount.rotation : 0);
//            float mountX = unit.x + Angles.trnsx(unit.rotation - 90, weapon.x, weapon.y);
//            float mountY = unit.y + Angles.trnsy(unit.rotation - 90, weapon.x, weapon.y);
        float mountX = platform.x, mountY = platform.y;
        Tmp.v1.trns(weaponRotation, weapon.shootX, weapon.shootY);
        float shootX = mountX + Tmp.v1.x;
        float shootY = mountY + Tmp.v1.y;
        float shootAngle = weapon.rotate ? weaponRotation + 90 : Angles.angle(shootX, shootY, mount.aimX, mount.aimY) + (platform.rotation - platform.angleTo(mount.aimX, mount.aimY));
        if (weapon.continuous && mount.bullet != null) {
            if (!mount.bullet.isAdded() || mount.bullet.time >= mount.bullet.lifetime || mount.bullet.type != weapon.bullet) {
                mount.bullet = null;
            } else {
                mount.bullet.rotation(weaponRotation + 90);
                mount.bullet.set(shootX, shootY);
                mount.reload = weapon.reload;
                unit.vel.add(Tmp.v1.trns(unit.rotation + 180.0F, mount.bullet.type.recoil));
                if (weapon.shootSound != Sounds.none && !headless) {
                    if (mount.sound == null) mount.sound = new SoundLoop(weapon.shootSound, 1.0F);
                    mount.sound.update(unit.x, unit.y, true);
                }
            }
        } else {
            mount.heat = Math.max(mount.heat - Time.delta * unit.reloadMultiplier() / mount.weapon.cooldownTime, 0);
            if (mount.sound != null) {
                mount.sound.update(unit.x, unit.y, false);
            }
        }
        if (weapon.rotate && (mount.rotate || mount.shoot) && can) {
//                float axisX = this.x + Angles.trnsx(unit.rotation - 90, weapon.x, weapon.y);
//                float axisY = this.y + Angles.trnsy(unit.rotation - 90, weapon.x, weapon.y);
            float axisX = platform.x, axisY = platform.y;
            mount.targetRotation = Angles.angle(axisX, axisY, mount.aimX, mount.aimY);// - unit.rotation;
            mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, weapon.rotateSpeed * Time.delta);
        } else if (!weapon.rotate) {
            mount.rotation = 0;
            mount.targetRotation = platform.angleTo(mount.aimX, mount.aimY);
        }
        if (mount.shoot && can && (unit.ammo > 0 || !state.rules.unitAmmo || unit.team().rules().infiniteAmmo) && (unit.vel.len() >= mount.weapon.minShootVelocity || (net.active() && !unit.isLocal())) && mount.reload <= 1.0E-4F && Angles.within(weapon.rotate ? mount.rotation : unit.rotation, mount.targetRotation, mount.weapon.shootCone)) {
            platform.shoot(mount, shootX, shootY, mount.aimX, mount.aimY, mountX, mountY, shootAngle, Mathf.sign(weapon.x));
            mount.reload = weapon.reload;
            unit.ammo--;
            if (unit.ammo < 0) unit.ammo = 0;
        }

    }

    public void drawWeapon(OrbitalPlatform platform) {
        WeaponMount mount = platform.mount;
        if (mount == null) return;
        Weapon weapon = mount.weapon;
        float rotation = platform.rotation - 90f;
        float weaponRotation = rotation + (weapon.rotate ? mount.rotation : 0);
        float recoil = -((mount.reload) / weapon.reload * weapon.recoil);
        float wx = platform.x + Angles.trnsx(rotation, weapon.x, weapon.y) + Angles.trnsx(weaponRotation, 0, recoil),
                wy = platform.y + Angles.trnsy(rotation, weapon.x, weapon.y) + Angles.trnsy(weaponRotation, 0, recoil);

        if (weapon.shadow > 0) {
            Drawf.shadow(wx, wy, weapon.shadow);
        }

        if (weapon.outlineRegion.found()) {
            float z = Draw.z();
            if (!weapon.top) Draw.z(z - outlineSpace);

            Draw.rect(weapon.outlineRegion,
                    wx, wy,
                    weapon.outlineRegion.width * Draw.scl * -Mathf.sign(weapon.flipSprite),
                    weapon.region.height * Draw.scl,
                    weaponRotation);

            Draw.z(z);
        }

        Draw.rect(weapon.region,
                wx, wy,
                weapon.region.width * Draw.scl * -Mathf.sign(weapon.flipSprite),
                weapon.region.height * Draw.scl,
                weaponRotation);

        if (weapon.heatRegion.found() && mount.heat > 0) {
            Draw.color(weapon.heatColor, mount.heat);
            Draw.blend(Blending.additive);
            Draw.rect(weapon.heatRegion,
                    wx, wy,
                    weapon.heatRegion.width * Draw.scl * -Mathf.sign(weapon.flipSprite),
                    weapon.heatRegion.height * Draw.scl,
                    weaponRotation);
            Draw.blend();
            Draw.color();
        }


        Draw.reset();
    }
protected void rotateTo(OrbitalPlatform platform,float angel){
    float v =  ((ability.rotateSpeed() % 360f) / 180f);
    
    float speed =Math.abs(platform.orbitRotation %360f - angel %360f) * 0.01f;
    speed=Math.min(ability.rotateSpeed(),speed);
//            float perfectAngle=

    platform.orbitRotation = Mathf.mod(ModAngles.moveLerpToward(platform.orbitRotation, angel, speed * Time.delta),360f);
}
    public void update() {
        float unitRotation = unit.rotation;
        final float platformCount = orbitalPlatforms.size;
        float oneAngle = 360f / platformCount;
        float platformHitsize=16f;
        Vec2 target=new Vec2(unit.aimX(),unit.aimY());
        float onePlatformAngle= ModMath.atan(platformHitsize/unit.hitSize());
        for (OrbitalPlatform platform : orbitalPlatforms) {
            updateWeapon(platform);
            if (!unit.isShooting()){
                rotateTo(platform,unitRotation+oneAngle * (platform.id));
            } else {
                float v = unit.angleTo(target)-90f;
                rotateTo(platform, v+((platform.id+platformCount/2f)%platformCount)*onePlatformAngle);

            }
        }
//        Angles.moveToward()
//        Mathf.slerpDelta()

//        rotation = Mathf.lerpDelta(rotation, unit.rotation, v);
//        rotation = Mathf.lerpDelta(rotation, unit.rotation, (ability.rotateSpeed()%360f)/360f);
    }
    public static boolean notNullPlatform(OrbitalPlatform platform){
        return platform!=null && platform.mount!=null;
    }
    @Override
    public void remove() {
        orbitalPlatforms.select(OrbitalPlatformsContainer::notNullPlatform).each(platform -> {
            WeaponMount mount = platform.mount;
            if(mount.bullet != null){
                mount.bullet.time = mount.bullet.lifetime - 10f;
                mount.bullet = null;
            }

            if(mount.sound != null){
                mount.sound.stop();
            }
        });
        orbitalPlatforms.clear();
    }
}
