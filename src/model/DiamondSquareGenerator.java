package model;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

/**
 * Generates random fractal terrains assuming corners and edges as predefined to a height of zero.
 */
public class DiamondSquareGenerator extends SessionBasedObject implements HeightMapGenerator {

    private double roughness;
    private int dimension;
    private Random rand;
    private HeightMap map;

    public DiamondSquareGenerator(final BasicSession session) {
        super(session);
    }

    /**
     * Generates random fractal terrains assuming corners and edges as predefined to a height of zero.
     *
     * @param dimension
     *            - number of nodes along one side of the generated terrain. terrain will be a square consisting of dimension² nodes
     * @param roughness
     *            - defines how far apart to neighboring points are allowed to be. higher values will result in rougher surfaces
     * @return heightMap - an array of float[dimension][dimension] containing the height values to be used
     */

    @Override
    public HeightMap generate(final int dimension, final double roughness, final double elevation) {
        this.roughness = roughness;
        logger().info("Starting terrain generation. Dimension: " + dimension + ", roughness: " + roughness);
        this.dimension = dimension;
        this.rand = new Random();
        this.map = new HeightMap(dimension);
        this.map.set(0, 0, 0);
        this.map.set(dimension - 1, 0, this.rand.nextGaussian() * dimension * elevation * 0.05);
        this.map.set(0, dimension - 1, this.rand.nextGaussian() * dimension * elevation * 0.05);
        this.map.set(dimension - 1, dimension - 1, this.rand.nextGaussian() * dimension * elevation * 0.05);
        refine(dimension - 1, 0);
        logger().info("Terrain generation finished");
        return this.map;
    }

    private void refine(final int size, int step) {
        final int half = size / 2;
        if (1 > half) {
            return;
        }
        for (int z = half; z < this.dimension - 1; z += size) {
            for (int x = half; x < this.dimension - 1; x += size) {
                square(x, z, half, step);
            }
        }
        for (int z = 0; z <= this.dimension - 1; z += half) {
            for (int x = (z + half) % size; x <= this.dimension - 1; x += size) {
                diamond(x, z, half, step);
            }
        }
        session().getEventBus().publish(new TerrainGenerationProgressEvent(101 - (int) (100 * ((float) size) / this.dimension)));
        refine(half, ++step);
    }

    private void square(final int x, final int z, final int delta, final int step) {
        final float averageOfCorners = average(this.map.get(x - delta, z - delta), this.map.get(x + delta, z - delta), this.map.get(x + delta, z + delta), this.map.get(x - delta, z + delta));
        final double offset = this.rand.nextGaussian() * this.roughness * delta / (step + 1);
        this.map.set(x, z, (averageOfCorners + offset));
    }

    private void diamond(final int x, final int z, final int delta, final int step) {
        final float averageOfCorners = calculateDiamondAveragesFor(x, z, delta);
        final double offset = this.rand.nextGaussian() * this.roughness * delta / (step + 1);
        this.map.set(x, z, (averageOfCorners + offset));
    }

    private float calculateDiamondAveragesFor(final int x, final int z, final int delta) {
        float sum = 0f;
        int numberOfSummands = 0;
        if (0 <= x - delta) {
            sum += this.map.get(x - delta, z);
            numberOfSummands++;
        }
        if (this.dimension > x + delta) {
            sum += this.map.get(x + delta, z);
            numberOfSummands++;
        }
        if (0 <= z - delta) {
            sum += this.map.get(x, z - delta);
            numberOfSummands++;
        }
        if (this.dimension > z + delta) {
            sum += this.map.get(x, z + delta);
            numberOfSummands++;
        }
        return sum / numberOfSummands;
    }

    private float average(final double... args) {
        float sum = 0;
        if (0 == args.length) {
            return 0f;
        }
        for (final double current : args) {
            sum += current;
        }
        return sum / args.length;
    }
}
