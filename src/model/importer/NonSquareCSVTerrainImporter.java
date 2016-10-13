package model.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import hochberger.utilities.application.session.BasicSession;
import model.SurfaceMap;

public class NonSquareCSVTerrainImporter extends TerrainImporter {

    private static final String DELIMITER = ", ";

    public NonSquareCSVTerrainImporter(final BasicSession session) {
        super(session);
    }

    @Override
    public SurfaceMap importTerrain(final File file) throws IOException {
        final List<String> lines = Files.readAllLines(file.toPath());
        final int zDimension = lines.size();
        int xDimension = 0;
        final SurfaceMap map = new SurfaceMap(zDimension);
        for (int z = 0; z < zDimension; z++) {
            final String line = lines.get(z);
            final String[] split = line.split(DELIMITER);
            if (xDimension <= split.length) {
                xDimension = split.length;
            }
        }
        logger().info("Determined Dimension: " + xDimension + ", " + zDimension);
        for (int z = 0; z < zDimension; z++) {
            final String line = lines.get(z);
            final String[] split = line.split(DELIMITER);
            for (int x = 0; x < xDimension; x++) {
                final String elevation = split[x];
                try {
                    map.set(x, z, Float.parseFloat(elevation));
                } catch (final NumberFormatException e) {
                    map.set(xDimension, z, 0d);
                }
            }
        }
        return map;
    }
}
