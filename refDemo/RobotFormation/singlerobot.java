package refDemo.RobotFormation;

public class singlerobot extends realDevs {
	public singlerobot() {
		this("singlerobot");
	}

	public singlerobot(String name) {
		super(name);

		addInport("vin");
		addInport("pin");
		addInport("formation");
		addInport("L");
		addInport("T");
		addInport("stop");
		addOutport("vout");
		addOutport("pout");
	}

	public static int sign(double x) {
		if (x >= 0)
			return 1;
		else
			return -1;
	}

}
