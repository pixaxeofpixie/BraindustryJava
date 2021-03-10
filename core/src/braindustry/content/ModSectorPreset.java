package braindustry.content;

import mindustry.Vars;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;

import static ModVars.modFunc.fullName;

public class ModSectorPreset{
    public static SectorPreset meltingPoint, FFOf,icyBeach,magmaticElectrostation,methaneLaboratories,ritual,polarRift,spacePort;

    public void load() {
        Planet osorePlanet = Vars.content.planets().find(p -> p.name.toLowerCase().contains("osore") && p.name.startsWith(fullName("")));
        meltingPoint = new SectorPreset("melting-point", osorePlanet, 20) {
            {
                localizedName = "Melting point";
                alwaysUnlocked = true;
                difficulty = 3;
                captureWave = 20;
            }
        };

        FFOf = new SectorPreset("451f", osorePlanet, 50) {
            {
                localizedName = "451F";
                //alwaysUnlocked = true;
                difficulty = 4;
                captureWave = 35;
            }
        };
        icyBeach = new SectorPreset("icy-beach", osorePlanet, 70) {
            {
                localizedName = "Icy Beach";
                //alwaysUnlocked = true;
                difficulty = 7;
                captureWave = 35;
            }
        };
        methaneLaboratories = new SectorPreset("methane-laboratories", osorePlanet, 71) {
            {
                localizedName = "Methane Laboratories";
                //alwaysUnlocked = true;
                difficulty = 7;
                //captureWave = 35;
            }
        };
        magmaticElectrostation = new SectorPreset("magmatic-electrostation", osorePlanet, 10) {
            {
                localizedName = "Magmatic Electrostation";
                //alwaysUnlocked = true;
                difficulty = 6;
                captureWave = 40;
            }
        };
        ritual = new SectorPreset("ritual", osorePlanet, 86) {
            {
                localizedName = "Ritual";
                //alwaysUnlocked = true;
                difficulty = 10;
                captureWave = 15;
            }
        };
        polarRift = new SectorPreset("polar-rift", osorePlanet, 38) {
            {
                localizedName = "Polar Rift";
                //alwaysUnlocked = true;
                difficulty = 6;
                captureWave = 40;
            }
        };
    }
}
