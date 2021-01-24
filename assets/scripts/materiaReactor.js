const F = require("func");
const ElectricColor = Color.valueOf("C6DBFF09");
const ElectricColorMulled = Color.valueOf("C6DBFF18");
const Distance = Core.camera.width + Core.camera.height + 50*Vars.tilesize;

const ElectricExplosionPart1 = new Effect(20, Distance, e => {
    Draw.color(Color.white, ElectricColor, e.fin());
    Lines.stroke(e.fout() * 10.0 + 0.5);
    Lines.circle(e.x, e.y, e.fin() * 250.0);
});

const ElectricExplosionPart2 = new Effect(120, Distance, e => {
    Draw.color(ElectricColor, ElectricColorMulled, e.fslope());
    Draw.alpha(e.fout()/120.0);
    Draw.rect("collos-flash", e.x, e.y, 150.0 + 400.0 * e.fin(), 150.0 + 400.0 * e.fin());
    Draw.color();
});

const ElectricExplosionPart3 = new Effect(120, Distance, e => {
    Draw.color(ElectricColor, ElectricColorMulled, e.fslope());
    Draw.alpha(e.fout()/120);
    Draw.rect("collos-FlareWhite", e.x, e.y, 350 + 400 * e.fin(), 350 + 400 * e.fin());
    Draw.color();
});

const MaterialReactor = extendContent(ItemLiquidGenerator, "materia-reactor", {
	setBars(){
	    this.super$setBars();

	    this.bars.add("efficiency",
            func(e =>
		        new Bar(
	                prov(() => Core.bundle.get("efficiency")+": "+e.getEfficiency()),
			        prov(() => Tmp.c1.set(Color.valueOf("#FFE679")).lerp(Color.valueOf("#78FFFD"), Mathf.sin(Time.time*0.03)*e.getEfficiency()/2+0.5)),
			        floatp(() => e.getEfficiency()/10)
	            )
	        )
        )
    }
});

MaterialReactor.buildType = prov(() => {
	const ent = extendContent(ItemLiquidGenerator.ItemLiquidGeneratorBuild, MaterialReactor, {
		init(tile, team, shouldAdd, rotation){
			if(!this.initialized) this.create(tile.block(), team);

			this.rotation = rotation;
			this.tile = tile;
			this.set(tile.drawx(), tile.drawy());
			if(shouldAdd) this.add();
			this.created();

            this.setItem(null); 
            this.setLiquid(null);
            this.setWork(false);
			this.setEfficiency(1);

			return this;
		},

		buildConfiguration(table){
			var slider = table.slider(0, 10, 1, this.getEfficiency(), null).width(240).color(Color.valueOf("FFFFFF")).get();
			slider.changed(run(() => {
		        this.setEfficiency(slider.getValue().toFixed(1));
			}));
		},

	    draw(){
            this.super$draw();
  
	        Draw.blend(Blending.additive);
	        Draw.color(Color.valueOf("#1F74F3"));
            Draw.alpha(0.5 + Mathf.sin(Time.time * this.getEfficiency() * 0.15) * 0.5);
            Draw.rect(Core.atlas.find("collos-materia-reactor-lights"), this.x, this.y);
            Draw.alpha(1.0);
	        Draw.color();
	        Draw.blend();
		},

	    updateTile(){
	        var item = this.getItem();
	        var liquid = this.getLiquid();
	
		    if(this.items.total() == 0) this.setItem(null);
		    if(this.liquids.total() == 0) this.setLiquid(null);
		    if(this.items.total() == 0 && this.liquids.total() == 0) this.setWork(false);
	
	        if(item != null && liquid != null && this.getWork() != false) {
	            var num = this.getEfficiency() != 0 ? this.getEfficiency() : 1;
	            var time = 60/num;
	
		        if(this.timer.get(time)) {
	                Lightning.create(Team.get(99), Color.valueOf("#00FFF9"), Mathf.pow(3, Mathf.round(this.getEfficiency()/2)), this.x + Mathf.range(2.5)*Vars.tilesize, this.y + Mathf.range(2.5)*Vars.tilesize, Mathf.random(0.0, 360.0), 3+Mathf.pow(3, Mathf.round(this.getEfficiency()/4)));
	
	                this.liquids.remove(liquid, Mathf.round(Mathf.clamp(3, 0, this.liquids.total())));
	                this.items.remove(item, Mathf.round(Mathf.clamp(1, 0, this.items.total())));
			    };
		    }
	    }, 

	    onDestroyed() {
	        if(this.getWork() == false) return;
	
	        ElectricExplosionPart1.at(this.x, this.y);
	        ElectricExplosionPart1.at(this.x, this.y);
	        ElectricExplosionPart1.at(this.x, this.y);
	
	        for(var x = 0; x < 30; x++){
	            Lightning.create(Team.get(99), Color.valueOf("#00FFF9"), Mathf.pow(4, Mathf.round(this.getEfficiency())), this.x + Mathf.range(2.5)*Vars.tilesize, this.y + Mathf.range(2.5)*Vars.tilesize, Mathf.random(0.0, 360.0), 35.0 + Mathf.range(15.0));
		    };
	
	        for(var x = 0; x < 40; x++){
	            var v = new Vec2();
	            v.trns(Mathf.random(360.0), Mathf.random(100.0));
	         
	            var tile = Vars.world.tileWorld(this.x + v.x, this.y + v.y)
				Puddles.deposit(tile, Vars.world.tileWorld(this.x, this.y), this.getLiquid(), this.getEfficiency()*10); 
		    }
	    }, 

	    getPowerProduction(){
	        var item = this.getItem();
	        var liquid = this.getLiquid();
	
	        if(item != null && liquid != null) {
	            this.setWork(true);
			    return item.hardness * item.cost * 10.0 * liquid.temperature * this.getEfficiency();
		    } else {
	            this.setWork(false);
			    return 0;
		    }
	    }, 
	
	    acceptItem(source, item){
		    if(this == null || this.items == null) return false;
		    if(this.items.get(item) >= this.block.itemCapacity) return false;
		    if(this.items.total() >= this.block.itemCapacity) return false;

		    if(item.hardness != null && item.cost != null) {
			    if(item.hardness >= 0.5 && item.cost >= 0.5) {
			        if(this.getItem() != null && this.getItem() != item) {
		                this.items.remove(this.getItem(), this.items.total());
					};
			        this.setItem(item);
					return true
				}
			} else {
	            return false
			}
		}, 
	
	    acceptLiquid(source, liquid){
		    if(this == null || this.liquids == null) return false;
		    if(this.liquids.get(liquid) >= this.block.liquidCapacity) return false;
		    if(this.liquids.total() >= this.block.liquidCapacity) return false;

		    if(liquid.temperature != null && liquid.heatCapacity != null) {
			    if(liquid.temperature >= 0.0 && liquid.heatCapacity >= 0.0) {
			        if(this.getLiquid() != null && this.getLiquid() != liquid) {
		                this.liquids.remove(this.getLiquid(), this.liquids.total());
					};
			        this.setLiquid(liquid);
					return true
				}
			} else {
	            return false 
			}
		}, 

		writeBase(write){
			this.super$writeBase(write);

			write.i(this._item != null ? this._item.id : -1);
			write.i(this._efficiency);
			write.i(this._liquid != null ? this._liquid.id : -1 );
			write.bool(this._work);
		},

		readBase(read){
			this.super$readBase(read);
  
            var itemid = read.i()
			this._item = itemid != -1 ? Vars.content.getByID(ContentType.item, itemid) : null;
			this._efficiency = read.i();
			
            var liquidid = read.i()
			this._liquid = liquidid != -1 ? Vars.content.getByID(ContentType.liquid, liquidid) : null;
			this._work = read.bool();
		},

		getItem(){
			return this._item;
		},
		
		setItem(a){
			this._item = a;
		},

		getLiquid(){
			return this._liquid;
		},
		
		setLiquid(a){
			this._liquid = a;
		}, 

		getWork(){
			return this._work;
		},
		
		setWork(a){
			this._work = a;
		}, 

		getEfficiency(){
			return this._efficiency;
		},
		
		setEfficiency(a){
			this._efficiency = a;
		} 
	});

	return ent;
});
MaterialReactor.itemCapacity = 150;
MaterialReactor.liquidCapacity = 150;
MaterialReactor.configurable = true;
MaterialReactor.hasPower = true;
MaterialReactor.hasItems = true;
MaterialReactor.hasLiquids = true;
MaterialReactor.outputsPower = true;
MaterialReactor.size = 7;
MaterialReactor.health = 8250;
MaterialReactor.category = Category.power;
MaterialReactor.buildVisibility = BuildVisibility.shown;
MaterialReactor.requirements = ItemStack.with(Items.silicon, 350, F.fi("cutol"), 400, F.fi("orbon"), 300, Items.plastanium, 400, Items.surgeAlloy, 300, Items.graphite, 375);

F.techNode(Blocks.impactReactor, MaterialReactor, ItemStack.with(Items.silicon, 40000, F.fi("cutol"), 24000, F.fi("orbon"), 18000, Items.plastanium, 22500, Items.surgeAlloy, 25000, Items.graphite, 30000));
