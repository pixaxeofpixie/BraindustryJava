package braindustry.world.blocks.Unit.power;

import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Log;
import braindustry.entities.abilities.PowerGeneratorAbility;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.modules.PowerModule;

import static mindustry.Vars.*;

public class UnitPowerNode extends PowerNode {
    public UnitPowerNode(String name) {
        super(name);
        this.buildVisibility = BuildVisibility.debugOnly;
        this.configurations.clear();

        this.config(Integer.class, (entity, value) -> {


        });
    }

    public void getPotentialLinks(Tile tile, PowerGeneratorAbility ability, Cons<Building> others) {
        float laserRange = ability.laserRange;
        Boolf<Building> valid = other -> other != null  && other.power != null &&
                (other.block.outputsPower || other.block.consumesPower || other.block instanceof PowerNode) &&
                overlaps(tile.x * tilesize + offset, tile.y * tilesize + offset, other.tile(), laserRange * tilesize) && other.team == player.team()
                /*&& !other.proximity.contains(e -> e.tile == tile)*/ && !graphs.contains(other.power.graph);

        tempTileEnts.clear();
        graphs.clear();
        if(tile.build != null && tile.build.power != null){
            graphs.add(tile.build.power.graph);
        }

        Geometry.circle(tile.x, tile.y, (int)(laserRange + 2), (x, y) -> {
            Building other = world.build(x, y);
            if(valid.get(other) && !tempTileEnts.contains(other)){
                tempTileEnts.add(other);
            }
        });

        tempTileEnts.sort((a, b) -> {
            int type = -Boolean.compare(a.block instanceof PowerNode, b.block instanceof PowerNode);
            if(type != 0) return type;
            return Float.compare(a.dst2(tile), b.dst2(tile));
        });

        tempTileEnts.each(valid, t -> {
            graphs.add(t.power.graph);
            others.get(t);
        });
    }
    public class UnitPowerNodeBuild extends PowerNodeBuild {
        public Unit parent;
        private PowerGeneratorAbility ability;


        public void setParent(Unit parent, PowerGeneratorAbility ability) {
            this.parent = parent;
            this.ability = ability;
        }

        @Override
        public void draw() {
        }

        @Override
        public void drawStatus() {
        }

        @Override
        public void drawTeam() {
        }

        @Override
        public void drawTeamTop() {
        }

        public void setupColor(float satisfaction) {
            Draw.color(UnitPowerNode.this.laserColor1, UnitPowerNode.this.laserColor2, (1.0F - satisfaction) * 0.86F + Mathf.absin(3.0F, 0.1F));
            Draw.alpha(Vars.renderer == null ? 0.5F : Vars.renderer.laserOpacity);
        }

        public void drawLasers(float z) {
            if (!Mathf.zero(Vars.renderer.laserOpacity)) {
                Draw.z(z);
                UnitPowerNode.this.setupColor(this.power.graph.getSatisfaction());

                for (int i = 0; i < this.power.links.size; ++i) {
                    Building link = Vars.world.build(this.power.links.get(i));
                    UnitPowerNode.this.drawLaser(this.team, this.x, this.y, link.x, link.y, UnitPowerNode.this.size, link.block.size);

                }

                Draw.reset();
            }
        }

        public void setConnect(Building other, boolean add) {
            if (other == null) return;
            PowerModule power = this.power;
            int value = other.pos();
            boolean contains = power.links.contains(value);
            boolean valid = other.power != null;
            if (contains && !add) {
                power.links.removeValue(value);
                if (valid) {
                    other.power.links.removeValue(getPos());
                }

                PowerGraph newgraph = new PowerGraph();
                newgraph.reflow(this);
                if (valid && other.power.graph != newgraph) {
                    PowerGraph og = new PowerGraph();
                    og.reflow(other);
                }
            } else if (valid && power.links.size < ability.maxNodes && !contains && add) {
                if (other.block instanceof PowerNode && ((PowerNode) other.block).maxNodes<=other.power.links.size)return;
                if (!power.links.contains(other.pos())) {
                    power.links.add(other.pos());
                }

                if (other.team == this.team && !other.power.links.contains(getPos())) {
                    other.power.links.add(getPos());
                }

                power.graph.addGraph(other.power.graph);
            }
        }

        protected int getPos() {
            return -parent.id;
        }

        public int pos() {
            return Point2.pack((int) this.x / 8, (int) this.y / 8);
        }

        @Override
        public void update() {
            super.update();
            this.x = parent.x;
            this.y = parent.y;
            Log.info("Update");
        }

        public void kill() {
//            Call.
//            Call.tileDestroyed(this);
        }

        @Override
        public void add() {
            if (!this.added) {
                Groups.all.add(this);
                this.added = true;
            }
        }

        public float getPowerProduction() {

            if (!this.isValid()) {
                for (int pos : power.links.toArray()) {
                    Building build = Vars.world.build(pos);
                    if (build == null || !build.power.links.contains(getPos())) continue;
                    build.power.links.removeValue(getPos());
                }
                this.power.graph.remove(this);
            }
            return 0;
        }

        @Override
        public boolean isValid() {
            boolean near = true;
            /*for (int i = 0; i < this.power.links.size; i++) {
                Building building= Vars.world.build(this.power.links.get(i));
                near=near || (building!=null && parent.validLink(building));
            }*/
            return parent != null && parent.isValid() && near;
        }

        public void set(float x, float y) {
//        this.x = x;
//        this.y = y;
        }
    }
}
