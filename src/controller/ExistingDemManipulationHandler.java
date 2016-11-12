package controller;

import controller.events.ErodeTerrainEvent;
import controller.events.GenerateBouldersEvent;
import controller.events.ImportFinishedEvent;
import controller.events.TerrainGeneratedEvent;
import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import model.BoulderGenerator;
import model.Eroder;
import model.SurfaceMap;

public class ExistingDemManipulationHandler extends SessionBasedObject implements Lifecycle {

    private final Eroder eroder;
    private final BoulderGenerator boulderGenerator;
    private SurfaceMap map;

    public ExistingDemManipulationHandler(final BasicSession session) {
        super(session);
        this.eroder = new Eroder(session());
        this.boulderGenerator = new BoulderGenerator(session());
        this.map = new SurfaceMap(0);
    }

    @Override
    public void start() {
        logger().info("Starting DEM manipulation handler");
        session().getEventBus().register(new TerrainGenerationFinishedEventHandler(), TerrainGeneratedEvent.class);
        session().getEventBus().register(new TerrainImportFinishedEventHandler(), ImportFinishedEvent.class);
        session().getEventBus().register(new ErodeTerrainEventHandler(), ErodeTerrainEvent.class);
        session().getEventBus().register(new GenerateBouldersEventHandler(), GenerateBouldersEvent.class);
    }

    @Override
    public void stop() {
        logger().info("Shutting down DEM manipulation handler");
    }

    private final class TerrainImportFinishedEventHandler implements EventReceiver<ImportFinishedEvent> {

        public TerrainImportFinishedEventHandler() {
            super();
        }

        @Override
        public void receive(final ImportFinishedEvent event) {
            ExistingDemManipulationHandler.this.map = event.getHeightMap();
        }
    }

    private final class TerrainGenerationFinishedEventHandler implements EventReceiver<TerrainGeneratedEvent> {

        public TerrainGenerationFinishedEventHandler() {
            super();
        }

        @Override
        public void receive(final TerrainGeneratedEvent event) {
            ExistingDemManipulationHandler.this.map = event.getHeightMap();
        }
    }

    private final class GenerateBouldersEventHandler implements EventReceiver<GenerateBouldersEvent> {

        public GenerateBouldersEventHandler() {
            super();
        }

        @Override
        public void receive(final GenerateBouldersEvent event) {
            ExistingDemManipulationHandler.this.boulderGenerator.generateAndDistributeBoulders(ExistingDemManipulationHandler.this.map, event.getBoulderAmountFactor());
        }
    }

    private final class ErodeTerrainEventHandler implements EventReceiver<ErodeTerrainEvent> {

        public ErodeTerrainEventHandler() {
            super();
        }

        @Override
        public void receive(final ErodeTerrainEvent event) {
            ExistingDemManipulationHandler.this.eroder.erode(ExistingDemManipulationHandler.this.map, event.getIterations());
        }
    }
}