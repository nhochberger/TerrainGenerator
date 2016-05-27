package controller;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import model.HeightMapGenerator;
import controller.events.GenerateTerrainEvent;
import controller.events.TerrainGeneratedEvent;

public class GenerateTerrainEventForwarder extends SessionBasedObject implements EventReceiver<GenerateTerrainEvent> {

	private final HeightMapGenerator generator;

	protected GenerateTerrainEventForwarder(final BasicSession session, final HeightMapGenerator generator) {
		super(session);
		this.generator = generator;
	}

	@Override
	public void receive(final GenerateTerrainEvent event) {
		logger().info("Received GenerateTerrainEvent");
		final float[][] heightMap = this.generator.generate(129, 0.f);
		session().getEventBus().publish(new TerrainGeneratedEvent(heightMap));
	}
}
