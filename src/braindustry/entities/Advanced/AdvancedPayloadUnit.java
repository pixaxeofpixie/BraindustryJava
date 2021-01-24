package braindustry.entities.Advanced;

import ModVars.modVars;
import mindustry.gen.PayloadUnit;
import mindustryAddition.versions.ModEntityc;

public class AdvancedPayloadUnit extends PayloadUnit implements ModEntityc {
    public static int classId = 43;
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
