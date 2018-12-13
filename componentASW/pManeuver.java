/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW;

import java.awt.Dimension;
import java.awt.Point;

import componentASW.platform.Maneuver_Actor;
import componentASW.platform.Maneuver_Updater;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class pManeuver extends ViewableDigraph {

	// Add Default Constructor
	public pManeuver() {
		this("pManeuver_0_0");
	}

	// Add Parameterized Constructors
	public pManeuver(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("scen_info");
		addInport("env_info");
		addInport("engage_result");
		addInport("move_cmd");

		// Add output port names
		addOutport("move_finished");
		addOutport("move_result");
		addOutport("fuel_exhausted");

//add test input ports:

		// Initialize sub-components
		ViewableAtomic updater = new Maneuver_Updater("updater");
		ViewableAtomic actor = new Maneuver_Actor("actor");

		// Add sub-components
		add(updater);
		add(actor);

		// Add Couplings
		addCoupling(this, "move_cmd", updater, "move_cmd");
		addCoupling(this, "scen_info", actor, "scen_info");
		addCoupling(this, "env_info", actor, "env_info");
		addCoupling(this, "engage_result", actor, "engage_result");

		addCoupling(updater, "cmd_info", actor, "cmd_info");

		addCoupling(actor, "move_finished", this, "move_finished");
		addCoupling(actor, "move_result", this, "move_result");
		addCoupling(actor, "fuel_exhausted", this, "fuel_exhausted");

// Structure information end
		initialize();
	}
	public void layoutForSimView() {
		preferredSize = new Dimension(550, 120);
		((ViewableComponent) withName("updater")).setPreferredLocation(new Point(-60, 15));
		((ViewableComponent) withName("actor")).setPreferredLocation(new Point(100, 45));
	}

}
