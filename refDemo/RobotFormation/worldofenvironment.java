package refDemo.RobotFormation;

import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class worldofenvironment extends ViewableDigraph {

	public worldofenvironment() {
		super("world");

		ViewableDigraph ef = new Ef1();
		ViewableDigraph r = new Robots1();
		ViewableDigraph d = new display();

		add(ef);
		add(r);
		add(d);

		initialize();

		addCoupling(ef, "velocity", r, "vin");
		addCoupling(ef, "T", r, "T");
		addCoupling(ef, "L", r, "L");
		addCoupling(ef, "command", r, "formation");
		addCoupling(ef, "stop", r, "stop");
		addCoupling(ef, "startx", d, "startx");
		addCoupling(ef, "starty", d, "starty");
		addCoupling(ef, "endx", d, "endx");
		addCoupling(ef, "endy", d, "endy");
		addCoupling(r, "pout1", d, "p1in");
		addCoupling(r, "pout2", d, "p2in");
		addCoupling(r, "pout3", d, "p3in");
		addCoupling(r, "pout4", d, "p4in");
		addCoupling(r, "pout5", d, "p5in");
		addCoupling(r, "pout6", d, "p6in");
		addCoupling(r, "pout1", ef, "pin1");
		addCoupling(r, "pout6", ef, "pin6");
		isHidden();

		preferredSize = new Dimension(549, 181);
		ef.setPreferredLocation(new Point(150, 150));
		r.setPreferredLocation(new Point(150, 30));
		d.setPreferredLocation(new Point(150, 30));

	}

	/**
	 * Automatically generated by the SimView program. Do not edit this manually, as
	 * such changes will get overwritten.
	 */
	public void layoutForSimView() {
		preferredSize = new Dimension(998, 982);
		((ViewableComponent) withName("Ef")).setPreferredLocation(new Point(159, 72));
		((ViewableComponent) withName("Robots")).setPreferredLocation(new Point(126, 328));
		((ViewableComponent) withName("DataProc")).setPreferredLocation(new Point(150, 30));
	}
}
