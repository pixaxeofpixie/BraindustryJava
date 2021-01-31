package braindustry.world.blocks.logic;

import mindustry.world.blocks.logic.SwitchBlock;

public class AdvancedSwitcher extends SwitchBlock {

    public AdvancedSwitcher(String name) {
        super(name);
    }
    public class AdvancedSwitcherBuild extends SwitchBuild{
        @Override
        public void update() {
                proximity.each(build->{
                    if(build instanceof AdvancedSwitcherBuild)return;
                    build.enabledControlTime(60);
                    build.enabled(enabled);
                });

        }
    }
}
