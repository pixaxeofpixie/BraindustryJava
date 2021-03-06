package braindustry.world.blocks.Unit.power;

import arc.math.geom.Point2;
import braindustry.entities.abilities.PowerGeneratorAbility;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.meta.BuildVisibility;

public class UnitPowerGenerator extends PowerGenerator {
    public UnitPowerGenerator(String name) {
        super(name);
        this.buildVisibility = BuildVisibility.debugOnly;
        this.powerProduction = 10;
    }

    public class UnitPowerGeneratorBuild extends PowerGenerator.GeneratorBuild {
        public Unit parent;


        private PowerGeneratorAbility ability;

        public UnitPowerGeneratorBuild() {

        }


        public int pos() {
            return Point2.pack((int) this.x / 8, (int) this.y / 8);
        }

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
            return ability.powerProduction;
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
