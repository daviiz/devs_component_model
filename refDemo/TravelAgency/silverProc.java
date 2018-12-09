/*  Name: Vijay Ramamurthy / Micah Lesmeister
 *  ECE 575 Project: Travel Agency Call Center Simulation
 *  Date: 12-15-03
 */

package refDemo.TravelAgency;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class silverProc extends ViewableAtomic
{
    protected double processing_time, SPafterCallTime, SPserviceTime;
    protected Customer currCus;
    protected boolean idle, afterCall;
    protected String currType, procID;

//    public silverProc()
//    {
//        this("silverProc", 0.0);
//    }

    public silverProc(String  name, double Processing_time, String ID)
    {
        super("silverProc " + ID);
        addInport("newGC");
        addInport("newSC");
        addOutport("out");
        addOutport("idle");
        addOutport("freeline");
        processing_time = Processing_time;
        procID = ID;
        initialize();
    }

    public void initialize()
    {
        SPafterCallTime = 0;
        SPserviceTime = 0;
        idle = true;
        afterCall = false;
        phase = "passive";
        sigma = INFINITY;
        super.initialize();
    }

    public void deltext(double e,message x)
    {
        Continue(e);

        if(somethingOnPort(x,"newGC"))
        {
            entity temp = getEntityOnPort(x,"newGC");
            currCus = (Customer)temp;
            currType = currCus.type();
            SPserviceTime = 0.95 * currCus.serviceTime();
            SPafterCallTime = currCus.afterCallTime();
            idle = false;
            holdIn(currType,SPserviceTime);
        }

        else if(somethingOnPort(x,"newSC"))
        {
            entity temp = getEntityOnPort(x,"newSC");
            currCus = (Customer)temp;
            currType = currCus.type();
            SPserviceTime = 0.95 * currCus.serviceTime();
            SPafterCallTime = currCus.afterCallTime();
            idle = false;
            holdIn(currType,SPserviceTime);
        }
    }

    public void  deltint( )
    {
        if (afterCall == true)
            holdIn("afterCall",SPafterCallTime);
        else
           passivate();
    }

    public void deltcon(double e,message x)
    {
        deltint();
        deltext(0,x);
    }

    public message out()
    {
        if (phaseIs("idle") && idle == true)
        {
            message  m = new message();
            content con = makeContent("idle",new entity(procID));
            m.add(con);
            return m;
        }

        if(phaseIs("gold") || phaseIs("silver"))
        {
            message  m = new message();
            content con1 = makeContent("out",currCus);
            content con2 = makeContent("freeline",new entity("freeline"));
            m.add(con1);
            m.add(con2);
            idle = true;
            afterCall = true;
            holdIn("afterCall",SPafterCallTime);
            return m;
        }

        if(phaseIs("afterCall"))
        {
            message m = new message();
            content con = makeContent("idle",new entity(procID));
            m.add(con);
            idle = true;
            afterCall = false;
            return m;
        }

        else return new message();
    }

    public boolean somethingOnPort(message x,String port)
    {
        for (int i=0; i< x.getLength();i++)
            if (messageOnPort(x,port,i))
            {
              return true;
            }
            return false;
    }

    public void addRealTestInput(String port,double value)
    {
        addTestInput(port,new doubleEnt(value),0);
    }

    public double getRealValueOnPort(message x,String port)
    {
        doubleEnt dv = (doubleEnt)getEntityOnPort(x,port);
        return dv.getv();
    }

    public entity getEntityOnPort(message x,String port)
    {
        for (int i=0; i< x.getLength();i++)
            if (messageOnPort(x,port,i))
                return x.getValOnPort(port,i);
            return null;
    }

    public message outputRealOnPort(double r,String port)
    {
        message m = new message();
        m.add(makeContent(port,new doubleEnt(r)));
        return m;
    }
}