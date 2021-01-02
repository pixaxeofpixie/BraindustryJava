package braindustry.modVars.Classes.UI;

import arc.Core;
import arc.Input;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextField;
import mindustry.Vars;

public class ModTextInput {
    public static void showTextInput(String title, String text, String def, Cons<String> confirmed) {
        ModTextInput.showTextInput(title, text, 32, def, confirmed);
    }

    public static void showTextInput(String titleText, String text, int textLength, String def, Cons<String> confirmed) {
        ModTextInput.showTextInput(titleText, text, textLength, def, (t,c)->{return true;}, confirmed);
    }
    public static void showTextInput(final String titleText, final String dtext, final int textLength, final String def, final TextField.TextFieldFilter filter, final Cons<String> confirmed) {
        if (Vars.mobile) {
            Core.input.getTextInput(new Input.TextInput() {
                {
                    this.title = titleText.startsWith("@") ? Core.bundle.get(titleText.substring(1)) : titleText;
                    this.text = def;
                    this.numeric=filter== TextField.TextFieldFilter.digitsOnly;
//                    this.numeric = inumeric;
                    this.maxLength = textLength;
                    this.accepted = confirmed;
                }
            });
        } else {
            Dialog var10001 = new Dialog(titleText) {
                {
                    this.cont.margin(30.0F).add(dtext).padRight(6.0F);
                    TextField field = (TextField)this.cont.field(def, (t) -> {
                    }).size(330.0F, 50.0F).get();
                    field.setFilter((f, c) -> {
                        return field.getText().length() < textLength && filter.acceptChar(f, c);
                    });
                    this.buttons.defaults().size(120.0F, 54.0F).pad(4.0F);
                    this.buttons.button("@cancel", this::hide);
                    this.buttons.button("@ok", () -> {
                        confirmed.get(field.getText());
                        this.hide();
                    }).disabled((b) -> {
                        return field.getText().isEmpty();
                    });
                    this.keyDown(KeyCode.enter, () -> {
                        String text = field.getText();
                        if (!text.isEmpty()) {
                            confirmed.get(text);
                            this.hide();
                        }

                    });
                    this.keyDown(KeyCode.escape, this::hide);
                    this.keyDown(KeyCode.back, this::hide);
                    this.show();
                    Core.scene.setKeyboardFocus(field);
                    field.setCursorPosition(def.length());
                }
            };
        }

    }

}
