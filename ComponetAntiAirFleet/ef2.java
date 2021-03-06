/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package ComponetAntiAirFleet;

import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class ef2 extends ViewableDigraph {

	public ef2() {
		super("ef");
		efConstruct(10, 100);
	}

	public ef2(String nm, double int_arr_t, double observe_t) {
		super(nm);
		efConstruct(int_arr_t, observe_t);
	}

	public void efConstruct(double int_arr_t, double observe_t) {

		addInport("in");
		addInport("start");
		addInport("stop");
		addOutport("out");
		addOutport("out2");
		addOutport("result");

		ViewableAtomic g = new genr2("g", int_arr_t);
		ViewableAtomic t = new transd2("t", observe_t);

		add(g);
		add(t);

		/*addTestInput("start", new entity());
		addTestInput("stop", new entity());
		addTestInput("in", new entity("job0"));
		addTestInput("in", new entity("job1"));*/

		initialize();

		addCoupling(g, "out", t, "ariv");
		addCoupling(g, "out2", t, "solved");
		addCoupling(this, "in", g, "in");
		//addCoupling(t, "out", g, "stop");
		addCoupling(this, "start", g, "start");
		addCoupling(this, "stop", g, "stop");
		addCoupling(g, "out", this, "out");
		addCoupling(g, "out2", this, "out2");
		addCoupling(t, "Thru", this, "result");

		preferredSize = new Dimension(279, 146);
		t.setPreferredLocation(new Point(6, 17));
		g.setPreferredLocation(new Point(-5, 81));
	}

	/**
	 * Automatically generated by the SimView program. Do not edit this manually, as
	 * such changes will get overwritten.
	 */
	public void layoutForSimView() {
		preferredSize = new Dimension(497, 127);
		((ViewableComponent) withName("g")).setPreferredLocation(new Point(0, 37));
		((ViewableComponent) withName("t")).setPreferredLocation(new Point(220, 28));
	}
}
