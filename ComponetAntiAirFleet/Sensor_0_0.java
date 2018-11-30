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

public class Sensor_0_0 extends view.modeling.ViewableAtomic {

	protected double processing_time;
	protected double shipLoc;
	protected boolean samLaunch_in; //1000
	protected boolean ramLaunch_in; //500
	protected boolean ciwsLaunch_in;//200
	protected int rand;
	protected entity mission;
	// Add Default Constructor
	public Sensor_0_0() {
		this("Sensor_0_0");
	}

	// Add Parameterized Constructors
	public Sensor_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("in");
		addInport("in2");//shipLoc_in  --double
		addInport("in3");//samLaunch_in  --true/false
		addInport("in4");//ramLaunch_in  --true/false
		addInport("in5");//ciwsLaunch_in --true/false

		// Add output port names
		addOutport("out2");//missileExplode_out  //shipLoc_out<1时发出爆炸命令true，命中！否则属于被拦截  --true,false
		addOutport("out");//shipLoc_out
		
		addTestInput("in", new entity("nil"));
		addTestInput("in2", new entity(2000+""));
		addTestInput("in3", new entity("false"));
		addTestInput("in4", new entity("false"));
		addTestInput("in5", new entity("false"));

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		shipLoc = 2000;
		samLaunch_in = false;
		ramLaunch_in = false;
		ciwsLaunch_in = false;
		processing_time = 1;
		rand = 9;
		mission = new entity("nil");
		holdIn("active", processing_time);
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is: " + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		entity ent = null;
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in", i)) {
					holdIn("active", processing_time);
				}
				
			}
		}
		if(phaseIs("active")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "in2", i)) {
					ent = x.getValOnPort("in2", i);
					if(ent.eq("true")) {
						shipLoc = 2000;
						samLaunch_in = false;
						ramLaunch_in = false;
						ciwsLaunch_in = false;
						processing_time = 1;
						rand = 9;
						mission = new entity("nil");
					}else if(!ent.eq("false")) {
						shipLoc = Double.parseDouble(ent.getName());
					}
				}else if(messageOnPort(x, "in3", i)) {
					ent = x.getValOnPort("in3", i);
					samLaunch_in = (ent.eq("true"));
				}else if(messageOnPort(x, "in4", i)) {
					ent = x.getValOnPort("in4", i);
					ramLaunch_in = (ent.eq("true"));
				}
				else if(messageOnPort(x, "in5", i)) {
					ent = x.getValOnPort("in5", i);
					ciwsLaunch_in = (ent.eq("true"));
				}else if(messageOnPort(x, "in", i)) {
					mission = x.getValOnPort("in", i);
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
			samLaunch_in = false;
			ramLaunch_in = false;
			ciwsLaunch_in = false;
			processing_time = 1;
			rand = 9;
			mission = new entity("nil");
			holdIn("active", processing_time);
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
		if(!mission.eq("nil")) {
			con = makeContent("out2", new entity("false"));
			m.add(con);
			con = makeContent("out",new entity("" + 2000));
			m.add(con);
		}
		mission = new entity("nil");
		
		if(shipLoc<1) {
			con = makeContent("out2", new entity("true"));
			m.add(con);
			passivate();
		}else {
			con = makeContent("out2", new entity("false"));
			m.add(con);
		}
		con = makeContent("out",new entity("" + shipLoc));
		m.add(con);
		return m;
	}

	// Add Show State function
}
