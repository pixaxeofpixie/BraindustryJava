package braindustry.entities;

import arc.func.Cons;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Entityc;

public class ZeroEntity implements Entityc {
    protected ZeroEntity() {
    }

    public <T> T as() {
        return (T)this;
    }

    public <T extends Entityc> T self() {
        return (T)this;
    }

    public <T> T with(Cons<T> cons) {
        cons.get(as());
        return (T)this;
    }

    public void add() {
    }

    public void afterRead() {
    }

    public int classId() {
        return 0;
    }

    public int id() {
        return -1;
    }

    public void id(int id) {
    }

    public boolean isAdded() {
        return false;
    }

    public boolean isLocal() {
        return false;
    }

    public boolean isNull() {
        return true;
    }

    public boolean isRemote() {
        return false;
    }

    public void read(Reads read) {
    }

    public void remove() {
    }

    public boolean serialize() {
        return false;
    }

    public void update() {
    }

    public void write(Writes write) {
    }
}
