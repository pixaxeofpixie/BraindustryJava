const F = require("functions");

//Зетсубо
const zetsubo = new JavaAdapter(Planet, {}, "zetsubo", Planets.sun, 3, 2.4);
zetsubo.hasAtmosphere = false;
zetsubo.meshLoader = () => new SunMesh(zetsubo, 4, 5, 0.3, 1.7, 1.2, 1, 1.5, Color.valueOf("9FFCFFFF"), Color.valueOf("50DDE2FF"), Color.valueOf("4749C9FF"));
zetsubo.orbitRadius = 55.7;
zetsubo.accessible = false;
zetsubo.bloom = true;

//Каено
const simplex = new Packages.arc.util.noise.Simplex();
const rid = new Packages.arc.util.noise.RidgedPerlin(1, 2);

const OsoreGenerator = extend(PlanetGenerator, {
    getColor(position){
        var block = this.getBlock(position);

        Tmp.c1.set(block.mapColor).a = 1 - block.albedo;
        return Tmp.c1
    },
    
    getBlock(pos){
	    var height = this.rawHeight(pos);
	
	    Tmp.v31.set(pos);
	    pos = Tmp.v33.set(pos).scl(OsoreGenerator.scl);
	    var rad = OsoreGenerator.scl;
	    var temp = Mathf.clamp(Math.abs(pos.y * 2) / rad);
	    var tnoise = simplex.octaveNoise3D(7, 0.56, 1 / 3, pos.x, pos.y + 999, pos.z);
	    temp = Mathf.lerp(temp, tnoise, 0.5);
	    height *= 0.9
	    height = Mathf.clamp(height);
	
	    var tar = simplex.octaveNoise3D(4, 0.55, 0.5, pos.x, pos.y + 999, pos.z) * 0.3 + Tmp.v31.dst(0, 0, 1) * 0.2;
	    var res = OsoreGenerator.arr[
	       Mathf.clamp(Mathf.floor(temp * OsoreGenerator.arr.length), 0, OsoreGenerator.arr[0].length - 1)][ Mathf.clamp(Mathf.floor(height * OsoreGenerator.arr[0].length), 0, OsoreGenerator.arr[0].length - 1)
	    ];
	
	    if (tar > 0.5){
	        return OsoreGenerator.tars.get(res, res);
	    } else {
	        return res;
	    };
    },
    
    rawHeight(pos){
		var pos = Tmp.v33.set(pos);
		pos.scl(OsoreGenerator.scl);
		
		return (Mathf.pow(simplex.octaveNoise3D(7, 0.5, 1 / 3, pos.x, pos.y, pos.z), 2.3) + OsoreGenerator.waterOffset) / (1 + OsoreGenerator.waterOffset);  
    },
    
    getHeight(position){
        var height = this.rawHeight(position);
        return Math.max(height, OsoreGenerator.water);
    },
    
    genTile(position, tile){
        tile.floor = this.getBlock(position);
        tile.block = tile.floor.asFloor().wall;

        if(rid.getValue(position.x, position.y, position.z, 22) > 0.32){
            tile.block = Blocks.air;
        }
    }
});

OsoreGenerator.arr = [
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt],
    [Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt],
    [Blocks.slag, Blocks.dirt, Blocks.mud, Blocks.craters, Blocks.stone, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.dirt, Blocks.dirt],
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.slag, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.slag, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.slag, Blocks.slag],  
    [Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt],
    [Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt], 
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt], 
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.stone, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.dirt],
    [Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.dirt]
];
OsoreGenerator.scl = 1.0;
OsoreGenerator.waterOffset = 0.01;
OsoreGenerator.basegen = new BaseGenerator();
OsoreGenerator.water = 0.05;

OsoreGenerator.dec = new ObjectMap().of(
    Blocks.slag, Blocks.dirt,
    Blocks.dirt, Blocks.dirt,
    Blocks.slag, Blocks.slag,
    Blocks.slag, Blocks.slag
);

OsoreGenerator.tars = new ObjectMap().of(
    Blocks.slag, Blocks.mud,
    Blocks.dirt, Blocks.mud
);
const OsorePlanet = new JavaAdapter(Planet, {}, "Osore", zetsubo, 3, 0.7);
OsorePlanet.generator = OsoreGenerator;
OsorePlanet.startSector = 25;
OsorePlanet.hasAtmosphere = false;
OsorePlanet.meshLoader = prov(() => new HexMesh(OsorePlanet, 8));
OsorePlanet.orbitRadius = 11.2;
OsorePlanet.rotateTime = Infinity; //НИКОГДА
OsorePlanet.orbitTime = Mathf.pow((2.0 + 14.0 + 0.66), 1.5) * 80;
OsorePlanet.accessible = true;

//Мизу
const MizuGenerator = extend(PlanetGenerator, {
    getColor(position){
        var block = this.getBlock(position);

        Tmp.c1.set(block.mapColor).a = 1 - block.albedo;
        return Tmp.c1
    },
    
    getBlock(pos){
	    var height = this.rawHeight(pos);
	
	    Tmp.v31.set(pos);
	    pos = Tmp.v33.set(pos).scl(MizuGenerator.scl);
	    var rad = MizuGenerator.scl;
	    var temp = Mathf.clamp(Math.abs(pos.y * 2) / rad);
	    var tnoise = simplex.octaveNoise3D(7, 0.56, 1 / 3, pos.x, pos.y + 999, pos.z);
	    temp = Mathf.lerp(temp, tnoise, 0.5);
	    height *= 1.2
	    height = Mathf.clamp(height);
	
	    var tar = simplex.octaveNoise3D(4, 0.55, 0.5, pos.x, pos.y + 999, pos.z) * 0.3 + Tmp.v31.dst(0, 0, 1) * 0.2;
	    var res = MizuGenerator.arr[
	       Mathf.clamp(Mathf.floor(temp * MizuGenerator.arr.length), 0, MizuGenerator.arr[0].length - 1)][ Mathf.clamp(Mathf.floor(height * MizuGenerator.arr[0].length), 0, MizuGenerator.arr[0].length - 1)
	    ];
	
	    if (tar > 0.5){
	        return MizuGenerator.tars.get(res, res);
	    } else {
	        return res;
	    };
    },
    
    rawHeight(pos){
		var pos = Tmp.v33.set(pos);
		pos.scl(MizuGenerator.scl);
		
		return (Mathf.pow(simplex.octaveNoise3D(7, 0.5, 1 / 3, pos.x, pos.y, pos.z), 2.3) + MizuGenerator.waterOffset) / (1 + MizuGenerator.waterOffset);  
    },
    
    getHeight(position){
        var height = this.rawHeight(position);
        return Math.max(height, MizuGenerator.water);
    },
    
    genTile(position, tile){
        tile.floor = this.getBlock(position);
        tile.block = tile.floor.asFloor().wall;

        if(rid.getValue(position.x, position.y, position.z, 22) > 0.32){
            tile.block = Blocks.air;
        }
    }
});

MizuGenerator.arr = [
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],
    [Blocks.water, Blocks.water, Blocks.water, Blocks.craters, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],  
    [Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],  
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],  
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],  
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],  
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water], 
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water], 
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water],
    [Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.water, Blocks.grass, Blocks.water, Blocks.water, Blocks.water]
];
MizuGenerator.scl = 2.0;
MizuGenerator.waterOffset = 0.01;
MizuGenerator.basegen = new BaseGenerator();
MizuGenerator.water = 0.05;

MizuGenerator.dec = new ObjectMap().of(
    Blocks.water, Blocks.water,
    Blocks.water, Blocks.water,
    Blocks.water, Blocks.water,
    Blocks.water, Blocks.water
);

MizuGenerator.tars = new ObjectMap().of(
    Blocks.grass, Blocks.grass,
    Blocks.grass, Blocks.grass
);
const MizuPlanet = new JavaAdapter(Planet, {}, "Mizu", zetsubo, 3, 1);
MizuPlanet.generator = MizuGenerator;
MizuPlanet.startSector = 25;
MizuPlanet.hasAtmosphere = false;
MizuPlanet.meshLoader = prov(() => new HexMesh(MizuPlanet, 8));
MizuPlanet.orbitRadius = 21.7;
MizuPlanet.rotateTime = 24;
MizuPlanet.orbitTime = Mathf.pow((2.0 + 14.0 + 0.66), 1.5) * 80;
MizuPlanet.accessible = true;

//Сектора
//const TPolus = new SectorPreset("thermal Polus", OsorePlanet, 72);
//TPolus.alwaysUnlocked = true;
//TPolus.difficulty = 30;
//TPolus.captureWave = 30;

const Mpoint = new SectorPreset("melting_point", OsorePlanet, 20);
Mpoint.alwaysUnlocked = true;
Mpoint.difficulty = 6;
Mpoint.captureWave = 20;

//const FFOf = new SectorPreset("451F", OsorePlanet, 50);
/FFOf.alwaysUnlocked = true;
//FFOf.difficulty = 8;
//FFOf.captureWave = 35;

//while(interplanetaryAccelerator.unlocked()){
//  OsorePlanet.accessible = true;
//};
