package controller.events;

import hochberger.utilities.eventbus.Event;
import model.SurfaceMap;

public class DemManipulationFinishedEvent implements Event {

    private final SurfaceMap map;

    public DemManipulationFinishedEvent(final SurfaceMap map) {
        super();
        this.map = map;
    }

    public SurfaceMap getSurfaceMap() {
        return this.map;
    }

    @Override
    public void performEvent() {
    }
}
