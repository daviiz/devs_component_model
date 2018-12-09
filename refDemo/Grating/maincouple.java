/*      Copyright 1999 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA2.6
 *  Date       : 04-15-00
 */

package refDemo.Grating;
import java.awt.Dimension;
import java.awt.Point;

import GenCol.doubleEnt;
import SimpArc.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import pulseExpFrames.*;
import simView.*;
import statistics.*;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class maincouple extends ViewableDigraph{
protected randNormal r;
//protected double mean=0,sigma=1;

public maincouple(){
    super("maincouple");
    r=new randNormal(1,0,1);

   ViewableDigraph g1 = new couple1();//10
   ViewableDigraph g2 = new couple2();//10
   ViewableAtomic d = new differencer("d",0);
   ViewableAtomic ng = new normgenr("ng",1);
    add(g1);
    add(g2);
    add(d);
    add(ng);

   // addInport("velocity1");
    //addInport("velocity2");

    addOutport("timediff");
    //addOutport("result");

     addTestInput("velocity1",new doubleEnt(r.sample()));
     addTestInput("velocity2",new doubleEnt(r.sample()));

   addCoupling(ng,"out1",g1,"velocity1");
     addCoupling(ng,"out2",g2,"velocity2");


     addCoupling(g1,"outT1",d,"in");
     addCoupling(g2,"outT2",d,"in");
     addCoupling(d,"out",this,"timediff");
   //g1.setBlackBox(true);
     //g2.setBlackBox(true);

 //    initialize();
     showState();
/*
    preferredSize = new Dimension(484, 145);
    g.setPreferredLocation(new Point(13, 18));
    p.setPreferredLocation(new Point(195, 18));
    t.setPreferredLocation(new Point(193, 80));
    */
}



    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(1166, 630);
        ((ViewableComponent)withName("couple2 ")).setPreferredLocation(new Point(136, 315));
        ((ViewableComponent)withName("ng")).setPreferredLocation(new Point(16, 255));
        ((ViewableComponent)withName("d")).setPreferredLocation(new Point(823, 261));
        ((ViewableComponent)withName("couple1 ")).setPreferredLocation(new Point(133, -24));
    }
}
