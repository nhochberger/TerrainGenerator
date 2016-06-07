package view;

import controller.events.TerrainGeneratedEvent;
import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ApplicationGui;
import hochberger.utilities.gui.WindowClosedApplicationShutdownEventPublisher;

public class TerrainGeneratorGui extends SessionBasedObject implements ApplicationGui {

    private TerrainGeneratorMainFrame mainFrame;

    public TerrainGeneratorGui(final BasicSession session) {
        super(session);
    }

    @Override
    public void activate() {
        this.mainFrame = new TerrainGeneratorMainFrame(session());
        this.mainFrame.show();
        this.mainFrame.addWindowListener(new WindowClosedApplicationShutdownEventPublisher(session()));
        session().getEventBus().register(new TerrainGenerationProgressForwarder(), TerrainGenerationProgressEvent.class);
        session().getEventBus().register(new TerrainGeneratedEventForwarder(), TerrainGeneratedEvent.class);
    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
    }

    public class TerrainGeneratedEventForwarder implements EventReceiver<TerrainGeneratedEvent> {

        public TerrainGeneratedEventForwarder() {
            super();
        }

        @Override
        public void receive(final TerrainGeneratedEvent event) {
            TerrainGeneratorGui.this.mainFrame.setHeightMap(event.getHeightMap());
        }
    }

    public class TerrainGenerationProgressForwarder implements EventReceiver<TerrainGenerationProgressEvent> {

        public TerrainGenerationProgressForwarder() {
            super();
        }

        @Override
        public void receive(final TerrainGenerationProgressEvent event) {
            TerrainGeneratorGui.this.mainFrame.setProgress(event.getProgressPercentage());
        }
    }
}
