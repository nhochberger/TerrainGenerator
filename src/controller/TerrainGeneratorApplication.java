package controller;

import controller.events.GenerateTerrainEvent;
import controller.events.ImportTerrainEvent;
import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.ApplicationShutdownEventReceiver;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import model.Eroder;
import model.HeightMapGenerator;
import model.NaturalSurfaceDiamondSquareGenerator;
import model.ObstacleGenerator;
import model.RandomBoulderGenerator;
import model.ThermalEroder;
import model.serialization.exporter.CSVHeightMapExporter;
import model.serialization.importer.CSVTerrainImporter;
import view.TerrainGeneratorGui;

public class TerrainGeneratorApplication extends BasicLoggedApplication {

    private final BasicSession session;
    private final TerrainGeneratorGui gui;
    private final HeightMapGenerator generator;
    private final ExportTerrainEventHandler exportHandler;
    private final ExistingDemManipulationHandler demManipulationHandler;

    public static void main(final String[] args) {
        setUpLoggingServices(TerrainGeneratorApplication.class);
        try {
            final ApplicationProperties applicationProperties = new ApplicationProperties();
            final TerrainGeneratorApplication application = new TerrainGeneratorApplication(applicationProperties);
            application.start();
        } catch (final Exception e) {
            getLogger().fatal("Error while starting application. Shutting down.", e);
            System.exit(0);
        }
    }

    public TerrainGeneratorApplication(final ApplicationProperties applicationProperties) {
        this.session = new BasicSession(applicationProperties, new SimpleEventBus(), getLogger());
        this.gui = new TerrainGeneratorGui(this.session);
        final Eroder eroder = new ThermalEroder(this.session);
        final ObstacleGenerator obstacleGenerator = new RandomBoulderGenerator(this.session);
        this.generator = new NaturalSurfaceDiamondSquareGenerator(this.session, eroder, obstacleGenerator);
        this.demManipulationHandler = new ExistingDemManipulationHandler(this.session, eroder, obstacleGenerator);
        this.exportHandler = new ExportTerrainEventHandler(this.session, new CSVHeightMapExporter(this.session));
    }

    @Override
    public void start() {
        super.start();
        logger().info("TerrainGenerator " + this.session.getProperties().version());
        this.session.getEventBus().register(new ApplicationShutdownEventReceiver(this.session, this), ApplicationShutdownEvent.class);
        this.session.getEventBus().register(new GenerateTerrainEventForwarder(this.session, this.generator), GenerateTerrainEvent.class);
        this.session.getEventBus().register(new CSVTerrainImporter(this.session), ImportTerrainEvent.class);
        this.exportHandler.start();
        this.demManipulationHandler.start();
        this.gui.activate();
    }

    @Override
    public void stop() {
        this.demManipulationHandler.stop();
        this.exportHandler.stop();
        this.gui.deactivate();
        super.stop();
    }
}
