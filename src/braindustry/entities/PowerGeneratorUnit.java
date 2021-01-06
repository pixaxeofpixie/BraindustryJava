package braindustry.entities;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.gen.Building;
import mindustry.gen.UnitWaterMove;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;

public class PowerGeneratorUnit extends UnitWaterMove {
    public static int classId = 41;
    public Seq<Building> links = new Seq<>();
    public UnitPowerGenerator.UnitPowerGeneratorBuild generatorBuilding;
    public UnitPowerNode.UnitPowerNodeBuild nodeBuild;
    private transient boolean initStats = false;
    public PowerGeneratorUnit() {
        super();
        this.loadStats();
    }

    public static PowerGeneratorUnit create() {
        PowerGeneratorUnit powerGeneratorUnit = new PowerGeneratorUnit();
        return powerGeneratorUnit;
    }

    public int classId() {
        return classId;
    }
    private <T extends Building,E extends Block> T createBuild(E block){
        Building building=block.buildType.get().create(block,team);
        return (T)building;
    }
    public void loadStats() {
        if (initStats || this.type == null) return;
        initStats = true;

        generatorBuilding = createBuild(((PowerUnitType) this.type).getGeneratorBlock());
        nodeBuild = createBuild(((PowerUnitType) this.type).getNodeBlock());
        generatorBuilding.setParent(this);
        nodeBuild.setParent(this);
        nodeBuild.setConnect(generatorBuilding,true);
    }

    @Override
    public void afterSync() {
        super.afterSync();
        this.loadStats();
    }

    @Override
    public void move(float cx, float cy) {
        super.move(cx, cy);
    }

    public void drawLasers(float z) {
        if (!Mathf.zero(Vars.renderer.laserOpacity)) {
            Draw.z(z);
            nodeBuild.setupColor(nodeBuild.power.graph.getSatisfaction());
            this.links.each(link -> {
                ((PowerUnitType) this.type).drawLaser(this.team, this.x, this.y, link.x, link.y, 1, link.block.size);
            });

            Draw.reset();
        }
    }
    @Override
    public void draw() {
        super.draw();
        Tile tile = this.tileOn();
        float z = this.elevation > 0.5F ? (this.type.lowAltitude ? 90.0F : 115.0F) : this.type.groundLayer + Mathf.clamp(this.type.hitSize / 4000.0F, 0.0F, 0.01F);
//        Draw.z(z + 0.1f);
        drawLasers(z+0.1f);
        /*this.links.each(link -> {
            ((PowerUnitType) this.type).drawLaser(this.team, this.x, this.y, link.x, link.y, 1, link.block.size);
        });*/
    }

    public String toString() {
        return "PowerGeneratorUnit#" + this.id;
    }

    Seq<Building> oldLinks=new Seq<>();
    public void reloadLinks() {
//        this.links=this.links.select(this::goodLink);
        oldLinks=links.copy();
        this.links.clear();
        Tile tile = this.tileOn();
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
        this.links = this.links.sort((b) -> this.dst(b)).select((link) -> {
            if (!oldLinks.contains(link)) {
                nodeBuild.setConnect(link,true);
            }
            return true;
        });
        oldLinks.each((oldLink)->{
            if (!links.contains(oldLink)){
                nodeBuild.setConnect(oldLink,false);
            }
        });
    }

    @Override
    public void update() {
        loadStats();
        super.update();
        if (this.type instanceof PowerUnitType) {
            ((PowerUnitType) type).update(this);
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

    public void resetLinks() {
        if (links == null) links = new Seq<>();
        this.links.each((b) -> {
            nodeBuild.setConnect(b,false);
//            b.power.graph.remove(this.generatorBuilding);
        });
        if (generatorBuilding != null) this.generatorBuilding.kill();
        if (nodeBuild != null) this.nodeBuild.kill();
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

    public boolean goodLink(Building link) {
        return link != null && link.isValid() && ((PowerUnitType) this.type).good.get(link, this);
    }
}
