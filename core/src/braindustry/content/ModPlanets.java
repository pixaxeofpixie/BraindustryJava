package braindustry.content;

import arc.graphics.Color;
import arc.math.Mathf;
import braindustry.maps.generators.OsorePlanetGenerator;
import braindustry.maps.generators.ShinrinPlanetGenerator;
import mindustry.content.Planets;
import mindustry.ctype.ContentList;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.SunMesh;
import mindustry.type.Planet;

public class ModPlanets implements ContentList {
    public static Planet zetsubo, osore, shinrin;

    public void load() {
        zetsubo = new Planet("zetsubo", Planets.sun, 3, 2.4f) {
            {
                hasAtmosphere = true;
                meshLoader = () -> new SunMesh(
                        this, 4, 5, 0.3f, 1.0f, 1.2f, 1, 1.3f,
                        Color.valueOf("5E94FF"), Color.valueOf("214CDF"), Color.valueOf("2DA9DF"), Color.valueOf("9CB5FF"),
                        Color.valueOf("61729F"), Color.valueOf("3BB1CA"));
                orbitRadius = 51.5f;
                accessible = false;
                bloom = true;
            }
        };
        osore = new Planet("osore", zetsubo, 3, 1f) {
            {
                generator = new OsorePlanetGenerator();
                startSector = 25;
                atmosphereColor = Color.valueOf("8c3149");
                atmosphereRadIn = 0.01f;
                atmosphereRadOut = 0.4f;
                hasAtmosphere = true;
                meshLoader = () -> new HexMesh(this, 6);
                orbitRadius = 11.2f;
                rotateTime = Float.POSITIVE_INFINITY;
                orbitTime = Mathf.pow((2.0f + 14.0f + 0.66f), 1.5f) * 80;
                accessible = true;
            }
        };
        shinrin = new Planet("shinrin", osore, 3, 0.4f) {
            {
                generator = new ShinrinPlanetGenerator();
                startSector = 42;
                atmosphereColor = Color.valueOf("36af54");
                atmosphereRadIn = 0.01f;
                atmosphereRadOut = 0.2f;
                hasAtmosphere = true;
                meshLoader = () -> new HexMesh(this, 6);
                orbitRadius = 5.0f;
                rotateTime = Float.POSITIVE_INFINITY;
                orbitTime = Mathf.pow((2.0f + 14.0f + 0.66f), 1.5f) * 80;
                accessible = true;
            }
        };
    }
}
