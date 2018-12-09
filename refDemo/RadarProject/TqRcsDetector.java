package refDemo.RadarProject;

import java.awt.Color;

import model.modeling.message;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author J Phelps 12/2003
 * @version 1.0
 */

public class TqRcsDetector extends TqDetector {
  public TqRcsDetector()
  {
    super("TqRcsDetector", 3, 2,15, new Color(0,255,0));
  }

  public TqRcsDetector(String name,double x, double y, double range,Color color)
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



        int TQ = CalculateTQbyRCS(mytrackent.getX() , mytrackent.getY(),mytrackent.getRCS());

        System.out.println("RCS Detecot " + TQ);



         m_radarentity = new radarentity(mytrackent.getName(),TQ, mytrackent.getX(), mytrackent.getY(), m_Color);




      }

}

  public int CalculateTQbyRCS(double x, double y, double RCS)
  {

    m_bSendRadar = true;
    double TQ = 0.0;
    double distance = this.CalcDistance(x,y);
    if(distance > m_range)
    {
      m_bSendRadar = false;
      return 0;
    }
    else {

      double factor = m_range / 15.0;
      TQ = distance / factor;
      int myfactor = (int)RCS;
      TQ = 15 - TQ;
      TQ = TQ + myfactor;
      if(TQ > 15 )
      {
        TQ = 15;
      }
      else if (TQ < 0)
      {
        TQ = 0;
      }


    }

  return (int) Math.abs(TQ);
}




}