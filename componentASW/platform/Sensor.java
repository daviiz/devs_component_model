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

public class Sensor extends ViewableDigraph{

    protected double processing_time;

    // Add Default Constructor
    public Sensor(){
        this("Sensor")  ;  }

    // Add Parameterized Constructors
    public Sensor(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("move_result");
        addInport("scen_info");
        addInport("env_info");
        addInport("engage_result");

        // Add output port names
        addOutport("threat_info");

//add test input ports:
        
        // Initialize sub-components
		ViewableAtomic updater = new Sensor_Updater("s_updater");
		ViewableAtomic actor = new Sensor_Actor("s_actor");
		
		// Add sub-components
		add(updater);
		add(actor);
		
		// Add Couplings
		addCoupling(this, "engage_result", actor, "engage_result");
		addCoupling(this, "scen_info", actor, "scen_info");
		addCoupling(this, "env_info", actor, "env_info");
		addCoupling(this, "move_result", updater, "move_result");
		
		
		addCoupling(updater, "response", actor, "response");
		addCoupling(actor, "request", updater, "request");
		
		addCoupling(actor, "threat_info", this, "threat_info");

// Structure information end
        initialize();
    }

}

