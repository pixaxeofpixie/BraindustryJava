package braindustry.entities.abilities;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.ModListener;
import braindustry.entities.ContainersForUnits.OrbitalPlatformsContainer;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

import java.util.Objects;

import static ModVars.modFunc.fullName;

public class OrbitalPlatformAbility extends ModAbility {
    private static final ObjectMap<Unit, OrbitalPlatformsContainer> unitMap = new ObjectMap<>();
    public static TextureRegion region;

    static {
        ModListener.updaters.add(() -> {
            Seq<Unit> deletedUnits = unitMap.keys().toSeq().select(u -> !u.isValid());
            deletedUnits.each(unit -> {
                unitMap.remove(unit).remove();
            });
        });
    }

    public final Seq<Weapon> weapons;
    private final int platformsCount;
    private final float rotateSpeed;
    public float visualElevation = 1;
    public OrbitalPlatformAbility(int platformsCount, float rotateSpeed, Weapon... weapons) {
        this.platformsCount = platformsCount;
        this.rotateSpeed = rotateSpeed;
        this.weapons = Seq.with(weapons);
        for (int i = 0; i < (platformsCount - weapons.length); i++) {
            this.weapons.add(null);
        }
    }

    @Override
    public void load() {
//        Log.info("loadCC: @==@==", getClass().getName());
        if (region == null) region = Core.atlas.find(fullName("orbital-platform"));
        weapons.select(Objects::nonNull).each(Weapon::load);
    }

    public TextureRegion region() {
        return region;
    }

    public int platformsCount() {
        return platformsCount;
    }

    public float rotateSpeed() {
        return rotateSpeed;
    }

    @Override
    public Seq<Weapon> weapons() {
        return weapons.copy();
    }

    @Override
    public void update(Unit unit) {
        getContainer(unit).update();
    }

    @Override
    public void draw(Unit unit) {
        getContainer(unit).draw();
    }

    private OrbitalPlatformsContainer getContainer(Unit unit) {
        return unitMap.get(unit, () -> new OrbitalPlatformsContainer(unit, this));
    }
}
