package braindustry.type;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import mindustry.entities.Effect;
import mindustry.entities.EntityGroup;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class OrbitalPlatform implements Position {
   public float rotation=0f;
   public final long id;
   public final OrbitalPlatformAbility ability;
   public WeaponMount mount=null;
   public static int sequenceNum = 0;
   public final Unit unit;
public float x,y;

   public OrbitalPlatform(OrbitalPlatformAbility ability, Unit unit, Weapon weapon) {
      this.ability = ability;
      this.unit = unit;
      id=EntityGroup.nextId();
//      setupWeapon(ability);

      if (weapon!=null) {
         mount = new WeaponMount(weapon);
      }
   }

   public void set(Vec2 pos){
      x=pos.x;
      y=pos.y;
   }
   public void shoot(WeaponMount mount, float x, float y, float aimX, float aimY, float mountX,
                      float mountY, float rotation, int side) {

      Weapon weapon = mount.weapon;
      float baseX = this.x;
      float baseY = this.y;
      boolean delay = weapon.firstShotDelay + weapon.shotDelay > 0.0F;
      (delay ? weapon.chargeSound : weapon.continuous ? Sounds.none : weapon.shootSound).at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
      BulletType ammo = weapon.bullet;
      float lifeScl = ammo.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y, aimX, aimY) / ammo.range()) : 1.0F;
      sequenceNum = 0;
      if (delay) {
         Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f)->{
            Time.run(sequenceNum * weapon.shotDelay + weapon.firstShotDelay, ()->{
               if (!unit.isAdded()) return;
               mount.bullet = bullet(weapon, x + this.x - baseX, y + this.y - baseY, f + Mathf.range(weapon.inaccuracy), lifeScl);
            });
            sequenceNum++;
         });
      } else {
         Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f)->mount.bullet = bullet(weapon, x, y, f + Mathf.range(weapon.inaccuracy), lifeScl));
      }
      boolean parentize = ammo.keepVelocity;
      if (delay) {
         Time.run(weapon.firstShotDelay, ()->{
            if (!unit.isAdded()) return;
            unit.vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
            Effect.shake(weapon.shake, weapon.shake, x, y);
            mount.heat = 1.0F;
            if (!weapon.continuous) {
               weapon.shootSound.at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
            }
         });
      } else {
         unit.vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
         Effect.shake(weapon.shake, weapon.shake, x, y);
         mount.heat = 1.0F;
      }
      weapon.ejectEffect.at(mountX, mountY, rotation * side);
      ammo.shootEffect.at(x, y, rotation, parentize ? this : null);
      ammo.smokeEffect.at(x, y, rotation, parentize ? this : null);
      unit.apply(weapon.shootStatus, weapon.shootStatusDuration);
   }
   private Bullet bullet(Weapon weapon, float x, float y, float angle, float lifescl) {

      float xr = Mathf.range(weapon.xRand);
      return weapon.bullet.create(unit, unit.team(), x + Angles.trnsx(angle, 0, xr), y + Angles.trnsy(angle, 0, xr), angle, (1.0F - weapon.velocityRnd) + Mathf.random(weapon.velocityRnd), lifescl);
   }

   @Override
   public float getX() {
      return x;
   }

   @Override
   public float getY() {
      return y;
   }
}
