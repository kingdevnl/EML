package nl.kingdev.eml.event.render;

import guis.GuiTexture;
import nl.kingdev.eml.eventapi.events.Event;

public class EventRenderGUI implements Event {
    private GuiTexture guiTexture;
    public EventRenderGUI(GuiTexture gui) {
        this.guiTexture = gui;
    }

    public GuiTexture getGuiTexture() {
        return guiTexture;
    }
}
