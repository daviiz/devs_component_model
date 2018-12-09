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

import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;




public class TQOffsetControl extends ViewableAtomic {

  protected radarentity m_radarentity;
  protected double m_offset;

  public TQOffsetControl() {
    this("TQOffSetControl",2);
  }

  public TQOffsetControl(String name, double offset) {
    super(name);
    m_offset = offset;

    addInport("RadarIn");
    addTestInput("RadarIn",new trackentity("T1",69,2,3,4,10,new Color(0, 0, 0)));
   addTestInput("RadarIn",new trackentity("T2",5,6,3,4,10,new Color(0, 0, 0)));

    addOutport("RadarOut");

  }



public void SetOffset(double offset)
  {
    m_offset = offset;

  }


  public void  deltext(double e,message x)
{
   passivateIn("active");

radarentity mytrackent;
entity jason;
double Xpos;
double Ypos;
Continue(e);

int lenth = x.getLength();
for (int i=0; i< x.getLength();i++)
   if (messageOnPort(x,"RadarIn",i))
   {

     holdIn("active",0);
      mytrackent =  (radarentity)x.getValOnPort("RadarIn",i);
      System.out.println(" out: " + mytrackent.getTQ()+" "+ mytrackent.getX()+" "+ mytrackent.getY()+" " + mytrackent.getColor());

      Xpos = mytrackent.getX();
      Ypos = mytrackent.getY();
      int TQ = mytrackent.getTQ();

      TQ = 15 - TQ ;
  //    System.out.println("TQ " + mytrackent.getTQ());
   //   System.out.println("new TQ " + TQ);


   if(m_offset == 0)
   {
     m_offset = 1;
   }
      double offset = (TQ / m_offset);

    //  System.out.println("X in out " + Xpos);

    //System.out.println("X offset " + (Xpos+offset));

      m_radarentity = new radarentity(mytrackent.getName(), mytrackent.getTQ(), (Xpos+offset), Ypos, mytrackent.getColor());



   }


}


  public void initialize(){
   holdIn("passive", INFINITY);

    //  phase = "passive";
   //  sigma = INFINITY;
     super.initialize();
 }

 public void  deltint( )
{


    passivate();



}

  public message  out( )
  {
     message m = new message();
     m.add(makeContent("RadarOut",m_radarentity));

    // System.out.println("Tqr radarout");

     return m;


  }



}