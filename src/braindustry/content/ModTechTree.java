package braindustry.content;

import arc.struct.Seq;
import ModVars.Classes.TechTreeManager;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.TechTree;
import mindustry.game.Objectives;
//import static mindustry.game.Objectives.*;

public class ModTechTree extends TechTreeManager {
    public TechTreeManager techTree = new TechTreeManager();
    boolean loaded = false;

    public ModTechTree() {
        TechTree.class.getClass();
    }

    public void load() {
        if (loaded) return;
        loaded = true;
        /*

         * */

        node(Liquids.slag, () -> {
            nodeProduce(ModLiquids.magma);
            nodeProduce(ModLiquids.thoriumRefrigerant);
        });
//        Liquids.cryofluid
        parentNode(Blocks.groundFactory, ModBlocks.hyperGroundFactory, () -> {
            node(ModUnitTypes.ibis, () -> {
                node(ModUnitTypes.aries, () -> {
                    node(ModUnitTypes.capra, () -> {
                        node(ModUnitTypes.lacerta, () -> {
                            node(ModUnitTypes.aquila);
                        });
                    });
                });
            });
        });
        parentNode(Blocks.airFactory, ModBlocks.hyperAirFactory, () -> {
            node(ModUnitTypes.armor, () -> {
                node(ModUnitTypes.shield, () -> {
                    node(ModUnitTypes.chestplate, () -> {
                        node(ModUnitTypes.chainmail, () -> {
                            node(ModUnitTypes.broadsword);
                        });
                    });
                });
            });
        });
        parentNode(Blocks.navalFactory, ModBlocks.hyperNavalFactory, () -> {
            node(ModUnitTypes.venti, () -> {
                node(ModUnitTypes.lyra, () -> {
                    node(ModUnitTypes.tropsy, () -> {
                        node(ModUnitTypes.cenda);
                    });
                });
            });
        });

        parentNode(Blocks.additiveReconstructor, ModBlocks.hyperAdditiveReconstructor);
        parentNode(Blocks.multiplicativeReconstructor, ModBlocks.hyperMultiplicativeReconstructor, Seq.with(new Objectives.Research(ModBlocks.hyperAdditiveReconstructor)));
        parentNode(Blocks.exponentialReconstructor, ModBlocks.hyperExponentialReconstructor, Seq.with(new Objectives.Research(ModBlocks.hyperMultiplicativeReconstructor)));
        parentNode(Blocks.tetrativeReconstructor, ModBlocks.hyperTetrativeReconstructor, Seq.with(new Objectives.Research(ModBlocks.hyperExponentialReconstructor)));
        parentNode(Items.titanium, ModItems.chromium);
        parentNode(Items.surgeAlloy, ModItems.exoticAlloy);
        parentNode(Items.graphite, ModItems.graphenite);
        parentNode(Items.thorium, ModItems.odinum);
        parentNode(ModItems.exoticAlloy, ModItems.phaseAlloy);
        parentNode(Items.plastanium, ModItems.plastic);
        //=======
        //========
        parentNode(Liquids.cryofluid, ModLiquids.thoriumRefrigerant);
        //=======

        node(Blocks.blastDrill, () -> {
            node(ModBlocks.geothermicDrill);
            node(ModBlocks.quarryDrill);
        });
        node(Blocks.surgeSmelter, () -> {
            node(ModBlocks.hyperAlloySmelter);
            node(ModBlocks.grapheniteForge, () -> {
                node(ModBlocks.grapheniteFluidizer,()->{
                    node(ModBlocks.refrigerantmixer);
                });
                node(ModBlocks.chromiumForge,()->{
                    node(ModBlocks.magmaMixer);
                });
                node(ModBlocks.exoticAlloySmelter);
                node(ModBlocks.grapheniteKiln);
                node(ModBlocks.odinumExtractor);
            });
        });
        parentNode(Blocks.phaseWeaver, ModBlocks.hyperPhaseWeaver);
        node(Blocks.surgeWall, ()->{
            node(ModBlocks.exoticAlloyWall,()->{
                node(ModBlocks.exoticAlloyWallLarge);
            });
        });
        //==
        node(Blocks.titaniumWallLarge, () -> {
            node(ModBlocks.grapheniteWall, () -> {
                node(ModBlocks.grapheniteWallLarge, () -> {
                    node(ModBlocks.odinumWall, () -> {
                        node(ModBlocks.odinumWallLarge);
                    });
                });
            });
        });
        //===
        node(Blocks.plastaniumWall, () -> {
            node(ModBlocks.plasticWall);
            node(ModBlocks.plasticWallLarge);
        });
        node(Blocks.armoredConveyor, () -> {
            node(ModBlocks.armoredPlastaniumConveyor, () -> {
                node(ModBlocks.plasticConveyor);
                node(ModBlocks.surgeConveyor);
            });
        });
        parentNode(Blocks.pulseConduit, ModBlocks.chromiumConduit);
        parentNode(Blocks.phaseConveyor, ModBlocks.phaseAlloyConveyor);
        node(Blocks.largeSolarPanel, () -> {
            node(ModBlocks.grapheniteSolarCollectorLarge, () -> {
                node(ModBlocks.phaseAlloySolarPanel);
            });
        });
        node(Blocks.differentialGenerator, ()->{
            node(ModBlocks.magmaGenerator,()->{
                node( ModBlocks.differentialMagmaGenerator);
            });
        });
        node(Blocks.thoriumReactor, () -> {
            node(ModBlocks.odinumReactor,()->{
                node(ModBlocks.materialReactor,()->{
                    node(ModBlocks.blackHoleReactor);
                });
            });
            node(ModBlocks.refrigerantReactor);
        });
        parentNode(Blocks.surgeTower, ModBlocks.phaseTower);
        node(Blocks.arc, () -> {
            node(ModBlocks.impulse, () -> {
                node(ModBlocks.synaps, () -> {
                    node(ModBlocks.axon, () -> {
                        node(ModBlocks.electron);
                    });
                });
            });
        });
        parentNode(Blocks.fuse, ModBlocks.blaze);
        parentNode(Blocks.segment, ModBlocks.fragment);
        node(Blocks.lancer, () -> {
            node(ModBlocks.soul, () -> {
                node(ModBlocks.mind);
            });
            node(ModBlocks.neuron);
        });
        parentNode(Blocks.parallax, ModBlocks.perlin);
        parentNode(Blocks.swarmer, ModBlocks.stinger);
        parentNode(Blocks.router, ModBlocks.smartRouter);
        //parentNode(Blocks.interplanetaryAccelerator, Osore);
    }
}


