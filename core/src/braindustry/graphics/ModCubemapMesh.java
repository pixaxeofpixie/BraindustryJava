package braindustry.graphics;

import arc.Core;
import arc.graphics.*;
import arc.graphics.gl.Shader;
import arc.math.geom.Mat3D;
import mindustry.graphics.CubemapMesh;

public class ModCubemapMesh extends CubemapMesh {
    private static final float[] vertices = new float[]{-1.0F, 1.0F, -1.0F, -1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, -1.0F, 1.0F, -1.0F, -1.0F, -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F, -1.0F, -1.0F, -1.0F, -1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, 1.0F};
    private final Mesh mesh;
    private final Shader shader;
    private Cubemap map;

    public ModCubemapMesh(Cubemap map) {
        super(map);
        this.map = map;
        this.map.setFilter(Texture.TextureFilter.linear);
        this.mesh = new Mesh(true, vertices.length, 0, VertexAttribute.position3);
        this.mesh.getVerticesBuffer().limit(vertices.length);
        this.mesh.getVerticesBuffer().put(vertices, 0, vertices.length);
        this.shader = new ModShaders.ModCupemapShader();
    }

    public void setCubemap(Cubemap map) {
        this.map = map;
    }

    public void render(Mat3D projection) {
        render(projection,50f);
    }
    public void render(Mat3D projection, float scale) {
        this.map.bind();
        this.shader.bind();
        this.shader.setUniformi("u_cubemap", 0);
        this.shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformf("SCALE",scale);
        this.mesh.render(this.shader, 4);
    }

    public void dispose() {
        super.dispose();
        this.mesh.dispose();
        this.map.dispose();
    }
}
