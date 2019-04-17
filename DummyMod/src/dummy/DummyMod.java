package dummy;

import nl.kingdev.eml.event.PreInitEvent;
import nl.kingdev.eml.eventapi.EventTarget;
import nl.kingdev.eml.mod.EMLModID;
import nl.kingdev.eml.mod.EMLModInfo;
import nl.kingdev.eml.mod.EMLModInstance;
import nl.kingdev.eml.mod.ModInfo;

@EMLModID("dummymod")
public class DummyMod {

    @EMLModInstance
    public static DummyMod instance;

    @EMLModInfo
    public ModInfo modInfo;


    @EventTarget
    public void preInit(PreInitEvent e) {
        System.out.println("DummyMod preInit "+modInfo.modid + " v:"+modInfo.version);
    }
}
