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
            
        dec = ObjectMap.of(
                Blocks.slag, Blocks.dirt,
                Blocks.dirt, Blocks.dirt,
                Blocks.slag, Blocks.slag,
                Blocks.slag, Blocks.slag
        );
            
        water = 0.05f;
        waterOffset = 0.01f;
        scl = 1.3f;
        
        
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
                var osec = sector.planet.getSector(other);

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
    public Color getColor(Vec3 position){
        Block block = getBlock(position);
        //replace salt with sand color
        if(block == Blocks.salt) return Blocks.sand.mapColor;
        return Tmp.c1.set(block.mapColor).a(1f - block.albedo);
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = getBlock(position);
        tile.block = tile.floor.asFloor().wall;

        //if(noise.octaveNoise3D(5, 0.6, 8.0, position.x, position.y, position.z) > 0.65){
            //tile.block = Blocks.air;
        //}

        if(rid.getValue(position.x, position.y, position.z, 22) > 0.32){
            tile.block = Blocks.air;
        }
    }
    }
}
