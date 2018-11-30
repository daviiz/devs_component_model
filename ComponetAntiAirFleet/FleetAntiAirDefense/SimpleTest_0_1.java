/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet.FleetAntiAirDefense;

import view.modeling.ViewableDigraph;

public class SimpleTest_0_1 extends view.modeling.ViewableDigraph {

	// Add Default Constructor
	public SimpleTest_0_1() {
		this("SimpleTest_0_1");
	}

	// Add Parameterized Constructor
	public SimpleTest_0_1(String name) {
		super(name);

// Structure information start
		// Add input port names

		// Add output port names

//add test input ports:

		// Initialize sub-components
		ViewableDigraph WarShip_1_1 = new WarShip_1_1("WarShip_1_1");
		ViewableDigraph WarShip_2_2 = new WarShip_1_1("WarShip_2_2");
		ViewableDigraph WarShip_3_3 = new WarShip_1_1("WarShip_3_3");
		ViewableDigraph RedMissile_1_1 = new RedMissile_1_1("RedMissile_1_1");
		ViewableDigraph RedMissile_2_2 = new RedMissile_1_1("RedMissile_2_2");
		ViewableDigraph RedMissile_3_3 = new RedMissile_1_1("RedMissile_3_3");
		ViewableDigraph RedMissile_4_4 = new RedMissile_1_1("RedMissile_4_4");
		ViewableDigraph RedMissile_5_5 = new RedMissile_1_1("RedMissile_5_5");
		ViewableDigraph RedMissile_6_6 = new RedMissile_1_1("RedMissile_6_6");
		ViewableDigraph RedMissile_7_7 = new RedMissile_1_1("RedMissile_7_7");
		ViewableDigraph RedMissile_8_8 = new RedMissile_1_1("RedMissile_8_8");

		// Add sub-components
		add(WarShip_1_1);
		add(WarShip_2_2);
		add(WarShip_3_3);
		add(RedMissile_1_1);
		add(RedMissile_2_2);
		add(RedMissile_3_3);
		add(RedMissile_4_4);
		add(RedMissile_5_5);
		add(RedMissile_6_6);
		add(RedMissile_7_7);
		add(RedMissile_8_8);

		// Add Couplings
		addCoupling(RedMissile_1_1, "redDestroyed_out", WarShip_1_1, "redDestroyed_in");
		addCoupling(RedMissile_1_1, "redLoc_out", WarShip_1_1, "redLoc_in");
		addCoupling(RedMissile_2_2, "redDestroyed_out", WarShip_1_1, "redDestroyed_in");
		addCoupling(RedMissile_2_2, "redLoc_out", WarShip_1_1, "redLoc_in");
		addCoupling(RedMissile_3_3, "redDestroyed_out", WarShip_1_1, "redDestroyed_in");
		addCoupling(RedMissile_3_3, "redLoc_out", WarShip_1_1, "redLoc_in");
		addCoupling(RedMissile_4_4, "redDestroyed_out", WarShip_3_3, "redDestroyed_in");
		addCoupling(RedMissile_4_4, "redLoc_out", WarShip_3_3, "redLoc_in");
		addCoupling(RedMissile_5_5, "redDestroyed_out", WarShip_2_2, "redDestroyed_in");
		addCoupling(RedMissile_5_5, "redLoc_out", WarShip_2_2, "redLoc_in");
		addCoupling(RedMissile_6_6, "redDestroyed_out", WarShip_3_3, "redDestroyed_in");
		addCoupling(RedMissile_6_6, "redLoc_out", WarShip_3_3, "redLoc_in");
		addCoupling(RedMissile_7_7, "redDestroyed_out", WarShip_2_2, "redDestroyed_in");
		addCoupling(RedMissile_7_7, "redLoc_out", WarShip_2_2, "redLoc_in");
		addCoupling(RedMissile_8_8, "redDestroyed_out", WarShip_3_3, "redDestroyed_in");
		addCoupling(RedMissile_8_8, "redLoc_out", WarShip_3_3, "redLoc_in");
		addCoupling(WarShip_1_1, "ciwsLaunch_out", RedMissile_1_1, "ciwsLaunch_in");
		addCoupling(WarShip_1_1, "ciwsLaunch_out", RedMissile_2_2, "ciwsLaunch_in");
		addCoupling(WarShip_1_1, "ciwsLaunch_out", RedMissile_3_3, "ciwsLaunch_in");
		addCoupling(WarShip_1_1, "ramLaunch_out", RedMissile_1_1, "ramLaunch_in");
		addCoupling(WarShip_1_1, "ramLaunch_out", RedMissile_2_2, "ramLaunch_in");
		addCoupling(WarShip_1_1, "ramLaunch_out", RedMissile_3_3, "ramLaunch_in");
		addCoupling(WarShip_1_1, "samLaunch_out", RedMissile_1_1, "samLaunch_in");
		addCoupling(WarShip_1_1, "samLaunch_out", RedMissile_2_2, "samLaunch_in");
		addCoupling(WarShip_1_1, "samLaunch_out", RedMissile_3_3, "samLaunch_in");
		addCoupling(WarShip_1_1, "shipLoc_out", RedMissile_1_1, "shipLoc_in");
		addCoupling(WarShip_1_1, "shipLoc_out", RedMissile_2_2, "shipLoc_in");
		addCoupling(WarShip_1_1, "shipLoc_out", RedMissile_3_3, "shipLoc_in");
		addCoupling(WarShip_2_2, "ciwsLaunch_out", RedMissile_5_5, "ciwsLaunch_in");
		addCoupling(WarShip_2_2, "ramLaunch_out", RedMissile_5_5, "ramLaunch_in");
		addCoupling(WarShip_2_2, "ramLaunch_out", RedMissile_7_7, "ciwsLaunch_in");
		addCoupling(WarShip_2_2, "samLaunch_out", RedMissile_5_5, "samLaunch_in");
		addCoupling(WarShip_2_2, "samLaunch_out", RedMissile_7_7, "ramLaunch_in");
		addCoupling(WarShip_2_2, "samLaunch_out", RedMissile_7_7, "samLaunch_in");
		addCoupling(WarShip_2_2, "shipLoc_out", RedMissile_5_5, "shipLoc_in");
		addCoupling(WarShip_2_2, "shipLoc_out", RedMissile_7_7, "shipLoc_in");
		addCoupling(WarShip_3_3, "ciwsLaunch_out", RedMissile_4_4, "ciwsLaunch_in");
		addCoupling(WarShip_3_3, "ciwsLaunch_out", RedMissile_6_6, "ciwsLaunch_in");
		addCoupling(WarShip_3_3, "ciwsLaunch_out", RedMissile_8_8, "ciwsLaunch_in");
		addCoupling(WarShip_3_3, "ramLaunch_out", RedMissile_4_4, "ramLaunch_in");
		addCoupling(WarShip_3_3, "ramLaunch_out", RedMissile_6_6, "ramLaunch_in");
		addCoupling(WarShip_3_3, "ramLaunch_out", RedMissile_8_8, "ramLaunch_in");
		addCoupling(WarShip_3_3, "samLaunch_out", RedMissile_4_4, "samLaunch_in");
		addCoupling(WarShip_3_3, "samLaunch_out", RedMissile_6_6, "samLaunch_in");
		addCoupling(WarShip_3_3, "samLaunch_out", RedMissile_8_8, "samLaunch_in");
		addCoupling(WarShip_3_3, "shipLoc_out", RedMissile_4_4, "shipLoc_in");
		addCoupling(WarShip_3_3, "shipLoc_out", RedMissile_6_6, "shipLoc_in");
		addCoupling(WarShip_3_3, "shipLoc_out", RedMissile_8_8, "shipLoc_in");

// Structure information end
		initialize();
	}

}
