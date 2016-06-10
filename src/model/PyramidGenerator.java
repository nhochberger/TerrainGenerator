package model;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class PyramidGenerator extends SessionBasedObject implements HeightMapGenerator {

    public PyramidGenerator(final BasicSession session) {
        super(session);
    }

    @Override
    public HeightMap generate(final int dimension, final double roughness) {
        final HeightMap map = new HeightMap(dimension);
        for (int x = 0; x < dimension; x++) {
            for (int z = 0; z < dimension; z++) {
                map.set(x, z, x + z);
            }
        }
        return map;
    }
}
