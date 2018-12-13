package componentASW.platform;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Sensor_Updater extends ViewableAtomic {
	
	
	// Add Default Constructor
    public Sensor_Updater(){
        this("Sensor_Updater") ;   }
	
	// Add Parameterized Constructors
    public Sensor_Updater(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("move_result");
        addInport("request");

        // Add output port names
        addOutport("response");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = "UPDATE";
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
