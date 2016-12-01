package model.serialization.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.files.Closer;
import model.Boulder;
import model.SurfaceMap;
import model.serialization.SerializationConstants;

public class CSVHeightMapExporter extends SessionBasedObject implements HeightMapExporter {

    public CSVHeightMapExporter(final BasicSession session) {
        super(session);
    }

    @Override
    public void export(final SurfaceMap heightMap, final String filePath) {
        final DecimalFormat formatter = new DecimalFormat("#.####");
        formatter.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        final int numOfPoints = heightMap.getXDimension() * heightMap.getZDimension();
        final StringBuffer buffer = new StringBuffer();
        for (int z = 0; z < heightMap.getZDimension(); z++) {
            for (int x = 0; x < heightMap.getXDimension(); x++) {
                buffer.append(formatter.format(heightMap.get(x, z)));
                buffer.append(SerializationConstants.VALUE_DELIMITER);
            }
            buffer.append(System.lineSeparator());
        }
        logger().info("Exporting " + numOfPoints + " elevation points (" + buffer.length() + " byte) to " + filePath);
        logger().info("Beginning to export " + heightMap.getBoulders().size() + " obstacles");
        buffer.append(SerializationConstants.SECTION_DELIMITER);
        buffer.append(System.lineSeparator());
        for (final Boulder boulder : heightMap.getBoulders()) {
            buffer.append(formatter.format(boulder.getX()));
            buffer.append(SerializationConstants.VALUE_DELIMITER);
            buffer.append(formatter.format(boulder.getY()));
            buffer.append(SerializationConstants.VALUE_DELIMITER);
            buffer.append(formatter.format(boulder.getZ()));
            buffer.append(SerializationConstants.VALUE_DELIMITER);
            buffer.append(formatter.format(boulder.getRadius()));
            buffer.append(SerializationConstants.VALUE_DELIMITER);
            buffer.append(System.lineSeparator());
        }
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
