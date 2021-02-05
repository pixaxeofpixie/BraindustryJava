package braindustry.ui.fragments;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.event.Touchable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Scaling;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.ModShaders;
import mindustry.content.StatusEffects;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.game.SpawnGroup;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.Binding;
import mindustry.net.Packets;
import mindustry.type.Item;
import mindustry.ui.*;
import mindustry.ui.fragments.Fragment;
import mindustry.ui.fragments.HudFragment;

import static mindustry.Vars.*;

public class ModHudFragment extends HudFragment {
    private static final float dsize = 65f;
    private ImageButton flip;
    private CoreItemsDisplay coreItems = new CoreItemsDisplay();

    private String hudText = "";
    private boolean showHudText;

    private Table lastUnlockTable;
    private Table lastUnlockLayout;
    private long lastToast;
    protected static void clear(Element el){
        el.clearActions();
        el.clearListeners();
        el.clear();
    }

    public static void init() {
        WidgetGroup hudGroup = ui.hudGroup;
        hudGroup.getChildren().each(Core.scene.root::removeChild);
        Seq.<Element>with(hudGroup,ui.chatfrag,ui.scriptfrag).each(Core.scene.root::removeChild);
        hudGroup.clearChildren();
        hudGroup.clear();
        hudGroup.remove();
        clear(ui.minimapfrag.elem);
        newHudGroup();
        WidgetGroup  newGroup=ui.hudGroup;

        Group group=new Group() {};
        ui.hudfrag.blockfrag.build(group);
        ui.hudfrag=new ModHudFragment();
        ui.hudfrag.build(newGroup);
        ui.chatfrag.container().build(newGroup);
        ui.minimapfrag.build(newGroup);
        ui.listfrag.build(newGroup);
        ui. scriptfrag.container().build(newGroup);
    }
    protected static void newHudGroup(){
        ui.hudGroup = new WidgetGroup();
        ui.hudGroup.setFillParent(true);
        ui.hudGroup.touchable = Touchable.childrenOnly;
        ui.hudGroup.visible(() -> state.isGame());
        Core.scene.add(ui.hudGroup);
    }
    @Override
    public void build(Group parent) {

        //warn about guardian/boss waves
        /*
        Events.on(EventType.WaveEvent.class, e -> {
            int max = 10;
            int winWave = state.isCampaign() && state.rules.winWave > 0 ? state.rules.winWave : Integer.MAX_VALUE;
            outer:
            for (int i = state.wave - 1; i <= Math.min(state.wave + max, winWave - 2); i++) {
                for (SpawnGroup group : state.rules.spawns) {
                    if (group.effect == StatusEffects.boss && group.getSpawned(i) > 0) {
                        int diff = (i + 2) - state.wave;

                        //increments at which to warn about incoming guardian
                        if (diff == 1 || diff == 2 || diff == 5 || diff == 10) {
                            showToast(Icon.warning, Core.bundle.format("wave.guardianwarn" + (diff == 1 ? ".one" : ""), diff));
                        }

                        break outer;
                    }
                }
            }
        });

        Events.on(EventType.SectorCaptureEvent.class, e -> {
            showToast(Core.bundle.format("sector.captured", e.sector.isBeingPlayed() ? "" : e.sector.name() + " "));
        });

        Events.on(EventType.SectorLoseEvent.class, e -> {
            showToast(Icon.warning, Core.bundle.format("sector.lost", e.sector.name()));
        });

        Events.on(EventType.SectorInvasionEvent.class, e -> {
            showToast(Icon.warning, Core.bundle.format("sector.attacked", e.sector.name()));
        });

        Events.on(EventType.ResetEvent.class, e -> {
            coreItems.resetUsed();
            coreItems.clear();
        });
*/
        //paused table
        parent.fill(t -> {
            t.name = "paused";
            t.top().visible(() -> state.isPaused() && shown).touchable = Touchable.disabled;
            t.table(Styles.black5, top -> top.label(() -> state.gameOver && state.isCampaign() ? "@sector.curlost" : "@paused").style(Styles.outlineLabel).pad(8f)).growX();
        });

        //minimap + position
        parent.fill(t -> {
            t.name = "minimap/position";
            t.visible(() -> Core.settings.getBool("minimap") && shown);
            //minimap
            t.add(new Minimap()).name("minimap");
            t.row();
            //position
            t.label(() -> player.tileX() + "," + player.tileY())
                    .visible(() -> Core.settings.getBool("position"))
                    .touchable(Touchable.disabled)
                    .name("position");
            t.top().right();
        });

        ui.hints.build(parent);

        //menu at top left
        parent.fill(cont -> {
            cont.name = "overlaymarker";
            cont.top().left();

            if (mobile) {
                cont.table(select -> {
                    select.name = "mobile buttons";
                    select.left();
                    select.defaults().size(dsize).left();

                    ImageButton.ImageButtonStyle style = Styles.clearTransi;

                    select.button(Icon.menu, style, ui.paused::show).name("menu");
                    flip = select.button(Icon.upOpen, style, this::toggleMenus).get();
                    flip.name = "flip";

                    select.button(Icon.paste, style, ui.schematics::show)
                            .name("schematics");

                    select.button(Icon.pause, style, () -> {
                        if (net.active()) {
                            ui.listfrag.toggle();
                        } else {
                            state.set(state.is(GameState.State.paused) ? GameState.State.playing : GameState.State.paused);
                        }
                    }).name("pause").update(i -> {
                        if (net.active()) {
                            i.getStyle().imageUp = Icon.players;
                        } else {
                            i.setDisabled(false);
                            i.getStyle().imageUp = state.is(GameState.State.paused) ? Icon.play : Icon.pause;
                        }
                    });

                    select.button(Icon.chat, style, () -> {
                        if (net.active() && mobile) {
                            if (ui.chatfrag.shown()) {
                                ui.chatfrag.hide();
                            } else {
                                ui.chatfrag.toggle();
                            }
                        } else if (state.isCampaign()) {
                            ui.research.show();
                        } else {
                            ui.database.show();
                        }
                    }).name("chat").update(i -> {
                        if (net.active() && mobile) {
                            i.getStyle().imageUp = Icon.chat;
                        } else if (state.isCampaign()) {
                            i.getStyle().imageUp = Icon.tree;
                        } else {
                            i.getStyle().imageUp = Icon.book;
                        }
                    });

                    select.image().color(Pal.gray).width(4f).fillY();
                });

                cont.row();
                cont.image().height(4f).color(Pal.gray).fillX();
                cont.row();
            }

            cont.update(() -> {
                if (Core.input.keyTap(Binding.toggle_menus) && !ui.chatfrag.shown() && !Core.scene.hasDialog() && !(Core.scene.getKeyboardFocus() instanceof TextField)) {
                    Core.settings.getBoolOnce("ui-hidden", () -> {
                        ui.announce(Core.bundle.format("showui", Core.keybinds.get(Binding.toggle_menus).key.toString(), 11));
                    });
                    toggleMenus();
                }
            });

            Table wavesMain, editorMain;

            cont.stack(wavesMain = new Table(), editorMain = new Table()).height(wavesMain.getPrefHeight())
                    .name("waves/editor");

            wavesMain.visible(() -> shown && !state.isEditor());
            wavesMain.top().left().name = "waves";

            wavesMain.table(s -> {
                //wave info button with text
                s.add(makeStatusTable()).grow().name("status");

                //table with button to skip wave
                s.button(Icon.play, Styles.righti, 30f, () -> {
                    if (net.client() && player.admin) {
                        Call.adminRequest(player, Packets.AdminAction.wave);
                    } else {
                        logic.skipWave();
                    }
                }).growY().fillX().right().width(40f).disabled(b -> !canSkipWave()).name("skip");
            }).width(dsize * 5 + 4f);

            wavesMain.row();

            wavesMain.table(Tex.button, t -> t.margin(10f).add(new Bar("boss.health", Pal.health, () -> state.boss() == null ? 0f : state.boss().healthf()).blink(Color.white))
                    .grow()).fillX().visible(() -> state.rules.waves && state.boss() != null).height(60f).name("boss");

            wavesMain.row();

            editorMain.name = "editor";
            editorMain.table(Tex.buttonEdge4, t -> {
                //t.margin(0f);
                t.name = "teams";
                t.add("@editor.teams").growX().left();
                t.row();
                t.table(teams -> {
                    teams.left();
                    int i = 0;
                    for (Team team : Team.baseTeams) {
                        ImageButton button = teams.button(Tex.whiteui, Styles.clearTogglePartiali, 40f, () -> Call.setPlayerTeamEditor(player, team))
                                .size(50f).margin(6f).get();
                        button.getImageCell().grow();
                        button.getStyle().imageUpColor = team.color;
                        button.update(() -> button.setChecked(player.team() == team));

                        if (++i % 3 == 0) {
                            teams.row();
                        }
                    }
                }).left();
            }).width(dsize * 5 + 4f);
            editorMain.visible(() -> shown && state.isEditor());

            //fps display
            cont.table(info -> {
                info.name = "fps/ping";
                info.touchable = Touchable.disabled;
                info.top().left().margin(4).visible(() -> Core.settings.getBool("fps") && shown);
                IntFormat fps = new IntFormat("fps");
                IntFormat ping = new IntFormat("ping");
                IntFormat mem = new IntFormat("memory");
                IntFormat memnative = new IntFormat("memory2");

                info.label(() -> fps.get(Core.graphics.getFramesPerSecond())).left().style(Styles.outlineLabel).name("fps");
                info.row();

                if (android) {
                    info.label(() -> memnative.get((int) (Core.app.getJavaHeap() / 1024 / 1024), (int) (Core.app.getNativeHeap() / 1024 / 1024))).left().style(Styles.outlineLabel).name("memory2");
                } else {
                    info.label(() -> mem.get((int) (Core.app.getJavaHeap() / 1024 / 1024))).left().style(Styles.outlineLabel).name("memory");
                }
                info.row();

                info.label(() -> ping.get(netClient.getPing())).visible(net::client).left().style(Styles.outlineLabel).name("ping");

            }).top().left();
        });

        //core items
        parent.fill(t -> {
            t.name = "coreitems";
            t.top().add(coreItems);
            t.visible(() -> Core.settings.getBool("coreitems") && !mobile && !state.isPaused() && shown);
        });

        //spawner warning
        parent.fill(t -> {
            t.name = "nearpoint";
            t.touchable = Touchable.disabled;
            t.table(Styles.black, c -> c.add("@nearpoint")
                    .update(l -> l.setColor(Tmp.c1.set(Color.white).lerp(Color.scarlet, Mathf.absin(Time.time, 10f, 1f))))
                    .get().setAlignment(Align.center, Align.center))
                    .margin(6).update(u -> u.color.a = Mathf.lerpDelta(u.color.a, Mathf.num(spawner.playerNear()), 0.1f)).get().color.a = 0f;
        });

        parent.fill(t -> {
            t.name = "waiting";
            t.visible(() -> netServer.isWaitingForPlayers());
            t.table(Tex.button, c -> c.add("@waiting.players"));
        });

        //'core is under attack' table
        parent.fill(t -> {
            t.name = "coreattack";
            t.touchable = Touchable.disabled;
            float notifDuration = 240f;
            float[] coreAttackTime = {0};
            float[] coreAttackOpacity = {0};

            Events.run(EventType.Trigger.teamCoreDamage, () -> {
                coreAttackTime[0] = notifDuration;
            });

            t.top().visible(() -> {
                if (!shown) return false;
                if (state.isMenu() || !state.teams.get(player.team()).hasCore()) {
                    coreAttackTime[0] = 0f;
                    return false;
                }

                t.color.a = coreAttackOpacity[0];
                if (coreAttackTime[0] > 0) {
                    coreAttackOpacity[0] = Mathf.lerpDelta(coreAttackOpacity[0], 1f, 0.1f);
                } else {
                    coreAttackOpacity[0] = Mathf.lerpDelta(coreAttackOpacity[0], 0f, 0.1f);
                }

                coreAttackTime[0] -= Time.delta;

                return coreAttackOpacity[0] > 0;
            });
            t.table(Tex.button, top -> top.add("@coreattack").pad(2)
                    .update(label -> label.color.set(Color.orange).lerp(Color.scarlet, Mathf.absin(Time.time, 2f, 1f)))).touchable(Touchable.disabled);
        });

        //'saving' indicator
        parent.fill(t -> {
            t.name = "saving";
            t.bottom().visible(() -> control.saves.isSaving());
            t.add("@saving").style(Styles.outlineLabel);
        });

        parent.fill(p -> {
            p.name = "hudtext";
            p.top().table(Styles.black3, t -> t.margin(4).label(() -> hudText)
                    .style(Styles.outlineLabel)).padTop(10).visible(p.color.a >= 0.001f);
            p.update(() -> {
                p.color.a = Mathf.lerpDelta(p.color.a, Mathf.num(showHudText), 0.2f);
                if (state.isMenu()) {
                    p.color.a = 0f;
                    showHudText = false;
                }
            });
            p.touchable = Touchable.disabled;
        });

        //TODO DEBUG: rate table
        if (false)
            parent.fill(t -> {
                t.name = "rates";
                t.bottom().left();
                t.table(Styles.black6, c -> {
                    Bits used = new Bits(content.items().size);

                    Runnable rebuild = () -> {
                        c.clearChildren();

                        for (Item item : content.items()) {
                            if (state.rules.sector != null && state.rules.sector.info.getExport(item) >= 1) {
                                c.image(item.icon(Cicon.small));
                                c.label(() -> (int) state.rules.sector.info.getExport(item) + " /s").color(Color.lightGray);
                                c.row();
                            }
                        }
                    };

                    c.update(() -> {
                        boolean wrong = false;
                        for (Item item : content.items()) {
                            boolean has = state.rules.sector != null && state.rules.sector.info.getExport(item) >= 1;
                            if (used.get(item.id) != has) {
                                used.set(item.id, has);
                                wrong = true;
                            }
                        }
                        if (wrong) {
                            rebuild.run();
                        }
                    });
                }).visible(() -> state.isCampaign() && content.items().contains(i -> state.rules.sector != null && state.rules.sector.info.getExport(i) > 0));
            });

        blockfrag.build(parent);
    }

    private void scheduleToast(Runnable run) {
        long duration = (int) (3.5 * 1000);
        long since = Time.timeSinceMillis(lastToast);
        if (since > duration) {
            lastToast = Time.millis();
            run.run();
        } else {
            Time.runTask((duration - since) / 1000f * 60f, run);
            lastToast += duration;
        }
    }

    private void toggleMenus() {
        if (flip != null) {
            flip.getStyle().imageUp = shown ? Icon.downOpen : Icon.upOpen;
        }

        shown = !shown;
    }

    private Table makeStatusTable() {
        Table table = new Table(Tex.wavepane);

        StringBuilder ibuild = new StringBuilder();

        IntFormat wavef = new IntFormat("wave");
        IntFormat wavefc = new IntFormat("wave.cap");
        IntFormat enemyf = new IntFormat("wave.enemy");
        IntFormat enemiesf = new IntFormat("wave.enemies");
        IntFormat enemycf = new IntFormat("wave.enemycore");
        IntFormat enemycsf = new IntFormat("wave.enemycores");
        IntFormat waitingf = new IntFormat("wave.waiting", i -> {
            ibuild.setLength(0);
            int m = i / 60;
            int s = i % 60;
            if (m > 0) {
                ibuild.append(m);
                ibuild.append(":");
                if (s < 10) {
                    ibuild.append("0");
                }
            }
            ibuild.append(s);
            return ibuild.toString();
        });

        table.touchable = Touchable.enabled;

        StringBuilder builder = new StringBuilder();

        table.name = "waves";

        table.marginTop(0).marginBottom(4).marginLeft(4);

        class SideBar extends Element {
            public final Floatp amount;
            public final boolean flip;
            public final Boolp flash;

            float last, blink, value;

            public SideBar(Floatp amount, Boolp flash, boolean flip) {
                this.amount = amount;
                this.flip = flip;
                this.flash = flash;

                setColor(Pal.health);
            }

            @Override
            public void draw() {
                float next = amount.get();

                if (Float.isNaN(next) || Float.isInfinite(next)) next = 1f;

                if (next < last && flash.get()) {
                    blink = 1f;
                }

                blink = Mathf.lerpDelta(blink, 0f, 0.2f);
                value = Mathf.lerpDelta(value, next, 0.15f);
                last = next;

                if (Float.isNaN(value) || Float.isInfinite(value)) value = 1f;

                drawInner(Pal.darkishGray);

                Draw.beginStencil();

                Fill.crect(x, y, width, height * value);

                Draw.beginStenciled();

                drawInner(Tmp.c1.set(color).lerp(Color.white, blink));

                Draw.endStencil();
            }

            void drawInner(Color color) {
                if (flip) {
                    x += width;
                    width = -width;
                }

                float stroke = width * 0.35f;
                float bh = height / 2f;
                Draw.color(color);

                Fill.quad(
                        x, y,
                        x + stroke, y,
                        x + width, y + bh,
                        x + width - stroke, y + bh
                );

                Fill.quad(
                        x + width, y + bh,
                        x + width - stroke, y + bh,
                        x, y + height,
                        x + stroke, y + height
                );

                Draw.reset();

                if (flip) {
                    width = -width;
                    x -= width;
                }
            }
        }

        table.stack(
                new Element() {
                    @Override
                    public void draw() {
                        Draw.color(Pal.darkerGray);
                        float radius = height / Mathf.sqrt3;
                        Fill.poly(x + width/2f, y + height/2f, 6, radius);
                        Draw.reset();
//                        Draw.flush();
                        if (player!=null && player.unit() instanceof StealthUnitc){
                            StealthUnitc unit=player.unit().as();
                            float offset = unit.stealthf();
                            Draw.color(Color.valueOf("6966A1"));
                            ModShaders.iconBackgroundShader.set(y+(radius*2f )* offset);
                            Fill.poly(x + width / 2f, y + height / 2f, 6, radius);
                            Draw.shader();
                            Draw.reset();
//                            Draw.flush();
                        }
                        Drawf.shadow(x + width / 2f, y + height / 2f, height * 1.13f);
                    }
                },
                new Table(t -> {
                    float bw = 40f;
                    float pad = -20;
                    t.margin(0);
                    t.clicked(() -> {
                        if (!player.dead() && mobile) {
                            Call.unitClear(player);
                            control.input.controlledType = null;
                        }
                    });

                    t.add(new SideBar(() -> player.unit().healthf(), () -> true, true)).width(bw).growY().padRight(pad);
                    t.image(() -> player.icon()).scaling(Scaling.bounded).grow().maxWidth(54f);
                    t.add(new SideBar(() -> player.dead() ? 0f : player.displayAmmo() ? player.unit().ammof() : player.unit().healthf(), () -> !player.displayAmmo(), false)).width(bw).growY().padLeft(pad).update(b -> {
                        b.color.set(player.displayAmmo() ? player.dead() || player.unit() instanceof BlockUnitc ? Pal.ammo : player.unit().type.ammoType.color : Pal.health);
                    });

                    t.getChildren().get(1).toFront();
                })).size(120f, 80).padRight(4);

        table.labelWrap(() -> {
            builder.setLength(0);

            if (!state.rules.waves && state.rules.attackMode) {
                int sum = Math.max(state.teams.present.sum(t -> t.team != player.team() ? t.cores.size : 0), 1);
                builder.append(sum > 1 ? enemycsf.get(sum) : enemycf.get(sum));
                return builder;
            }

            if (!state.rules.waves && state.isCampaign()) {
                builder.append("[lightgray]").append(Core.bundle.get("sector.curcapture"));
            }

            if (!state.rules.waves) {
                return builder;
            }

            if (state.rules.winWave > 1 && state.rules.winWave >= state.wave && state.isCampaign()) {
                builder.append(wavefc.get(state.wave, state.rules.winWave));
            } else {
                builder.append(wavef.get(state.wave));
            }
            builder.append("\n");

            if (state.enemies > 0) {
                if (state.enemies == 1) {
                    builder.append(enemyf.get(state.enemies));
                } else {
                    builder.append(enemiesf.get(state.enemies));
                }
                builder.append("\n");
            }

            if (state.rules.waveTimer) {
                builder.append((logic.isWaitingWave() ? Core.bundle.get("wave.waveInProgress") : (waitingf.get((int) (state.wavetime / 60)))));
            } else if (state.enemies == 0) {
                builder.append(Core.bundle.get("waiting"));
            }

            return builder;
        }).growX().pad(8f);

        table.row();

        float[] count = new float[]{-1};
        table.table().update(t -> {
            if (player.unit() instanceof Payloadc) {
                Payloadc payload = player.unit().as();
                if (count[0] != payload.payloadUsed()) {
                    payload.contentInfo(t, 8 * 2, 275f);
                    count[0] = payload.payloadUsed();
                }
            } else {
                count[0] = -1;
                t.clear();
            }
        }).growX().visible(() -> player.unit() instanceof Payloadc && ((Payloadc) player.unit()).payloadUsed() > 0).colspan(2);

        return table;
    }

    private boolean canSkipWave() {
        return state.rules.waves && ((net.server() || player.admin) || !net.active()) && state.enemies == 0 && !spawner.isSpawning();
    }
}
