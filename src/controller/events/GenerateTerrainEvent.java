package controller.events;

import hochberger.utilities.eventbus.Event;

public class GenerateTerrainEvent implements Event {

    private final int dimension;
    private final double roughness;
    private final double elevation;

    public GenerateTerrainEvent(final int dimension, final double roughness, final double elevation) {
        super();
        this.dimension = dimension;
        this.roughness = roughness;
        this.elevation = elevation;
    }

    @Override
    public void performEvent() {
    }

    public int getDimension() {
        return this.dimension;
    }

    public double getRoughness() {
        return this.roughness;
    }

    public double getElevation() {
        return this.elevation;
    }
}
