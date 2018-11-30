/*     
 *    
 *  Author     : ACIMS(Arizona Centre for Integrative Modeling & Simulation)
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */
package ComponetAntiAirFleet;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class genr2 extends ViewableAtomic {

	protected double int_arr_time = 30;
	protected int count;//统计发射多少颗导弹
	protected int success;//统计命中发射多少颗导弹
	static int c = 0;

	public genr2() {
		this("genr", 30);
	}

	public genr2(String name, double Int_arr_time) {
		super(name);
		addInport("in");
		addOutport("out");
		addOutport("out2");
		addInport("stop");
		addInport("start");
		int_arr_time = Int_arr_time;

		addTestInput("start", new entity(""));
		addTestInput("stop", new entity(""));
		addTestInput("in", new entity(""));//红方导弹命中目标，接收标识
	}

	public void initialize() {
		holdIn("active", int_arr_time);

		// phase = "passive";
		// sigma = INFINITY;
		sigma = 0;
		count = 0;
		success = 0;
		super.initialize();
	}

	public void deltext(double e, message x) {
		Continue(e);
		System.out.println("******************** elapsed time for generator is " + e + "************************");
		entity ent = null;
		if(count == 10) {
			phase = "finishing";
		}
		if (phaseIs("passive")) {
			for (int i = 0; i < x.getLength(); i++) {
				if (messageOnPort(x, "start", i)) {
					
					holdIn("active", int_arr_time);
				}
				if(messageOnPort(x, "in", i)) {
					ent = x.getValOnPort("in", i);
					
					if(ent.eq("success")&&(count < 10)) {
						success++;
						//再发一发导弹
						holdIn("active", 1);
					}
					if(ent.eq("true")&&(count < 10) ) {//导弹被击毁
						//	再发一发导弹
						holdIn("active", 1);
					}
				}
				
			}
		}
		if (phaseIs("active")) {
			for (int i = 0; i < x.getLength(); i++) {
				
				if (messageOnPort(x, "stop", i)) {
					phase = "finishing";
				}
				
			}
		}
	}

	public void deltint() {
		/*
		 * System.out.println(name+" deltint count "+count);
		 * System.out.println(name+" deltint int_arr_time "+int_arr_time);
		 * System.out.println(name+" deltint tL "+tL);
		 * System.out.println(name+" deltint tN "+tN);
		 */

		// System.out.println("********generator**********" + c);
		
		count = count + 1;
		if(count > 9) {
			passivate();
		}
		
		
	}

	public message out() {

		// System.out.println(name+" out count "+count);

		message m = new message();
		if(phaseIs("active")) {
			content con = makeContent("out", new entity("missile" + (count+1)));
			m.add(con);
			content con2 = makeContent("out2", new entity("hit"+success));
			m.add(con2);
			passivate();
		}
		

		return m;
	}

	public void showState() {
		super.showState();
		System.out.println(" int_arr_time: " + int_arr_time + 
				"" + " count: " + count+ " ,hit: " + success);
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n" + " int_arr_time: " + int_arr_time + 
				"\n" + " count: " + count+ " ,hit: " + success;
	}

}
