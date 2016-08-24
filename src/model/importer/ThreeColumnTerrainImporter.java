package model.importer;

import controller.events.ImportTerrainEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

public class ThreeColumnTerrainImporter extends SessionBasedObject implements TerrainImporter {

    public ThreeColumnTerrainImporter(final BasicSession session) {
        super(session);
    }

    @Override
    public void importTerrain(final String filepath) {
        // TODO Auto-generated method stub
    }

    @Override
    public void receive(final ImportTerrainEvent event) {
        importTerrain(event.getFilePath());
    }
}
