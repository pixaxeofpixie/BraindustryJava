package braindustry.maps.generators;

import arc.struct.ObjectMap;
import mindustry.content.Blocks;
import mindustry.world.Block;
import braindustry.content.ModBlocks;

public class OsorePlanetGenerator extends ModPlanetGenerator{
    public OsorePlanetGenerator(){
        arr = new Block[][]{
                {Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt},
                {Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {Blocks.slag, Blocks.dirt, Blocks.mud, Blocks.craters, Blocks.stone, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.dirt, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.mud, Blocks.slag, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.slag, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.slag, Blocks.slag},
                {Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt},
                {Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.stone, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.dirt},
                {Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.dirt}
        };
        tars = ObjectMap.of(
                Blocks.slag, Blocks.mud,
                Blocks.dirt, Blocks.mud
        );
        dec=  ObjectMap.of(
                Blocks.slag, Blocks.dirt,
                Blocks.dirt, Blocks.dirt,
                Blocks.slag, Blocks.slag,
                Blocks.slag, Blocks.slag
        );
        water = 0.05f;
        waterOffset = 0.01f;
        scl=1f;
    }
}
