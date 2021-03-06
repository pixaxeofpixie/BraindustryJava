package braindustry.graphics;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.gl.Shader;
import mindustry.graphics.Shaders;

import static ModVars.modVars.floorRenderer;
import static mindustry.Vars.renderer;

public enum ModCacheLayer{
    water{
        @Override
        public void begin(){
            beginShader();
        }

        @Override
        public void end(){
            endShader(Shaders.water);
        }
    },
    mud{
        @Override
        public void begin(){
            beginShader();
        }

        @Override
        public void end(){
            endShader(Shaders.mud);
        }
    },
    tar{
        @Override
        public void begin(){
            beginShader();
        }

        @Override
        public void end(){
            endShader(Shaders.tar);
        }
    },
    slag{
        @Override
        public void begin(){
            beginShader();
        }

        @Override
        public void end(){
            endShader(Shaders.slag);
        }
    },
    space{
        @Override
        public void begin(){
            beginShader();
        }

        @Override
        public void end(){
            endShader(Shaders.space);
        }
    },
    normal,
    walls;

    public static final ModCacheLayer[] all = values();

    public void begin(){

    }

    public void end(){

    }

    void beginShader(){
        if(!Core.settings.getBool("animatedwater")) return;

        floorRenderer.endc();
        renderer.effectBuffer.begin();
        Core.graphics.clear(Color.clear);
        floorRenderer.beginc();
    }

    void endShader(Shader shader){
        if(!Core.settings.getBool("animatedwater")) return;

        floorRenderer.endc();
        renderer.effectBuffer.end();

        renderer.effectBuffer.blit(shader);

        floorRenderer.beginc();
    }
}
