/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package refDemo.RoadTraffic;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class turnOnlight extends ViewableAtomic{


  protected double int_arr_time,ran;

 public turnOnlight() {this("turnOnlight", 20);}

public turnOnlight(String name,double Int_arr_time){
   super(name);

   addOutport("turn on light");
   addInport("start");
   addTestInput("start",new entity(""));
   int_arr_time = Int_arr_time ;

  }

public void initialize(){
   holdIn("passive", int_arr_time);
   super.initialize();
 }

public void  deltext(double e,message x)
{
   Continue(e);
   for (int i = 0; i < x.getLength(); i++) {
    if (messageOnPort(x, "start", i))
      holdIn("passive", 0);
   }

}

public void  deltint( )
{
 if(phaseIs("passive"))
holdIn("turning",0);
 else if (phaseIs("turning"))
holdIn("light on", INFINITY);
}

public message  out( )
{
   message  m = new message();
    if(phaseIs("passive")){
      content con1 = makeContent("turn on light", new doubleEnt(0));
      m.add(con1);
    }
  return m;
}
}

