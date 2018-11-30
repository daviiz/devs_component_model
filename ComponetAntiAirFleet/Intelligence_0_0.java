
package ComponetAntiAirFleet;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Intelligence_0_0 extends ViewableAtomic {

	protected double processing_time;

	protected entity redDetectEnt;

	protected entity redDestroyedEnt;

	// Add Default Constructor
	public Intelligence_0_0() {
		this("Intelligence_0_0");
	}

	// Add Parameterized Constructors
	public Intelligence_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("redDetect_in");
		addInport("redDestroyed_in");

		// Add output port names
		addOutport("redDetectReport_out");
		addOutport("redDestroyedReport_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		processing_time = 2;
		redDetectEnt = new entity("nil");
		redDestroyedEnt = new entity("nil");

	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is" + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "redDetect_in", i)) {
				redDetectEnt = x.getValOnPort("redDetect_in", i);
			} else if (messageOnPort(x, "redDestroyed_in", i)) {
				redDestroyedEnt = x.getValOnPort("redDestroyed_in", i);
			}

			holdIn("active", processing_time);
		}
		/////
		System.out.println("external-Phase after: " + phase);
		
	}

	// Add internal transition function
	public void deltint() {
		if (phaseIs("passive"))
			holdIn("active", processing_time);
	}

	// Add confluent function
	public void deltcon(double e, message x) {
		System.out.println("confluent");
		deltint();
		deltext(0, x);
	}

	// Add output function
	public message out() {
		message m = new message();
		content con = null;
		if (phaseIs("active")) {
			con = makeContent("redDetectReport_out", redDetectEnt);
			m.add(con);
			con = makeContent("redDestroyedReport_out", redDestroyedEnt);
			m.add(con);
		}
		return m;
	}

	// Add Show State function
}
