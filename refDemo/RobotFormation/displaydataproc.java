package refDemo.RobotFormation;

import java.awt.Color;

import model.modeling.message;
import model.plots.DrawCellEntity;
import refDemo.PursuerEvader.vect2DEnt;
import view.modeling.ViewableAtomic;

public class displaydataproc extends ViewableAtomic {
	protected vect2DEnt position;
	protected vect2DEnt p1, p2, p3, p4;
	protected double x, y, period, clock;

	public displaydataproc() {
		this("DataProc");
	}

	public displaydataproc(String name) {
		super(name);

		addInport("p1in");
		addInport("p2in");
		addInport("p3in");
		addInport("p4in");
		addInport("p5in");
		addInport("p6in");
		addInport("o1");
		addInport("o2");
		addInport("o3");
		addInport("o4");
		addOutport("out");
	}

	public void initialize() {
		position = new vect2DEnt(0, 0);
		p1 = new vect2DEnt(0, 0);
		p2 = new vect2DEnt(0, 0);
		p3 = new vect2DEnt(0, 0);
		p4 = new vect2DEnt(0, 0);
		period = 0;
		clock = 0;
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x) {
		Continue(e);
		clock = clock + e;
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "p1in", i)) {
				position = (vect2DEnt) x.getValOnPort("p1in", i);
				holdIn("robot1", 0);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "p2in", i)) {
				position = (vect2DEnt) x.getValOnPort("p2in", i);
				holdIn("robot2", 0);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "p3in", i)) {
				position = (vect2DEnt) x.getValOnPort("p3in", i);
				holdIn("robot3", 0);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "p4in", i)) {
				position = (vect2DEnt) x.getValOnPort("p4in", i);
				holdIn("robot4", 0);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "p5in", i)) {
				position = (vect2DEnt) x.getValOnPort("p5in", i);
				holdIn("robot5", 0);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "p6in", i)) {
				position = (vect2DEnt) x.getValOnPort("p6in", i);
				holdIn("robot6", 0);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "o1", i)) {
				p1 = (vect2DEnt) x.getValOnPort("o1", i);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "o2", i)) {
				p2 = (vect2DEnt) x.getValOnPort("o2", i);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "o3", i)) {
				p3 = (vect2DEnt) x.getValOnPort("o3", i);
			}
		}
		for (int i = 0; i < x.size(); i++) {
			if (messageOnPort(x, "o4", i)) {
				p4 = (vect2DEnt) x.getValOnPort("o4", i);
				holdIn("display1", 0);
			}
		}
	}

	public void deltint() {
		if (clock == 0) {
			if (phaseIs("display1") || (phaseIs("o1"))) {
				x = p1.x + (p3.x - p1.x) * period / 10;
				y = p1.y;
				period++;
				if (period > 10) {
					holdIn("display2", 0);
					period = 0;
				} else
					holdIn("o1", 0);
			}
			if (phaseIs("display2") || (phaseIs("o2"))) {
				x = p2.x + (p4.x - p2.x) * period / 10;
				y = p2.y;
				period++;
				if (period > 10) {
					holdIn("passive", INFINITY);
					period = 0;
				} else
					holdIn("o2", 0);
			}
		} else
			holdIn("passive", INFINITY);
	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	public message out() {
		message m = new message();
		if (phaseIs("robot1"))
			m.add(makeContent("out", new DrawCellEntity(position.x, position.y, Color.red, Color.red)));
		else if (phaseIs("robot2"))
			m.add(makeContent("out", new DrawCellEntity(position.x, position.y, Color.pink, Color.pink)));
		else if (phaseIs("robot3"))
			m.add(makeContent("out", new DrawCellEntity(position.x, position.y, Color.yellow, Color.yellow)));
		else if (phaseIs("robot4"))
			m.add(makeContent("out", new DrawCellEntity(position.x, position.y, Color.white, Color.white)));
		else if (phaseIs("robot5"))
			m.add(makeContent("out", new DrawCellEntity(position.x, position.y, Color.green, Color.green)));
		else if (phaseIs("robot6"))
			m.add(makeContent("out", new DrawCellEntity(position.x, position.y, Color.blue, Color.blue)));
		else if ((phaseIs("o1")) || (phaseIs("o2")))
			m.add(makeContent("out", new DrawCellEntity(x, y, Color.black, Color.gray)));
		return m;
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "period:" + period;
	}

}
