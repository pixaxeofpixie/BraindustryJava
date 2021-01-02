package braindustry.ModContent.world.blocks.sandbox;

import arc.Core;
import arc.Input;
import arc.math.geom.Vec2;
import arc.scene.ui.TextArea;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

public class JsExecutor extends Block {

    public JsExecutor(String name) {
        super(name);
        this.configurable = true;
        this.solid = true;
        this.destructible = true;
        this.group = BlockGroup.logic;
        this.config(String.class, (tile, text) -> {
            JsExecutorBuild build;
            if ((build = (JsExecutorBuild) tile) == tile)
                if (tile instanceof JsExecutorBuild) {
                    build.code = text;
                }
        });
    }

    public class JsExecutorBuild extends Building {
        public String code = "";

        public JsExecutorBuild() {
        }

        public void drawSelect() {
            super.drawSelect();
        }

        public void buildConfiguration(Table table) {
            float buttonSize = 60.0f;
            table.button(Icon.edit, () -> {
                if (Vars.mobile) {
                    Input.TextInput textInput = new Input.TextInput();
                    textInput.text = this.code;
                    textInput.multiline = true;
                    textInput.maxLength = 900000000;
                    textInput.accepted = (str) -> {
                        this.configure(str);
                    };
                    Core.input.getTextInput(textInput);
                } else {
                    BaseDialog dialog = new BaseDialog("�������� js ����");
                    dialog.setFillParent(false);
                    TextArea a = dialog.cont.add(new TextArea(this.code.replace("\r", "\n"))).size(380 * 2, 160 * 3).get();
                    a.setFilter((textField, c) -> {
                        return true;

                    });
                    a.setMaxLength(900000000);
                    dialog.buttons.button("@ok", () -> {
                        this.configure(a.getText());
                        dialog.hide();
                    }).size(130, 60);
                    dialog.update(() -> {
                        if (this.tile.block() != block) {
                            dialog.hide();
                        }

                    });
                    dialog.show();
                }

                this.deselect();
            }).size(buttonSize);
            table.button(Icon.play, () -> {
                String result = Vars.mods.getScripts().runConsole(this.code);
                //Vars.ui.showText("[gray]result","result)
                int top;
                if (Vars.mobile) {
                    top = 153;
                } else {
                    top = 84;
                }
                Vars.ui.showInfoPopup("[gray]result[lightgray]:[white]\n" + result, 2, 10, top, 0, 0, 0);
                //Vars.ui.showInfoPopup("[gray]result[lightgray]:[white] "+result, 2, 27, 170, 0, 0, 0)
                //Vars.ui.showInfoFade("[gray]result[lightgray]:[white] "+result)
                //Vars.player.sendMessage("[gray]result[lightgray]:[white] "+result)
            }).size(buttonSize);
        }

        public void handleString(Object value) {
            this.code = "" + value;
        }

        public void updateTableAlign(Table table) {
            Vec2 pos = Core.input.mouseScreen(this.x, this.y + (float) (JsExecutor.this.size * 8) / 2.0F + 1.0F);
            table.setPosition(pos.x, pos.y, 4);
        }

        public String config() {
            return this.code;
        }

        public void write(Writes write) {
            super.write(write);
            write.str(this.code);
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            this.code = read.str();
        }
    }
}
