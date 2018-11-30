/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet.FleetAntiAirDefense;

import model.modeling.content;
import model.modeling.message;

public class Sensor_0_0 extends view.modeling.ViewableAtomic {

	protected double processing_time;

	// Add Default Constructor
	public Sensor_0_0() {
		this("Sensor_0_0");
	}

	// Add Parameterized Constructors
	public Sensor_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("shipLoc_in");
		addInport("samLaunch_in");
		addInport("ramLaunch_in");
		addInport("ciwsLaunch_in");

		// Add output port names
		addOutport("missileExplode_out");
		addOutport("shipLoc_out");

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
