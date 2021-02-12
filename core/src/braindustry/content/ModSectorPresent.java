package braindustry.content;

import mindustry.Vars;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;

import static ModVars.modFunc.fullName;

public class ModSectorPresent {
    public static SectorPreset meltingPoint, FFOf;

    public void load() {
        Planet osorePlanet = Vars.content.planets().find(p -> p.name.toLowerCase().contains("osore") && p.name.startsWith(fullName("")));
        meltingPoint = new SectorPreset("melting-point", osorePlanet, 20) {
            {
                localizedName = "Melting point";
                alwaysUnlocked = true;
                difficulty = 6;
                captureWave = 20;
            }
        };

        FFOf = new SectorPreset("451f", osorePlanet, 50) {
            {
                localizedName = "451F";
                alwaysUnlocked = true;
                difficulty = 8;
                captureWave = 35;
            }
        };


    }
}
