package model.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.files.Closer;

public class CSVHeightMapExporter extends SessionBasedObject implements HeightMapExporter {

    private static String DELIMITER = ", ";

    public CSVHeightMapExporter(final BasicSession session) {
        super(session);
    }

    @Override
    public void export(final float[][] heightMap, final String filePath) {

        final int numOfPoints = heightMap.length * heightMap.length;
        final StringBuffer buffer = new StringBuffer();
        for (final float[] z : heightMap) {
            for (final float x : z) {
                buffer.append(x);
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
