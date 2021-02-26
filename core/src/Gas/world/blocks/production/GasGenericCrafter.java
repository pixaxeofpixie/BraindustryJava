package Gas.world.blocks.production;

import Gas.gen.GasBuilding;
import Gas.GasStack;
import Gas.world.GasBlock;
import Gas.world.draw.GasDrawBlock;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class GasGenericCrafter extends GasBlock {
    public ItemStack outputItem;
    public LiquidStack outputLiquid;
    public GasStack outputGas;

    public float craftTime = 80;
    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;

    public GasDrawBlock drawer = new GasDrawBlock();

    public GasGenericCrafter(String name){
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
    }

    @Override
    public void setStats(){
        super.setStats();
        aStats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds);

        if (outputItem != null) {
            aStats.add(Stat.output, outputItem);
        }

        if (outputLiquid != null) {
            aStats.add(Stat.output, outputLiquid.liquid, outputLiquid.amount * (60f / craftTime), true);
        }

        if (outputGas != null) {
            aStats.add(Stat.output, outputGas.gas, outputLiquid.amount * (60f / craftTime), true);
        }
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    @Override
    public void init(){
        outputsLiquid = outputLiquid != null;
        outputsGas = outputGas != null;
        super.init();
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.icons(this);
    }

    @Override
    public boolean outputsItems(){
        return outputItem != null;
    }

    public class GasGenericCrafterBuild extends GasBuilding {
        public float progress;
        public float totalProgress;
        public float warmup;

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public boolean shouldConsume(){
            if (outputItem != null && items.get(outputItem.item) >= itemCapacity) {
                return false;
            }

            return (outputLiquid == null || !(liquids.get(outputLiquid.liquid) >= liquidCapacity - 0.001f)) && enabled;
        }

        @Override
        public void updateTile(){
            if (consValid()) {
                progress += getProgressIncrease(craftTime);
                totalProgress += delta();
                warmup = Mathf.lerpDelta(warmup, 1f, 0.02f);

                if(Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(getX() + Mathf.range(size * 4f), getY() + Mathf.range(size * 4));
                }
            } else {
                warmup = Mathf.lerp(warmup, 0f, 0.02f);
            }

            if (progress >= 1f) {
                consume();

                if(outputItem != null){
                    for(int i = 0; i < outputItem.amount; i++){
                        offload(outputItem.item);
                    }
                }

                if(outputLiquid != null){
                    handleLiquid(this, outputLiquid.liquid, outputLiquid.amount);
                }

                if(outputGas != null){
                    handleGas(this, outputGas.gas, outputGas.amount);
                }

                craftEffect.at(x, y);
                progress = 0f;
            }

            if (outputItem != null && timer(timerDump, dumpTime)) {
                dump(outputItem.item);
            }

            if (outputLiquid != null) {
                dumpLiquid(outputLiquid.liquid);
            }

            if (outputLiquid != null) {
                dumpGas(outputGas.gas);
            }
        }

        @Override
        public int getMaximumAccepted(Item item){
            return itemCapacity;
        }

        @Override
        public boolean shouldAmbientSound(){
            return cons.valid();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
        }
    }
}
