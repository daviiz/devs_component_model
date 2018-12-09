package refDemo.RadarProject;

import java.awt.Color;

import GenCol.doubleEnt;
import model.modeling.message;
import view.modeling.ViewableAtomic;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author J Phelps 12/2003
 * @version 1.0
 */

public class TqDetector extends ViewableAtomic{

  protected double m_X;
  protected double m_Y;
  protected String m_name;
  protected Color m_Color;
  protected double m_range;
  protected radarentity m_radarentity;
  protected boolean m_bSendRadar;

  public TqDetector()
  {

    this("TqDetector", 0, 0,25, new Color(0,255,0));

  }



  public TqDetector(String name,double x, double y, double range,Color color)
    {
   super(name);

  m_name = name;
     m_Color = color;

     m_X = x;
     m_Y = y;
     m_range = range;

     addInport("TrackEntity");
     addOutport("RadarOut");
     addTestInput("TrackEntity",new trackentity("T1",2,3,3,4,10,new Color(255, 0, 0)));
     addTestInput("TrackEntity",new trackentity("T2",5,6,3,4,10,new Color(0, 255, 255)));
  }
  public void initialize()
  {
    m_bSendRadar = false;
    passivate();
    super.initialize();

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

         int TQ = CalculateTQbyDistance( mytrackent.getX(), mytrackent.getY());
        // System.out.println("The Distance TQ " + TQ);

         m_radarentity = new radarentity(mytrackent.getName(),TQ, mytrackent.getX(), mytrackent.getY(), m_Color);




      }

}
  public int  CalculateTQbyDistance(double x, double y )
  {
    m_bSendRadar = true;

    double distance = CalcDistance(x,y);

 //   System.out.println("distance " + distance + " range " + m_range);
    if(distance > m_range)
    {
      m_bSendRadar = false;
      return 0;
    }
    else
    {

      double factor = m_range/15.0;
      double TQ = distance / factor;
      TQ -= 15;

      return (int) Math.abs(TQ);
    }
  }

public double CalcDistance(double x, double y)
  {
    double distance = 0;
     distance =  (x-m_X) * (x-m_X) +  (y-m_Y) * (y-m_Y);
     distance = Math.sqrt(distance);

    return distance;
  }

public void SetXYColorRange(double x, double y, Color color, double range)
  {

    m_X = x;
    m_Y = y;
    m_Color = color;
    m_range = range;



  }

  public double GetX()
  {


    return m_X;
  }
  public double GetY()
{


  return m_Y;
}

public void  deltint( )
{
    passivate();

}

public message  out( )
{
    message m = new message();

//System.out.println("send " + m_bSendRadar);
    if(m_bSendRadar)
    {
      m.add(makeContent("RadarOut", new radarentity(m_radarentity)));
  //    System.out.println("radar out");
    }
    return m;

}
 public message outputRealOnPort(double r,String port){
 message m = new message();
 m.add(makeContent(port,new doubleEnt(r)));
 return m;
 }

public void showState(){
    super.showState();
}

}