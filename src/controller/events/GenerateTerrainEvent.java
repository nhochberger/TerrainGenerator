package controller.events;

import hochberger.utilities.eventbus.Event;

public class GenerateTerrainEvent implements Event {

    private final float roughness;
    private final int dimension;

    public GenerateTerrainEvent(final int dimension, final float roughness) {
        super();
        this.dimension = dimension;
        this.roughness = roughness;
    }

    @Override
    public void performEvent() {
    }

    public int getDimension() {
        return this.dimension;
    }

    public float getRoughness() {
        return this.roughness;
    }
}
