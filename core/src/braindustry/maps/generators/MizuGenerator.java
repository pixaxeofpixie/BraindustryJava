package braindustry.maps.generators;

import arc.struct.ObjectMap;
import mindustry.content.Blocks;
import mindustry.world.Block;

public class MizuGenerator extends ModPlanetGenerator {
    public MizuGenerator() {
        arr = new Block[][]{
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.craters, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water},
                {Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water}
        };
        scl=2f;
        waterOffset=0.01f;
        water=0.05f;
        dec= ObjectMap.of(
                Blocks.water, Blocks.water,
                Blocks.water, Blocks.water,
                Blocks.water, Blocks.water,
                Blocks.water, Blocks.water
        );
        tars = ObjectMap.of(
                Blocks.grass, Blocks.grass,
                Blocks.grass, Blocks.grass
        );
    }

}
