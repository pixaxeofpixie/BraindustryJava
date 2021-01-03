package braindustry;

import arc.graphics.Color;
import braindustry.content.*;
import braindustry.modVars.Classes.ModAtlas;
import braindustry.modVars.Classes.ModEventType;
import braindustry.modVars.Classes.UI.Cheat.*;
import braindustry.modVars.ModVars;
import arc.*;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.*;
import arc.scene.ui.layout.Cell;
import arc.struct.Seq;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.EntityCollisions;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.mod.Mod;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.world.Tile;
import mindustry.world.meta.BuildVisibility;

import static braindustry.modVars.ModFunc.*;
import static braindustry.modVars.ModVars.*;

public class MainModClass extends Mod {
    public void init() {
        Events.fire(new ModEventType.ModInit());
        modInfo = Vars.mods.getMod(this.getClass());
    }
    public static TextureRegion getIcon() {

//        if (modInfo==null)modInfo = Vars.mods.getMod(mod.getClass());
        print("modInfo: @",modInfo);
        if (modInfo.iconTexture == null) return Core.atlas.find("nomap");
        return new TextureRegion(modInfo.iconTexture);
    }
    private void showUnitChangeDialog(){
    }
    private void showUnlockDialog(){
        BaseDialog dialog = new BaseDialog("Unlock content dialog");
        dialog.cont.table(i -> {
            i.table(t -> {
                final int buttonSize = 20;
                for (Team team : Team.all) {
                    if (Seq.with(Team.all).indexOf(team) % 20 == 0) t.row();
                    ImageButton button = new ImageButton(Tex.whitePane, Styles.clearToggleTransi);
                    button.clearChildren();
                    Image image = new Image();
                    button.background(image.getDrawable()).setColor(team.color);
                    Cell<Image> imageCell = button.add(image).color(team.color).size(buttonSize);
                    button.clicked(() -> {
                        try {
                            Vars.player.team(team);
                        } catch (Exception exception) {
                            showException(exception);
                        }
                        dialog.hide();
                    });
                    t.add(button).color(team.color).width(buttonSize).height(buttonSize).pad(6);
                }
            });
        }).width(360).bottom().center();
        dialog.addCloseButton();
        dialog.show();
    }
    private void constructor() {
        modInfo = Vars.mods.getMod(this.getClass());
        new ModCheatMenu((table)->{
            table.button("Cheat menu",()->{
                BaseDialog dialog=new BaseDialog("Cheat-menu");
                dialog.cont.table((t)->{
                    t.defaults().size(280.0F, 60.0F);
                    t.button("Change team", () -> {
                        new TeamChooseDialog((team)->{
                            try {
                                Vars.player.team(team);
                            } catch (Exception exception) {
                                showException(exception);
                            }
                        }).show();
                    }).growX().row();
                    t.button("Change unit", () -> {
                        new UnitChooseDialog((unitType)->{
                            Tile tile=Vars.player.tileOn();
                            if (unitType.constructor.get() instanceof UnitWaterMove && !unitType.flying && EntityCollisions.waterSolid(Vars.player.tileX(), Vars.player.tileY())){
                                Color color=Color.valueOf(Strings.format("#@",Color.scarlet.toString()));
                                getInfoDialog("","Can't spawn water unit!!!","You not on the water",color.lerp(Color.white,0.2f)).show();
                                return false;
                            }
                            Unit newUnit = unitType.spawn(Vars.player.x, Vars.player.y);
                            newUnit.spawnedByCore = true;
                            Vars.player.unit().spawnedByCore = true;
                            Vars.player.unit(newUnit);
                            return true;
                        }).show();
                    }).growX().row();
                    t.button("Change sandbox", () -> {
                        Vars.state.rules.infiniteResources=!Vars.state.rules.infiniteResources;
                    }).growX().row();
                    t.button("Items manager", () -> {
                        new ModCheatItemsMenu().show(()->{},()->{});
                    }).growX().row();
                    t.button("Unlock content", () -> {
                        new UnlockContentDialog().show();
                    }).growX().row();
                });
                dialog.addCloseListener();
                dialog.addCloseButton();
                dialog.show();

            }).size(280.0f/2f, 60.0F);
            table.visibility=()-> settings.cheating();
        });
        Time.runTask(10f, () -> {
            BaseDialog dialog = new BaseDialog("Welcome");
            dialog.cont.add("Hello it's braindustry java mod").row();
            dialog.cont.image(Core.atlas.find(getFullName("welcome"))).pad(20f).row();
            dialog.cont.button("Ok", dialog::hide).size(100f, 50f);
            dialog.show();
        });
    }
    public MainModClass() {
        modInfo = Vars.mods.getMod(this.getClass());
        ModVars.load();
        EventOn(ClientLoadEvent.class, (e) -> {
            Blocks.interplanetaryAccelerator.buildVisibility = BuildVisibility.shown;
            Blocks.blockForge.buildVisibility = BuildVisibility.shown;
            Blocks.blockLoader.buildVisibility = BuildVisibility.shown;
            Blocks.blockUnloader.buildVisibility = BuildVisibility.shown;
            modInfo = Vars.mods.getMod(this.getClass());
            constructor();
        });
    }

    public void loadContent() {
        modInfo = Vars.mods.getMod(this.getClass());
        modAtlas=new ModAtlas();
        Events.fire(ModEventType.ModContentLoad.class);
//        loadMaps();
        new ModSounds().load();
        new ModStatusEffects().load();
        new ModItems().load();
        new ModLiquids().load();
        new ModUnitTypes().load();
        new ModBlocks().load();
        new ModTechTree().load();
        Vars.content.each((c)->{
            if (c instanceof UnlockableContent) checkTranslate((UnlockableContent)c);
        });
    }

}
