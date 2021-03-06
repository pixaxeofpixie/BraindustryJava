package braindustry.graphics.g2d;

import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.g2d.Batch;
import arc.graphics.g2d.SpriteCache;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.struct.Seq;
import arc.util.Pack;

import java.util.Iterator;

public class FloorMultiCacheBatch extends Batch {
    private static final int maxSpritesPerCache = 102000;
    Seq<ModSpriteCache> caches = new Seq<>();
    Shader shader = SpriteCache.createDefaultShader();
    int currentid = -1;
    int maxCacheSize;
    int offset;
    boolean recaching = false;
    Seq<Integer> removeCacheIDs=new Seq<>();
    public FloorMultiCacheBatch(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    ModSpriteCache currentCache() {
        int needed = this.currentid == -1 ? this.offset / 102000 : this.currentid;
        if (needed >= this.caches.size) {
            this.caches.add(new ModSpriteCache(102000, 16, this.shader, false));
        }

        return (ModSpriteCache) this.caches.get(needed);
    }

    public void removeCache(int id) {
        if (id==-1)return;
        int cacheID = Pack.leftShort(id);
        int batch = Pack.rightShort(id);
        ModSpriteCache spriteCache = caches.get(batch);
//        spriteCache.draw(cacheID);
        removeCacheIDs.add(cacheID);
//        spriteCache.getCaches().remove(cacheID);
//        if (currentid==batch)currentid=-1;
    }
    public void removeSelectedCache(){
        ModSpriteCache spriteCache = currentCache();
        caches.remove(spriteCache);
        spriteCache.dispose();
    }
    public void flush() {
    }

    public void setColor(float r, float g, float b, float a) {
        this.currentCache().setColor(r, g, b, a);
    }

    public Color getColor() {
        return this.currentCache().getColor();
    }

    public void setColor(Color tint) {
        this.currentCache().setColor(tint);
    }

    public float getPackedColor() {
        return this.currentCache().getPackedColor();
    }

    public void setPackedColor(float color) {
        this.currentCache().setPackedColor(color);
    }

    public void setProjection(Mat projection) {
        this.currentid = 0;
        this.currentCache().setProjectionMatrix(projection);
    }

    public void reserve(int amount) {
        this.offset += this.currentCache().reserve(amount);
    }

    public void beginCache(int id) {
        int cacheID = Pack.leftShort(id);
        int batch = Pack.rightShort(id);
        ModSpriteCache spriteCache = this.caches.get(batch);
        int size = spriteCache.getCaches().size;
        spriteCache.getCaches().size=cacheID;
        spriteCache.beginCache();
        spriteCache.getCaches().size=size;

//        spriteCache.getCaches().insert(cacheID,spriteCache.getCaches().pop());
//        spriteCache.beginCache(cacheID);
        this.currentid = batch;
        this.recaching = true;
    }

    public void beginCache() {
        this.currentid = this.offset / 102000;
        if (this.currentid < (this.offset + this.maxCacheSize) / 102000) {
            this.offset += this.maxCacheSize - this.offset % this.maxCacheSize + 2;
            this.currentid = this.offset / 102000;
        }

        this.currentCache().beginCache();
        this.recaching = false;
    }

    public int endCache() {
        int id = Pack.shortInt((short) this.currentCache().endCache(), (short) this.currentid);
        this.currentid = -1;
        this.recaching = false;
        return id;
    }

    protected void draw(Texture texture, float[] spriteVertices, int offset, int count) {
    }

    protected void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float rotation) {
        this.currentCache().add(region, x, y, originX, originY, width, height, 1.0F, 1.0F, rotation);
        if (!this.recaching) {
            ++this.offset;
        }

    }

    public void setShader(Shader shader, boolean apply) {
        boolean drawing = this.currentCache().isDrawing();
        if (drawing) {
            this.currentCache().end();
        }

        this.currentCache().setShader(shader);
        if (drawing) {
            this.currentCache().begin();
        }

        if (apply && shader != null) {
            shader.apply();
        }

    }

    public void dispose() {
        super.dispose();

        for (ModSpriteCache cache : this.caches) {
            cache.dispose();
        }

        this.shader.dispose();
    }

    public void beginDraw() {
        this.currentid = 0;
        this.currentCache().begin();
    }

    public void endDraw() {
        this.currentCache().end();
        this.currentid = -1;
    }

    public void drawCache(int id) {
        int cacheID = Pack.leftShort(id);
        int batch = Pack.rightShort(id);
        if (this.currentid != batch) {
            ModSpriteCache prev = this.currentCache();
            prev.end();
            this.currentid = batch;
            this.currentCache().setProjectionMatrix(prev.getProjectionMatrix());
            this.currentCache().begin();
        }

        this.currentCache().draw(cacheID);
    }
}
