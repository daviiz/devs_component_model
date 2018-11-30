/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.platform;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

class Controller extends ViewableDigraph{

	// Add Default Constructor
    public Controller(){
        this("Controller");
    }

    // Add Parameterized Constructor
    public Controller(String name){
        super(name);

// Structure information start
     // Add input port names
        addInport("move_finished");
        addInport("engage_result");
        addInport("scen_info");
        addInport("threat_info");
        addInport("guidance_info");

        // Add output port names
        addOutport("move_cmd");
        addOutport("wp_launch");
        addOutport("wp_guidance");

//add test input ports:

        // Initialize sub-components
        ViewableAtomic updater =  new Controller_Updater("c_updater");
        ViewableAtomic actor =  new Controller_Actor("c_actor");

        // Add sub-components
        add(updater);
        add(actor);

        // Add Couplings
        addCoupling(this, "move_finished", actor, "move_finished");
        addCoupling(this, "engage_result", actor, "engage_result");
        addCoupling(this, "scen_info", actor, "scen_info");
        addCoupling(this, "scen_info", updater, "scen_info");
        addCoupling(this,"threat_info",updater,"threat_info");
        addCoupling(this,"guidance_info",actor,"guidance_info");
        
        addCoupling(updater, "target_info", actor, "target_info");
        
        addCoupling(actor,"move_cmd",this,"move_cmd");
        addCoupling(actor,"wp_launch",this,"wp_launch");
        addCoupling(actor,"wp_guidance",this,"wp_guidance");

// Structure information end
        initialize();
        }

    
}

