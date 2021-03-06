package refDemo.RobotFormation;

import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class r1 extends ViewableDigraph {

	public r1() {
		super("Robot1");

//    this.setBlackBox(true);
		addInport("vin");
		addInport("stop");
		addInport("L");
		addInport("T");
		addInport("formation");
		addInport("startx");
		addInport("starty");
		addInport("endx");
		addInport("endy");
		addOutport("vout");
		addOutport("pout");
		addOutport("command");

		ViewableAtomic r1controller = new r1controller("Robot1-controller");
		ViewableAtomic r1camera = new r1camera("Robot1-camera");

		add(r1controller);
		add(r1camera);

		addCoupling(this, "vin", r1controller, "vin");
		addCoupling(this, "L", r1controller, "L");
		addCoupling(this, "T", r1controller, "T");
		addCoupling(this, "formation", r1controller, "formation");
		addCoupling(this, "stop", r1controller, "stop");
		addCoupling(this, "L", r1camera, "L");
		addCoupling(this, "T", r1camera, "T");
		addCoupling(this, "vin", r1camera, "v");
		addCoupling(this, "startx", r1camera, "startx");
		addCoupling(this, "starty", r1camera, "starty");
		addCoupling(this, "endx", r1camera, "endx");
		addCoupling(this, "endy", r1camera, "endy");
		addCoupling(r1camera, "command", r1controller, "formation");
		addCoupling(r1camera, "command", this, "command");
		addCoupling(r1controller, "pout", r1camera, "pin1");
		addCoupling(r1controller, "vout", this, "vout");
		addCoupling(r1controller, "pout", this, "pout");

		this.isHidden();

		initialize();
		preferredSize = new Dimension(200, 100);
		r1controller.setPreferredLocation(new Point(13, 18));
		r1camera.setPreferredLocation(new Point(45, 18));

	}

	/**
	 * Automatically generated by the SimView program. Do not edit this manually, as
	 * such changes will get overwritten.
	 */
	public void layoutForSimView() {
		preferredSize = new Dimension(527, 162);
		((ViewableComponent) withName("Robot1-camera")).setPreferredLocation(new Point(-12, 22));
		((ViewableComponent) withName("Robot1-controller")).setPreferredLocation(new Point(157, 31));
	}
}
