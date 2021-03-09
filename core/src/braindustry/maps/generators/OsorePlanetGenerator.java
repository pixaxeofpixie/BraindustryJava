package braindustry.maps.generators;

import arc.struct.ObjectMap;
import mindustry.content.Blocks;
import mindustry.world.Block;
import braindustry.content.Blocks.ModBlocks;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.ai.*;
import mindustry.ai.BaseRegistry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;

public class OsorePlanetGenerator extends ModPlanetGenerator{
        
    public OsorePlanetGenerator(){
        arr = new Block[][]{
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, ModBlocks.magmaFloor, Blocks.dirt, Blocks.dirt},
                {Blocks.dirt, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, ModBlocks.magmaFloor, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {ModBlocks.magmaFloor, Blocks.dirt, Blocks.mud, Blocks.craters, Blocks.stone, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.dirt, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, ModBlocks.magmaFloor, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, ModBlocks.magmaFloor, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, ModBlocks.magmaFloor, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, ModBlocks.magmaFloor, ModBlocks.magmaFloor},
                {ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, ModBlocks.magmaFloor, Blocks.dirt, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt},
                {Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.dirt, ModBlocks.magmaFloor, Blocks.dirt, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.dirt, ModBlocks.magmaFloor, ModBlocks.magmaFloor, ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.magmaFloor, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.stone, ModBlocks.magmaFloor, Blocks.dirt, Blocks.dirt, Blocks.dirt, ModBlocks.magmaFloor, Blocks.dirt},
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
