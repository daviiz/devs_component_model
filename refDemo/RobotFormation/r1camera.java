package refDemo.RobotFormation;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import refDemo.PursuerEvader.vect2DEnt;

public class r1camera extends camera {
	protected double T, L;
	protected vect2DEnt v, startx, starty, endx, endy, p1;
	protected String formation;
	protected double clock;

	public r1camera() {
		this("Camera");
	}

	public r1camera(String name) {
		super(name);
		addInport("pin1");
	}

	public void initialize() {
		holdIn("watch", INFINITY);
		startx = new vect2DEnt(600, 600);
		starty = new vect2DEnt(0, 0);
		endx = new vect2DEnt(1000, -1000);
		endy = new vect2DEnt(0, 0);
		p1 = new vect2DEnt(0, 0);
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
			if (messageOnPort(x, "pin1", i)) {
				p1 = (vect2DEnt) x.getValOnPort("pin1", i);
				if ((v.x > 0) && (Math.abs(startx.x - p1.x) < (v.x * T))) {
					System.out.println("Enter change!");
					if ((p1.y - starty.y) < L)
						formation = "line";
					else if ((starty.x - p1.y) < L)
						formation = "block";
					else
						formation = "triangle";
					holdIn("output", 0);
				} else if ((v.x < 0) && (Math.abs(p1.x - startx.x) < (-v.x * T))) {
					System.out.println("Enter change!");
					if ((starty.x - p1.y) < L)
						formation = "line";
					else if ((p1.y - starty.y) < L)
						formation = "block";
					else
						formation = "triangle";
					holdIn("output", 0);
				}
				if (((p1.x - endx.x) >= 0) && (v.x > 0)) {
					System.out.println("Enter change back!");
					formation = "triangle";
					holdIn("output", 0);
				}
				if (((endx.y - p1.x) >= 0) && (v.x < 0)) {
					System.out.println("Enter change back!");
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
			if ((clock > 0) && (messageOnPort(x, "startx", i))) {
				startx = vect2DEnt.toObject(x.getValOnPort("startx", i));
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if ((clock > 0) && (messageOnPort(x, "starty", i))) {
				starty = vect2DEnt.toObject(x.getValOnPort("starty", i));
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
		return super.getTooltipText() + "\n" + "startx:" + startx + "\n" + "starty:" + starty + "\n" + "endx:" + endx
				+ "\n" + "endy:" + endy + "\n" + "formation:" + formation + "\n" + "p1:" + p1 + "\n" + "L:" + L + "\n"
				+ "v:" + v + "\n" + "T:" + T;
	}

}
