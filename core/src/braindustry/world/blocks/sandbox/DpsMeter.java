package braindustry.world.blocks.sandbox;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.TextArea;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.ReusableByteInStream;
import arc.util.io.ReusableByteOutStream;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.gen.ModBuilding;
import braindustry.gen.WritableInterface;
import braindustry.tools.ModReads;
import braindustry.tools.ModWrites;
import braindustry.world.blocks.BuildingLabel;
import mindustry.Vars;
import mindustry.entities.TargetPriority;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Icon;
import mindustry.io.JsonIO;
import mindustry.io.TypeIO;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustryAddition.graphics.ModDraw;

import java.io.DataOutputStream;
import java.util.Arrays;

public class DpsMeter extends Block {
    public TextureRegion teamRegionButton;

    public DpsMeter(String name) {
        super(name);
        placeableLiquid = true;
        configurable = true;
        update = true;
//
        rebuildable = true;
        floating = true;
        priority = TargetPriority.turret;
        unitCapModifier = 1000;
        health = Integer.MAX_VALUE;
        config(MeterContainer.class, (t, conteiner) -> {
            DpsMeterBuild tile = (DpsMeterBuild) t;
            tile.container = conteiner == null ? new MeterContainer() : conteiner;
            Vars.indexer.updateIndices(tile.tile);
        });
        config(Team.class, (t, team) -> {
            DpsMeterBuild tile = (DpsMeterBuild) t;
            tile.container.selectedTeam=team==null?Team.derelict:team;
            Vars.indexer.updateIndices(tile.tile);
        });
        config(Float.class, (t, time) -> {
            DpsMeterBuild tile = (DpsMeterBuild) t;
            tile.container.time=time==null?0f:time;
            Vars.indexer.updateIndices(tile.tile);
        });
        config(Boolean.class, (t, unit) -> {
            DpsMeterBuild tile = (DpsMeterBuild) t;
            tile.container.unit= unit != null && unit;
            Vars.indexer.updateIndices(tile.tile);
        });
    }

    @Override
    public void load() {
        super.load();
        teamRegionButton = Core.atlas.find(name + "-team-region");
    }

    @Override
    protected TextureRegion[] icons() {
        return this.teamRegion.found() ? new TextureRegion[]{this.region, this.teamRegions[Team.sharded.id]} : new TextureRegion[]{this.region};
    }
    @ModAnnotations.WritableObject
    public static class MeterContainer implements  WritableInterface {
        public float time = 60;
        public Team selectedTeam = Team.derelict;
        public boolean unit = false;
        public MeterContainer() {
        }

        public void write(Writes write) {
            write.bool(unit);
            write.f(time);
            TypeIO.writeTeam(write,selectedTeam);
        }

        public void read(Reads read) {
            unit=read.bool();
            time=read.f();
            selectedTeam=TypeIO.readTeam(read);
        }
    }

    public class DpsMeterBuild extends ModBuilding implements BuildingLabel {
        float deltaHealth = 0;
        float editHHealth = 0;
        MeterContainer container;
        float scrollPos = 0;

        @Override
        public void playerPlaced(Object config) {
            configure(config);
        }
protected void configure(){
            configure(container);
}
        @Override
        public void created() {
            super.created();
            container = new MeterContainer();
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if (container != null) {
                team = container.selectedTeam;
            } else {
                container = new MeterContainer();
            }

            if (timer.get(0, container.time)) {
//                Log.info("t");
//                this.deltaHealth = maxHealth-this.health;
                deltaHealth = editHHealth;
                editHHealth = 0;
//                deltaHealth = editHHealth;
//                editHHealth =maxHealth();
//                health=maxHealth;
            }
        }

        @Override
        public void damage(float damage) {
            if (Mathf.zero(Vars.state.rules.blockHealthMultiplier)) {
                damage = this.health + 1.0F;
            } else {
                damage /= Vars.state.rules.blockHealthMultiplier;
            }
//            Log.info("damage @",damage);
//            editHHealth -= damage;
            editHHealth -= damage;
//            health -= damage;
        }

        @Override
        public boolean interactable(Team team) {
            return true;
        }


        @Override
        public void placed() {
            super.placed();
            editHHealth = block.health;
            container = lastConfig == null ? new MeterContainer() : lastConfig instanceof MeterContainer ? (MeterContainer) lastConfig : new MeterContainer();
        }

        @Override
        public void draw() {
            super.draw();
            StringBuilder amount;

            if (deltaHealth < 5) {
                amount = new StringBuilder("" + Mathf.round(deltaHealth, 0.000001f));
            } else {
                amount = new StringBuilder("" + (int) Mathf.round(deltaHealth, 1f));
            }
            Strings.replace(amount,"-","");
            while (amount.length() < 7) {
                amount.insert(0, "0");
            }

            float textSize = 0.23f;
            Font font = Fonts.outline;
            boolean ints = font.usesIntegerPositions();
            font.getData().setScale(textSize / Scl.scl(1.0f));
            font.setUseIntegerPositions(false);

            font.setColor(Color.white);
            ModDraw.drawLabel(x-block.size*4f,y+1,amount.toString());
            float z = Draw.z();
//            Draw.z(300);
//            font.draw(amount.toString() + "", x - block.size * 4, y + 1);
//            Draw.z(z);

            font.setUseIntegerPositions(ints);
            font.getData().setScale(1);
        }

        @Override
        public boolean collision(Bullet other) {

            this.damage(other.damage * other.damageMultiplier() *
                    (container.unit ? 1f : other.type().buildingDamageMultiplier));
            return true;
        }

        @Override
        public boolean collide(Bullet other) {
            return super.collide(other);
        }

        public void addButton(Table cont, Team team) {
            cont.button(new TextureRegionDrawable(teamRegionButton).tint(team.color), Styles.clearToggleTransi, () -> {
                container.selectedTeam = team;
                configure();
                deselect();
            });
        }

        @Override
        public void buildConfiguration(Table te) {
            te.table((table) -> {
                table.background(Styles.black6);
                Table cont = new Table();
                cont.defaults().size(50);

                ScrollPane scrollPane = table.pane((t) -> {
                    for (int i = 0; i < Team.all.length; i++) {
                        Team team = Team.all[i];
                        addButton(t, team);
                        if ((i + 1) % 4 == 0) {
                            t.row();
                        }
                    }
                }).maxHeight(Scl.scl(40)).get();
                scrollPane.setScrollingDisabled(true, false);
                scrollPane.setScrollYForce(scrollPos);
                scrollPane.update(() -> {
                    scrollPos = scrollPane.getScrollY();
                });
                scrollPane.setOverscroll(false, false);
                table.button(Icon.units, () -> {
                    container.unit = !container.unit;
                    configure();
                });

                table.row();
                table.add(Core.bundle.get("dpsmeter.time"));
                table.row();
                Cell<TextArea> a = table.area(((container.time / 60f) + ""), text -> {
                    if (Strings.canParsePositiveFloat(text)) {
                        container.time = Mathf.clamp(Mathf.round(60f * Strings.parseFloat(text)), 60f, 36000f);
                    } else {
                        container.time = 60f;
                    }
                    configure();
                }).width(100);
                TextArea textArea = a.get();
                textArea.setMaxLength((Float.MAX_VALUE + "").length());

                textArea.setFilter(((textField, c) -> {
                    StringBuilder b = new StringBuilder(textArea.getText());
                    b.insert(textField.getCursorPosition(), c);
                    return (Strings.canParseFloat(b.toString()));
                }));
                //a.setMaxLength(5);
                table.add(Core.bundle.format("setting.seconds", ""));
            });

        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            container = JsonIO.json().fromJson(MeterContainer.class, read.str());
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.str(JsonIO.json().toJson(container));
        }
    }
}
