package model.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.files.Closer;
import model.SurfaceMap;

public class CSVHeightMapExporter extends SessionBasedObject implements HeightMapExporter {

    private static String DELIMITER = ", ";

    public CSVHeightMapExporter(final BasicSession session) {
        super(session);
    }

    @Override
    public void export(final SurfaceMap heightMap, final String filePath) {
        final int numOfPoints = heightMap.getXDimension() * heightMap.getZDimension();
        final StringBuffer buffer = new StringBuffer();
        for (int z = 0; z < heightMap.getZDimension(); z++) {
            for (int x = 0; x < heightMap.getXDimension(); x++) {
                buffer.append(heightMap.get(x, z));
                buffer.append(DELIMITER);
            }
            buffer.append(System.lineSeparator());
        }
        logger().info("Exporting " + numOfPoints + " elevation points (" + buffer.length() + " byte) to " + filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(filePath));
            writer.write(buffer.toString());
            logger().info("Export finished");
        } catch (final IOException e) {
            logger().error("Unable to write file to specified location: " + filePath, e);
        } finally {
            Closer.close(writer);
        }
    }
}
