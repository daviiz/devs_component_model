package refDemo.RobotFormation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import refDemo.PursuerEvader.vect2DEnt;

public class generator extends realDevs {

	protected double period;
	protected String Vx, Vy;
	protected String line;
	protected File file;
	protected BufferedReader buffer;
	protected String inputfilename;
	protected FileReader fis;
	protected double T;
	protected double L;
	protected String formation;

	public generator(String name, double period, String inputfilename) {
		super(name);
		this.period = period;
		this.inputfilename = inputfilename;
		addInport("start");
		addInport("inputL");
		addInport("inputT");
		addInport("formation");
		addOutport("velocity");
		addOutport("L");
		addOutport("T");
		addOutport("command");
		addOutport("stop");

		this.addNameTestInput("formation", "line", 0);
		this.addNameTestInput("formation", "block", 0);
		this.addNameTestInput("formation", "triangle", 0);
		this.addNameTestInput("start", "", 0);
		this.addRealTestInput("inputT", 10, 0);
		this.addRealTestInput("inputL", 60, 0);
	}

	public generator() {
		// this("generator",5,"FreeSpaceInput.txt");
		this("generator", 5, "./src/StudentProjects/FreeSpaceInput.txt");
	}

	public void initialize() {
		super.initialize();

		int index;
		// the file from where the data will be read
		try {
			file = new File(inputfilename);
		} catch (Exception e) {
			throw new RuntimeException("Didn't find file");
		}
		try {
			fis = new FileReader(file);
		} catch (Exception e) {
			throw new RuntimeException("Error constructing the reader");
		}
		buffer = new BufferedReader(fis);

		// get inital velocities from file
		/*
		 * try{line = buffer.readLine();} catch(Exception e) {throw new
		 * RuntimeException("Error reading");}
		 * 
		 * if(line == null) //no more velocities passivate();
		 * 
		 * index = line.indexOf(" ",0); Vx = line.substring(0,index); index++; Vy =
		 * line.substring(index,line.length());
		 */ holdIn("passivve", INFINITY);
	}

	public void deltext(double e, message x) {
		Continue(e);
		if (somethingOnPort(x, "start")) {
			holdIn("start", 0);
		}
		if (somethingOnPort(x, "inputT")) {
			T = this.getRealValueOnPort(x, "inputT");
			holdIn("outputT", 0);
		}
		if (somethingOnPort(x, "inputL")) {
			L = this.getRealValueOnPort(x, "inputL");
			holdIn("outputL", 0);
		}
		if (somethingOnPort(x, "formation")) {
			formation = this.getNameOnPort(x, "formation");
			holdIn("outputFormation", 0);
		}
	}

	public void deltint() {
		int index;

		if (phaseIs("generate") || phaseIs("start")) {
			// get new velocities from file
			try {
				line = buffer.readLine();
			} catch (Exception e) {
				throw new RuntimeException("Error reading");
			}
			if (line == null) // no more velocities
				holdIn("stop", 0);
			else {
				index = line.indexOf(" ", 0);
				Vx = line.substring(0, index);
				index++;
				Vy = line.substring(index, line.length());
				holdIn("generate", period);
			}
		} else
			holdIn("passive", INFINITY);
	}

	public message out() {
		message m = new message();

		if (phaseIs("outputT"))
			m.add(makeContent("T", new doubleEnt(T)));
		else if (phaseIs("outputL"))
			m.add(makeContent("L", new doubleEnt(L)));
		else if (phaseIs("outputFormation"))
			m.add(makeContent("command", new entity(formation)));
		else if (phaseIs("stop"))
			m.add(makeContent("stop", new entity("")));
		else if (phaseIs("generate")) {
			vect2DEnt p = new vect2DEnt(new Double(Vx).doubleValue(), new Double(Vy).doubleValue());
			m.add(makeContent("velocity", p));
		}
		return m;
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + "Dispatched velocities :" + Vx + " " + Vy;
	}

}
