package controller;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.ApplicationShutdownEventReceiver;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import view.TerrainGeneratorGui;

public class TerrainGeneratorApplication extends BasicLoggedApplication {

    private final BasicSession session;
    private final TerrainGeneratorGui gui;

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
    }

    @Override
    public void start() {
        super.start();
        this.session.getEventBus().register(new ApplicationShutdownEventReceiver(this.session, this), ApplicationShutdownEvent.class);
        this.gui.activate();
    }

    @Override
    public void stop() {
        this.gui.deactivate();
        super.stop();
    }

}
