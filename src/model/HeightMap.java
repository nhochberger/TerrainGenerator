package model;

public class HeightMap {

    private final double[][] points;
    private final int xDimension;
    private final int zDimension;

    public HeightMap(final int dimension) {
        this(dimension, dimension);
    }

    public HeightMap(final int xDimension, final int zDimension) {
        super();
        this.xDimension = xDimension;
        this.zDimension = zDimension;
        this.points = new double[xDimension][zDimension];
    }

    public void set(final int x, final int z, final double elevation) {
        this.points[x][z] = elevation;
    }

    public double get(final int x, final int z) {
        return this.points[x][z];
    }

    public int getXDimension() {
        return this.xDimension;
    }

    public int getZDimension() {
        return this.zDimension;
    }
}
