package braindustry.graphics;

import arc.Core;
import arc.func.Func;
import arc.func.Prov;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.gen.Unit;

import java.util.Arrays;

import static ModVars.modFunc.fullName;

public class BlackHoleDrawer {
    DistortionField[] distortions = create("black-hole-distortion", 4, Mathf.random(0.3f, 0.5f), -0.5f, 0.8f, -1.0f, -1.15f, Mathf.random(0.6f, 1.3f));
    DistortionField[] smallDistortions = create("black-hole-distortion-small", 5, 2.0f, -2.0f, -2.0f, 2.0f, 2.0f, -2.0f, 2.0f, -2.0f);
    DistortionField[] largeDistortions = create("black-hole-distortion-large", 3, -Mathf.random(0.2f, 0.45f), 0.6f, -0.55f, Mathf.random(0.4f, 0.8f));
    TextureRegion baseRegion;
    private Prov<Float> healthfProvider = () -> (-1f);
    private float healthf = -1;
    private void load(){
        baseRegion=Core.atlas.find(fullName("black-hole-base"));
    }
    public BlackHoleDrawer(Building building) {
        healthfProvider = () -> building.health();
        load();
    }
    public BlackHoleDrawer(Unit unit) {
        healthfProvider = () -> unit.health();
        load();
    }

    static DistortionField[] create(String name, int nameSize, float... sds) {
        String fullName = fullName(name);
        String[] sd = new String[nameSize];
        Arrays.fill(sd, fullName);
        return push(sd, sds);
    }

    static DistortionField[] push(String[] strings, float[] floats) {
        Seq<DistortionField> seq = new Seq<>();
        for (int i = 0; i < strings.length; i++) {
            seq.add(new DistortionField(strings[i], floats[i], Mathf.randomSeed(strings.length, 360)));
        }
        return seq.toArray(DistortionField.class);
    }
    float timeMul=0f;
    public void drawBlackHole(float x, float y, float rotation, float timeMul, float sizeC) {
        if (!baseRegion.found())load();
        Vec2 v = new Vec2();
        v.trns(rotation, 0);
        this.timeMul=timeMul;
        float speed = Mathf.clamp(1f - healthf, 0.0f, 2.5f) * 2.5f;

        float size = 1.0f + Mathf.sin(Time.time * (speed)) * 0.1f;
        size = ((sizeC/22.5f) + (Mathf.absin(Time.time, 16f, 0.1f)));
        Draw.xscl = size;//* 0.85f;
        Draw.yscl = size;//* 0.85f;
        Draw.rect(baseRegion, x - v.x, y - v.y, Time.time);
        Draw.xscl = 1.0f;
        Draw.yscl = 1.0f;

        drawDistotions(x, y, v, size, distortions, 72f);
        drawDistotions(x, y, v, size, smallDistortions, 51f);
        drawDistotions(x, y, v, size, largeDistortions, 90f);

        Draw.xscl = 1.0f;
        Draw.yscl = 1.0f;
    }

    private void drawDistotions(float x, float y, Vec2 v, float size, DistortionField[] distortions, float v2) {
        Vec2 vec = new Vec2();
        for (int i = 0; i < distortions.length; i++) {
            DistortionField distortion = distortions[i];
            float rot = i * v2 + distortion.rotation;
            vec.trns(rot, Math.max(0f,(size/2f) * 22.5f-3f));
            Draw.scl(size);
            if (!distortion.region.found()) distortion.load();
            Draw.rect(distortion.region, x - v.x + vec.x, y - v.y + vec.y, rot - 90);
            Draw.scl();
        }
    }

    public void update() {
        healthf = healthfProvider.get();
        float speed = Mathf.clamp(1f - healthf, 0.0f, 2.5f) * 2.5f;
        float speed2 = Mathf.clamp(1.0f + speed * 0.05f, 1.0f, 25000000.0f);
        Func<DistortionField, Float> rotProvider = (distortionField -> {
            return (distortionField.offset + speed2) / 60f;
        });

        updateDistortion(distortions, rotProvider);
        updateDistortion(smallDistortions, rotProvider);
        updateDistortion(largeDistortions, rotProvider );
    }

    private void updateDistortion(DistortionField[] distortions, Func<DistortionField, Float> rotProvider) {
        for (DistortionField distortion : distortions) {
            distortion.rotation += rotProvider.get(distortion) * Time.delta*timeMul;
        }
    }

    public void drawBlackHole(Building build) {
        drawBlackHole(build.x, build.y, build.rotation, 1f, build.block.size*8f);
    }
    public void drawBlackHole(Building build,float size) {
        drawBlackHole(build.x, build.y, build.rotation, 1f, size);
    }
    public void drawBlackHole(Unit unit) {
        drawBlackHole(unit.x, unit.y, unit.rotation, 1f, unit.hitSize);
    }
    public void drawBlackHole(Unit unit,float size) {
        drawBlackHole(unit.x, unit.y, unit.rotation, 1f, size);
    }
    public void drawBlackHole(Building build,float size,float timeMul) {
        drawBlackHole(build.x, build.y, build.rotation, timeMul, size);
    }

    static class DistortionField {
        public String name;
        public float rotation;
        public float offset;
        public TextureRegion region;

        public DistortionField(String name, float rotation, float offset) {
            this.name = name;
            this.rotation = rotation;
            this.offset = offset;
            load();
        }

        public void load() {
            region = Core.atlas.find(fullName(name),name);
        }
    }
}
