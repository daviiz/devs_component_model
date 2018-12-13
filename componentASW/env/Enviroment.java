/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.env;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Enviroment extends ViewableAtomic{

    protected double processing_time;

    // Add Default Constructor
    public Enviroment(){
        this("Enviroment");    }

    // Add Parameterized Constructors
    public Enviroment(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("scen_info");
        addInport("move_result");

        // Add output port names
        addOutport("env_info");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = "passive";
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

