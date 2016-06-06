package controller.events;

import hochberger.utilities.eventbus.Event;

public class ExportTerrainEvent implements Event {

    private final String filepath;

    public ExportTerrainEvent(final String filepath) {
        super();
        this.filepath = filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }

    @Override
    public void performEvent() {

    }
}
