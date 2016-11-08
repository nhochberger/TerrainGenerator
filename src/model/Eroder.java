package model;

import java.util.List;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.text.i18n.DirectI18N;

public class Eroder extends SessionBasedObject {

    private final double threshold;

    public Eroder(final BasicSession session, final double threshold) {
        super(session);
        this.threshold = threshold;
    }

    public void erode(final SurfaceMap originalMap, final int iterations) {
        logger().info("Eroding surface with " + iterations + " iterations");
        for (int n = 0; n < iterations; n++) {
            for (int z = 1; z < originalMap.getZDimension() - 1; z++) {
                for (int x = 1; x < originalMap.getXDimension() - 1; x++) {
                    int maxX = x;
                    int maxZ = z;
                    double maxD = 0d;
                    for (int i = -1; i <= 1; i += 2) {
                        for (int j = -1; j <= 1; j += 2) {
                            final double d = originalMap.get(x, z) - originalMap.get(x + j, z + i);
                            if (maxD <= d) {
                                maxD = d;
                                maxX = x + j;
                                maxZ = z + i;
                            }
                        }
                    }
                    if (2 * this.threshold / (originalMap.getXDimension() + originalMap.getZDimension()) < maxD) {
                        originalMap.set(x, z, originalMap.get(x, z) - (maxD / 2d));
                        originalMap.set(maxX, maxZ, originalMap.get(maxX, maxZ) + (maxD / 2d));
                    }
                }
            }
            session().getEventBus().publish(new TerrainGenerationProgressEvent(new DirectI18N("Erosion"), 100 * (int) ((float) n + 1) / iterations));
        }
        logger().info("Erosion finished");
        adjustBoulders(originalMap);
    }

    private void adjustBoulders(final SurfaceMap originalMap) {
        logger().info("Adjusting boulders");
        final List<Boulder> boulders = originalMap.getBoulders();
        for (final Boulder boulder : boulders) {
            final double x = boulder.getX();
            final double z = boulder.getZ();
            final double radius = boulder.getRadius();
            final int xInt = (int) x;
            final int zInt = (int) z;
            final double y = ((originalMap.get(xInt, zInt) + originalMap.get(xInt + 1, zInt) + originalMap.get(xInt, zInt + 1) + originalMap.get(xInt + 1, zInt + 1)) / 4d) + radius * (3d / 5d);
            boulder.adjustY(y);
        }
        logger().info("Adjustment finished");
    }
}
