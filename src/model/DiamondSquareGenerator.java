package model;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

import java.util.Random;

import controller.events.TerrainGenerationProgressEvent;

/**
 * Generates random fractal terrains assuming corners and edges as predefined to
 * a height of zero.
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
	 * Generates random fractal terrains assuming corners and edges as
	 * predefined to a height of zero.
	 *
	 * @param dimension
	 *            - number of nodes along one side of the generated terrain.
	 *            terrain will be a square consisting of dimension² nodes
	 * @param roughness
	 *            - defines how far apart to neighboring points are allowed to
	 *            be. higher values will result in rougher surfaces
	 * @return heightMap - an array of float[dimension][dimension] containing
	 *         the height values to be used
	 */

	@Override
	public float[][] generate(final int dimension, final float roughness) {
		this.roughness = roughness;
		logger().info("Staring terrain generation. Dimension: " + dimension + ", roughness: " + roughness);
		this.dimension = dimension;
		this.heightMap = new float[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			this.heightMap[0][i] = 0;
			this.heightMap[dimension - 1][i] = 0;
			this.heightMap[i][0] = 0;
			this.heightMap[i][dimension - 1] = 0;
		}
		this.rand = new Random();
		int step = 1;
		refine(step);
		return this.heightMap;
	}

	private void refine(int step) {
		System.err.println("refining step " + step);
		step++;
		int delta = this.dimension / step;
		System.err.println(delta);
		if (0 == delta) {
			return;
		}
		for (int x = delta; x < this.dimension - 1; x += delta) {
			for (int z = delta; z < this.dimension - 1; z += delta) {
				this.heightMap[x][z] = (float) (this.rand.nextGaussian() * this.roughness * 10f / step);
				System.err.println("[" + x + "][" + z + "] = " + this.heightMap[x][z]);
			}
			final int percentage = (100 * (step + 1) / (this.dimension + 1));
			logger().info("Terrain generation at " + percentage + "%");
			session().getEventBus().publish(new TerrainGenerationProgressEvent(percentage));
		}
		refine(step);
	}
}
