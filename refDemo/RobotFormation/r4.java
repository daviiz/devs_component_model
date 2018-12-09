package refDemo.RobotFormation;

import GenCol.doubleEnt;
import model.modeling.message;
import refDemo.PursuerEvader.vect2DEnt;

public class r4 extends singlerobot {
	protected double L, T, v;
	protected vect2DEnt velocity, positionin, position;
	protected String formation;
	protected boolean change;

	public r4() {
		this("Robot4");
	}

	public r4(String name) {
		super(name);
	}

	public void initialize() {
		position = new vect2DEnt(0, 0);
		velocity = new vect2DEnt(0, 0);
		change = false;
		holdIn("passive", INFINITY);
		super.initialize();
	}

	public void deltext(double e, message x) {
		Continue(e);
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "L", i)) {
				L = ((doubleEnt) x.getValOnPort("L", i)).getv();
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "T", i)) {
				T = ((doubleEnt) x.getValOnPort("T", i)).getv();
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "formation", i)) {
				formation = (x.getValOnPort("formation", i)).getName();
				phase = formation;
				change = true;
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "stop", i))
				passivate();
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "pin", i)) {
				positionin = (vect2DEnt) (x.getValOnPort("pin", i));
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "vin", i)) {
				position.x = position.x + e * velocity.x;
				position.y = position.y + e * velocity.y;
				velocity = (vect2DEnt) (x.getValOnPort("vin", i));
				v = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
				change = true;
				if (!phaseIs("passive"))
					holdIn("output", 3 * T / 100);
			}
		}
	}

	public void deltint() {
		if (phaseIs("output"))
			holdIn(formation, T);
		else if (change == true) {
			change = false;
			if (phaseIs("line")) {
				position.x = positionin.x - 3 * velocity.x * L / v;
				position.y = positionin.y - 3 * velocity.y * L / v;
				holdIn("output", 3 * T / 100);
			} else if (phaseIs("block")) {
				position.x = positionin.x + velocity.y * L / v - velocity.x * L / v;
				position.y = positionin.y - velocity.x * L / v - velocity.y * L / v;
				holdIn("output", 3 * T / 100);
			} else {
				position.x = positionin.x - Math.sqrt(3) * velocity.x * L / v + velocity.y * L / v;
				position.y = positionin.y - Math.sqrt(3) * velocity.y * L / v - velocity.x * L / v;
				holdIn("output", 3 * T / 100);
			}
		} else {
			position.x = position.x + velocity.x * T;
			position.y = position.y + velocity.y * T;
			holdIn("output", 3 * T / 100);
		}

	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	public message out() {
		message m = new message();
		if (phaseIs("output"))
			m.add(makeContent("pout", position));
		return m;
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "position:" + position + "\n" + "positionin:" + positionin + "\n"
				+ "velocity:" + velocity + "\n" + "L:" + L + "\n" + "T:" + T;
	}

}
