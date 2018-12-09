package refDemo.RoadTraffic;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class numberInfirstcell extends ViewableAtomic{//ViewableAtomic is used instead
                                    //of atomic due to its
                                    //graphics capability
protected double number, time;

public numberInfirstcell(){
this("numberInfirstcell");
}

public numberInfirstcell(String  name){
super(name);
addInport("in");
addOutport("queue");
addTestInput("in",new doubleEnt(0));
}

public void initialize(){
     phase = "passive";
     sigma = INFINITY;
     super.initialize();
     time=0;
 }

public void  deltext(double e,message   x)
{
Continue(e);
   time=time+1;
entity ent;
if (phaseIs("passive"))
 for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,"in",i))
      {
        ent = x.getValOnPort("in", i);
         doubleEnt d = (doubleEnt) ent;
         number = d.getv();
    }
holdIn("display",0);
}

public void  deltint( )
{
holdIn("passive",INFINITY);
System.out.println("qunue: " + number);
System.out.println("time: " + time);

}

public void deltcon(double e,message x)
{
   deltint();
   deltext(0,x);
}

public message    out( )
{
message   m = new message();
if (phaseIs("display")) {
m.add(makeContent("queue",new doubleEnt(number)));
}
return m;
}

 public void showState(){
  super.showState();
 }
}