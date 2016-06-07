package model;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

/**
 * Generates random fractal terrains assuming corners and edges as predefined to a height of zero.
 */
public class DiamondSquareGenerator extends SessionBasedObject implements HeightMapGenerator {

    private float roughness;
    private int dimension;
    private Random rand;
    private float[][] heightMap;

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
    public float[][] generate(final int dimension, final float roughness) {
        this.roughness = roughness;
        logger().info("Starting terrain generation. Dimension: " + dimension + ", roughness: " + roughness);
        this.dimension = dimension;
        this.heightMap = new float[this.dimension][this.dimension];
        this.heightMap[0][0] = 0;
        this.heightMap[dimension - 1][0] = 0;
        this.heightMap[0][dimension - 1] = 0;
        this.heightMap[dimension - 1][dimension - 1] = 0;
        this.rand = new Random();
        refine(dimension - 1, 0);
        logger().info("Terrain generation finished");
        return this.heightMap;
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
        final float averageOfCorners = average(this.heightMap[x - delta][z - delta], this.heightMap[x + delta][z - delta], this.heightMap[x + delta][z + delta], this.heightMap[x - delta][z + delta]);
        final double offset = this.rand.nextGaussian() * (this.roughness * delta);
        this.heightMap[x][z] = (float) (averageOfCorners + offset);
    }

    private void diamond(final int x, final int z, final int delta, final int step) {
        final float averageOfCorners = calculateDiamondAveragesFor(x, z, delta);
        final double offset = this.rand.nextGaussian() * (this.roughness * delta);
        this.heightMap[x][z] = (float) (averageOfCorners + offset);
    }

    private float calculateDiamondAveragesFor(final int x, final int z, final int delta) {
        float sum = 0f;
        int numberOfSummands = 0;
        if (0 <= x - delta) {
            sum += this.heightMap[x - delta][z];
            numberOfSummands++;
        }
        if (this.dimension > x + delta) {
            sum += this.heightMap[x + delta][z];
            numberOfSummands++;
        }
        if (0 <= z - delta) {
            sum += this.heightMap[x][z - delta];
            numberOfSummands++;
        }
        if (this.dimension > z + delta) {
            sum += this.heightMap[x][z + delta];
            numberOfSummands++;
        }
        return sum / numberOfSummands;
    }

    private float average(final float... args) {
        float sum = 0;
        if (0 == args.length) {
            return 0f;
        }
        for (final float current : args) {
            sum += current;
        }
        return sum / args.length;
    }
}
