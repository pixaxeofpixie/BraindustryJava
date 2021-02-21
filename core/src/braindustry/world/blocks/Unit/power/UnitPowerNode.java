package braindustry.world.blocks.Unit.power;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Point2;
import braindustry.entities.abilities.PowerUnitAbility;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.modules.PowerModule;

public class UnitPowerNode extends PowerNode {
    public UnitPowerNode(String name) {
        super(name);
        this.buildVisibility = BuildVisibility.debugOnly;
        this.configurations.clear();

        this.config(Integer.class, (entity, value) -> {


        });
    }

    public class UnitPowerNodeBuild extends PowerNodeBuild {
        public Unit parent;
        private PowerUnitAbility ability;


        public void setParent(Unit parent,PowerUnitAbility ability) {
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
//                    other.power.links.removeValue(this.pos());
                }

                PowerGraph newgraph = new PowerGraph();
                newgraph.reflow(this);
                if (valid && other.power.graph != newgraph) {
                    PowerGraph og = new PowerGraph();
                    og.reflow(other);
                }
            } else if (valid && power.links.size < ability.maxNodes && !contains && add) {
                if (!power.links.contains(other.pos())) {
                    power.links.add(other.pos());
                }

                if (other.team == this.team && !other.power.links.contains(this.pos())) {
//                    other.power.links.add(this.pos());
                }

                power.graph.addGraph(other.power.graph);
            }
        }

        public int pos() {
            return Point2.pack((int) this.x / 8, (int) this.y / 8);
        }

        @Override
        public void update() {
            super.update();
            this.x = parent.x;
            this.y = parent.y;
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
