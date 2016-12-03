package model;

import hochberger.utilities.application.session.BasicSession;

public class NaturalSurfaceDiamondSquareGenerator extends DiamondSquareGenerator {

    private final Eroder eroder;
    BoulderGenerator boulderGenerator;

    public NaturalSurfaceDiamondSquareGenerator(final BasicSession session) {
        super(session);
        this.eroder = new Eroder(session);
        this.boulderGenerator = new BoulderGenerator(session);
    }

    @Override
    public SurfaceMap generate(final int dimension, final double roughness, final double elevation, final int erosion, final double boulderAmountFactor) {
        final SurfaceMap surface = super.generate(dimension, roughness, elevation, erosion, boulderAmountFactor);
        logger().info("Fractal base surface generated");
        this.eroder.erode(surface, erosion);
        this.boulderGenerator.generateAndDistributeBoulders(surface, boulderAmountFactor);
        return surface;
    }
}
