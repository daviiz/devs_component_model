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
import util.rand;

public class RedController_0_0 extends view.modeling.ViewableAtomic {

	protected double processing_time =1;
	protected double shipLoc;
	protected boolean missileExplode_in;
	protected double missileLoc;
	protected rand r;
	protected double dis;

	// Add Default Constructor
	public RedController_0_0() {
		this("RedController_0_0");
	}

	// Add Parameterized Constructors
	public RedController_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("missileExplode_in");
		addInport("shipLoc_in");
		addInport("missileLoc_in");

		// Add output port names
		addOutport("redDestroyed_out");
		addOutport("advance_out");
		addOutport("hit_out");
//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		missileExplode_in = false;
		shipLoc = 2000;
		missileLoc = 0;
		//holdIn("active",0);
		r = new rand(1);
		dis = 2000;
		processing_time =2;
		holdIn("active",processing_time);
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is：" + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		if(phaseIs("passive")) {
			holdIn("active", processing_time);
		}
		entity ent = null;
		if(phaseIs("active")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "missileExplode_in", i)) {
					ent = x.getValOnPort("missileExplode_in", i);
					missileExplode_in = (ent.eq("true"));
				}else if(messageOnPort(x, "shipLoc_in", i)) {
					ent = x.getValOnPort("shipLoc_in", i);
					shipLoc = Double.parseDouble(ent.getName());
				}else if(messageOnPort(x, "missileLoc_in", i)) {
					ent = x.getValOnPort("missileLoc_in", i);
					missileLoc = Double.parseDouble(ent.getName());
				}
			}
			//passivate();
		}
		/////
		System.out.println("external-Phase after: " + phase);
	}

	// Add internal transition function
	public void deltint() {
		System.out.println("====================================");
		System.out.println(this.getClass().getName()+" Internal-Phase before: " + phase);
		/////internal transition specific biz code :
		if (phaseIs("passive")) {
			shipLoc = 2000;
			missileLoc = 0;
			//holdIn("active",0);
			r = new rand(1);
			dis = 2000;
			processing_time =2;
			//holdIn("active", processing_time);
		}
		/////
		System.out.println(this.getClass().getName()+"Internal-Phase after: " + phase);
	}

	// Add confluent function
	public void deltcon(double e, message x) {
		System.out.println("confluent");
		deltint();
		deltext(0, x);
	}

	// Add output function
	public message out() {
		message m = new message();
		content con = null;
		double tmp = 10;
		double dis = (shipLoc - missileLoc);
		if(dis < 0) {
			con = makeContent("redDestroyed_out", new entity("false"));
			m.add(con);
			con = makeContent("advance_out",new entity("0"));
			m.add(con);
			con = makeContent("hit_out",new entity("success"));//只有命中了才会返回消息
			m.add(con);
			
			passivate();
		}else if (dis < 200) {
			 tmp = r.uniform(10);
			if(tmp>6) {
				con = makeContent("redDestroyed_out", new entity("false"));
				m.add(con);
				missileLoc = missileLoc + 100;
				con = makeContent("advance_out",new entity(missileLoc+""));
				m.add(con);
			}else {
				con = makeContent("redDestroyed_out", new entity("true"));
				
				m.add(con);
				passivate();
			}
		}else if(dis <500) {
			tmp = r.uniform(10);
			if(tmp>5) {
				con = makeContent("redDestroyed_out", new entity("false"));
				m.add(con);
				missileLoc = missileLoc + 100;
				con = makeContent("advance_out",new entity(missileLoc+""));
				m.add(con);
			}else {
				con = makeContent("redDestroyed_out", new entity("true"));
				m.add(con);
				passivate();
			}
		}else if(dis <1000) {
			tmp = r.uniform(10);
			if(tmp>4) {
				con = makeContent("redDestroyed_out", new entity("false"));
				m.add(con);
				missileLoc = missileLoc + 100;
				con = makeContent("advance_out",new entity(missileLoc+""));
				m.add(con);
			}else {
				con = makeContent("redDestroyed_out", new entity("true"));
				m.add(con);
				passivate();
			}
		}else {
			con = makeContent("redDestroyed_out", new entity("false"));
			m.add(con);
			missileLoc = missileLoc + 100;
			con = makeContent("advance_out",new entity(missileLoc+""));
			m.add(con);
		}

		return m;
	}

	// Add Show State function
}
