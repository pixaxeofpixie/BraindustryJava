package braindustry.type;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;

public class PowerUnitContainer {
    public final Unit unit;
    public Seq<Building> links = new Seq<>();
    private transient boolean initStats = false;
    public UnitPowerGenerator.UnitPowerGeneratorBuild generatorBuilding;
    public UnitPowerNode.UnitPowerNodeBuild nodeBuild;
    Seq<Building> oldLinks=new Seq<>();

    private <T extends Building,E extends Block> T createBuild(E block){
        Building building=block.buildType.get().create(block,unit.team);
        return (T)building;
    }
    public PowerUnitContainer( Unit unit) {
       this. unit = unit;
    }

    public void resetLinks() {
        if (links == null) links = new Seq<>();
        links.each((b) -> {
            nodeBuild.setConnect(b,false);
//            b.power.graph.remove(unit.generatorBuilding);
        });
        if (generatorBuilding != null) generatorBuilding.kill();
        if (nodeBuild != null) nodeBuild.kill();
    }
    private void sortLinks() {
        links = links.sort((b) -> unit.dst(b)).select((link) -> {
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
    public void draw(){

        float z = unit.elevation > 0.5F ? (unit.type.lowAltitude ? 90.0F : 115.0F) : unit.type.groundLayer + Mathf.clamp(unit.type.hitSize / 4000.0F, 0.0F, 0.01F);
        drawLasers(z+0.1f);
    }
    public void drawLasers(float z) {
        if (!Mathf.zero(Vars.renderer.laserOpacity)) {
            Draw.z(z);
            nodeBuild.setupColor(nodeBuild.power.graph.getSatisfaction());
            links.each(link -> {
                ((PowerUnitType) unit.type).drawLaser(unit.team, unit.x, unit.y, link.x, link.y, 1, link.block.size);
            });

            Draw.reset();
        }
    }
    public void update(){
        loadStats();
        reloadLinks();
        links.each(building -> {
            PowerGraph graph=building.power.graph;
            graph.reflow(generatorBuilding);
        });
    }

    public void loadStats() {
        if (initStats || unit.type == null) return;
        initStats = true;

        generatorBuilding = createBuild(((PowerUnitType) unit.type).getGeneratorBlock());
        nodeBuild = createBuild(((PowerUnitType) unit.type).getNodeBlock());
        generatorBuilding.setParent(unit);
        nodeBuild.setParent(unit);
        nodeBuild.setConnect(generatorBuilding,true);
    }

    public void reloadLinks() {
//        unit.links=unit.links.select(unit::goodLink);
        oldLinks=links.copy();
        links.clear();
        Tile tile = unit.tileOn();
        for (int x = (int) ((float) tile.x - unit.type.range); (float) x <= (float) tile.x + unit.type.range; ++x) {
            for (int y = (int) ((float) tile.y - unit.type.range); (float) y <= (float) tile.y + unit.type.range; ++y) {
                Building link = Vars.world.build(x, y);
                boolean linked = goodLink(link);
                if (linked && !links.contains(link)) {
                    links.add(link);
                }

            }
        }
        sortLinks();
    }
    public boolean goodLink(Building link) {
        return link != null && link.isValid() && ((PowerUnitType) unit.type).good.get(link, unit);
    }

    public void remove() {
        resetLinks();
    }
}
