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

import GenCol.entity;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * CritterEvent extends entity.  It is the class that Critters use to exchange messages.
 */
public class CritterEvent extends entity{
	public static final int NO_TYPE			= 0;
	public static final int BROADCAST		= 1;
	public static final int REPLY			= 2;
	public static final int EATING			= 3;
	public static final int ATTACKING		= 4;
	public static final int LEAVING			= 5;
	public static final int DYING			= 6;
	public static final int MIGRATING		= 7;
	public static final int SPAWNING		= 8;
	public static final int MIGRATE_REQUEST	= 9;
	public static final int MIGRATE_OK		= 10;
	public static final int MIGRATE_DENIED	= 11;

	public static final int MAX_TYPE		= 11;
	
	
	public static final String NO_TYPE_STR			= "NoType!";
	public static final String BROADCAST_STR		= "Hello!?";
	public static final String REPLY_STR			= "Reply";
	public static final String EATING_STR			= "Eating";
	public static final String ATTACKING_STR		= "Attacking";
	public static final String LEAVING_STR			= "Goodbye";
	public static final String DYING_STR			= "Gasp!";
	public static final String MIGRATING_STR		= "Migrating";
	public static final String SPAWNING_STR			= "Spawning";
	public static final String MIGRATE_REQUEST_STR	= "Migrate Request";
	public static final String MIGRATE_OK_STR		= "Migrate OK";
	public static final String MIGRATE_DENIED_STR	= "Migrate Denied";

	protected Critter c = null;
	protected int type = 0;

	public CritterEvent() {
		this(null, NO_TYPE);
	}

	public CritterEvent(Critter critter, int eventType) {
		super("CritterEvent");
		setCritter(critter);
		setType(eventType);
	}

	public int setType(int t){
		return type = ((t >= 0) && (t <= MAX_TYPE)) ? t : NO_TYPE;
	}
	
	public int getType(){ return type; }
	
	public Critter setCritter(Critter critter){
		return c = critter;
	}
	
	public Critter getCritter(){ return c; }
	
	public String getName(){
		switch(type){
			case BROADCAST:			return BROADCAST_STR;
			case REPLY:				return REPLY_STR;
			case EATING:			return EATING_STR;
			case ATTACKING:			return ATTACKING_STR;
			case LEAVING:			return LEAVING_STR;
			case DYING:				return DYING_STR;
			case MIGRATING:			return MIGRATING_STR;
			case SPAWNING:			return SPAWNING_STR;
			case MIGRATE_REQUEST:	return MIGRATE_REQUEST_STR;
			case MIGRATE_OK:		return MIGRATE_OK_STR;
			case MIGRATE_DENIED:	return MIGRATE_DENIED_STR;
			default:				return NO_TYPE_STR;
		}
	}
}
