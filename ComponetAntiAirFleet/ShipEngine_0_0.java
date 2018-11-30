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
import view.modeling.ViewableAtomic;

public class ShipEngine_0_0 extends ViewableAtomic {

	protected double processing_time;

	protected entity moveOrderEnt;

	// private double x = 0;
	private double y = 2000;

	// Add Default Constructor
	public ShipEngine_0_0() {
		this("ShipEngine_0_0");
	}

	// Add Parameterized Constructors
	public ShipEngine_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("moveOrder_in");

		// Add output port names
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
		moveOrderEnt = new entity("nil");
		processing_time = 3;
		holdIn("moving", processing_time);
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is：" + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		if (phaseIs("moving")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "moveOrder_in", i)) {
					moveOrderEnt = x.getValOnPort("moveOrder_in", i);
					
				}
			}
		}
		/////
		System.out.println("external-Phase after: " + phase);
		

	}

	// Add internal transition function
	public void deltint() {
		System.out.println("====================================");
		System.out.println(this.getClass().getName()+" Internal-Phase before: " + phase);
		/////internal transition specific biz code :
		if(phaseIs("passive")) {
			holdIn("moving", processing_time);
		}
		/////
		System.out.println(this.getClass().getName()+"Internal-Phase after: " + phase);
	}

	// Add confluent function
	/*
	 * public void deltcon(double e, message x){ }
	 */

	// Add output function
	public message out() {
		message m = new message();
		double loc = y - 30;
		content con = null;
		if (phaseIs("moving")) {
			// shipLoc_out
			
			loc = loc > 0 ? loc : 0;
			con = makeContent("shipLoc_out", new entity("" + loc));
			moveOrderEnt = new entity("nil");
		}
		return m;
	}

	// Add Show State function
}
