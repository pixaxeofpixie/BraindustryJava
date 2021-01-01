package Main.ModContent.content;

import arc.graphics.Color;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.type.Liquid;

import static Main.modVars.modFunc.*;

public class ModLiquids implements ContentList {
    public static Liquid liquidGraphenite;
    public static Liquid magma;
    public static Liquid thoriumRefrigerant;

    @Override
    public void load() {
        liquidGraphenite = new Liquid("liquid-graphenite") {
            {
                this.localizedName = "Liquid Graphenite";
                this.description = "Strange slurry with high heat capacity.";
                this.temperature = 0.5f;
                this.viscosity = 1.2f;
                this.color = Color.valueOf("5b5780");
                addResearch(Liquids.slag, this);
            }
        };
        magma = new Liquid("magma") {
            {
                this.localizedName = "Magma";
                this.description = "Very hot slurry. Can be used in Magma Generator.";
                this.temperature = 100f;
                this.heatCapacity = 0;
                this.flammability = 0;
                this.explosiveness = 0;
                this.viscosity = 0.7f;
                this.color = Color.valueOf("c9523a");
                addResearch(Liquids.slag, this);
            }
        };
        thoriumRefrigerant = new Liquid("thorium-refrigerant") {
            {
                this.localizedName = "Thorium Refrigerant";
                this.description = "A cool liquid, made from Thorium and Cryofluid.";
                this.temperature = 0.01f;
                this.heatCapacity = 1.32f;
                this.viscosity = 0.9f;
                this.color = Color.valueOf("dac5fc");
                addResearch(Liquids.cryofluid, this);
            }
        };
    }
}
