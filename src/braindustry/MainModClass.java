package braindustry;

import Gas.GasInit;
import arc.func.Boolf;
import arc.func.Func;
import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.ObjectMap;
import braindustry.content.*;
import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModEventType;
import ModVars.Classes.UI.Cheat.*;
import ModVars.modVars;
import arc.*;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.*;
import arc.scene.ui.layout.Cell;
import arc.struct.Seq;
import arc.util.*;
import braindustry.entities.bullets.ModLightningBulletType;
import braindustry.graphics.ModShaders;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.EntityCollisions;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.io.JsonIO;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.BuildVisibility;
import mindustryAddition.iu.AdvancedContentInfoDialog;

import static ModVars.modFunc.*;
import static ModVars.modVars.*;
import static arc.Core.assets;
import static mindustry.Vars.schematics;

public class MainModClass extends Mod {
    public void init() {
        Events.fire(new ModEventType.ModInit());
        EventOn(EventType.ClientLoadEvent.class,(e)->{
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
        });
        AdvancedContentInfoDialog.init();
    }
    public static TextureRegion getIcon() {

//        if (modInfo==null)modInfo = Vars.mods.getMod(mod.getClass());
//        print("modInfo: @",modInfo);
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
            dialog.cont.image(Core.atlas.find(fullName("welcome"))).pad(20f).row();
            dialog.cont.button("Ok", dialog::hide).size(100f, 50f);
            dialog.show();
        });
    }
    public MainModClass() {
        EventOn(DisposeEvent.class,(d)->{
            if (Vars.ui.menufrag instanceof ModMenuFragment)((ModMenuFragment)Vars.ui.menufrag).dispose();
            Vars.ui.dispose();
        });
        modInfo = Vars.mods.getMod(this.getClass());
        modVars.load();
        EventOn(ClientLoadEvent.class, (e) -> {
            Blocks.interplanetaryAccelerator.buildVisibility = BuildVisibility.shown;
            Blocks.blockForge.buildVisibility = BuildVisibility.shown;
            Blocks.blockLoader.buildVisibility = BuildVisibility.shown;
            Blocks.blockUnloader.buildVisibility = BuildVisibility.shown;
            modInfo = Vars.mods.getMod(this.getClass());
            constructor();
            Boolf<BulletType> replace=(b)->(b instanceof LightningBulletType && !(b instanceof ModLightningBulletType));
            Func<BulletType,ModLightningBulletType> newBullet=(old)->{

                ModLightningBulletType newType=new ModLightningBulletType();
                JsonIO.copy(old,newType);
                return newType;
            };
            Vars.content.each((c)->{
                if (c instanceof UnlockableContent){
                    UnlockableContent content=(UnlockableContent)c;
                    if (content instanceof Block){
                        if (content instanceof Turret){
                            if (content instanceof PowerTurret){
                                PowerTurret powerTurret=(PowerTurret)content;
                                if (replace.get(powerTurret.shootType)){
                                    powerTurret.shootType=newBullet.get(powerTurret.shootType);
                                }
                            } else if (content instanceof ItemTurret){
                                ItemTurret itemTurret=(ItemTurret)content;
                                ObjectMap<Item,BulletType> toReplace=new ObjectMap<>();
                                for (ObjectMap.Entry<Item, BulletType> entry: itemTurret.ammoTypes.entries()) {
                                    if (replace.get(entry.value)){
                                        toReplace.put(entry.key,entry.value);
                                    }
                                }
                                for (ObjectMap.Entry<Item, BulletType> entry: toReplace.entries()) {
                                        itemTurret.ammoTypes.put(entry.key, newBullet.get(entry.value));
                                }
                            }
                        }
                    }
                }
            });
        });
    }

    public void loadContent() {
        modInfo = Vars.mods.getMod(this.getClass());
        modAtlas=new ModAtlas();
        inTry(()->{
            ModShaders.init();
        });
        Events.fire(ModEventType.ModContentLoad.class);
//        loadMaps();
        GasInit.init(true);
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
