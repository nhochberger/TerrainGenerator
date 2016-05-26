package model;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.exceptions.NotYetImplementedException;

public class DiamondSquareGenerator extends SessionBasedObject implements HeightMapGenerator {

	protected DiamondSquareGenerator(final BasicSession session) {
		super(session);
	}

	@Override
	public float[][] generate(final int dimension, final float roughness) {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}
}
