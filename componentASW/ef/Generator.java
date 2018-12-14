/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW.ef;

import GenCol.entity;
import componentASW.om.CombatEnt;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Generator extends ViewableAtomic{

    
    protected CombatEnt shipEnt;

    // Add Default Constructor
    public Generator(){
        this("Generator");    }

    // Add Parameterized Constructors
    public Generator(String name){
        super(name);
// Structure information start
        // Add input port names
        addInport("wp_launch");
        // Add output port names
        addOutport("scen_gen");
        addOutport("entity_gen");

//add test input ports:

// Structure information end
        initialize();
    }

    // Add initialize function
    public void initialize(){
        super.initialize();
        phase = "active";
        sigma = 0;
        shipEnt = new CombatEnt();
    }

    // Add external transition function
    public void deltext(double e, message x){
    	Continue(e);
    	for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "wp_launch", i)) {
				shipEnt = new CombatEnt((CombatEnt)x.getValOnPort("wp_launch", i));
			}
		}
    }

    // Add internal transition function
    public void deltint(){
    }

    // Add confluent function
//    public void deltcon(double e, message x){
//    }

    // Add output function
    public message out(){
    	message m = new message();
		if(phaseIs("active")) {
			content con = makeContent("scen_gen", new entity("scen_starting"));
			m.add(con);
			
			if(!shipEnt.eq("combatEntBase")) {
				content con2 = makeContent("entity_gen", shipEnt);
				m.add(con2);
			}
			passivate();
		}
		return m;
    }
}

