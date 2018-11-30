/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.platform;

import view.modeling.ViewableDigraph;

public class Platform extends ViewableDigraph{

    // Add Default Constructor
    public Platform(){
        this("Platform");
    }

    // Add Parameterized Constructor
    public Platform(String name){
        super(name);

// Structure information start
        // Add input port names
        addInport("scen_info");
        addInport("engage_result");
        addInport("move_result");
        addInport("env_info");
        addInport("guidance_info");

        // Add output port names
        addOutport("move_result");
        addOutport("wp_launch");
        addOutport("wp_guidance");

//add test input ports:

        // Initialize sub-components
        ViewableDigraph pManeuver =  new Maneuver("pManeuver");
        ViewableDigraph pController =  new Controller("pController");
        ViewableDigraph pSensor =  new Sensor("pSensor");

        // Add sub-components
        add(pManeuver);
        add(pController);
        add(pSensor);

        // Add Couplings
       
        addCoupling(this, "engage_result", pController, "engage_result");
        addCoupling(this, "engage_result", pManeuver, "engage_result");
        addCoupling(this, "engage_result", pSensor, "engage_result");
        addCoupling(this, "env_info", pManeuver, "env_info");
        addCoupling(this, "env_info", pSensor, "env_info");
        addCoupling(this, "guidance_info", pController, "guidance_info");
        addCoupling(this, "move_result", pSensor, "move_result");
        addCoupling(this, "move_result", pManeuver, "move_result");
        addCoupling(this, "scen_info", pController, "scen_info");
        addCoupling(this, "scen_info", pManeuver, "scen_info");
        addCoupling(this, "scen_info", pSensor, "scen_info");
        addCoupling(this, "wp_guidance", pController, "wp_guidance");
        addCoupling(this, "wp_launch", pController, "wp_launch");
        
        addCoupling(pSensor, "threat_info", pController, "threat_info");
        
        addCoupling(pController, "move_cmd", pManeuver, "move_cmd");
        addCoupling(pController, "wp_launch", this, "wp_launch");
        addCoupling(pController, "wp_guidance", this, "wp_guidance");
        
        addCoupling(pManeuver, "fuel_exhausted", pController, "engage_result");
        addCoupling(pManeuver, "fuel_exhausted", pSensor, "engage_result");
        addCoupling(pManeuver, "move_finished", pController, "move_finished");
        addCoupling(pManeuver, "move_result", this, "move_result");
        

// Structure information end
        initialize();
        }

    }
