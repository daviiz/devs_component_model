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

public class stationnew extends ViewableAtomic{

  protected double delay,volume,size,max,numberofacks;
  protected int count,unitcount,window,packcount,ackcount;
  protected entity job;
  protected String destination;

  public stationnew() {this("stationnew",1, 600,100,"station2",1);}

public stationnew(String name,int windowSize,double Volume,double Size,String Destination,double Delay){
   super(name);
   addInport("in");
   addOutport("out");
   addOutport("done");
   addInport("stop");
   addInport("start");
   delay = Delay ;
   volume = Volume;
   window = windowSize;
   destination = Destination;
   size = Size;

    addTestInput("start",new entity(""));
    addTestInput("stop",new entity(""));
    addTestInput("in",new entity("ack"));
}

public void initialize(){

     phase = "passive";
     sigma = INFINITY;
     count = 1;
     unitcount = 1;
     packcount = 0;
     ackcount = 0;
     numberofacks = 0;

     if(volume%size == 0)
       max = volume/size;
    else
       max = Math.round(volume/size) + 1;          //Calculate for the number of packets

      numberofacks = max/window;                // number of acks expected

     super.initialize();
 }

public void  deltext(double e,message x)
{
Continue(e);

if(phaseIs("passive")){
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"start",i)){             //start packet transmission
        holdIn("transmit", delay);
      }
}
 for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"stop",i))                 //stop ack or packet transmission
          passivate();


 for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"in",i)){
        job = x.getValOnPort("in", i);
        String s = job.toString();

        if(s.startsWith("ack")){
          ackcount = ackcount + 1;

        if (count <= max)
            holdIn("transmit", delay);
        else  if(ackcount == numberofacks)
            holdIn("transmitdone", 0);
        else
            passivate();
        }
      }

 for (int i=0; i< x.getLength();i++)
     if (messageOnPort(x,"in",i)){
      job = x.getValOnPort("in", i);
      String s = job.toString();

      if(s.startsWith("packet"))
         packcount = packcount + 1;

      if(s.startsWith("packet") && (packcount == window)){
        holdIn("transmitack", delay);
        packcount = 0;
      }
    }
}


public void  deltint( )
{

  if (phaseIs("timeouttransmit")){
     count = count - window;
     unitcount = unitcount - 1;
     holdIn("transmit", 0);
   }

if (phaseIs("transmit") && (count <= max)){

     if (unitcount < window){
       holdIn("transmit", 1);
       unitcount = unitcount + 1;
     }
    else {
      unitcount = 1;
      holdIn("timeouttransmit", 10);
    }
   }
   else
   passivate();

  if (phaseIs("transmitack") || phaseIs("transmitdone"))
    passivate();
}

public message  out( )
{
message  m = new message();

if (phaseIs("transmit")){
m.add(makeContent("out", new entity("packet " + count+';'+destination)));
count = count + 1;
}

if (phaseIs("transmitack"))
m.add(makeContent("out", new entity("ack")));

if (phaseIs("transmitdone"))
m.add(makeContent("done", new entity("complete")));

  return m;
}

}

