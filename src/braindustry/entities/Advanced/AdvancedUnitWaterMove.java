package braindustry.entities.Advanced;

import mindustry.gen.UnitWaterMove;

public class AdvancedUnitWaterMove extends UnitWaterMove {
    public static int classId = 44;
    @Override
    public String toString() {
        return "Advanced"+super.toString();
    }
    public int classId() {
        return classId;
    }
    @Override
    public void update() {
        super.update();
        this.type.update(this);
    }
}
