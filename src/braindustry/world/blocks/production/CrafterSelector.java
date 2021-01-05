package braindustry.world.blocks.production;


import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class CrafterSelector extends Block {
    public Color[] statusColors = {
            Color.valueOf("f25555"),
            Color.lime,
            Color.yellow,
    };

    public CrafterSelector(String name) {
        super(name);
        this.solid = true;
        this.update = true;
        this.configurable = true;
    }

    public class CrafterSelectorBuild extends Building {
        int up = 0, left = 0, right = 0, down = 0;

        public CrafterSelectorBuild() {

        }

        @Override
        public void updateTableAlign(Table table) {
//            Vec2 pos = Core.input.mouseScreen(this.x-1F, this.y + 5f + (this.block.size % 2)*8F);
            float addPos = Mathf.ceil(this.block.size / 2f) - 1;
//            addPos=0;
            Vec2 pos = Core.input.mouseScreen((this.x) + addPos - 0.5f, this.y + addPos);
            table.setSize(this.block.size * 12f);
            table.setPosition(pos.x, pos.y, 0);
        }

        private void openEditDialog() {
            BaseDialog dialog = new BaseDialog("@edit");
            dialog.cont.table((t) -> {
                float size = t.defaults().prefHeight();
                t.add();
                t.button(Icon.up, () -> {
                }).colspan(this.block.size);
                t.add().row();
                t.button(Icon.left, () -> {
                }).height(size * this.block.size);
                t.add().colspan(this.block.size).height(size * this.block.size);
                t.button(Icon.right, () -> {
                }).height(size * this.block.size).row();
                t.add();
                t.button(Icon.down, () -> {
                }).colspan(this.block.size);
                t.add();
            });
            dialog.addCloseButton();
            dialog.addCloseListener();
            dialog.show();
        }

        @Override
        public void buildConfiguration(Table table) {
            Table t = table;
            t.add();
            t.button(Icon.up, () -> {
                up=Mathf.mod(up+1,CrafterSelector.this.statusColors.length);
                updateConfig();
            }).update((b) -> {
                b.setColor(CrafterSelector.this.statusColors[up]);
            });
            t.add().row();
            t.button(Icon.left, () -> {
                left=Mathf.mod(left+1,CrafterSelector.this.statusColors.length);
                updateConfig();
            }).update((b) -> {
                b.setColor(CrafterSelector.this.statusColors[left]);
            });
            t.add();
            t.button(Icon.right, () -> {
                right=Mathf.mod(right+1,CrafterSelector.this.statusColors.length);
                updateConfig();
            }).update((b) -> {
                b.setColor(CrafterSelector.this.statusColors[right]);
            });
            t.row();
            t.add();
            t.button(Icon.down, () -> {
                down=Mathf.mod(down+1,CrafterSelector.this.statusColors.length);
                updateConfig();
            }).update((b) -> {
                b.setColor(CrafterSelector.this.statusColors[down]);
            });
            t.add();
            /*
            table.button("@edit",()->{
                BaseDialog dialog=new BaseDialog("@edit");
                dialog.addCloseButton();
                dialog.show();
            });*/
//            super.buildConfiguration(table);
        }

        private void updateConfig() {
        }
    }
}

