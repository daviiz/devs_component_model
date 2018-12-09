/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package refDemo.Simpnet;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class TObs extends ViewableAtomic{

protected double store;
protected double Length;
protected double clock;



  public TObs(){
    this("TObs");
}

public TObs(String name){
   super(name);
   addInport("start");
   addInport("done");
   addOutport("tout");

   addTestInput("start",new entity(""));
   addTestInput("done",new entity(""));
   /*addTestInput("L",new doubleEnt(100));
   addTestInput("pin",new doubleEnt(10));
   addTestInput("pin",new doubleEnt(20));
   addTestInput("pin",new doubleEnt(100));*/
}

public void initialize(){
//     holdIn("passive", 1);
    passivate();
     clock = 0;
     super.initialize();
 }

public void  deltext(double e,message x){
Continue(e);

/*     for (int i=0; i< x.getLength();i++)
       if (messageOnPort(x,"L",i)){
         entity val = x.getValOnPort("L",i);
         doubleEnt f = (doubleEnt)val;
         Length = f.getv();
       }*/
if (phaseIs("passive")) {
    for (int i = 0; i < x.getLength(); i++)
      if (messageOnPort(x, "start", i))
          holdIn("passive", 0);
  }


  if (phaseIs("passive")) {
    for (int i = 0; i < x.getLength(); i++)
      if (messageOnPort(x, "done", i))
          holdIn("respond", 1);
  }
}

public void  deltint(){

  if (phaseIs("respond")) {
    passivate();
  }
  else{
    holdIn("passive", 1);
    clock = clock + 1;
  }
}

public void deltcon(double e,message x)
{
   deltint();          //internal fn followed by external
   deltext(0,x);
}

public message  out(){
    message m = new message();
    if (phaseIs("respond")) {
      content con = makeContent("tout", new doubleEnt(clock));
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
    +"\n"+" display: " + store;
}
}

