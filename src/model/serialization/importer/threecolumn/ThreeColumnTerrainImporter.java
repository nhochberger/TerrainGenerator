package model.serialization.importer.threecolumn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.geo.GeoPoint;
import model.SurfaceMap;
import model.serialization.importer.TerrainImporter;

public class ThreeColumnTerrainImporter extends TerrainImporter {

    public ThreeColumnTerrainImporter(final BasicSession session) {
        super(session);
    }

    @Override
    public SurfaceMap importTerrain(final File file) throws IOException {
        SurfaceMap map = new SurfaceMap(0);
        final List<String> lines = Files.readAllLines(file.toPath());
        final List<Double> lats = new LinkedList<>();
        final List<Double> lons = new LinkedList<>();
        final List<GeoPoint> demPoints = new LinkedList<>();
        logger().info("Parsing DEM information.");
        for (final String line : lines) {
            final ThreeColumnDemEntry entry = ThreeColumnDemEntry.from(line);
            if (0 != entry.getAltitude()) {
                demPoints.add(new GeoPoint(entry.getLatitude(), entry.getLongitude(), entry.getAltitude()));
                lats.add(entry.getLatitude());
                lons.add(entry.getLongitude());
            }
        }
        final GeoPoint origin = determineOrigin(lats, lons);
        final List<MetricGridPoint> metricGrid = new LinkedList<>();
        double xMax = 0;
        double zMax = 0;
        logger().info("Determining the dimension of the area.");
        for (final GeoPoint point : demPoints) {
            final MetricGridPoint metricGridPoint = new MetricGridPoint(xDistance(origin, point), point.getAlt(), zDistance(origin, point));
            metricGrid.add(metricGridPoint);
            if (xMax < metricGridPoint.getX()) {
                xMax = metricGridPoint.getX();
            }
            if (zMax < metricGridPoint.getZ()) {
                zMax = metricGridPoint.getZ();
            }
        }
        map = new SurfaceMap((int) Math.max(xMax, zMax) + 1);
        logger().info("Interpolating elevation of points in grid. This may take several minutes.");
        for (int z = 0; z < map.getZDimension(); z++) {
            for (int x = 0; x < map.getXDimension(); x++) {
                map.set(x, z, findElevationOfClosestPoint(metricGrid, x, z));
            }
        }
        return map;
    }

    private double findElevationOfClosestPoint(final List<MetricGridPoint> metricGrid, final int x, final int z) {
        final MetricGridPoint point = new MetricGridPoint(x, 0, z);
        double minDistance = Double.MAX_VALUE;
        double elevation = 0;
        for (final MetricGridPoint metricGridPoint : metricGrid) {
            final double distance = point.distanceTo(metricGridPoint);
            if (distance < minDistance) {
                minDistance = distance;
                elevation = metricGridPoint.getY();
            }
        }
        return elevation;
    }

    private double xDistance(final GeoPoint origin, final GeoPoint point) {
        return origin.distanceTo(new GeoPoint(origin.getLat(), point.getLon(), 0));
    }

    private double zDistance(final GeoPoint origin, final GeoPoint point) {
        return origin.distanceTo(new GeoPoint(point.getLat(), origin.getLon(), 0));
    }

    protected GeoPoint determineOrigin(final List<Double> lats, final List<Double> lons) {
        double minLat = lats.get(0);
        for (final Double lat : lats) {
            if (minLat > lat) {
                minLat = lat;
            }
        }
        double minLon = lons.get(0);
        for (final Double lon : lons) {
            if (minLon > lon) {
                minLon = lon;
            }
        }
        return new GeoPoint(minLat, minLon, 0);
    }

    private static class ThreeColumnDemEntry {

        private final double latitude;
        private final double longitude;
        private final double altitude;

        public ThreeColumnDemEntry(final double latitude, final double longitude, final double altitude) {
            super();
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public double getAltitude() {
            return this.altitude;
        }

        @Override
        public String toString() {
            return "(" + this.latitude + "|" + this.longitude + "|" + this.altitude + ")";
        }

        public static ThreeColumnDemEntry from(final String readLine) {
            final String[] lineParts = readLine.trim().replaceAll(" +", " ").split(" ");
            return new ThreeColumnDemEntry(Double.parseDouble(lineParts[0]), Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2]));
        }
    }
}
