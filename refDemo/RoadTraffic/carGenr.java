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
import util.rand;
import view.modeling.ViewableAtomic;

public class carGenr extends ViewableAtomic{


  protected double int_arr_time,ran;
  protected int stat;//stat=1,constant value, stat=0, random
  protected rand r;

 public carGenr() {this("carGenr", 1);}

public carGenr(String name,double Int_arr_time){
   super(name);
   addInport("constant");
   addInport("random");
   addOutport("carout");
   int_arr_time = Int_arr_time ;

   addTestInput("constant",new entity(""),0);
   addTestInput("random",new entity(""),0);
}

public void initialize(){
   holdIn("constant", 1);
  super.initialize();
  ran=25./6.;
 }

public void  deltext(double e,message x)
{
   Continue(e);
     for (int i = 0; i < x.getLength(); i++) {
       if (messageOnPort(x, "constant", i))
         holdIn("constant", int_arr_time);
      }
     for (int i = 0; i < x.getLength(); i++) {
        if (messageOnPort(x, "random", i))
         holdIn("rand", int_arr_time);
     }
}

public void  deltint( )
{
 if(phaseIs("rand")){
     double mean=1.0/4.5;
       ran=-mean*Math.log(Math.random());
    //   ran = r.expon(mean);
       ran=1.0/ran;
       if (ran>20) ran=4.1;
       holdIn("rand",int_arr_time);
       System.out.println("ran: " + ran);
}
 else if(phaseIs("constant")){
        holdIn("constant", int_arr_time);
        ran=25./6.;
      }
else passivate();
}

public message  out( )
{
   message  m = new message();
   content con1 = makeContent("carout",new doubleEnt(ran));
   m.add(con1);
  return m;
}

public void showState(){
    super.showState();
    System.out.println("int_arr_t: " + ran);
}

}

