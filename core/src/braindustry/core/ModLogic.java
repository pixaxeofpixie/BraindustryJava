package braindustry.core;

import arc.ApplicationListener;
import arc.Events;
import arc.struct.Seq;
import braindustry.content.ModBullets;
import braindustry.content.ModUnitTypes;
import braindustry.type.ModUnitType;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;

public class ModLogic implements ApplicationListener {
    public ModLogic() {
        Events.on(EventType.WorldLoadEvent.class, e -> {
            Groups.build.each(this::removeUnitPowerGenerators);
        });
        Events.on(EventType.UnitDestroyEvent.class, (e) -> {
            Unit unit = e.unit;
            if (!(unit.type instanceof ModUnitType))return;
            ModUnitType type= (ModUnitType) unit.type;
            if (type.hasAfterDeathLaser) {

                float anglePart = 360f / (float)type.afterDeathLaserCount;
                for (float i = 0; i < 360f / anglePart; i++) {
//                    print("i: @,anglePart: @",i,anglePart);
                    Call.createBullet(ModBullets.deathLaser, unit.team, unit.x, unit.y, (360f + unit.rotation + 90f + anglePart / 2f + anglePart * i) % 360f, 1200f, 1f, 30f);
                }
            }
            if (type.dropItems.length>0){
                for (Team team : Team.all) {
                    if (!team.active() || team.cores().isEmpty())continue;
                    for (ItemStack stack : type.dropItems) {
                        team.core().items.add(stack.item,stack.amount);
                    }
                }
            }
        });
    }

    protected void removeUnitPowerGenerators(Building build) {
        if (build != null && build.power != null){
            int[] ints = build.power.links.toArray();
            Seq<Integer> seq=new Seq<>();
            for (int anInt : ints) {
                seq.add(anInt);
            }
            seq.select(i->i<0).each(integer -> {
                build.power.links.removeValue(integer);
            });
        }
    }
}
