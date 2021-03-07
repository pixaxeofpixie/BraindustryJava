package braindustry.ui;

import arc.graphics.Color;
import arc.scene.ui.ImageButton;
import mindustry.ui.Styles;

import static mindustry.gen.Tex.*;
import static mindustry.gen.Tex.buttonDisabled;

public class ModStyles extends Styles{
    private static final Color black = new Color(0f, 0f, 0f, 1f),black9 = new Color(0f, 0f, 0f, 0.9f),black8 = new Color(0f, 0f, 0f, 0.8f),black6 = new Color(0f, 0f, 0f, 0.6f), black5 = new Color(0f, 0f, 0f, 0.5f),black3 = new Color(0f, 0f, 0f, 0.3f),none = new Color(0f, 0f, 0f, 0f);

    public static ImageButton.ImageButtonStyle buttonSquarei, alphai;

    public static void load() {

        buttonSquarei = new ImageButton.ImageButtonStyle() {
            {
                imageDisabledColor = Color.gray;
                imageUpColor = Color.white;
                over = buttonSquareOver;
                disabled = buttonDisabled;
                down = buttonSquareDown;
                up = buttonSquare;
            }
        };
        alphai = new ImageButton.ImageButtonStyle() {
            {
                imageDisabledColor = Color.gray;
                imageUpColor = Color.white;
                over = buttonDisabled.tint(black3);
                disabled = buttonDisabled.tint(none);
                down = buttonSquareDown.tint(none);
                up = buttonSquare.tint(none);
            }
        };
    }
}
