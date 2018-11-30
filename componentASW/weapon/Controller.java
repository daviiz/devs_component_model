/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.weapon;

import componentASW.weapon.Controller_Actor;
import componentASW.weapon.Controller_Updater;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

class Controller extends ViewableDigraph {

	protected double processing_time;

	// Add Default Constructor
	public Controller() {
		this("wpController");
	}

	// Add Parameterized Constructors
	public Controller(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("move_finished");
		addInport("threat_info");
		addInport("scen_info");
		addInport("engage_result");
		addInport("wp_guidance");

		// Add output port names
		addOutport("move_cmd");

//add test input ports:

		// Initialize sub-components
        ViewableAtomic updater =  new Controller_Updater("w_updater");
        ViewableAtomic actor =  new Controller_Actor("w_actor");

        // Add sub-components
        add(updater);
        add(actor);

        // Add Couplings
        addCoupling(this,"threat_info",updater,"threat_info");
        addCoupling(this,"scen_info",updater,"scen_info");
        addCoupling(this,"scen_info",actor,"scen_info");
        addCoupling(this,"move_finished",actor,"move_finished");
        addCoupling(this,"engage_result",actor,"engage_result");
        addCoupling(this,"wp_guidance",actor,"wp_guidance");
        
        addCoupling(updater,"target_info",actor,"target_info");
        
        addCoupling(actor,"move_cmd",this,"move_cmd");
        
        
// Structure information end
		initialize();
	}

}
