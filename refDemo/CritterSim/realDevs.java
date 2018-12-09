/*
 * Package erikhix.Critters
 * Written by Erik Hix
 * Class project
 * ECE - 575
 * The University of Arizona
 * Fall 2003
 * December 2003
 */
package refDemo.CritterSim;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 * @author Erik Hix
 * @version 1.0 12/19/2003
 * @see <A HREF="http://acims.arizona.edu/EDUCATION/ECE575Fall03/ECE575Fall03.html">ECE 575 
 * Home Page >/A>
 *
 * A SLIGHTLY modified version of realDevs.  This class provides most of the functionality of
 * SimpArc.realDevs but does not have a realValue and does not create the "in" and "out" ports
 * commonly found in realDevs.  This class is used to avoid the unused ports.
 */
public class realDevs extends ViewableAtomic {
  public realDevs(String nm){
    super(nm);
  }

  public realDevs(){
    this("realDevs");
  }

  public void initialize(){
    super.initialize();
    passivate();
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

    public void addNameTestInput(String port,String name,double elapsed){
          addTestInput(port,new entity(name),elapsed);
        }

        public void addNameTestInput(String port,String name){
          addTestInput(port,new entity(name),0);
        }

        public void addPortTestInput(String port,double elapsed){
          addTestInput(port,new entity(),elapsed);
        }

        public void addPortTestInput(String port){
          addTestInput(port,new entity(),0);
        }
        public void addRealTestInput(String port,double value,double elapsed){
          addTestInput(port,new doubleEnt(value),elapsed);
        }
        public void addRealTestInput(String port,double value){
          addTestInput(port,new doubleEnt(value),0);
        }

        public void addIntTestInput(String port,int value,double elapsed){
          addTestInput(port,new intEnt(value),elapsed);
        }
        public void addIntTestInput(String port,int value){
          addTestInput(port,new intEnt(value),0);
        }

}