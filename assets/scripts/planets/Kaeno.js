const F = require("functions");
const simplex = new Packages.arc.util.noise.Simplex();
const rid = new Packages.arc.util.noise.RidgedPerlin(1, 2);

const KaenoGenerator = extend(PlanetGenerator, {
    getColor(position){
        var block = this.getBlock(position);

        Tmp.c1.set(block.mapColor).a = 1 - block.albedo;
        return Tmp.c1
    },
    
    getBlock(pos){
	    var height = this.rawHeight(pos);
	
	    Tmp.v31.set(pos);
	    pos = Tmp.v33.set(pos).scl(KaenoGenerator.scl);
	    var rad = KaenoGenerator.scl;
	    var temp = Mathf.clamp(Math.abs(pos.y * 2) / rad);
	    var tnoise = simplex.octaveNoise3D(7, 0.56, 1 / 3, pos.x, pos.y + 999, pos.z);
	    temp = Mathf.lerp(temp, tnoise, 0.5);
	    height *= 1.2
	    height = Mathf.clamp(height);
	
	    var tar = simplex.octaveNoise3D(4, 0.55, 0.5, pos.x, pos.y + 999, pos.z) * 0.3 + Tmp.v31.dst(0, 0, 1) * 0.2;
	    var res = KaenoGenerator.arr[
	       Mathf.clamp(Mathf.floor(temp * KaenoGenerator.arr.length), 0, KaenoGenerator.arr[0].length - 1)][ Mathf.clamp(Mathf.floor(height * KaenoGenerator.arr[0].length), 0, KaenoGenerator.arr[0].length - 1)
	    ];
	
	    if (tar > 0.5){
	        return KaenoGenerator.tars.get(res, res);
	    } else {
	        return res;
	    };
    },
    
    rawHeight(pos){
		var pos = Tmp.v33.set(pos);
		pos.scl(KaenoGenerator.scl);
		
		return (Mathf.pow(simplex.octaveNoise3D(7, 0.5, 1 / 3, pos.x, pos.y, pos.z), 2.3) + KaenoGenerator.waterOffset) / (1 + KaenoGenerator.waterOffset);  
    },
    
    getHeight(position){
        var height = this.rawHeight(position);
        return Math.max(height, KaenoGenerator.water);
    },
    
    genTile(position, tile){
        tile.floor = this.getBlock(position);
        tile.block = tile.floor.asFloor().wall;

        if(rid.getValue(position.x, position.y, position.z, 22) > 0.32){
            tile.block = Blocks.air;
        }
    }
});

KaenoGenerator.arr = [
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt],
    [Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt],
    [Blocks.slag, Blocks.dirt, Blocks.mud, Blocks.craters, Blocks.stone, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt],
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.slag, Blocks.stone, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.slag, Blocks.stone, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag],  
    [Blocks.slag, Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.dirt],  
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.stone, Blocks.dirt, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.slag, Blocks.dirt],
    [Blocks.dirt, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.mud, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.slag], 
    [Blocks.slag, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.slag], 
    [Blocks.slag, Blocks.slag, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.mud, Blocks.slag, Blocks.slag, Blocks.dirt, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.dirt],
    [Blocks.mud, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.dirt, Blocks.slag, Blocks.mud, Blocks.mud, Blocks.dirt, Blocks.stone, Blocks.dirt, Blocks.slag, Blocks.dirt]
];
KaenoGenerator.scl = 1.4;
KaenoGenerator.waterOffset = 0.01;
KaenoGenerator.basegen = new BaseGenerator();
KaenoGenerator.water = 0.05;

KaenoGenerator.dec = new ObjectMap().of(
    Blocks.slag, Blocks.dirt,
    Blocks.dirt, Blocks.dirt,
    Blocks.slag, Blocks.slag,
    Blocks.slag, Blocks.slag
);

KaenoGenerator.tars = new ObjectMap().of(
    Blocks.slag, Blocks.mud,
    Blocks.dirt, Blocks.mud
);
const KaenoPlanet = new JavaAdapter(Planet, {}, "Kaeno", Planets.sun, 3, 0.5);
KaenoPlanet.generator = KaenoGenerator;
KaenoPlanet.startSector = 25;
KaenoPlanet.hasAtmosphere = false;
KaenoPlanet.meshLoader = prov(() => new HexMesh(KaenoPlanet, 8));
KaenoPlanet.orbitRadius = 7.3

Planets.serpulo.orbitRadius = 29.8;
