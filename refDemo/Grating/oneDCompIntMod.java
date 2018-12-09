package refDemo.Grating;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;

class sv {
public double s,v;

public sv(double s, double v){
this.s = s;
this.v = v;
}
public String toString(){
    return util.doubleFormat.niceDouble(s)
         + ","+util.doubleFormat.niceDouble(v);
  }
}

public class oneDCompIntMod extends realDevs{

protected double  input,quantum,quantumLeft;
protected double clock;
protected double vel;
protected sv state,initialState;
protected boolean flag;

public oneDCompIntMod(String  name, double quantum, sv initialState){
super(name);
this.quantum = quantum;
this.initialState = initialState;
addOutport("outV");
addInport("velocity");
    addTestInput("velocity",new doubleEnt(30));
    addTestInput("velocity",new doubleEnt(40));
    addTestInput("velocity",new doubleEnt(10));
}

public oneDCompIntMod(String  name, double quantum, double s, double v,double eps){
this ( name, quantum, new sv(s,v));
}

public oneDCompIntMod(){
  this("oneDCompIntMod",.1,new sv(0,0)); //quantum taken as 0.1
}

public void initialize(){
super.initialize();
state = initialState;
quantumLeft = quantum;
holdIn(state.toString(),0);
input = 0;
clock = 0;
  flag = true;
}

public double timeAdvance(){ //calculate timeadv depending on accel or vel
    double ta = 0;
    double tav,tax;

    if(input != 0 ){
      tav = Math.abs(quantumLeft / input); //time adv is based on accel
    }
    else
      tav = INFINITY;

      if(state.v != 0)
        tax = Math.abs(quantumLeft / state.v); //time adv is based on vel
      else
        tax = INFINITY;

   return  Math.min(tav,tax);
}

public sv nextState(double e){   //Calculate the next state depending on prev state var and present input
if (e <INFINITY){
     if(flag){
       double nexts, nextv;
      nexts = state.s +e*state.v;
      nextv =  state.v +vel ;
     // flag=false;
      return new sv(nexts,nextv);

     }
 else{
      double nexts, nextv;
      nexts = state.s +e*state.v;
      nextv = state.v + e*input;
      return new sv(nexts,nextv);
   }

}else return state;

}

public void deltint(){

if(state.v < 0 )
    holdIn(state.toString(),INFINITY);  //Once velocity becomes less than zero then passivate
else{
    clock+=sigma;
    state = nextState(sigma);
    quantumLeft = quantum;
    holdIn(state.toString(),timeAdvance());
  }
}

public void  deltext(double e,message   x)
{
  clock += e;
  if (flag){
    for (int i=0; i< x.getLength();i++){ //get whatever value is there on velocity into ve1
     if (messageOnPort(x,"velocity",i)){
         entity val = x.getValOnPort("velocity",i);
         doubleEnt f1 = (doubleEnt)val;
           vel = Math.abs(f1.getv());
           this.state.v=vel;
           flag = false;

         }
     }
  }
  else{
input = getRealValueOnPort(x,"in");
  }

clock+=e;
state = nextState(e);
quantumLeft = quantumLeft*(1 - e/sigma);


holdIn(state.toString(),timeAdvance());

}
public void  deltcon(double e,message   x)
{
clock+=sigma;
state = nextState(e);
quantumLeft = quantum;
input = sumValuesOnPort(x,"in");
holdIn(state.toString(),timeAdvance());
}


public message  out()
{
message   m = new message();
 if (sigma > 0){
      sv next = nextState(sigma);
      m.add(makeContent("out",  new doubleEnt(next.s)));
      m.add(makeContent("outV",  new doubleEnt(next.v)));
    }
return m;
}


public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"quantum :"+ quantum
  +"\n"+"state.s :"+ state.s
    +"\n"+"state.v :"+ vel
    +"\n"+"input :"+ input;
}
}
