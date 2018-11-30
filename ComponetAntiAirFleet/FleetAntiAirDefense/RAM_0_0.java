/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet.FleetAntiAirDefense;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class RAM_0_0 extends ViewableAtomic {

	protected double processing_time;

	protected double intercept_distance;

	protected double intercept_rate = 0.5;

	protected entity ramOderEnt;

	protected entity redDetectEnt;

	protected entity redDestroyedEnt;

	// Add Default Constructor
	public RAM_0_0() {
		this("RAM_0_0");
	}

	// Add Parameterized Constructors
	public RAM_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("ramOrder_in");
		addInport("redDestroyed_in");
		addInport("redDetect_in");

		// Add output port names
		addOutport("ramLaunch_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;

		intercept_distance = 500;
		processing_time = 5;
		ramOderEnt = new entity("nil");
		redDetectEnt = new entity("nil");
		redDestroyedEnt = new entity("nil");
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "ramOrder_in", i)) {
				ramOderEnt = x.getValOnPort("ramOrder_in", i);
			} else if (messageOnPort(x, "redDestroyed_in", i)) {
				redDestroyedEnt = x.getValOnPort("redDestroyed_in", i);
			} else if (messageOnPort(x, "redDetect_in", i)) {
				redDetectEnt = x.getValOnPort("redDetect_in", i);
			}
			if (ramOderEnt.eq("true") && redDestroyedEnt.eq("false") && redDetectEnt.eq("true"))
				holdIn("active", processing_time);
			else
				passivate();
		}
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
		if (phaseIs("active")) {
			if (redDestroyedEnt.eq("false") && redDetectEnt.eq("true") && ramOderEnt.eq("true"))
				con = makeContent("ramLaunch_out", new entity("true"));
			else
				con = makeContent("ramLaunch_out", new entity("false"));

			m.add(con);
			ramOderEnt = new entity("nil");
			redDetectEnt = new entity("nil");
			redDestroyedEnt = new entity("nil");
		}
		return m;
	}

	// Add Show State function
}
