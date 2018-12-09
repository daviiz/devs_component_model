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
import java.awt.Dimension;

import model.modeling.ContentIteratorInterface;
import model.modeling.content;
import model.modeling.message;
import model.plots.CellGridView;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * CritterPlot extends realDevs.  It uses CellGridView to plot Critter movement.
 */
public class CritterPlot extends realDevs {
	public static final String IN = "in";

	private CellGridView view;
	private int w, h;
	
	public CritterPlot(int width, int height) {
		super("CritterPlot");
		
		setHidden(true);

		w = width;
		h = height;

		int maxDim = 800;
		int cellSize = 25;
		//pick a good cell size
		if(Math.max(cellSize*width, cellSize*height) > maxDim){
			cellSize = Math.min((maxDim/width),(maxDim/h));
		}
		view = new CellGridView("Critter Plot", "", "");
		view.setCellSize(cellSize);
		view.setSpaceSize(new Dimension(w, h));
		if(! view.isVisible())
			view.setVisible(true);
	}

	public CritterPlot() {
		super("CritterPlot");
	}
	
	public void initialize(){
		passivate();
		for(int i = 0; i < w; i++)
			for(int j = 0; j< h; j++)
				view.drawCell(i,j,Color.lightGray);
		if(! view.isVisible())
			view.setVisible(true);
	}
	
	public void deltext(double e, message x){
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
			//Now we examine the event type to make a plot decision
			switch(event.getType()){
				case CritterEvent.BROADCAST:
				view.drawCell(crit.getX(), crit.getY(), crit.getColor());
				break;
				//Clear the cell
				case CritterEvent.DYING:
				//Clear the cell
				view.drawCell(crit.getX(), crit.getY(), Color.lightGray);
				break;
				case CritterEvent.LEAVING:
				//Clear the cell
				view.drawCell(crit.getOldX(), crit.getOldY(), Color.lightGray);
				break;
				default:
			}//end switch
		}//end while
	}
}
