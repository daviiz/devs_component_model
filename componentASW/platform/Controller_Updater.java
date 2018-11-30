package componentASW.platform;

import model.modeling.message;
import view.modeling.ViewableAtomic;

 class Controller_Updater extends ViewableAtomic {
	
	private double identify_time;
	
	// Add Default Constructor
    public Controller_Updater(){
        this("Controller_Updater") ;   }
	
	// Add Parameterized Constructors
    public Controller_Updater(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("threat_info");
        addInport("scen_info");

        // Add output port names
        addOutport("target_info");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        identify_time = 2;  //2 sec
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
    public message out(){
    	return null;
    }

    // Add Show State function

}
