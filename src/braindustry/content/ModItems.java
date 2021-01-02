package braindustry.content;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Item;

import static braindustry.modVars.modFunc.*;

public class ModItems implements ContentList {
    static {
        Items.class.isArray();
    }

    public static Item chromium, exoticAlloy, graphenite, odinum, phaseAlloy, plastic;

    public void load() {
        chromium = new Item("chromium") {
            {
                this.localizedName = "Chromium";
                this.description = "A very infusible resource.";
                this.hardness = 4;
                this.cost = 2;
                this.color = Color.valueOf("dededf");
            }
        };

        exoticAlloy = new Item("exotic-alloy") {
            {
                this.localizedName="Exo Metal";
                this.description="Heavy organic metal with high durability and low weight.";
                this.cost = 3;
                this.alwaysUnlocked = true;
                this.explosiveness = 0.02f;
                this.color = Color.valueOf("e2f723");
                this.radioactivity = 0;
                this.flammability = 0.06f;
            }
        };

        graphenite = new Item("graphenite") {
            {
                this.localizedName = "Graphenite";
                this.description = "Progressed synthetic metal with low weight.";
                this.cost = 2;
                this.alwaysUnlocked = true;
                this.color = Color.valueOf("9868AB");
                this.explosiveness = 0;
                this.radioactivity = 0;
                this.flammability = 0;
            }
        };

        odinum = new Item("odinum") {
            {
                this.localizedName="Odinum";
                this.description="A radioactive resource.";
                this.radioactivity=2;
                this.hardness=5;
                this.cost=2;
                this.color=Color.valueOf("bddedd");
            }
        };

        phaseAlloy = new Item("phase-alloy") {
            {
                this.localizedName = "Phase Alloy";
                this.description = "This alloy combines features of Platanium, Surge Alloy and Phase fabric.";
                this.flammability = 0;
                this.explosiveness = 0;
                this.radioactivity = 0;
                this.cost = 2;
                this.color = Color.valueOf("FF4F00");
            }
        };

        plastic = new Item("plastic") {
            {
                this.localizedName="Plastic";
                this.description="A lightweight and durable material.";
                this.flammability=0;
                this.explosiveness=0;
                this.radioactivity=0.01f;
                this.cost=4;
                this.color = Color.valueOf("f6bfff");
            }
        };
    }
}