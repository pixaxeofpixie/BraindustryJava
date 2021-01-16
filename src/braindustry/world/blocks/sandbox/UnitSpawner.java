package braindustry.world.blocks.sandbox;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import braindustry.content.ModFx;
import braindustry.graphics.ModShaders;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.input.DesktopInput;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.meta.StatUnit;

import java.util.Iterator;

import static braindustry.ModVars.modFunc.print;
import static mindustry.Vars.world;

public class UnitSpawner extends Block {
    public TextureRegion colorRegion;
    public UnitSpawnerBuild currentBuilding;
    public boolean choose=false;
    public static boolean local(){
        return !Vars.net.active();
    }
    public UnitSpawner(String name) {
        super(name);
        this.update = true;
        this.configurable = true;
        Events.on(EventType.TapEvent.class,(e)->{
            if (local() && e.player==Vars.player && choose && currentBuilding!=null) {
                currentBuilding.tapAt(e.tile);
            }
        });
    }

    @Override
    public void load() {
        super.load();
        colorRegion = Core.atlas.find(this.name + "-color", "air");
    }

    public class UnitEntry {
        public final UnitType unitType;
        public int amount;
        public Vec2 pos;
        public Team team;

        public UnitEntry(UnitType unitType, Team team, int amount, Vec2 pos) {
            this.unitType = unitType;
            this.amount = amount;
            this.pos = pos;
            this.team = team;
        }

        public void spawn() {
            for (int i = 0; i < amount; i++) {
                Unit unit = unitType.spawn(team, pos.x, pos.y);
            }
        }
    }

    public class UnitSpawnerBuild extends Building {
        Tile spawnTile;
        Team defaultUnitTeam;
        Seq<UnitEntry> unitEntries=new Seq<>();

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            UnitSpawnerBuild build = (UnitSpawnerBuild) super.init(tile, Team.derelict, shouldAdd, rotation);
            build.defaultUnitTeam = team;
            build.spawnTile=build.tile;
//            build.
            return build;
        }

        public void text(Table cont, String text) {
            cont.table(t -> {
                t.top().margin(6);
                t.add(text).growX().color(Pal.accent);
                t.row();
                t.image().fillX().height(3).pad(4).color(Pal.accent);
            }).fillX().center().row();
        }
        public Table tmpCont;
        public void openUnitDialog() {
            BaseDialog dialog = new BaseDialog("@dialog.unit-block-units.name");
            Table cont;
            dialog.cont.pane((t)-> {
                tmpCont = t;
            });
            cont=tmpCont;
            tmpCont=null;

            this.text(cont, "@text.choose-units");

            cont.table(t -> {
                int[] num = {0};
                Seq<UnitType> units = Vars.content.units();

                units.each(u -> {
                    if (u.constructor.get() instanceof BlockUnitc)return;
                            t.button(b -> {
                                        b.left();
                                        b.image(u.icon(Cicon.medium)).size(40).padRight(2);
                                        b.add(u.localizedName);
                                    },
                                    () -> {
                                        unitEntries.add(new UnitEntry(u,Vars.player.team(),1,new Vec2(spawnTile.worldx(),spawnTile.worldy())));
//                                    this.addUnit([u.id, this.getTeam(), this.getMultiplier(), this.getSpawnPos()]);
                                    }
                            ).width(188.0f).margin(12).fillX();

                            if (++num[0] % 4 == 0) t.row();
                        }
                );
            }).width(800).top().center().row();

            this.text(cont, "@text.actions-with-all-units");

            cont.table(t -> {
                t.button("@button.kill-all-units", () -> {
                    Groups.unit.each(unit -> unit.kill());
                }).growX().height(54).pad(4);

                t.button("@button.heal-all-units", () -> {
                    Groups.unit.each(unit -> unit.heal());
                }).growX().height(54).pad(4).row();

                t.button("@button.tp-all-units", () -> {
                    Groups.unit.each(unit -> unit.set(spawnTile.worldx(), spawnTile.worldy()));
                }).growX().height(54).pad(4);

                t.button("@button.damage-all-units", () -> {
                    Groups.unit.each(unit -> unit.damage(unit.health - 1));
                }).growX().height(54).pad(4).row();
            }).width(300*2f).row();



            this.text(cont, "@text.spawn");

            cont.table(t -> {
                    t.button("@button.show-all-units", () -> {
                    this.showUnitEntries();
                    }).growX().height(54).pad(4);
                t.button("@button.spawn-units", () -> {
                                    this.spawnUnits();
                }).growX().height(54).pad(4).row();

                if (Vars.spawner.getSpawns().size > 0) {
                    t.button("@button.spawn-units-on-spawners", () -> {
//                        this.createUnitsOnSpawners();
                    }).growX().height(54).pad(4);

                    t.button("@button.spawn-units-on-random-spawner", () -> {
//                        this.createUnitsOnRandomSpawner();
                    }).growX().height(54).pad(4).row();
                }
            }).width(300 * 2f).row();

            dialog.addCloseButton();
            dialog.show();
        }private int step(int amount) {
            if (amount < 10) {
                return 1;
            } else if (amount < 100) {
                return 10;
            } else {
                return amount/10;
            }
        }
        public void showUnitEntries(){
            BaseDialog dialog=new BaseDialog("@dialog.all-unit-entry");
            dialog.cont.pane((p)->{
                float bsize = 40.0F;
                int countc=0;
                for (UnitEntry unitEntry:unitEntries){
                    p.table(Tex.pane,(t)->{
                        t.margin(4.0F).marginRight(8.0F).left();
                        t.image(unitEntry.unitType.icon(Cicon.small)).size(24.0F).padRight(4.0F).padLeft(4.0F);
                        t.label(() -> {
                            return unitEntry.amount + "";
                        }).left().width(90.0F);
                        t.button(Tex.whiteui, Styles.clearTransi, 24.0F, () -> {
                            this.unitEntries.remove(unitEntry);
                            p.removeChild(t);

                        }).size(bsize).get().getStyle().imageUp=Icon.trash;
                        t.button("-", Styles.cleart, () -> {
                            unitEntry.amount = Math.max(unitEntry.amount-step(unitEntry.amount), 0);
                        }).size(bsize);
                        t.button("+", Styles.cleart, () -> {
                            unitEntry.amount+=step(unitEntry.amount);
                        }).size(bsize);
                        t.button(Icon.pencil, Styles.cleari, () -> {
                            Vars.ui.showTextInput("@configure", unitEntry.unitType.localizedName, 10, unitEntry.amount + "", true, (str) -> {
                                if (Strings.canParsePositiveInt(str)) {
                                    int amount = Strings.parseInt(str);
                                    if (amount >= 0) {
                                        unitEntry.amount = amount;
                                        return;
                                    }
                                }
                            });
                        }).size(bsize);
                    });
                    countc++;
                    if (countc%3==0)p.row();
                }
            });

            dialog.addCloseListener();
            dialog.addCloseButton();
            dialog.show();
        }
        public void spawnUnits(){
            unitEntries.each(UnitEntry::spawn);
//            unitEntries.clear();
        }
        public void tapAt(Tile tile) {
            UnitSpawner.this.currentBuilding=null;
            UnitSpawner.this.choose=false;
            spawnTile=tile;
            ModFx.blockSelect.at(tile.worldx(),tile.worldy(),1,Color.lime,Color.white);
            if (Vars.control.input instanceof DesktopInput){
                ((DesktopInput)Vars.control.input).panning=false;
            }
        }
        public void openCoordsDialog(){
            if (!local()) {
                print("un active");
                return;
            }
            if (UnitSpawner.this.currentBuilding!=null && UnitSpawner.this.currentBuilding.isValid())return;
            Tile tile=this.tile.nearby(-Mathf.floor(this.block.size/2f),0);
            if (Vars.control.input instanceof DesktopInput){
                ((DesktopInput)Vars.control.input).panning=true;
            }
            Vars.ui.announce("click on need tile");
            UnitSpawner.this.currentBuilding=this;
            UnitSpawner.this.choose=true;
            Core.camera.position.set(spawnTile.worldx(),spawnTile.worldy());
//            Core.camera.position.set(this.x,this.y);
        }
        @Override
        public void buildConfiguration(Table table) {
            table.defaults().size(40);
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, () -> {
                this.spawnUnits();
            }).get().getStyle().imageUp=Icon.commandAttack;
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, () -> {
                openUnitDialog();
            }).fill().grow().get().getStyle().imageUp=Icon.play;
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, () -> {
                openCoordsDialog();
            }).fill().grow().get().getStyle().imageUp=Icon.grid;
        }

        public boolean selected() {
            return Vars.control.input.frag.config.getSelectedTile() == this;
        }

        @Override
        public void drawTeam() {
        }

        @Override
        public void drawTeamTop() {
        }

        @Override
        public void draw() {
            Draw.rect(this.block.region, this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
            Draw.draw(Draw.z(), () -> {
                Vec2 vec = Core.input.mouseWorld(Vars.control.input.getMouseX(), Vars.control.input.getMouseY());
                Building build = world.buildWorld(vec.x, vec.y);
                if (build == this) {
                    drawCursor();
                } else {
                    Draw.color(Color.gray);
                    Draw.rect(UnitSpawner.this.colorRegion, this.x, this.y);
                }
            });
        }

        public void drawCursor() {
            ModShaders.rainbow.offsetId=this.id;
            Draw.shader(ModShaders.rainbow);
            Draw.rect(UnitSpawner.this.colorRegion, this.x, this.y);
            Draw.shader();
        }

        @Override
        public void damage(float damage) {
            ModFx.shieldWave.at(this.x, this.y, this.block.size + 1, Pal.shield,this.block.size*8f);
        }

        public void damage(float amount, boolean withEffect) {
            this.damage(amount);
        }

        public boolean collision(Bullet other) {
            this.damage(other.damage() * other.type().buildingDamageMultiplier);
            return false;
        }

        @Override
        public void damageContinuousPierce(float amount) {
            super.damageContinuousPierce(amount);
        }

        public void drawUnitPlace() {

        }

        @Override
        public void updateTile() {
            super.updateTile();
            this.team = Team.derelict;
        }

        @Override
        public void display(Table table) {
            table.table((t) -> {
                t.left();
                t.add(new Image(this.block.getDisplayIcon(this.tile))).size(32.0F);
                t.labelWrap(this.block.getDisplayName(this.tile)).left().width(190.0F).padLeft(5.0F);
            }).growX().left();
            table.row();

            table.table((bars) -> {
                bars.defaults().growX().height(18.0F).pad(4.0F);
                this.displayBars(bars);
            }).growX();
            table.row();
            table.table(this::displayConsumption).growX();
            boolean displayFlow = (this.block.category == Category.distribution || this.block.category == Category.liquid) && Core.settings.getBool("flow") && this.block.displayFlow;
            if (displayFlow) {
                String ps = " " + StatUnit.perSecond.localized();
                if (this.items != null) {
                    table.row();
                    table.left();
                    table.table((l) -> {
                        Bits current = new Bits();
                        Runnable rebuild = () -> {
                            l.clearChildren();
                            l.left();
                            Iterator var3 = Vars.content.items().iterator();

                            while (var3.hasNext()) {
                                Item item = (Item) var3.next();
                                if (this.items.hasFlowItem(item)) {
                                    l.image(item.icon(Cicon.small)).padRight(3.0F);
                                    l.label(() -> {
                                        return this.items.getFlowRate(item) < 0.0F ? "..." : Strings.fixed(this.items.getFlowRate(item), 1) + ps;
                                    }).color(Color.lightGray);
                                    l.row();
                                }
                            }

                        };
                        rebuild.run();
                        l.update(() -> {
                            Iterator var3 = Vars.content.items().iterator();

                            while (var3.hasNext()) {
                                Item item = (Item) var3.next();
                                if (this.items.hasFlowItem(item) && !current.get(item.id)) {
                                    current.set(item.id);
                                    rebuild.run();
                                }
                            }

                        });
                    }).left();
                }

                if (this.liquids != null) {
                    table.row();
                    table.table((l) -> {
                        boolean[] had = new boolean[]{false};
                        Runnable rebuild = () -> {
                            l.clearChildren();
                            l.left();
                            l.image(() -> {
                                return this.liquids.current().icon(Cicon.small);
                            }).padRight(3.0F);
                            l.label(() -> {
                                return this.liquids.getFlowRate() < 0.0F ? "..." : Strings.fixed(this.liquids.getFlowRate(), 2) + ps;
                            }).color(Color.lightGray);
                        };
                        l.update(() -> {
                            if (!had[0] && this.liquids.hadFlow()) {
                                had[0] = true;
                                rebuild.run();
                            }

                        });
                    }).left();
                }
            }

            if (Vars.net.active() && this.lastAccessed != null) {
                table.row();
                table.add(Core.bundle.format("lastaccessed", new Object[]{this.lastAccessed})).growX().wrap().left();
            }

            table.marginBottom(-5.0F);

        }

        @Override
        public boolean interactable(Team team) {
            return true;
        }

    }
}
