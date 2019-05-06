package nl.kingdev.eml.event.blueprint;

import blueprints.Blueprint;
import nl.kingdev.eml.eventapi.events.Event;

public class EventLoadBlueprint implements Event {
    private Blueprint blueprint;

    public EventLoadBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
    }
}
