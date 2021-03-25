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
        
    public OsorePlanetGenerator() {

        arr = new Block[][]{
                {ModBlocks.liquidMethaneFloor, ModBlocks.crimzesFloor, Blocks.snow, Blocks.snow, Blocks.mud, Blocks.ice, Blocks.ice, Blocks.iceSnow, ModBlocks.crimzesFloor, Blocks.mud, ModBlocks.obsidianFloor, Blocks.dirt, Blocks.dirt},
                {Blocks.dirt, ModBlocks.obsidianFloor, Blocks.mud, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, ModBlocks.obsidianFloor, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {ModBlocks.magmaFloor, Blocks.dirt, Blocks.mud, Blocks.craters, Blocks.stone, ModBlocks.crimzesFloor, Blocks.dirt, Blocks.stone, Blocks.stone, ModBlocks.obsidianFloor, ModBlocks.crimzesFloor, Blocks.dirt, Blocks.dirt},
                {ModBlocks.magmaFloor, Blocks.stone, Blocks.mud, ModBlocks.obsidianFloor, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.stone, Blocks.dirt, Blocks.dirt, ModBlocks.crimzesFloor, ModBlocks.crimzesFloor, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.obsidianFloor, Blocks.mud, Blocks.stone, Blocks.dirt, Blocks.dirt, ModBlocks.crimzesFloor, ModBlocks.crimzesFloor, Blocks.mud, Blocks.mud, Blocks.mud, ModBlocks.obsidianFloor, Blocks.dirt},
                {ModBlocks.magmaFloor, Blocks.stone, Blocks.stone, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.stone, Blocks.stone, Blocks.dirt, ModBlocks.obsidianFloor, Blocks.dirt},
                {ModBlocks.magmaFloor, ModBlocks.obsidianFloor, ModBlocks.crimzesFloor, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, ModBlocks.crimzesFloor, ModBlocks.obsidianFloor},
                {ModBlocks.magmaFloor, ModBlocks.obsidianFloor, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.stone, Blocks.mud, Blocks.stone, Blocks.stone, ModBlocks.crimzesFloor, ModBlocks.crimzesFloor, Blocks.mud, Blocks.dirt},
                {ModBlocks.magmaFloor, Blocks.mud, Blocks.mud, ModBlocks.crimzesFloor, ModBlocks.crimzesFloor, Blocks.dirt, ModBlocks.obsidianFloor, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.stone, Blocks.dirt, Blocks.dirt},
                {ModBlocks.liquidMethaneFloor, Blocks.iceSnow, Blocks.mud, Blocks.dirt, ModBlocks.obsidianFloor, Blocks.dirt, ModBlocks.obsidianFloor, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt},
                {ModBlocks.liquidMethaneFloor, Blocks.iceSnow, Blocks.mud, ModBlocks.obsidianFloor, Blocks.dirt, Blocks.dirt, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.snow, Blocks.snow, Blocks.stone},
                {ModBlocks.liquidMethaneFloor, Blocks.snow, Blocks.mud, Blocks.dirt, Blocks.snow, Blocks.mud, Blocks.stone, Blocks.snow, Blocks.stone, Blocks.dirt, Blocks.dirt, ModBlocks.obsidianFloor, Blocks.dirt},
                {ModBlocks.liquidMethaneFloor, Blocks.mud, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.mud, Blocks.dirt, Blocks.ice, Blocks.snow, Blocks.stone, ModBlocks.crimzesFloor, Blocks.stone, Blocks.dirt}
        };

        tars = ObjectMap.of(
                Blocks.mud, ModBlocks.crimzesFloor,
                Blocks.dirt, ModBlocks.obsidianFloor
        );

        dec = ObjectMap.of(
                ModBlocks.crimzesFloor, Blocks.snow,
                Blocks.dirt, ModBlocks.obsidianFloor,
                Blocks.stone, Blocks.mud,
                ModBlocks.magmaFloor, ModBlocks.liquidMethaneFloor
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
    public Block getBlock(Vec3 position){
        float height = rawHeight(position);
        Tmp.v31.set(position);
        position = Tmp.v33.set(position).scl(scl);
        float rad = scl;
        float temp = Mathf.clamp(Math.abs(position.y * 2f) / (rad));
        float tnoise = (float)noise.octaveNoise3D(7, 0.56, 1f/3f, position.x, position.y + 999f, position.z);
        temp = Mathf.lerp(temp, tnoise, 0.5f);
        height *= 1.2f;
        height = Mathf.clamp(height);

        float tar = (float)noise.octaveNoise3D(4, 0.55f, 1f/2f, position.x, position.y + 999f, position.z) * 0.3f + Tmp.v31.dst(0, 0, 1f) * 0.2f;

        Block res = arr[Mathf.clamp((int)(temp * arr.length), 0, arr[0].length - 1)][Mathf.clamp((int)(height * arr[0].length), 0, arr[0].length - 1)];
        if(tar > 0.5f){
            return tars.get(res, res);
        }else{
            return res;
        }
    }
}
