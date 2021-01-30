package ModVars.Classes.UI;

import ModVars.Classes.UI.Cheat.ModCheatMenu;
import ModVars.Classes.UI.settings.AdvancedSettingsMenuDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import ModVars.modVars;
import arc.Core;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import braindustry.ModListener;
import braindustry.gen.StealthUnitc;
import braindustry.input.ModBinding;
import braindustry.input.ModKeyBinds;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;
import mindustryAddition.iu.AdvancedContentInfoDialog;

import static ModVars.modVars.*;
import static braindustry.MainModClass.*;
import static braindustry.input.ModBinding.*;

public class ModUI {

    public void init() {
        if (Vars.headless) return;
        AdvancedSettingsMenuDialog.init();
        Core.settings.put("uiscalechanged", false);
        ModListener.updaters.add(() -> {
            boolean noDialog = !Core.scene.hasDialog();
            boolean inGame = Vars.state.isGame();
            boolean inMenu = Vars.state.isMenu();
            if (keyBinds.keyTap(unitDialogBing) && noDialog && inGame) {
                openUnitChooseDialog();
            } else if (keyBinds.keyTap(teamDialogBing) && noDialog && inGame) {
                openTeamChooseDialog();
            } else if (keyBinds.keyTap(unlockDialogBing) && !inMenu) {
                openUnlockContentDialog();
            } else if (keyBinds.keyTap(itemManagerDialogBing) && noDialog) {
                openModCheatItemsMenu();
            }
            if (inGame && Vars.state.isPaused() && Vars.player.unit() instanceof StealthUnitc){
                StealthUnitc unit = (StealthUnitc) Vars.player.unit();
                unit.updateStealthStatus();
            }

        });

        Vars.ui.menuGroup.remove();
        Vars.ui.menuGroup = new WidgetGroup();
        Vars.ui.menuGroup.setFillParent(true);
        Vars.ui.menuGroup.touchable = Touchable.childrenOnly;
        Vars.ui.menuGroup.visible(() -> {
            return Vars.state.isMenu();
        });
        Core.scene.add(Vars.ui.menuGroup);
        Vars.ui.menufrag = new ModMenuFragment();
//        Vars.ui.menufrag.
        Vars.ui.menufrag.build(Vars.ui.menuGroup);
        AdvancedContentInfoDialog.init();

        new ModCheatMenu((table) -> {
            table.button("@cheat-menu.title", () -> {
                BaseDialog dialog = new BaseDialog("@cheat-menu.title");
                dialog.cont.table((t) -> {
                    t.defaults().size(280.0F, 60.0F);
                    t.button("@cheat-menu.change-team", () -> {
                        openTeamChooseDialog();
                    }).growX().row();
                    t.button("@cheat-menu.change-unit", () -> {
                        openUnitChooseDialog();
                    }).growX().row();
                    t.button("@cheat-menu.change-sandbox", () -> {
                        Vars.state.rules.infiniteResources = !Vars.state.rules.infiniteResources;
                    }).growX().row();
                    t.button("@cheat-menu.items-manager", () -> {
                        openModCheatItemsMenu();
                    }).growX().row();
                    t.button("@cheat-menu.unlock-content", () -> {
                        openUnlockContentDialog();
                    }).growX().row();
                });
                dialog.addCloseListener();
                dialog.addCloseButton();
                dialog.show();

            }).size(280.0f / 2f, 60.0F);
            table.visibility = () -> settings.cheating() && (!Vars.net.active() || Vars.net.server()) && modVars.showCheatMenu();
        });
        keyBinds = new ModKeyBinds();
        keyBinds.setDefaults(ModBinding.values());
        keyBinds.load();
        controls = new ModControlsDialog();
        otherSettingsDialog = new ModOtherSettingsDialog();
        settingsDialog = new ModSettingsDialog();
    }
}
