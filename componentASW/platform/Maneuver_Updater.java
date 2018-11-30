package componentASW.platform;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Maneuver_Updater extends ViewableAtomic {
	
	
	// Add Default Constructor
    public Maneuver_Updater(){
        this("Maneuver_Updater") ;   }
	
	// Add Parameterized Constructors
    public Maneuver_Updater(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("move_cmd");

        // Add output port names
        addOutport("cmd_info");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = "WAIT";
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
