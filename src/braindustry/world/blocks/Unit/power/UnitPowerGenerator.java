package braindustry.world.blocks.Unit.power;

import arc.struct.Seq;
import braindustry.entities.PowerGeneratorUnit;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.meta.BuildVisibility;

import java.util.concurrent.atomic.AtomicBoolean;

public class UnitPowerGenerator extends PowerGenerator {
    public UnitPowerGenerator(String name) {
        super(name);
        this.buildVisibility = BuildVisibility.debugOnly;
        this.powerProduction = 10;
    }

    public class UnitPowerGeneratorBuild extends PowerGenerator.GeneratorBuild {
        public PowerGeneratorUnit parent;

        public UnitPowerGeneratorBuild() {

        }

        public void setParent(PowerGeneratorUnit parent) {
            this.parent = parent;
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
            return UnitPowerGenerator.this.powerProduction;
        }

        @Override
        public boolean isValid() {
            boolean near=true;
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
