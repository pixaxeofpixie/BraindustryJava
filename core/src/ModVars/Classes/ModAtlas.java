package ModVars.Classes;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

import static ModVars.modFunc.*;

public class ModAtlas {
    public TextureRegion laser, laserEnd,flash,flareWhite;
    public ModAtlas(){
        this.load();
    }
    public void load(){
        laser = Core.atlas.find(fullName("laser"),"laser");
        laserEnd = Core.atlas.find(fullName("laser-end"),"laser-end");
        flash=Core.atlas.find(fullName("flash"));
        flareWhite=Core.atlas.find(fullName("FlareWhite"));
    }
}
