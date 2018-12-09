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

import GenCol.entity;
import model.modeling.message;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * Critter is the super class for critters that will interact on the CritterBoard.  The Critter
 * class extends realDevs.  The Critter class never gets coupled to the CritetrBoard or to
 * other Critters.  Instead, when a CritterCell is occupied by a Critter, the CritterCell passes
 * all DEVS calls (delta methods, initialize method, and out method) to the Critter.  This
 * allows the Critter to control the behavior of the cell it is currently located in, yet easily
 * move between cells.
 */
public class Critter extends realDevs{
	//in ports
	public static final String inN = "in N";
	public static final String inNE = "in NE";
	public static final String inE = "in E";
	public static final String inSE = "in SE";
	public static final String inS = "in S";
	public static final String inSW = "in SW";
	public static final String inW = "in W";
	public static final String inNW = "in NW";
	//out ports	
	public static final String outN = "out N";
	public static final String outNE = "out NE";
	public static final String outE = "out E";
	public static final String outSE = "out SE";
	public static final String outS = "out S";
	public static final String outSW = "out SW";
	public static final String outW = "out W";
	public static final String outNW = "out NW";
	public static final String eventsOut = "events";
	
	//phases
	public static final String MIGRATING = "Migrating...";
	//Background color
	public static final Color color= Color.lightGray;
	
	private String migrationPort = null;
	private String requestPort = null;
	private boolean migrating = false;
	private boolean requesting = false;
	private boolean dying = false;
	private long seed;
	 
	protected int xcoord, oldxcoord;
	protected int ycoord, oldycoord;
	protected final int initxcoord;
	protected final int initycoord;
	protected final int width;
	protected final int height;
	protected Random rand;
	
	public Critter() {
		this("Critter");
	}

	public Critter(String name) {
		this(name, 0,0,1,1);
	}

	public Critter(String name, int x, int y, int width, int height) {
		this(name, x, y, width, height, 0);
	}
	
	public Critter(String name, int x, int y, int width, int height, long randomSeed) {
		super(name);
		initxcoord = x;
		initycoord = y;
		this.width = width;
		this.height = height;
		seed = randomSeed;
		/**
		 * This is kind of strange.  A Critter does not have any in or out ports, just names
		 * of ports.  When a critter is in a CritterCell, the Critter cell relays all calls
		 * to devs methods (deltint(), deltext(), deltcon(), initialize(), and out() to the
		 * Critter.  So when the Critter's out method sends a message, it is actually being
		 * sent on the CritterCell's port.  Odd but effective!  :-)
		 */
	}
	
	/**
	 * The initialize() method is called the simulation is restarted.  It is REQUIRED that
	 * subclasses of Critter that override initialize call super.initialize() in their
	 * initialize() body.
	 */
	public void initialize(){
		xcoord = oldxcoord = initxcoord;
		ycoord = oldycoord = initycoord;
		dying = migrating = requesting = false;
		migrationPort = requestPort = null;
		if(rand == null){
			//create a new Random with the specified seed
			rand = new Random(seed);
		}else{
			//Set the seed to the initial value.
			//This is just like creating an new Random with the same seed.
			rand.setSeed(seed);
		}
		passivate();
	}
	
	//used by CritterCell to determine current phase
	public String getPhase(){ return phase; }
	
	//used by CritterCell to determine the time advance
	public double getSigma(){ return sigma; }
	
	//Used by CritterCell to determine if we are requesting a migration
	public boolean requesting(){ return requesting; }
	
	//Used by CritterCell to determine if we are migrating
	public boolean migrating(){ return migrating; }
	
	public boolean dying(){ return dying; }

	//Used by CritterCell to acknowledge our migration request
	public void requestSent(){
		requestPort = null;
		requesting = false;
	}

	//Used by CritterCell to acknowledge our migration
	public void migrationOK(){
		//We are moving now, so we can clear our migration destination
		migrationPort = null;
		migrating = false;
	}

	//Used by CritterCell to acknowledge our death
	public void deathOK(){
		//we are no longer dying
		dying = false;
	}

	public message requestMessage(){
		message m = new message();
		CritterEvent e = new CritterEvent(this, CritterEvent.MIGRATE_REQUEST);
		m.add(makeContent(requestPort,e));
		return m;	
	}

	public message migrationMessage(){
		message m = new message();
		CritterEvent e = new CritterEvent(this, CritterEvent.LEAVING);
		if(migrating){
			broadcast(m,e);
			e = new CritterEvent(this, CritterEvent.MIGRATING);
			m.add(makeContent(migrationPort,e));
		}
		return m;	
	}
	
	public message dyingMessage(){
		message m = new message();
		if(dying){
			CritterEvent ev = new CritterEvent(this, CritterEvent.DYING);
			broadcast(m, ev);
		}
		return m;
	}

	public int getCol(){ return getX(); }
	public int getRow(){ return getY(); }
	public int getX(){ return xcoord; }
	public int getY(){ return ycoord; }
	public int getOldX(){ return oldxcoord; }
	public int getOldY(){ return oldycoord; }
	
	public Color getColor(){
		return color;
	}
	
	/**
	 * A Critter subclass should call this method when it would like to migrate to a new cell.
	 * The method will send a migration request.  Once a MIGRATION_OK response has been received
	 * the Critter subclass should call migrate() to move to the new cell.
	 * @param onPort
	 */
	protected void request(String onPort){
		//We want to migrate to the port onPort
		//make sure we can migrate.  If we can, send the message
		if(onPort == null){
			return;
		}
		if(onPort.compareTo(outN) == 0){
			if(! canMigrateN()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outNE) == 0){
			if(! canMigrateNE()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outE) == 0){
			if(! canMigrateE()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outSE) == 0){
			if(! canMigrateSE()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outS) == 0){
			if(! canMigrateS()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outSW) == 0){
			if(! canMigrateSW()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outW) == 0){
			if(! canMigrateW()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outNW) == 0){
			if(! canMigrateNW()){
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else{
			//System.out.println(name + ":ERROR: Tried to migrate on UNKNOWN port: " + onPort);
		}
		//If we made it here, we are requesting
		requesting = true;
		requestPort = onPort;
		sigma = 0;
	}

	/**
	 * A Critter subclass should call this method when it would like to migrate to a new cell.
	 * The method will cause the neccessary DEVS events to move the critter on the specified
	 * port and notify other cells that the cell is becoming empty.
	 * @param onPort
	 */
	protected void migrate(String onPort){
		//We want to migrate to the port onPort
		//make sure we can migrate.  If we can, change coordinates now.
		int x = xcoord;
		int y = ycoord;
		if(onPort == null){
			return;
		}
		if(onPort.compareTo(outN) == 0){
			if(canMigrateN()){
				ycoord--;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outNE) == 0){
			if(canMigrateNE()){
				xcoord++;
				ycoord--;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outE) == 0){
			if(canMigrateE()){
				xcoord++;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outSE) == 0){
			if(canMigrateSE()){
				xcoord++;
				ycoord++;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outS) == 0){
			if(canMigrateS()){
				ycoord++;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outSW) == 0){
			if(canMigrateSW()){
				xcoord--;
				ycoord++;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outW) == 0){
			if(canMigrateW()){
				xcoord--;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}else if(onPort.compareTo(outNW) == 0){
			if(canMigrateNW()){
				xcoord--;
				ycoord--;
			}else{
				//System.out.println(name + ":ERROR: Tried to migrate on port " + onPort);
				return;
			}
		}
		//If we made it here, we are migrating
		oldxcoord = x;
		oldycoord = y;
		migrating = true;
		migrationPort = onPort;
		holdIn(MIGRATING, 0);
	}
	
	/**
	 * A Critter subclass should call this method when it is dying.  The method will cause the
	 * neccessary DEVS events to announce the death and tell CritterCell to remove the critter.
	 */
	protected void amDying(){
		dying = true;
	}
	
	public String getTooltipText(){
		String result = name + "\n";
		result += "Location: (" + xcoord + "," + ycoord + ")";
		return result;
	}

	
	protected boolean canMigrateN(){  return (ycoord > 0); }
	protected boolean canMigrateNE(){ return ((ycoord > 0) && (xcoord < width-1)); }
	protected boolean canMigrateE(){  return (xcoord < width-1); }
	protected boolean canMigrateSE(){ return (ycoord < height-1) && (xcoord < width-1); }
	protected boolean canMigrateS(){  return (ycoord < height-1); }
	protected boolean canMigrateSW(){ return (ycoord < height-1) && (xcoord > 0); }
	protected boolean canMigrateW(){  return (xcoord > 0); }
	protected boolean canMigrateNW(){ return ((ycoord > 0) && (xcoord > 0)); }

	/**
	 * broadcast sends the specified entity out on all 8 directional ports.
	 * @param m The message we will be sending out.
	 * @param e The entity to send out.
	 */
	protected void broadcast(message m, entity e){
		if(m == null || e == null)
			return;
		m.add(makeContent(outN, e));
		m.add(makeContent(outNE, e));
		m.add(makeContent(outE, e));
		m.add(makeContent(outSE, e));
		m.add(makeContent(outS, e));
		m.add(makeContent(outSW, e));
		m.add(makeContent(outW, e));
		m.add(makeContent(outNW, e));
		m.add(makeContent(eventsOut, e));
	}
}
