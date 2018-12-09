/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

/*
This model timeobs accepts L (length of stretch which is by default put as 70) and pin
as inputs. The time is measured like that of a stop watch with a calibration of 0.1s.
Whenever pin first exceeds L , the corresponding time is given out on tout.
For the acceptor model functioning also a stop input port is kept such that
whenever an input appears on the stop port then the present time is outputted as tout.
This model is designed to output time only once.
*/

package refDemo.Grating;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class timeobs extends ViewableAtomic{

protected double store;
protected double velocity;
protected double clock;
protected boolean flag;

  public timeobs(){
    this("timeobs");
}

public timeobs(String name){
   super(name);
   addInport("V1");
   addOutport("tout");

   addTestInput("V1",new doubleEnt(0));
   addTestInput("V1",new doubleEnt(4));
}

public void initialize(){
     holdIn("passive", INFINITY);
     clock = 0;
   //  Length = 70; //default value assumed in case of no input on port L
     flag = true;
 }

public void  deltext(double e,message x){
Continue(e);

   if (phaseIs("passive")) {
     for (int i=0; i< x.getLength();i++)
       if (messageOnPort(x,"V1",i)){
         entity val = x.getValOnPort("V1", i);
         doubleEnt f = (doubleEnt) val;
         velocity = f.getv();
         if(velocity!=0)
           holdIn("calctime",0);

       }
    }
    if (phaseIs("calctime")) {
        for (int i=0; i< x.getLength();i++)
          if (messageOnPort(x,"V1",i)){
            entity val = x.getValOnPort("V1", i);
            doubleEnt f = (doubleEnt) val;
            velocity = f.getv();
            if(velocity==0 || velocity <0)
              holdIn("respond",0);

          }
       }


  }

public void  deltint(){

  if(phaseIs("calctime")){
    holdIn("calctime", 0.1);
    clock = clock + 0.1;
    showState();
  }

    if(phaseIs("respond"))
      passivate();
}

public void deltcon(double e,message x)
{
   deltint();          //internal fn followed by external
   deltext(0,x);
}

public message  out(){
    message m = new message();
    if (phaseIs("respond") && flag) {
      content con = makeContent("tout", new doubleEnt(clock));
      m.add(con);
      flag = false; //After once the ouput is not given
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
   super.getTooltipText();

}
}

