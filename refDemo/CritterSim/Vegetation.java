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

import java.awt.Color;
import java.util.Random;

import model.modeling.ContentIteratorInterface;
import model.modeling.content;
import model.modeling.message;
/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * RoamingCritter extends Critter.  The RoamingCritter simply moves around the board.
 */
public class Vegetation extends Critter{
	public static final String LIVING = "Living";
	public static final String DYING = "Dying";
	public static final String DEAD = "DEAD";
	public static final String REPLYING = "Replying";
	public static final String BROADCASTING = "Broadcasting";
	
	public static final Color color = new Color(128,255,128);
	
	//The natural lifespan of vegetation...	
	protected static final double lifeSpan = 50;
	
	//The natural lifespan variation...
	protected static final double lifeDev = .20;
	
	//The natural population density of vegetation
	protected static final double density = .20;

	//A random numnber generator to use for the entire Vegetation class...
	protected static Random rand = new Random(0);
	
	protected double delay;
	protected message sending;
	
	public Vegetation() {
		this("Vegetation");
	}

	public Vegetation(String name){
		this(name, 0, 0, 1, 1);
	}
	public Vegetation(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}
	
	public void initialize(){
		super.initialize();
		sending = new message();
		holdIn(BROADCASTING,0);
	}

	public void deltint(){
		if(phaseIs(LIVING)){
			//It's time to die...
			amDying();
			holdIn(DYING,0);
		}else if(phaseIs(BROADCASTING)){
			delay = (int)((1.0 - lifeDev)*lifeSpan) + rand.nextInt(1+(int)(lifeDev*2*lifeSpan));
			//Let's keep waiting to die...
			if(delay > 0.000001){
				holdIn(LIVING,delay);
			}else{
				amDying();
				holdIn(DYING,0);
			}
		}else if(phaseIs(REPLYING)){
			//Let's keep waiting to die...
			if(delay > 0.000001){
				holdIn(LIVING,delay);
			}else{
				amDying();
				holdIn(DYING,0);
			}
		}else if(phaseIs(DYING)){
			passivateIn(DEAD);
		}else{
			//System.out.println(name + "ERROR:Found unknown phase: " + phase);
		}
	}
	
	public void deltext(double e, message x){
		Continue(e);
		delay -= e;
		CritterEvent reply = new CritterEvent(this, CritterEvent.BROADCAST);
		ContentIteratorInterface it = x.mIterator();
		while(it.hasNext()){
			content c = (content)it.next();
			if(! (c.getValue() instanceof CritterEvent)){
				//System.err.println(name+":ERROR:  receievd unknown entity: " + c.getValue());
				break;
			}
			CritterEvent event = (CritterEvent)c.getValue();
			switch(event.getType()){
				case CritterEvent.BROADCAST:
				respond(sending, c);
				holdIn(REPLYING, 0);
				break;
				case CritterEvent.EATING:
				case CritterEvent.ATTACKING:
				amDying();
				holdIn(DYING,0);
				break;
				case CritterEvent.NO_TYPE:
				case CritterEvent.REPLY:
				case CritterEvent.LEAVING:
				//ignore
				break;
				case CritterEvent.MIGRATING:
				//Yikes!  A Critter is trying to migrate here!
				//System.out.println(name + ":ERROR: Received migration on port: " + c.getPortName());
				break;
				default:
				//We got a strange entity...
				break;
			}//end switch
		}
	}
	
	public message out(){
		message m = sending;
		sending = new message();
		if(phaseIs(BROADCASTING)){
			broadcast(m, new CritterEvent(this, CritterEvent.BROADCAST));
		}else if(phaseIs(REPLYING)){
			//We don't add anything
		}
		return m;
	}
	
	public static boolean plantAnother(){
		return (rand.nextDouble() <= density);
	}
	
	public Color getColor(){ return color; }
	
	//Resets the random number generator
	public static void reset(){
		rand = new Random(0);
	}

	/**************************
	 * HELPER FUNCTIONS BELOW *
	 **************************/
	
	protected void respond(message m, content c){
		Object o = c.getValue();
		if(! (o instanceof CritterEvent))
			return;
		CritterEvent e = (CritterEvent)o;
		//check for invalid entities...
		if(e.getType() != CritterEvent.BROADCAST){
			return;
		}
		String port = c.getPortName();
		CritterEvent ev = new CritterEvent(this,CritterEvent.REPLY);
		if(port.compareTo(inN) == 0) m.add(makeContent(outN, ev));
		else if(port.compareTo(inNE) == 0)	m.add(makeContent(outNE, ev));
		else if(port.compareTo(inE) == 0)	m.add(makeContent(outE, ev));
		else if(port.compareTo(inSE) == 0)	m.add(makeContent(outSE, ev));
		else if(port.compareTo(inS) == 0)	m.add(makeContent(outS, ev));
		else if(port.compareTo(inSW) == 0)	m.add(makeContent(outSW, ev));
		else if(port.compareTo(inW) == 0)	m.add(makeContent(outW, ev));
		else if(port.compareTo(inNW) == 0)	m.add(makeContent(outNW, ev));
	}
}
