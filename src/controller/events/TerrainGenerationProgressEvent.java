package controller.events;

import hochberger.utilities.eventbus.Event;
import hochberger.utilities.text.i18n.I18N;

public class TerrainGenerationProgressEvent implements Event {

    private final int progressPercentage;
    private final I18N stage;

    public TerrainGenerationProgressEvent(final I18N stage, final int progressPercentage) {
        super();
        this.stage = stage;
        this.progressPercentage = progressPercentage;
    }

    @Override
    public void performEvent() {

    }

    public I18N getStage() {
        return this.stage;
    }

    public int getProgressPercentage() {
        return this.progressPercentage;
    }
}
