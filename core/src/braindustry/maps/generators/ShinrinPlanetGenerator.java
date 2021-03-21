package braindustry.maps.generators;

import arc.struct.ObjectMap;
import braindustry.world.ModBlock;
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

public class ShinrinPlanetGenerator extends ModPlanetGenerator{

    public ShinrinPlanetGenerator() {

        arr = new Block[][]{
                {ModBlocks.swampWater, ModBlocks.blackIce, ModBlocks.blackSnow, ModBlocks.blackSnow, ModBlocks.darkShrubsFloor, ModBlocks.blackIce, ModBlocks.blackIce, Blocks.iceSnow, ModBlocks.darkShrubsFloor, ModBlocks.darkShrubsFloor, Blocks.grass, Blocks.dirt, Blocks.dirt},
                {Blocks.water, Blocks.snow, ModBlocks.darkShrubsFloor, Blocks.snow, Blocks.stone, Blocks.grass, ModBlocks.darkShrubsFloor, Blocks.snow, ModBlocks.blackSnow, ModBlocks.blackIce, Blocks.grass, ModBlocks.darkShrubsFloor, Blocks.stone},
                {ModBlocks.swampWater, Blocks.dirt, Blocks.mud, Blocks.grass, Blocks.stone, ModBlocks.darkShrubsFloor, ModBlocks.jungleFloor, Blocks.stone, Blocks.stone, ModBlocks.darkShrubsFloor, Blocks.grass, Blocks.dirt, Blocks.stone},
                {ModBlocks.swampWater, Blocks.stone, Blocks.grass, ModBlocks.blackIce, Blocks.stone, Blocks.mud, ModBlocks.darkShrubsFloor, Blocks.dirt, Blocks.dirt, Blocks.dirt, ModBlocks.jungleFloor, ModBlocks.blackSnow, Blocks.stone},
                {ModBlocks.swampSandWater, ModBlocks.jungleFloor, Blocks.grass, Blocks.stone, Blocks.dirt, ModBlocks.jungleFloor, ModBlocks.crimzesFloor, ModBlocks.jungleFloor, Blocks.mud, Blocks.grass, Blocks.stone, ModBlocks.jungleFloor, Blocks.dirt},
                {ModBlocks.swampWater, Blocks.grass, Blocks.grass, ModBlocks.jungleFloor, Blocks.mud, Blocks.mud, ModBlocks.jungleFloor, Blocks.stone, ModBlocks.jungleFloor, Blocks.stone, ModBlocks.jungleFloor, Blocks.grass, Blocks.dirt},
                {ModBlocks.swampSandWater, Blocks.stone, ModBlocks.jungleFloor, Blocks.stone, ModBlocks.jungleFloor, Blocks.grass, Blocks.mud, Blocks.stone, Blocks.dacite, Blocks.dirt, ModBlocks.jungleFloor, ModBlocks.darkShrubsFloor, ModBlocks.darkShrubsFloor},
                {ModBlocks.swampWater, Blocks.dacite, Blocks.grass, ModBlocks.jungleFloor, Blocks.dirt, Blocks.dirt, Blocks.stone, ModBlocks.darkShrubsFloor, Blocks.stone, ModBlocks.jungleFloor, ModBlocks.jungleFloor, Blocks.mud, Blocks.dacite},
                {ModBlocks.swampWater, Blocks.dacite, Blocks.stone, ModBlocks.darkShrubsFloor, ModBlocks.darkShrubsFloor, Blocks.dirt, ModBlocks.jungleFloor, Blocks.snow, Blocks.mud, Blocks.stone, Blocks.stone, Blocks.grass, Blocks.dirt},
                {Blocks.water, Blocks.iceSnow, Blocks.mud, Blocks.grass, ModBlocks.blackIce, Blocks.grass, ModBlocks.darkShrubsFloor, Blocks.stone, Blocks.dacite, Blocks.mud, ModBlocks.blackSnow, Blocks.dirt, Blocks.dirt},
                {Blocks.water, Blocks.sandWater, Blocks.mud, ModBlocks.obsidianFloor, Blocks.dirt, Blocks.dirt, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.snow, Blocks.snow, Blocks.stone},
                {Blocks.sandWater, Blocks.dacite, Blocks.mud, Blocks.dirt, Blocks.snow, ModBlocks.darkShrubsFloor, Blocks.stone, ModBlocks.blackSnow, Blocks.dacite, Blocks.dirt, ModBlocks.darkShrubsFloor, ModBlocks.blackSnow, Blocks.dirt},
                {ModBlocks.swampWater, ModBlocks.darkShrubsFloor, ModBlocks.blackSnow, ModBlocks.darkShrubsFloor, Blocks.snow, ModBlocks.blackSnow, ModBlocks.blackIce, Blocks.ice, Blocks.snow, Blocks.stone, ModBlocks.blackSnow, Blocks.stone, ModBlocks.darkShrubsFloor}
        };

        tars = ObjectMap.of(
                ModBlocks.jungleFloor, Blocks.grass,
                Blocks.stone, Blocks.stone
        );

        dec = ObjectMap.of(
                ModBlocks.blackIce, ModBlocks.blackSnow,
                ModBlocks.darkShrubsFloor, Blocks.stone,
                ModBlocks.swampWater, ModBlocks.swampSandWater,
                Blocks.water, ModBlocks.swampSandWater
        );

        water = 0.05f;
        waterOffset = 0.04f;
        scl = 4.5f;

    }
    @Override

    public void generateSector(Sector sector){

        //these always have bases

        if(sector.id == 154 || sector.id == 0){
            sector.generateEnemyBase = true;
            return;
        }

        Ptile tile = sector.tile;

        boolean any = false;
        float poles = Math.abs(tile.v.y);
        float noise = Noise.snoise3(tile.v.x, tile.v.y, tile.v.z, 0.001f, 0.58f);

        if(noise + poles/7.1 > 0.12 && poles > 0.23){
            any = true;
        }

        if(noise < 0.16){
            for(Ptile other : tile.tiles){
                Sector osec = sector.planet.getSector(other);

                //Enemy base doesnt spawn in start sector and other maps

                if(osec.id == sector.planet.startSector || osec.generateEnemyBase && poles < 0.85 || (sector.preset != null && noise < 0.11)){
                    return;
                }
            }
        }

        sector.generateEnemyBase = any;
    }

    @Override
    public float getHeight(Vec3 position){
        float height = rawHeight(position);
        return Math.max(height, water);
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = getBlock(position);
        tile.block = tile.floor.asFloor().wall;

        //if(noise.octaveNoise3D(5, 0.6, 8.0, position.x, position.y, position.z) > 0.65){
        //    tile.block = Blocks.air;
        //}

        if(rid.getValue(position.x, position.y, position.z, 22) > 0.32){
            tile.block = Blocks.air;
        }

    }
}
