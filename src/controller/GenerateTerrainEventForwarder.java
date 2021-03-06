package controller;

import controller.events.GenerateTerrainEvent;
import controller.events.TerrainGeneratedEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import model.SurfaceMap;
import model.HeightMapGenerator;

public class GenerateTerrainEventForwarder extends SessionBasedObject implements EventReceiver<GenerateTerrainEvent> {

    private final HeightMapGenerator generator;

    protected GenerateTerrainEventForwarder(final BasicSession session, final HeightMapGenerator generator) {
        super(session);
        this.generator = generator;
    }

    @Override
    public void receive(final GenerateTerrainEvent event) {
        logger().info("Received GenerateTerrainEvent");
        final SurfaceMap heightMap = this.generator.generate(event.getDimension(), event.getRoughness(), event.getElevation(), event.getErosion(), event.getBoulders());
        session().getEventBus().publish(new TerrainGeneratedEvent(heightMap));
    }
}
