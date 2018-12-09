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

public class TqTestEF extends RadarEF {
  public TqTestEF() {
  super("TqTestEF",new TqRcsSensorModel("Tq RCsSensor Model Orange", 0.0, 0.0, Color.orange, 25),
        new TqVelocityModel("TqVelocity Model Cyan", 0.0, 0.0, Color.cyan, 25));
 //   super("TqTestEF",  new RadarModel("Radar 1", -10.0, 0.0, Color.orange, 25),
  //        new RadarModel("Radar 2", 10.0, 0.0, Color.cyan, 25));
   //                        name, dellay x y
    TqPlot tqP  = new TqPlot();
    add(tqP);
//String nm,double x, double y, double course,double speed, double RCS, Color color){

//this.tGen.SetTrack(new trackentity("Teste",1, 1, 45,5,10, new Color(69,69,69)));
//this.tGen.SetTrack(new trackentity("Teste",0, 15, 190,5,10, new Color(69,69,69)));
//this.tGen.SetTrack(new trackentity("Teste",15, 15, 235,5,10, new Color(69,69,69)));
    double x, y, course, speed, RCS;
    x = 1;
  y = 1;
  course = 45;
  speed = 5;
  RCS = 10;
  int test = 13;

    switch(test)
    {
        case 0:
        {
          x = 1;
        y = 1;
        course = 45;
        speed = 5;
        RCS = 10;
        break;
      }
      case 1:
      {
        x = 0;
         y = -15;
         course = 90;
         speed = 5;
         RCS = 10;
         break;
      }
      case 2:
      {
        x = 0;
            y = 15;
            course = 190;
            speed = 5;
            RCS = 10;
            break;
      }
      case 3:
      {
        x = 15;
            y = 15;
            course = 230;
            speed = 5;
            RCS = 10;
            break;
      }
      case 4:
      {
        x = 15;
           y = -15;
           course = 275;
           speed = 5;
           RCS = 10;
           break;
      }
      case 5:
      {
        x = 1;
        y = 1;
        course = 45;
        speed = 10;
        RCS = 5;
        break;
      }
      case 6:
      {
        x = 0;
        y = -15;
        course = 90;
        speed = 10;
        RCS = 5;
        break;
      }
      case 7:
      {
        x = 0;
        y = 15;
        course = 190;
        speed = 10;
        RCS = 5;
        break;
      }
      case 8:
      {
        x = 15;
y = 15;
course = 235;
speed = 10;
RCS = 5;
        break;

      }
      case 9:
     {
       x = 15;
       y = -15;
       course = 280;
       speed = 10;
       RCS = 5;

       break;
     }


     case 10:
     {
       x = 1;
     y = 1;
     course = 45;
     speed = 10;
     RCS = 10;
     break;
   }
   case 11:
   {
     x = 0;
      y = -15;
      course = 90;
      speed = 10;
      RCS = 10;
      break;
   }
   case 12:
   {
     x = 0;
         y = 15;
         course = 190;
         speed = 10;
         RCS = 10;
         break;
   }
   case 13:
   {
     x = 15;
         y = 15;
         course = 230;
         speed = 10;
         RCS = 10;
         break;
   }
   case 14:
   {
     x = 15;
        y = -15;
        course = 275;
        speed = 10;
        RCS = 10;
        break;
   }



   }
    this.tGen.SetTrack(new trackentity("Teste",x, y, course,speed,RCS, new Color(69,69,69)));

    this.TqOff1.SetOffset(3);
    this.TqOff2.SetOffset(4);
   addCoupling(this.TqOff1, "RadarOut", tqP,"RadarIn");
    addCoupling(this.TqOff2, "RadarOut", tqP,"RadarIn");




  }

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(938, 581);
        ((ViewableComponent)withName("TrackGenerator")).setPreferredLocation(new Point(18, 242));
        ((ViewableComponent)withName("Tq RCsSensor Model Orange")).setPreferredLocation(new Point(135, 314));
        ((ViewableComponent)withName("TQ Offset Control 2")).setPreferredLocation(new Point(611, 93));
        ((ViewableComponent)withName("TQ Offset Control 1")).setPreferredLocation(new Point(694, 352));
        ((ViewableComponent)withName("TqVelocity Model Cyan")).setPreferredLocation(new Point(124, 22));
    }
}