package braindustry.content;

import mindustry.Vars;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;

public class ModSectorPresent {
    public static SectorPreset meltingPoint, FFOf;

    public void load() {
        Planet osorePlanet = Vars.content.planets().find(p -> p.name.toLowerCase().contains("osore"));
        meltingPoint = new SectorPreset("melting-point", osorePlanet, 20){
            {
            alwaysUnlocked = true;
            difficulty = 6;
            captureWave = 20;
            }
        };

        FFOf = new SectorPreset("451F", osorePlanet, 50){
            {
               alwaysUnlocked = true;
               difficulty = 8;
               captureWave = 35;
            }
        };
       

    }
}
