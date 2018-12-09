package refDemo.PursuerEvader;


import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class sum extends  ViewableAtomic{
protected double state,initState,lossPercent;


public sum(String nm,double state,double lossPercent){
super(nm);
initState = state;
this.lossPercent = lossPercent;
addInport("in");
addInport("reset");
addOutport("out");
addTestInput("in",new doubleEnt(1),0);
addTestInput("in",new doubleEnt(-1),0);
addTestInput("reset",new entity(),0);
}

public sum(String nm,double state){
this(nm,state,0);
}


public sum(String nm){
this(nm,0,0);
}

public sum(){
this("sum");
}


public void initialize(){
super.initialize();
state = initState;
holdIn("active "+state,0);
}

public void deltext(double e,message x){
Continue(e);

 for (int i=0; i< x.getLength();i++){
 if (messageOnPort(x,"in",i)){
  doubleEnt dv = (doubleEnt)x.getValOnPort("in",i);
  state += dv.getv();
  state = state*(1-lossPercent/100);
   holdIn("active "+state,0);
 }
 else if (messageOnPort(x,"reset",i)){
  state = 0;
  passivateIn("passive "+state);
 }
 }
}

public void deltint(){
  passivateIn("passive "+state);
}

public void deltcon(double e,message x)
{
 deltext(e,x);
}

public message out(){
message m = new message();
m.add(makeContent("out",new doubleEnt(state)));
return m;
}

public void showState(){
   super.showState();
   System.out.println(
  "\n"+ "state :"+ state
  );
  }

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"state :"+ state;
  }

public static void main(String args[]){
//new  sum(" ");
//entity e = new intEnt(1);
}
 }





