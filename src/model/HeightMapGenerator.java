package model;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class HeightMapGenerator extends SessionBasedObject {

    public HeightMapGenerator(final BasicSession session) {
        super(session);
    }

    public float[][] generate(final int dimension, final float roughness) {
        final float[][] points = new float[dimension][dimension];
        final Random rand = new Random();
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                points[x][y] = rand.nextFloat() * roughness;
                final int percentage = (((x + 1) * 100) / dimension);
                logger().info("Terrain generation at " + percentage + "%");
                session().getEventBus().publish(new TerrainGenerationProgressEvent(percentage));
            }
        }
        return points;
    }
}
