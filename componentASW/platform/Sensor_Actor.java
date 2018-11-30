package componentASW.platform;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Sensor_Actor extends ViewableAtomic {
	
	
	// Add Default Constructor
    public Sensor_Actor(){
        this("Sensor_Actor") ;   }
	
	// Add Parameterized Constructors
    public Sensor_Actor(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("engage_result");
        addInport("scen_info");
        addInport("env_info");
        addInport("response");

        // Add output port names
        addOutport("threat_info");
        addOutport("request");

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
