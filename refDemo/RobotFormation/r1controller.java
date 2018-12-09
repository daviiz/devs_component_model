package refDemo.RobotFormation;

import GenCol.doubleEnt;
import model.modeling.message;
import refDemo.PursuerEvader.vect2DEnt;

public class r1controller extends singlerobot {
	protected double L, T, v;
	protected vect2DEnt velocity, position;
	protected String formation;

	public r1controller() {
		this("Robot1");
	}

	public r1controller(String name) {
		super(name);
	}

	public void initialize() {
		position = new vect2DEnt(0, 0);
		velocity = new vect2DEnt(0, 0);
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
			}
		}
		for (int i = 0; i < x.getLength(); i++) {
			if (messageOnPort(x, "stop", i))
				passivate();
		}
		for (int i = 0; i < x.getLength(); i++) {
			position.x = position.x + e * velocity.x;
			position.y = position.y + e * velocity.y;
			if (messageOnPort(x, "vin", i)) {
				velocity = (vect2DEnt) (x.getValOnPort("vin", i));
				v = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
				holdIn("change", 0);
			}
		}
	}

	public void deltint() {
		if (phaseIs("change") || phaseIs("output"))
			holdIn(formation, T);
		else {
			position.x = position.x + velocity.x * T;
			position.y = position.y + velocity.y * T;
			holdIn("output", 0);
		}
	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	public message out() {
		message m = new message();
		if (phaseIs("change") || phaseIs("output"))
			m.add(makeContent("pout", position));
		if (phaseIs("change"))
			m.add(makeContent("vout", velocity));
		return m;
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "position:" + position + "\n" + "velocity:" + velocity + "\n" + "L:" + L
				+ "\n" + "T:" + T;
	}

}
