package braindustry.entities;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.content.ModBlocks;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;

import java.nio.FloatBuffer;

import static braindustry.modVars.modFunc.print;

public class PowerGeneratorUnit extends AmmoDistributeUnit {
    public static final int classId = 41;
    public Seq<Building> links=new Seq<>();
    public Seq<PowerGraph> graphs=new Seq<>();
    public UnitPowerGenerator.UnitPowerGeneratorBuild generatorBuilding;
    private transient boolean initStats =false;

    public static PowerGeneratorUnit create() {
        PowerGeneratorUnit powerGeneratorUnit=new PowerGeneratorUnit();
        return powerGeneratorUnit;
    }
    public void loadStats(){
        if (initStats || this.type==null)return;
        initStats =true;

            generatorBuilding= (UnitPowerGenerator.UnitPowerGeneratorBuild)((PowerUnitType)this.type).getGeneratorBlock().buildType.get();
            generatorBuilding=(UnitPowerGenerator.UnitPowerGeneratorBuild)generatorBuilding.create(((PowerUnitType)this.type).getGeneratorBlock(),this.team);
            generatorBuilding.setParent(this);
    }

    @Override
    public void afterSync() {
        super.afterSync();
        this.loadStats();
    }

    public PowerGeneratorUnit(){
        super();
        this.loadStats();
    }

    @Override
    public void move(float cx, float cy) {
        super.move(cx, cy);    }

    @Override
    public void draw() {
        super.draw();
        Tile tile=this.tileOn();
        float z = this.elevation > 0.5F ? (this.type.lowAltitude ? 90.0F : 115.0F) : this.type.groundLayer + Mathf.clamp(this.type.hitSize / 4000.0F, 0.0F, 0.01F);
        Draw.z(z-0.1f);
        this.links.each(link->{
            ((PowerUnitType)this.type).drawLaser(this.team,this.x,this.y,link.x,link.y,1,link.block.size);
        });
    }

    public String toString() {
        return "PowerGeneratorUnit#" + this.id;
    }
    public void reloadLinks(){
//        this.links=this.links.select(this::goodLink);
        this.links.clear();
        graphs.each((g)->g.remove(generatorBuilding));
        this.graphs.clear();
        Tile tile=this.tileOn();
        for (int x = (int) ((float) tile.x - this.type.range); (float) x <= (float) tile.x + this.type.range; ++x) {
            for (int y = (int) ((float) tile.y - this.type.range); (float) y <= (float) tile.y + this.type.range; ++y) {
                Building link = Vars.world.build(x, y);
                boolean linked = goodLink(link);
                if (linked && !this.links.contains(link)) {
                    this.links.add(link);
                }

            }
        }
        sortLinks();
    }

    private void sortLinks() {
        this.links=this.links.sort((b)->this.dst(b)).select((link)->{
            if (!graphs.contains(link.power.graph)) {
                graphs.add(link.power.graph);
                link.power.graph.add(generatorBuilding);
                return true;
            }
            return false;
        });
    }

    @Override
    public void update() {
        loadStats();
        super.update();
        if (this.type instanceof PowerUnitType){
            ((PowerUnitType)type).update(this);
        }
    }

    @Override
    public void write(Writes write) {
        super.write(write);
    }

    @Override
    public void read(Reads read) {
        super.read(read);
    }

    public boolean serialize() {
        return true;
    }

    public int classId() {
        return 41;
    }
    public void resetLinks(){
        if (links==null)links=new Seq<>();
        this.links.each((b)->{
            b.power.graph.remove(this.generatorBuilding);
        });
        if (generatorBuilding!=null) this.generatorBuilding.kill();
    }

    @Override
    public void clearCommand() {
        resetLinks();
        super.clearCommand();
    }

    @Override
    public void kill() {
        resetLinks();
        super.kill();
    }

    @Override
    public void remove() {
        resetLinks();
        super.remove();
    }
    public boolean validLink(Building link){
        return this.links.contains(link);
    }
    public boolean goodLink(Building link) {
        return link!=null && link.isValid() && ((PowerUnitType)this.type).good.get(link,this);
    }
}
