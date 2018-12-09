/*      Copyright 1999 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA2.6
 *  Date       : 04-15-00
 */

package refDemo.Grating;
import GenCol.doubleEnt;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class normgenr extends ViewableAtomic{


  protected double interArrivalTime;
  protected int count;
  protected randNormal r;

  public normgenr() {this("normgenr", 1);}

public normgenr(String name,double interArrivalTime){
   super(name);

   addOutport("out1");
  addOutport("out2");

   this.interArrivalTime = interArrivalTime ;


}

public void initialize(){
   holdIn("active", interArrivalTime);

    //  phase = "passive";
   //  sigma = INFINITY;
     count = 0;
     r=new randNormal(1,0,3);
     super.initialize();
 }

public void  deltext(double e,message x)
{

}





public void  deltint( )
{

if(phaseIs("active")&&count<1){
 count =count+1;
   holdIn("active",interArrivalTime);
}
else passivate();
}




public message  out( )
{
   message  m = new message();
   content con = makeContent("out1",
            new doubleEnt(r.sample()));
   content con1 = makeContent("out2",
           new doubleEnt(r.sample()));
   //content con2 = makeContent("out1",
          //  new doubleEnt(r.sample()));

   m.add(con);
   m.add(con1);
   //m.add(con2);
   return m;
}


public void showState(){
    super.showState();
    System.out.println("int_arr_t: " + interArrivalTime);
}

public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" interArrivalTime: " + interArrivalTime
     +"\n"+" count: " + count;
  }

}

