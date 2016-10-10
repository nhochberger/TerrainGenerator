package model;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class ErosionDiamondSquareGenerator extends SessionBasedObject implements HeightMapGenerator {

    private static final int ITERATIONS = 500;

    private static final double THRESHOLD_FACTOR = 4d;

    private final DiamondSquareGenerator generator;

    public ErosionDiamondSquareGenerator(final BasicSession session) {
        super(session);
        this.generator = new DiamondSquareGenerator(session);
    }

    @Override
    public HeightMap generate(final int dimension, final double roughness, final double elevation) {
        final HeightMap terrain = this.generator.generate(dimension, roughness, elevation);
        erode(terrain);
        return terrain;
    }

    protected void erode(final HeightMap terrain) {
        for (int n = 0; n < ITERATIONS; n++) {
            for (int z = 1; z < terrain.getZDimension() - 1; z++) {
                for (int x = 1; x < terrain.getXDimension() - 1; x++) {
                    int maxX = x;
                    int maxZ = z;
                    double maxD = 0d;
                    for (int i = -1; i <= 1; i += 1) {
                        for (int j = -1; j <= 1; j += 1) {
                            final double d = terrain.get(x, z) - terrain.get(x + j, z + i);
                            if (maxD <= d) {
                                maxD = d;
                                maxX = x + j;
                                maxZ = z + i;
                            }
                        }
                    }
                    if (2 * THRESHOLD_FACTOR / (terrain.getXDimension() + terrain.getZDimension()) < maxD) {
                        terrain.set(x, z, terrain.get(x, z) - (maxD / 5d));
                        terrain.set(maxX, maxZ, terrain.get(maxX, maxZ) + (maxD / 5d));
                    }
                }
            }
            session().getEventBus().publish(new TerrainGenerationProgressEvent(100 * (int) ((float) n + 1) / ITERATIONS));
        }
    }
}
