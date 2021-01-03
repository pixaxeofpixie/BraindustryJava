package braindustry.entities;

import arc.func.Boolf;
import arc.func.Boolf2;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import braindustry.content.ModBlocks;
import braindustry.modVars.modVars;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerBlock;
import mindustry.world.blocks.power.PowerGraph;

import static braindustry.modVars.modFunc.print;

public abstract class PowerUnitType extends UnitType {

    public abstract Block getGeneratorBlock();
     boolean loaded=false;
    @Override
    public void load() {
        super.load();
    }

    protected void drawLaser(Team team, float x1, float y1, float x2, float y2, int size1, int size2) {
        float angle1 = Angles.angle(x1, y1, x2, y2);
        float vx = Mathf.cosDeg(angle1);
        float vy = Mathf.sinDeg(angle1);
        float len1 = (float)(size1 * 8) / 2.0F - 1.5F;
        float len2 = (float)(size2 * 8) / 2.0F - 1.5F;
        Drawf.laser(team,  modVars.modAtlas.laser, modVars.modAtlas.laserEnd, x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, 0.25F);
    }
    Boolf2<Building,Unit> good=((building,unit) -> {
        if (building==null)return false;
        return building.block.hasPower && building.team==unit.team;
    });
    public void draw(PowerGeneratorUnit unit) {
        super.draw(unit);

        /*for (int x = (int) ((float) tile.x - this.range); (float) x <= (float) tile.x + this.range; ++x) {
            for (int y = (int) ((float) tile.y - this.range); (float) y <= (float) tile.y + this.range; ++y) {
                Building link = Vars.world.build(x, y);
                boolean linked = good.get(link,unit);
                if (linked) {
                    Draw.z(z-0.1f);
                    drawLaser(unit.team,unit.x,unit.y,link.x,link.y,1,link.block.size);
                }

            }
        }*/
    }
    public boolean goodBuilding(BlockSwitcher.BlockSwitcherBuild forB, Building other) {
        return other.dst(forB) <= (range + other.block.size+Mathf.ceil(forB.block.size/2f)) * 8f && forB != other && forB.isValid() && other.isValid();
    }
    public void update(PowerGeneratorUnit unit) {
//        print(unit.toString()+" update.type");
        super.update(unit);
        unit.reloadLinks();
        unit.links.each(building -> {
            PowerGraph graph=building.power.graph;
            graph.reflow(unit.generatorBuilding);
//            building.power.graph.add(unit.generatorBuilding);
//            graph.distributePower(graph.getPowerNeeded(),graph.getPowerProduced());
        });
    }

    public PowerUnitType(String name) {
        super(name);
    }

    @Override
    public void init() {
        if (constructor!=null){
            if (!(constructor.get() instanceof PowerGeneratorUnit)){
                constructor=PowerGeneratorUnit::new;
            }
        }
        super.init();
    }
}
