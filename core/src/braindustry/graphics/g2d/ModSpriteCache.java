package braindustry.graphics.g2d;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.SpriteCache;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.math.Mathf;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.ArcRuntimeException;
import arc.util.Disposable;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class ModSpriteCache implements Disposable {
    static final int VERTEX_SIZE = 5;
    private static final float[] tempVertices = new float[30];
    private final Mesh mesh;
    private final Mat transformMatrix;
    private final Mat projectionMatrix;
    private final Mat combinedMatrix;
    private final Shader shader;
    private final Seq<Texture> textures;
    private final IntSeq counts;
    private final Color color;
    public int renderCalls;
    public int totalRenderCalls;
    private boolean drawing;
    private Seq<Cache> caches;
    private Cache currentCache;
    private float colorPacked;
    private Shader customShader;

    public ModSpriteCache() {
        this(1000, false);
    }

    public ModSpriteCache(int size, boolean useIndices) {
        this(size, 16, createDefaultShader(), useIndices);
    }

    public ModSpriteCache(int size, int cacheSize, boolean useIndices) {
        this(size, cacheSize, createDefaultShader(), useIndices);
    }

    public ModSpriteCache(int size, int cacheSize, Shader shader, boolean useIndices) {
        this.transformMatrix = new Mat();
        this.projectionMatrix = new Mat();
        this.combinedMatrix = new Mat();
        this.textures = new Seq(8);
        this.counts = new IntSeq(8);
        this.color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderCalls = 0;
        this.totalRenderCalls = 0;
        this.colorPacked = Color.whiteFloatBits;
        this.customShader = null;
        this.shader = shader;
        if (useIndices && size > 8191) {
            throw new IllegalArgumentException("Can't have more than 8191 sprites per batch: " + size);
        } else {
            this.mesh = new Mesh(true, size * (useIndices ? 4 : 6), useIndices ? size * 6 : 0, new VertexAttribute[]{new VertexAttribute(1, 2, "a_position"), new VertexAttribute(4, 4, "a_color"), new VertexAttribute(16, 2, "a_texCoord0")});
            this.mesh.setAutoBind(false);
            this.caches = new Seq(cacheSize);
            if (useIndices) {
                int length = size * 6;
                short[] indices = new short[length];
                short j = 0;

                for(int i = 0; i < length; j = (short)(j + 4)) {
                    indices[i] = j;
                    indices[i + 1] = (short)(j + 1);
                    indices[i + 2] = (short)(j + 2);
                    indices[i + 3] = (short)(j + 2);
                    indices[i + 4] = (short)(j + 3);
                    indices[i + 5] = j;
                    i += 6;
                }

                this.mesh.setIndices(indices);
            }

            this.projectionMatrix.setOrtho(0.0F, 0.0F, (float) Core.graphics.getWidth(), (float)Core.graphics.getHeight());
        }
    }

    public static Shader createDefaultShader() {
        String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projectionViewMatrix;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main(){\n   v_color = a_color;\n   v_color.a = v_color.a * (255.0/254.0);\n   v_texCoords = a_texCoord0;\n   gl_Position =  u_projectionViewMatrix * a_position;\n}\n";
        String fragmentShader = "varying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nvoid main(){\n  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n}";
        return new Shader(vertexShader, fragmentShader);
    }

    public Seq<Cache> getCaches() {
        return this.caches;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        this.colorPacked = this.color.toFloatBits();
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color tint) {
        this.color.set(tint);
        this.colorPacked = tint.toFloatBits();
    }

    public float getPackedColor() {
        return this.colorPacked;
    }

    public void setPackedColor(float packedColor) {
        this.color.abgr8888(packedColor);
        this.colorPacked = packedColor;
    }

    public void beginCache() {
        if (this.drawing) {
            throw new IllegalStateException("end must be called before beginCache");
        } else if (this.currentCache != null) {
            throw new IllegalStateException("endCache must be called before begin.");
        } else {
            Cache last =caches.isEmpty()?null: caches.peek();
            this.mesh.getVerticesBuffer().position(last==null ? 0 : last.offset + last.maxCount);
            this.currentCache = new Cache(this.caches.size, this.mesh.getVerticesBuffer().position());
            this.caches.add(this.currentCache);
            this.mesh.getVerticesBuffer().limit(this.mesh.getVerticesBuffer().capacity());
        }
    }

    public void beginCache(int cacheID) {
        if (this.drawing) {
            throw new IllegalStateException("end must be called before beginCache");
        } else if (this.currentCache != null) {
            throw new IllegalStateException("endCache must be called before begin.");
        } else {
            this.currentCache = (Cache)this.caches.get(cacheID);
            Arrays.fill(this.currentCache.counts, 0);
            this.mesh.getVerticesBuffer().position(this.currentCache.offset);
        }
    }

    public int endCache() {
        if (this.currentCache == null) {
            throw new IllegalStateException("beginCache must be called before endCache.");
        } else {
            Cache cache = this.currentCache;
            int cacheCount = this.mesh.getVerticesBuffer().position() - cache.offset;
            int i;
            int n;
            if (cache.textures == null) {
                cache.maxCount = cacheCount;
                cache.textureCount = this.textures.size;
                cache.textures = (Texture[])this.textures.toArray(Texture.class);
                cache.counts = new int[cache.textureCount];
                i = 0;

                for(n = this.counts.size; i < n; ++i) {
                    cache.counts[i] = this.counts.get(i);
                }
            } else {
                if (cacheCount > cache.maxCount) {
                    throw new ArcRuntimeException("If a cache is not the last created, it cannot be redefined with more entries than when it was first created: " + cacheCount + " (" + cache.maxCount + " max)");
                }

                cache.textureCount = this.textures.size;
                if (cache.textures.length < cache.textureCount) {
                    cache.textures = new Texture[cache.textureCount];
                }

                i = 0;

                for(n = cache.textureCount; i < n; ++i) {
                    cache.textures[i] = (Texture)this.textures.get(i);
                }

                if (cache.counts.length < cache.textureCount) {
                    cache.counts = new int[cache.textureCount];
                }

                i = 0;

                for(n = cache.textureCount; i < n; ++i) {
                    cache.counts[i] = this.counts.get(i);
                }

                FloatBuffer vertices = this.mesh.getVerticesBuffer();
                vertices.position(0);
                Cache lastCache = (Cache)this.caches.get(this.caches.size - 1);
                vertices.limit(lastCache.offset + lastCache.maxCount);
            }

            this.currentCache = null;
            this.textures.clear();
            this.counts.clear();
            if (Core.app.isWeb()) {
                this.mesh.getVerticesBuffer().position(0);
            }

            return cache.id;
        }
    }

    public void clear() {
        this.caches.clear();
        this.mesh.getVerticesBuffer().clear().flip();
    }

    public int reserve(int sprites) {
        if (this.currentCache == null) {
            throw new IllegalStateException("beginCache must be called before ensureSize.");
        } else {
            int spriteSize = 5 * (this.mesh.getNumIndices() > 0 ? 4 : 6);
            int currentUsed = this.currentCache.maxCount;
            int required = sprites * spriteSize;
            int toAdd = required - currentUsed;
            if (toAdd > 0) {
                Cache var10000 = this.currentCache;
                var10000.maxCount += toAdd;
                this.mesh.getVerticesBuffer().position(this.currentCache.offset + this.currentCache.maxCount);
                return toAdd / spriteSize;
            } else {
                return 0;
            }
        }
    }

    public void add(Texture texture, float[] vertices, int offset, int length) {
        if (this.currentCache == null) {
            throw new IllegalStateException("beginCache must be called before add.");
        } else if (this.mesh.getVerticesBuffer().position() + length >= this.mesh.getVerticesBuffer().limit()) {
            throw new IllegalStateException("Out of vertex space! Size: " + this.mesh.getVerticesBuffer().capacity() + " Required: " + (this.mesh.getVerticesBuffer().position() + length));
        } else {
            int verticesPerImage = this.mesh.getNumIndices() > 0 ? 4 : 6;
            int count = length / (verticesPerImage * 5) * 6;
            int lastIndex = this.textures.size - 1;
            if (lastIndex >= 0 && this.textures.get(lastIndex) == texture) {
                this.counts.incr(lastIndex, count);
            } else {
                this.textures.add(texture);
                this.counts.add(count);
            }

            this.mesh.getVerticesBuffer().put(vertices, offset, length);
        }
    }

    public void add(TextureRegion region, float x, float y) {
        this.add(region, x, y, (float)region.width, (float)region.height);
    }

    public void add(TextureRegion region, float x, float y, float width, float height) {
        float fx2 = x + width;
        float fy2 = y + height;
        float u = region.u;
        float v = region.v2;
        float u2 = region.u2;
        float v2 = region.v;
        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = this.colorPacked;
        tempVertices[3] = u;
        tempVertices[4] = v;
        tempVertices[5] = x;
        tempVertices[6] = fy2;
        tempVertices[7] = this.colorPacked;
        tempVertices[8] = u;
        tempVertices[9] = v2;
        tempVertices[10] = fx2;
        tempVertices[11] = fy2;
        tempVertices[12] = this.colorPacked;
        tempVertices[13] = u2;
        tempVertices[14] = v2;
        if (this.mesh.getNumIndices() > 0) {
            tempVertices[15] = fx2;
            tempVertices[16] = y;
            tempVertices[17] = this.colorPacked;
            tempVertices[18] = u2;
            tempVertices[19] = v;
            this.add(region.texture, tempVertices, 0, 20);
        } else {
            tempVertices[15] = fx2;
            tempVertices[16] = fy2;
            tempVertices[17] = this.colorPacked;
            tempVertices[18] = u2;
            tempVertices[19] = v2;
            tempVertices[20] = fx2;
            tempVertices[21] = y;
            tempVertices[22] = this.colorPacked;
            tempVertices[23] = u2;
            tempVertices[24] = v;
            tempVertices[25] = x;
            tempVertices[26] = y;
            tempVertices[27] = this.colorPacked;
            tempVertices[28] = u;
            tempVertices[29] = v;
            this.add(region.texture, tempVertices, 0, 30);
        }

    }

    public void add(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        float worldOriginX = x + originX;
        float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;
        if (scaleX != 1.0F || scaleY != 1.0F) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;
        float u;
        float v;
        if (rotation != 0.0F) {
            u = Mathf.cosDeg(rotation);
            v = Mathf.sinDeg(rotation);
            x1 = u * fx - v * fy;
            y1 = v * fx + u * fy;
            x2 = u * fx - v * fy2;
            y2 = v * fx + u * fy2;
            x3 = u * fx2 - v * fy2;
            y3 = v * fx2 + u * fy2;
            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = fx;
            y1 = fy;
            x2 = fx;
            y2 = fy2;
            x3 = fx2;
            y3 = fy2;
            x4 = fx2;
            y4 = fy;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;
        u = region.u;
        v = region.v2;
        float u2 = region.u2;
        float v2 = region.v;
        tempVertices[0] = x1;
        tempVertices[1] = y1;
        tempVertices[2] = this.colorPacked;
        tempVertices[3] = u;
        tempVertices[4] = v;
        tempVertices[5] = x2;
        tempVertices[6] = y2;
        tempVertices[7] = this.colorPacked;
        tempVertices[8] = u;
        tempVertices[9] = v2;
        tempVertices[10] = x3;
        tempVertices[11] = y3;
        tempVertices[12] = this.colorPacked;
        tempVertices[13] = u2;
        tempVertices[14] = v2;
        if (this.mesh.getNumIndices() > 0) {
            tempVertices[15] = x4;
            tempVertices[16] = y4;
            tempVertices[17] = this.colorPacked;
            tempVertices[18] = u2;
            tempVertices[19] = v;
            this.add(region.texture, tempVertices, 0, 20);
        } else {
            tempVertices[15] = x3;
            tempVertices[16] = y3;
            tempVertices[17] = this.colorPacked;
            tempVertices[18] = u2;
            tempVertices[19] = v2;
            tempVertices[20] = x4;
            tempVertices[21] = y4;
            tempVertices[22] = this.colorPacked;
            tempVertices[23] = u2;
            tempVertices[24] = v;
            tempVertices[25] = x1;
            tempVertices[26] = y1;
            tempVertices[27] = this.colorPacked;
            tempVertices[28] = u;
            tempVertices[29] = v;
            this.add(region.texture, tempVertices, 0, 30);
        }

    }

    public void begin() {
        if (this.drawing) {
            throw new IllegalStateException("end must be called before begin.");
        } else if (this.currentCache != null) {
            throw new IllegalStateException("endCache must be called before begin");
        } else {
            this.renderCalls = 0;
            this.combinedMatrix.set(this.projectionMatrix).mul(this.transformMatrix);
            Shader shader = this.customShader != null ? this.customShader : this.shader;
            shader.bind();
            shader.setUniformMatrix4("u_projectionViewMatrix", this.combinedMatrix);
            shader.setUniformi("u_texture", 0);
            this.mesh.bind(shader);
            this.drawing = true;
        }
    }

    public void end() {
        if (!this.drawing) {
            throw new IllegalStateException("begin must be called before end.");
        } else {
            this.drawing = false;
            Gl.depthMask(true);
            if (this.customShader != null) {
                this.mesh.unbind(this.customShader);
            } else {
                this.mesh.unbind(this.shader);
            }

        }
    }

    public void draw(int cacheID) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteCache.begin must be called before draw.");
        } else {
            Cache cache = (Cache)this.caches.get(cacheID);
            int verticesPerImage = this.mesh.getNumIndices() > 0 ? 4 : 6;
            int offset = cache.offset / (verticesPerImage * 5) * 6;
            Texture[] textures = cache.textures;
            int[] counts = cache.counts;
            int textureCount = cache.textureCount;
            Shader shader = this.customShader != null ? this.customShader : this.shader;

            for(int i = 0; i < textureCount; ++i) {
                int count = counts[i];
                textures[i].bind();
                this.mesh.render(shader, 4, offset, count);
                offset += count;
            }

            this.renderCalls += textureCount;
            this.totalRenderCalls += textureCount;
        }
    }

    public void draw(int cacheID, int offset, int length) {
        if (!this.drawing) {
            throw new IllegalStateException("SpriteCache.begin must be called before draw.");
        } else {
            Cache cache = (Cache)this.caches.get(cacheID);
            offset = offset * 6 + cache.offset;
            length *= 6;
            Texture[] textures = cache.textures;
            int[] counts = cache.counts;
            int textureCount = cache.textureCount;

            for(int i = 0; i < textureCount; ++i) {
                textures[i].bind();
                int count = counts[i];
                if (count > length) {
                    i = textureCount;
                    count = length;
                } else {
                    length -= count;
                }

                if (this.customShader != null) {
                    this.mesh.render(this.customShader, 4, offset, count);
                } else {
                    this.mesh.render(this.shader, 4, offset, count);
                }

                offset += count;
            }

            this.renderCalls += cache.textureCount;
            this.totalRenderCalls += textureCount;
        }
    }

    public void dispose() {
        this.mesh.dispose();
        if (this.shader != null) {
            this.shader.dispose();
        }

    }

    public Mat getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void setProjectionMatrix(Mat projection) {
        if (this.drawing) {
            throw new IllegalStateException("Can't set the matrix within begin/end.");
        } else {
            this.projectionMatrix.set(projection);
        }
    }

    public Mat getTransformMatrix() {
        return this.transformMatrix;
    }

    public void setTransformMatrix(Mat transform) {
        if (this.drawing) {
            throw new IllegalStateException("Can't set the matrix within begin/end.");
        } else {
            this.transformMatrix.set(transform);
        }
    }

    public void setShader(Shader shader) {
        this.customShader = shader;
    }

    public boolean isDrawing() {
        return this.drawing;
    }

     static class Cache {
        final int id;
        final int offset;
        int maxCount;
        int textureCount;
        Texture[] textures;
        int[] counts;

        public Cache(int id, int offset) {
            this.id = id;
            this.offset = offset;
        }
    }
}
