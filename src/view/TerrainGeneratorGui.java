package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.gui.ApplicationGui;

public class TerrainGeneratorGui extends SessionBasedObject implements ApplicationGui {

    private TerrainGeneratorMainFrame mainFrame;

    public TerrainGeneratorGui(final BasicSession session) {
        super(session);
    }

    @Override
    public void activate() {
        this.mainFrame = new TerrainGeneratorMainFrame(session().getProperties().title());
        this.mainFrame.show();
        this.mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent arg0) {
                super.windowClosed(arg0);
                session().getEventBus().publishFromEDT(new ApplicationShutdownEvent());
            }
        });
    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
    }
}
