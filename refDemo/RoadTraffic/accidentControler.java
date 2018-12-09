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

public class accidentControler extends ViewableAtomic{

  protected double int_arr_time;
  protected double accidentlevel;

 public accidentControler() {this("accidentControler", 20,1);}

public accidentControler(String name,double Int_arr_time, double level){
   super(name);
   addInport("accidentlevel");
   addOutport("out");
   int_arr_time = Int_arr_time ;
   accidentlevel=level;

   addTestInput("accidentlevel",new doubleEnt(0.33),0);
   addTestInput("accidentlevel",new doubleEnt(0.66),0);
   addTestInput("accidentlevel",new doubleEnt(1.0),0);
   addTestInput("accidentlevel",new doubleEnt(0),0);
}

public void initialize(){
   holdIn("passive", int_arr_time);
   super.initialize();
   //accidentlevel=1.0;
 }

public void  deltext(double e,message x)
{
   Continue(e);
   entity ent;
     for (int i = 0; i < x.getLength(); i++) {
       if (messageOnPort(x, "accidentlevel",i)) {
          ent = x.getValOnPort("accidentlevel", i);
          doubleEnt d = (doubleEnt) ent;
          accidentlevel = d.getv();
          if(accidentlevel==0.33)
            holdIn("minor accident",20);
            else if(accidentlevel==0.66)
              holdIn("medium accident",120);
            else if(accidentlevel==1)
              holdIn("serious accident",20);
        }
     }
}

public void  deltint( )
{
if(phaseIs("passive")){
       if(accidentlevel==0.33)
       holdIn("minor accident", 100);
       else if(accidentlevel==0.66)
         holdIn("medium accident", 100);
       else if(accidentlevel==1)
         holdIn("serious accident", 100);
       accidentlevel=0.0;
     }
else
holdIn("accident clean",INFINITY);
}

public message  out( )
{
   message  m = new message();
   content con1 = makeContent("out",new doubleEnt(accidentlevel));
   m.add(con1);
  return m;
}

public void showState(){
    super.showState();
   // System.out.println("int_arr_t: " + ran);
}

}

