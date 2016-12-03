package model;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.text.i18n.DirectI18N;

public class SimpleEroder extends SessionBasedObject implements Eroder {

    public SimpleEroder(final BasicSession session) {
        super(session);
    }

    @Override
    public void erode(final SurfaceMap originalMap, final int iterations) {
        logger().info("Eroding surface with " + iterations + " iterations");
        for (int n = 0; n < iterations; n++) {
            for (int z = 0; z < originalMap.getZDimension(); z++) {
                for (int x = 0; x < originalMap.getXDimension(); x++) {
                    originalMap.set(x, z, originalMap.get(x, z) * 0.9d);
                }
            }
            session().getEventBus().publish(new TerrainGenerationProgressEvent(new DirectI18N("Erosion"), 100 * (int) ((float) n + 1) / iterations));
        }
        logger().info("Erosion finished");
    }
}
