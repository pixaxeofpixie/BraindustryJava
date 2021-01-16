package mindustryAddition.world.blocks;

import arc.struct.Seq;
import mindustry.gen.Building;
import mindustry.gen.Buildingc;

public interface BuildingTaskQueue extends Buildingc {
    Seq<Runnable> updateTaskQueue=new Seq<>();
    default void addTast(Runnable runnable){
        updateTaskQueue.add(runnable);
    }
    public default void runUpdateTaskQueue(){

        updateTaskQueue.each(Runnable::run);
        updateTaskQueue.clear();
    }
}
