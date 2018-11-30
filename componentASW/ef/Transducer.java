/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.ef;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Transducer extends ViewableAtomic{

    protected double processing_time;

    // Add Default Constructor
    public Transducer(){
        this("Transducer");    }

    // Add Parameterized Constructors
    public Transducer(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("engage_result");

        // Add output port names

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
    public message out(){return null;
    }

    // Add Show State function
    }

