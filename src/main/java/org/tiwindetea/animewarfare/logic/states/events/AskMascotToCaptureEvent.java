package org.tiwindetea.animewarfare.logic.states.events;

public class AskMascotToCaptureEvent extends RemoveUnitEvent<AskUnitToCaptureEventListener> {
	public AskMascotToCaptureEvent(int player) {
		super(player, 1);
	}

	@Override
	public void notify(AskUnitToCaptureEventListener listener) {
		listener.askUnitToCaptureEvent(this);
	}
}
