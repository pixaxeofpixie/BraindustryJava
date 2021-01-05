package braindustry.entities.Advanced;

import mindustry.gen.PayloadUnit;

public class AdvancedPayloadUnit extends PayloadUnit {
    public static final int classId = 43;
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
