package componentASW.bak;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class ViewableAtomic_TP extends ViewableAtomic {
	
	
	
	// Add Default Constructor
    public ViewableAtomic_TP(){
        this("Controller_Updater") ;   }
	
	// Add Parameterized Constructors
    public ViewableAtomic_TP(String name){
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
