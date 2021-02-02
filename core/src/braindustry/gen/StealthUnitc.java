package braindustry.gen;

import mindustry.gen.*;

public interface StealthUnitc extends Boundedc , Rotc, Itemsc, Weaponsc, Physicsc, Flyingc, Posc, Statusc, Shieldc, Minerc, Builderc, Hitboxc, Velc, Healthc, Teamc, Drawc, Entityc, Syncc, Commanderc, Unitc {
    boolean healing();
    void healing(boolean healing);
    boolean mustHeal();

    boolean inStealth();
    void inStealth(boolean inStealth);

    void longPress(boolean longPress);
    boolean longPress();
    void updateStealthStatus();
    void drawAlpha();
    void removeStealth();
    void removeStealth(float time);
    void setStealth();
    void setStealth(float time);

    float stealthf();
}
