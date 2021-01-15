package braindustry.ModVars.Classes.UI.Cheat;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Tex;
import mindustry.ui.Cicon;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

import java.util.concurrent.atomic.AtomicInteger;

public class UnlockContentDialog extends BaseDialog {
    private Table items;

    public UnlockContentDialog() {
        super("Unlock content dialog");
        addCloseButton();
        addCloseListener();
    }

    @Override
    public void addCloseButton() {
        this.setFillParent(true);
        this.keyDown((key) -> {
            if (key == KeyCode.escape || key == KeyCode.back) {
                Core.app.post(this::hide);
            }

        });
        this.cont.pane((t) -> {
            this.items = t.margin(10.0F);
        }).left();
        this.shown(this::setup);
        this.hidden(() -> {
        });
        super.addCloseButton();
    }

    void setup() {
        this.items.clearChildren();
        this.items.left();
        float bsize = 40.0F;
        AtomicInteger i=new AtomicInteger(0);
        Vars.content.each(c -> {
            if (c instanceof UnlockableContent) {

                UnlockableContent content = (UnlockableContent) c;
                if (content instanceof Block && (((Block)content).buildVisibility!= BuildVisibility.shown &&((Block)content).buildVisibility!= BuildVisibility.campaignOnly))return;
                this.items.table(Tex.pane, (t) -> {
                    t.margin(4.0F).marginRight(8.0F).left();
                    t.image(content.icon(Cicon.small)).size(24.0F).padRight(4.0F).padLeft(4.0F);
                    t.label(() -> {
                        return content.localizedName;
                    }).left().width(90.0F*2f);
                    ImageButton button = new ImageButton(Tex.whitePane);
                    button.clicked(()->{
                        if (content.unlocked()) {
                            content.clearUnlock();
                        } else {
                            content.unlock();
                        }
                    });
                    Image image = new Image();
//                   button.image().size(bsize);
//                    Cell<Image> imageCell = button.add(image).size(bsize);

                    t.add(button).size(bsize).update((b) -> {
//                        b.getImage().fillParent=true;
//                        b.getImage().setDrawable(content.unlocked()?Icon.cancel:Icon.add);
                        b.setColor(content.unlocked() ? Color.lime : Color.scarlet);
                        b.getImageCell().color(b.color);
                    });
                    /*t.button(new Image().getDrawable(), () -> {
                        if (content.unlocked()) {
                            content.clearUnlock();
                        } else {
                            content.unlock();
                        }

                    }).size(bsize).update((b) -> {
                        b.getImageCell().size(bsize);

                        b.setColor(content.unlocked() ? Color.lime : Color.scarlet);
                    });*/
                }).pad(2.0F).left().fillX();
                i.addAndGet(1);
                if (i.get()%3==0 && (!Vars.mobile || !Core.graphics.isPortrait())){
                    this.items.row();
                }
            }
        });
    }
}
