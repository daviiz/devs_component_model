package refDemo.RobotFormation;

import model.modeling.message;
import view.modeling.ViewableAtomic;

public class camera extends ViewableAtomic {
	public camera() {
		this("Camera");
	}

	public camera(String name) {
		super(name);
		addInport("v");
		addInport("T");
		addInport("L");
		addInport("startx");
		addInport("starty");
		addInport("endx");
		addInport("endy");
		addOutport("command");
	}

	public void deltint() {
		holdIn("watch", INFINITY);
	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

}
