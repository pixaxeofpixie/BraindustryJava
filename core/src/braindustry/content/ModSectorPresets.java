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
                description = "Tremors, caused by such a massive explosion created this place";
                difficulty = 3;
                captureWave = 20;
            }
        };

        FFOf = new SectorPreset("451F", ModPlanets.osore, 50) {
            {
                localizedName = "451F";
                description = "Cataclysm covered this area one of the first";
                difficulty = 4;
                captureWave = 35;
            }
        };
        icyBeach = new SectorPreset("icy-beach", ModPlanets.osore, 70) {
            {
                localizedName = "Icy Beach";
                description = "Earlier, the most important Space Dock, now, the ruins that are captured and flooded";
                difficulty = 7;
                captureWave = 35;
            }
        };
        methaneLaboratories = new SectorPreset("methane-laboratories", ModPlanets.osore, 71) {
            {
                localizedName = "Methane Laboratories";
                description = "The beginning of the end was laid here, black hole energy was a mistake";
                difficulty = 7;
            }
        };
        magmaticElectrostation = new SectorPreset("magmatic-electrostation", ModPlanets.osore, 10) {
            {
                localizedName = "Magmatic Electrostation";
                description = "One of the largest stations that fed research bases. " +
                        "Unfortunaly, the scale and bad architcture ruined it, and together the entire Civilization";
                difficulty = 6;
                captureWave = 40;
            }
        };
        ritual = new SectorPreset("ritual", ModPlanets.osore, 86) {
            {
                localizedName = "Ritual";
                description = "A place that the enemies could not reach for a long time, it was the only refuge for the survivors." +
                        "But when the enemies got there they sprayed a crimson terror that infected and eradicated most of the life...";
                difficulty = 9;
                captureWave = 15;
            }
        };
        polarRift = new SectorPreset("polar-rift", ModPlanets.osore, 38) {
            {
                localizedName = "Polar Rift";
                description = "Failed prototype station for the production of magmatic and black hole energy";
                difficulty = 6;
                captureWave = 40;
            }
        };
        spacePort= new SectorPreset("space-port", ModPlanets.osore, 25) {
            {
                localizedName = "Space Port";
                description = "The most important point of the planet was destroyed and devastated in a moment, it was from here that the crimson terror was brought to the Serpulo  " +
                        "But now eternal night reigns here";
                alwaysUnlocked = false;
                difficulty = 4;
                captureWave = 30;
            }
        };
        //shinrin sectors
        azureLandscape = new SectorPreset("azure-landscape", ModPlanets.shinrin, 85) {
            {
                localizedName = "Azure Landscape";
                description = "The first weapon testing port that civilization was able to build on another celestial body was captured first. "
                            + "[red]WARNING!Impossible sector!Use this sector only for Chlorophylum farm from TX units";
                difficulty = 9;
                captureWave = 85;
            }
        };
        jungleExplorationComplex = new SectorPreset("jungle-explorer-complex", ModPlanets.shinrin, 42) {
            {
                localizedName = "Jungle Exploration Complex";
                description = "Another testing ground, was built by Civilization in the unfavorable jungle and for the extraction of the newly discovered Chlorophyllum, crimson terror was developed in these labs "
                        +   "[red]WARNING!Impossible sector!Use this sector only for Chlorophylum farm from TX units";
                difficulty = 9;
                captureWave = 85;
            }
        };
        icyDarkness= new SectorPreset("black-polus-oil-fabrics", ModPlanets.shinrin, 12) {
            {
                localizedName = "Frozen Valley";
                description = "The ice cave to which the enemies reached the last, but it was from here that they wanted to start an attack on Civilization" +
                            "[red]WARNING!Impossible sector!Use this sector only for Chlorophylum farm from TX units";
                difficulty = 9;
                captureWave = 85;
            }
        };
        deentForest= new SectorPreset("deent-forest", ModPlanets.shinrin, 63) {
            {
                localizedName = "Deent Forest";
                description = "Strange forest with interesting flora"+"[red]WARNING!Impossible sector!Use this sector only for Chlorophylum farm from TX units";
                difficulty = 9;
                captureWave = 85;
            }
        };
        emeraldSwamp= new SectorPreset("emerald-swamp", ModPlanets.shinrin, 98) {
            {
                localizedName = "Emerald Swamp";
                description = "The sector from which the war with Ozore was controlled"+"[red]WARNING!Impossible sector!Use this sector only for Chlorophylum farm from TX units";
                difficulty = 9;
                captureWave = 85;
            }
        };
    }
}
