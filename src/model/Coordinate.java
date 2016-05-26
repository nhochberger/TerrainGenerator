package model;

public class Coordinate {

	private final int x;
	private final int z;

	public Coordinate(final int x, final int z) {
		super();
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		return ((Coordinate) obj).x == this.x && ((Coordinate) obj).z == this.z;
	}
}
