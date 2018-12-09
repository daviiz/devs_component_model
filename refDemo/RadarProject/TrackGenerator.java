package refDemo.RadarProject;
import java.awt.Color;

import model.modeling.message;
import view.modeling.ViewableAtomic;


class sv {
public double x,y,velocity;

public sv(double x,double y, double velocity){
this.x = x;
this.y = y;
this.velocity = velocity;
}
public String toString(){
    return util.doubleFormat.niceDouble(x)
         + ","+ util.doubleFormat.niceDouble(y)+ ","+util.doubleFormat.niceDouble(velocity);
  }
}

public class TrackGenerator extends ViewableAtomic{

protected double  input,quantum,quantumLeft;
protected double clock;
public double eps = 0;//10./100; //choice could be max input / 100;

public double m_postiton;
public double m_lastpostion;
protected trackentity m_track;


protected sv state,initialState;

public TrackGenerator(String  name, double quantum,trackentity track){

super(name);
    m_track = track;
    this.quantum = quantum;
sv intiatialState = new sv(track.getX(),track.getY(),track.getSpeed());
this.initialState = intiatialState;
addOutport("TrackOut");
addOutport("GroundTruth");
}


public TrackGenerator(){
  this("TrackGenerator",.1,new trackentity("T1",1, 1, 45,1,1, new Color(69,69,69)));
  }


public static int positive(double x){
     if (x > 0) return 1;
    else return  0;
}

public  double equivInputZero(double x){
if (Math.abs(x) <= eps)
 return 0;
else return x;
}

public void initialize(){
super.initialize();
state = initialState;
quantumLeft = quantum;

holdIn(state.toString(),timeAdvance());
input = 0;
clock = 0;

}

  public double timeAdvance()
  {




    double value = 0;

//System.out.println("Velocity " + state.velocity);
    value = (1.0/state.velocity);
  return  value;

  }

public void SetTrack(trackentity track)
  {
    m_track = track;
    sv intiatialState = new sv(track.getX(),track.getY(),track.getSpeed());
    this.initialState = intiatialState;

    state = intiatialState;
   // System.out.println("hi hi");
    holdIn(state.toString(),timeAdvance());


  }
  public sv nextState(double e){

  if (e <INFINITY){
    double nextx = m_track.getX();
    double nexty = m_track.getY();
    double nextv = state.velocity + e*input;
    double course = m_track.getCourse();
    if(course > 0 && course <= 45)
    {
      nextx = state.x +e*state.velocity;
      nexty = state.y +e*state.velocity;


    }
    else if(course > 45 && course <= 90)
    {
      nextx = state.x +e*state.velocity;



    }
    else if(course >90 && course <=135)
    {
      nextx = state.x +e*state.velocity;
      nexty = state.y -e*state.velocity;



    }
    else if(course > 135 && course <= 180)
    {
      nextx = state.x +e*state.velocity;
      nexty = state.y -e*state.velocity;



    }
    else if(course > 180 && course <= 225)
    {
      nexty = state.y -e*state.velocity;
    }
    else if(course > 225 && course <= 270)
    {
      nextx = state.x -e*state.velocity;
      nexty = state.y -e*state.velocity;
    }
    else if(course > 270 && course <= 315)
    {
      nextx = state.x -e*state.velocity;
      nexty = state.y +e*state.velocity;
    }
    else if(course > 315 && course <= 360)
    {
      nextx = state.x -e*state.velocity;
      nexty = state.y +e*state.velocity;
    }
    else
    {
      nextx = state.x -e*state.velocity;

    }


  return new sv(nextx,nexty,nextv);
  }
  else
   {

     return state;
   }
  }


public void deltint(){


clock+=sigma;
state = nextState(sigma);
quantumLeft = quantum;


holdIn(state.toString(),timeAdvance());


}

public void  deltext(double e,message   x)
{


clock+=e;
state = nextState(e);
quantumLeft = quantumLeft*(1 - e/sigma);


}

public void  deltcon(double e,message   x)
{


clock+=sigma;
state = nextState(e);
quantumLeft = quantum;
//input = equivInputZero(sumValuesOnPort(x,"in"));
holdIn(state.toString(),timeAdvance());
}


public message    out( )
{


message   m = new message();
/*sv next = nextState(sigma);
       */

//(String nm,double x, double y, double course,double speed, double RCS, Color color){

m.add(makeContent("TrackOut",  new trackentity(m_track.getName(),state.x,state.y,m_track.getCourse(),state.velocity,m_track.getRCS(),m_track.getColor())));


//String nm,int TQ, double x, double y, Color color
m.add(makeContent("GroundTruth",  new radarentity(m_track.getName(),15,state.x,state.y,Color.black)));


return m;
}


public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"quantum :"+ quantum
  +"\n"+"state.x :"+ state.x
  +"\n"+"state.y :"+ state.y
    +"\n"+"state.velocity :"+ state.velocity
    +"\n"+"input :"+ input;
}
}
