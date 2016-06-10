package controller.events;

import hochberger.utilities.eventbus.Event;
import model.HeightMap;

public class TerrainGeneratedEvent implements Event {

    private final HeightMap heightMap;

    public TerrainGeneratedEvent(final HeightMap heightMap) {
        super();
        this.heightMap = heightMap;
    }

    @Override
    public void performEvent() {
        // TODO Auto-generated method stub
    }

    public HeightMap getHeightMap() {
        return this.heightMap;
    }
}
