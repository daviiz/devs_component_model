/*  Name: Vijay Ramamurthy / Micah Lesmeister
 *  ECE 575 Project: Travel Agency Call Center Simulation
 *  Date: 12-15-03
 */


package refDemo.TravelAgency;
import java.awt.Dimension;
import java.awt.Point;

import GenCol.entity;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class TravelAgency extends ViewableDigraph
{
  public TravelAgency ()
  {
    super("TravelAgency");

    addInport("start");
    addOutport("out");
    addTestInput("start",new entity("start"));

    genrCardCust gCC = new genrCardCust();
    genrRegCust gRC = new genrRegCust();
    Switchboard swb = new Switchboard();
    QueueManager qM = new QueueManager();
    SystemStats sS = new SystemStats();

    goldQ gQ = new goldQ();
    silverQ sQ = new silverQ();
    regularQ rQ = new regularQ();

    goldProc gP1 = new goldProc("goldProc1", 0.0, "goldP1");
    silverProc sP1 = new silverProc("silvProc1", 0.0, "silvP1");
    silverProc sP2 = new silverProc("silvProc2", 0.0, "silvP2");
    silverProc sP3 = new silverProc("silvProc3", 0.0, "silvP3");
    regularProc rP1 = new regularProc("regProc1", 0.0, "regP1");
    regularProc rP2 = new regularProc("regProc2", 0.0, "regP2");
    regularProc rP3 = new regularProc("regProc3", 0.0, "regP3");
    regularProc rP4 = new regularProc("regProc4", 0.0, "regP4");
    regularProc rP5 = new regularProc("regProc5", 0.0, "regP5");
    regularProc rP6 = new regularProc("regProc6", 0.0, "regP6");
    regularProc rP7 = new regularProc("regProc7", 0.0, "regP7");

    add(gCC);
    add(gRC);
    add(swb);
    add(qM);
    add(sS);
    add(gQ);
    add(sQ);
    add(rQ);
    add(gP1);
    add(sP1);
    add(sP2);
    add(sP3);
    add(rP1);
    add(rP2);
    add(rP3);
    add(rP4);
    add(rP5);
    add(rP6);
    add(rP7);

    addCoupling(this,"start",gCC,"start");
    addCoupling(this,"start",gRC,"start");
    addCoupling(gCC,"out",swb,"in");
    addCoupling(gRC,"out",swb,"in");
    addCoupling(swb,"goldQ",gQ,"in");
    addCoupling(swb,"silvQ",sQ,"in");
    addCoupling(swb,"regQ",rQ,"in");
    addCoupling(swb,"reject",sS,"busySig");

    addCoupling(gQ,"goldP1",gP1,"newGC");   //gold queue connections
    addCoupling(gQ,"silvP1",sP1,"newGC");
    addCoupling(gQ,"silvP2",sP2,"newGC");
    addCoupling(gQ,"silvP3",sP3,"newGC");
    addCoupling(gQ,"regP1",rP1,"newGC");
    addCoupling(gQ,"regP2",rP2,"newGC");
    addCoupling(gQ,"regP3",rP3,"newGC");
    addCoupling(gQ,"regP4",rP4,"newGC");
    addCoupling(gQ,"regP5",rP5,"newGC");
    addCoupling(gQ,"regP6",rP6,"newGC");
    addCoupling(gQ,"regP7",rP7,"newGC");
    addCoupling(gQ,"status",qM,"goldQStat");
    addCoupling(gQ,"dequeued",swb,"dequeued");
    addCoupling(qM,"goldQCmd",gQ,"cmd");

    addCoupling(sQ,"silvP1",sP1,"newSC");    //silver queue connections
    addCoupling(sQ,"silvP2",sP2,"newSC");
    addCoupling(sQ,"silvP3",sP3,"newSC");
    addCoupling(sQ,"regP1",rP1,"newSC");
    addCoupling(sQ,"regP2",rP2,"newSC");
    addCoupling(sQ,"regP3",rP3,"newSC");
    addCoupling(sQ,"regP4",rP4,"newSC");
    addCoupling(sQ,"regP5",rP5,"newSC");
    addCoupling(sQ,"regP6",rP6,"newSC");
    addCoupling(sQ,"regP7",rP7,"newSC");
    addCoupling(sQ,"status",qM,"silvQStat");
    addCoupling(sQ,"dequeued",swb,"dequeued");
    addCoupling(qM,"silvQCmd",sQ,"cmd");

    addCoupling(rQ,"regP1",rP1,"newRC");      //regular queue connections
    addCoupling(rQ,"regP2",rP2,"newRC");
    addCoupling(rQ,"regP3",rP3,"newRC");
    addCoupling(rQ,"regP4",rP4,"newRC");
    addCoupling(rQ,"regP5",rP5,"newRC");
    addCoupling(rQ,"regP6",rP6,"newRC");
    addCoupling(rQ,"regP7",rP7,"newRC");
    addCoupling(rQ,"status",qM,"regQStat");
    addCoupling(rQ,"dequeued",swb,"dequeued");
    addCoupling(qM,"regQCmd",rQ,"cmd");

    addCoupling(gP1,"freeline",swb,"freeline");
    addCoupling(sP1,"freeline",swb,"freeline");
    addCoupling(sP2,"freeline",swb,"freeline");
    addCoupling(sP3,"freeline",swb,"freeline");
    addCoupling(rP1,"freeline",swb,"freeline");
    addCoupling(rP2,"freeline",swb,"freeline");
    addCoupling(rP3,"freeline",swb,"freeline");
    addCoupling(rP4,"freeline",swb,"freeline");
    addCoupling(rP5,"freeline",swb,"freeline");
    addCoupling(rP6,"freeline",swb,"freeline");
    addCoupling(rP7,"freeline",swb,"freeline");

    addCoupling(gP1,"idle",qM,"goldPIdle");
    addCoupling(gP1,"out",sS,"done");
    addCoupling(sP1,"idle",qM,"silvPIdle");
    addCoupling(sP2,"idle",qM,"silvPIdle");
    addCoupling(sP3,"idle",qM,"silvPIdle");
    addCoupling(sP1,"out",sS,"done");
    addCoupling(sP2,"out",sS,"done");
    addCoupling(sP3,"out",sS,"done");
    addCoupling(rP1,"idle",qM,"regPIdle");
    addCoupling(rP2,"idle",qM,"regPIdle");
    addCoupling(rP3,"idle",qM,"regPIdle");
    addCoupling(rP4,"idle",qM,"regPIdle");
    addCoupling(rP5,"idle",qM,"regPIdle");
    addCoupling(rP6,"idle",qM,"regPIdle");
    addCoupling(rP7,"idle",qM,"regPIdle");
    addCoupling(rP1,"out",sS,"done");
    addCoupling(rP2,"out",sS,"done");
    addCoupling(rP3,"out",sS,"done");
    addCoupling(rP4,"out",sS,"done");
    addCoupling(rP5,"out",sS,"done");
    addCoupling(rP6,"out",sS,"done");
    addCoupling(rP7,"out",sS,"done");
    addCoupling(swb,"late",sS,"late");
    addCoupling(sS,"out",this,"out");

    initialize();
    showState();
  }
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(1127, 686);
        ((ViewableComponent)withName("regularProc regP7")).setPreferredLocation(new Point(577, 599));
        ((ViewableComponent)withName("SysStats")).setPreferredLocation(new Point(807, 237));
        ((ViewableComponent)withName("genrCardCust")).setPreferredLocation(new Point(-9, 217));
        ((ViewableComponent)withName("goldQ")).setPreferredLocation(new Point(351, 18));
        ((ViewableComponent)withName("regularProc regP3")).setPreferredLocation(new Point(577, 362));
        ((ViewableComponent)withName("silverProc silvP1")).setPreferredLocation(new Point(578, 72));
        ((ViewableComponent)withName("regularProc regP5")).setPreferredLocation(new Point(577, 479));
        ((ViewableComponent)withName("goldProc goldP1")).setPreferredLocation(new Point(578, 14));
        ((ViewableComponent)withName("regularProc regP4")).setPreferredLocation(new Point(577, 420));
        ((ViewableComponent)withName("genrRegCust")).setPreferredLocation(new Point(-10, 325));
        ((ViewableComponent)withName("silverQ")).setPreferredLocation(new Point(349, 266));
        ((ViewableComponent)withName("regularProc regP1")).setPreferredLocation(new Point(576, 247));
        ((ViewableComponent)withName("silverProc silvP2")).setPreferredLocation(new Point(578, 131));
        ((ViewableComponent)withName("regularProc regP2")).setPreferredLocation(new Point(576, 304));
        ((ViewableComponent)withName("silverProc silvP3")).setPreferredLocation(new Point(577, 190));
        ((ViewableComponent)withName("regularProc regP6")).setPreferredLocation(new Point(578, 538));
        ((ViewableComponent)withName("Switchboard")).setPreferredLocation(new Point(107, 85));
        ((ViewableComponent)withName("regularQ")).setPreferredLocation(new Point(349, 499));
        ((ViewableComponent)withName("QManager")).setPreferredLocation(new Point(104, 399));
    }
}
