package org.tiwindetea.animewarfare.net.logicevent;

public class NumberOfUnitiesToReconfortEvent extends ActionEvent<NumberOfUnitiesToReconfortEventListener> {
	private final int number;

	public NumberOfUnitiesToReconfortEvent(int playerID, int number) {
		super(playerID);

		this.number = number;
	}

	@Override
	public void notify(NumberOfUnitiesToReconfortEventListener listener) {
		listener.handleNumberOfUnitiesToReconfortEvent(this);
	}

	public int getNumber() {
		return this.number;
	}
}
