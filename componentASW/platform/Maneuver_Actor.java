package componentASW.platform;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Maneuver_Actor extends ViewableAtomic {
	
	
	// Add Default Constructor
    public Maneuver_Actor(){
        this("Maneuver_Actor") ;   }
	
	// Add Parameterized Constructors
    public Maneuver_Actor(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("engage_result");
        addInport("scen_info");
        addInport("env_info");
        addInport("cmd_info");

        // Add output port names
        addOutport("move_finished");
        addOutport("fuel_exhasuted");
        addOutport("move_result");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = "IDLE";
        sigma = INFINITY;
    }

    // Add external transition function
    public void deltext(double e, message x){
    }

    // Add internal transition function
    public void deltint(){
    }

    // Add confluent function
    public void deltcon(double e, message x){
    }

    // Add output function
    public message out(){return null;
    }

    // Add Show State function

}
