package braindustry.ui.fragments;

import arc.Application;
import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.Action;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Tmp;
import braindustry.graphics.ModMenuShaderRender;
import mindustry.Vars;
import mindustry.core.Platform;
import mindustry.core.Version;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.graphics.MenuRenderer;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.ui.MobileButton;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.ui.fragments.MenuFragment;

import java.util.Objects;

public class ModMenuFragment extends MenuFragment {
    private Table container;
    private Table submenu;
    private Button currentMenu;
    private ModMenuShaderRender renderer;

    public ModMenuFragment() {
        Events.on(EventType.DisposeEvent.class, (event) -> {
            this.renderer.dispose();
        });
    }

    public void build(Group parent) {
        this.renderer = new ModMenuShaderRender();
        Group group = new WidgetGroup();
        group.setFillParent(true);
        group.visible(() -> {
            return !Vars.ui.editor.isShown();
        });
        parent.addChild(group);
        group.fill((x, y, w, h) -> {
            this.renderer.render();
        });
        group.fill((c) -> {
            this.container = c;
            c.name = "menu container";
            if (!Vars.mobile) {
                this.buildDesktop();
                Events.on(EventType.ResizeEvent.class, (event) -> {
                    this.buildDesktop();
                });
            } else {
                this.buildMobile();
                Events.on(EventType.ResizeEvent.class, (event) -> {
                    this.buildMobile();
                });
            }

        });
        if (Vars.mobile) {
            group.fill((c) -> {
                Table var10000 = c.bottom().left();
                TextButton.TextButtonStyle var10002 = Styles.infot;
                AboutDialog var10003 = Vars.ui.about;
                Objects.requireNonNull(var10003);
                var10000.button("", var10002, var10003::show).size(84.0F, 45.0F).name("info");
            });
            group.fill((c) -> {
                Table var10000 = c.bottom().right();
                TextButton.TextButtonStyle var10002 = Styles.discordt;
                DiscordDialog var10003 = Vars.ui.discord;
                Objects.requireNonNull(var10003);
                var10000.button("", var10002, var10003::show).size(84.0F, 45.0F).name("discord");
            });
        } else if (Vars.becontrol.active()) {
            group.fill((c) -> {
                c.bottom().right().button("@be.check", Icon.refresh, () -> {
                    Vars.ui.loadfrag.show();
                    Vars.becontrol.checkUpdate((result) -> {
                        Vars.ui.loadfrag.hide();
                        if (!result) {
                            Vars.ui.showInfo("@be.noupdates");
                        }

                    });
                }).size(200.0F, 60.0F).name("becheck").update((t) -> {
                    t.getLabel().setColor(Vars.becontrol.isUpdateAvailable() ? Tmp.c1.set(Color.white).lerp(Pal.accent, Mathf.absin(5.0F, 1.0F)) : Color.white);
                });
            });
        }

        String versionText = (Version.build == -1 ? "[#fc8140aa]" : "[#ffffffba]") + Version.combined();
        group.fill((x, y, w, h) -> {
            TextureRegion logo = Core.atlas.find("logo");
            float width = (float)Core.graphics.getWidth();
            float height = (float)Core.graphics.getHeight() - Core.scene.marginTop;
            float logoscl = Scl.scl(1.0F);
            float logow = Math.min((float)logo.width * logoscl, (float)Core.graphics.getWidth() - Scl.scl(20.0F));
            float logoh = logow * (float)logo.height / (float)logo.width;
            float fx = (float)((int)(width / 2.0F));
            float fy = (float)((int)(height - 6.0F - logoh)) + logoh / 2.0F - (Core.graphics.isPortrait() ? Scl.scl(30.0F) : 0.0F);
            Draw.color();
            Draw.rect(logo, fx, fy, logow, logoh);
            Fonts.def.setColor(Color.white);
            Fonts.def.draw(versionText, fx, fy - logoh / 2.0F, 1);
        }).touchable = Touchable.disabled;
    }

    private void buildMobile() {
        this.container.clear();
        this.container.name = "buttons";
        this.container.setSize((float)Core.graphics.getWidth(), (float)Core.graphics.getHeight());
        float size = 120.0F;
        this.container.defaults().size(size).pad(5.0F).padTop(4.0F);
        MobileButton play = new MobileButton(Icon.play, "@campaign", () -> {
            PlanetDialog var10001 = Vars.ui.planet;
            Objects.requireNonNull(var10001);
            this.checkPlay(var10001::show);
        });
        MobileButton custom = new MobileButton(Icon.rightOpenOut, "@customgame", () -> {
            CustomGameDialog var10001 = Vars.ui.custom;
            Objects.requireNonNull(var10001);
            this.checkPlay(var10001::show);
        });
        MobileButton maps = new MobileButton(Icon.download, "@loadgame", () -> {
            LoadDialog var10001 = Vars.ui.load;
            Objects.requireNonNull(var10001);
            this.checkPlay(var10001::show);
        });
        MobileButton join = new MobileButton(Icon.add, "@joingame", () -> {
            JoinDialog var10001 = Vars.ui.join;
            Objects.requireNonNull(var10001);
            this.checkPlay(var10001::show);
        });
        MobileButton editor = new MobileButton(Icon.terrain, "@editor", () -> {
            MapsDialog var10001 = Vars.ui.maps;
            Objects.requireNonNull(var10001);
            this.checkPlay(var10001::show);
        });
        TextureRegionDrawable var10002 = Icon.settings;
        SettingsMenuDialog var10004 = Vars.ui.settings;
        Objects.requireNonNull(var10004);
        MobileButton tools = new MobileButton(var10002, "@settings", var10004::show);
        var10002 = Icon.book;
        ModsDialog var10 = Vars.ui.mods;
        Objects.requireNonNull(var10);
        MobileButton mods = new MobileButton(var10002, "@mods", var10::show);
        MobileButton exit = new MobileButton(Icon.exit, "@quit", () -> {
            Core.app.exit();
        });
        if (!Core.graphics.isPortrait()) {
            this.container.marginTop(60.0F);
            this.container.add(play);
            this.container.add(join);
            this.container.add(custom);
            this.container.add(maps);
            this.container.row();
            this.container.table((table) -> {
                table.defaults().set(this.container.defaults());
                table.add(editor);
                table.add(tools);
                table.add(mods);
                if (!Vars.ios) {
                    table.add(exit);
                }

            }).colspan(4);
        } else {
            this.container.marginTop(0.0F);
            this.container.add(play);
            this.container.add(maps);
            this.container.row();
            this.container.add(custom);
            this.container.add(join);
            this.container.row();
            this.container.add(editor);
            this.container.add(tools);
            this.container.row();
            this.container.table((table) -> {
                table.defaults().set(this.container.defaults());
                table.add(mods);
                if (!Vars.ios) {
                    table.add(exit);
                }

            }).colspan(2);
        }

    }

    private void buildDesktop() {
        this.container.clear();
        this.container.setSize((float)Core.graphics.getWidth(), (float)Core.graphics.getHeight());
        float width = 230.0F;
        Drawable background = Styles.black6;
        this.container.left();
        this.container.add().width((float)Core.graphics.getWidth() / 10.0F);
        this.container.table(background, (t) -> {
            t.defaults().width(width).height(70.0F);
            t.name = "buttons";
            Buttoni[] var10002 = new Buttoni[]{new Buttoni("@play", Icon.play, new Buttoni[]{new Buttoni("@campaign", Icon.play, () -> {
                PlanetDialog var10001 = Vars.ui.planet;
                Objects.requireNonNull(var10001);
                this.checkPlay(var10001::show);
            }), new Buttoni("@joingame", Icon.add, () -> {
                JoinDialog var10001 = Vars.ui.join;
                Objects.requireNonNull(var10001);
                this.checkPlay(var10001::show);
            }), new Buttoni("@customgame", Icon.terrain, () -> {
                CustomGameDialog var10001 = Vars.ui.custom;
                Objects.requireNonNull(var10001);
                this.checkPlay(var10001::show);
            }), new Buttoni("@loadgame", Icon.download, () -> {
                LoadDialog var10001 = Vars.ui.load;
                Objects.requireNonNull(var10001);
                this.checkPlay(var10001::show);
            })}), new Buttoni("@editor", Icon.terrain, () -> {
                MapsDialog var10001 = Vars.ui.maps;
                Objects.requireNonNull(var10001);
                this.checkPlay(var10001::show);
            }), null, null, null, null, null};
            Buttoni var10005;
            TextureRegionDrawable var10008;
            if (Vars.steam) {
                var10008 = Icon.book;
                Platform var10009 = Vars.platform;
                Objects.requireNonNull(var10009);
                var10005 = new Buttoni("@workshop", var10008, var10009::openWorkshop);
            } else {
                var10005 = null;
            }

            var10002[2] = var10005;
            var10008 = Icon.book;
            ModsDialog var3 = Vars.ui.mods;
            Objects.requireNonNull(var3);
            var10002[3] = new Buttoni("@mods", var10008, var3::show);
            var10008 = Icon.settings;
            SettingsMenuDialog var4 = Vars.ui.settings;
            Objects.requireNonNull(var4);
            var10002[4] = new Buttoni("@settings", var10008, var4::show);
            var10008 = Icon.info;
            AboutDialog var5 = Vars.ui.about;
            Objects.requireNonNull(var5);
            var10002[5] = new Buttoni("@about.button", var10008, var5::show);
            var10008 = Icon.exit;
            Application var6 = Core.app;
            Objects.requireNonNull(var6);
            var10002[6] = new Buttoni("@quit", var10008, var6::exit);
            this.buttons(t, var10002);
        }).width(width).growY();
        this.container.table(background, (t) -> {
            this.submenu = t;
            t.name = "submenu";
            t.color.a = 0.0F;
            t.top();
            t.defaults().width(width).height(70.0F);
            t.visible(() -> {
                return !t.getChildren().isEmpty();
            });
        }).width(width).growY();
    }

    private void checkPlay(Runnable run) {
        if (!Vars.mods.hasContentErrors()) {
            run.run();
        } else {
            Vars.ui.showInfo("@mod.noerrorplay");
        }

    }

    private void fadeInMenu() {
        this.submenu.clearActions();
        this.submenu.actions(new Action[]{Actions.alpha(1.0F, 0.15F, Interp.fade)});
    }

    private void fadeOutMenu() {
        if (!this.submenu.getChildren().isEmpty()) {
            this.submenu.clearActions();
            this.submenu.actions(new Action[]{Actions.alpha(1.0F), Actions.alpha(0.0F, 0.2F, Interp.fade), Actions.run(() -> {
                this.submenu.clearChildren();
            })});
        }
    }

    private void buttons(Table t, Buttoni... buttons) {
        Buttoni[] var3 = buttons;
        int var4 = buttons.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Buttoni b = var3[var5];
            if (b != null) {
                Button[] out = new Button[]{null};
                out[0] = (Button)t.button(b.text, b.icon, Styles.clearToggleMenut, () -> {
                    if (this.currentMenu == out[0]) {
                        this.currentMenu = null;
                        this.fadeOutMenu();
                    } else if (b.submenu != null) {
                        this.currentMenu = out[0];
                        this.submenu.clearChildren();
                        this.fadeInMenu();
                        this.submenu.add().height(((float)Core.graphics.getHeight() - Core.scene.marginTop - Core.scene.marginBottom - out[0].getY(10)) / Scl.scl(1.0F));
                        this.submenu.row();
                        this.buttons(this.submenu, b.submenu);
                    } else {
                        this.currentMenu = null;
                        this.fadeOutMenu();
                        b.runnable.run();
                    }

                }).marginLeft(11.0F).get();
                out[0].update(() -> {
                    out[0].setChecked(this.currentMenu == out[0]);
                });
                t.row();
            }
        }

    }

    private static class Buttoni {
        final Drawable icon;
        final String text;
        final Runnable runnable;
        final Buttoni[] submenu;

        public Buttoni(String text, Drawable icon, Runnable runnable) {
            this.icon = icon;
            this.text = text;
            this.runnable = runnable;
            this.submenu = null;
        }

        public Buttoni(String text, Drawable icon, Buttoni... buttons) {
            this.icon = icon;
            this.text = text;
            this.runnable = () -> {
            };
            this.submenu = buttons;
        }
    }
}
