/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW;

import componentASW.ef.ExperimentFrame;
import view.modeling.ViewableDigraph;

public class CombatSysModel extends ViewableDigraph{

    // Add Default Constructor
    public CombatSysModel(){
        this("CombatSys_0_1");
    }

    // Add Parameterized Constructor
    public CombatSysModel(String name){
        super(name);

// Structure information start
        // Add input port names

        // Add output port names

//add test input ports:

        // Initialize sub-components
        ViewableDigraph ef_1_1 =  new ExperimentFrame("ef_1_1");
        ViewableDigraph simModel_1_1 =  new SimModel("simModel_1_1");

        // Add sub-components
        add(ef_1_1);
        add(simModel_1_1);

        // Add Couplings
        addCoupling(ef_1_1, "entity_info", simModel_1_1, "entity_info");
        addCoupling(ef_1_1, "scen_info", simModel_1_1, "scen_info");
        addCoupling(simModel_1_1, "engage_result", ef_1_1, "engage_result");
        addCoupling(simModel_1_1, "wp_launch", ef_1_1, "wp_launch");

// Structure information end
        initialize();
        }

    }
