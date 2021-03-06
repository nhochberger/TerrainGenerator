package model;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.text.i18n.DirectI18N;

public class RandomBoulderGenerator extends SessionBasedObject implements ObstacleGenerator {

    public RandomBoulderGenerator(final BasicSession session) {
        super(session);
    }

    @Override
    public void generateAndDistributeObstacles(final SurfaceMap originalMap, final double boulderAmountFactor) {
        originalMap.getBoulders().clear();
        final int amount = (int) (boulderAmountFactor * originalMap.getXDimension() * originalMap.getZDimension());
        logger().info("Generating " + amount + " boulders");
        final Random random = new Random();
        for (int i = 0; i < amount; i++) {
            final double x = 1 + random.nextDouble() * (originalMap.getXDimension() - 2);
            final double z = 1 + random.nextDouble() * (originalMap.getZDimension() - 2);
            final int xInt = (int) x;
            final int zInt = (int) z;
            final double radius = 0.1d + random.nextDouble() * 0.6d;
            final double y = ((originalMap.get(xInt, zInt) + originalMap.get(xInt + 1, zInt) + originalMap.get(xInt, zInt + 1) + originalMap.get(xInt + 1, zInt + 1)) / 4d) + radius * (3d / 5d);
            originalMap.addBoulder(new Boulder(x, y, z, radius));
            if (0 == i % 100 || i + 1 == amount) {
                session().getEventBus().publish(new TerrainGenerationProgressEvent(new DirectI18N("Boulders"), 100 * (int) ((float) i + 1) / amount));
            }
        }
        logger().info("Boulder generation finished");
    }
}
