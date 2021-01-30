package braindustry.graphics.g3d;

import arc.func.Func;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.HexMesher;
import mindustry.graphics.g3d.MeshBuilder;
import mindustry.graphics.g3d.PlanetGrid;
import mindustry.graphics.g3d.PlanetRenderer;
import mindustry.type.Planet;
import mindustry.type.Sector;

public class ModPlanetRenderer extends PlanetRenderer {
    protected static final float radiusOffset = .17f;
    public static final Func<Object, Float> radiusProvider = (obj) -> {
        try {
            if (obj instanceof Sector) {
                obj = ((Sector) obj).planet;
            }
            if (obj instanceof Planet) {
                return ((Planet) obj).radius + ((Planet) obj).radius * radiusOffset;
            }
        } catch (NullPointerException e) {

        }
        return 0f;
    };
    protected static final Seq<Vec3> points = new Seq<>();
    protected Mesh[] outlines = new Mesh[10];
    protected ObjectMap<String, Mesh> meshMap = new ObjectMap<>();

    @Override
    public void dispose() {
        super.dispose();
        meshMap.values().forEach(Mesh::dispose);
    }

    @Override
    public void drawArc(Planet planet, Vec3 a, Vec3 b, Color from, Color to, float length, float timeScale, int pointCount) {
        Vec3 avg = Tmp.v31.set(b).add(a).scl(0.5f);
        avg.setLength(planet.radius * (1f + length));
        float outlineRad = radiusProvider.get(planet);
        points.clear();
        points.addAll(Tmp.v33.set(b).setLength(outlineRad), Tmp.v31, Tmp.v34.set(a).setLength(outlineRad));
        Tmp.bz3.set(points);

        for (int i = 0; i < pointCount + 1; i++) {
            float f = i / (float) pointCount;
            Tmp.c1.set(from).lerp(to, (f + Time.globalTime / timeScale) % 1f);
            batch.color(Tmp.c1);
            batch.vertex(Tmp.bz3.valueAt(Tmp.v32, f));

        }
        batch.flush(Gl.lineStrip);
    }

    @Override

    public void drawBorders(Sector sector, Color base) {
        float outlineRad = radiusProvider.get(sector);
        Color color = Tmp.c1.set(base).a(base.a + 0.3f + Mathf.absin(Time.globalTime, 5f, 0.3f));

        float r1 = sector.planet.radius;
        float r2 = outlineRad + 0.001f;

        for (int i = 0; i < sector.tile.corners.length; i++) {
            PlanetGrid.Corner c = sector.tile.corners[i], next = sector.tile.corners[(i + 1) % sector.tile.corners.length];

            Tmp.v31.set(c.v).setLength(r2);
            Tmp.v32.set(next.v).setLength(r2);
            Tmp.v33.set(c.v).setLength(r1);

            batch.tri2(Tmp.v31, Tmp.v32, Tmp.v33, color);

            Tmp.v31.set(next.v).setLength(r2);
            Tmp.v32.set(next.v).setLength(r1);
            Tmp.v33.set(c.v).setLength(r1);

            batch.tri2(Tmp.v31, Tmp.v32, Tmp.v33, color);
        }

        if (batch.getNumVertices() >= batch.getMaxVertices() - 6 * 6) {
            batch.flush(Gl.triangles);
        }
    }

    @Override
    public void setPlane(Sector sector) {
        float rotation = -planet.getRotation();
        float length = 0.01f;

        float outlineRad = radiusProvider.get(sector);
        projector.setPlane(
                //origin on sector position
                Tmp.v33.set(sector.tile.v).setLength(outlineRad + length).rotate(Vec3.Y, rotation).add(planet.position),
                //face up
                sector.plane.project(Tmp.v32.set(sector.tile.v).add(Vec3.Y)).sub(sector.tile.v).rotate(Vec3.Y, rotation).nor(),
                //right vector
                Tmp.v31.set(Tmp.v32).rotate(Vec3.Y, -rotation).add(sector.tile.v).rotate(sector.tile.v, 90).sub(sector.tile.v).rotate(Vec3.Y, rotation).nor()
        );
    }

    @Override
    public void fill(Sector sector, Color color, float offset) {
        float outlineRad = radiusProvider.get(sector);
        float rr = outlineRad + offset;
        for (int i = 0; i < sector.tile.corners.length; i++) {
            PlanetGrid.Corner c = sector.tile.corners[i], next = sector.tile.corners[(i + 1) % sector.tile.corners.length];
            batch.tri(Tmp.v31.set(c.v).setLength(rr), Tmp.v32.set(next.v).setLength(rr), Tmp.v33.set(sector.tile.v).setLength(rr), color);
        }
    }

    @Override
    public void drawSelection(Sector sector, Color color, float stroke, float length) {

        float outlineRad = radiusProvider.get(sector);
        float arad = outlineRad + length;

        for (int i = 0; i < sector.tile.corners.length; i++) {
            PlanetGrid.Corner next = sector.tile.corners[(i + 1) % sector.tile.corners.length];
            PlanetGrid.Corner curr = sector.tile.corners[i];

            next.v.scl(arad);
            curr.v.scl(arad);
            sector.tile.v.scl(arad);

            Tmp.v31.set(curr.v).sub(sector.tile.v).setLength(curr.v.dst(sector.tile.v) - stroke).add(sector.tile.v);
            Tmp.v32.set(next.v).sub(sector.tile.v).setLength(next.v.dst(sector.tile.v) - stroke).add(sector.tile.v);

            batch.tri(curr.v, next.v, Tmp.v31, color);
            batch.tri(Tmp.v31, next.v, Tmp.v32, color);

            sector.tile.v.scl(1f / arad);
            next.v.scl(1f / arad);
            curr.v.scl(1f / arad);
        }
    }

    @Override
    public void renderSectors(Planet planet) {
        //apply transformed position
        batch.proj().mul(planet.getTransform(mat));

        irenderer.renderSectors(planet);
        float outlineRad = radiusProvider.get(planet);
        //render sector grid
        Mesh mesh = outline(planet.grid.size, outlineRad);
        Shader shader = Shaders.planetGrid;
        Vec3 tile = planet.intersect(cam.getMouseRay(), outlineRad);
        Shaders.planetGrid.mouse.lerp(tile == null ? Vec3.Zero : tile.sub(planet.position).rotate(Vec3.Y, planet.getRotation()), 0.2f);

        shader.bind();
        shader.setUniformMatrix4("u_proj", cam.combined.val);
        shader.setUniformMatrix4("u_trans", planet.getTransform(mat).val);
        shader.apply();
        mesh.render(shader, Gl.lines);
    }

    public Mesh outline(int size, float radius) {
        return meshMap.get(size + "_" + radius, () -> {
            return MeshBuilder.buildHex(new HexMesher() {
                @Override
                public float getHeight(Vec3 position) {
                    return 0;
                }

                @Override
                public Color getColor(Vec3 position) {
                    return outlineColor;
                }
            }, size, true, radius, 0.2f);
        });
    }
}
