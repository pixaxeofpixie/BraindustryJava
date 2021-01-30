package ModVars.Classes.UI.settings;

import ModVars.modVars;
import arc.Core;
import arc.KeyBinds;
import arc.graphics.Color;
import arc.input.InputDevice;
import arc.input.KeyCode;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.style.Style;
import arc.scene.ui.*;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectIntMap;
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.Strings;
import arc.util.Time;

public class ModKeybindDialog extends Dialog {
    protected KeybindDialog.KeybindDialogStyle style;
    protected KeyBinds.Section section;
    protected KeyBinds.KeyBind rebindKey = null;
    protected boolean rebindAxis = false;
    protected boolean rebindMin = true;
    protected KeyCode minKey = null;
    protected Dialog rebindDialog;
    protected ObjectIntMap<KeyBinds.Section> sectionControls = new ObjectIntMap<>();

    public ModKeybindDialog() {
        super(Core.bundle.get("keybind.title", "Rebind Keys"));
        this.style = Core.scene.getStyle(KeybindDialog.KeybindDialogStyle.class);
        this.setup();
        this.addCloseButton();
    }

    @Override
    public void closeOnBack() {
        super.closeOnBack();
    }

    @Override
    public void hide() {
        if (isShown()){
            modVars.keyBinds.save();
        }
        super.hide();
    }

    public void setStyle(KeybindDialog.KeybindDialogStyle style) {
        this.style = style;
        this.setup();
    }

    private void setup() {
        this.cont.clear();
        KeyBinds.Section[] sections = modVars.keyBinds.getSections();
        Stack stack = new Stack();
        ButtonGroup<TextButton> group = new ButtonGroup<>();
        ScrollPane pane = new ScrollPane(stack);
        pane.setFadeScrollBars(false);
        this.section = sections[0];

        for (KeyBinds.Section section : sections) {
            if (!this.sectionControls.containsKey(section)) {
                this.sectionControls.put(section, Core.input.getDevices().indexOf(section.device, true));
            }

            if (this.sectionControls.get(section, 0) >= Core.input.getDevices().size) {
                this.sectionControls.put(section, 0);
                section.device = Core.input.getDevices().get(0);
            }

            if (sections.length != 1) {
                TextButton button = new TextButton(Core.bundle.get("section." + section.name + ".name", Strings.capitalize(section.name)));
                if (section.equals(this.section)) {
                    button.toggle();
                }

                button.clicked(() -> {
                    this.section = section;
                });
                group.add(button);
                this.cont.add(button).fill();
            }

            Table table = new Table();
            Label device = new Label("Keyboard");
            device.setAlignment(1);
            Seq<InputDevice> devices = Core.input.getDevices();
            Table stable = new Table();
            stable.button("<", () -> {
                int i = this.sectionControls.get(section, 0);
                if (i - 1 >= 0) {
                    this.sectionControls.put(section, i - 1);
                    section.device =devices.get(i - 1);
                    this.setup();
                }

            }).disabled(this.sectionControls.get(section, 0) - 1 < 0).size(40.0F);
            stable.add(device).minWidth(device.getMinWidth() + 60.0F);
            device.setText(( Core.input.getDevices().get(this.sectionControls.get(section, 0))).name());
            stable.button(">", () -> {
                int i = this.sectionControls.get(section, 0);
                if (i + 1 < devices.size) {
                    this.sectionControls.put(section, i + 1);
                    section.device = devices.get(i + 1);
                    this.setup();
                }

            }).disabled(this.sectionControls.get(section, 0) + 1 >= devices.size).size(40.0F);
            table.add(stable).colspan(4);
            table.row();
            table.add().height(10.0F);
            table.row();
            if (section.device.type() == InputDevice.DeviceType.controller) {
                table.table((info) -> {
                    info.add("Controller Type: [#" + this.style.controllerColor.toString().toUpperCase() + "]" + Strings.capitalize(section.device.name())).left();
                });
            }

            table.row();
            String lastCategory = null;
            KeyBinds.KeyBind[] var14 = modVars.keyBinds.getKeybinds();

            for (KeyBinds.KeyBind keybind : var14) {
                if (lastCategory != keybind.category() && keybind.category() != null) {
                    table.add(Core.bundle.get("category." + keybind.category() + ".name", Strings.capitalize(keybind.category()))).color(Color.gray).colspan(4).pad(10.0F).padBottom(4.0F).row();
                    table.image().color(Color.gray).fillX().height(3.0F).pad(6.0F).colspan(4).padTop(0.0F).padBottom(10.0F).row();
                    lastCategory = keybind.category();
                }

                KeyBinds.Axis axis = modVars.keyBinds.get(section, keybind);
                if (keybind.defaultValue(section.device.type()) instanceof KeyBinds.Axis) {
                    table.add(Core.bundle.get("keybind." + keybind.name() + ".name", Strings.capitalize(keybind.name())), this.style.keyNameColor).left().padRight(40.0F).padLeft(8.0F);
                    if (axis.key != null) {
                        table.add(axis.key.toString(), this.style.keyColor).left().minWidth(90.0F).padRight(20.0F);
                    } else {
                        Table axt = new Table();
                        axt.left();
                        axt.labelWrap(axis.min.toString() + " [red]/[] " + axis.max.toString()).color(this.style.keyColor).width(140.0F).padRight(5.0F);
                        table.add(axt).left().minWidth(90.0F).padRight(20.0F);
                    }

                    table.button(Core.bundle.get("settings.rebind", "Rebind"), () -> {
                        this.rebindAxis = true;
                        this.rebindMin = true;
                        this.openDialog(section, keybind);
                    }).width(130.0F);
                } else {
                    table.add(Core.bundle.get("keybind." + keybind.name() + ".name", Strings.capitalize(keybind.name())), this.style.keyNameColor).left().padRight(40.0F).padLeft(8.0F);
                    table.add(modVars.keyBinds.get(section, keybind).key.toString(), this.style.keyColor).left().minWidth(90.0F).padRight(20.0F);
                    table.button(Core.bundle.get("settings.rebind", "Rebind"), () -> {
                        this.rebindAxis = false;
                        this.rebindMin = false;
                        this.openDialog(section, keybind);
                    }).width(130.0F);
                }

                table.button(Core.bundle.get("settings.resetKey", "Reset"), () -> {
                    modVars.keyBinds.resetToDefault(section, keybind);
                    this.setup();
                }).width(130.0F);
                table.row();
            }

            table.visible(() -> {
                return this.section.equals(section);
            });
            table.button(Core.bundle.get("settings.reset", "Reset to Defaults"), () -> {
                modVars.keyBinds.resetToDefaults();
                this.setup();
            }).colspan(4).padTop(4.0F).fill();
            stack.add(table);
        }

        this.cont.row();
        this.cont.add(pane).growX().colspan(sections.length);
    }

    void rebind(KeyBinds.Section section, KeyBinds.KeyBind bind, KeyCode newKey) {
        if (this.rebindKey != null) {
            this.rebindDialog.hide();
            boolean isAxis = bind.defaultValue(section.device.type()) instanceof KeyBinds.Axis;
            if (isAxis) {
                if (newKey.axis || !this.rebindMin) {
                    ((OrderedMap)section.binds.get(section.device.type(), OrderedMap::new)).put(this.rebindKey, newKey.axis ? new KeyBinds.Axis(newKey) : new KeyBinds.Axis(this.minKey, newKey));
                }
            } else {
                ((OrderedMap)section.binds.get(section.device.type(), OrderedMap::new)).put(this.rebindKey, new KeyBinds.Axis(newKey));
            }

            if (this.rebindAxis && isAxis && this.rebindMin && !newKey.axis) {
                this.rebindMin = false;
                this.minKey = newKey;
                this.openDialog(section, this.rebindKey);
            } else {
                this.rebindKey = null;
                this.rebindAxis = false;
                this.setup();
            }

        }
    }

    private void openDialog(final KeyBinds.Section section, final KeyBinds.KeyBind name) {
        this.rebindDialog = new Dialog(this.rebindAxis ? Core.bundle.get("keybind.press.axis", "Press an axis or key...") : Core.bundle.get("keybind.press", "Press a key..."));
        this.rebindKey = name;
        this.rebindDialog.titleTable.getCells().first().pad(4.0F);
        if (section.device.type() == InputDevice.DeviceType.keyboard) {
            this.rebindDialog.keyDown((i) -> {
                this.setup();
            });
            this.rebindDialog.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    if (Core.app.isAndroid()) {
                        return false;
                    } else {
                        rebind(section, name, button);
                        return false;
                    }
                }

                public boolean keyDown(InputEvent event, KeyCode keycode) {
                    rebindDialog.hide();
                    if (keycode == KeyCode.escape) {
                        return false;
                    } else {
                        rebind(section, name, keycode);
                        return false;
                    }
                }

                public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                    if (!rebindAxis) {
                        return false;
                    } else {
                        rebindDialog.hide();
                        rebind(section, name, KeyCode.scroll);
                        return false;
                    }
                }
            });
        }

        this.rebindDialog.show();
        Time.runTask(1.0F, () -> {
            this.getScene().setScrollFocus(this.rebindDialog);
        });
    }

    public static class KeybindDialogStyle extends Style {
        public Color keyColor;
        public Color keyNameColor;
        public Color controllerColor;

        public KeybindDialogStyle() {
            this.keyColor = Color.white;
            this.keyNameColor = Color.white;
            this.controllerColor = Color.white;
        }
    }
}
