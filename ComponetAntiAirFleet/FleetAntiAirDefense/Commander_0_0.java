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
import view.modeling.ViewableAtomic;

public class Commander_0_0 extends ViewableAtomic {

	protected double processing_time;

	// Add Default Constructor
	public Commander_0_0() {
		this("Commander_0_0");
	}

	// Add Parameterized Constructors
	public Commander_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("redDetectReport_in");
		addInport("redDestroyedReport_in");

		// Add output port names
		addOutport("moveOrder_out");
		addOutport("samLaunchOrder_out");
		addOutport("ramLaunchOrder_out");
		addOutport("ciwsLaunchOrder_out");

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
