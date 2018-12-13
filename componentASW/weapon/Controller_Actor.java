package componentASW.weapon;

import model.modeling.message;
import view.modeling.ViewableAtomic;

 public class Controller_Actor extends ViewableAtomic {
	
	
	
	// Add Default Constructor
    public Controller_Actor(){
        this("wpController_Actor") ;   }
	
	// Add Parameterized Constructors
    public Controller_Actor(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("move_finishied");
        addInport("engage_result");
        addInport("scen_info");
        addInport("target_info");
        addInport("wp_guidance");

        // Add output port names
        addOutport("move_cmd");

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
