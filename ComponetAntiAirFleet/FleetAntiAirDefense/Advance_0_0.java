
package ComponetAntiAirFleet.FleetAntiAirDefense;

import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Advance_0_0 extends ViewableAtomic {

	protected double processing_time;

	// Add Default Constructor
	public Advance_0_0() {
		this("Advance_0_0");
	}

	// Add Parameterized Constructors
	public Advance_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("advance_in");

		// Add output port names
		addOutport("missileLoc_out");
		addOutport("redLoc_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
	}

	// Add external transition function
	public void deltext(double e, message x) {
	}

	// Add internal transition function
	public void deltint() {
	}

	// Add confluent function
	public void deltcon(double e, message x) {
	}

	// Add output function
	public message out() {
		message m = new message();
		content con = null;

		return m;
	}

	// Add Show State function
}
