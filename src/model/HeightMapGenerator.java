package model;

public interface HeightMapGenerator {

    public SurfaceMap generate(int dimension, double roughness, double elevation, int erosion, double boulders);

}
