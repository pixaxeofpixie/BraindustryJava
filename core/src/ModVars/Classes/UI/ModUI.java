package ModVars.Classes.UI;

import ModVars.Classes.UI.Cheat.ModCheatMenu;
import ModVars.Classes.UI.settings.AdvancedSettingsMenuDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import ModVars.modVars;
import arc.Core;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import braindustry.MainModClass;
import braindustry.ModListener;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.g3d.ModPlanetRenderer;
import braindustry.input.ModBinding;
import braindustry.input.ModKeyBinds;
import braindustry.ui.dialogs.ModPlanetDialog;
import braindustry.ui.fragments.ModHudFragment;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.graphics.g3d.PlanetRenderer;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.PlanetDialog;
import mindustryAddition.iu.AdvancedContentInfoDialog;

import static ModVars.Classes.UI.CheatUI.*;
import static ModVars.modVars.*;
import static braindustry.MainModClass.*;
import static braindustry.input.ModBinding.*;

public class ModUI {

    public void init() {
        
        if (Vars.headless) return;
        AdvancedSettingsMenuDialog.init();
        ModHudFragment.init();
        Core.settings.put("uiscalechanged", false);
        ModListener.updaters.add(() -> {
            boolean noDialog = !Core.scene.hasDialog();
            boolean inGame = Vars.state.isGame();

            boolean inMenu = Vars.state.isMenu() || !Vars.ui.planet.isShown();
            if (!controls.isShown()){
                if (keyBinds.keyTap(unitDialogBing) && noDialog && inGame) {
                    openUnitChooseDialog();
                } else if (keyBinds.keyTap(teamDialogBing) && noDialog && inGame) {
                    openTeamChooseDialog();
                } else if (keyBinds.keyTap(unlockDialogBing) && !inMenu) {
                    openUnlockContentDialog();
                } else if (keyBinds.keyTap(itemManagerDialogBing) && noDialog) {
                    openModCheatItemsMenu();
                } else if (keyBinds.keyTap(rulesEditDialogBing) && inGame && noDialog){
                    openRulesEditDialog();
                }
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
        Vars.ui.planet.remove();
        Vars.renderer.planets.dispose();
        Vars.renderer.planets=new ModPlanetRenderer();
        Vars.ui.planet=new ModPlanetDialog();
        new ModCheatMenu((table) -> {
            table.button("@cheat-menu.title", () -> {
                BaseDialog dialog = new BaseDialog("@cheat-menu.title");
                dialog.cont.table((t) -> {
                    t.defaults().size(280.0F, 60.0F);
                    t.button("@cheat-menu.change-team", CheatUI::openTeamChooseDialog).growX().row();
                    t.button("@cheat-menu.change-unit", CheatUI::openUnitChooseDialog).growX().row();
                   if (!Vars.net.client()) t.button("@cheat-menu.edit-rules", CheatUI::openRulesEditDialog).growX().row();
                    t.button("@cheat-menu.items-manager", CheatUI::openModCheatItemsMenu).growX().row();
                    t.button("@cheat-menu.unlock-content", CheatUI::openUnlockContentDialog).growX().row();
                });
                dialog.addCloseListener();
                dialog.addCloseButton();
                dialog.show();

            }).size(280.0f / 2f, 60.0F);
            table.visibility = () -> CheatUI.visibility.get();
        });
        keyBinds = new ModKeyBinds();
        keyBinds.setDefaults(ModBinding.values());
        keyBinds.load();
        controls = new ModControlsDialog();
        otherSettingsDialog = new ModOtherSettingsDialog();
        settingsDialog = new ModSettingsDialog();
    }
}
