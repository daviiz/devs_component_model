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
 * PassiveCritter extends Critter.  The PassiveCritter moves around the board, eats vegetation
 * and reproduces.  It does not attack other critters.
 */
public class PassiveCritter extends Critter{
	//Phases for this critter
	public static final String IDLE				= "Idle";
	public static final String REQUESTING		= "Requesting";
	public static final String REQUEST_SPAWN	= "Spawn Request";
	public static final String WAITING			= "Waiting";
	public static final String WAITING_SPAWN	= "Waiting to Spawn";
	public static final String MOVING			= "Moving";
	public static final String EATING			= "Eating";
	public static final String SPAWNING			= "Spawning";
	public static final String REPLYING			= "Replying";
	public static final String BROADCASTING		= "Broadcasting";
	public static final String DEAD				= "Dead";
	//The color we should draw this cell
	public static final Color color = Color.yellow;

	//The weight of the past hunger values when calculating cumulative hunger rate
	protected static final double historicalWeight = .80;
	//The minimum time between feeding.
	protected static final int minFeedInterval = 50;
	//The maximum time between feeding.
	protected static final int maxFeedInterval = 150;
	//The maximum hungerRate allowed for spawning.
	protected static final double maxHungerRate = .40;
	//The minimum age for spawning.
	protected static final double spawnAge = 75;

	//The maximum time we should wait between events
	protected static final int maxDelay = 30;

	protected double eatTimer = 0;		//The time since we last ate
	protected double spawnTimer = 0;	//The time since we last spawned
	protected double age = 0;			//The total lifespan of this critter
	protected double delay = 0;			//The delay before our next state change
	protected double hungerRate = 0;	//The averagerate of hunger - percent of maxFeedInterval we spend hungry
	protected int spawnCount = 0;		//The number of children we have spawned
	protected message sending = null;	//The next message we will be sending
	protected String myDir = null;		//The direction we are travelling
	protected String spawnDir = null;	//The direction we are going to spawn
	protected String eatDir = null;		//The direction we are going to be eating
	private Critter[][] map = null;		//A map of our surrounding neighbors.


	public PassiveCritter() {
		this("PassiveCritter");
	}

	public PassiveCritter(String name) {
		this(name, 0,0,1,1);
	}

	public PassiveCritter(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}

	public PassiveCritter(String name, int x, int y, int width, int height, long randomSeed) {
		super(name, x, y, width, height, randomSeed);
	}

	public void initialize(){
		super.initialize();
		if(map == null)
			map = new Critter[3][3];
		age = eatTimer = spawnTimer = hungerRate = 0;
		spawnCount = 0;
		sending = new message();
		myDir = getDirection();
		eatDir = spawnDir = null;
		//We start by broadcasting...
		broadcastSelf();
		holdIn(BROADCASTING,0);
	}

	public void deltint(){
		spawnTimer += sigma;
		eatTimer += sigma;
		age += sigma;
		delay -= sigma;

		//We select our next phase and sigma based on our current phase

		if(phaseIs(IDLE)){
			//We are ready for a new activity
			setNextPhase();
		}else if(phaseIs(REQUESTING)){
			//We just sent a request, now let's wait for a response
			delay = 5;
			holdIn(WAITING,delay);
		}else if(phaseIs(REQUEST_SPAWN)){
			//We just sent a spawn request, now let's wait for a response
			delay = 5;
			holdIn(WAITING_SPAWN,delay);
		}else if(phaseIs(WAITING)){
			//We just gave up waiting for a response...  Try again later
			if(delay > 0.0001)
				holdIn(WAITING,delay);
			else
				setIdle();
		}else if(phaseIs(WAITING_SPAWN)){
			//We just gave up waiting for a response...  Try again later
			if(delay > 0.0001)
				holdIn(WAITING_SPAWN,delay);
			else
				setIdle();
		}else if(phaseIs(MOVING)){
			//We just moved, let's let people know we are around...
			clearMap();
			broadcastSelf();
			holdIn(BROADCASTING,0);
		}else if(phaseIs(SPAWNING)){
			//We just spawned...  We're so proud!  Let's take a rest
			setIdle();
		}else if(phaseIs(EATING)){
			//We just ate.  Yummy!  Now let's take a nap...
			setIdle();
		}else if(phaseIs(REPLYING)){
			//We just replyed, let's keep waiting...
			holdIn(IDLE,delay);
		}else if(phaseIs(BROADCASTING)){
			//We just broadcasted, let's enjoy the scenery...
			setIdle();
		}else{
			//System.out.println(name + "ERROR:Found unknown phase: " + phase);
		}
	}

	public void deltext(double e, message x){
		Continue(e);
		//Update our timers and our delay
		delay -= e;
		spawnTimer += e;
		eatTimer += e;
		age += e;

		boolean replying = false;
		boolean spawning = false;
		boolean migrating = false;
		boolean idle = false;
		//Let's iterate through the messages
		ContentIteratorInterface it = x.mIterator();
		while(it.hasNext()){
			//We look at each message.  If it's not a CritterEvent, we get very angry!
			content c = (content)it.next();
			if(! (c.getValue() instanceof CritterEvent)){
				//System.err.println(name+":ERROR:  receievd unknown entity: " + c.getValue());
				break;
			}
			CritterEvent event = (CritterEvent)c.getValue();
			//Now we examine the event type to make a behavior decision
			switch(event.getType()){
			//Respond to eating and attacking events (by dying)
			case CritterEvent.EATING:
			case CritterEvent.ATTACKING:
				//Time to die!
				die();
				return;

			//Respond to broadcasts (and store the info in our map)
			case CritterEvent.BROADCAST:
				updateMap(c);
				respond(sending, c);
				replying = true;
				break;

			//Store the info in our map for replies, deaths, and leaving critters
			case CritterEvent.REPLY:
			case CritterEvent.LEAVING:
			case CritterEvent.DYING:
				updateMap(c);
				break;

			case CritterEvent.MIGRATING:
			case CritterEvent.SPAWNING:
				//Yikes!  A Critter is trying to occupy our cell!
				//System.out.println(name + ":ERROR: Received migration or spawn on port: " + c.getPortName());
				break;

			case CritterEvent.MIGRATE_REQUEST:
				//send a negative response
				respond(sending, c);
				replying = true;
				break;

			case CritterEvent.MIGRATE_DENIED:
				//Were we waiting for this?
				updateMap(c);
				if(phaseIs(WAITING)){
					//We were denied!  Wait and try again
					idle = true;
				}else if(phaseIs(WAITING_SPAWN)){
					//We were denied, so let's wait and try later
					idle = true;
				}
				break;

			case CritterEvent.MIGRATE_OK:
				if(phaseIs(WAITING_SPAWN)){
					//Time to send a new child
					sending.add(makeContent(spawnDir, spawnEvent(spawnDir)));
					sending.add(makeContent(eventsOut, spawnEvent(spawnDir)));
					spawning = true;
				}else if(phaseIs(WAITING)){
					//We were accepted - let's go!
					migrating = true;
				}else{
					//System.out.println(name + ":ERROR:Received MIGRATE_OK in phase: " + phase);
				}
				break;

				default:
				//We got a strange entity...
			}//end switch
		}//end while loop
		//Now we decide which phase we need to hold in...
		if(migrating){
			//If we are leaving, then the other stuff does not matter
			sending = new message();
			migrate(myDir);
			holdIn(MOVING,0);
		}else if(spawning){
                       // holdIn(SPAWNING,0);
                       holdIn(SPAWNING,5);
		}else if(replying){
			if(phaseIs(WAITING) || phaseIs(WAITING_SPAWN)){
				//we want to send the message now, but keep on waiting afterwards
				sigma = 0;
			}else if(idle){
				setIdle();
				//We will go to the idle state after the reply is sent
			}else holdIn(REPLYING,0);
		}else if(idle){
			setIdle();
		}
	}//end deltext()

	public message out(){
		message m = sending;
		sending = new message();
		if(phaseIs(SPAWNING)){
			spawnTimer = 0;
		}else if(phaseIs(EATING)){
			calcHunger();
			eatTimer = 0;
		}
		return m;
	}

	public Color getColor(){ return color; }

	public String getTooltipText(){
		return super.getTooltipText() + "\n" +
		"Critter's age      : "	+ age + "\n" +
		"Average hunger rate: "	+ hungerRate + "\n" +
		"Time since eating  : "	+ eatTimer + "\n" +
		"Time since spawn   : "	+ spawnTimer + "\n" +
		"Current direction  : "	+ (myDir==null ? "NULL" : myDir)		+ "\n" +
		"Spawning direction : "	+ (spawnDir==null ? "NULL" : spawnDir)	+ "\n" +
		"Eating direction   : "	+ (eatDir==null ? "NULL" : eatDir)	+ "\n"/*+mapText()*/;
	}

	/**************************
	 * HELPER FUNCTIONS BELOW *
	 **************************/

	//Chooses the next phase and delay, and sets them accordingly
	protected void setNextPhase(){
		/* Piorities:
		 * If we are starving, we should die
		 * If we are hungry, we should eat
		 * If we think spawning is a good idea, we should spawn
		 * Otherwise, we should move
		 */

		if(eatTimer > maxFeedInterval){
			//We've waited too long without eating, so we die
			die();
			return;
		}//end if die

		if(eatTimer >= minFeedInterval){
			//We should eat if we can
			String food = vegLocation();
			if(food != null){
				//Store the food location
				eatDir = food;
				sending.add(makeContent(eatDir, new CritterEvent(this, CritterEvent.EATING)));
				holdIn(EATING,0);
				return;
			}
		}//end if eat

		/**
		 * We can spawn if:
		 * + We are old enough
		 * + We are not hungry
		 * + We have not been too hungry historically
		 * + We have enough room
		 * Note: emptyNeighbors() is an expensive call, so we check it last
		 **/
		if(	(age > spawnAge) &&
			(eatTimer < minFeedInterval) &&
			(hungerRate < maxHungerRate) &&
			(fullNeighbors() <= emptyNeighbors())){
			//choose a direction
			spawnDir = getDirection();
			if(spawnDir != null){
				request(spawnDir);
				holdIn(REQUEST_SPAWN, 0);
				return;
			}
		}//end if spawn

		//check for a bad direction
		if(!canMigrate(myDir))
			myDir = getDirection();

		//if we still have a bad direction, we may be surrounded, so we just wait
		if(!canMigrate(myDir)){
			setIdle();
		}else{
			request(myDir);
			holdIn(REQUESTING, 0);
		}
	}

	//Calculate the hunger and re-compute the hungerRate
	protected void calcHunger(){
		double maxWait = maxFeedInterval - minFeedInterval;
		if(maxWait <= 0){
			//the hunger rate will always be zero
			hungerRate = 0;
			return;
		}
		//hunger is simply the amount of time we waited to eat over the max delay allowed
		//In other words, hunger is the percentage of the max time that we waited to eat
		double hunger = (eatTimer - minFeedInterval)/(maxWait);

		//To compute the rate, we scale the existing hunger rate by the historicalWeight factor
		//and add it to the scaled current hunger.  This should always yield values between 0 and 1

		double rate = (historicalWeight * hungerRate)
					+ ((1.0 - historicalWeight) * hunger);
		//Now we store the new value
		hungerRate = rate;
	}

	protected void setIdle(){
		delay = rand.nextInt(maxDelay) + 1;
		holdIn(IDLE, delay);
	}

	protected void die(){
		amDying();
		holdIn(DEAD,0);
	}

	protected void broadcastSelf(){
		CritterEvent ev = new CritterEvent(this,CritterEvent.BROADCAST);
		broadcast(sending, ev);
	}

	//Return a SPAWNING type CritterEvent with the correct coordinates in the critter
	protected CritterEvent spawnEvent(String dir){
		CritterEvent evt;
		int newx = this.xcoord;  //x is the column (left to right)
		int newy = this.ycoord;  //y is the row (top to bottom)

		if(dir.compareTo(outN) == 0){
			newy--;
		}else if(dir.compareTo(outNE) == 0){
			newy--;
			newx++;
		}else if(dir.compareTo(outE)  == 0){
			newx++;
		}else if(dir.compareTo(outSE) == 0){
			newy++;
			newx++;
		}else if(dir.compareTo(outS)  == 0){
			newy++;
		}else if(dir.compareTo(outSW) == 0){
			newy++;
			newx--;
		}else if(dir.compareTo(outW)  == 0){
			newx--;
		}else if(dir.compareTo(outNW) == 0){
			newx--;
			newy--;
		}

		PassiveCritter crit = new PassiveCritter(	name + "." + ++spawnCount,
													newx, newy,
													width, height,
													rand.nextLong());
		crit.initialize();
		evt = new CritterEvent(crit, CritterEvent.SPAWNING);
		return evt;
	}

	protected void respond(message m, content c){
		String port = c.getPortName();
		Object o = c.getValue();
		if(! (o instanceof CritterEvent))
			return;
		CritterEvent e = (CritterEvent)o;
		CritterEvent ev;
		//check for event type and generate the correct response
		switch(e.getType()){
			case CritterEvent.BROADCAST:
			ev = new CritterEvent(this,CritterEvent.REPLY);
			break;
			case CritterEvent.MIGRATE_REQUEST:
			case CritterEvent.SPAWNING:
			ev = new CritterEvent(this,CritterEvent.MIGRATE_DENIED);
			break;
			default:
			//We don't respond to these events:
			return;
		}//end switch
		if(port.compareTo(inN) == 0)		m.add(makeContent(outN, ev));
		else if(port.compareTo(inNE) == 0)	m.add(makeContent(outNE, ev));
		else if(port.compareTo(inE)  == 0)	m.add(makeContent(outE, ev));
		else if(port.compareTo(inSE) == 0)	m.add(makeContent(outSE, ev));
		else if(port.compareTo(inS)  == 0)	m.add(makeContent(outS, ev));
		else if(port.compareTo(inSW) == 0)	m.add(makeContent(outSW, ev));
		else if(port.compareTo(inW)  == 0)	m.add(makeContent(outW, ev));
		else if(port.compareTo(inNW) == 0)	m.add(makeContent(outNW, ev));
	}

	protected void updateMap(content c){
		Object o = c.getValue();
		if(! (o instanceof CritterEvent))	return;
		CritterEvent e = (CritterEvent)o;
		String port = c.getPortName();
		//examine the event type and choos appropriate action
		Critter crit = null;
		switch(e.getType()){
			//Error cases:
			case CritterEvent.MIGRATING:
			case CritterEvent.SPAWNING:
				//System.out.println(name + ":ERROR: Received migration on port: " + port);
				return;

			//Arrivals
			case CritterEvent.BROADCAST:
			case CritterEvent.REPLY:
				crit = e.getCritter();
				break;

			//Departures
			case CritterEvent.MIGRATE_DENIED:  //This is a neighbor we may not have seen
			case CritterEvent.LEAVING:
			case CritterEvent.DYING:
				crit = null;
				break;

			//Cases we do not handle here...
//			case CritterEvent.EATING:
//			case CritterEvent.ATTACKING:
//			case CritterEvent.MIGRATE_REQUEST:
//			case CritterEvent.MIGRATE_OK:
//			case CritterEvent.MIGRATE_DENIED:
			default:
			//These are not errors, but thet don't change the map
				return;
		}
		//If we got here, crit is now the new value for the map cell.  Store it
		if(port.compareTo(inN) == 0)		map[1][0] = crit;
		else if(port.compareTo(inNE) == 0)	map[2][0] = crit;
		else if(port.compareTo(inE)  == 0)	map[2][1] = crit;
		else if(port.compareTo(inSE) == 0)	map[2][2] = crit;
		else if(port.compareTo(inS)  == 0)	map[1][2] = crit;
		else if(port.compareTo(inSW) == 0)	map[0][2] = crit;
		else if(port.compareTo(inW)  == 0)	map[0][1] = crit;
		else if(port.compareTo(inNW) == 0)	map[0][0] = crit;
	}

	String vegLocation(){
		if(map[0][0] instanceof Vegetation)	return outNW;
		if(map[1][0] instanceof Vegetation)	return outN;
		if(map[2][0] instanceof Vegetation)	return outNE;
		if(map[0][1] instanceof Vegetation)	return outW;
		if(map[2][1] instanceof Vegetation)	return outE;
		if(map[0][2] instanceof Vegetation)	return outSW;
		if(map[1][2] instanceof Vegetation)	return outS;
		if(map[2][2] instanceof Vegetation)	return outSE;
		return null;
	}

	protected int emptyNeighbors(){
		int count = 0;
		if(canMigrate(outN))	count++;
		if(canMigrate(outNE))	count++;
		if(canMigrate(outE))	count++;
		if(canMigrate(outSE))	count++;
		if(canMigrate(outS))	count++;
		if(canMigrate(outSW))	count++;
		if(canMigrate(outW))	count++;
		if(canMigrate(outNW))	count++;
		return count;
	}

	protected int fullNeighbors(){
		int count = 0;
		for(int i = 0; i< 3; i++)
			for(int j = 0; j< 3; j++)
				if(map[i][j] != null)
					count++;
		return count;
	}

	protected String getDirection(){
		String dir = null;
		//We try 100 times or until we find a direction we can go.
		//We limit it just in case we are surrounded (or in 1 1-cell board)...
		for(int attempt = 0; (attempt < 100 && dir==null); attempt++){
			int choice = rand.nextInt(8);
			switch(choice){
				case 0:
					if(canMigrateN() && map[1][0]==null)	dir = outN;
					break;
				case 1:
					if(canMigrateNE() && map[2][0]==null)	dir = outNE;
					break;
				case 2:
					if(canMigrateE() && map[2][1]==null)	dir = outE;
					break;
				case 3:
					if(canMigrateSE() && map[2][2]==null)	dir = outSE;
					break;
				case 4:
					if(canMigrateS() && map[1][2]==null)	dir = outS;
					break;
				case 5:
					if(canMigrateSW() && map[0][2]==null)	dir = outSW;
					break;
				case 6:
					if(canMigrateW() && map[0][1]==null)	dir = outW;
					break;
				case 7:
					if(canMigrateNW() && map[0][0]==null)	dir = outNW;
					break;
			}//end switch
		}//end for loop
		return dir;
	}

	protected boolean canMigrate(String dir){
		if(dir == null) return false;
		if(dir.compareTo(outN) == 0)	return (canMigrateN()  && (map[1][0]==null));
		if(dir.compareTo(outNE) == 0)	return (canMigrateNE() && (map[2][0]==null));
		if(dir.compareTo(outE)  == 0)	return (canMigrateE()  && (map[2][1]==null));
		if(dir.compareTo(outSE) == 0)	return (canMigrateSE() && (map[2][2]==null));
		if(dir.compareTo(outS)  == 0)	return (canMigrateS()  && (map[1][2]==null));
		if(dir.compareTo(outSW) == 0)	return (canMigrateSW() && (map[0][2]==null));
		if(dir.compareTo(outW)  == 0)	return (canMigrateW()  && (map[0][1]==null));
		if(dir.compareTo(outNW) == 0)	return (canMigrateNW() && (map[0][0]==null));
		return false;
	}

	protected void clearMap(){
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				map[i][j] = null;
	}

	protected String mapText(){
		return "[" +
		(map[0][0]==null ? "None" : map[0][0].getName()) + "] [" +
		(map[1][0]==null ? "None" : map[1][0].getName()) + "] [" +
		(map[2][0]==null ? "None" : map[2][0].getName()) + "]\n[" +
		(map[0][1]==null ? "None" : map[0][1].getName()) + "] [SELF] [" +
		(map[2][1]==null ? "None" : map[2][1].getName()) + "]\n[" +
		(map[0][2]==null ? "None" : map[0][2].getName()) + "] [" +
		(map[1][2]==null ? "None" : map[1][2].getName()) + "] [" +
		(map[2][2]==null ? "None" : map[2][2].getName()) + "]";
	}

}
