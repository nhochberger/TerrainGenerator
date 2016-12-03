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
        logger().info("Fractal base surface generated");
        this.eroder.erode(surface, erosion);
        this.obstacleGenerator.generateAndDistributeObstacles(surface, boulderAmountFactor);
        return surface;
    }
}
