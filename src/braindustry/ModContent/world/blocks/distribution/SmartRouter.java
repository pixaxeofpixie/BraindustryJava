package braindustry.ModContent.world.blocks.distribution;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Blocks;
import mindustry.gen.*;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.meta.BlockGroup;

public class SmartRouter extends Router {
    TextureRegion cross, arrow;

    public SmartRouter(String name) {
        super(name);
        this.solid = true;
        this.update = true;
        this.configurable = true;
        this.hasItems = true;
        this.itemCapacity = 1;
        this.group = BlockGroup.transportation;
        this.unloadable = false;
        this.noUpdateDisabled = true;
//        Core.assets.get
    }

    @Override
    public void load() {
        super.load();
        this.cross = Core.atlas.find(this.name + "-cross");
        this.arrow = Core.atlas.find(this.name + "-arrow");
    }

    public class CustomRouterBuild extends Building {
        public Item lastItem;
        public Tile lastInput;
        public float time;
        boolean up = false, left = false, right = false, down = false;

        @Override
        public void updateTableAlign(Table table) {
//            Vec2 pos = Core.input.mouseScreen(this.x-1F, this.y + 5f + (this.block.size % 2)*8F);
            float addPos = Mathf.ceil(this.block.size / 2f) - 1;
//            addPos=0;
            Vec2 pos = Core.input.mouseScreen((this.x) + addPos - 0.5f, this.y + addPos);
            table.setSize(this.block.size * 12f);
            table.setPosition(pos.x, pos.y, 0);
        }

        @Override
        public void buildConfiguration(Table table) {
            Table t = table;
            t.add();
            t.button(Icon.up, () -> {
                up = !up;
                updateConfig();
            }).update((b) -> {
                b.setColor(up ? Color.lime : Color.valueOf("f25555"));
            });
            t.add().row();
            t.button(Icon.left, () -> {
                left = !left;
                updateConfig();
            }).update((b) -> {
                b.setColor(left ? Color.lime : Color.valueOf("f25555"));
            });
            t.add();
            t.button(Icon.right, () -> {
                right = !right;
                updateConfig();
            }).update((b) -> {
                b.setColor(right ? Color.lime : Color.valueOf("f25555"));
            });
            t.row();
            t.add();
            t.button(Icon.down, () -> {
                down = !down;
                updateConfig();
            }).update((b) -> {
                b.setColor(down ? Color.lime : Color.valueOf("f25555"));
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

        public void updateTile() {
            if (this.lastItem == null && this.items.any()) {
                this.lastItem = this.items.first();
            }

            if (this.lastItem != null) {
                this.time += 1.0F / SmartRouter.this.speed * this.delta();
                Building target = this.getTileTarget(this.lastItem, this.lastInput, false);
                if (target != null && (this.time >= 1.0F || !(target.block instanceof Router) && !target.block.instantTransfer)) {
                    this.getTileTarget(this.lastItem, this.lastInput, true);
                    target.handleItem(this, this.lastItem);
                    this.items.remove(this.lastItem, 1);
                    this.lastItem = null;
                }
            }

        }

        private void drawSide(boolean bool, int side) {
            if (bool) {
                Draw.rect(SmartRouter.this.arrow, this.x, this.y, side * 90);
            } else {
                Draw.rect(SmartRouter.this.cross, this.x, this.y, side * 90);
            }
        }

        @Override
        public void draw() {
            super.draw();
            drawSide(up, 1);
            drawSide(left, 2);
            drawSide(right, 0);
            drawSide(down, 3);
        }

        public int acceptStack(Item item, int amount, Teamc source) {
            return 0;
        }

        public boolean acceptItem(Building source, Item item) {
            return this.team == source.team && this.lastItem == null && this.items.total() == 0 && acceptSide(source);
        }

        public void handleItem(Building source, Item item) {
            this.items.add(item, 1);
            this.lastItem = item;
            this.time = 0.0F;
            this.lastInput = source.tile();
        }

        public int removeStack(Item item, int amount) {
            int result = super.removeStack(item, amount);
            if (result != 0 && item == this.lastItem) {
                this.lastItem = null;
            }

            return result;
        }

        public Building getTileTarget(Item item, Tile from, boolean set) {
            int counter;
            counter = this.rotation;

            for (int i = 0; i < this.proximity.size; ++i) {
                Building other = this.proximity.get((i + counter) % this.proximity.size);
                if (set) {
                    this.rotation = (byte) ((this.rotation + 1) % this.proximity.size);
                }

                if ((other.tile != from || from.block() != Blocks.overflowGate) && other.acceptItem(this, item)) {
                    if (this.acceptSide(other)) return other;
                }
            }

            return null;

        }

        private boolean acceptSide(Building other) {

            if (left && other.x < this.x) return true;
            if (right && other.x > this.x) return true;
            if (down && other.y < this.y) return true;
            if (up && other.y > this.y) return true;
            return false;
        }

        @Override
        public Object config() {
            return Strings.format("@ @ @ @", up, down, left, right);
        }
        private void updateConfig(){
            this.block.lastConfig=config();
        }
        @Override
        public void write(Writes write) {
            super.write(write);
            write.str(config() + "");
        }

        @Override
        public void playerPlaced(Object config) {
            if (block.lastConfig==null){
                block.lastConfig="false false false false";
            }
            if (config==null)config=block.lastConfig;
            handleString(config);
        }

        @Override
        public void handleString(Object value) {
            try {
                String[] bools = (value+"").split(" ");
                if (bools.length == 4) {
                    up = Boolean.parseBoolean(bools[0]);
                    down = Boolean.parseBoolean(bools[1]);
                    left = Boolean.parseBoolean(bools[2]);
                    right = Boolean.parseBoolean(bools[3]);
                }
            } catch (Exception exception) {

            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            handleString(read.str());
        }
    }
}
