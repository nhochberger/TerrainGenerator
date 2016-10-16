package model.serialization.importer.threecolumn;

public class MetricGridPoint {

    private double x;
    private double y;
    private double z;

    public MetricGridPoint(final double x, final double y, final double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public double distanceTo(final MetricGridPoint other) {
        if (null == other) {
            return Double.NaN;
        }
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.z - other.z, 2));
    }

    @Override
    public String toString() {
        return "(" + this.x + "|" + this.y + "|" + this.z + ")";
    }
}
