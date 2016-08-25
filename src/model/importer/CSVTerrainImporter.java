package model.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import hochberger.utilities.application.session.BasicSession;
import model.HeightMap;

public class CSVTerrainImporter extends TerrainImporter {

    private static final String DELIMITER = ", ";

    public CSVTerrainImporter(final BasicSession session) {
        super(session);
    }

    @Override
    public HeightMap importTerrain(final File file) throws IOException {
        final List<String> lines = Files.readAllLines(file.toPath());
        final int dimension = lines.size();
        final HeightMap map = new HeightMap(dimension);
        for (int z = 0; z < dimension; z++) {
            final String line = lines.get(z);
            final String[] split = line.split(DELIMITER);
            for (int x = 0; x < split.length; x++) {
                if (dimension != split.length) {
                    logger().error("Corrupt file. Dimensions do not fit.");
                    return new HeightMap(0);
                }
                final String elevation = split[x];
                map.set(x, z, Float.parseFloat(elevation));
            }
        }
        return map;
    }
}
