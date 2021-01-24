package ModVars.Classes.UI.settings;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.files.ZipFi;
import arc.graphics.Texture;
import arc.input.KeyCode;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.event.FocusListener;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.io.Streams;
import braindustry.input.ModDesktopInput;
import braindustry.input.ModMobileInput;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.core.GameState;
import mindustry.core.Version;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Saves;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.graphics.Shaders;
import mindustry.input.DesktopInput;
import mindustry.input.MobileInput;
import mindustry.type.Planet;
import mindustry.type.Sector;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;


import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static arc.Core.*;
import static arc.Core.bundle;
import static mindustry.Vars.net;
import static mindustry.Vars.*;

public class AdvancedSettingsMenuDialog extends SettingsMenuDialog {

    private Table prefs;
    private Table menu;
    private BaseDialog dataDialog;
    private boolean wasPaused;
    public void edge(int edge){
        this.edge=edge;
    }
    public int edge(){
        return edge;
    }
    protected void DialogConstuctor(){
        this.ignoreTouchDown = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                event.cancel();
                return false;
            }
        };
        AdvancedSettingsMenuDialog me=this;
        boolean keepWithinStage=true;
        /*this.isMovable = false;
        this.isModal = true;
        this.isResizable = false;
        this.center = true;
        this.resizeBorder = 8;
        this.keepWithinStage = true;*/
        Dialog.DialogStyle style=getStyle();
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null.");
        } else {
            this.touchable = Touchable.enabled;
            this.setClip(true);
//            this.titleTable.add(this.title).expandX().fillX().minWidth(0.0F);
            this.add(this.titleTable).growX().row();
            this.setStyle(style);
            this.setWidth(150.0F);
            this.setHeight(150.0F);
            this.addCaptureListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    me.toFront();
                    return false;
                }
            });
            this.addListener(new InputListener() {
                float startX;
                float startY;
                float lastX;
                float lastY;

                private void updateEdge(float x, float y) {
                    float border = (float)8f / 2.0F;
                    float width = me.getWidth();
                    float height = me.getHeight();
                    float padTop = me.getMarginTop();
                    float padRight = me.getMarginRight();
                    float right = width - padRight;
                    me.edge(0);
                    if (me.isResizable() && x >= me.getMarginLeft() - border && x <= right + border && y >= me.getMarginBottom() - border) {
                        Dialog mevar10000;
                        if (x < me.getMarginLeft() + border) {
                            me.edge |= 8;
                        }

                        if (x > right - border) {
                            me.edge |= 16;
                        }

                        if (y < me.getMarginBottom() + border) {
                            me.edge |= 4;
                        }

                        if (me.edge != 0) {
                            border += 25.0F;
                        }

                        if (x < me.getMarginLeft() + border) {
                            me.edge |= 8;
                        }

                        if (x > right - border) {
                            me.edge |= 16;
                        }

                        if (y < me.getMarginBottom() + border) {
                            me.edge |= 4;
                        }
                    }

                    if (me.isMovable() && me.edge == 0 && y <= height && y >= height - padTop && x >= me.getMarginLeft() && x <= right) {
                        me.edge = 32;
                    }

                }

                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    if (button == KeyCode.mouseLeft) {
                        this.updateEdge(x, y);
                        me.dragging = me.edge != 0;
                        this.startX = x;
                        this.startY = y;
                        this.lastX = x - me.getWidth();
                        this.lastY = y - me.getHeight();
                    }

                    return me.edge != 0 || me.isModal();
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    me.dragging = false;
                }

                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (me.dragging) {
                        float width = me.getWidth();
                        float height = me.getHeight();
                        float windowX = me.x;
                        float windowY = me.y;
                        float minWidth = me.getMinWidth();
                        float minHeight = me.getMinHeight();
                        Scene stage = me.getScene();
                        boolean clampPosition = keepWithinStage && me.parent == stage.root;
                        float amountY;
                        if ((me.edge & 32) != 0) {
                            amountY = x - this.startX;
                            float amountYx = y - this.startY;
                            windowX += amountY;
                            windowY += amountYx;
                        }

                        if ((me.edge & 8) != 0) {
                            amountY = x - this.startX;
                            if (width - amountY < minWidth) {
                                amountY = -(minWidth - width);
                            }

                            if (clampPosition && windowX + amountY < 0.0F) {
                                amountY = -windowX;
                            }

                            width -= amountY;
                            windowX += amountY;
                        }

                        if ((me.edge & 4) != 0) {
                            amountY = y - this.startY;
                            if (height - amountY < minHeight) {
                                amountY = -(minHeight - height);
                            }

                            if (clampPosition && windowY + amountY < 0.0F) {
                                amountY = -windowY;
                            }

                            height -= amountY;
                            windowY += amountY;
                        }

                        if ((me.edge & 16) != 0) {
                            amountY = x - this.lastX - width;
                            if (width + amountY < minWidth) {
                                amountY = minWidth - width;
                            }

                            if (clampPosition && windowX + width + amountY > stage.getWidth()) {
                                amountY = stage.getWidth() - windowX - width;
                            }

                            width += amountY;
                        }

                        if ((me.edge & 2) != 0) {
                            amountY = y - this.lastY - height;
                            if (height + amountY < minHeight) {
                                amountY = minHeight - height;
                            }

                            if (clampPosition && windowY + height + amountY > stage.getHeight()) {
                                amountY = stage.getHeight() - windowY - height;
                            }

                            height += amountY;
                        }

                        me.setBounds((float)Math.round(windowX), (float)Math.round(windowY), (float)Math.round(width), (float)Math.round(height));
                    }
                }

                public boolean mouseMoved(InputEvent event, float x, float y) {
                    this.updateEdge(x, y);
                    return me.isModal();
                }

                public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                    return me.isModal();
                }

                public boolean keyDown(InputEvent event, KeyCode keycode) {
                    return me.isModal();
                }

                public boolean keyUp(InputEvent event, KeyCode keycode) {
                    return me.isModal();
                }

                public boolean keyTyped(InputEvent event, char character) {
                    return me.isModal();
                }
            });
            this.setOrigin(1);
            this.defaults().pad(3.0F);
            this.add(this.cont).expand().fill();
            this.row();
            this.add(this.buttons).fillX();
            this.cont.defaults().pad(3.0F);
            this.buttons.defaults().pad(3.0F);
            this.shown(this::updateScrollFocus);
        }
    }
    public AdvancedSettingsMenuDialog(){
        super();
        clearContent(this);
        this.addCloseButton();
        this.main = new SettingsDialog.SettingsTable();
        this.cont.add(this.main);
        DialogConstuctor();
        hidden(() -> {
            Sounds.back.play();
            if(state.isGame()){
                if(!wasPaused || net.active())
                    state.set(GameState.State.playing);
            }
        });

        shown(() -> {
            back();
            if(state.isGame()){
                wasPaused = state.is(GameState.State.paused);
                state.set(GameState.State.paused);
            }

            rebuildMenu();
        });

        setFillParent(true);
//        title.setAlignment(Align.center);
//        titleTable.row();
//        titleTable.add(new Image()).growX().height(3f).pad(4f).get().setColor(Pal.accent);

        cont.clearChildren();
        cont.remove();
        buttons.remove();

        menu = new Table(Tex.button);

        game = new SettingsTable();
        graphics = new SettingsTable();
        sound = new SettingsTable();

        prefs = new Table();
        prefs.top();
        prefs.margin(14f);

        rebuildMenu();

        prefs.clearChildren();
        prefs.add(menu);

        dataDialog = new BaseDialog("@settings.data");
        dataDialog.addCloseButton();

        dataDialog.cont.table(Tex.button, t -> {
            t.defaults().size(280f, 60f).left();
            TextButton.TextButtonStyle style = Styles.cleart;

            t.button("@settings.cleardata", Icon.trash, style, () -> ui.showConfirm("@confirm", "@settings.clearall.confirm", () -> {
                ObjectMap<String, Object> map = new ObjectMap<>();
                for(String value : Core.settings.keys()){
                    if(value.contains("usid") || value.contains("uuid")){
                        map.put(value, Core.settings.get(value, null));
                    }
                }
                Core.settings.clear();
                Core.settings.putAll(map);

                for(Fi file : dataDirectory.list()){
                    file.deleteDirectory();
                }

                Core.app.exit();
            })).marginLeft(4);

            t.row();

            t.button("@settings.clearsaves", Icon.trash, style, () -> {
                ui.showConfirm("@confirm", "@settings.clearsaves.confirm", () -> {
                    control.saves.deleteAll();
                });
            }).marginLeft(4);

            t.row();

            t.button("@settings.clearresearch", Icon.trash, style, () -> {
                ui.showConfirm("@confirm", "@settings.clearresearch.confirm", () -> {
                    universe.clearLoadoutInfo();
                    for(TechTree.TechNode node : TechTree.all){
                        node.reset();
                    }
                    content.each(c -> {
                        if(c instanceof UnlockableContent){
                           ((UnlockableContent) c).clearUnlock();
                        }
                    });
                    settings.remove("unlocks");
                });
            }).marginLeft(4);

            t.row();

            t.button("@settings.clearcampaignsaves", Icon.trash, style, () -> {
                ui.showConfirm("@confirm", "@settings.clearcampaignsaves.confirm", () -> {
                    for(Planet planet : content.planets()){
                        for(Sector sec : planet.sectors){
                            sec.clearInfo();
                            if(sec.save != null){
                                sec.save.delete();
                                sec.save = null;
                            }
                        }
                    }

                    for(Saves.SaveSlot slot : control.saves.getSaveSlots().copy()){
                        if(slot.isSector()){
                            slot.delete();
                        }
                    }
                });
            }).marginLeft(4);

            t.row();

            t.button("@data.export", Icon.upload, style, () -> {
                if(ios){
                    Fi file = Core.files.local("mindustry-data-export.zip");
                    try{
                        exportData(file);
                    }catch(Exception e){
                        ui.showException(e);
                    }
                    platform.shareFile(file);
                }else{
                    platform.showFileChooser(false, "zip", file -> {
                        try{
                            exportData(file);
                            ui.showInfo("@data.exported");
                        }catch(Exception e){
                            e.printStackTrace();
                            ui.showException(e);
                        }
                    });
                }
            }).marginLeft(4);

            t.row();

            t.button("@data.import", Icon.download, style, () -> ui.showConfirm("@confirm", "@data.import.confirm", () -> platform.showFileChooser(true, "zip", file -> {
                try{
                    importData(file);
                    Core.app.exit();
                }catch(IllegalArgumentException e){
                    ui.showErrorMessage("@data.invalid");
                }catch(Exception e){
                    e.printStackTrace();
                    if(e.getMessage() == null || !e.getMessage().contains("too short")){
                        ui.showException(e);
                    }else{
                        ui.showErrorMessage("@data.invalid");
                    }
                }
            }))).marginLeft(4);

            if(!mobile){
                t.row();
                t.button("@data.openfolder", Icon.folder, style, () -> Core.app.openFolder(Core.settings.getDataDirectory().absolutePath())).marginLeft(4);
            }

            t.row();

            t.button("@crash.export", Icon.upload, style, () -> {
                if(settings.getDataDirectory().child("crashes").list().length == 0 && !settings.getDataDirectory().child("last_log.txt").exists()){
                    ui.showInfo("@crash.none");
                }else{
                    if(ios){
                        Fi logs = tmpDirectory.child("logs.txt");
                        logs.writeString(getLogs());
                        platform.shareFile(logs);
                    }else{
                        platform.showFileChooser(false, "txt", file -> {
                            file.writeString(getLogs());
                            app.post(() -> ui.showInfo("@crash.exported"));
                        });
                    }
                }
            }).marginLeft(4);
        });

        ScrollPane pane = new ScrollPane(prefs);
        pane.addCaptureListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                Element actor = pane.hit(x, y, true);
                if(actor instanceof Slider){
                    pane.setFlickScroll(false);
                    return true;
                }

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                pane.setFlickScroll(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        pane.setFadeScrollBars(false);

        row();
        add(pane).grow().top();
        row();
        add(buttons).fillX();

        addSettings();
    }
    String getLogs(){
        Fi log = settings.getDataDirectory().child("last_log.txt");

        StringBuilder out = new StringBuilder();
        for(Fi fi : settings.getDataDirectory().child("crashes").list()){
            out.append(fi.name()).append("\n\n").append(fi.readString()).append("\n");
        }

        if(log.exists()){
            out.append("\nlast log:\n").append(log.readString());
        }

        return out.toString();
    }

    void rebuildMenu(){
        menu.clearChildren();

        TextButton.TextButtonStyle style = Styles.cleart;

        menu.defaults().size(300f, 60f);
        menu.button("@settings.game", style, () -> visible(0));
        menu.row();
        menu.button("@settings.graphics", style, () -> visible(1));
        menu.row();
        menu.button("@settings.sound", style, () -> visible(2));
        menu.row();
        menu.button("@settings.language", style, ui.language::show);
        if(!mobile || Core.settings.getBool("keyboard")){
            menu.row();
            menu.button("@settings.controls", style, ui.controls::show);
        }

        menu.row();
        menu.button("@settings.data", style, () -> dataDialog.show());
    }

    void addSettings(){
        sound.sliderPref("musicvol", bundle.get("setting.musicvol.name", "Music Volume"), 100, 0, 100, 1, i -> i + "%");
        sound.sliderPref("sfxvol", bundle.get("setting.sfxvol.name", "SFX Volume"), 100, 0, 100, 1, i -> i + "%");
        sound.sliderPref("ambientvol", bundle.get("setting.ambientvol.name", "Ambient Volume"), 100, 0, 100, 1, i -> i + "%");

        game.screenshakePref();
        if(mobile){
            game.checkPref("autotarget", true);
            game.checkPref("keyboard", false, val -> control.setInput(val ? new ModDesktopInput() : new ModMobileInput()));
            if(Core.settings.getBool("keyboard")){
                control.setInput(new ModDesktopInput());
            }
        }
        //the issue with touchscreen support on desktop is that:
        //1) I can't test it
        //2) the SDL backend doesn't support multitouch
        /*else{
            game.checkPref("touchscreen", false, val -> control.setInput(!val ? new DesktopInput() : new MobileInput()));
            if(Core.settings.getBool("touchscreen")){
                control.setInput(new MobileInput());
            }
        }*/
        game.sliderPref("saveinterval", 60, 10, 5 * 120, 10, i -> Core.bundle.format("setting.seconds", i));

        if(!mobile){
            game.checkPref("crashreport", true);
        }

        game.checkPref("savecreate", true);
        game.checkPref("blockreplace", true);
        game.checkPref("conveyorpathfinding", true);
        game.checkPref("hints", true);

        if(!mobile){
            game.checkPref("backgroundpause", true);
            game.checkPref("buildautopause", false);
        }

        if(steam){
            game.sliderPref("playerlimit", 16, 2, 32, i -> {
                platform.updateLobby();
                return i + "";
            });

            if(!Version.modifier.contains("beta")){
                game.checkPref("publichost", false, i -> {
                    platform.updateLobby();
                });
            }
        }

        graphics.sliderPref("uiscale", 100, 25, 300, 25, s -> {
            if(ui.settings != null){
                Core.settings.put("uiscalechanged", true);
            }
            return s + "%";
        });
        graphics.sliderPref("fpscap", 240, 15, 245, 5, s -> (s > 240 ? Core.bundle.get("setting.fpscap.none") : Core.bundle.format("setting.fpscap.text", s)));
        graphics.sliderPref("chatopacity", 100, 0, 100, 5, s -> s + "%");
        graphics.sliderPref("lasersopacity", 100, 0, 100, 5, s -> {
            if(ui.settings != null){
                Core.settings.put("preferredlaseropacity", s);
            }
            return s + "%";
        });
        graphics.sliderPref("bridgeopacity", 100, 0, 100, 5, s -> s + "%");

        if(!mobile){
            graphics.checkPref("vsync", true, b -> Core.graphics.setVSync(b));
            graphics.checkPref("fullscreen", false, b -> {
                if(b){
                    Core.graphics.setFullscreenMode(Core.graphics.getDisplayMode());
                }else{
                    Core.graphics.setWindowedMode(Core.graphics.getWidth(), Core.graphics.getHeight());
                }
            });

            graphics.checkPref("borderlesswindow", false, b -> Core.graphics.setUndecorated(b));

            Core.graphics.setVSync(Core.settings.getBool("vsync"));
            if(Core.settings.getBool("fullscreen")){
                Core.app.post(() -> Core.graphics.setFullscreenMode(Core.graphics.getDisplayMode()));
            }

            if(Core.settings.getBool("borderlesswindow")){
                Core.app.post(() -> Core.graphics.setUndecorated(true));
            }
        }else if(!ios){
            graphics.checkPref("landscape", false, b -> {
                if(b){
                    platform.beginForceLandscape();
                }else{
                    platform.endForceLandscape();
                }
            });

            if(Core.settings.getBool("landscape")){
                platform.beginForceLandscape();
            }
        }

        graphics.checkPref("effects", true);
        graphics.checkPref("atmosphere", !mobile);
        graphics.checkPref("destroyedblocks", true);
        graphics.checkPref("blockstatus", false);
        graphics.checkPref("playerchat", true);
        if(!mobile){
            graphics.checkPref("coreitems", true);
        }
        graphics.checkPref("minimap", !mobile);
        graphics.checkPref("smoothcamera", true);
        graphics.checkPref("position", false);
        graphics.checkPref("fps", false);
        graphics.checkPref("playerindicators", true);
        graphics.checkPref("indicators", true);
        graphics.checkPref("showweather", true);
        graphics.checkPref("animatedwater", true);
        if(Shaders.shield != null){
            graphics.checkPref("animatedshields", !mobile);
        }

        if(!ios){
            graphics.checkPref("bloom", true, val -> renderer.toggleBloom(val));
        }else{
            Core.settings.put("bloom", false);
        }

        graphics.checkPref("pixelate", false, val -> {
            if(val){
                Events.fire(EventType.Trigger.enablePixelation);
            }
        });

        graphics.checkPref("linear", !mobile, b -> {
            for(Texture tex : Core.atlas.getTextures()){
                Texture.TextureFilter filter = b ? Texture.TextureFilter.linear : Texture.TextureFilter.nearest;
                tex.setFilter(filter, filter);
            }
        });

        if(Core.settings.getBool("linear")){
            for(Texture tex : Core.atlas.getTextures()){
                Texture.TextureFilter filter = Texture.TextureFilter.linear;
                tex.setFilter(filter, filter);
            }
        }

        if(!mobile){
            Core.settings.put("swapdiagonal", false);
        }

        graphics.checkPref("flow", true);
    }

    @Override
    public void exportData(Fi file) throws IOException {
        Seq<Fi> files = new Seq<>();
        files.add(Core.settings.getSettingsFile());
        files.addAll(customMapDirectory.list());
        files.addAll(saveDirectory.list());
        files.addAll(screenshotDirectory.list());
        files.addAll(modDirectory.list());
        files.addAll(schematicDirectory.list());
        String base = Core.settings.getDataDirectory().path();

        try(OutputStream fos = file.write(false, 2048); ZipOutputStream zos = new ZipOutputStream(fos)){
            for(Fi add : files){
                if(add.isDirectory()) continue;
                zos.putNextEntry(new ZipEntry(add.path().substring(base.length())));
                Streams.copy(add.read(), zos);
                zos.closeEntry();
            }
        }
    }

    @Override
    public void importData(Fi file){
        Fi dest = Core.files.local("zipdata.zip");
        file.copyTo(dest);
        Fi zipped = new ZipFi(dest);

        Fi base = Core.settings.getDataDirectory();
        if(!zipped.child("settings.bin").exists()){
            throw new IllegalArgumentException("Not valid save data.");
        }

        //delete old saves so they don't interfere
        saveDirectory.deleteDirectory();

        //purge existing tmp data, keep everything else
        tmpDirectory.deleteDirectory();

        zipped.walk(f -> f.copyTo(base.child(f.path())));
        dest.delete();

        //clear old data
        settings.clear();
        //load data so it's saved on exit
        settings.load();
    }

    private void back(){
        rebuildMenu();
        prefs.clearChildren();
        prefs.add(menu);
    }

    private void visible(int index){
        prefs.clearChildren();
        prefs.add(new Table[]{game, graphics, sound}[index]);
    }

    @Override
    public void addCloseButton() {
        buttons.button("@back", Icon.leftOpen, () -> {
            if (prefs.getChildren().first() != menu) {
                back();
            } else {
                hide();
            }
        }).size(230f, 64f);

        keyDown(key -> {
            if (key == KeyCode.escape || key == KeyCode.back) {
                if (prefs.getChildren().first() != menu) {
                    back();
                } else {
                    hide();
                }
            }
        });
    }
    public static void clearContent(Table dialog) {
        dialog.clear();
        dialog.clearActions();
        dialog.clearListeners();
        dialog.reset();
    }

    public static void clearContent(SettingsMenuDialog dialog){
        dialog.clear();
        dialog.clearActions();
        dialog.clearListeners();
        clearContent(dialog.cont);
        clearContent(dialog.main);
        clearContent(dialog.buttons);
        dialog.reset();
        clearContent(dialog.game);
        clearContent(dialog.graphics);
    }
    public static void clearContent(AdvancedSettingsMenuDialog dialog){
        dialog.clear();
        dialog.clearActions();
        dialog.clearListeners();
        clearContent(dialog.cont);
        clearContent(dialog.main);
        clearContent(dialog.buttons);
        clearContent(dialog.game);
        clearContent(dialog.graphics);
    }
    public static void init() {
        if (mobile){
            control.setInput(new ModMobileInput());
        } else {
            control.setInput(new ModDesktopInput());
        }
        clearContent(ui.settings);
//        Vars.ui.settings.clipEnd();
        Vars.ui.settings.remove();
//        Vars.ui.settings.shown();
        Vars.ui.settings = new AdvancedSettingsMenuDialog();

    }
}
