package braindustry.content;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;

import static braindustry.modVars.modFunc.addResearch;

public class ModTechTree implements ContentList {
    static ObjectMap<UnlockableContent, TechTree.TechNode> map = new ObjectMap();
    static TechTree.TechNode context = null;
    public static Seq<TechTree.TechNode> all;
    public static TechTree.TechNode root;
    public ModTechTree(){
        TechTree.class.getClass();
    }
    @Override
    public void load() {
        setup();
        node(Blocks.additiveReconstructor,()->{
            node(ModBlocks.hyperAdditiveReconstructor,()->{
                node(Blocks.exponentialReconstructor, ()->{
                    node(ModBlocks.hyperExponentialReconstructor);
                });
            });

        });
        addResearch(Items.titanium,ModItems.chromium);
        addResearch(Items.surgeAlloy,ModItems.exoticAlloy);
        addResearch(Items.graphite,ModItems.graphenite);
        addResearch(Items.thorium,ModItems.odinum);
        addResearch(ModItems.exoticAlloy,ModItems.phaseAlloy);
        addResearch(Items.plastanium,ModItems.plastic);
        //=======
        addResearch(Liquids.slag, ModLiquids.liquidGraphenite);
        addResearch(Liquids.slag, ModLiquids.magma);
        addResearch(Liquids.cryofluid, ModLiquids.thoriumRefrigerant);
        //=======
        addResearch(UnitTypes.poly, ModUnitTypes.armor);
        addResearch(ModUnitTypes.armor, ModUnitTypes.shield);
        addResearch(ModUnitTypes.shield, ModUnitTypes.chestplate);
        addResearch(ModUnitTypes.chestplate, ModUnitTypes.chainmail);
        addResearch(ModUnitTypes.chainmail, ModUnitTypes.broadsword);
        //
        addResearch(UnitTypes.risso, ModUnitTypes.venti);
        addResearch(ModUnitTypes.venti, ModUnitTypes.lyra);
        addResearch(ModUnitTypes.lyra, ModUnitTypes.tropsy);
        addResearch(ModUnitTypes.tropsy, ModUnitTypes.cenda);

        addResearch(Blocks.additiveReconstructor,ModBlocks.hyperAdditiveReconstructor);
        addResearch(Blocks.airFactory, ModBlocks.hyperAirFactory);
        addResearch(Blocks.exponentialReconstructor, ModBlocks.hyperExponentialReconstructor);
        addResearch(Blocks.groundFactory, ModBlocks.hyperGroundFactory);
        addResearch(Blocks.multiplicativeReconstructor, ModBlocks.hyperMultiplicativeReconstructor);
        addResearch(Blocks.navalFactory, ModBlocks.hyperNavalFactory);
        addResearch(Blocks.tetrativeReconstructor, ModBlocks.hyperTetrativeReconstructor);
        addResearch(Blocks.blastDrill, ModBlocks.geothermicDrill);
        addResearch(Blocks.surgeSmelter, ModBlocks.grapheniteForge);
        addResearch(ModBlocks.grapheniteForge, ModBlocks.grapheniteFluidizer);
        addResearch(ModBlocks.grapheniteForge, ModBlocks.chromiumForge);
        addResearch(ModBlocks.grapheniteForge, ModBlocks.exoticAlloySmelter);
        addResearch(ModBlocks.grapheniteForge, ModBlocks.grapheniteKiln);
        addResearch(Blocks.surgeSmelter, ModBlocks.hyperAlloySmelter);
        addResearch(Blocks.phaseWeaver, ModBlocks.hyperPhaseWeaver);
        addResearch(ModBlocks.chromiumForge, ModBlocks.magmaMixer);
        addResearch(ModBlocks.grapheniteForge, ModBlocks.odinumExtractor);
        addResearch(ModBlocks.exoticAlloySmelter, ModBlocks.plasticForge);
        addResearch(ModBlocks.plasticForge, ModBlocks.phaseAlloySmelter);
        addResearch(Blocks.blastDrill, ModBlocks.quarryDrill);
        addResearch(ModBlocks.grapheniteFluidizer, ModBlocks.refrigerantmixer);
        addResearch(Blocks.surgeWall, ModBlocks.exoticAlloyWall);
        addResearch(ModBlocks.exoticAlloyWall, ModBlocks.exoticAlloyWallLarge);
        addResearch(Blocks.titaniumWallLarge, ModBlocks.grapheniteWall);
        addResearch(ModBlocks.grapheniteWall, ModBlocks.grapheniteWallLarge);
        addResearch(ModBlocks.grapheniteWallLarge, ModBlocks.odinumWall);
        addResearch(ModBlocks.odinumWall, ModBlocks.odinumWallLarge);
        addResearch(Blocks.plastaniumWall, ModBlocks.plasticWall);
        addResearch(Blocks.plastaniumWall, ModBlocks.plasticWallLarge);
        addResearch(Blocks.armoredConveyor, ModBlocks.armoredPlastaniumConveyor);
        addResearch(Blocks.pulseConduit, ModBlocks.chromiumConduit);
        addResearch(Blocks.phaseConveyor, ModBlocks.phaseAlloyConveyor);
        addResearch(ModBlocks.armoredPlastaniumConveyor, ModBlocks.plasticConveyor);
        addResearch(ModBlocks.armoredPlastaniumConveyor, ModBlocks.surgeConveyor);
        addResearch(Blocks.largeSolarPanel, ModBlocks.grapheniteSolarCollectorLarge);
        addResearch(Blocks.differentialGenerator, ModBlocks.magmaGenerator);
        addResearch(ModBlocks.magmaGenerator, ModBlocks.differentialMagmaGenerator);
        addResearch(Blocks.thoriumReactor, ModBlocks.odinumReactor);
        addResearch(ModBlocks.grapheniteSolarCollectorLarge, ModBlocks.phaseAlloySolarPanel);
        addResearch(Blocks.surgeTower, ModBlocks.phaseTower);
        addResearch(Blocks.thoriumReactor, ModBlocks.refrigerantReactor);
        addResearch(Blocks.arc, ModBlocks.impulse);
        addResearch(ModBlocks.impulse, ModBlocks.synaps);
        addResearch(ModBlocks.synaps, ModBlocks.axon);
        addResearch(Blocks.fuse, ModBlocks.blaze);
        addResearch(ModBlocks.axon, ModBlocks.electron);
        addResearch(ModBlocks.synaps, ModBlocks.electron);
        addResearch(Blocks.segment, ModBlocks.fragment);
        addResearch(Blocks.lancer, ModBlocks.soul);
        addResearch(ModBlocks.soul, ModBlocks.mind);
        addResearch(Blocks.lancer, ModBlocks.neuron);
        addResearch(Blocks.parallax, ModBlocks.perlin);
        addResearch(Blocks.swarmer, ModBlocks.stinger);
        addResearch(Blocks.router, ModBlocks.smartRouter);
    }
    public static void setup() {
        context = null;
        map = new ObjectMap();
        all = new Seq();
    }

    static TechTree.TechNode node(UnlockableContent content, Runnable children) {
        return node(content, content.researchRequirements(), children);
    }

    static TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Runnable children) {
        return node(content, requirements, null, children);
    }

    static TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Runnable children) {
        TechTree.TechNode node = new TechTree.TechNode(context, content, requirements);
        if (objectives != null) {
            node.objectives.addAll(objectives);
        }

        TechTree.TechNode prev = context;
        context = node;
        children.run();
        context = prev;
        return node;
    }

    static TechTree.TechNode node(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children) {
        return node(content, content.researchRequirements(), objectives, children);
    }

    static TechTree.TechNode node(UnlockableContent block) {
        return node(block, () -> {
        });
    }

    static TechTree.TechNode nodeProduce(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children) {
        return node(content, content.researchRequirements(), objectives.and(new Objectives.Produce(content)), children);
    }

    static TechTree.TechNode nodeProduce(UnlockableContent content, Runnable children) {
        return nodeProduce(content, new Seq(), children);
    }

}
