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

import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class Weapon extends ViewableDigraph{
	
	protected int x;
    protected int y;
    protected int speed;
	
	// Add Default Constructor
    public Weapon(){
        this("weapon",0,0,0);
    }

    // Add Parameterized Constructor
    public Weapon(String name,int x,int y, int speed){
        super(name);
        this.x = x;
        this.y = y;
        this.speed = speed;

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
        ViewableDigraph wpManeuver =  new wManeuver("wpManeuver");
        ViewableDigraph wpController =  new wController("wpController");
        ViewableDigraph wpSensor =  new wSensor("wpSensor");

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
		public void layoutForSimView() {
			preferredSize = new Dimension(790, 500);
			((ViewableComponent) withName("wpManeuver")).setPreferredLocation(new Point(40, 330));
			((ViewableComponent) withName("wpController")).setPreferredLocation(new Point(20, 165));
			((ViewableComponent) withName("wpSensor")).setPreferredLocation(new Point(0, 15));
		}

    }
