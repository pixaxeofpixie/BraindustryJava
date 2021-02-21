package braindustry.type;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import braindustry.ModListener;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class ModUnitType extends UnitType {
    Seq<OrbitalPlatform> platforms=new Seq<>();
    private static ObjectMap<Unit, Unit> unitMap=new ObjectMap<>();
    public ModUnitType(String name) {
        super(name);
        ModListener.updaters.add(this::triggerUpdate);
    }
    protected void triggerUpdate(){

    }
}
