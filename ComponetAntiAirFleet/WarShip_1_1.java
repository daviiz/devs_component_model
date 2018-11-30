/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet;

import view.modeling.ViewableDigraph;

public class WarShip_1_1 extends view.modeling.ViewableDigraph {

	// Add Default Constructor
	public WarShip_1_1() {
		this("WarShip_1_1");
	}

	// Add Parameterized Constructor
	public WarShip_1_1(String name) {
		super(name);

// Structure information start
		// Add input port names
		addInport("redLoc_in");
		addInport("redDestroyed_in");

		// Add output port names
		addOutport("shipLoc_out");
		addOutport("samLaunch_out");
		addOutport("ramLaunch_out");
		addOutport("ciwsLaunch_out");

//add test input ports:

		// Initialize sub-components
		ViewableDigraph ShipEquipment_1_1 = new ShipEquipment_1_1("ShipEquipment_1_1");
		ViewableDigraph ShipC2_1_1 = new ShipC2_1_1("ShipC2_1_1");

		// Add sub-components
		add(ShipEquipment_1_1);
		add(ShipC2_1_1);

		// Add Couplings
		addCoupling(ShipC2_1_1, "ciwsOrder_out", ShipEquipment_1_1, "ciwsOrder_in");
		addCoupling(ShipC2_1_1, "moveOrder_out", ShipEquipment_1_1, "moveOrder_in");
		addCoupling(ShipC2_1_1, "ramOrder_out", ShipEquipment_1_1, "ramOrder_in");
		addCoupling(ShipC2_1_1, "samOrder_out", ShipEquipment_1_1, "samOrder_in");
		addCoupling(ShipEquipment_1_1, "redDestroyed_out", ShipC2_1_1, "redDestroyed_in");
		addCoupling(ShipEquipment_1_1, "redDetect_out", ShipC2_1_1, "redDetect_in");
		addCoupling(this, "ciwsLaunch_out", ShipEquipment_1_1, "ciwsLaunch_out");
		addCoupling(this, "ramLaunch_out", ShipEquipment_1_1, "ramLaunch_out");
		addCoupling(this, "redDestroyed_in", ShipEquipment_1_1, "redDestroyed_in");
		addCoupling(this, "redLoc_in", ShipEquipment_1_1, "redLoc_in");
		addCoupling(this, "samLaunch_out", ShipEquipment_1_1, "samLaunch_out");
		addCoupling(this, "shipLoc_out", ShipEquipment_1_1, "shipLoc_out");

// Structure information end
		initialize();
	}

}
