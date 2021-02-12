package braindustry.content;

import arc.struct.Seq;
import arc.util.Log;
import mindustry.ctype.ContentList;
import mindustry.type.StatusEffect;

public class ModStatusEffects implements ContentList {
    public static Seq<StatusEffect> speedMul;

    private static Seq<StatusEffect> StatusEffectSeq(int amount, float value, float startValue, String name) {
        Seq<StatusEffect> seq = new Seq<>();

        for (int i = 0; i < amount; i++) {
                float val = startValue + value * (i + 1.0f);
                String n = name + val;

            try {
                StatusEffect e = new StatusEffect(n) {
                    {
                        this.speedMultiplier = name.equals("speedMul") ? val : 1.0f;
                        this.damageMultiplier = name.equals("damageMul") ? val : 1.0f;
//                        this.armorMultiplier = name == "armorMul" ? val : 1.0f;
                        this.damage = name.equals("damage") ? val : 0.0f;
                    }
                };
                seq.add(e);
            } catch (Exception e) {
                Log.err(e);
                seq.add(new StatusEffect(n));
            }
        }
        return seq;
    }

    @Override
    public void load() {
        speedMul = StatusEffectSeq(30, 0.1f, 0.0f, "speedMul");
    }
}
