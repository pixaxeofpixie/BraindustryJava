package braindustry.world;

import arc.struct.ObjectMap;
import arc.util.Pack;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.io.TypeIO;
import mindustry.type.Item;
import mindustry.world.blocks.payloads.Payload;

import static mindustry.Vars.content;

public class PayloadBuffer {
    private ObjectMap<Payload,Float> buffer;
    private int capacity;

    public PayloadBuffer(int capacity){
        this.buffer = new ObjectMap<>(capacity);
        this.capacity=capacity;
    }

    public boolean accepts(){
        return buffer.size < capacity;
    }

    public void accept(Payload payload){
        //if(!accepts()) return;
        if (!buffer.containsKey(payload))buffer.put(payload,0f);
    }
    public void each(PayloadBufferPasser passer){
        for (ObjectMap.Entry<Payload, Float> entry : buffer) {
            passer.accept(entry.key,entry.value);
        }
    }
    public void update(PayloadBufferUpdater passer){
        for (ObjectMap.Entry<Payload, Float> entry : buffer) {
          entry.value=  passer.accept(entry.key,entry.value);
        }
    }
    public void update(){
        for (ObjectMap.Entry<Payload, Float> e : buffer.copy()) {
            buffer.remove(e.key);
            buffer.put(e.key,e.value+Time.delta);
        }
    }
    public Payload poll(float speed){
        if (buffer.size>0){
            for (ObjectMap.Entry<Payload, Float> e : buffer) {
                if (e.value>=speed){
                    return e.key;
                }
            }
        }
        return null;
    }

    public void remove(Payload key){
        buffer.remove(key);
    }

    public void write(Writes write){
        write.i(buffer.size);
        for (ObjectMap.Entry<Payload, Float> e : buffer) {
            TypeIO.writePayload(write,e.key);
            write.f(e.value);
        }
    }

    public void read(Reads read){
        int size=read.i();
        buffer.clear();
        for (int i = 0; i < size; i++) {
            Payload payload=TypeIO.readPayload(read);
            float time=read.f();
            if (payload!=null)buffer.put(payload,time);
        }
    }

    public int size() {
        return buffer.size;
    }

    public interface PayloadBufferPasser{
        void accept(Payload payload,float time);
    }
    public interface PayloadBufferUpdater{
        float accept(Payload payload,float time);
    }
}
