package ModVars.Classes.UI;

import ModVars.Classes.ModEventType;
import ModVars.Classes.UI.settings.AdvancedSettingsMenuDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import ModVars.modVars;
import arc.Core;
import arc.Events;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import braindustry.ModListener;
import braindustry.gen.ModPlayer;
import braindustry.input.ModBinding;
import braindustry.input.ModKeyBinds;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.EntityMapping;
import mindustryAddition.iu.AdvancedContentInfoDialog;

import static ModVars.modFunc.EventOn;
import static ModVars.modVars.keyBinds;
import static braindustry.MainModClass.*;
import static braindustry.input.ModBinding.*;
import static braindustry.input.ModBinding.itemManagerDialogBing;
import static ModVars.modVars.*;
import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class ModUI {

    public void init(){
        if (Vars.headless)return;
        AdvancedSettingsMenuDialog.init();
        Core.settings.put("uiscalechanged", false);
        ModListener.updaters.add(()->{
            if ((Vars.state.isPlaying() || Vars.state.isPaused()) && !Core.scene.hasDialog()){
                if (keyBinds.keyTap(unitDialogBing)){
                    openUnitChooseDialog();
                }
                if (keyBinds.keyTap(teamDialogBing)){
                    openTeamChooseDialog();
                }
                if (keyBinds.keyTap(unlockDialogBing)){
                    openUnlockContentDialog();
                }
                if (keyBinds.keyTap(itemManagerDialogBing)){
                    openModCheatItemsMenu();
                }
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
        Vars.ui.menufrag=new ModMenuFragment();
//        Vars.ui.menufrag.
        Vars.ui.menufrag.build(Vars.ui.menuGroup);
        AdvancedContentInfoDialog.init();


        keyBinds = new ModKeyBinds();
        keyBinds.setDefaults(ModBinding.values());
        keyBinds.load();
        controls = new ModControlsDialog();
        otherSettingsDialog = new ModOtherSettingsDialog();
        settingsDialog = new ModSettingsDialog();
    }
}
