package refDemo.RobotFormation;

import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class r6 extends ViewableDigraph {

	public r6() {
		super("Robot6");

		// this.setBlackBox(true);
		addInport("vin");
		addInport("stop");
		addInport("L");
		addInport("T");
		addInport("formation");
		addInport("endx");
		addInport("endy");
		addInport("pin");
		addOutport("pout");
		addOutport("command");

		ViewableAtomic r6controller = new r6controller("Robot6-controller");
		ViewableAtomic r6camera = new r6camera("Robot6-camera");

		add(r6controller);
		add(r6camera);

		addCoupling(this, "vin", r6controller, "vin");
		addCoupling(this, "pin", r6controller, "pin");
		addCoupling(this, "L", r6controller, "L");
		addCoupling(this, "T", r6controller, "T");
		addCoupling(this, "formation", r6controller, "formation");
		addCoupling(this, "stop", r6controller, "stop");
		addCoupling(this, "L", r6camera, "L");
		addCoupling(this, "T", r6camera, "T");
		addCoupling(this, "vin", r6camera, "v");
		addCoupling(this, "endx", r6camera, "endx");
		addCoupling(this, "endy", r6camera, "endy");
		addCoupling(r6controller, "pout", r6camera, "pin6");
		addCoupling(r6camera, "command", r6controller, "formation");
		addCoupling(r6camera, "command", this, "command");
		addCoupling(r6controller, "pout", this, "pout");

		this.isHidden();

		initialize();
		preferredSize = new Dimension(200, 100);
		r6controller.setPreferredLocation(new Point(13, 18));
		r6camera.setPreferredLocation(new Point(45, 18));

	}

	/**
	 * Automatically generated by the SimView program. Do not edit this manually, as
	 * such changes will get overwritten.
	 */
	public void layoutForSimView() {
		preferredSize = new Dimension(531, 194);
		((ViewableComponent) withName("Robot6-controller")).setPreferredLocation(new Point(167, 40));
		((ViewableComponent) withName("Robot6-camera")).setPreferredLocation(new Point(-5, 29));
	}
}
