package controller;

import controller.events.GenerateTerrainEvent;
import controller.events.TerrainGeneratedEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import model.CompletelyRandomGenerator;

public class GenerateTerrainEventForwarder extends SessionBasedObject implements EventReceiver<GenerateTerrainEvent> {

    private final CompletelyRandomGenerator generator;

    protected GenerateTerrainEventForwarder(final BasicSession session, final CompletelyRandomGenerator generator) {
        super(session);
        this.generator = generator;
    }

    @Override
    public void receive(final GenerateTerrainEvent event) {
        logger().info("Received GenerateTerrainEvent");
        final float[][] heightMap = this.generator.generate(100, 3f);
        session().getEventBus().publish(new TerrainGeneratedEvent(heightMap));
    }
}
