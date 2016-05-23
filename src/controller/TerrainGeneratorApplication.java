package controller;

import controller.events.GenerateTerrainEvent;
import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.ApplicationShutdownEventReceiver;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import model.HeightMapGenerator;
import view.TerrainGeneratorGui;

public class TerrainGeneratorApplication extends BasicLoggedApplication {

    private final BasicSession session;
    private final TerrainGeneratorGui gui;
    private final HeightMapGenerator generator;

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
        this.generator = new HeightMapGenerator(this.session);
    }

    @Override
    public void start() {
        super.start();
        this.session.getEventBus().register(new ApplicationShutdownEventReceiver(this.session, this), ApplicationShutdownEvent.class);
        this.session.getEventBus().register(new GenerateTerrainEventForwarder(this.session, this.generator), GenerateTerrainEvent.class);
        this.gui.activate();
    }

    @Override
    public void stop() {
        this.gui.deactivate();
        super.stop();
    }
}
