package model;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.text.i18n.DirectI18N;

public class ErosionDiamondSquareGenerator extends SessionBasedObject implements HeightMapGenerator {

    private static final double THRESHOLD_FACTOR = 4d;
    private static final double EROSION_COEFFICIENT = 0.5d;

    private final DiamondSquareGenerator generator;

    public ErosionDiamondSquareGenerator(final BasicSession session) {
        super(session);
        this.generator = new DiamondSquareGenerator(session);
    }

    @Override
    public HeightMap generate(final int dimension, final double roughness, final double elevation, final int erosion) {
        final HeightMap terrain = this.generator.generate(dimension, roughness, elevation, erosion);
        erode(terrain, erosion);
        return terrain;
    }

    protected void erode(final HeightMap terrain, final int erosion) {
        for (int n = 0; n < erosion; n++) {
            for (int z = 1; z < terrain.getZDimension() - 1; z++) {
                for (int x = 1; x < terrain.getXDimension() - 1; x++) {
                    double dMax = 0d;
                    double dTotal = 0d;
                    final double[][] neighborhood = new double[3][3];
                    for (int i = -1; i <= 1; i++) { // Moore neighborhood
                        for (int j = -1; j <= 1; j++) {
                            final double d = terrain.get(x, z) - terrain.get(x + j, z + i);
                            if (threshold(terrain) < d) {
                                neighborhood[j + 1][i + 1] = d;
                                dTotal += d;
                                if (dMax < d) {
                                    dMax = d;
                                }
                            }
                        }
                    }
                    if (0.001 < dTotal) {
                        for (int i = -1; i <= 1; i++) { // Moore neighborhood
                            for (int j = -1; j <= 1; j++) {
                                final double offset = (neighborhood[j + 1][i + 1] / dTotal) * EROSION_COEFFICIENT * (dMax - threshold(terrain));
                                final double newElevation = terrain.get(x + j, z + i) + offset;
                                terrain.set(x + j, z + i, newElevation);
                            }
                        }
                        System.err.println(terrain.get(x, z) - dTotal);
                    }
                }
            }
            session().getEventBus().publish(new TerrainGenerationProgressEvent(new DirectI18N("Erosion"), 100 * (int) ((float) n + 1) / erosion));
        }
    }

    private double threshold(final HeightMap terrain) {
        return 2d * THRESHOLD_FACTOR / (terrain.getXDimension() + terrain.getZDimension());
    }
}
