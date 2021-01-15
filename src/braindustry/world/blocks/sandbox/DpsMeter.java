package braindustry.world.blocks.sandbox;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import braindustry.world.blocks.BuildingLabel;
import mindustry.entities.TargetPriority;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.ui.Fonts;
import mindustry.world.Block;

public class DpsMeter extends Block {
    public DpsMeter(String name) {
        super(name);
        this.placeableLiquid = true;
        this.configurable = true;
        this.update = true;
//        this.
        this.rebuildable = true;
        this.floating = true;
        this.priority = TargetPriority.turret;
        unitCapModifier = 1000;
    }

    public class DpsMeterBuild extends Building implements BuildingLabel {
        float deltaHealth = 0;
        float editHHealth = 0;
        Team selectedTeam;
        float t = 60;

        @Override
        public void update() {
            super.update();
            if (this.selectedTeam != null) {
                this.team = this.selectedTeam;
            } else {
                this.selectedTeam = this.team;
            }

            if (this.timer.get(0, this.t)) {
                this.deltaHealth = this.block.health - this.editHHealth;

                this.editHHealth = this.block.health;
            }
        }

        @Override
        public void damage(float damage) {
            this.editHHealth -= damage;
        }

        @Override
        public boolean interactable(Team team) {
            return true;
        }

        @Override
        public boolean collision(Bullet other) {
            this.damage(other.damage*other.type.buildingDamageMultiplier);

            return true;
        }

        @Override
        public void placed() {
            super.placed();
            this.editHHealth=block.health;
            this.selectedTeam = DpsMeter.this.lastConfig == null ? this.team : DpsMeter.this.lastConfig instanceof Team ? (Team) DpsMeter.this.lastConfig : this.team;
        }

        @Override
        public void draw() {
            super.draw();
            StringBuilder amount;
            if (this.deltaHealth < 5) {
                amount = new StringBuilder("" + this.deltaHealth);
            } else {
                amount = new StringBuilder("" + (int) Mathf.round(this.deltaHealth, 1f));
            }
            for (;amount.length() < 7; amount.insert(0, "0")) {
            }

            float textSize = 0.23f;
            Font font = Fonts.outline;
            boolean ints = font.usesIntegerPositions();
            font.getData().setScale(textSize / Scl.scl(1.0f));
            font.setUseIntegerPositions(false);

            font.setColor(Color.white);

            float z = Draw.z();
            Draw.z(300);
            font.draw(amount.toString(), this.x - this.block.size * 4, this.y + 1);
            Draw.z(z);

            font.setUseIntegerPositions(ints);
            font.getData().setScale(1);
        }
    }
}
