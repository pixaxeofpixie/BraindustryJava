package braindustry.entities.Advanced;

import ModVars.modVars;
import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Structs;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.ai.formations.DistanceAssignmentStrategy;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationMember;
import mindustry.ai.formations.FormationPattern;
import mindustry.ai.types.FormationAI;
import mindustry.ai.types.LogicAI;
import mindustry.async.PhysicsProcess;
import mindustry.audio.SoundLoop;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.core.World;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.entities.*;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.*;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.InverseKinematics;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.io.TypeIO;
import mindustry.logic.LAccess;
import mindustry.type.*;
import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.blocks.storage.CoreBlock;
import mindustryAddition.versions.ModEntityc;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;

public class AdvancedLegsUnit extends LegsUnit implements ModEntityc {
    public static int classId = 42;
    public int classId() {
        return modVars.MOD_CONTENT_ID;
    }

    @Override
    public int modClassId() {
        return classId;
    }

    @Override
    public void update() {
        super.update();
        this.type.update(this);
    }

    @Override
    public String toString() {
        return "Advanced"+super.toString();
    }

    @Override
    public void draw() {
//        Draw.scl(this.healthf());
//        Lines.stroke();
        super.draw();
    }

}
