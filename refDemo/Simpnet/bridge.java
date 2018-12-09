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

public class bridge extends ViewableAtomic{//ViewableAtomic is used instead
                                    //of atomic due to its
                                    //graphics capability
    protected entity job;
    protected double processing_time;
    protected String inport1,inport2,outport1,outport2,station;

public bridge(){
this("bridge","LANAin","LANBin","LANAout","LANBout","station2",20);
}

public bridge(String  name,String  inPort1,String  inPort2,String outPort1,String outPort2,String  Station,double  Processing_time){
super(name);

addInport(inPort1);    // adding input and output ports
addInport(inPort2);
addOutport(outPort1);
addOutport(outPort2);

inport1 = inPort1;
inport2 = inPort2;
outport1 = outPort1;
outport2 = outPort2;

station = Station;

processing_time = Processing_time;
addTestInput(inport1,new entity("packet1;station2"));
addTestInput(inport1,new entity("packet1;station1"));
addTestInput(inport2,new entity("packet1;station2"));
addTestInput(inport2,new entity("packet1;station1"));

addTestInput(inport1,new entity("job2"));
addTestInput(inport2,new entity("job"));
}

public void initialize(){
     phase = "passive";
     sigma = INFINITY;
     job = new entity("job");
     super.initialize();
 }

public void  deltext(double e,message   x)
{
Continue(e);

//if (phaseIs("passive"))
 for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,inport1,i))
      {
      job = x.getValOnPort(inport1,i);
      String s = job.toString();
      if(s.startsWith("packet"))
      {
        String s1 = s.substring(s.indexOf(';')+1);
        if(s1.equals(station))                   //check for destination
          holdIn("Output2",processing_time);
      }
      else
        holdIn("Output2",processing_time);
      }

    for (int i=0; i< x.getLength();i++)
    if (messageOnPort(x,inport2,i))
        {
        job = x.getValOnPort(inport2,i);
        String s = job.toString();
        if(s.startsWith("packet"))
        {
          String s1 = s.substring(s.indexOf(';')+1);
          if(s1.equals(station))                      //check for destination
            holdIn("Output1",processing_time);
        }
        else
          holdIn("Output1",processing_time);
        }

}

public void  deltint( )
{
passivate();
job = new entity("none");
}

public void deltcon(double e,message x)
{
   deltint();
   deltext(0,x);
}

public message    out( )
{
message   m = new message();

if (phaseIs("Output2")) {
m.add(makeContent(outport2,job));          //output whatever is received
}
   else if (phaseIs("Output1")) {
   m.add(makeContent(outport1,job));
   }

return m;
}

}
