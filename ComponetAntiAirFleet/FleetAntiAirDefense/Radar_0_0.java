package ComponetAntiAirFleet.FleetAntiAirDefense;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

//战舰的雷达组件
public class Radar_0_0 extends ViewableAtomic {

	protected double processing_time;

	protected int radius;
	// protected int scanCycle;

	protected entity redLocEntity;

	protected entity redDestroyedEntity;

	protected entity shipLocEntity;

	protected double scanCycle = 2;

	// Add Default Constructor
	public Radar_0_0() {
		this("Radar_0_0");
	}

	// Add Parameterized Constructors
	public Radar_0_0(String name) {
		super(name);
// Structure information start
		// Add input port names
		addInport("redLoc_in");
		addInport("redDestroyed_in");
		addInport("shipLoc_in");

		// Add output port names
		addOutport("redDetect_out");
		addOutport("redDestroyed_out");

//add test input ports:

// Structure information end
		initialize();
	}

	// Add initialize function
	public void initialize() {

		super.initialize();
		phase = "passive";
		sigma = INFINITY;
		radius = 500000;// km
		scanCycle = 2; // mins

		redLocEntity = new entity("nil");
		redDestroyedEntity = new entity("nil");
		shipLocEntity = new entity("nil");

		holdIn("Scanning", scanCycle);
	}

	// Add external transition function
	public void deltext(double e, message x) {
		// updating the sigma
		Continue(e);
		// if phase is Scanning
		if (phaseIs("Scanning")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "redLoc_in", i)) {
					redLocEntity = x.getValOnPort("redLoc_in", i);
				} else if (messageOnPort(x, "redDestroyed_in", i)) {
					redDestroyedEntity = x.getValOnPort("redDestroyed_in", i);
				} else if (messageOnPort(x, "shipLoc_in", i)) {
					shipLocEntity = x.getValOnPort("shipLoc_in", i);
				}
			}
			holdIn("Scanning", scanCycle);
		}

	}

	// Add internal transition function
	public void deltint() {
		// holdIn("Scanning", scanCycle);
	}

	// Add confluent function
	/*
	 * public void deltcon(double e, message x){ }
	 */

	// Add output function
	public message out() {
		message m = new message();
		content con = null;
		if (phaseIs("Scanning")) {
			if (redDestroyedEntity.eq("false")) {
				double dis = Double.parseDouble(shipLocEntity.getName()) - Double.parseDouble(redLocEntity.getName());
				if (dis > radius) {
					// redDetect_out
					con = makeContent("redDetect_out", new entity("false"));
					m.add(con);
					// redDestroyed_out
					con = makeContent("redDestroyed_out", new entity("false"));
					m.add(con);

				} else if (redDestroyedEntity.eq("true")) {
					// redDetect_out
					con = makeContent("redDetect_out", new entity("true"));
					m.add(con);
					// redDestroyed_out
					con = makeContent("redDestroyed_out", new entity("false"));
					m.add(con);
				}
			} else if (redDestroyedEntity.eq("true")) {
				// redDetect_out
				con = makeContent("redDetect_out", new entity("false"));
				m.add(con);
				// redDestroyed_out
				con = makeContent("redDestroyed_out", new entity("true"));
				m.add(con);
			}
		}
		redLocEntity = new entity("nil");
		redDestroyedEntity = new entity("nil");
		shipLocEntity = new entity("nil");
		return m;
	}

	public double CalcDistance(double srcX, double srcY, double desX, double desY) {
		double distance = 0;
		distance = (srcX - desX) * (srcX - desX) + (srcY - desY) * (srcY - desY);
		distance = Math.sqrt(distance);

		return distance;
	}

	// Add Show State function
}