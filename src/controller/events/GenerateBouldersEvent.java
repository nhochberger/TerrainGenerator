package controller.events;

import hochberger.utilities.eventbus.Event;

public class GenerateBouldersEvent implements Event {

    private final double boulderAmountFactor;

    public GenerateBouldersEvent(final double boulderAmountFactor) {
        super();
        this.boulderAmountFactor = boulderAmountFactor;
    }

    public double getBoulderAmountFactor() {
        return this.boulderAmountFactor;
    }

    @Override
    public void performEvent() {
    }
}
