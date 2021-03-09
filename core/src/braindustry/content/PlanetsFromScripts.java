package braindustry.content;

import arc.graphics.Color;
import arc.math.Mathf;
import braindustry.maps.generators.OsorePlanetGenerator;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.SunMesh;
import mindustry.type.Planet;

public class PlanetsFromScripts {
    public static Planet zetsubo, osore;

    public void load() {
        zetsubo = new Planet("zetsubo", Planets.sun, 3, 2.4f) {
            {
                hasAtmosphere = false;
                meshLoader = () -> new SunMesh(this, 4, 5, 0.3f, 1.7f, 1.2f, 1, 1.5f, Color.valueOf("9FFCFFFF"), Color.valueOf("50DDE2FF"), Color.valueOf("4749C9FF"));
                orbitRadius = 55.7f;
                accessible = false;
                bloom = true;
            }
        };
        osore = new Planet("osore", zetsubo, 3, 0.7f) {
            {
                generator = new OsorePlanetGenerator();
                startSector = 25;
                hasAtmosphere = false;
                meshLoader = () -> new HexMesh(this, 8);
                orbitRadius = 11.2f;
                rotateTime = Float.POSITIVE_INFINITY;
                orbitTime = Mathf.pow((2.0f + 14.0f + 0.66f), 1.5f) * 80;
                accessible = true;
            }
        };
    }
}
