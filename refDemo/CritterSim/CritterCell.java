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

import model.modeling.ContentIteratorInterface;
import model.modeling.content;
import model.modeling.message;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * CritterCell implements one space of the Critter board.  The Cell has an out and in port for
 * each of the 8 directions.
 */
public class CritterCell extends realDevs{
	//in ports - use same names as Critter for clarity
	public static final String inN			= Critter.inN;
	public static final String inNE			= Critter.inNE;
	public static final String inE			= Critter.inE;
	public static final String inSE			= Critter.inSE;
	public static final String inS			= Critter.inS;
	public static final String inSW			= Critter.inSW;
	public static final String inW			= Critter.inW;
	public static final String inNW			= Critter.inNW;
	//out ports	
	public static final String outN			= Critter.outN;
	public static final String outNE		= Critter.outNE;
	public static final String outE			= Critter.outE;
	public static final String outSE		= Critter.outSE;
	public static final String outS			= Critter.outS;
	public static final String outSW		= Critter.outSW;
	public static final String outW			= Critter.outW;
	public static final String outNW		= Critter.outNW;
	public static final String eventsOut	= Critter.eventsOut;
	//phases
	public static final String EMPTY	= "Empty";
	public static final String INCOMING	= "Incoming";
	public static final String SENDING	= "Cell Sending";
	
	protected static final int growthRate = 30;
	//class vars
	protected final int xcoord;
	protected final int ycoord;
	protected final int width;
	protected final int height;
	protected Critter initCritter;
	protected Critter critter;
	protected Vegetation veg;
	protected boolean incoming;
	protected message outgoing;
	protected double delay;

	public CritterCell() {
		this("CritterCell");
	}

	public CritterCell(String name) {
		this(name,0,0,1,1);
	}

	public CritterCell(String name, int xcoord, int ycoord, int width, int height) {
		this(name, xcoord, ycoord, width, height, null);
	}
	
	public CritterCell(String name, int xcoord, int ycoord, int width, int height, Critter c) {
		super(name);
		//store coord and dimensions...
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.width = width;
		this.height = height;
		this.initCritter = c;
		//initialize the vegetation...
		veg = new Vegetation("Veggies", xcoord, ycoord, width, height);
		veg.initialize();
		//add in ports
		addInport(inN);		addInport(inNE);	addInport(inE);	addInport(inSE);
		addInport(inS);		addInport(inSW);	addInport(inW);	addInport(inNW);
		//add out ports
		addOutport(outN);	addOutport(outNE);	addOutport(outE);	addOutport(outSE);
		addOutport(outS);	addOutport(outSW);	addOutport(outW);	addOutport(outNW);
		addOutport(eventsOut);
	}
	
	public Critter setInitCritter(Critter c){
		return initCritter = c;
	}
	
	public Critter getInitCritter(){
		return initCritter;
	}

	public void initialize(){
		//we don't have any incoming critters yet
		incoming = false;
		outgoing = new message();
		delay = growthRate;
		//initialize the critter if we have one
		if(initCritter != null){
			setCritter(initCritter);
			critter.initialize();
			holdIn(critter.getPhase(), critter.getSigma());
		//If we don't have a critter, maybe we should plant some vegetation
		}else if(Vegetation.plantAnother()){
			setCritter(veg);
			critter.initialize();
			holdIn(critter.getPhase(), critter.getSigma());
		//Otherwise, we should wait a while...
		}else{
			setCritter(null);
			holdIn(EMPTY, delay);
		}
	}

	public void deltint(){
		if(critter != null){
			//Is the critter mirating or dying?
			if(critter.requesting()){
				//We just sent the migration, now we need to clean up
				critter.requestSent();
				critter.deltint();
				holdIn(critter.getPhase(), critter.getSigma());				
			}else if(critter.migrating()){
				//We just sent the migration, now we need to clean up
				critter.migrationOK();
				critter.deltint();
				setCritter(null);
				delay = growthRate;
				holdIn(EMPTY, delay);
			}else if(critter.dying()){
				//We just send the death message...
				critter.deathOK();
				setCritter(null);
				delay = growthRate;
				holdIn(EMPTY, delay);
			}else{
				//Do what the critter does...
				critter.deltint();
				holdIn(critter.getPhase(), critter.getSigma());
			}
		}else{//if no critter...
			if(phaseIs(EMPTY)){
				if(Vegetation.plantAnother() && veg != null){
					//We should grow some food
					setCritter(veg);
					critter.initialize();
					holdIn(critter.getPhase(), critter.getSigma());
				}else{
					//Wait for now, maybe we'll grow some food later...
					delay = growthRate;
					holdIn(EMPTY, delay);
				}
			}else if(phaseIs(SENDING)){
				if(incoming){
					//Wait for the migration
					holdIn(INCOMING,5);
				}else{
					//we can continue growing food
					holdIn(EMPTY,delay);
				}
			}else if(phaseIs(INCOMING)){
				//We did not receive a Critter in time
				incoming = false;
				holdIn(EMPTY,delay);
			}
		}
	}

	public void deltext(double e, message m){
		if(critter != null){
			//Do what the critter does...
			critter.deltext(e,m);
			holdIn(critter.getPhase(), critter.getSigma());			
		}else{
			Continue(e);
			delay -= e;
			//Let's see if we have any migrations or migration requests...
			ContentIteratorInterface it = m.mIterator();
			while(it.hasNext()){
				content c = (content)it.next();
				if(! (c.getValue() instanceof CritterEvent)){
					//ignore this
					break;
				}
				CritterEvent event = (CritterEvent)c.getValue();
				switch(event.getType()){
					case CritterEvent.MIGRATE_REQUEST:
					respond(outgoing, c);
					holdIn(SENDING,0);
					break;
					case CritterEvent.MIGRATING:
					case CritterEvent.SPAWNING:
					if(incoming){
						//we need to inherit this critter...
						setCritter(event.getCritter());
						incoming = false;
						holdIn(critter.getPhase(), critter.getSigma());
					}else{
						//We need to ignore this critter
						/*System.out.println(name + 
						":ERROR:Received migration/spawn without prior approval from Critter: " + 
						event.getCritter().getName());
						*/
					}
				}//end switch
			}//end while
		}//end else (if no critter)
	}//end deltext

	public message out(){
		message m;
		if(phaseIs(SENDING)){
			//send the outgoing message
			m = outgoing;
			outgoing = new message();
		}else if(critter != null){
			//Is our critter leaving?
			if(critter.requesting()){
				m = critter.requestMessage();
			}else if(critter.migrating()){
					m = critter.migrationMessage();
			}else if(critter.dying()){
				m = critter.dyingMessage();
			}else{
				m = critter.out();
			}
		}else{
			//no critter, so send our own message
			m = new message();
		}
		return m;
	}//end out()
	
	public String getTooltipText(){
		String result = super.getTooltipText();
		if(critter == null)
			result += "\nCell EMPTY";
		else
			result += "\nCell OCCUPIED:\n" + critter.getTooltipText();
		return result;
	}
	
	protected synchronized void respond(message m, content c){
		Object o = c.getValue();
		if(! (o instanceof CritterEvent))
			return;
		CritterEvent e = (CritterEvent)o;
		//check for invalid entities...
		if(e.getType() != CritterEvent.MIGRATE_REQUEST){
			return;
		}
		String port = c.getPortName();
		CritterEvent ev;
		if(incoming){
			ev = new CritterEvent(null,CritterEvent.MIGRATE_DENIED);			
		}else{
			incoming = true;
			ev = new CritterEvent(null,CritterEvent.MIGRATE_OK);
		}
		if(port.compareTo(inN) == 0) 		m.add(makeContent(outN, ev));
		else if(port.compareTo(inNE) == 0)	m.add(makeContent(outNE, ev));
		else if(port.compareTo(inE) == 0)	m.add(makeContent(outE, ev));
		else if(port.compareTo(inSE) == 0)	m.add(makeContent(outSE, ev));
		else if(port.compareTo(inS) == 0)	m.add(makeContent(outS, ev));
		else if(port.compareTo(inSW) == 0)	m.add(makeContent(outSW, ev));
		else if(port.compareTo(inW) == 0)	m.add(makeContent(outW, ev));
		else if(port.compareTo(inNW) == 0)	m.add(makeContent(outNW, ev));
	}
	
	protected void setCritter(Critter c){
		critter = c;
		if(c == null){
			setBackgroundColor(Color.gray);
		}else{
			setBackgroundColor(c.getColor());
		}
	}
}
