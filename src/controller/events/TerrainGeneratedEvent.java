package controller.events;

import hochberger.utilities.eventbus.Event;

public class TerrainGeneratedEvent implements Event {

    private final float[][] heightMap;

    public TerrainGeneratedEvent(final float[][] heightMap) {
        super();
        this.heightMap = heightMap;
    }

    @Override
    public void performEvent() {
        // TODO Auto-generated method stub
    }

    public float[][] getHeightMap() {
        return this.heightMap;
    }

}
