package braindustry.gen;

import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.io.ModTypeIO;
import mindustry.gen.Player;
import mindustry.io.TypeIO;

public class ModPlayer extends Player {
    public static ModPlayer create() {
        return new ModPlayer();
    }

    @Override
    public void read(Reads read) {
        short REV = read.s();
        if (REV == 0) {
            this.admin = read.bool();
            this.boosting = read.bool();
            this.color = TypeIO.readColor(read, this.color);
            this.mouseX = read.f();
            this.mouseY = read.f();
            this.name = TypeIO.readString(read);
            this.shooting = read.bool();
            this.team = TypeIO.readTeam(read);
            this.typing = read.bool();
            this.unit = ModTypeIO.readUnit(read);
            this.x = read.f();
            this.y = read.f();
            this.afterRead();
        } else {
            throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'PlayerComp'");
        }
    }

    @Override
    public void readSync(Reads read) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
        boolean islocal = this.isLocal();
        this.admin = read.bool();
        if (!islocal) {
            this.boosting = read.bool();
        } else {
            read.bool();
        }

        this.color = TypeIO.readColor(read, this.color);
        if (!islocal) {
            this.mouseX = read.f();
        } else {
            read.f();
        }

        if (!islocal) {
            this.mouseY = read.f();
        } else {
            read.f();
        }

        this.name = TypeIO.readString(read);
        if (!islocal) {
            this.shooting = read.bool();
        } else {
            read.bool();
        }

        this.team = TypeIO.readTeam(read);
        if (!islocal) {
            this.typing = read.bool();
        } else {
            read.bool();
        }

        this.unit = ModTypeIO.readUnit(read);
        /*if (!islocal) {
            this.x_LAST_ = this.x;
            this.x_TARGET_ = read.f();
        } else {
            read.f();
            this.x_LAST_ = this.x;
            this.x_TARGET_ = this.x;
        }

        if (!islocal) {
            this.y_LAST_ = this.y;
            this.y_TARGET_ = read.f();
        } else {
            read.f();
            this.y_LAST_ = this.y;
            this.y_TARGET_ = this.y;
        }*/
        snapInterpolation();

        this.afterSync();
    }

    @Override
    public void write(Writes write) {
        super.write(write);
    }

    @Override
    public void writeSync(Writes write) {
        super.writeSync(write);
    }
}
