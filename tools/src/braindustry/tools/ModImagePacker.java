package braindustry.tools;

import ModVars.modVars;
import arc.Core;
import arc.files.Fi;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.ArcNativesLoader;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import arc.util.io.PropertiesUtils;
import braindustry.core.ModContentLoader;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.ctype.UnlockableContent;
import mindustry.tools.ImagePacker;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.blocks.ConstructBlock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Writer;

public class ModImagePacker extends ImagePacker {
    static ObjectMap<String, TextureRegion> regionCache = new ObjectMap<>();
    static ObjectMap<String, BufferedImage> imageCache = new ObjectMap<>();

    public ModImagePacker() {
    }

    public static void main(String[] args) throws Exception {
        Vars.headless = true;
        modVars.packSprites=true;
        ArcNativesLoader.load();
        Log.logger = new Log.NoopLogHandler();
        Vars.content = new ModContentLoader();
        Vars.content.createBaseContent();
        Vars.content.createModContent();
        Log.logger = new Log.DefaultLogHandler();
        Fi.get("../../../assets-raw/sprites_out").walk((path) -> {
//            Log.info("path: @",path);
            if (path.extEquals("png")) {
                String fname = path.nameWithoutExtension();

                try {
                    final BufferedImage image = ImageIO.read(path.file());
                    if (image == null) {
                        throw new IOException("image " + path.absolutePath() + " is null for terrible reasons");
                    } else {
                        GenRegion region = new GenRegion(fname, path) {
                            {
                                this.width = image.getWidth();
                                this.height = image.getHeight();
                                this.u2 = this.v2 = 1.0F;
                                this.u = this.v = 0.0F;
                            }
                        };
                        regionCache.put(fname, region);
                        imageCache.put(fname, image);
                    }
                } catch (IOException var4) {
                    throw new RuntimeException(var4);
                }
            }
        });
        Seq<String> notExistNames=new Seq<>();
        Core.atlas = new TextureAtlas() {
            public AtlasRegion find(String name) {
                if (!regionCache.containsKey(name)) {
//                    if(!notExistNames.contains(name))notExistNames.add(name);
                    ((GenRegion)error).addName(name);
                    if (error!=null){
                        return error;
                    }
                    GenRegion region = new GenRegion(name, (Fi)null);
                    region.invalid = true;
                    return region;
                } else {
                    return (AtlasRegion)regionCache.get(name);
                }
            }

            public AtlasRegion find(String name, TextureRegion def) {
                return !regionCache.containsKey(name) ? (AtlasRegion)def : (AtlasRegion)regionCache.get(name);
            }

            public AtlasRegion find(String name, String def) {
                return !regionCache.containsKey(name) ? (AtlasRegion)regionCache.get(def) : (AtlasRegion)regionCache.get(name);
            }

            public boolean has(String s) {
                return regionCache.containsKey(s);
            }
        };
        ((GenRegion) regionCache.get("error")).invalid=true;
        Core.atlas.setErrorRegion("error");
        Draw.scl = 1.0F / (float)Core.atlas.find("scale_marker").width;
        Time.mark();
        Generators.generate();
        Log.info("&ly[Generator]&lc Total time to generate: &lg@&lcms", Time.elapsed());
        Log.info("&ly[Generator]&lc Total images created: &lg@", Image.total());
        Image.dispose();
        Seq<UnlockableContent> cont = Seq.withArrays(new Object[]{Vars.content.blocks(), Vars.content.items(), Vars.content.liquids(), Vars.content.units()});
        cont.removeAll((u) -> {
            return u instanceof ConstructBlock || u == Blocks.air;
        });
        notExistNames.each(name->{
            Log.warn("Region does not exist: @",name);
        });
        modVars.packSprites=false;
    }

    static String texname(UnlockableContent c) {
        if (c instanceof Block) {
            return "block-" + c.name + "-medium";
        } else {
            return c instanceof UnitType ? "unit-" + c.name + "-medium" : c.getContentType() + "-" + c.name + "-icon";
        }
    }

    static void generate(String name, Runnable run) {
        Time.mark();
        run.run();
        Log.info("&ly[Generator]&lc Time to generate &lm@&lc: &lg@&lcms", name, Time.elapsed());
    }

    static BufferedImage buf(TextureRegion region) {
        return (BufferedImage)imageCache.get(((TextureAtlas.AtlasRegion)region).name);
    }


    static Image get(String name){
        return get(Core.atlas.find(name));
    }

    static boolean has(String name) {
        return Core.atlas.has(name);
    }

    static Image get(TextureRegion region) {
        GenRegion.validate(region);
        return new Image((BufferedImage)imageCache.get(((TextureAtlas.AtlasRegion)region).name));
    }

    static void replace(String name, Image image) {
        image.save(name);
        ((GenRegion)Core.atlas.find(name)).path.delete();
    }

    static void replace(TextureRegion region, Image image) {
        replace(((GenRegion)region).name, image);
    }

    static void err(String message, Object... args) {
        Log.err(message,args);
//        throw new IllegalArgumentException(Strings.format(message, args));
    }

    static class GenRegion extends TextureAtlas.AtlasRegion {
        boolean invalid;
        String regionName="unknown";
        Fi path;
        Seq<String> notExistNames=new Seq<>();

        GenRegion(String name, Fi path) {
            if (name == null) {
                throw new IllegalArgumentException("name is null");
            } else {
                this.name = name;
                this.path = path;
            }
        }

        public boolean found() {
            return !this.invalid;
        }

        static void validate(TextureRegion region) {
            GenRegion genRegion = (GenRegion) region;
            if (genRegion.invalid) {
                Seq<String> names = genRegion.notExistNames;
                if (names.size==1){
                    err("Region does not exist: @", names.first());
                }else if(names.size==0){
//                    err("Region does not exist0: @", genRegion.name);
                }else {
                    err("Regions does not exist: @", names.toString(", "));
                }
                names.clear();
            }

        }

        public void addName(String name) {
            if (!notExistNames.contains(name))notExistNames.add(name);
        }
    }
}
