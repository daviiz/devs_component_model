/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package ComponetAntiAirFleet;

import java.awt.Dimension;
import java.awt.Point;

import GenCol.entity;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class gp extends ViewableDigraph {

	public gp() {
		super("gp");

		ViewableAtomic g = new genr("g", 10);
		ViewableAtomic p = new proc("p", 10);

		add(g);
		add(p);

		addInport("in");
		addInport("start");
		addInport("stop");
		addOutport("out");

		addTestInput("start", new entity());
		addTestInput("stop", new entity(), 5.0);

		addCoupling(this, "in", g, "in");

		addCoupling(this, "start", g, "start");
		addCoupling(this, "stop", g, "stop");

		addCoupling(g, "out", p, "in");

		addCoupling(p, "out", this, "out");

		// initialize();
		// showState();
		/*
		 * preferredSize = new Dimension(484, 145); g.setPreferredLocation(new Point(13,
		 * 18)); p.setPreferredLocation(new Point(195, 18)); t.setPreferredLocation(new
		 * Point(193, 80));
		 */
	}

	/**
	 * Automatically generated by the SimView program. Do not edit this manually, as
	 * such changes will get overwritten.
	 */
	public void layoutForSimView() {
		preferredSize = new Dimension(591, 269);
		// ((ViewableComponent)withName("t")).setPreferredLocation(new Point(-10, 180));
		((ViewableComponent) withName("p")).setPreferredLocation(new Point(233, 196));
		((ViewableComponent) withName("g")).setPreferredLocation(new Point(113, 82));
	}
}
