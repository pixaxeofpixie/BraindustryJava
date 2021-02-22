package braindustry.entities;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.math.geom.Geometry;
import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.gen.StealthUnitc;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.game.Teams;
import mindustry.gen.*;
import mindustry.type.UnitType;
import mindustry.world.Tile;

import java.util.Iterator;

public class ModUnits extends Units {
    private static final Rect hitrect = new Rect();
    private static Unit result;
    private static float cdist;
    private static boolean boolResult;

    public ModUnits() {
    }

    public static void unitCapDeath(Unit unit) {
        if (unit != null) {
            unit.dead = true;
            Fx.unitCapKill.at(unit);
            Core.app.post(() -> {
                Call.unitDestroy(unit.id);
            });
        }

    }

    public static void unitDeath(int uid) {
        Unit unit = (Unit)Groups.unit.getByID(uid);
        if (Vars.netClient != null) {
            Vars.netClient.addRemovedEntity(uid);
        }

        if (unit != null) {
            unit.killed();
        }

    }

    public static void unitDestroy(int uid) {
        Unit unit = (Unit)Groups.unit.getByID(uid);
        if (Vars.netClient != null) {
            Vars.netClient.addRemovedEntity(uid);
        }

        if (unit != null) {
            unit.destroy();
        }

    }

    public static void unitDespawn(Unit unit) {
        Fx.unitDespawn.at(unit.x, unit.y, 0.0F, unit);
        unit.remove();
    }

    public static boolean canCreate(Team team, UnitType type) {
        return team.data().countType(type) < getCap(team);
    }

    public static int getCap(Team team) {
        return (team != Vars.state.rules.waveTeam || Vars.state.rules.pvp) && (!Vars.state.isCampaign() || team != Vars.state.rules.waveTeam) ? Vars.state.rules.unitCap + Vars.indexer.getExtraUnits(team) : 2147483647;
    }

    public static boolean canInteract(Player player, Building tile) {
        return player == null || tile == null || tile.interactable(player.team());
    }

    public static boolean invalidateTarget(Posc target, Team team, float x, float y, float range) {
        return target == null || range != 3.4028235E38F && !target.within(x, y, range) || target instanceof Teamc && ((Teamc)target).team() == team || target instanceof Healthc && !((Healthc)target).isValid();
    }

    public static boolean invalidateTarget(Posc target, Team team, float x, float y) {
        return invalidateTarget(target, team, x, y, 3.4028235E38F);
    }

    public static boolean invalidateTarget(Teamc target, Unit targeter, float range) {
        return invalidateTarget(target, targeter.team(), targeter.x(), targeter.y(), range);
    }

    public static boolean anyEntities(Tile tile, boolean ground) {
        float size = (float)(tile.block().size * 8);
        return anyEntities(tile.drawx() - size / 2.0F, tile.drawy() - size / 2.0F, size, size, ground);
    }

    public static boolean anyEntities(Tile tile) {
        return anyEntities(tile, true);
    }

    public static boolean anyEntities(float x, float y, float width, float height) {
        return anyEntities(x, y, width, height, true);
    }

    public static boolean anyEntities(float x, float y, float width, float height, boolean ground) {
        boolResult = false;
        nearby(x, y, width, height, (unit) -> {
            if (!boolResult) {
                if ((unit.isGrounded() && !unit.type.hovering) == ground) {
                    unit.hitbox(hitrect);
                    if (hitrect.overlaps(x, y, width, height)) {
                        boolResult = true;
                    }
                }

            }
        });
        return boolResult;
    }

    public static Building findDamagedTile(Team team, float x, float y) {
        return Geometry.findClosest(x, y, Vars.indexer.getDamaged(team));
    }

    public static Building findAllyTile(Team team, float x, float y, float range, Boolf<Building> pred) {
        return Vars.indexer.findTile(team, x, y, range, pred);
    }

    public static Building findEnemyTile(Team team, float x, float y, float range, Boolf<Building> pred) {
        return team == Team.derelict ? null : Vars.indexer.findEnemyTile(team, x, y, range, pred);
    }

    public static Teamc closestTarget(Team team, float x, float y, float range) {
        return closestTarget(team, x, y, range, Healthc::isValid);
    }

    public static Teamc closestTarget(Team team, float x, float y, float range, Boolf<Unit> unitPred) {
        return closestTarget(team, x, y, range, unitPred, (t) -> {
            return true;
        });
    }

    public static Teamc closestTarget(Team team, float x, float y, float range, Boolf<Unit> unitPred, Boolf<Building> tilePred) {
        if (team == Team.derelict) {
            return null;
        } else {
            Unit unit = closestEnemy(team, x, y, range, unitPred);
            return (unit != null ? unit : findEnemyTile(team, x, y, range, tilePred));
        }
    }

    public static Teamc bestTarget(Team team, float x, float y, float range, Boolf<Unit> unitPred, Boolf<Building> tilePred, Units.Sortf sort) {
        if (team == Team.derelict) {
            return null;
        } else {
            Unit unit = bestEnemy(team, x, y, range, unitPred, sort);
            return (unit != null ? unit : findEnemyTile(team, x, y, range, tilePred));
        }
    }

    public static Unit closestEnemy(Team team, float x, float y, float range, Boolf<Unit> predicate) {
        if (team == Team.derelict) {
            return null;
        } else {
            result = null;
            cdist = 0.0F;
            nearbyEnemies(team, x - range, y - range, range * 2.0F, range * 2.0F, (e) -> {
                if (!e.dead() && predicate.get(e) && e.team != Team.derelict) {
                    float dst2 = e.dst2(x, y);
                    if (dst2 < range * range && (result == null || dst2 < cdist)) {
                        result = e;
                        cdist = dst2;
                    }

                }
            });
            return result;
        }
    }

    public static Unit bestEnemy(Team team, float x, float y, float range, Boolf<Unit> predicate, Units.Sortf sort) {
        if (team == Team.derelict) {
            return null;
        } else {
            result = null;
            cdist = 0.0F;
            nearbyEnemies(team, x - range, y - range, range * 2.0F, range * 2.0F, (e) -> {
                if (!e.dead() && predicate.get(e) && e.within(x, y, range)) {
                    float cost = sort.cost(e, x, y);
                    if (result == null || cost < cdist) {
                        result = e;
                        cdist = cost;
                    }

                }
            });
            return result;
        }
    }

    public static Unit closest(Team team, float x, float y, Boolf<Unit> predicate) {
        result = null;
        cdist = 0.0F;
        Iterator<Unit> var4 = Groups.unit.iterator();

        while(true) {
            Unit e;
            float dist;
            do {
                do {
                    do {
                        if (!var4.hasNext()) {
                            return result;
                        }

                        e = var4.next();
                    } while(!predicate.get(e));
                } while(e.team() != team);

                dist = e.dst2(x, y);
            } while(result != null && dist >= cdist);

            result = e;
            cdist = dist;
        }
    }

    public static Unit closest(Team team, float x, float y, float range, Boolf<Unit> predicate) {
        result = null;
        cdist = 0.0F;
        nearby(team, x, y, range, (e) -> {
            if (predicate.get(e)) {
                float dist = e.dst2(x, y);
                if (result == null || dist < cdist) {
                    result = e;
                    cdist = dist;
                }

            }
        });
        return result;
    }

    public static Unit closestOverlap(Team team, float x, float y, float range, Boolf<Unit> predicate) {
        result = null;
        cdist = 0.0F;
        nearby(team, x - range, y - range, range * 2.0F, range * 2.0F, (e) -> {
            if (predicate.get(e)) {
                float dist = e.dst2(x, y);
                if (result == null || dist < cdist) {
                    result = e;
                    cdist = dist;
                }

            }
        });
        return result;
    }

    public static void nearby(Team team, float x, float y, float width, float height, Cons<Unit> cons) {
        QuadTree<Unit> tree=team.data().tree();
        Seq<Unit> stealthUnits=new Seq<>();
        Groups.all.each((entityc -> {
            if (entityc instanceof Unit && entityc instanceof StealthUnitc && ((StealthUnitc)entityc).inStealth() && ((Unit) entityc).team()==team){
                stealthUnits.add((Unit)entityc);
            }
        }));
        stealthUnits.each(tree::insert);
        tree.intersect(x, y, width, height, cons);
        stealthUnits.each(tree::remove);
    }

    public static void nearby(Team team, float x, float y, float radius, Cons<Unit> cons) {
        nearby(team, x - radius, y - radius, radius * 2.0F, radius * 2.0F, (unit) -> {
            if (unit.within(x, y, radius)) {
                cons.get(unit);
            }

        });
    }

    public static void nearby(float x, float y, float width, float height, Cons<Unit> cons) {
        Groups.unit.intersect(x, y, width, height, cons);
    }

    public static void nearby(Rect rect, Cons<Unit> cons) {
        nearby(rect.x, rect.y, rect.width, rect.height, cons);
    }

    public static void nearbyEnemies(Team team, float x, float y, float width, float height, Cons<Unit> cons) {
        Seq<Teams.TeamData> data = Vars.state.teams.present;

        for(int i = 0; i < data.size; ++i) {
            if (((Teams.TeamData[])data.items)[i].team != team) {
                nearby(((Teams.TeamData[])data.items)[i].team, x, y, width, height, cons);
            }
        }

    }

    public static void nearbyEnemies(Team team, Rect rect, Cons<Unit> cons) {
        nearbyEnemies(team, rect.x, rect.y, rect.width, rect.height, cons);
    }

    public interface Sortf {
        float cost(Unit var1, float var2, float var3);
    }
}
