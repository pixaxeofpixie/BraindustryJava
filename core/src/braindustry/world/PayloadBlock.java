package braindustry.world;

import arc.math.geom.Geometry;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.world.modules.PayloadModule;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.modules.ItemModule;

public class PayloadBlock extends Block {
    public boolean hasPayload=true;
    public int payloadCapacity=2;
    public PayloadBlock(String name) {
        super(name);
    }
    public class PayloadBuild extends Building{
     public    PayloadModule payloads;
     public boolean blocked;

        @Override
        public Payload getPayload() {
            return payloads.first();
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            return payloads.count()  < payloadCapacity;
        }

        @Override
        public void handlePayload(Building source, Payload payload) {
            payloads.add(payload);
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            int ntrns = 1 + size/2;
            Tile next = tile.nearby(Geometry.d4(rotation).x * ntrns, Geometry.d4(rotation).y * ntrns);
            blocked = (next != null && next.solid() && !next.block().outputsPayload);
        }

        public boolean dumpPayload() {
            Payload take = payloads.take();
            boolean b = super.dumpPayload(take);
            if (!b)payloads.add(take);else{
                if (!blocked) {
                    take.dump();
                    moved();
                } else {
                    moveFailed();
                }
            }
            return b;
        }
        public void moveFailed(){

        }

        public void moved(){

        }
        @Override
        public boolean movePayload(Payload todump) {
            return super.movePayload(todump);
        }

        @Override
        public Building create(Block block, Team team) {

            Building building = super.create(block, team);
            if (hasPayload) payloads=new PayloadModule();
            return building;
        }

        public boolean canDumpPayload(Building to, Payload payload){
            return true;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            payloads.write(write);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            payloads.read(read);
        }
    }
}
