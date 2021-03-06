/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;

public class SAM_0_0 extends view.modeling.ViewableAtomic {

	protected double processing_time;

	protected double intercept_distance;

	protected double intercept_rate = 0.4;

	protected entity samOderEnt;

	protected entity redDetectEnt;

	protected entity redDestroyedEnt;

	// Add Default Constructor
	public SAM_0_0() {
		this("SAM_0_0");
	}

	// Add Parameterized Constructors
	public SAM_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("samOrder_in");
		addInport("redDetect_in");
		addInport("redDestroyed_in");

		// Add output port names
		addOutport("samLaunch_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		intercept_distance = 1000;
		processing_time = 5;
		samOderEnt = new entity("nil");
		redDetectEnt = new entity("nil");
		redDestroyedEnt = new entity("nil");
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is：" + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "samOrder_in", i)) {
				samOderEnt = x.getValOnPort("samOrder_in", i);
			} else if (messageOnPort(x, "redDestroyed_in", i)) {
				redDestroyedEnt = x.getValOnPort("redDestroyed_in", i);
			} else if (messageOnPort(x, "redDetect_in", i)) {
				redDetectEnt = x.getValOnPort("redDetect_in", i);
			}
			if (samOderEnt.eq("true") && redDestroyedEnt.eq("false") && redDetectEnt.eq("true"))
				holdIn("active", processing_time);
			else
				passivate();
		}
		/////
		System.out.println("external-Phase after: " + phase);

	}

	// Add internal transition function
	public void deltint() {
		System.out.println("====================================");
		System.out.println(this.getClass().getName()+" Internal-Phase before: " + phase);
		
		System.out.println(this.getClass().getName()+" Internal-Phase after: " + phase);
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
			if (redDestroyedEnt.eq("false") && redDetectEnt.eq("true") && samOderEnt.eq("true"))
				con = makeContent("samLaunch_out", new entity("true"));
			else
				con = makeContent("samLaunch_out", new entity("false"));

			m.add(con);
			samOderEnt = new entity("nil");
			redDetectEnt = new entity("nil");
			redDestroyedEnt = new entity("nil");
		}
		return m;
	}

	// Add Show State function
}
