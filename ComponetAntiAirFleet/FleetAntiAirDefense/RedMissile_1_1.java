/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet.FleetAntiAirDefense;

import view.modeling.ViewableAtomic;

public class RedMissile_1_1 extends view.modeling.ViewableDigraph {

	// Add Default Constructor
	public RedMissile_1_1() {
		this("RedMissile_1_1");
	}

	// Add Parameterized Constructor
	public RedMissile_1_1(String name) {
		super(name);

// Structure information start
		// Add input port names
		addInport("shipLoc_in");
		addInport("samLaunch_in");
		addInport("ramLaunch_in");
		addInport("ciwsLaunch_in");

		// Add output port names
		addOutport("redLoc_out");
		addOutport("redDestroyed_out");

//add test input ports:

		// Initialize sub-components
		ViewableAtomic Sensor_1_0 = new Sensor_0_0("Sensor_1_0");
		ViewableAtomic RedController_1_0 = new RedController_0_0("RedController_1_0");
		ViewableAtomic Advance_1_0 = new Advance_0_0("Advance_1_0");

		// Add sub-components
		add(Sensor_1_0);
		add(RedController_1_0);
		add(Advance_1_0);

		// Add Couplings
		addCoupling(Advance_1_0, "missileLoc_out", RedController_1_0, "missileLoc_in");
		addCoupling(RedController_1_0, "advance_out", Advance_1_0, "advance_in");
		addCoupling(this, "ciwsLaunch_in", Sensor_1_0, "ciwsLaunch_in");
		addCoupling(this, "ramLaunch_in", Sensor_1_0, "ramLaunch_in");
		addCoupling(this, "redDestroyed_out", RedController_1_0, "redDestroyed_out");
		addCoupling(this, "redLoc_out", Advance_1_0, "redLoc_out");
		addCoupling(this, "samLaunch_in", Sensor_1_0, "samLaunch_in");
		addCoupling(this, "shipLoc_in", Sensor_1_0, "shipLoc_in");
		addCoupling(Sensor_1_0, "missileExplode_out", RedController_1_0, "missileExplode_in");
		addCoupling(Sensor_1_0, "shipLoc_out", RedController_1_0, "shipLoc_in");

// Structure information end
		initialize();
	}

}
