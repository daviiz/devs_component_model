/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-10-29 ����2:01
*/

// Default Package
package ComponetAntiAirFleet;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class WarShipT extends ViewableAtomic {

	protected double processing_time;

	protected entity moveOrderEnt;

	private double y = 2000;
	protected double redLoc = 0;
	protected boolean redDestroyed_in = false;

	// Add Default Constructor
	public WarShipT() {
		this("WarShipT");
	}

	// Add Parameterized Constructors
	public WarShipT(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("redLoc_in");
		addInport("redDestroyed_in");

		// Add output port names
		addOutport("shipLoc_out");
		addOutport("samLaunch_out");
		addOutport("ramLaunch_out");
		addOutport("ciwsLaunch_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		moveOrderEnt = new entity("nil");
		processing_time = 6;
		holdIn("moving", processing_time);
		y = 2000;
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is：" + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		if(phaseIs("passive")) {
			holdIn("moving", processing_time);
		}
		if (phaseIs("moving")) {
			entity ent = null;
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "redLoc_in", i)) {
					ent = x.getValOnPort("redLoc_in", i);
					redLoc = Double.parseDouble(ent.getName());
					
				}
				if (messageOnPort(x, "redDestroyed_in", i)) {
					ent = x.getValOnPort("redDestroyed_in", i);
					redDestroyed_in = ent.eq("true");
					if(redDestroyed_in) {
						y = 2000;
						redLoc = 0;
						redDestroyed_in = false;
					}
					
				}
			}
		}
		/////
		System.out.println("external-Phase after: " + phase);
		

	}

	// Add internal transition function
	public void deltint() {
		System.out.println("====================================");
		System.out.println(this.getClass().getName()+" Internal-Phase before: " + phase);
		/////internal transition specific biz code :
		if(phaseIs("passive")) {
			y = 2000;
			redLoc = 0;
			redDestroyed_in = false;
			
		}
		/////
		System.out.println(this.getClass().getName()+"Internal-Phase after: " + phase);
	}

	// Add confluent function
	/*
	 * public void deltcon(double e, message x){ }
	 */

	// Add output function
	public message out() {
		message m = new message();
		y = (y-60>0)?(y-60):0;
		double dis = y - redLoc;
		content con = null;
		if (phaseIs("moving")) {
			// shipLoc_out
			con = makeContent("shipLoc_out", new entity(y+""));
			m.add(con);
			
			dis = dis > 0 ? dis : 0;
			
			if (dis < 200) {
				con = makeContent("ciwsLaunch_out",new entity("true"));
				m.add(con);
			}else {
				con = makeContent("ciwsLaunch_out",new entity("false"));
				m.add(con);
			}
			if(dis <500) {
				con = makeContent("ramLaunch_out",new entity("true"));
				m.add(con);
			}else {
				con = makeContent("ramLaunch_out",new entity("false"));
				m.add(con);
			}
			if(dis <1000) {
				con = makeContent("samLaunch_out",new entity("true"));
				m.add(con);
			}else {
				con = makeContent("samLaunch_out",new entity("false"));
				m.add(con);
			}
			if((int) dis == 0) {
				
				passivate();
			}
		}
		return m;
	}

	// Add Show State function
}
