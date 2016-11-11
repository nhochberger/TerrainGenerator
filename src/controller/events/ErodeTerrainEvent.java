package controller.events;

import hochberger.utilities.eventbus.Event;

public class ErodeTerrainEvent implements Event {

    private final int iterations;

    public ErodeTerrainEvent(final int iterations) {
        super();
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    @Override
    public void performEvent() {
    }
}
