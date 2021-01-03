package braindustry.entities;

import arc.Core;
import arc.func.Boolf2;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import braindustry.modVars.modVars;
import braindustry.world.blocks.sandbox.BlockSwitcher;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.blocks.power.PowerGraph;

public abstract class PowerUnitType extends UnitType {

    public abstract Block getGeneratorBlock();
    public TextureRegion bottomRegion;
    public TextureRegion light;
    public TextureRegion[] plasmaRegions;
    public Color plasma1,plasma2;
    public void drawReactor(Unit unit){
        Draw.rect(this.bottomRegion, unit.x, unit.y);

        for(int i = 0; i < this.plasmaRegions.length; ++i) {
            float r = (float)(this.hitSize) - 3.0F + Mathf.absin(Time.time, 2.0F + (float)i * 1.0F, 5.0F - (float)i * 0.5F);
            Draw.color(this.plasma1, this.plasma2, (float)i / (float)this.plasmaRegions.length);
            Draw.alpha((0.3F + Mathf.absin(Time.time, 2.0F + (float)i * 2.0F, 0.3F + (float)i * 0.05F)) * 1);
            Draw.blend(Blending.additive);
            Draw.rect(this.plasmaRegions[i], unit.x, unit.y, r, r, Time.time * (12.0F + (float)i * 6.0F) * 1);
            Draw.blend();
        }

        this.applyColor(unit);
        Draw.rect(this.region, unit.x, unit.y,unit.rotation - 90.0F);
        Draw.color();
    }
    @Override
    public void load() {
        super.load();
        this.bottomRegion = Core.atlas.find(this.name+"-bottom");
        this.light= Core.atlas.find(this.name+"-light");
        Seq<TextureRegion> plasmas=new Seq<>();
        int i=0;
        for (TextureRegion plasma=Core.atlas.find(this.name+"-plasma-"+i);Core.atlas.isFound(plasma);plasma=Core.atlas.find(this.name+"-plasma-"+(++i))){
            plasmas.add(plasma);
        }
        this.plasmaRegions =new TextureRegion[plasmas.size];
        for (int j = 0; j < plasmaRegions.length; j++) {
            plasmaRegions[j]=plasmas.get(j);
        }
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

    @Override
    public void drawBody(Unit unit) {
//        super.drawBody(unit);
        drawReactor(unit);
    }

    public void draw(Unit unit) {
        super.draw(unit);
//        drawReactor(unit);
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
        this.plasma1 = Color.valueOf("ffd06b");
        this.plasma2 = Color.valueOf("ff361b");
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
