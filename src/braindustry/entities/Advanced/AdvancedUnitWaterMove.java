package braindustry.entities.Advanced;

import ModVars.modVars;
import mindustry.gen.UnitWaterMove;
import mindustryAddition.versions.ModEntityc;

public class AdvancedUnitWaterMove extends UnitWaterMove implements ModEntityc {
    public static int classId = 44;
    @Override
    public String toString() {
        return "Advanced"+super.toString();
    }
    public int classId() {
        return modVars.MOD_CONTENT_ID;
    }

    @Override
    public int modClassId() {
        return classId;
    }

    @Override
    public void update() {
        super.update();
        this.type.update(this);
    }
}
