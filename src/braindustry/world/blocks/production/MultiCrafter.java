package braindustry.world.blocks.production;

import braindustry.content.ModFx;
import arc.Core;
import arc.func.Func;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.consumers.ConsumeItemDynamic;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stats;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static braindustry.modVars.ModFunc.*;
import static braindustry.modVars.ModFunc.getInfoDialog;

public class MultiCrafter extends Block {
    public Seq<Recipe> recipes = new Seq<>();
    public Effect craftEffect;
    public Effect updateEffect;
    public int queueSize = 1;
    public final int timerDump;
    public float updateEffectChance;
    public TextureRegion[] itemsTexture;
    public boolean changeTexture = false;

    public MultiCrafter(String name) {
        super(name);
        timerDump = timers++;
        this.craftEffect = Fx.none;
        this.updateEffect = Fx.none;
        this.updateEffectChance = 0.04F;
        this.update = true;
        this.solid = true;
        this.hasItems = true;
        this.ambientSound = Sounds.machine;
        this.sync = true;
        this.ambientSoundVolume = 0.03F;
        this.flags = EnumSet.of(BlockFlag.factory);
        this.configurable = true;
        this.itemCapacity = 10;
        this.destructible = true;
        this.group = BlockGroup.none;
        this.config(Integer.class, (build, i) -> {
            MultyCrafterBuild tile = (MultyCrafterBuild) build;
            tile.currentRecipe = i >= 0 && i < recipes.size ? i : -1;
            Color color;
            if (tile.currentRecipe != -1) {
                color = new Color(Color.white).lerp(Color.yellow, 1.0f);
            } else {
                color = new Color(Color.black).lerp(Color.white, 0.0f);
            }
            ModFx.changeCraft.at(tile.x, tile.y, tile.block.size*1.1f, color,new Color(Color.black).lerp(Color.white, 8.0f));
            tile.progress = 0.0F;
        });
        this.consumes.add(new ConsumeItemDynamic(e -> {
            MultyCrafterBuild build = (MultyCrafterBuild) e;
            ItemStack[] result = build.currentRecipe == -1 ? ItemStack.empty : this.recipes.get(build.currentRecipe).requirements;
            return build.currentRecipe == -1 ? ItemStack.empty : this.recipes.get(build.currentRecipe).requirements;
        }));
    }

    public void init() {
        super.init();
        if (changeTexture) {
            itemsTexture = new TextureRegion[this.recipes.size];
            for (int i = 0; i < itemsTexture.length; i++) {
                String itemName = this.recipes.get(i).outputItem.item.name;
                if (itemName.startsWith(getFullName(""))) itemName = itemName.split(getFullName(""), 2)[1];
//                print("load: @",this.name+"-"+itemName);
                itemsTexture[i] = Core.atlas.find(this.name + "-" + itemName, this.region);
            }
        }
        AtomicInteger minNeedCapacity = new AtomicInteger();
        recipes.each((recipe -> {
            Seq.with(recipe.requirements).each((itemStack -> {
                minNeedCapacity.addAndGet(itemStack.amount);
            }));
        }));
        int minCapacity = minNeedCapacity.get();
        this.config(Item.class, (obj, item) -> {
            MultyCrafterBuild tile = (MultyCrafterBuild) obj;
            tile.currentRecipe = this.recipes.indexOf((p) -> {
                return p.outputItem.item == item;
            });
            tile.resetProgress();
        });
        if (minCapacity > this.itemCapacity) {
            this.itemCapacity = minCapacity;
            if (this.stats.intialized) {
                this.stats = new Stats();
                this.checkStats();
            }
        }
    }

    public void setBars() {
        this.bars.add("health", (entity) -> {
            Color var10003 = Pal.health;
            Objects.requireNonNull(entity);
            return (new Bar("stat.health", var10003, entity::healthf)).blink(Color.white);
        });
        if (this.hasLiquids) {
            Func<Building, Liquid> current;
            if (this.consumes.has(ConsumeType.liquid) && this.consumes.get(ConsumeType.liquid) instanceof ConsumeLiquid) {
                Liquid liquid = ((ConsumeLiquid) this.consumes.get(ConsumeType.liquid)).liquid;
                current = (entity) -> {
                    return liquid;
                };
            } else {
                current = (entity) -> {
                    return entity.liquids == null ? Liquids.water : entity.liquids.current();
                };
            }

            this.bars.add("liquid", (entity) -> {
                return new Bar(() -> {
                    return entity.liquids.get((Liquid) current.get(entity)) <= 0.001F ? Core.bundle.get("bar.liquid") : ((Liquid) current.get(entity)).localizedName;
                }, () -> {
                    return ((Liquid) current.get(entity)).barColor();
                }, () -> {
                    return entity != null && entity.liquids != null ? entity.liquids.get((Liquid) current.get(entity)) / this.liquidCapacity : 0.0F;
                });
            });
        }

        if (this.hasPower && this.consumes.hasPower()) {
            ConsumePower cons = this.consumes.getPower();
            boolean buffered = cons.buffered;
            float capacity = cons.capacity;
            this.bars.add("power", (entity) -> {
                return new Bar(() -> {
                    return buffered ? Core.bundle.format("bar.poweramount", new Object[]{Float.isNaN(entity.power.status * capacity) ? "<ERROR>" : (int) (entity.power.status * capacity)}) : Core.bundle.get("bar.power");
                }, () -> {
                    return Pal.powerBar;
                }, () -> {
                    return Mathf.zero(cons.requestedPower(entity)) && entity.power.graph.getPowerProduced() + entity.power.graph.getBatteryStored() > 0.0F ? 1.0F : entity.power.status;
                });
            });
        }

        if (this.hasItems && this.configurable) {

            this.bars.add("items", (entity) -> {
                return new Bar(() -> {
                    return Core.bundle.format("bar.items", new Object[]{entity.items.total()});
                }, () -> {
                    return Pal.items;
                }, () -> {
                    return (float) entity.items.total() / (float) this.itemCapacity;
                });
            });
        }

    }

    public class Recipe {
        public ItemStack outputItem;
        public ItemStack[] requirements;

        //        public TextureRegion textureRegion;
        public float craftTime;

        public Recipe(ItemStack outputItem, ItemStack[] requirements, float craftTime) {
            this.outputItem = outputItem;
            this.requirements = requirements;
//            this.textureRegion = textureRegion;
            this.craftTime = craftTime;
        }
    }

    public class MultyCrafterBuild extends Building {
        public int currentRecipe = -1;
        public MultiCrafter block;
        public float progress;
        public float totalProgress;
        public MultyCrafterBuild() {
            this.block = MultiCrafter.this;
        }

        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            return super.init(tile, team, shouldAdd, rotation);
        }

        public void drawStatus() {
            if (this.currentRecipe==-1)return;
            if (!MultiCrafter.this.changeTexture || (MultiCrafter.this.size >= 2)) {
                super.drawStatus();
            } else {
                float brcx = this.tile.drawx() + (float) (this.block.size * 8) / 2.0F - 2.0F;
                float brcy = this.tile.drawy() - (float) (this.block.size * 8) / 2.0F + 2.0F;
                Draw.z(71.0F);
                Draw.color(Pal.gray);
                Fill.square(brcx, brcy, 1.25f, 45.0F);
                Draw.color(this.cons.status().color);
                Fill.square(brcx, brcy, 0.75f, 45.0F);
                Draw.color();
            }

        }

        public int getMaximumAccepted(Item item) {
            ItemStack itemStack = Seq.with(getCurrentRecipe().requirements).find((i) -> i.item == item);
            return itemStack == null ? 0 : itemStack.amount;
        }

        public void draw() {
            if (!MultiCrafter.this.changeTexture || this.currentRecipe == -1) {
                Draw.rect(this.block.region, this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
            } else {
                Draw.rect(MultiCrafter.this.itemsTexture[currentRecipe], this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
            }
        }

        public void configure() {

        }

        public void buildConfiguration(Table table) {
            Seq<Item> recipes = Seq.with(MultiCrafter.this.recipes).map((u) -> {
                return u.outputItem.item;
            }).filter((u) -> {
                return u.unlockedNow();
            });
            if (recipes.any()) {
                ItemSelection.buildTable(table, recipes, () -> {
                    return this.currentRecipe == -1 ? null : MultiCrafter.this.recipes.get(this.currentRecipe).outputItem.item;
                }, (item) -> {
                    this.configure(MultiCrafter.this.recipes.indexOf((u) -> {
                        return u.outputItem.item == item;
                    }));
                });
            } else {
                table.table(Styles.black3, (t) -> {
                    t.add("@none").color(Color.lightGray);
                });
            }
//            table.button(this.consValid()+"",()->{});
            if (true) return;
            table.button("debug", () -> {
                String[] lines = {
                        Strings.format("if1: @", this.consValid()),
                        Strings.format("if2: @", (getCurrentRecipe().outputItem != null
                                && this.items.get(getCurrentRecipe().outputItem.item) >= this.block.itemCapacity)),
                        Strings.format("can add copper: @", this.acceptItem(this, Items.copper))
                };
                getInfoDialog("", "Debug window", Strings.join("\n", lines), Color.green.add(Color.black)).show();
            });
        }

        public boolean acceptItem(Building source, Item item) {
            AtomicInteger itemCount = new AtomicInteger();
            Seq.with(getCurrentRecipe().requirements).select((itemStack) -> {
                return itemStack.item == item;
            }).each((itemStack -> {
                itemCount.addAndGet(itemStack.amount);
            }));
            int count = itemCount.get();
            return count > 0 && this.items.get(item) < this.getMaximumAccepted(item);
        }

        public boolean shouldConsume() {
            if (getCurrentRecipe().outputItem != null && this.items.get(getCurrentRecipe().outputItem.item) >= MultiCrafter.this.itemCapacity) {
                return false;
            }
            return true;
        }

        protected MultiCrafter.Recipe getCurrentRecipe() {
            if (currentRecipe == -1) return new Recipe(null, ItemStack.empty, -1);
            return MultiCrafter.this.recipes.get(currentRecipe);
        }

        public boolean canCraft() {
            int req = 0;
            int has = 0;
            Seq<ItemStack> requirements = Seq.with(this.getCurrentRecipe().requirements);
            AtomicInteger count = new AtomicInteger(0);
            requirements.each((i) -> {
                count.addAndGet(i.amount + i.item.id);
            });
            req = count.get();
            count.set(0);
            this.items.each((item, c) -> {
                count.addAndGet(item.id + c);
            });
            has = count.get();
//            this.cons

            return this.consValid() && req <= has;
        }

        public void updateTile() {
            if (this.currentRecipe < 0 || this.currentRecipe >= this.block.recipes.size) {
                this.currentRecipe = -1;
                this.progress = 0;
            }


            if (this.canCraft() && this.currentRecipe != -1) {
                this.progress += this.getProgressIncrease(this.block.recipes.get(currentRecipe).craftTime);
                this.totalProgress += this.delta();
                /*if (Mathf.chanceDelta((double)this.block.updateEffectChance)) {
                    GenericCrafter.this.updateEffect.at(this.getX() + Mathf.range((float)GenericCrafter.this.size * 4.0F), this.getY() + (float)Mathf.range(GenericCrafter.this.size * 4));
                }*/
            }

            if (this.progress >= 1.0F && this.currentRecipe != -1) {
                this.consume();
                if (getCurrentRecipe().outputItem != null) {
                    for (int i = 0; i < getCurrentRecipe().outputItem.amount; ++i) {
                        this.offload(getCurrentRecipe().outputItem.item);
                    }
                }
/*
                if (GenericCrafter.this.outputLiquid != null) {
                    this.handleLiquid(this, GenericCrafter.this.outputLiquid.liquid, GenericCrafter.this.outputLiquid.amount);
                }*/

                this.block.craftEffect.at(this.x, this.y);
                this.progress = 0.0F;
            }
            if (getCurrentRecipe().outputItem != null && this.timer(MultyCrafterBuild.this.block.timerDump, 5.0F)) {
                this.dump(getCurrentRecipe().outputItem.item);
            }




            /*if (GenericCrafter.this.outputLiquid != null) {
                this.dumpLiquid(GenericCrafter.this.outputLiquid.liquid);
            }*/

        }

        public Object config() {
            return this.currentRecipe;
        }

        public void write(Writes write) {
            super.write(write);
            write.i(this.currentRecipe);
        }

        public void playerPlaced(Object config) {
            if (MultiCrafter.this.lastConfig==null) MultiCrafter.this.lastConfig=-1;
            if (config == null) {
                if (!MultiCrafter.this.lastConfig.equals(-1)) this.configure(MultiCrafter.this.lastConfig);
            } else {
                this.configure(config);
            }
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            this.currentRecipe = read.i();
            if (this.currentRecipe < 0 || this.currentRecipe >= this.block.recipes.size) {
                this.currentRecipe = -1;
                this.progress = 0;
            }
        }

        public void resetProgress() {
            this.progress = 0f;
            this.totalProgress = 0f;
        }
    }
}