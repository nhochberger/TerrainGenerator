package view;

import controller.events.DemManipulationFinishedEvent;
import controller.events.ImportFinishedEvent;
import controller.events.TerrainGeneratedEvent;
import controller.events.TerrainGenerationProgressEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ApplicationGui;
import hochberger.utilities.gui.WindowClosedApplicationShutdownEventPublisher;
import hochberger.utilities.text.i18n.DirectI18N;

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
        session().getEventBus().register(new TerrainImportedEventForwarder(), ImportFinishedEvent.class);
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

    public class TerrainImportedEventForwarder implements EventReceiver<ImportFinishedEvent> {

        public TerrainImportedEventForwarder() {
            super();
        }

        @Override
        public void receive(final ImportFinishedEvent event) {
            TerrainGeneratorGui.this.mainFrame.setHeightMap(event.getHeightMap());
            TerrainGeneratorGui.this.mainFrame.setStage(new DirectI18N("Waiting"));
        }

    }

    public class DemManipulationFinishedEventForwarder implements EventReceiver<DemManipulationFinishedEvent> {

        public DemManipulationFinishedEventForwarder() {
            super();
        }

        @Override
        public void receive(final DemManipulationFinishedEvent event) {
            TerrainGeneratorGui.this.mainFrame.setHeightMap(event.getSurfaceMap());
            TerrainGeneratorGui.this.mainFrame.setStage(new DirectI18N("Waiting"));
        }
    }

    public class TerrainGenerationProgressForwarder implements EventReceiver<TerrainGenerationProgressEvent> {

        public TerrainGenerationProgressForwarder() {
            super();
        }

        @Override
        public void receive(final TerrainGenerationProgressEvent event) {
            TerrainGeneratorGui.this.mainFrame.setStage(event.getStage());
            TerrainGeneratorGui.this.mainFrame.setProgress(event.getProgressPercentage());
        }
    }
}
