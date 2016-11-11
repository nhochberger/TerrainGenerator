package controller;

import controller.events.DemManipulationFinishedEvent;
import controller.events.ExportTerrainEvent;
import controller.events.ImportFinishedEvent;
import controller.events.TerrainGeneratedEvent;
import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import model.SurfaceMap;
import model.serialization.exporter.HeightMapExporter;

public class ExportTerrainEventHandler extends SessionBasedObject implements Lifecycle {

    private final HeightMapExporter exporter;
    private SurfaceMap heightMap;

    public ExportTerrainEventHandler(final BasicSession session, final HeightMapExporter exporter) {
        super(session);
        this.exporter = exporter;
    }

    @Override
    public void start() {
        logger().info("Starting ExportTerrainEventHandler");
        session().getEventBus().register(new TerrainGeneratedEventForwarder(), TerrainGeneratedEvent.class);
        session().getEventBus().register(new TerrainImportedEventForwarder(), ImportFinishedEvent.class);
        session().getEventBus().register(new DemManipulationFinishedEventForwarder(), DemManipulationFinishedEvent.class);
        session().getEventBus().register(new ExportTerrainEventForwarder(), ExportTerrainEvent.class);
    }

    @Override
    public void stop() {
        logger().info("ExportTerrainEventHandler stopped");
    }

    private final class DemManipulationFinishedEventForwarder implements EventReceiver<DemManipulationFinishedEvent> {

        public DemManipulationFinishedEventForwarder() {
            super();
        }

        @Override
        public void receive(final DemManipulationFinishedEvent event) {
            ExportTerrainEventHandler.this.heightMap = event.getSurfaceMap();
        }
    }

    private final class TerrainImportedEventForwarder implements EventReceiver<ImportFinishedEvent> {

        public TerrainImportedEventForwarder() {
            super();
        }

        @Override
        public void receive(final ImportFinishedEvent event) {
            ExportTerrainEventHandler.this.heightMap = event.getHeightMap();
        }
    }

    private final class TerrainGeneratedEventForwarder implements EventReceiver<TerrainGeneratedEvent> {

        public TerrainGeneratedEventForwarder() {
            super();
        }

        @Override
        public void receive(final TerrainGeneratedEvent event) {
            ExportTerrainEventHandler.this.heightMap = event.getHeightMap();
        }
    }

    private final class ExportTerrainEventForwarder implements EventReceiver<ExportTerrainEvent> {

        public ExportTerrainEventForwarder() {
            super();
        }

        @Override
        public void receive(final ExportTerrainEvent event) {
            ExportTerrainEventHandler.this.exporter.export(ExportTerrainEventHandler.this.heightMap, event.getFilepath());
        }
    }
}
