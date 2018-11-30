/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.weapon;

import componentASW.platform.Maneuver;
import componentASW.platform.Sensor;
import view.modeling.ViewableDigraph;

public class Weapon extends ViewableDigraph{

    // Add Default Constructor
    public Weapon(){
        this("weapon_1_1");
    }

    // Add Parameterized Constructor
    public Weapon(String name){
        super(name);

// Structure information start
        // Add input port names
        addInport("wp_guidance");
        addInport("engage_result");
        addInport("move_result");
        addInport("env_info");
        addInport("entity_info");

        // Add output port names
        addOutport("move_result");
        addOutport("guidance_info");

//add test input ports:

        // Initialize sub-components
        ViewableDigraph wpManeuver =  new Maneuver("wpManeuver");
        ViewableDigraph wpController =  new Controller("wpController");
        ViewableDigraph wpSensor =  new Sensor("wpSensor");

        // Add sub-components
        add(wpManeuver);
        add(wpController);
        add(wpSensor);

        // Add Couplings
        addCoupling(this, "wp_guidance", wpController, "wp_guidance");
        addCoupling(this, "move_result", wpSensor, "move_result");
        addCoupling(this, "engage_result", wpController, "engage_result");
        addCoupling(this, "engage_result", wpManeuver, "engage_result");
        addCoupling(this, "engage_result", wpSensor, "engage_result");
        addCoupling(this, "entity_info", wpController, "scen_info");
        addCoupling(this, "entity_info", wpManeuver, "scen_info");
        addCoupling(this, "entity_info", wpSensor, "scen_info");
        addCoupling(this, "env_info", wpManeuver, "env_info");
        addCoupling(this, "env_info", wpSensor, "env_info");
        
        addCoupling(wpSensor, "threat_info", wpController, "threat_info");
        
        addCoupling(wpController,"move_cmd",wpManeuver,"move_cmd");
      
        addCoupling(wpManeuver, "fuel_exhausted", wpController, "engage_result");
        addCoupling(wpManeuver, "fuel_exhausted", wpSensor, "engage_result");
        addCoupling(wpManeuver, "move_finished", wpController, "move_finished");
        addCoupling(wpManeuver, "guidance_info",this, "guidance_info");
        addCoupling(wpManeuver, "move_result",this, "move_result");
        

// Structure information end
        initialize();
        }

    }
