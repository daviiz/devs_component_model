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
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class lightControler extends ViewableAtomic{


 public double int_arr_time;

  public lightControler() {this("lightControler", 1);}

public lightControler(String name,double Int_arr_time){
   super(name);
   addInport("start");
   addOutport("rowcontrol");
   addOutport("columcontrol");
   int_arr_time = Int_arr_time ;

   addTestInput("start",new entity(""));
   addTestInput("rowcontrol",new doubleEnt(1.0),0);//totally blocked
   addTestInput("columcontrol",new doubleEnt(1.0),0);//totally blocked

}

public void initialize(){
      phase = "passive";
      sigma = INFINITY;
     super.initialize();
 }

public void  deltext(double e,message x)
{
Continue(e);
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"start",i))
         holdIn("green",int_arr_time);
}

public void  deltint( )
{
if(phaseIs("red")){
    holdIn("green",int_arr_time);
    System.out.println("phase: " + phase);
 }
else
   holdIn("red",int_arr_time);
   System.out.println("phase: " + phase);
}

public message  out( )
{
   message  m = new message();

   if (phaseIs("green")){
     m.add(makeContent("rowcontrol", new doubleEnt(1))); //block level
     m.add(makeContent("columcontrol",new doubleEnt(0)));
   }
   if (phaseIs("red")){
     m.add(makeContent("columcontrol", new doubleEnt(1)));
     m.add(makeContent("rowcontrol", new doubleEnt(0)));
   }
  return m;
}

public void showState(){
    super.showState();
}
}

