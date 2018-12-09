package refDemo.Grating;
import java.awt.Dimension;
import java.awt.Point;

import model.plots.CellGridPlot;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

/*
The couple2 model couples the decelerator1 model with the integrator where the
accelerations of the segments are provided by the decelerator1 to the integrator and the
integrator then functions accordingly sending its outputs as position and velocity
*/


public class couple2 extends ViewableDigraph{

public couple2 (){
super("couple2 ");

addInport("velocity2");
addOutport("outT2");

double initialPosition = 0;
double quantum = 0.1, eps = 0;

ViewableAtomic comp = new oneDCompIntMod("oneDCompIntMod",quantum,initialPosition,0,0);
add(comp);

ViewableAtomic time2 = new timeobs2("timeobs2");
add(time2);

addCoupling(time2,"tout",this,"outT2");


addCoupling(this,"velocity2",comp,"velocity");
ViewableAtomic speed2 = new speedobs2("speedobs2",10);
add(speed2);
addCoupling(comp,"out",speed2,"pin");
addCoupling(speed2,"vout",time2,"V2");
addCoupling(comp,"outV",speed2,"vin");
ViewableAtomic decelerator2 = new decelerator2("decelerator2",5,-.1,-3,-3,-3,-1,-1,-2);
add(decelerator2);

addCoupling(decelerator2,"out",comp,"in");
addCoupling(comp,"out",decelerator2,"in");

//addCoupling(comp,"out",this,"outP");
//addCoupling(comp,"outV",this,"outV");

CellGridPlot timeP = new CellGridPlot("Time Plot",20,220);
timeP.setCellGridViewLocation(500,500);
timeP.setSpaceSize(100,40);
timeP.setCellSize(5);
timeP.setTimeScale(20);

add(timeP);
addCoupling(comp,"out",timeP,"timePlot");
addCoupling(comp,"outV",timeP,"timePlot");
}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(686, 278);
        ((ViewableComponent)withName("timeobs2")).setPreferredLocation(new Point(393, 25));
        ((ViewableComponent)withName("decelerator2")).setPreferredLocation(new Point(62, 178));
        ((ViewableComponent)withName("oneDCompIntMod")).setPreferredLocation(new Point(84, 61));
        ((ViewableComponent)withName("speedobs2")).setPreferredLocation(new Point(284, 137));
    }
}