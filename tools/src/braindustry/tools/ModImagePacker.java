package braindustry.tools;

import ModVars.modVars;
import arc.Core;
import arc.files.Fi;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.ArcNativesLoader;
import arc.util.Log;
import arc.util.Time;
import braindustry.core.ModContentLoader;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.tools.ImagePacker;
import mindustry.type.UnitType;
import mindustry.world.Block;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ModImagePacker extends ImagePacker {
    static ObjectMap<String, TextureRegion> regionCache = new ObjectMap<>();
    static ObjectMap<String, BufferedImage> imageCache = new ObjectMap<>();

    public ModImagePacker() {
    }

    public static void main(String[] args) throws Exception {
        Vars.headless = true;
        modVars.packSprites = true;
        ArcNativesLoader.load();
        Log.logger = new Log.NoopLogHandler();
        Vars.content = new ModContentLoader();
        Vars.content.createBaseContent();
        Vars.content.createModContent();
        Log.logger = new Log.DefaultLogHandler();
        Fi.get("../../../assets-raw/sprites").walk((path) -> {
            if (path.extEquals("png")) {
//                Log.info("path: @",path);
                String fname = path.nameWithoutExtension();
                try {
                    BufferedImage testImage = ImageIO.read(path.file());
                    if (testImage == null)
                        throw new IOException("image " + path.absolutePath() + " is null for terrible reasons");
                    Image image2 = new Image(testImage);
                    String path2 =".."+ path.absolutePath().split("../../../assets-raw/sprites")[1];

                    Fi.get(path2.replace("/"+path.name(), "")).mkdirs();
                    final Fi path2Fi = Fi.get(path2);
                    fname=path2Fi.nameWithoutExtension();
                    String saveName = path2Fi.parent().child(fname).absolutePath();
                    image2.save(saveName);
//                    Log.info("path: @ ||| path2: @ ||| @",path, );
                    final BufferedImage image = ImageIO.read(path2Fi.file());
                    if (image == null) {
                        throw new IOException("image " + path.absolutePath() + " is null for terrible reasons");
                    } else {
                        GenRegion region = new GenRegion(fname, path2Fi) {
                            {
                                this.width = image.getWidth();
                                this.height = image.getHeight();
                                this.u2 = this.v2 = 1.0F;
                                this.u = this.v = 0.0F;
                            }
                        };
                        Log.info("added: @ from: @",saveName,path2Fi.absolutePath());
                        regionCache.put(fname, region);
                        imageCache.put(fname, image);

                    }
                } catch (IOException var4) {
                    throw new RuntimeException(var4);
                }
            }
        });
        Seq<String> notExistNames = new Seq<>();
        Core.atlas = new TextureAtlas() {
            public AtlasRegion find(String name) {
                if (!regionCache.containsKey(name)) {
//                    if(!notExistNames.contains(name))notExistNames.add(name);
                    ((GenRegion) error).addName(name);
                    if (error != null) {
                        return error;
                    }
                    GenRegion region = new GenRegion(name, (Fi) null);
                    region.invalid = true;
                    return region;
                } else {
                    return (AtlasRegion) regionCache.get(name);
                }
            }

            public AtlasRegion find(String name, TextureRegion def) {
                return !regionCache.containsKey(name) ? (AtlasRegion) def : (AtlasRegion) regionCache.get(name);
            }

            public AtlasRegion find(String name, String def) {
                return !regionCache.containsKey(name) ? (AtlasRegion) regionCache.get(def) : (AtlasRegion) regionCache.get(name);
            }

            public boolean has(String s) {
                return regionCache.containsKey(s);
            }
        };
        ((GenRegion) regionCache.get("error")).invalid = true;
        Core.atlas.setErrorRegion("error");
        Draw.scl = 1.0F / (float) Core.atlas.find("scale_marker").width;
        /*
        *//*regionCache.each((name, region) -> {
        });*//*
        */
        Time.mark();
        Generators.generate();
        Log.info("&ly[Generator]&lc Total time to generate: &lg@&lcms", Time.elapsed());
        Time.mark();
        Fi.get("../../../assets-raw/sprites").walk((path) -> {
            if (path.extEquals("png") && !path.name().endsWith("-outline")) {
                String fname = path.nameWithoutExtension();
                try {
                    BufferedImage testImage = ImageIO.read(path.file());
                    if (testImage == null)
                        throw new IOException("image " + path.absolutePath() + " is null for terrible reasons");
                    Image image2 = new Image(testImage);
                    String path2 =".."+ path.absolutePath().split("../../../assets-raw/sprites")[1];

                    Fi.get(path2.replace("/"+path.name(), "")).mkdirs();
                    final Fi path2Fi = Fi.get(path2);
                    fname=path2Fi.nameWithoutExtension();
                    String saveName = path2Fi.parent().child(fname).absolutePath();
                    image2.save(saveName,false);
                    final BufferedImage image = ImageIO.read(path2Fi.file());
                    if (image == null) {
                        throw new IOException("image " + path.absolutePath() + " is null for terrible reasons");
                    }
                } catch (IOException var4) {
                    throw new RuntimeException(var4);
                }
            }
        });
        Log.info("&ly[Copy]&lc Total time to copy: &lg@", Time.elapsed());
        Log.info("&ly[Generator]&lc Total images created: &lg@", Image.total());
        Log.info("&ly[Disposing]&lc Start");
        Time.mark();
        Image.dispose();
        Log.info("&ly[Disposing]&lc Total time: @",Time.elapsed());
//        notExistNames.each(name->{
//            Log.warn("Region does not exist: @",name);
//        });
        modVars.packSprites = false;
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
        Log.info("&ly[Generator]&lc Start &lm@&lc", name);
        run.run();
        Log.info("&ly[Generator]&lc Time to generate &lm@&lc: &lg@&lcms", name, Time.elapsed());
    }

    static BufferedImage buf(TextureRegion region) {
        return (BufferedImage) imageCache.get(((TextureAtlas.AtlasRegion) region).name);
    }


    static Image get(String name) {
        return get(Core.atlas.find(name));
    }

    static boolean has(String name) {
        return Core.atlas.has(name);
    }

    static Image get(TextureRegion region) {
        GenRegion.validate(region);
        return new Image((BufferedImage) imageCache.get(((TextureAtlas.AtlasRegion) region).name));
    }

    static void replace(String name, Image image) {
        image.save(name);
        ((GenRegion) Core.atlas.find(name)).path.delete();
    }

    static void replace(TextureRegion region, Image image) {
        replace(((GenRegion) region).name, image);
    }

    static void err(String message, Object... args) {
        Log.err(message, args);
//        throw new IllegalArgumentException(Strings.format(message, args));
    }

    static class GenRegion extends TextureAtlas.AtlasRegion {
        boolean invalid;
        String regionName = "unknown";
        Fi path;
        Seq<String> notExistNames = new Seq<>();

        GenRegion(String name, Fi path) {
            if (name == null) {
                throw new IllegalArgumentException("name is null");
            } else {
                this.name = name;
                this.path = path;
            }
        }

        static void validate(TextureRegion region) {
            GenRegion genRegion = (GenRegion) region;
            if (genRegion.invalid) {
                Seq<String> names = genRegion.notExistNames;
                if (names.size == 1) {
                    err("Region does not exist: @", names.first());
                } else if (names.size == 0) {
//                    err("Region does not exist0: @", genRegion.name);
                } else {
                    err("Regions does not exist: @", names.toString(", "));
                }
                names.clear();
            }

        }

        public boolean found() {
            return !this.invalid;
        }

        public void addName(String name) {
            if (!notExistNames.contains(name)) notExistNames.add(name);
        }
    }
}
