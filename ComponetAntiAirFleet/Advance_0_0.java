
package ComponetAntiAirFleet;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class Advance_0_0 extends ViewableAtomic {

	protected double processing_time;
	
	protected double missileLoc;

	// Add Default Constructor
	public Advance_0_0() {
		this("Advance_0_0");
	}

	// Add Parameterized Constructors
	public Advance_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("advance_in");

		// Add output port names
		addOutport("missileLoc_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {
		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		missileLoc = -1;
		processing_time = 3;
		//holdIn("active",processing_time);
	}

	// Add external transition function
	public void deltext(double e, message x) {
		Continue(e);

		System.out.println("The elapsed time of the "+this.getClass().getName()+" is" + e);
		System.out.println("*****************************************");
		System.out.println("external-Phase before: " + phase);
		/////external transition specific biz code :
		if (phaseIs("passive")) {
			holdIn("active", processing_time);
		}
		if(phaseIs("active")) {
			entity ent = null;
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "advance_in", i)) {
					ent = x.getValOnPort("advance_in", i);
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
		System.out.println("Internal-Phase before: " + phase);
		/////internal transition specific biz code :
		if (phaseIs("passive")) {
			missileLoc = 0;
			processing_time = 3;
			//holdIn("active", processing_time);
		}
		/////
		System.out.println("Internal-Phase after: " + phase);
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
		con = makeContent("missileLoc_out", new entity(""+missileLoc));
		m.add(con);
		if((int)missileLoc == 0) {
			passivate();
		}
		return m;
	}

	public void showState() {
		super.showState();
		// System.out.println("job: " + job.getName());
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "";
	}
}
