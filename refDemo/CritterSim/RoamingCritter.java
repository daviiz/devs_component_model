/*
 * Package erikhix.Critters
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
 * RoamingCritter extends Critter.  The RoamingCritter simply moves around the board.  It was
 * developed before the model was fully designed.  While the critter does behave correctly on
 * an empty board, it does not properly respond to other critters that attack it or eat it.
 * It is included only as a sample.
 */
public class RoamingCritter extends Critter{
	public static final String IDLE = "Idle";
	public static final String REQUESTING = "Requesting";
	public static final String WAITING = "Waiting";
	public static final String MOVING = "Moving";
	public static final String REPLYING = "Replying";
	public static final String BROADCASTING = "Broadcasting";
	
	public static final Color color = Color.yellow;
	
	protected double delay = 0;
	protected double elapsed = 0;
	protected message sending = null;
	protected String direction = null;
	private boolean[][] map = null;
	
	public RoamingCritter() {
		this("RoamingCritter");
	}

	public RoamingCritter(String name) {
		this(name, 0,0,1,1);
	}
	
	public RoamingCritter(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}

	public RoamingCritter(String name, int x, int y, int width, int height, long randomSeed) {
		super(name, x, y, width, height, randomSeed);		
	}
	
	public void initialize(){
		super.initialize();
		if(map == null)
			map = new boolean[3][3];
		delay = rand.nextInt(30) + 1;
		clearMap();
		holdIn(BROADCASTING,0);
	}

	public void deltint(){
		if(phaseIs(IDLE)){
			//It's time to migrate...  let's make our request
			direction = getDirection();
			if(direction == null){
				//We can't pick a direction to migrate, let's wait around again
				delay = rand.nextInt(30) + 1;
				holdIn(IDLE,delay);
			}else{
				request(direction);
				holdIn(REQUESTING,0);
			}
		}else if(phaseIs(REQUESTING)){
			//We just sent a request, now let's wait for a response
			holdIn(WAITING,5);
		}else if(phaseIs(WAITING)){
			//We just gave up waiting for a response...  Let's try a diffeent direction
			holdIn(IDLE,0);
		}else if(phaseIs(MOVING)){
			//We just moved, let's let people know we are around...
			clearMap();
			holdIn(BROADCASTING,0);
			//we also need a new delay value...
			delay = rand.nextInt(30) + 1;
		}else if(phaseIs(BROADCASTING)){
			//We just broadcasted, let's keep waiting...
			if(delay > 0.000001){
				holdIn(IDLE,delay);
			}else{
				holdIn(MOVING,0);
			}
		}else if(phaseIs(REPLYING)){
			//We just replyed, let's keep waiting...
			if(delay > 0.000001){
				holdIn(IDLE,delay);
			}else{
				holdIn(MOVING,0);
			}
		}else{
			//System.out.println(name + "ERROR:Found unknown phase: " + phase);
		}
	}
	
	public void deltext(double e, message x){
		Continue(e);
		delay -= e;
		sending = new message();
		CritterEvent reply = new CritterEvent(this, CritterEvent.BROADCAST);
		ContentIteratorInterface it = x.mIterator();
		while(it.hasNext()){
			content c = (content)it.next();
			if(! (c.getValue() instanceof CritterEvent)){
				//System.out.println(name+":ERROR:  receievd unknown entity: " + c.getValue());
				break;
			}
			CritterEvent event = (CritterEvent)c.getValue();
			switch(event.getType()){
				case CritterEvent.BROADCAST:
				markMap(c);
				respond(sending, c);
				holdIn(REPLYING, 0);
				break;
				case CritterEvent.REPLY:
				case CritterEvent.LEAVING:
				//change the map
				markMap(c);
				break;
				case CritterEvent.MIGRATING:
				//Yikes!  A Critter is trying to migrate here!
				//System.out.println(name + ":ERROR: Received migration on port: " + c.getPortName());
				break;
				case CritterEvent.MIGRATE_REQUEST:
				//send a negative response
				respond(sending, c);
				holdIn(REPLYING,0);
				break;
				case CritterEvent.MIGRATE_DENIED:
				if(phaseIs(WAITING)){
					//We were denied!  Try again
					holdIn(IDLE,0);
				}
				break;
				case CritterEvent.MIGRATE_OK:
				//We were accepted - let's go!
				migrate(direction);
				holdIn(MOVING,0);
				break;
				default:
				//We got a strange entity...
				break;
			}//end switch
		}
	}
	
	public message out(){
		message m = new message();
		if(phaseIs(BROADCASTING)){
			broadcast(m, new CritterEvent(this, CritterEvent.BROADCAST));
		}else if(phaseIs(REPLYING)){
			m = sending;
			sending = null;
		}
		return m;
	}	

	public Color getColor(){ return color; }
	
	public String getTooltipText(){
		return super.getTooltipText()
		/*
		+ "\n[" + 
		(map[0][0] ? "T" : "F") + "] [" +
		(map[1][0] ? "T" : "F") + "] [" +
		(map[2][0] ? "T" : "F") + "]\n[" +
		(map[0][1] ? "T" : "F") + "] [X] [" +
		(map[2][1] ? "T" : "F") + "]\n[" +
		(map[0][2] ? "T" : "F") + "] [" +
		(map[1][2] ? "T" : "F") + "] [" +
		(map[2][2] ? "T]" : "F]")
		*/
		;
	}
	
	/**
	 * HELPER FUNCTIONS BELOW
	 */
	
	protected void respond(message m, content c){
		String port = c.getPortName();
		Object o = c.getValue();
		if(! (o instanceof CritterEvent))
			return;
		CritterEvent e = (CritterEvent)o;
		CritterEvent ev;
		//check for ecvent type and generate the correct response
		switch(e.getType()){
			case CritterEvent.BROADCAST:
			ev = new CritterEvent(this,CritterEvent.REPLY);
			break;
			case CritterEvent.MIGRATE_REQUEST:
			ev = new CritterEvent(this,CritterEvent.MIGRATE_DENIED);
			break;
			default:
			//We don't respond to these events:
			return;
		}//end switch
		if(port.compareTo(inN) == 0)		m.add(makeContent(outN, ev));
		else if(port.compareTo(inNE) == 0)	m.add(makeContent(outNE, ev));
		else if(port.compareTo(inE) == 0)	m.add(makeContent(outE, ev));
		else if(port.compareTo(inSE) == 0)	m.add(makeContent(outSE, ev));
		else if(port.compareTo(inS) == 0)	m.add(makeContent(outS, ev));
		else if(port.compareTo(inSW) == 0)	m.add(makeContent(outSW, ev));
		else if(port.compareTo(inW) == 0)	m.add(makeContent(outW, ev));
		else if(port.compareTo(inNW) == 0)	m.add(makeContent(outNW, ev));
	}
	
	protected void markMap(content c){
		Object o = c.getValue();
		if(! (o instanceof CritterEvent))
			return;
		CritterEvent e = (CritterEvent)o;
		String port = c.getPortName();
		//check for invalid entities...
		if(e.getType() == CritterEvent.MIGRATING){
			//System.out.println(name + ":ERROR: Received migration on port: " + port);
			return;
		}else if((e.getType() == CritterEvent.EATING) ||
				(e.getType() == CritterEvent.NO_TYPE)){
			//we don't handle these here
			return;
		}
		boolean mark = false;
		if((e.getType() == CritterEvent.REPLY) ||
		  (e.getType() == CritterEvent.BROADCAST)){
			mark = true;
		}else if(e.getType() == CritterEvent.LEAVING){
			mark = false;
		}
		
		if(port.compareTo(inN) == 0)		map[1][0] = mark;
		else if(port.compareTo(inNE) == 0)	map[2][0] = mark;
		else if(port.compareTo(inE) == 0)	map[2][1] = mark;
		else if(port.compareTo(inSE) == 0)	map[2][2] = mark;
		else if(port.compareTo(inS) == 0)	map[1][2] = mark;
		else if(port.compareTo(inSW) == 0)	map[0][2] = mark;
		else if(port.compareTo(inW) == 0)	map[0][1] = mark;
		else if(port.compareTo(inNW) == 0)	map[0][0] = mark;
	}
	
	protected String getDirection(){
		String dir = null;
		//We try 100 times or until we find a direction we can go.
		//We limit it just in case we are surrounded (or in 1 1-cell board)...
		for(int attempt = 0; (attempt < 100 && dir==null); attempt++){
			int choice = rand.nextInt(8);
			switch(choice){
				case 0:
					if(canMigrateN() && !map[1][0])		dir = outN;
					break;
				case 1:
					if(canMigrateNE() && !map[2][0])	dir = outNE;
					break;
				case 2:
					if(canMigrateE() && !map[2][1])		dir = outE;
					break;
				case 3:
					if(canMigrateSE() && !map[2][2])	dir = outSE;
					break;
				case 4:
					if(canMigrateS() && !map[1][2])		dir = outS;
					break;
				case 5:
					if(canMigrateSW() && !map[0][2])	dir = outSW;
					break;
				case 6:
					if(canMigrateW() && !map[0][1])		dir = outW;
					break;
				case 7:
					if(canMigrateNW() && !map[0][0])	dir = outNW;
					break;
			}//end switch
		}//end for loop
		return dir;
	}
	
	protected void clearMap(){
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				map[i][j] = false;
	}
}
