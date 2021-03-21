package braindustry.content;

import mindustry.Vars;
import mindustry.ctype.ContentList;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;

import static ModVars.modFunc.fullName;

public class ModSectorPresets implements ContentList {
    public static SectorPreset meltingPoint, FFOf,icyBeach,magmaticElectrostation,methaneLaboratories,ritual,polarRift,spacePort,
            azureLandscape, icyDarkness, deentForest, emeraldSwamp, jungleExplorationComplex;
//osore sectors
    public void load() {
        meltingPoint = new SectorPreset("melting-point", ModPlanets.osore, 20) {
            {
                localizedName = "Melting point";
                alwaysUnlocked = true;
                difficulty = 3;
                captureWave = 20;
            }
        };

        FFOf = new SectorPreset("451F", ModPlanets.osore, 50) {
            {
                localizedName = "451F";
                //alwaysUnlocked = true;
                difficulty = 4;
                captureWave = 35;
            }
        };
        icyBeach = new SectorPreset("icy-beach", ModPlanets.osore, 70) {
            {
                localizedName = "Icy Beach";
                //alwaysUnlocked = true;
                difficulty = 7;
                captureWave = 35;
            }
        };
        methaneLaboratories = new SectorPreset("methane-laboratories", ModPlanets.osore, 71) {
            {
                localizedName = "Methane Laboratories";
                //alwaysUnlocked = true;
                difficulty = 7;
                //captureWave = 35;
            }
        };
        magmaticElectrostation = new SectorPreset("magmatic-electrostation", ModPlanets.osore, 10) {
            {
                localizedName = "Magmatic Electrostation";
                //alwaysUnlocked = true;
                difficulty = 6;
                captureWave = 40;
            }
        };
        ritual = new SectorPreset("ritual", ModPlanets.osore, 86) {
            {
                localizedName = "Ritual";
                //alwaysUnlocked = true;
                difficulty = 10;
                captureWave = 15;
            }
        };
        polarRift = new SectorPreset("polar-rift", ModPlanets.osore, 38) {
            {
                localizedName = "Polar Rift";
                //alwaysUnlocked = true;
                difficulty = 6;
                captureWave = 40;
            }
        };
        spacePort= new SectorPreset("space-port", ModPlanets.osore, 25) {
            {
                localizedName = "Space Port";
                alwaysUnlocked = true;
                difficulty = 4;
                captureWave = 30;
            }
        };
        //shinrin sectors
        azureLandscape = new SectorPreset("azure-landscape", ModPlanets.shinrin, 85) {
            {
                localizedName = "Azure Landscape";
                alwaysUnlocked = true;
                difficulty = 10;
                captureWave = 35;
            }
        };
        jungleExplorationComplex = new SectorPreset("jungle-explorer-complex", ModPlanets.shinrin, 42) {
            {
                localizedName = "Jungle Exploration Complex";
                alwaysUnlocked = true;
                difficulty = 4;
                captureWave = 35;
            }
        };
    }
}
