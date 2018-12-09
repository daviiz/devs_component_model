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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * CritterStatsBox extends JFrame.  It displays the current and maximum critter populations by
 * critter type.  It is fed by CritterStats.
 */
public class CritterStatBox extends JFrame {

	protected JLabel vegCount;
	protected JLabel vegMax;
	protected JLabel pasCount;
	protected JLabel pasMax;
	protected JLabel aggCount;
	protected JLabel aggMax;
	public CritterStatBox() throws HeadlessException {
		super("Critter Stats");
		vegCount = new JLabel("0", SwingConstants.CENTER);
		vegMax = new JLabel("0", SwingConstants.CENTER);
		pasCount = new JLabel("0", SwingConstants.CENTER);
		pasMax = new JLabel("0", SwingConstants.CENTER);
		aggCount = new JLabel("0", SwingConstants.CENTER);
		aggMax = new JLabel("0", SwingConstants.CENTER);

		Container c = getContentPane();
		c.setLayout(new GridLayout(4,3));
		c.add(new JLabel("Critter Type", SwingConstants.RIGHT));
		c.add(new JLabel("Current", SwingConstants.CENTER));
		c.add(new JLabel("Maximum", SwingConstants.CENTER));

		c.add(new JLabel(" Vegetation:", SwingConstants.RIGHT));
		c.add(vegCount);
		c.add(vegMax);

		c.add(new JLabel(" PassiveCritter:", SwingConstants.RIGHT));
		c.add(pasCount);
		c.add(pasMax);

		c.add(new JLabel(" AggressiveCritter:", SwingConstants.RIGHT));
		c.add(aggCount);
		c.add(aggMax);

		Dimension d = c.getPreferredSize();
		this.setSize(d.width, d.height * 2);
		this.setVisible(true);
	}

	public void setVegCount(int c){
		vegCount.setText(""+c);
	}

	public void setVegMax(int c){
		vegMax.setText(""+c);
	}

	public void setPasCount(int c){
		pasCount.setText(""+c);
	}

	public void setPasMax(int c){
		pasMax.setText(""+c);
	}

	public void setAggCount(int c){
		aggCount.setText(""+c);
	}

	public void setAggMax(int c){
		aggMax.setText(""+c);
	}
}
