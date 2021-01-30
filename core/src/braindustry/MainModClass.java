package braindustry;

import Gas.GasInit;
import ModVars.Classes.UI.settings.AdvancedSettingsMenuDialog;
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
import braindustry.gen.ModCall;
import braindustry.gen.ModPlayer;
import braindustry.graphics.ModShaders;
import braindustry.input.ModBinding;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.EntityCollisions;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.input.DesktopInput;
import mindustry.input.MobileInput;
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
import static braindustry.input.ModBinding.*;
import static mindustry.Vars.*;
import static mindustry.Vars.player;

public class MainModClass extends Mod {
    public static ModListener modListener;


    @Override
    public void registerServerCommands(CommandHandler handler) {
        modVars.netServer.registerCommands(handler);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        modVars.netClient.registerCommands(handler);
    }
    void createPlayer(){
        player = ModPlayer.create();
        player.name = Core.settings.getString("name");
        player.color.set(Core.settings.getInt("color-0"));
        if(state.isGame()){
            player.add();
        }
    }
    public void init() {
        if (!loaded)return;
        createPlayer();
        modVars.init();
        ModListener.load();
        EntityMapping.idMap[12] = ModPlayer::new;
        EntityMapping.nameMap.put("Player", ModPlayer::new);
        EntityMapping.nameMap.put("player", ModPlayer::new);
        Events.fire(new ModEventType.ModInit());
    }
    public static TextureRegion getIcon() {

//        if (modInfo==null)modInfo = Vars.mods.getMod(mod.getClass());
//        print("modInfo: @",modInfo);
        if (modInfo==null||modInfo.iconTexture == null) return Core.atlas.find("nomap");
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
        if (!loaded)return;
        modInfo = Vars.mods.getMod(this.getClass());
        Blocks.interplanetaryAccelerator.buildVisibility = BuildVisibility.shown;
        Blocks.blockForge.buildVisibility = BuildVisibility.shown;
        Blocks.blockLoader.buildVisibility = BuildVisibility.shown;
        Blocks.blockUnloader.buildVisibility = BuildVisibility.shown;

        Time.runTask(10f, () -> {
            /*BaseDialog dialog = new BaseDialog("Welcome");
            dialog.cont.add("Hello, its Braindustry Mod.").row();
            dialog.cont.image(Core.atlas.find("braindustry-java-screen2")).pad(20f).row();
            dialog.cont.button("Ok", dialog::hide).size(100f, 50f);
            dialog.show();*/
        });
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
    }

    public static  void openUnlockContentDialog() {
        new UnlockContentDialog().show();
    }

    public static  void openModCheatItemsMenu() {
        new ModCheatItemsMenu().show(()->{},()->{});
    }
//    private static boolean openDialog=false;
    public static void openUnitChooseDialog() {
        new UnitChooseDialog((unitType)->{
            Tile tile=Vars.player.tileOn();
            if (unitType.constructor.get() instanceof UnitWaterMove && !unitType.flying && EntityCollisions.waterSolid(Vars.player.tileX(), Vars.player.tileY())){
                Color color=Color.valueOf(Strings.format("#@",Color.scarlet.toString()));
                getInfoDialog("","Can't spawn water unit!!!","You not on the water",color.lerp(Color.white,0.2f)).show();
                return false;
            }
            ModCall.spawnUnits(unitType,Vars.player.x,Vars.player.y,1,true,Vars.player.team(), Vars.player);
            return true;
        }).show();
    }

    public static  void openTeamChooseDialog() {
        new TeamChooseDialog((team)->{
            try {
                Vars.player.team(team);
            } catch (Exception exception) {
                showException(exception);
            }
        }).show();
    }

    public MainModClass() {
        EventOn(DisposeEvent.class,(d)->{
            if (Vars.ui.menufrag instanceof ModMenuFragment)((ModMenuFragment)Vars.ui.menufrag).dispose();
            Vars.ui.dispose();
        });
        modInfo = Vars.mods.getMod(this.getClass());
        modVars.load();
        EventOn(ClientLoadEvent.class, (e) -> {
            constructor();
        });
    }

    public void loadContent() {
        modInfo = Vars.mods.getMod(this.getClass());
        print("java loadContent");
        modAtlas=new ModAtlas();
        inTry(()->{
            if (!headless) ModShaders.init();
        });
        Events.fire(ModEventType.ModContentLoad.class);
//        loadMaps();
        Seq<ContentList> loads=Seq.with(
                new ModSounds(),new ModStatusEffects(),new ModItems(),new ModLiquids(),new ModUnitTypes(),new ModBlocks(),new ModTechTree()
        );
        loads.each((load)->{
            try {
                load.load();
            } catch (NullPointerException e){
                if (!headless)showException(e);
            }
        });
        GasInit.init(true);
        Vars.content.each((c)->{
            if (c instanceof UnlockableContent) checkTranslate((UnlockableContent)c);
        });
        loaded=true;
    }

}
