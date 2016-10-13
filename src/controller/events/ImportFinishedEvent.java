package controller.events;

import hochberger.utilities.eventbus.Event;
import model.SurfaceMap;

public class ImportFinishedEvent implements Event {

    private final SurfaceMap heightMap;

    public ImportFinishedEvent(final SurfaceMap heightMap) {
        super();
        this.heightMap = heightMap;
    }

    public SurfaceMap getHeightMap() {
        return this.heightMap;
    }

    @Override
    public void performEvent() {
        // TODO Auto-generated method stub
    }
}
