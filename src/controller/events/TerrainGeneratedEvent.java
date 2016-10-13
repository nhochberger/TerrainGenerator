package controller.events;

import hochberger.utilities.eventbus.Event;
import model.SurfaceMap;

public class TerrainGeneratedEvent implements Event {

    private final SurfaceMap heightMap;

    public TerrainGeneratedEvent(final SurfaceMap heightMap) {
        super();
        this.heightMap = heightMap;
    }

    @Override
    public void performEvent() {
        // TODO Auto-generated method stub
    }

    public SurfaceMap getHeightMap() {
        return this.heightMap;
    }
}
