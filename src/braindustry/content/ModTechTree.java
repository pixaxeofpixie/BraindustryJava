package braindustry.content;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import braindustry.modVars.Classes.TechTreeManager;
import braindustry.modVars.modVars;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;

import static braindustry.modVars.modFunc.*;
//import static mindustry.game.Objectives.*;

public class ModTechTree  extends TechTreeManager{
    public ModTechTree(){
        TechTree.class.getClass();
    }
    public TechTreeManager techTree=new TechTreeManager();
    boolean loaded=false;
    public void load() {
        if (loaded)return;
        loaded=true;
        /*

        * */

        node(Liquids.slag,()->{
            nodeProduce(ModLiquids.magma);
            nodeProduce(ModLiquids.thoriumRefrigerant);
        });
//        Liquids.cryofluid
        parentNode(Blocks.groundFactory,ModBlocks.hyperGroundFactory,()->{
            node(ModUnitTypes.ibis,()->{
                node(ModUnitTypes.aries,()->{
                    node(ModUnitTypes.capra,()->{
                        node(ModUnitTypes.lacerta,()->{
                            node(ModUnitTypes.aquila,()->{

                            });
                        });
                    });
                });
            });
        });
        /**
         *  smartRouter откроется после изучения hyperProcessor, производства silicon и нужных ресурсов(как обычно) и будет находится после router
         *  --chromium будет находится после smartRouter и откроеться после изучения hyperProcessor, производства silicon и нужных ресурсов(если есть)
         *  ---у exoticAlloy и graphenite общий родитель chromium будут открыты после нужных ресурсов(если есть)
        **/
        parentNode(Items.titanium,ModItems.chromium);
        parentNode(Items.surgeAlloy,ModItems.exoticAlloy);
        parentNode(Items.graphite,ModItems.graphenite);
        parentNode(Items.thorium,ModItems.odinum);
        parentNode(ModItems.exoticAlloy,ModItems.phaseAlloy);
        parentNode(Items.plastanium,ModItems.plastic);
        //=======
        //========
        parentNode(Liquids.cryofluid, ModLiquids.thoriumRefrigerant);
        //=======
        parentNode(UnitTypes.poly, ModUnitTypes.armor);
        parentNode(ModUnitTypes.armor, ModUnitTypes.shield);
        parentNode(ModUnitTypes.shield, ModUnitTypes.chestplate);
        parentNode(ModUnitTypes.chestplate, ModUnitTypes.chainmail);
        parentNode(ModUnitTypes.chainmail, ModUnitTypes.broadsword);
        //
        parentNode(UnitTypes.risso, ModUnitTypes.venti);
        parentNode(ModUnitTypes.venti, ModUnitTypes.lyra);
        parentNode(ModUnitTypes.lyra, ModUnitTypes.tropsy);
        parentNode(ModUnitTypes.tropsy, ModUnitTypes.cenda);

        parentNode(Blocks.additiveReconstructor,ModBlocks.hyperAdditiveReconstructor);
        parentNode(Blocks.airFactory, ModBlocks.hyperAirFactory);
        parentNode(Blocks.exponentialReconstructor, ModBlocks.hyperExponentialReconstructor);
        parentNode(Blocks.groundFactory, ModBlocks.hyperGroundFactory);
        parentNode(Blocks.multiplicativeReconstructor, ModBlocks.hyperMultiplicativeReconstructor);
        parentNode(Blocks.navalFactory, ModBlocks.hyperNavalFactory);
        parentNode(Blocks.tetrativeReconstructor, ModBlocks.hyperTetrativeReconstructor);
        node(Blocks.blastDrill,()->{
            node(ModBlocks.geothermicDrill);
            node(ModBlocks.quarryDrill);
        });
        parentNode(Blocks.blastDrill, ModBlocks.geothermicDrill);
        parentNode(Blocks.blastDrill, ModBlocks.quarryDrill);
        node(Blocks.surgeSmelter,()->{
            node(ModBlocks.hyperAlloySmelter);
            node(ModBlocks.grapheniteForge,()->{
                node( ModBlocks.grapheniteFluidizer);
                node( ModBlocks.chromiumForge);
                node( ModBlocks.exoticAlloySmelter);
                node( ModBlocks.grapheniteKiln);
                node( ModBlocks.odinumExtractor);
            });
        });
        parentNode(Blocks.phaseWeaver, ModBlocks.hyperPhaseWeaver);
        parentNode(ModBlocks.chromiumForge, ModBlocks.magmaMixer);
        parentNode(ModBlocks.exoticAlloySmelter, ModBlocks.plasticForge);
        parentNode(ModBlocks.plasticForge, ModBlocks.phaseAlloySmelter);
        parentNode(ModBlocks.grapheniteFluidizer, ModBlocks.refrigerantmixer);
        parentNode(Blocks.surgeWall, ModBlocks.exoticAlloyWall);
        parentNode(ModBlocks.exoticAlloyWall, ModBlocks.exoticAlloyWallLarge);
        parentNode(Blocks.titaniumWallLarge, ModBlocks.grapheniteWall);
        parentNode(ModBlocks.grapheniteWall, ModBlocks.grapheniteWallLarge);
        parentNode(ModBlocks.grapheniteWallLarge, ModBlocks.odinumWall);
        parentNode(ModBlocks.odinumWall, ModBlocks.odinumWallLarge);
        parentNode(Blocks.plastaniumWall, ModBlocks.plasticWall);
        parentNode(Blocks.plastaniumWall, ModBlocks.plasticWallLarge);
        parentNode(Blocks.armoredConveyor, ModBlocks.armoredPlastaniumConveyor);
        parentNode(Blocks.pulseConduit, ModBlocks.chromiumConduit);
        parentNode(Blocks.phaseConveyor, ModBlocks.phaseAlloyConveyor);
        parentNode(ModBlocks.armoredPlastaniumConveyor, ModBlocks.plasticConveyor);
        parentNode(ModBlocks.armoredPlastaniumConveyor, ModBlocks.surgeConveyor);
        parentNode(Blocks.largeSolarPanel, ModBlocks.grapheniteSolarCollectorLarge);
        parentNode(Blocks.differentialGenerator, ModBlocks.magmaGenerator);
        parentNode(ModBlocks.magmaGenerator, ModBlocks.differentialMagmaGenerator);
        parentNode(Blocks.thoriumReactor, ModBlocks.odinumReactor);
        parentNode(ModBlocks.grapheniteSolarCollectorLarge, ModBlocks.phaseAlloySolarPanel);
        parentNode(Blocks.surgeTower, ModBlocks.phaseTower);
        parentNode(Blocks.thoriumReactor, ModBlocks.refrigerantReactor);
        parentNode(Blocks.arc, ModBlocks.impulse);
        parentNode(ModBlocks.impulse, ModBlocks.synaps);
        parentNode(ModBlocks.synaps, ModBlocks.axon);
        parentNode(Blocks.fuse, ModBlocks.blaze);
        parentNode(ModBlocks.axon, ModBlocks.electron);
        parentNode(ModBlocks.synaps, ModBlocks.electron);
        parentNode(Blocks.segment, ModBlocks.fragment);
        parentNode(Blocks.lancer, ModBlocks.soul);
        parentNode(ModBlocks.soul, ModBlocks.mind);
        parentNode(Blocks.lancer, ModBlocks.neuron);
        parentNode(Blocks.parallax, ModBlocks.perlin);
        parentNode(Blocks.swarmer, ModBlocks.stinger);
        parentNode(Blocks.router, ModBlocks.smartRouter);
    }
}
