package model;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class PyramidGenerator extends SessionBasedObject implements HeightMapGenerator {

    public PyramidGenerator(final BasicSession session) {
        super(session);
    }

    @Override
    public float[][] generate(final int dimension, final float roughness) {
        final float[][] heightMap = new float[dimension][dimension];
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                heightMap[x][y] = x + y;
            }
        }
        return heightMap;
    }
}
