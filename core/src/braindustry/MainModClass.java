package braindustry;

import Gas.GasInit;
import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModEventType;
import ModVars.modVars;
import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Func;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.CommandHandler;
import braindustry.content.*;
import braindustry.entities.bullets.AngelContinuousBulletType;
import braindustry.entities.bullets.ModLightningBulletType;
import braindustry.gen.ModPlayer;
import braindustry.graphics.ModShaders;
import braindustry.type.StealthUnitType;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.ctype.ContentList;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.game.EventType;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.game.EventType.DisposeEvent;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.EntityMapping;
import mindustry.gen.Tex;
import mindustry.io.JsonIO;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.BuildVisibility;

import static ModVars.modFunc.*;
import static ModVars.modVars.*;
import static mindustry.Vars.*;

public class MainModClass extends Mod {
    public static ModListener modListener;


    public MainModClass() {
        EventOn(DisposeEvent.class, (d) -> {
            if (Vars.ui.menufrag instanceof ModMenuFragment) ((ModMenuFragment) Vars.ui.menufrag).dispose();
            Vars.ui.dispose();
        });
        modInfo = Vars.mods.getMod(this.getClass());
        modVars.load();
        EventOn(ClientLoadEvent.class, (e) -> {
            constructor();
        });
    }

    public static TextureRegion getIcon() {

//        if (modInfo==null)modInfo = Vars.mods.getMod(mod.getClass());
//        print("modInfo: @",modInfo);
        if (modInfo == null || modInfo.iconTexture == null) return Core.atlas.find("nomap");
        return new TextureRegion(modInfo.iconTexture);
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        modVars.netServer.registerCommands(handler);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        modVars.netClient.registerCommands(handler);
    }

    void createPlayer() {
        player = ModPlayer.create();
        player.name = Core.settings.getString("name");
        player.color.set(Core.settings.getInt("color-0"));
        if (state.isGame()) {
            player.add();
        }
    }

    public void init() {
        if (!loaded) return;
        Events.on(EventType.UnitDestroyEvent.class, (e) -> {
            Seq<UnitType> types = Seq.with(ModUnitTypes.lyra,ModUnitTypes.lyra);
            if (types.contains(e.unit.type))
            Call.createBullet(new AngelContinuousBulletType(), Team.derelict, 0f, 0f, 90f, 1200f, 0f, 230f);
        });
        createPlayer();
        modVars.init();
        EntityMapping.idMap[12] = ModPlayer::new;
        EntityMapping.nameMap.put("Player", ModPlayer::new);
        EntityMapping.nameMap.put("player", ModPlayer::new);
        Events.fire(new ModEventType.ModInit());
    }

    private void showUnitChangeDialog() {
    }

    private void showUnlockDialog() {
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
        if (!loaded) return;
        modInfo = Vars.mods.getMod(this.getClass());
        Seq.with(Blocks.blockForge, Blocks.blockLoader, Blocks.blockUnloader).each(b -> b.buildVisibility = BuildVisibility.shown);
        Blocks.interplanetaryAccelerator.buildVisibility = BuildVisibility.shown;
        Boolf<BulletType> replace = (b) -> (b instanceof LightningBulletType && !(b instanceof ModLightningBulletType));
        Func<BulletType, ModLightningBulletType> newBullet = (old) -> {

            ModLightningBulletType newType = new ModLightningBulletType();
            JsonIO.copy(old, newType);
            return newType;
        };
        Vars.content.each((c) -> {
            if (c instanceof UnlockableContent) {
                UnlockableContent content = (UnlockableContent) c;
                if (content instanceof Block) {
                    if (content instanceof Turret) {
                        if (content instanceof PowerTurret) {
                            PowerTurret powerTurret = (PowerTurret) content;
                            if (replace.get(powerTurret.shootType)) {
                                powerTurret.shootType = newBullet.get(powerTurret.shootType);
                            }
                        } else if (content instanceof ItemTurret) {
                            ItemTurret itemTurret = (ItemTurret) content;
                            ObjectMap<Item, BulletType> toReplace = new ObjectMap<>();
                            for (ObjectMap.Entry<Item, BulletType> entry : itemTurret.ammoTypes.entries()) {
                                if (replace.get(entry.value)) {
                                    toReplace.put(entry.key, entry.value);
                                }
                            }
                            for (ObjectMap.Entry<Item, BulletType> entry : toReplace.entries()) {
                                itemTurret.ammoTypes.put(entry.key, newBullet.get(entry.value));
                            }
                        }
                    }
                }
            }
        });
    }

    public void loadContent() {
        modInfo = Vars.mods.getMod(this.getClass());
        print("java loadContent");
        modAtlas = new ModAtlas();
        inTry(() -> {
            if (!headless) ModShaders.init();
        });
        Events.fire(ModEventType.ModContentLoad.class);
//        loadMaps();
        Seq<ContentList> loads = Seq.with(
                new ModSounds(), new ModStatusEffects(), new ModItems(), new ModLiquids(), new ModUnitTypes(), new ModBlocks(), new ModTechTree()
        );
        loads.each((load) -> {
            try {
                load.load();
            } catch (NullPointerException e) {
                if (!headless) showException(e);
            }
        });
        GasInit.init(true);
        Vars.content.each((c) -> {
            if (c instanceof UnlockableContent) checkTranslate((UnlockableContent) c);
        });
        loaded = true;
    }

}
