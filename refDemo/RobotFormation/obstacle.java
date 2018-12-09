package refDemo.RobotFormation;

import model.modeling.message;
import refDemo.PursuerEvader.vect2DEnt;

public class obstacle extends realDevs {

	protected String name;
	protected vect2DEnt start1, start2, end1, end2, vin, pin;
	protected vect2DEnt outstartx, outstarty, outendx, outendy;
	protected double length;

	public obstacle() {
		this("testObstacle", new vect2DEnt(0, 0), new vect2DEnt(0, 0), new vect2DEnt(0, 0), new vect2DEnt(0, 0));
	}

	public obstacle(String name, vect2DEnt p1, vect2DEnt p2, vect2DEnt p3, vect2DEnt p4) {
		super(name);
		this.name = name;
		this.start1 = p1;
		this.start2 = p2;
		this.end1 = p3;
		this.end2 = p4;

		addInport("pin");
		addInport("vin");
		addOutport("startx");
		addOutport("starty");
		addOutport("endx");
		addOutport("endy");
	}

	public void initialize() {
		super.initialize();
		outstartx = new vect2DEnt(0, 0);
		outstarty = new vect2DEnt(0, 0);
		outendx = new vect2DEnt(0, 0);
		outendy = new vect2DEnt(0, 0);
		vin = new vect2DEnt(0, 0);
		pin = new vect2DEnt(0, 0);
		length = 20;
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x) {
		Continue(e);
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "in", i)) {
				holdIn("display", 0);
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "vin", i)) {
				vin = (vect2DEnt) x.getValOnPort("vin", i);
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "pin", i)) {
				pin = (vect2DEnt) x.getValOnPort("pin", i);
			}
			if ((vin.x > 0) && ((start1.x - pin.x) <= length)) {
				if ((pin.y < start1.y) && (pin.y > start2.y)) {
					outstartx.x = start1.x;
					outstartx.y = start1.x;
					outstarty.x = start1.y;
					outstarty.y = start2.y;
					outendx.x = end1.x;
					outendx.y = end1.x;
					outendy.x = end1.y;
					outendy.y = end2.y;
					holdIn("output", 0);
				}
			}
			if ((vin.x < 0) && ((pin.x - end1.x) <= length)) {
				if ((pin.y < start1.y) && (pin.y > start2.y)) {
					outstartx.x = end1.x;
					outstartx.y = end1.x;
					outstarty.x = end1.y;
					outstarty.y = end2.y;
					outendx.x = start1.x;
					outendx.y = start1.x;
					outendy.x = start1.y;
					outendy.y = start2.y;
					holdIn("output", 0);
				}
			}
		}
	}

	public void deltint() {
		holdIn("passive", INFINITY);
	}

	public message out() {
		message m = new message();
		if (phaseIs("output")) {
			m.add(makeContent("startx", outstartx));
			m.add(makeContent("starty", outstarty));
			m.add(makeContent("endx", outendx));
			m.add(makeContent("endy", outendy));
		} else {
			m.add(makeContent("startx", start1));
			m.add(makeContent("starty", start2));
			m.add(makeContent("endx", end1));
			m.add(makeContent("endy", end2));
		}
		return m;
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "Obstacle  :" + name + "\n" + "start1:" + start1 + "\n" + "start2:"
				+ start2 + "\n" + "end1:" + end1 + "\n" + "end2:" + end2 + "\n" + "v:" + vin + "\n" + "p1:" + pin;
	}

}
