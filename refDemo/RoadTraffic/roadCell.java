package refDemo.RoadTraffic;

import java.awt.Color;

import GenCol.Pair;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import model.plots.DrawCellEntity;

public class roadCell extends TwoDimCell{

  public boolean  accident; //accident cell
  public double aheadAcc; //accident level in ahead cell
  public double accident_to_behind;//tell the behind cell what level the accident is
  public boolean visible;
  public int colornumber,lightFlag1,lightFlag2;

//queue will build up to it
protected double maxflow; //Q
protected double jamdensity; //N
protected double number, numAhead, numBehind;
protected double time_step;
protected double accidentlevel; //how many percentage road are blocked
protected int Flag2=0;

  /* default Default constructor
   */

  public roadCell(){
    this(0,0);
  }

  /* Constructor
   */
  public roadCell(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));
       addInport("roadstart");
       addInport("numAhead");
       addInport("accident");
       addInport("aheadAcc");
       addInport("numBehind");
       addOutport("number"); //coupled to the "numAhead" of i-1 cell and
       addOutport("outaccident");

       addTestInput("roadstart",new entity(""));
       addTestInput("accident",new doubleEnt(1.0/3.0),0);//0.3 of road blocked
       addTestInput("accident",new doubleEnt(2.0/3.0),0);//0.6 of road blocked
       addTestInput("accident",new doubleEnt(1.0),0);//totally blocked
       addTestInput("accident",new doubleEnt(0.0),0);//free flow

     time_step = 1 ;
  } // End cell constructor

  /**
   * Initialization method
   */
  public void initialize() {

     super.initialize();

     //Those value decided by simulation time step,
     //Here is for 5s real time = 1 unit simulation time
     jamdensity = 30; // under congestion
     maxflow = 7.5; //under free flow
     number = 0;  //current vehicle number in this cell i
     numAhead = 0;//i+1 cell
     numBehind = 0;//i-1 cell

     aheadAcc=0.0;
     accident_to_behind=0.0;
     accidentlevel=0.0; //nothing is blocked

     lightFlag1=1;
     lightFlag2=1;
     accident=false;
     visible = true;
   }

  /**
   * External Transition Function
   */
  public void deltext(double e, message x) {
    Continue(e);
    entity ent;
    if(phaseIs("passive")){
       for (int i=0; i< x.getLength();i++)
          if (messageOnPort(x,"roadstart",i))
            numBehind=25./6.;
    }

    // Get notification message from neighbor cells
    for (int i = 0; i < x.getLength(); i++) {
      if (messageOnPort(x, "numAhead",i)) {
        ent = x.getValOnPort("numAhead", i);
        doubleEnt d = (doubleEnt) ent;
        numAhead = d.getv();
      }
    }
    for (int i = 0; i < x.getLength(); i++) {
      if (messageOnPort(x, "numBehind", i)) {
        ent = x.getValOnPort("numBehind", i);
        doubleEnt d = (doubleEnt) ent;
        numBehind = d.getv();
      }

      if (messageOnPort(x, "accident", i)) {
        ent = x.getValOnPort("accident", i);
        doubleEnt d = (doubleEnt) ent;
        accidentlevel = d.getv();
        accident = true;
        accident_to_behind = accidentlevel;
      }

        if (messageOnPort(x, "aheadAcc", i)) {
          ent = x.getValOnPort("aheadAcc", i);
          doubleEnt d = (doubleEnt) ent;
          aheadAcc = d.getv();
          if (aheadAcc == 1)
            lightFlag2 = 0;
          else {
            lightFlag2 = 1;
            Flag2 = 0;
          }
      }
    }
     update();
    holdIn("run", 1);
  }//End of deltext

/*update the vehicle number in the cell
      */
     public void update(){
        double inflow, outflow; //y(t,i) and y(t, i+1);

         inflow = Math.min(numBehind, (1.0 - accidentlevel) * maxflow);
         inflow = Math.min(inflow, 1.0 / 3.0 * (jamdensity - number)); //w=1/3;

      if (lightFlag2==0 && Flag2==0){//
         outflow = Math.min(number, maxflow);
         Flag2 = Flag2 + 1;
       }
         else
              outflow = Math.min(number,(1.0-aheadAcc)* maxflow);
              outflow = Math.min(outflow, 1.0 / 3.0 * (jamdensity - numAhead));

             number = number + inflow - outflow;
         colornumber=(int)(255.-25./3.*number);
       }//End of update()

 /*
     *Internal Transition Function
      */
     public void deltint(){
       holdIn("run",1);
     }

     public void deltcon(double e, message x){
       deltext(e, x);
     }
  /*
      *Message out Function
      */
    public message out(){
    message m = super.out();
    if (accident)
    m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
        x_pos, y_pos,  new Color(255,colornumber,255),
     new Color(255,colornumber,255))));

    if (phaseIs("run")){
     if (visible)
     m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
     x_pos, y_pos, new Color(255,colornumber,255),
     new Color(255,colornumber,255))));

     m.add(makeContent("number", new doubleEnt(number)));
     m.add(makeContent("outaccident",new doubleEnt(accident_to_behind)));
     m.add(makeContent("sendId",new Pair(new Integer(xcoord), new Integer(ycoord))));
    }
    return m;
  }// End of Message out

}//End of the roadCell class