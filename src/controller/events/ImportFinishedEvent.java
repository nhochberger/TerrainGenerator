package controller.events;

import hochberger.utilities.eventbus.Event;
import model.HeightMap;

public class ImportFinishedEvent implements Event {

    private final HeightMap heightMap;

    public ImportFinishedEvent(final HeightMap heightMap) {
        super();
        this.heightMap = heightMap;
    }

    public HeightMap getHeightMap() {
        return this.heightMap;
    }

    @Override
    public void performEvent() {
        // TODO Auto-generated method stub
    }
}
