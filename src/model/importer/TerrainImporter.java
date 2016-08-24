package model.importer;

import controller.events.ImportTerrainEvent;
import hochberger.utilities.eventbus.EventReceiver;

public interface TerrainImporter extends EventReceiver<ImportTerrainEvent> {
    public void importTerrain(String filepath);
}
