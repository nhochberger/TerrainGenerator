package model;

public interface HeightMapGenerator {

    public HeightMap generate(int dimension, double roughness, double elevation, int erosion);

}
