package braindustry.entities.ContainersForUnits;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Unitc;
import mindustry.world.Tile;

public class ModLightning {
    private static final Rand random = new Rand();
    private static final Rect rect = new Rect();
    private static final Seq<Unitc> entities = new Seq();
    private static final IntSet hit = new IntSet();
    private static final int maxChain = 8;
    private static final float hitRange = 30.0F;
    private static boolean bhit = false;
    private static int lastSeed = 0;

    public ModLightning() {
    }

    public static void create(Team team, Color color, float damage, float x, float y, float targetAngle, int length) {
        createLightningInternal((Bullet)null, lastSeed++, team, color, damage, x, y, targetAngle, length);
    }

    public static void create(Bullet bullet, Color color, float damage, float x, float y, float targetAngle, int length) {
        createLightningInternal(bullet, lastSeed++, bullet.team, color, damage, x, y, targetAngle, length);
    }

    private static void createLightningInternal(@Nullable Bullet hitter, int seed, Team team, Color color, float damage, float x, float y, float rotation, int length) {
        random.setSeed((long)seed);
        hit.clear();
        BulletType hitCreate = hitter != null && hitter.type.lightningType != null ? hitter.type.lightningType : Bullets.damageLightning;
        Seq<Vec2> lines = new Seq();
        bhit = false;

        for(int i = 0; i < length / 2; ++i) {
            hitCreate.create((Entityc)null, team, x, y, 0.0F, damage, 1.0F, 1.0F, hitter);
            lines.add(new Vec2(x + Mathf.range(3.0F), y + Mathf.range(3.0F)));
            if (lines.size > 1) {
                bhit = false;
                Vec2 from = (Vec2)lines.get(lines.size - 2);
                Vec2 to = (Vec2)lines.get(lines.size - 1);
                Vars.world.raycastEach(World.toTile(from.getX()), World.toTile(from.getY()), World.toTile(to.getX()), World.toTile(to.getY()), (wx, wy) -> {
                    Tile tile = Vars.world.tile(wx, wy);
                    if (tile != null && tile.block().insulated && tile.team() != team) {
                        bhit = true;
                        ((Vec2)lines.get(lines.size - 1)).set((float)(wx * 8), (float)(wy * 8));
                        return true;
                    } else {
                        return false;
                    }
                });
                if (bhit) {
                    break;
                }
            }

            rect.setSize(30.0F).setCenter(x, y);
            entities.clear();
            if (hit.size < 8) {
                Units.nearbyEnemies(team, rect, (u) -> {
                    if (!hit.contains(u.id()) && (hitter == null || u.checkTarget(hitter.type.collidesAir, hitter.type.collidesGround))) {
                        entities.add(u);
                    }

                });
            }

            Unitc furthest = (Unitc) Geometry.findFurthest(x, y, entities);
            if (furthest != null) {
                hit.add(furthest.id());
                x = furthest.x();
                y = furthest.y();
            } else {
                rotation += random.range(20.0F);
                x += Angles.trnsx(rotation, 15.0F);
                y += Angles.trnsy(rotation, 15.0F);
            }
        }

        Fx.lightning.at(x, y, rotation, color, lines);
    }
}
