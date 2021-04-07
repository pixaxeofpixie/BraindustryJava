package braindustry.content;

import arc.graphics.Color;
import mindustry.ctype.ContentList;
import mindustry.type.Liquid;

public class ModLiquids implements ContentList {
    public static Liquid liquidGraphenite, magma, thoriumRefrigerant, liquidMethane;

    @Override
    public void load() {
        liquidGraphenite = new Liquid("liquid-graphenite") {
            {
                this.localizedName = "Liquid Graphenite";
                this.description = "Strange slurry with high heat capacity.";
                this.temperature = 0.5f;
                this.viscosity = 1.2f;
                this.color = Color.valueOf("5b5780");
            }
        };

        magma = new Liquid("magma") {
            {
                this.localizedName = "Magma";
                this.description = "Very hot slurry. Can be used in Magma Generator.";
                this.temperature = 9f;
                this.heatCapacity = 0;
                this.flammability = 0;
                this.explosiveness = 0;
                this.viscosity = 0.7f;
                this.color = Color.valueOf("D04954");
                lightColor = Color.valueOf("FF5C77").a(0.5f);
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
                this.lightColor = Color.valueOf("EBCCFF").a(0.3f);
            }
        };
        liquidMethane = new Liquid("liquid-methane") {
            {
                this.localizedName = "Liquid Methane";
                this.description = "Liquid version of Methane.";
                this.temperature = 0.024f;
                this.heatCapacity = 1.13f;
                this.viscosity = 0.7f;
                this.color = Color.valueOf("37f29b");
                this.lightColor = Color.valueOf("A3FFE8").a(0.3f);
            }
        };
    }
}
