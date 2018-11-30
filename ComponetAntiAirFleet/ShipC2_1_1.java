/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet;

import view.modeling.ViewableAtomic;

public class ShipC2_1_1 extends view.modeling.ViewableDigraph {

	// Add Default Constructor
	public ShipC2_1_1() {
		this("ShipC2_1_1");
	}

	// Add Parameterized Constructor
	public ShipC2_1_1(String name) {
		super(name);

// Structure information start
		// Add input port names
		addInport("redDetect_in");
		addInport("redDestroyed_in");

		// Add output port names
		addOutport("moveOrder_out");
		addOutport("samOrder_out");
		addOutport("ramOrder_out");
		addOutport("ciwsOrder_out");

//add test input ports:

		// Initialize sub-components
		ViewableAtomic Intelligence_1_0 = new Intelligence_0_0("Intelligence_1_0");
		ViewableAtomic Commander_1_0 = new Commander_0_0("Commander_1_0");
		ViewableAtomic FireController_1_0 = new FireController_0_0("FireController_1_0");

		// Add sub-components
		add(Intelligence_1_0);
		add(Commander_1_0);
		add(FireController_1_0);

		// Add Couplings
		addCoupling(Commander_1_0, "ciwsLaunchOrder_out", FireController_1_0, "ciwsLaunchOrder_in");
		addCoupling(Commander_1_0, "ramLaunchOrder_out", FireController_1_0, "ramLaunchOrder_in");
		addCoupling(Commander_1_0, "samLaunchOrder_out", FireController_1_0, "samLaunchOrder_in");
		addCoupling(Intelligence_1_0, "redDestroyedReport_out", Commander_1_0, "redDestroyedReport_in");
		addCoupling(Intelligence_1_0, "redDetectReport_out", Commander_1_0, "redDetectReport_in");
		addCoupling(this, "ciwsOrder_out", FireController_1_0, "ciwsOrder_out");
		addCoupling(this, "moveOrder_out", Commander_1_0, "moveOrder_out");
		addCoupling(this, "ramOrder_out", FireController_1_0, "ramOrder_out");
		addCoupling(this, "redDestroyed_in", Intelligence_1_0, "redDestroyed_in");
		addCoupling(this, "redDetect_in", Intelligence_1_0, "redDetect_in");
		addCoupling(this, "samOrder_out", FireController_1_0, "samOrder_out");

// Structure information end
		initialize();
	}

}
