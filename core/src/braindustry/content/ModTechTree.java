package braindustry.content;

import Gas.content.GasBasicBlocks;
import arc.struct.Seq;
import ModVars.Classes.TechTreeManager;
import braindustry.content.Blocks.ModBlocks;
import braindustry.world.ModBlock;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.game.Objectives;
//import static mindustry.game.Objectives.*;

public class ModTechTree extends TechTreeManager implements ContentList {
    public TechTreeManager techTree = new TechTreeManager();
    boolean loaded = false;

    public ModTechTree() {
        TechTree.class.getClass();
    }

    public void load() {
        if (loaded) return;
        loaded = true;

         //osore sectors
        parentNode(SectorPresets.planetaryTerminal, Blocks.interplanetaryAccelerator, () -> {
            node(ModSectorPresets.spacePort, () -> {
                node(ModSectorPresets.FFOf, () -> {
                    node(ModSectorPresets.meltingPoint, () -> {
                        node(ModSectorPresets.magmaticElectrostation, () -> {
                            node(ModSectorPresets.polarRift, () -> {
                                node(ModSectorPresets.methaneLaboratories, () -> {
                                    node(ModSectorPresets.icyBeach, () -> {
                                        node(ModSectorPresets.ritual, () -> {
                                            //shinrin sectors
                                            node(ModSectorPresets.jungleExplorationComplex,  () -> {
                                                node(ModSectorPresets.emeraldSwamp,  () -> {
                                                    node(ModSectorPresets.deentForest,  () -> {
                                                        node(ModSectorPresets.icyDarkness,  () -> {
                                                            node(ModSectorPresets.azureLandscape);
                                                        });
                                                    });
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });

        node(Liquids.slag, () -> {
            nodeProduce(ModLiquids.magma);
            node(ModLiquids.liquidGraphenite, Seq.with(new Objectives.Research(ModItems.graphenite)));
        });

        node(Liquids.cryofluid, () -> {
            node(ModLiquids.thoriumRefrigerant, Seq.with(new Objectives.Research(ModBlocks.refrigerantmixer)));
        });

        parentNode(ModBlocks.hyperTetrativeReconstructor, ModBlocks.ultraReconstructor,Seq.with(new Objectives.Research(ModItems.chloroAlloy)), ()->{
            node(ModUnitTypes.griffon);
            node(ModUnitTypes.moray);
            node(ModUnitTypes.litix);
            node(ModUnitTypes.penumbra);
        });

        parentNode(Blocks.groundFactory, ModBlocks.hyperGroundFactory, () -> {
            node(ModUnitTypes.ibis, () -> {
                node(ModUnitTypes.aries, () -> {
                    node(ModUnitTypes.capra, () -> {
                        node(ModUnitTypes.lacerta,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
                            node(ModUnitTypes.aquila);
                        });
                    });
                });
            });
            node(ModUnitTypes.tyzen,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
                node(ModUnitTypes.kryox, () -> {
                    node(ModUnitTypes.intelix, () -> {
                        node(ModUnitTypes.nemesis, () -> {
                            node(ModUnitTypes.maverix);
                        });
                    });
                });
            });
        });
        parentNode(Blocks.airFactory, ModBlocks.hyperAirFactory,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
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
        parentNode(Blocks.navalFactory, ModBlocks.hyperNavalFactory,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
            node(ModUnitTypes.venti, () -> {
                node(ModUnitTypes.lyra, () -> {
                    node(ModUnitTypes.tropsy, () -> {
                        node(ModUnitTypes.cenda,()-> {
                            node(ModUnitTypes.vyvna);
                        });
                    });
                });
            });
        });
       /* node(SectorPresets.planetaryTerminal, () -> {
            node(ModSectorPresets.spacePort);
        });*/
        node(Blocks.blastDrill, () -> {
            node(ModBlocks.geothermicDrill);
            node(ModBlocks.quarryDrill);
        });
        node(Blocks.siliconSmelter, () -> {
            node(ModBlocks.grapheniteForge, () -> {
                node(ModBlocks.grapheniteFluidizer,()->{
                    node(ModBlocks.refrigerantmixer);
                    node(ModBlocks.chromiumForge,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)),()->{
                        node(ModBlocks.magmaMixer, ()->{
                            node(ModBlocks.methaneGasifier);
                            node(ModBlocks.methaneLiquifier);
                        });
                    });
                    node(ModBlocks.grapheniteKiln);
                    node(ModBlocks.odinumExtractor,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)),()-> {
                        node(ModBlocks.exoticAlloySmelter,()->{
                            node(ModBlocks.multiCrafter, ()->{
                                node(ModBlocks.largeMultiCrafter);
                            });
                            node(ModBlocks.hyperExoAlloySmelter);
                            node(ModBlocks.phaseAlloySmelter, ()->{
                                node(ModBlocks.hyperDenseCompositeSmelter);
                            });
                            node(ModBlocks.plasticForge);
                        });
                    });
                });
            });
        });
        node(Blocks.surgeWall, ()->{
            node(ModBlocks.exoticAlloyWall,()->{
                node(ModBlocks.exoticAlloyWallLarge,()->{
                    node(ModBlocks.chloroWall,()->{
                        node(ModBlocks.largeChloroWall);
                    });
                });
            });
        });
        node(Blocks.titaniumWallLarge, () -> {
            node(ModBlocks.grapheniteWall, () -> {
                node(ModBlocks.grapheniteWallLarge, () -> {
                    node(ModBlocks.odinumWall, () -> {
                        node(ModBlocks.odinumWallLarge);
                    });
                });
            });
        });
        node(Blocks.plastaniumWall, () -> {
            node(ModBlocks.plasticWall);
            node(ModBlocks.plasticWallLarge);
        });
        node(Blocks.armoredConveyor, () -> {
            node(ModBlocks.armoredPlastaniumConveyor, () -> {
                node(ModBlocks.plasticConveyor);
            });
        });
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
            node(ModBlocks.odinumReactor,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), ()->{
                node(ModBlocks.materialReactor,()->{
                    node(ModBlocks.blackHoleReactor);
                });
            });
            node(ModBlocks.refrigerantReactor);
        });
        parentNode(Blocks.surgeTower, ModBlocks.phaseTower);
        node(Blocks.arc, () -> {
            node(ModBlocks.impulse,  () -> {
                node(ModBlocks.synaps, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
                    node(ModBlocks.axon, () -> {
                        node(ModBlocks.electron, Seq.with(new Objectives.Research(ModBlocks.shinigami),
                        new Objectives.Research(ModSectorPresets.jungleExplorationComplex)));
                    });
                });
            });
        });
        parentNode(Blocks.fuse, ModBlocks.blaze, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
        node(Blocks.lancer, () -> {
            node(ModBlocks.soul,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.mind, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
                    node(ModBlocks.voidwave, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
                });
            });
            node(ModBlocks.neuron,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), ()->{
                node(ModBlocks.brain, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), ()->{
                    node(ModBlocks.shinigami, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), ()->{
                        node(ModBlocks.gem, Seq.with(new Objectives.Research(ModSectorPresets.jungleExplorationComplex)));
                    });
                });
            });
        });
        node(Blocks.differentialGenerator, ()->{
            node(ModBlocks.magmaGenerator, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)),  ()->{
                node(ModBlocks.differentialMagmaGenerator);
            });
            node(ModBlocks.methaneBurner, Seq.with(new Objectives.Research(ModSectorPresets.icyBeach)),  ()->{
                node(ModBlocks.hyperMethaneBurner);
            });
        });
        parentNode(Blocks.parallax, ModBlocks.perlin);
        parentNode(Blocks.swarmer, ModBlocks.stinger);
        parentNode(Blocks.router, ModBlocks.smartRouter);
        parentNode(Blocks.segment, ModBlocks.fragment);
        parentNode(Blocks.plastaniumConveyor, ModBlocks.plasticConveyor);
        parentNode(Blocks.armoredConveyor, ModBlocks.armoredPlastaniumConveyor);
        parentNode(Blocks.pulseConduit, ModBlocks.chromiumConduit);
        parentNode(Blocks.phaseConveyor, ModBlocks.phaseAlloyConveyor);
        parentNode(Blocks.additiveReconstructor, ModBlocks.hyperAdditiveReconstructor);
        parentNode(Blocks.multiplicativeReconstructor, ModBlocks.hyperMultiplicativeReconstructor, Seq.with(new Objectives.Research(ModBlocks.hyperAdditiveReconstructor)));
        parentNode(Blocks.exponentialReconstructor, ModBlocks.hyperExponentialReconstructor, Seq.with(new Objectives.Research(ModBlocks.hyperMultiplicativeReconstructor),
                new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(Blocks.tetrativeReconstructor, ModBlocks.hyperTetrativeReconstructor, Seq.with(new Objectives.Research(ModBlocks.hyperExponentialReconstructor),
                new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(Items.titanium, ModItems.chromium, Seq.with(new Objectives.Research(ModPlanets.osore)));
        parentNode(Items.surgeAlloy, ModItems.exoticAlloy, Seq.with(new Objectives.Research(ModPlanets.osore)));
        parentNode(Items.graphite, ModItems.graphenite);
        parentNode(Items.thorium, ModItems.odinum, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(ModItems.exoticAlloy, ModItems.phaseAlloy, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(Items.plastanium, ModItems.plastic, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(ModItems.phaseAlloy, ModItems.chloroAlloy, Seq.with(new Objectives.Research(ModSectorPresets.jungleExplorationComplex)));
        parentNode(Liquids.cryofluid, ModLiquids.thoriumRefrigerant, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(Blocks.phaseWeaver, ModBlocks.hyperPhaseWeaver);
        parentNode(Blocks.surgeSmelter, ModBlocks.hyperAlloySmelter);
        parentNode(ModBlocks.chromiumConduit, GasBasicBlocks.gasConduit, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)));
        parentNode(Blocks.liquidTank, ModBlocks.gasTank);
        parentNode(Blocks.salvo, ModBlocks.spark);
        parentNode(Blocks.liquidRouter, ModBlocks.gasRouter);
    }
}


