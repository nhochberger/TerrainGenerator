package model.importer.threecolumn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.geo.GeoPoint;
import model.HeightMap;
import model.importer.TerrainImporter;

public class ThreeColumnTerrainImporter extends TerrainImporter {

    public ThreeColumnTerrainImporter(final BasicSession session) {
        super(session);
    }

    @Override
    public HeightMap importTerrain(final File file) throws IOException {
        final HeightMap map = new HeightMap(0);
        final List<String> lines = Files.readAllLines(file.toPath());
        final List<Double> lats = new LinkedList<>();
        final List<Double> lons = new LinkedList<>();
        for (final String line : lines) {
            final ThreeColumnDemEntry entry = ThreeColumnDemEntry.from(line);
            lats.add(entry.getLatitude());
            lons.add(entry.getLongitude());
        }
        double minLat = lats.get(0);
        double maxLat = lats.get(0);
        for (final Double lat : lats) {
            if (minLat > lat) {
                minLat = lat;
            }
            if (maxLat < lat) {
                maxLat = lat;
            }
        }
        double minLon = lons.get(0);
        double maxLon = lons.get(0);
        for (final Double lon : lons) {
            if (minLon > lon) {
                minLon = lon;
            }
            if (maxLon < lon) {
                maxLon = lon;
            }
        }
        final GeoPoint origin = new GeoPoint(minLat, minLon, 0);
        final GeoPoint maxXPoint = new GeoPoint(minLat, maxLon, 0);
        final GeoPoint maxZPoint = new GeoPoint(maxLat, minLon, 0);
        return map;
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
