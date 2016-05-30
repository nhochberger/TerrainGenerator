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
        for (int i = 0; i < this.dimension; i++) {
            this.heightMap[0][i] = 0;
            this.heightMap[dimension - 1][i] = 0;
            this.heightMap[i][0] = 0;
            this.heightMap[i][dimension - 1] = 0;
        }
        this.rand = new Random();
        refine(dimension - 1, 1);
        logger().info("Terrain generation finished");
        // printHeightMap();
        return this.heightMap;
    }

    private void printHeightMap() {
        for (int x = 0; x < this.dimension; x++) {
            for (int y = 0; y < this.dimension; y++) {
                System.err.print(this.heightMap[x][y] + "\t");
            }
            System.err.print("\n\n");
        }
    }

    private void refine(final int size, int step) {
        final int half = size / 2;
        if (1 > half) {
            return;
        }
        for (int x = half; x < this.dimension - 1; x += size) {
            for (int z = half; z < this.dimension - 1; z += size) {
                square(x, z, half, step);
            }
        }
        for (int x = 0; x <= this.dimension - 1; x += half) {
            for (int z = (x + half) % size; z <= this.dimension - 1; z += size) {
                diamond(x, z, half, step);
            }
        }
        session().getEventBus().publish(new TerrainGenerationProgressEvent(101 - (int) (100 * ((float) size) / this.dimension)));
        refine(half, ++step);
    }

    private void square(final int x, final int z, final int delta, final int step) {
        this.heightMap[x][z] += 1;
        // final float averageOfCorners = average(this.heightMap[x - delta][z], this.heightMap[x][z - delta], this.heightMap[x + delta][z], this.heightMap[x][z + delta]);
        // final double offset = this.rand.nextGaussian() * this.roughness * delta * Math.pow(2d, -step);
        // this.heightMap[x][z] += (float) (averageOfCorners + offset);
        // System.err.println(offset + " + " + this.heightMap[x][z]);
    }

    private void diamond(final int x, final int z, final int delta, final int step) {
        this.heightMap[x][z] += 1;
        // final float averageOfCorners = average(this.heightMap[Math.abs(x - delta)][Math.abs(z - delta)], this.heightMap[Math.abs(x - delta)][Math.abs(z + delta)],
        // this.heightMap[Math.abs(x + delta)][Math.abs(z + delta)], this.heightMap[Math.abs(x + delta)][Math.abs(z - delta)]);
        // final double offset = this.rand.nextGaussian() * this.roughness * delta * Math.pow(2d, -step);
        // this.heightMap[x][z] += (float) (averageOfCorners + offset);
        // System.err.println(offset + " + " + this.heightMap[x][z]);
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
