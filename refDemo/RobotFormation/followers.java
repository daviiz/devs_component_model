package refDemo.RobotFormation;

import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class followers extends ViewableDigraph {

	public followers() {
		super("followers");

		addInport("vin");
		addInport("pin");
		addInport("formation");
		addInport("L");
		addInport("T");
		addInport("stop");
		addInport("endx");
		addInport("endy");
		addOutport("p2out");
		addOutport("p3out");
		addOutport("p4out");
		addOutport("p5out");
		addOutport("p6out");
		addOutport("command");

//    this.setBlackBox(true);

		ViewableAtomic r2 = new r2("Robot2");
		ViewableAtomic r3 = new r3("Robot3");
		ViewableAtomic r4 = new r4("Robot4");
		ViewableAtomic r5 = new r5("Robot5");
		ViewableDigraph r6 = new r6();

		add(r2);
		add(r3);
		add(r4);
		add(r5);
		add(r6);

		addCoupling(this, "L", r2, "L");
		addCoupling(this, "L", r3, "L");
		addCoupling(this, "L", r4, "L");
		addCoupling(this, "L", r5, "L");
		addCoupling(this, "L", r6, "L");
		addCoupling(this, "T", r2, "T");
		addCoupling(this, "T", r3, "T");
		addCoupling(this, "T", r4, "T");
		addCoupling(this, "T", r5, "T");
		addCoupling(this, "T", r6, "T");
		addCoupling(this, "stop", r2, "stop");
		addCoupling(this, "stop", r3, "stop");
		addCoupling(this, "stop", r4, "stop");
		addCoupling(this, "stop", r5, "stop");
		addCoupling(this, "stop", r6, "stop");
		addCoupling(this, "vin", r2, "vin");
		addCoupling(this, "pin", r2, "pin");
		addCoupling(this, "formation", r2, "formation");
		addCoupling(this, "vin", r3, "vin");
		addCoupling(this, "pin", r3, "pin");
		addCoupling(this, "formation", r3, "formation");
		addCoupling(this, "vin", r4, "vin");
		addCoupling(this, "pin", r4, "pin");
		addCoupling(this, "formation", r4, "formation");
		addCoupling(this, "vin", r5, "vin");
		addCoupling(this, "pin", r5, "pin");
		addCoupling(this, "formation", r5, "formation");
		addCoupling(this, "vin", r6, "vin");
		addCoupling(this, "pin", r6, "pin");
		addCoupling(this, "formation", r6, "formation");
		addCoupling(this, "endx", r6, "endx");
		addCoupling(this, "endy", r6, "endy");
		addCoupling(r6, "command", r2, "formation");
		addCoupling(r6, "command", r3, "formation");
		addCoupling(r6, "command", r4, "formation");
		addCoupling(r6, "command", r5, "formation");
		addCoupling(r2, "pout", this, "p2out");
		addCoupling(r3, "pout", this, "p3out");
		addCoupling(r4, "pout", this, "p4out");
		addCoupling(r5, "pout", this, "p5out");
		addCoupling(r6, "pout", this, "p6out");
		addCoupling(r6, "command", this, "command");

//    initialize();

		preferredSize = new Dimension(80, 40);
		r2.setPreferredLocation(new Point(15, 5));
		r3.setPreferredLocation(new Point(15, 20));
		r4.setPreferredLocation(new Point(15, 34));
		r5.setPreferredLocation(new Point(15, 45));
		r6.setPreferredLocation(new Point(15, 58));

	}

	/**
	 * Automatically generated by the SimView program. Do not edit this manually, as
	 * such changes will get overwritten.
	 */
	public void layoutForSimView() {
		preferredSize = new Dimension(687, 480);
		((ViewableComponent) withName("Robot5")).setPreferredLocation(new Point(165, 147));
		((ViewableComponent) withName("Robot3")).setPreferredLocation(new Point(163, 20));
		((ViewableComponent) withName("Robot6")).setPreferredLocation(new Point(3, 268));
		((ViewableComponent) withName("Robot2")).setPreferredLocation(new Point(-8, 21));
		((ViewableComponent) withName("Robot4")).setPreferredLocation(new Point(-8, 144));
	}
}
