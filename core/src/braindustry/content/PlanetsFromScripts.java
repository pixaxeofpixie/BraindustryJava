package braindustry.content;

import arc.graphics.Color;
import arc.math.Mathf;
import braindustry.maps.generators.MizuGenerator;
import braindustry.maps.generators.OsorePlanetGenerator;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.SunMesh;
import mindustry.type.Planet;

public class PlanetsFromScripts {
    public static Planet zetsubo, osore, mizu;
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
        osore =new Planet("Osore",zetsubo,3,0.7f){
            {
                generator=new OsorePlanetGenerator();
                startSector=25;
               hasAtmosphere = false;
               meshLoader = () -> new HexMesh(this, 8);
               orbitRadius = 11.2f;
               rotateTime = Float.POSITIVE_INFINITY; //ÍÈÊÎÃÄÀ
               orbitTime = Mathf.pow((2.0f + 14.0f + 0.66f), 1.5f) * 80;
               accessible = true;
            }
        };
        mizu=new Planet("Mizu", zetsubo, 3, 1f){
            {
                generator =new MizuGenerator();
                startSector = 25;
                hasAtmosphere = false;
                meshLoader = () -> new HexMesh(this, 8);
                orbitRadius = 21.7f;
                rotateTime = 24;
                orbitTime = Mathf.pow((2.0f + 14.0f + 0.66f), 1.5f) * 80;
                accessible = true;
            }
        };
    }
}
