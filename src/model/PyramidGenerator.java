package model;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class PyramidGenerator extends SessionBasedObject implements HeightMapGenerator {

    public PyramidGenerator(final BasicSession session) {
        super(session);
    }

    @Override
    public SurfaceMap generate(final int dimension, final double roughness, final double elevation, final int erosion, final double boulders) {
        final SurfaceMap map = new SurfaceMap(dimension);
        for (int x = 0; x < dimension; x++) {
            for (int z = 0; z < dimension; z++) {
                map.set(x, z, x + z);
            }
        }
        return map;
    }
}
