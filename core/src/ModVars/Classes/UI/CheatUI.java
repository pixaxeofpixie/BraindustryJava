package ModVars.Classes.UI;

import ModVars.Classes.UI.Cheat.ModCheatItemsMenu;
import ModVars.Classes.UI.Cheat.TeamChooseDialog;
import ModVars.Classes.UI.Cheat.UnitChooseDialog;
import ModVars.Classes.UI.Cheat.UnlockContentDialog;
import ModVars.modVars;
import arc.Core;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Strings;
import braindustry.gen.CheatModRulesTable;
import braindustry.gen.ModCall;
import mindustry.Vars;
import mindustry.entities.EntityCollisions;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.gen.UnitWaterMove;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Tile;

import static ModVars.modFunc.getInfoDialog;
import static ModVars.modFunc.showException;
import static ModVars.modVars.settings;
import static mindustry.Vars.*;

public class CheatUI {
    public static Prov<Boolean> visibility= modVars::showCheatMenu;
    static Runnable rebuildTeamValue = () -> {
    };

    public static void openUnlockContentDialog() {
        new UnlockContentDialog().show();
    }

    public static void openModCheatItemsMenu() {
        if (!visibility.get() || net.client())return;
        new ModCheatItemsMenu().show(() -> {
        }, () -> {
        });
    }

    //    private static boolean openDialog=false;
    public static void openUnitChooseDialog() {
        if (!visibility.get())return;
        new UnitChooseDialog((unitType) -> {
            Tile tile = Vars.player.tileOn();
            Unit unit = unitType.constructor.get();
            unit.type=unitType;
            EntityCollisions.SolidPred solidity = unit.solidity();
            if ((tile==null)|| solidity!=null && solidity.solid(tile.x,tile.y)) {
                Color color = Color.valueOf(Strings.format("#@", Color.scarlet));
                getInfoDialog("", "Can't spawn this unit!!!", "The selected unit type cannot be created on this block", color.lerp(Color.white, 0.2f)).show();
                return false;
            }
            ModCall.setNewUnit(unitType);
            return true;
        }).show();
    }

    protected static void addButton(Table cont, Team team) {
        TextureRegion white = new TextureRegion(Core.atlas.white());
        white.width = white.height = 38;
        cont.button(new TextureRegionDrawable(white, 1f).tint(team.color), Styles.clearTransi, () -> {
            modVars.settings.setInt("cheat-team", team.id);
            rebuildTeamValue.run();
        });
    }

    public static void openRulesEditDialog() {
        if (net.client() ||!visibility.get()) {
//            modFunc.getInfoDialog("@cant-open-rules-dialog","","@you-on-server",Color.scarlet);
            return;
        }
        BaseDialog dialog = new BaseDialog("@cheat-menu.rules-edit.title");
        ScrollPane mainPain = dialog.cont.pane(pane -> {
            Log.info("pane size(@,@)", pane.getWidth(), pane.getHeight());
            ScrollPane scrollPane = pane.pane(p -> {
                p.defaults().size(40).left();
                for (Team team : Team.all) {
                    addButton(p, team);
                    if ((team.id + 1) % (mobile ? 6 : 12) == 0) p.row();
                }
            }).maxHeight(Scl.scl(40 * 2)).get();
            pane.row();
            pane.table((teamValue) -> {
                rebuildTeamValue = () -> {
                    teamValue.clear();
                    teamValue.clearChildren();
                    Rules.TeamRule teamRule = state.rules.teams.get(Team.get(modVars.settings.getInt("cheat-team")));
                    teamValue.check("ai", teamRule.ai, (b) -> teamRule.ai = b).row();
                    teamValue.add("aiTier: " + teamRule.aiTier).update(label -> {
                        label.setText("aiTier: " + teamRule.aiTier);
                    });
                    teamValue.slider(0, Float.MAX_VALUE, 1f, teamRule.aiTier, (f) -> {
                        teamRule.aiTier = f;
                    }).row();

                    teamValue.check("aiCoreSpawn", teamRule.aiCoreSpawn, (b) -> teamRule.aiCoreSpawn = b).row();
                    teamValue.check("infiniteAmmo", teamRule.infiniteAmmo, (b) -> teamRule.infiniteAmmo = b).row();
                    teamValue.check("infiniteResources", teamRule.infiniteResources, (b) -> teamRule.infiniteResources = b).row();
                };
                rebuildTeamValue.run();
            }).row();
            pane.image(new TextureRegionDrawable(Core.atlas.white()).tint(Pal.accent)).growX().row();
//            Log.info("pane size(@,@)",pane.,pane.getHeight());
            pane.table((t) -> {
                t.defaults().left();
                CheatModRulesTable.build(t, state.rules);

            });
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setOverscroll(false, false);
        }).minWidth(520f).get();

        mainPain.setScrollingDisabled(true, false);
        mainPain.setOverscroll(false, false);

        dialog.addCloseListener();
        dialog.addCloseButton();
        dialog.show();
    }

    public static void openTeamChooseDialog() {
        if (!visibility.get())return;
        new TeamChooseDialog((team) -> {
            try {
                ModCall.setTeam(team);
            } catch (Exception exception) {
                showException(exception);
            }
        }).show();
    }

}
