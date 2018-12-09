package refDemo.RadarProject;
import java.awt.Color;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.message;
import view.modeling.ViewableAtomic;

// public class R2Analyzer extends SimpArc.realDevs{
public class R2Analyzer extends ViewableAtomic{

  protected radarentity m_myRadar;
  protected radarentity m_extRadar;
  protected boolean hasR2;
  protected double extTimer;

  public R2Analyzer(String name) {
    super(name);

    addInport("MyRadarIn");
    addInport("ExtRadarIn");

    addOutport("RadarOut");

    addTestInput("ExtRadarIn",new trackentity("T1",2,3,3,4,10,new Color(255, 0, 0)));
    addTestInput("MyRadarIn",new trackentity("T2",5,6,3,4,10,new Color(0, 255, 255)));
    addTestInput("MyRadarIn",new trackentity("T3",5,7,3,4,10,new Color(0, 255, 255)), 1);

  }

  public R2Analyzer() {
    this("R2Analyzer");
 }

 public void initialize() {
   super.initialize();

   m_myRadar = new radarentity("T0", 0, -50.0, 0.0, Color.gray);
   m_extRadar= new radarentity("T0", 0, -50.0, 0.0, Color.gray);

   hasR2 = false;
   passivateIn("HasR2");
 }

 public void deltext(double e, message   x){

   radarentity mytrackent;

  Continue(e);
  for (int i=0; i< x.getLength(); i++){
    if (messageOnPort(x, "MyRadarIn",i)){
      m_myRadar = new radarentity ((radarentity) x.getValOnPort("MyRadarIn", i));
 //     System.out.println(name + " my" + m_myRadar.getTQ() + " ext" + m_extRadar.getTQ() + " r2" +hasR2);

      if(HasR2()){
        if ( (phaseIs("HasR2Ext")) || (phaseIs("WaitExt"))) {
          holdIn("HasR2NoExt", 0);
        }
        else{
          m_extRadar.m_TQ = 0; // External not reporting
          holdIn("HasR2NoExt", 0);
        }
      }
      else{
        holdIn("WaitNoExt", 1);
      }
      System.out.println(name + " my" + m_myRadar.getTQ() + " ext" + m_extRadar.getTQ() + " state " +phase);
    }
    else if (messageOnPort(x, "ExtRadarIn",i)){
      m_extRadar = new radarentity ((radarentity) x.getValOnPort("ExtRadarIn", i));
 //     System.out.println(name + " ext" + m_extRadar.getTQ() + " my" + m_myRadar.getTQ() + " r2" + hasR2);
      if(HasR2()){
          holdIn("HasR2Ext", INFINITY);
      }
      else{
        holdIn("WaitExt", INFINITY);
      }
      System.out.println(name + " ext" + m_extRadar.getTQ() + " my" + m_myRadar.getTQ() + " state " +phase);
    }
  }
}

 public void  deltint( ){

   if (phaseIs("WaitNoExt")){
     m_extRadar.m_TQ = 0; // External not reporting
     holdIn("HasR2NoExt", 0);
   }
   passivateIn(phase);
 }

 public boolean HasR2(){

 return true;
 }

 public void deltcon(double e, message x){
   deltext(e,x);
   deltint();
}

 public message out(){
    message m = super.out();
    if((phaseIs("HasR2Ext"))||(phaseIs("HasR2NoExt"))){
      m.add(makeContent("RadarOut", new radarentity(m_myRadar)));
      System.out.println(name + " out: " + m_myRadar.getTQ()+" "+ m_myRadar.getX()+" "+ m_myRadar.getY()+" " + m_myRadar.getColor());
    }
    return m;
 }


 public String getTooltipText(){
  return
  super.getTooltipText()
  +"\n"+"my TQ:" + m_myRadar.getTQ()
  +"\n"+"ext TQ:" + m_extRadar.getTQ()
  +"\n"+"Has R2:" + hasR2;
 }

public static int signOf(double x){
   if (x == 0) return 0;
   else if (x > 0) return 1;
   else return -1;
 }

 public static double inv(double x){
   if (x == 0) return Double.POSITIVE_INFINITY;
   else if (x >= Double.POSITIVE_INFINITY ) return 0;
   else return 1/x;
 }

 public boolean somethingOnPort(message x,String port){
 for (int i=0; i< x.getLength();i++)
 if (messageOnPort(x,port,i))
 return true;
 return false;


 }

 public entity getEntityOnPort(message x,String port){

 for (int i=0; i< x.getLength();i++)
 if (messageOnPort(x,port,i)){
 return x.getValOnPort(port,i);
 }

 return null;
 }

 public int getIntValueOnPort(message x,String port){
 intEnt dv = (intEnt)getEntityOnPort(x,port);
 return dv.getv();
 }


 public double getRealValueOnPort(message x,String port){
 doubleEnt dv = (doubleEnt)getEntityOnPort(x,port);
 return dv.getv();
 }


 public double sumValuesOnPort(message x,String port){
 double val = 0;
 for (int i=0; i< x.getLength();i++)
 if (messageOnPort(x,port,i)){
 doubleEnt dv = (doubleEnt)x.getValOnPort(port,i);
 val += dv.getv();
 }
 return val;
 }

 public String getNameOnPort(message x,String port){
 entity ent = getEntityOnPort(x,port);
 return ent.getName();
 }


 public message outputNameOnPort(String nm,String port){
 message m = new message();
 m.add(makeContent(port,new entity(nm)));
 return m;
 }

 public message outputNameOnPort(message m,String nm,String port){
 m.add(makeContent(port,new entity(nm)));
 return m;
 }

 public message outputIntOnPort(message m,int r,String port){
 m.add(makeContent(port,new intEnt(r)));
 return m;
 }

 public message outputIntOnPort(int r,String port){
 message m = new message();
 m.add(makeContent(port,new intEnt(r)));
 return m;
 }

 public message outputRealOnPort(message m,int r,String port){
 m.add(makeContent(port,new doubleEnt(r)));
 return m;
 }

 public message outputRealOnPort(double r,String port){
 message m = new message();
 m.add(makeContent(port,new doubleEnt(r)));
 return m;
 }

 public message outputRealOnPort(message m,double r,String port){
 m.add(makeContent(port,new doubleEnt(r)));
 return m;
 }
}
