
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author J Phelps 12/2003
 * @version 1.0
 */

package refDemo.RadarProject;

import java.awt.Color;

import model.modeling.message;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TqVelocityDetector extends TqDetector {
  public TqVelocityDetector()
  {
    super("TqVeloictyDetector", 3, 2,15, new Color(0,255,0));
  }

  public TqVelocityDetector(String name,double x, double y, double range,Color color)
  {
    super(name, x, y,range, color);

  }

  public void  deltext(double e,message x)
{
   passivateIn("active");

   trackentity mytrackent;


   Continue(e);

   int lenth = x.getLength();
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"TrackEntity",i))
      {
        holdIn("active",0);
        mytrackent =  (trackentity)x.getValOnPort("TrackEntity",i);

        // Xpos =



        int TQ = CalculateTQbyAngularVelocity(mytrackent.getX() , mytrackent.getY(),mytrackent.getSpeed(),mytrackent.getCourse());

         System.out.println("TQ Velocity " + TQ);
         m_radarentity = new radarentity(mytrackent.getName(),TQ, mytrackent.getX(), mytrackent.getY(), m_Color);




      }

}

  public int CalculateTQbyAngularVelocity(double x, double y, double speed,double course )
  {

    double TQ = 0.0;
    m_bSendRadar = true;
    double distance = this.CalcDistance(x,y);
    if(distance > m_range)
    {
      m_bSendRadar = false;
     // System.out.println("false");
      return 0;
    }
    else {


     double factor = m_range / 15.0;
      TQ = (distance / factor);
     // System.out.println("Velocity TQ " + TQ);
      speed = speed;
      /* int myfactor = (int)speed;
     System.out.println("my factor " + myfactor + " T q " + TQ);
     TQ = 15 - TQ;
     TQ = TQ + myfactor;*/
    double angularvelociyt = (speed/distance);
//    System.out.println("angularvelociyt " + angularvelociyt);
    TQ = (distance / factor) + angularvelociyt;
      if(TQ > 15 )
      {
        TQ = 15;
      }
      else if (TQ < 0)
      {
        TQ = 0;
      }
      System.out.println(" T q " + TQ);

    }

  return (int) Math.abs(TQ);
}




}
