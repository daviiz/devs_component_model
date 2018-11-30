/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet.FleetAntiAirDefense;

import view.modeling.ViewableAtomic;

public class ShipEquipment_1_1 extends view.modeling.ViewableDigraph {

	// Add Default Constructor
	public ShipEquipment_1_1() {
		this("ShipEquipment_1_1");
	}

	// Add Parameterized Constructor
	public ShipEquipment_1_1(String name) {
		super(name);

// Structure information start
		// Add input port names
		addInport("moveOrder_in");
		addInport("redLoc_in");
		addInport("redDestroyed_in");
		addInport("samOrder_in");
		addInport("ramOrder_in");
		addInport("ciwsOrder_in");

		// Add output port names
		addOutport("shipLoc_out");
		addOutport("redDetect_out");
		addOutport("redDestroyed_out");
		addOutport("samLaunch_out");
		addOutport("ramLaunch_out");
		addOutport("ciwsLaunch_out");

//add test input ports:

		// Initialize sub-components
		ViewableAtomic Radar_1_0 = new Radar_0_0("Radar_1_0");
		ViewableAtomic ShipEngine_1_0 = new ShipEngine_0_0("ShipEngine_1_0");
		ViewableAtomic SAM_1_0 = new SAM_0_0("SAM_1_0");
		ViewableAtomic RAM_1_0 = new RAM_0_0("RAM_1_0");
		ViewableAtomic CIWS_1_0 = new CIWS_0_0("CIWS_1_0");

		// Add sub-components
		add(Radar_1_0);
		add(ShipEngine_1_0);
		add(SAM_1_0);
		add(RAM_1_0);
		add(CIWS_1_0);

		// Add Couplings
		addCoupling(Radar_1_0, "redDestroyed_out", CIWS_1_0, "redDestroyed_in");
		addCoupling(Radar_1_0, "redDestroyed_out", RAM_1_0, "redDestroyed_in");
		addCoupling(Radar_1_0, "redDestroyed_out", SAM_1_0, "redDestroyed_in");
		addCoupling(Radar_1_0, "redDetect_out", CIWS_1_0, "redDetect_in");
		addCoupling(Radar_1_0, "redDetect_out", RAM_1_0, "redDetect_in");
		addCoupling(Radar_1_0, "redDetect_out", SAM_1_0, "redDetect_in");
		addCoupling(ShipEngine_1_0, "shipLoc_out", Radar_1_0, "shipLoc_in");
		addCoupling(this, "ciwsLaunch_out", CIWS_1_0, "ciwsLaunch_out");
		addCoupling(this, "ciwsOrder_in", CIWS_1_0, "ciwsOrder_in");
		addCoupling(this, "moveOrder_in", ShipEngine_1_0, "moveOrder_in");
		addCoupling(this, "ramLaunch_out", RAM_1_0, "ramLaunch_out");
		addCoupling(this, "ramOrder_in", RAM_1_0, "ramOrder_in");
		addCoupling(this, "redDestroyed_in", Radar_1_0, "redDestroyed_in");
		addCoupling(this, "redDestroyed_out", Radar_1_0, "redDestroyed_out");
		addCoupling(this, "redDetect_out", Radar_1_0, "redDetect_out");
		addCoupling(this, "redLoc_in", Radar_1_0, "redLoc_in");
		addCoupling(this, "samLaunch_out", SAM_1_0, "samLaunch_out");
		addCoupling(this, "samOrder_in", SAM_1_0, "samOrder_in");
		addCoupling(this, "shipLoc_out", ShipEngine_1_0, "shipLoc_out");

// Structure information end
		initialize();
	}

}
