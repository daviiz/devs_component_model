/*
 * Package erikhix.CritterSim
 * Written by Erik Hix
 * Class project
 * ECE - 575
 * The University of Arizona
 * Fall 2003
 * December 2003
 */
package refDemo.CritterSim;

import model.modeling.ContentIteratorInterface;
import model.modeling.content;
import model.modeling.message;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * CritterStats extends realDevs.  It listens to CritterEvents to track population by critter
 * type.  It reports changes in population to the CritterStatBox.
 */
public class CritterStats extends realDevs{
	public static final String IN = "in";

	protected int initV, initP, initA;
	protected int v, p, a;
	protected int vMax, pMax, aMax;
	protected double time;
	protected CritterStatBox box;

	public CritterStats(int initVeg, int initPas, int initAgg) {
		super("CritterStats");
		initV = initVeg;
		initP = initPas;
		initA = initAgg;

		box = new CritterStatBox();

		addInport(IN);
		setHidden(true);
	}

	public void initialize(){
		v = vMax = initV;
		p = pMax = initP;
		a = aMax = initA;
		
		time = 0;

		box.setVegCount(v);
		box.setVegMax(vMax);
		box.setPasCount(p);
		box.setPasMax(pMax);
		box.setAggCount(a);
		box.setAggMax(aMax);

		passivate();
	}

	public void deltext(double e, message x){
		time += e;
		ContentIteratorInterface it = x.mIterator();
		while(it.hasNext()){
			content c = (content)it.next();
			if(! (c.getValue() instanceof CritterEvent)){
				System.out.println("Found non-criterevent entity!");
				break;
			}
			CritterEvent event = (CritterEvent)c.getValue();
			Critter crit = event.getCritter();
			if(crit == null)
				break;
			//Now we examine the event type to make a count decision
			switch(event.getType()){
				case CritterEvent.BROADCAST:
				if(crit instanceof Vegetation)
					addV();
				break;
				case CritterEvent.SPAWNING:
				if(crit instanceof Vegetation)
					addV();
				else if(crit instanceof PassiveCritter)
					addP();
				else if(crit instanceof AggressiveCritter)
					addA();
				break;
				case CritterEvent.DYING:
				if(crit instanceof Vegetation)
					subV();
				else if(crit instanceof PassiveCritter)
					subP();
				else if(crit instanceof AggressiveCritter)
					subA();
				break;
				default:
			}//end switch
		}//end while
	}

	private void addV(){
		v++;
		box.setVegCount(v);
		if(v>vMax){
			vMax = v;
			box.setVegMax(vMax);
		}
	}

	private void subV(){
		v--;
		box.setVegCount(v);
	}

	private void addA(){
		a++;
		box.setAggCount(a);
		if(a > aMax){
			aMax = a;
			box.setAggMax(aMax);
		}
	}

	private void subA(){
		a--;
		box.setAggCount(a);
		if(a == 0)
			System.out.println("CritterStats: AggressiveCount hit zero at " + time);
	}

	private void addP(){
		p++;
		box.setPasCount(p);
		if(p>pMax){
			pMax = p;
			box.setPasMax(pMax);
		}
	}

	private void subP(){
		p--;
		box.setPasCount(p);
		if(p == 0)
			System.out.println("CritterStats: PassiveCount hit zero at " + time);
	}
}
