package refDemo.Grating;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;


public class decelerator2 extends realDevs{

protected double accel[] = new double[7];
  protected int count;
  protected boolean flag;
  protected double Quantum,store;
  double value = 0;


public decelerator2(String  name,double length,double a1,double a2,double a3,double a4,double a5,double a6,double a7){
    super(name);

    accel[0] = a1;
    accel[1] = a2;
    accel[2] = a3;
    accel[3] = a4;
    accel[4] = a5;
    accel[5] = a6;
    accel[6] = a7;
    Quantum  =  length/7;

//addInport("in");
//addOutport("L");
//addOutport("outAccel");

    addTestInput("in",new doubleEnt(1));
    addTestInput("in",new doubleEnt(10));
    addTestInput("in",new doubleEnt(20));
    addTestInput("in",new doubleEnt(15));
}

public decelerator2(){
this("decelerator2",5,-.1,-.11,-.01,-.1,-.01,-.01,-.1);
}

public void initialize(){
    sigma = 0;
    store = 0;
    phase = "Temporary";
    count = 0;
//    super.initialize();
}

public void  deltext(double e,message   x)
{
    Continue(e);

   for (int i = 0; i < x.getLength(); i++)
     if (messageOnPort(x, "in", i)) {
       entity val = x.getValOnPort("in", i);
       doubleEnt f = (doubleEnt) val;
       if ( (f.getv() - store) >= Quantum) { //Only when input is greater by 10
         store = f.getv(); //it is accepted
         sigma = 0;
       if(count < 6)
          count++;
       }
     }
}

public void  deltint( )
{
        passivate();
}

public message  out(){

     value = accel[count];
     message   m = new message();
     m.add(makeContent("out", new doubleEnt(value)));

    return m;
}

}
