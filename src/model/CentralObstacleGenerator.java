package model;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class CentralObstacleGenerator extends SessionBasedObject implements ObstacleGenerator {

    public CentralObstacleGenerator(final BasicSession session) {
        super(session);
    }

    @Override
    public void generateAndDistributeObstacles(final SurfaceMap originalMap, final double boulderAmountFactor) {
        final int x = originalMap.getXDimension() / 2;
        final int z = originalMap.getZDimension() / 2;
        final double radius = 50d;
        final double y = originalMap.get(x, z) + radius * 0.75;
        originalMap.addBoulder(new Boulder(x, y, z, radius));
    }
}
