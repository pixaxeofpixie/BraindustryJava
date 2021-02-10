package braindustry.content;

import arc.graphics.Color;
import mindustry.content.Planets;
import mindustry.graphics.g3d.SunMesh;
import mindustry.type.Planet;

public class PlanetsFromScripts {
    public static Planet zetsubo;
    public void load(){
        zetsubo=new Planet("zetsubo", Planets.sun,3,2.4f){
            {
                hasAtmosphere = false;
                meshLoader = () -> new SunMesh(zetsubo, 4, 5, 0.3, 1.7, 1.2, 1, 1.5f, Color.valueOf("9FFCFFFF"), Color.valueOf("50DDE2FF"), Color.valueOf("4749C9FF"));
                orbitRadius = 55.7f;
                accessible = false;
                bloom = true;
            }
        };
    }
}
