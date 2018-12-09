/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package refDemo.Simpnet;

import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class station extends ViewableAtomic{

  protected double int_arr_time,volume,size,max;
  protected int count;
  protected entity job;

  public station() {this("station", 1000,100,1);}

public station(String name,double Volume,double Size,double Int_arr_time){
   super(name);
   addInport("in");
   addOutport("out");
   addInport("stop");
   addInport("start");
   int_arr_time = Int_arr_time ;
   volume = Volume;
   size = Size;

    addTestInput("start",new entity(""));
    addTestInput("stop",new entity(""));
    addTestInput("in",new entity("job1"));
}

public void initialize(){

     phase = "passive";
     sigma = INFINITY;
     count = 0;


     if(volume%size == 0)
       max = volume/size;
    else
       max = Math.round(volume/size) + 1;   //Calculate for the number of packets

     super.initialize();
 }

public void  deltext(double e,message x)
{
Continue(e);

if(phaseIs("passive")){
   for (int i=0; i< x.getLength();i++)          //start packet transmission
      if (messageOnPort(x,"start",i)){
        holdIn("transmit", int_arr_time);
      }
}

 //if(phaseIs("transmit")){
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"stop",i))              //stop ack or packet transmission
          // phase = "finishing";
          passivate();
 //}

 for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"in",i)){
        job = x.getValOnPort("in", i);
        String s = job.toString();

        if(s.startsWith("ack"))
        count = count + 1;
        if(count <= max)
        holdIn("transmit",int_arr_time);
        else
          passivate();
      }

 for (int i=0; i< x.getLength();i++)
     if (messageOnPort(x,"in",i)){
      job = x.getValOnPort("in", i);
      String s = job.toString();
      if(s.startsWith("packet"))
        holdIn("transmitack",int_arr_time);
      }
}


public void  deltint( )
{

if (phaseIs("transmit")){
     if (count <= max)
       holdIn("transmit", 10);
     else
       passivate();
   }

  if (phaseIs("transmitack"))
    passivate();
}

public message  out( )
{
   message  m = new message();

if (phaseIs("transmit"))
  m.add(makeContent("out",new entity("packet " + count)));
if (phaseIs("transmitack"))
  m.add(makeContent("out",new entity("ack")));

  return m;
}

}

