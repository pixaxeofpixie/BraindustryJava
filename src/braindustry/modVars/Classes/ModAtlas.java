package braindustry.modVars.Classes;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

import static braindustry.modVars.modFunc.*;

public class ModAtlas {
    public TextureRegion laser, laserEnd;
    public ModAtlas(){
        this.load();
    }
    public void load(){
        laser = Core.atlas.find(fullName("laser"),"laser");
        laserEnd = Core.atlas.find(fullName("laser-end"),"laser-end");
    }
}
