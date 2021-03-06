package refDemo.RadarProject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableComponent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author J Phelps 12/2003
 * @version 1.0
 */

public class TqVelocityModel extends RadarModel {
  public TqVelocityModel() {

    this("TqVelocityModel",0, 0, Color.gray, 15);

  }

  public TqVelocityModel(String name, double x, double y, Color color, double range) {


   super(name, new TqVelocityDetector(), new R2Analyzer(), x, y, color, range);
  // System.out.println("The TQ velociyt post " + x + " " + y );
 }
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(491, 217);
        ((ViewableComponent)withName("TqVeloictyDetector")).setPreferredLocation(new Point(1, 127));
        ((ViewableComponent)withName("R2Analyzer")).setPreferredLocation(new Point(67, 40));
    }
}
