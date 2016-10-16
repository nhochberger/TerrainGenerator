package model;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.text.i18n.DirectI18N;

public class NaturalSurfaceDiamondSquareGenerator extends SessionBasedObject implements HeightMapGenerator {

    private static final double THRESHOLD_FACTOR = 4d;

    private final DiamondSquareGenerator generator;

    public NaturalSurfaceDiamondSquareGenerator(final BasicSession session) {
        super(session);
        this.generator = new DiamondSquareGenerator(session);
    }

    @Override
    public SurfaceMap generate(final int dimension, final double roughness, final double elevation, final int erosion, final double boulders) {
        final SurfaceMap surface = this.generator.generate(dimension, roughness, elevation, erosion, boulders);
        logger().info("Fractal base surface generated");
        erode(surface, erosion);
        distributeBoulders(surface, boulders);
        return surface;
    }

    private void distributeBoulders(final SurfaceMap surface, final double boulders) {
        final int amount = (int) (boulders * surface.getXDimension() * surface.getZDimension());
        logger().info("Generating " + amount + " boulders");
        final Random random = new Random();
        for (int i = 0; i < amount; i++) {
            final double x = random.nextDouble() * surface.getXDimension() - 1;
            final double z = random.nextDouble() * surface.getZDimension() - 1;
            final int xInt = (int) x;
            final int zInt = (int) z;
            final double radius = 0.1d + random.nextDouble() * 0.6d;
            final double y = ((surface.get(xInt, zInt) + surface.get(xInt + 1, zInt) + surface.get(xInt, zInt + 1) + surface.get(xInt + 1, zInt + 1)) / 4d) + radius * (3d / 5d);
            surface.addBoulder(new Boulder(x, y, z, radius));
            if (0 == i % 100 || i + 1 == amount) {
                session().getEventBus().publish(new TerrainGenerationProgressEvent(new DirectI18N("Boulders"), 100 * (int) ((float) i + 1) / amount));
            }
        }
        logger().info("Boulder generation finished");
    }

    protected void erode(final SurfaceMap terrain, final int erosion) {
        logger().info("Eroding surface with " + erosion + " iterations");
        for (int n = 0; n < erosion; n++) {
            for (int z = 1; z < terrain.getZDimension() - 1; z++) {
                for (int x = 1; x < terrain.getXDimension() - 1; x++) {
                    int maxX = x;
                    int maxZ = z;
                    double maxD = 0d;
                    for (int i = -1; i <= 1; i += 2) {
                        for (int j = -1; j <= 1; j += 2) {
                            final double d = terrain.get(x, z) - terrain.get(x + j, z + i);
                            if (maxD <= d) {
                                maxD = d;
                                maxX = x + j;
                                maxZ = z + i;
                            }
                        }
                    }
                    if (2 * THRESHOLD_FACTOR / (terrain.getXDimension() + terrain.getZDimension()) < maxD) {
                        terrain.set(x, z, terrain.get(x, z) - (maxD / 2d));
                        terrain.set(maxX, maxZ, terrain.get(maxX, maxZ) + (maxD / 2d));
                    }
                }
            }
            session().getEventBus().publish(new TerrainGenerationProgressEvent(new DirectI18N("Erosion"), 100 * (int) ((float) n + 1) / erosion));
        }
        logger().info("Erosion finished");
    }
}
