package model;

import java.util.HashMap;
import java.util.Map;

public class HeightMap {

	private final Map<Coordinate, Float> specifiedPoints;

	public HeightMap() {
		super();
		this.specifiedPoints = new HashMap<Coordinate, Float>();
	}

	public void setSpecifiedPoint(final int x, final int z, final float value) {
		this.specifiedPoints.put(new Coordinate(x, z), value);
	}
}
