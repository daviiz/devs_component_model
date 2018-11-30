/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.ef;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

public class ExperimentFrame extends ViewableDigraph{

    // Add Default Constructor
    public ExperimentFrame(){
        this("ExperimentFrame");
    }

    // Add Parameterized Constructor
    public ExperimentFrame(String name){
        super(name);

// Structure information start
        // Add input port names
        addInport("wp_launch");
        addInport("engage_result");

        // Add output port names
        addOutport("scen_info");
        addOutport("entity_info");

//add test input ports:

        // Initialize sub-components
        ViewableAtomic transducer_1_0 =  new Transducer("transducer_1_0");
        ViewableAtomic generator_1_0 =  new Generator("generator_1_0");

        // Add sub-components
        add(transducer_1_0);
        add(generator_1_0);

        // Add Couplings
        addCoupling(this, "engage_result", transducer_1_0, "engage_result");
        addCoupling(this, "entity_info", generator_1_0, "entity_gen");
        addCoupling(this, "scen_info", generator_1_0, "scen_gen");
        addCoupling(this, "wp_launch", generator_1_0, "wp_launch");

// Structure information end
        initialize();
        }

    }
