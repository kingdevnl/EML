package dummy;

import blueprints.Blueprint;
import guis.GuiTexture;
import nl.kingdev.eml.event.EventPreInit;
import nl.kingdev.eml.event.render.EventRenderGUI;
import nl.kingdev.eml.event.blueprint.EventLoadBlueprint;
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
    public void preInit(EventPreInit e) {
        System.out.println("DummyMod preInit Event called"+modInfo.modid + " v:"+modInfo.version);
        System.out.println(instance.toString());

    }

    @EventTarget
    public void render(EventRenderGUI e) {
        GuiTexture guiTexture = e.getGuiTexture();

        System.out.println("DummyMod -> renderGUI"+ guiTexture);


    }

    @EventTarget
    public void onLoadBlueprint(EventLoadBlueprint e) {
        Blueprint blueprint = e.getBlueprint();


    }
}
