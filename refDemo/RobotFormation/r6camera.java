package refDemo.RobotFormation;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import refDemo.PursuerEvader.vect2DEnt;

public class r6camera extends camera {
	protected double T, L;
	protected vect2DEnt v, endx, endy, p6;
	protected String formation;
	protected double clock;

	public r6camera() {
		this("Camera");
	}

	public r6camera(String name) {
		super(name);
		addInport("pin6");
	}

	public void initialize() {
		holdIn("watch", INFINITY);
		endx = new vect2DEnt(0, 0);
		endy = new vect2DEnt(0, 0);
		p6 = new vect2DEnt(0, 0);
		v = new vect2DEnt(0, 0);
		formation = "triangle";
		clock = 0;
		super.initialize();
	}

	public void deltext(double e, message x) {
		clock = clock + e;
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "T", i)) {
				T = ((doubleEnt) x.getValOnPort("T", i)).getv();
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "L", i)) {
				L = ((doubleEnt) x.getValOnPort("L", i)).getv();
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "pin6", i)) {
				p6 = (vect2DEnt) x.getValOnPort("pin6", i);
				if (((p6.x - endx.x) >= 0) && (v.x > 0)) {
					System.out.println("Enter change back 6!");
					formation = "triangle";
					holdIn("output", 0);
				}
				if (((endx.x - p6.x) >= 0) && (v.x < 0)) {
					System.out.println("Enter change back 6!");
					formation = "triangle";
					holdIn("output", 0);
				}
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "v", i)) {
				v = vect2DEnt.toObject(x.getValOnPort("v", i));
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if ((clock > 0) && (messageOnPort(x, "endx", i))) {
				endx = vect2DEnt.toObject(x.getValOnPort("endx", i));
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if ((clock > 0) && (messageOnPort(x, "endy", i))) {
				endy = vect2DEnt.toObject(x.getValOnPort("endy", i));
			}
		}

	}

	public message out() {
		message m = new message();
		m.add(makeContent("command", new entity(formation)));
		return m;
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "endx:" + endx + "\n" + "endy:" + endy + "\n" + "formation:" + formation
				+ "\n" + "p6:" + p6 + "\n" + "L:" + L + "\n" + "v:" + v + "\n" + "T:" + T;
	}

}
