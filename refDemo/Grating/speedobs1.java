/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

/*
This model speedobs1 takes threshold as one of the arguments of its constructor. It accepts
pin and vin as inputs. Whenever pin crosses a particular threshold (here it is 10) then the
corresponding vin is outputted at port vout. For the acceptor model functioning
also a stop input port is kept such that whenever an input appears on the stop
port then the present vin is outputted as vout.
*/
package refDemo.Grating;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class speedobs1 extends ViewableAtomic{

protected double store,Threshold;
protected double Velocity;
protected boolean flag;

  public speedobs1(){
    this("speedobs1",10); //name and threshold are arguments
}

public speedobs1(String name,double threshold){
   super(name);
   addInport("vin");
   addInport("pin");
   addInport("stop");
   addOutport("vout");
   Threshold = threshold;
   addTestInput("vin",new doubleEnt(0));
   addTestInput("vin",new doubleEnt(1));
   addTestInput("vin",new doubleEnt(5));
   addTestInput("pin",new doubleEnt(5));
   addTestInput("pin",new doubleEnt(10));
   addTestInput("pin",new doubleEnt(20));
   addTestInput("stop",new entity("stop"));
}

public void initialize(){
     phase = "passive";
     sigma = INFINITY;
     store = 0;
     flag=true;
     super.initialize();
 }

public void  deltext(double e,message x){
Continue(e);

   if (phaseIs("passive")){
     for (int i=0; i< x.getLength();i++)
       if (messageOnPort(x,"vin",i)){
         entity val = x.getValOnPort("vin",i);
         doubleEnt f = (doubleEnt)val;
         Velocity = f.getv();
         if(Velocity==0 || Velocity<0)
           holdIn("respond",0);
       }
  }

  if (phaseIs("passive")) {
    for (int i = 0; i < x.getLength(); i++)
      if (messageOnPort(x, "pin", i)) {
        entity val = x.getValOnPort("pin", i);
        doubleEnt f = (doubleEnt) val;
        if ( ((f.getv() - store) >= Threshold)||flag){ //Only when input is greater by Threshold
          store = f.getv();                     //it is accepted
          flag=false;
          holdIn("respond", 0);
        }
      }
  }

  for (int i=0; i< x.getLength();i++)        //when input comes on stop, vout is given
      if (messageOnPort(x,"stop",i))
         holdIn("respond",0);

}

public void  deltint(){
    passivate();
    showState();
}

public void deltcon(double e,message x)
{
   deltint();          //internal fn followed by external
   deltext(0,x);
}

public message  out(){
    message m = new message();
    if (phaseIs("respond")) {
      content con = makeContent("vout", new doubleEnt(Velocity));
      m.add(con);
    }
    return m;
}

 public void showState(){
  super.showState();
  System.out.println("state of  "  +  name  +  ": " );
  System.out.println("phase, sigma : "
          + phase  +  " "  +  sigma  +  " "   );
  System.out.println("store: " + store);
}

 public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" store: " + store +"\n"+" Vel: "+Velocity;
}
}

