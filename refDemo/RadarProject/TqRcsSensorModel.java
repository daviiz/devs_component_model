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

public class TqRcsSensorModel extends RadarModel {
  public TqRcsSensorModel() {

    this("TqRcsSensorModel",0, 0, Color.gray, 15);

  }

  public TqRcsSensorModel(String name, double x, double y, Color color, double range) {

    super(name, new TqRcsDetector(), new R2Analyzer(), x, y, color, range);

  }

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(541, 225);
        ((ViewableComponent)withName("R2Analyzer")).setPreferredLocation(new Point(82, 41));
        ((ViewableComponent)withName("TqRcsDetector")).setPreferredLocation(new Point(45, 133));
    }
}
