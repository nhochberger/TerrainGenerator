package controller.events;

import hochberger.utilities.eventbus.Event;

public class TerrainGenerationProgressEvent implements Event {

    private final int progressPercentage;

    public TerrainGenerationProgressEvent(final int progressPercentage) {
        super();
        this.progressPercentage = progressPercentage;
    }

    @Override
    public void performEvent() {

    }

    public int getProgressPercentage() {
        return this.progressPercentage;
    }
}
