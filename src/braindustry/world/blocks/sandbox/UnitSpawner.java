package braindustry.world.blocks.sandbox;

import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.Tile;

public class UnitSpawner extends Block {
    public UnitSpawner(String name) {
        super(name);
        this.update=true;
        this.configurable=true;
    }
    public class UnitSpawnerBuild extends Building{
        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            UnitSpawnerBuild build=(UnitSpawnerBuild)super.init(tile, team, shouldAdd, rotation);
//            build.
            return build;
        }

        @Override
        public boolean interactable(Team team) {
            return true;
        }
    }
}
