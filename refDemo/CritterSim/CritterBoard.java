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

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import view.modeling.ViewableDigraph;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575
 * Home Page >/A>
 *
 * CritterBoard creates an array of Critter cells and couples neighboring cells to each other.
 * It is the main class that should be choose in SimView when running the CritterSim project.
 */
public class CritterBoard extends ViewableDigraph{
	//Initial population desities...  should not sum beyond 1!
	protected static final double passiveDensity = .05;
	protected static final double aggressiveDensity = .05;

	protected CritterCell[][] cells;
	protected int width;
	protected int height;
	protected Random rand;

	protected CritterPlot plot;
	protected CritterStats stats;

	public CritterBoard() {
		this(40,25);
	}

	public CritterBoard(int width, int height) {
		super("Critter Board");
		this.width = width;
		this.height = height;
		rand = new Random();

		setBlackBox(true);
		//initialize the cell array
		cells = new CritterCell[width][height];

		//create the cells and add them to the model
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				this.add(cells[i][j] = new CritterCell("("+i+","+j+")", i, j, width, height));
			}
		}


		//Add some critters...
		int pasCount, agCount;
		for(pasCount = 0; pasCount < (int)(passiveDensity*width*height) ; pasCount++){
			int x,y;
			do{
				x = rand.nextInt(width);
				y = rand.nextInt(height);
			}while( cells[x][y].getInitCritter() != null);
			Critter c = new PassiveCritter("P"+pasCount, x, y, width, height, rand.nextLong());
			cells[x][y].setInitCritter(c);
		}
		for(agCount = 0; agCount < (int)(aggressiveDensity*width*height) ; agCount++){
			int x,y;
			do{
				x = rand.nextInt(width);
				y = rand.nextInt(height);
			}while( cells[x][y].getInitCritter() != null);
			Critter c = new AggressiveCritter("A"+agCount, x, y, width, height, rand.nextLong());
			cells[x][y].setInitCritter(c);
		}

		//initialize the plot
		plot = new CritterPlot(width ,height);
		add(plot);

		//initialize the Critter Stats
		stats = new CritterStats(0, pasCount, agCount);
		add(stats);

		//add couplings
		coupleCells();
	}

	private void coupleCells(){
		/* For reference, the cells around cell[i][j] will have the following coordinates
		 * [i-1,j-1] [ i ,j-1] [i+1,j-1]
		 * [i-1, j ] [ i , j ] [i+1, j ]
		 * [i-1,j+1] [ i ,j+1] [i+1,j+1]
		 **/
		boolean north, south, east, west;
		north = south = east = west = false;
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//Add couplings from this cell's out ports to all it's in ports
				if(j > 0){
					//North is OK
					addCoupling(cells[i][j],CritterCell.outN,cells[i][j-1],CritterCell.inS);
					if(i > 0){
						//NorthWest is OK
						addCoupling(cells[i][j],CritterCell.outNW,cells[i-1][j-1],CritterCell.inSE);
					}
					if(i < width-1){
						//NorthEast is OK
						addCoupling(cells[i][j],CritterCell.outNE,cells[i+1][j-1],CritterCell.inSW);
					}
				}//end if (North OK)
				if(j < height-1){
					//South is OK
					addCoupling(cells[i][j],CritterCell.outS,cells[i][j+1],CritterCell.inN);
					if(i > 0){
						//SouthWest is OK
						addCoupling(cells[i][j],CritterCell.outSW,cells[i-1][j+1],CritterCell.inNE);
					}
					if(i < width-1){
						//SouthEast is OK
						addCoupling(cells[i][j],CritterCell.outSE,cells[i+1][j+1],CritterCell.inNW);
					}
				}//end if (south OK)
				if(i < width-1){
					//East is OK
					addCoupling(cells[i][j],CritterCell.outE,cells[i+1][j],CritterCell.inW);
				}
				if((j < height )  && (i > 0)){
					addCoupling(cells[i][j],CritterCell.outW,cells[i-1][j],CritterCell.inE);
				}
				//We always couple the cell to our event listeners
				addCoupling(cells[i][j],Critter.eventsOut, plot, CritterPlot.IN);
				addCoupling(cells[i][j],Critter.eventsOut, stats, CritterStats.IN);
			}//end inner for loop
		}//end outher for loop
	}//end coupleCells

	public boolean layoutForSimViewOverride(){
		int cellWidth = 210;
		int cellHeight = 150;
		preferredSize = new Dimension(width*cellWidth, height*cellHeight + 20);

		layoutCells(cellWidth, cellHeight);
		return true;
	}

	private void layoutCells(int columnWidth, int rowHeight){
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				// detm the cell's grid location (in pixels)
				Point location = new Point( i * columnWidth, j * rowHeight + 20);
				cells[i][j].setPreferredLocation(location);
			}
		}
	}
}
