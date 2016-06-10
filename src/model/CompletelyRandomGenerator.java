package model;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class CompletelyRandomGenerator extends SessionBasedObject implements HeightMapGenerator {

    public CompletelyRandomGenerator(final BasicSession session) {
        super(session);
    }

    @Override
    public HeightMap generate(final int dimension, final double roughness) {
        final HeightMap map = new HeightMap(dimension);
        final Random rand = new Random();
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                map.set(x, y, rand.nextFloat() * roughness);
                final int percentage = (((x + 1) * 100) / dimension);
                logger().info("Terrain generation at " + percentage + "%");
                session().getEventBus().publish(new TerrainGenerationProgressEvent(percentage));
            }
        }
        return map;
    }
}
