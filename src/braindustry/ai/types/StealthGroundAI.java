package braindustry.ai.types;

import arc.func.Prov;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.IntQueue;
import arc.struct.IntSeq;
import braindustry.gen.StealthMechUnit;
import mindustry.Vars;
import mindustry.ai.Pathfinder;
import mindustry.ai.types.FlyingAI;
import mindustry.ai.types.GroundAI;
import mindustry.entities.Predict;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.entities.units.UnitCommand;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.world.Tile;
import mindustry.world.meta.BlockFlag;

import java.util.Arrays;

import static mindustry.Vars.*;

public class StealthGroundAI extends AIController {
    static Prov<Pathfinder.Flowfield> dumpObj=StealthFlowfield::new;
    static {
//        if (Pathfinder.fieldTypes.size==2){
//            Pathfinder.fieldTypes.add(dumpObj);
//        } else if (!Pathfinder.fieldTypes.contains(dumpObj)){
//            throw new RuntimeException("Can't add StealthFlowfield in Pathfinder.fieldTypes");
//        }
    }
    @Override
    protected void updateMovement() {

        StealthMechUnit unit=(StealthMechUnit)this.unit;

        if(unit.type.canBoost && !unit.onSolid()){
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, 0.08f);
        }

        Teamc target = targetFlag(unit.x, unit.y, BlockFlag.repair, false);

        if(target != null && !unit.within(target, 70f)){
            pathfind(Pathfinder.fieldRally);
        }
        if(!Units.invalidateTarget(target, unit, unit.range()) && unit.type.rotateShooting){
            if(unit.type.hasWeapons()){
                unit.lookAt(Predict.intercept(unit, target, unit.type.weapons.first().bullet.speed));
            }
        }else if(unit.moving()){
            unit.lookAt(unit.vel().angle());
        }
    }

    @Override
    protected boolean useFallback() {
        return !(this.unit instanceof StealthMechUnit && ((StealthMechUnit)unit).inStealth);
    }

    @Override
    public AIController fallback(){
        return unit.type.flying ? new FlyingAI() : new GroundAI();
    }
    @Override
    public boolean shouldShoot(){
        return useFallback();
    }
    protected void attack(float circleLength){
        vec.set(target).sub(unit);

        float ang = unit.angleTo(target);
        float diff = Angles.angleDist(ang, unit.rotation());

        if(diff > 100f && vec.len() < circleLength){
            vec.setAngle(unit.vel().angle());
        }else{
            vec.setAngle(Mathf.slerpDelta(unit.vel().angle(), vec.angle(), 0.6f));
        }

        vec.setLength(unit.speed());

        unit.moveAt(vec);
    }
    protected static class StealthFlowfield extends Pathfinder.Flowfield {
        protected int refreshRate;
        public int[][] weights;
        public int[][] searches;
        IntQueue frontier;
        final IntSeq targets;
        int search;
        long lastUpdateTime;
        boolean initialized;

        public StealthFlowfield() {
            super();

            this.team = Team.derelict;
            this.frontier = new IntQueue();
            this.targets = new IntSeq();
            this.search = 1;
        }

        void setup(int width, int height) {
            this.weights = new int[width][height];
            this.searches = new int[width][height];
            this.frontier.ensureCapacity((width + height) * 3);
            this.initialized = true;
        }
        @Override
        protected void getPositions(IntSeq intSeq) {

        }
    }
}
