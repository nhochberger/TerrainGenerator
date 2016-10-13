package controller.events;

import hochberger.utilities.eventbus.Event;

public class GenerateTerrainEvent implements Event {

    private final int dimension;
    private final double roughness;
    private final double elevation;
    private final int erosion;
    private final double boulders;

    public GenerateTerrainEvent(final int dimension, final double roughness, final double elevation, final int erosion, final double boulders) {
        super();
        this.dimension = dimension;
        this.roughness = roughness;
        this.elevation = elevation;
        this.erosion = erosion;
        this.boulders = boulders;
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

    public int getErosion() {
        return this.erosion;
    }

    public double getBoulders() {
        return this.boulders;
    }
}
