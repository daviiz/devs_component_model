package ComponetAntiAirFleet;

import GenCol.entity;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableDigraph;

public class gpt2 extends ViewableDigraph {

	public gpt2() {
		super("gpt2");

		ViewableAtomic g = new genr("g", 10);
		ViewableAtomic p = new proc("p", 5);
		ViewableAtomic p2 = new proc2("p2", 100);
		ViewableAtomic t = new transd("t", 300);

		add(g);
		add(p);
		add(p2);
		add(t);

		addInport("in");
		addInport("start");
		addInport("stop");
		addOutport("out");
		addOutport("result");

		addTestInput("start", new entity());
		addTestInput("stop", new entity(), 5.0);

		addCoupling(this, "in", g, "in");

		addCoupling(this, "start", g, "start");
		addCoupling(this, "stop", g, "stop");

		addCoupling(g, "out", p, "in");
		addCoupling(g, "out", p2, "in");

		addCoupling(g, "out", t, "ariv");
		addCoupling(p, "out", t, "solved");
		addCoupling(p2, "out", t, "solved");
		addCoupling(t, "out", g, "stop");

		addCoupling(p, "out", this, "out");
		addCoupling(p2, "out", this, "out");
		addCoupling(t, "out", this, "result");

		// initialize();
		// showState();
		/*
		 * preferredSize = new Dimension(484, 145); g.setPreferredLocation(new Point(13,
		 * 18)); p.setPreferredLocation(new Point(195, 18)); t.setPreferredLocation(new
		 * Point(193, 80));
		 */
	}

}