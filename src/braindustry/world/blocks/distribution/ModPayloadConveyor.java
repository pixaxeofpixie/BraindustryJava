package braindustry.world.blocks.distribution;

import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.gen.Building;
import mindustry.world.blocks.distribution.PayloadConveyor;
import mindustry.world.blocks.payloads.Payload;

public class ModPayloadConveyor extends PayloadConveyor {
    public int maxPayload=2;
    public ModPayloadConveyor(String name) {
        super(name);
    }
    public class ModPayloadConveyorBuild extends PayloadConveyor.PayloadConveyorBuild{
        public Seq<Payload> items=new Seq<>();
        public ObjectMap<Payload,Float> itemRotations=new ObjectMap<>();
        public Payload takePayload() {
            Payload t = getFirst();
            items.remove(t);
            itemRotations.remove(t);
            return t;
        }
        private Payload getFirst(){
            if (items.size==0)return null;
            return items.first();
        }
        public Payload getPayload() {
            return getFirst();
        }
        public void handlePayload(Building source, Payload payload) {
            this.items.add(payload);
            this.stepAccepted = this.curStep();
            itemRotations.put(payload,source == this ? this.rotdeg() : source.angleTo(this));
//            this.animation = 0.0F;
            this.updatePayload();
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload) {
            if (source == this) {
            return this.items.size<=ModPayloadConveyor.this.maxPayload && payload.fits(ModPayloadConveyor.this.payloadLimit);
        } else {
            return this.items.size<=ModPayloadConveyor.this.maxPayload && this.progress <= 5.0F && payload.fits(ModPayloadConveyor.this.payloadLimit);
        }
        }

        @Override
        public void updateTile() {
            this.item=getFirst();
            this.itemRotation=itemRotations.get(item);
            super.updateTile();
        }

        @Override
        public void updatePayload() {
            items.each((item)-> {
                this.itemRotation=itemRotations.get(item);
                this.item=item;
                if (this.animation > this.fract()) {
                    this.animation = Mathf.lerp(this.animation, 0.8F, 0.15F);
                }

                this.animation = Math.max(this.animation, this.fract());
                float fract = this.animation;
                float rot = Mathf.slerp(this.itemRotation, this.rotdeg(), fract);
                if (fract < 0.5F) {
                    Tmp.v1.trns(this.itemRotation + 180.0F, (0.5F - fract) * 8.0F * (float)ModPayloadConveyor.this.size);
                } else {
                    Tmp.v1.trns(this.rotdeg(), (fract - 0.5F) * 8.0F * (float)ModPayloadConveyor.this.size);
                }

                float vx = Tmp.v1.x;
                float vy = Tmp.v1.y;
                this.item.set(this.x + vx, this.y + vy, rot);
            });

        }

    }
}
