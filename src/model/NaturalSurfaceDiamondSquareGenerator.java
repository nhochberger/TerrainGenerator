package model;

import hochberger.utilities.application.session.BasicSession;

public class NaturalSurfaceDiamondSquareGenerator extends DiamondSquareGenerator {

    private final Eroder eroder;
    ObstacleGenerator obstacleGenerator;

    public NaturalSurfaceDiamondSquareGenerator(final BasicSession session, final Eroder eroder, final ObstacleGenerator obstacleGenerator) {
        super(session);
        this.eroder = eroder;
        this.obstacleGenerator = obstacleGenerator;
    }

    @Override
    public SurfaceMap generate(final int dimension, final double roughness, final double elevation, final int erosion, final double boulderAmountFactor) {
        final SurfaceMap surface = super.generate(dimension, roughness, elevation, erosion, boulderAmountFactor);
        adjustSubAverageElevation(surface);
        logger().info("Fractal base surface generated");
        this.eroder.erode(surface, erosion);
        this.obstacleGenerator.generateAndDistributeObstacles(surface, boulderAmountFactor);
        return surface;
    }

    private void adjustSubAverageElevation(final SurfaceMap surface) {
        double sum = 0;
        for (int z = 0; z < surface.getZDimension(); z++) {
            for (int x = 0; x < surface.getXDimension(); x++) {
                sum += surface.get(x, z);
            }
        }
        final double average = sum / (surface.getXDimension() * surface.getZDimension());
        for (int z = 0; z < surface.getZDimension(); z++) {
            for (int x = 0; x < surface.getXDimension(); x++) {
                final double y = surface.get(x, z);
                if (average > y) {
                    final double newY = ((y - average) / 3) + average;
                    surface.set(x, z, newY);
                }
            }
        }
    }
}
