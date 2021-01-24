package braindustry.ai.types;

import arc.func.Prov;
import arc.math.Mathf;
import arc.struct.IntQueue;
import arc.struct.IntSeq;
import braindustry.gen.StealthUnitc;
import braindustry.gen.StealthUnitc;
import mindustry.Vars;
import mindustry.ai.Pathfinder;
import mindustry.ai.types.FlyingAI;
import mindustry.ai.types.GroundAI;
import mindustry.entities.Predict;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.game.Team;
import mindustry.gen.Teamc;
import mindustry.world.Tile;
import mindustry.world.blocks.units.RepairPoint;
import mindustry.world.meta.BlockFlag;

public class StealthGroundAI extends AIController {
    public static int fieldRepair = 2;
    static Prov<Pathfinder.Flowfield> dumpObj = StealthFlowfield::new;

    static {
        if (true) {
            if (Pathfinder.fieldTypes.size+1<=5) {
                Pathfinder.fieldTypes.add(dumpObj);
            } else if (!Pathfinder.fieldTypes.contains(dumpObj)) {
                throw new RuntimeException("Can't add StealthFlowfield in Pathfinder.fieldTypes");
            }
            fieldRepair=Pathfinder.fieldTypes.indexOf(dumpObj);
        }
    }

    public void updateUnit() {
        if (unit instanceof StealthUnitc)((StealthUnitc)unit).healing(false);
        if (this.useFallback() && (this.fallback != null || (this.fallback = this.fallback()) != null)) {

            if (this.fallback.unit() != this.unit) {
                this.fallback.unit(this.unit);
            }

            this.fallback.updateUnit();
        } else {
            this.updateVisuals();
            this.updateTargeting();
            this.updateMovement();
        }
    }

    protected void pathfind(int pathTarget) {
        super.pathfind(pathTarget);
        if (true) return;
        int costType = this.unit.pathType();
        Tile tile = this.unit.tileOn();
        if (tile != null) {
            Tile targetTile = Vars.pathfinder.getTargetTile(tile, Vars.pathfinder.getField(this.unit.team, costType, pathTarget));
            if (costType != 2 || targetTile.floor().isLiquid) {
                this.unit.moveAt(vec.trns(this.unit.angleTo(targetTile), this.unit.speed()));
            }
        }
    }
    @Override
    protected void updateMovement() {

        StealthUnitc sunit = (StealthUnitc) this.unit;
        Teamc target = targetFlag(unit.x, unit.y, BlockFlag.repair, false);
        float radius = 8f;
        checkRadius:
        {
            try {
                if (target instanceof RepairPoint.RepairPointBuild) {
                    radius = ((RepairPoint) ((RepairPoint.RepairPointBuild) target).block).repairRadius*0.75f;
                    break checkRadius;
                }
                Class<?> c = target.getClass();

                radius = (float) c.getField("repairRadius").get(target);
            } catch (Exception ex) {

            }
        }
//        this.target=target;
//        Arrays.fill(targets,target);
        if (target != null && !unit.within(target, radius)) {
            pathfind(fieldRepair);
//            targets=new Teamc[0];
        } else {
            sunit.healing(true);
        }
        if (unit.type.canBoost && !unit.onSolid()) {
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, 0.08f);
        }

        if (target != null && !unit.within(target, 70f)) {
//            unit.approach(Mathf.arrive(unit.x, unit.y, realtarget.x, realtarget.y, unit.vel, speed, 0f, speed, 1f).scl(1f / Time.delta));
        }
        if (!Units.invalidateTarget(target, unit, unit.range()) && unit.type.rotateShooting) {
            if (unit.type.hasWeapons()) {
                unit.lookAt(Predict.intercept(unit, target, unit.type.weapons.first().bullet.speed));
            }
        } else if (unit.moving()) {
            unit.lookAt(unit.vel().angle());
        }
    }

    @Override
    protected boolean useFallback() {
        return !(this.unit instanceof StealthUnitc && ((StealthUnitc) unit).mustHeal());
    }

    @Override
    public AIController fallback() {
        return unit.type.flying ? new FlyingAI() : new GroundAI();
    }

    @Override
    public boolean shouldShoot() {
        return (this.unit instanceof StealthUnitc && !((StealthUnitc) unit).inStealth());
    }

    protected static class StealthFlowfield extends Pathfinder.Flowfield {
        final IntSeq targets;
        public int[][] weights;
        public int[][] searches;
        protected int refreshRate;
        IntQueue frontier;
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
        protected void getPositions(IntSeq out) {

            for (Tile other : Vars.indexer.getAllied(this.team, BlockFlag.repair)) {
                out.add(other.pos());
            }
        }
    }
}
