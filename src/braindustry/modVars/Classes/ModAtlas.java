package braindustry.modVars.Classes;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;

import static braindustry.modVars.modFunc.getFullName;

public class ModAtlas {
    public TextureRegion laser, laserEnd;
    public ModAtlas(){
        this.load();
    }
    public void load(){
        laser = Core.atlas.find(getFullName("laser"),"laser");
        laserEnd = Core.atlas.find(getFullName("laser-end"),"laser-end");
    }
}
