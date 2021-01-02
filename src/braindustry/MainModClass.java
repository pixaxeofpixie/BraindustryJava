package braindustry;

import braindustry.content.*;
import braindustry.modVars.Classes.ModAtlas;
import braindustry.modVars.Classes.ModEventType;
import braindustry.modVars.Classes.UI.ModCheatMenu;
import braindustry.modVars.modVars;
import arc.*;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.*;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
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
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.world.Tile;
import mindustry.world.meta.BuildVisibility;

import static braindustry.modVars.modFunc.*;
import static braindustry.modVars.modVars.*;

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
        BaseDialog dialog = new BaseDialog("Choose unit:");
        Table table=new Table();
        final int buttonSize=100;
        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(true, false);
        for (UnitType unitType : Vars.content.units()) {
            if (unitType == UnitTypes.block)continue;
            if (Vars.content.units().indexOf(unitType)%5==0)table.row();
            Button button = new Button();
            button.clearChildren();
            Image image = new Image(unitType.region);
            Cell<Image> imageCell=button.add(image);
            float imageSize=buttonSize / 100 * 70;
            if (image.getWidth()==image.getHeight()) {
                imageCell.size(imageSize);
            } else{
                float width=image.getWidth(),height=image.getHeight();

                imageCell.size(imageSize*(width/height),imageSize);
            }

            if (unitType != UnitTypes.block) {
                button.clicked(() -> {
                    Tile tile=Vars.player.tileOn();
                    if (unitType.constructor.get() instanceof UnitWaterMove && !unitType.flying && EntityCollisions.waterSolid(Vars.player.tileX(), Vars.player.tileY())){
                        Color color=Color.valueOf(Strings.format("#@",Color.scarlet.toString()));
                        getInfoDialog("","Can't spawn water unit!!!","You not on the water",color.lerp(Color.white,0.2f)).show();
                        return;
                    }
                    Unit newUnit = unitType.spawn(Vars.player.x, Vars.player.y);
                    newUnit.spawnedByCore = true;
                    Vars.player.unit().spawnedByCore = true;
                    Vars.player.unit(newUnit);
                    dialog.hide();
                });
            } else {
                button.clicked(() -> {
                    getInfoDialog("","Don't use Block unit","",Color.scarlet).show();
                });
            }
            table.add(button).width(buttonSize).height(buttonSize).pad(6);
        }
        dialog.cont.add(pane).growY().growX().bottom().center();

        dialog.addCloseButton();
        dialog.show();
    }
    private void showChooseTeamDialog(){
        BaseDialog dialog = new BaseDialog("Choose team:");
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
        new ModCheatMenu((t)->{
            t.button("Change team", () -> {
                showChooseTeamDialog();
            });
            t.button("Change unit", () -> {
                showUnitChangeDialog();
            });
            t.visibility=()-> settings.cheating();
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
        modVars.load();
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
