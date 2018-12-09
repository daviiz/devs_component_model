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

public class Switchboard extends ViewableAtomic
{
    protected double processing_time;
    protected int numLinesUsed, regQCapac, cardQCapac;
    protected Customer currCus;
    protected double capacGoldP, capacSilvP, capacRegP;
    protected double cusInGoldQ, cusInSilvQ, cusInRegQ;
    protected double custWaitToler;
    protected double goldWait, silvWait, regWait;

    public Switchboard()
    {
        this("Switchboard", 0.0);
    }

    public Switchboard(String  name, double Processing_time)
    {
        super("Switchboard");
        addInport("in");
        addInport("freeline");
        addInport("dequeued");
        addOutport("reject");
        addOutport("regQ");
        addOutport("silvQ");
        addOutport("goldQ");
        addOutport("late");
        addOutport("out");
        processing_time = Processing_time;
        initialize();
    }

    public void initialize()
    {
        phase = "passive";
        sigma = INFINITY;
        numLinesUsed = 0;
        regQCapac = 47;
        cardQCapac = 50;
        capacGoldP = 1;
        capacSilvP = 3;
        capacRegP = 7;
        cusInGoldQ = 0;
        cusInSilvQ = 0;
        cusInRegQ = 0;
        custWaitToler = 0;
        super.initialize();
    }

    public void deltext(double e,message x)
    {
        Continue(e);

        if(phaseIs("passive") && somethingOnPort(x,"in"))
        {
            entity temp = getEntityOnPort(x,"in");
            currCus = (Customer)temp;
            String currType = currCus.type();
            custWaitToler = currCus.waitTolerance();

            if(currType == "regular")
            {
                if (numLinesUsed >= regQCapac)
                    holdIn("reject", processing_time);

                else
                {
                    if (custWaitToler >= this.compCurrWait("regular"))
                        holdIn("regularC", processing_time);
                    else
                        holdIn("intolerable", processing_time);
                }
            }

            else if(currType == "silver")
            {
                if (numLinesUsed >= cardQCapac)
                    holdIn("reject", processing_time);

                else
                {
                    if (custWaitToler >= this.compCurrWait("silver"))
                        holdIn("silverC", processing_time);
                    else
                        holdIn("intolerable", processing_time);
                }
            }

            else if(currType == "gold")
            {
                if (numLinesUsed >= cardQCapac)
                    holdIn("reject", processing_time);

                else
                {
                    if (custWaitToler >= this.compCurrWait("gold"))
                        holdIn("goldC", processing_time);
                    else
                        holdIn("intolerable", processing_time);
                }
            }
        }

        if(somethingOnPort(x,"freeline"))
        {
            numLinesUsed--;
        }

        if(somethingOnPort(x,"dequeued"))
        {
            entity temp = getEntityOnPort(x,"dequeued");
            if(temp.getName() == "GCdqd")
                cusInGoldQ--;
            else if(temp.getName() == "SCdqd")
                cusInSilvQ--;
            else if(temp.getName() == "RCdqd")
                cusInRegQ--;
        }
    }

    public void  deltint( )
    {
      if (phaseIs("passive") || phaseIs("reject") || phaseIs("regularC") || phaseIs("silverC") || phaseIs("goldC") || phaseIs("intolerable"))
      {
          passivate();
      }
    }

    public void deltcon(double e,message x)
    {
        deltint();
        deltext(0,x);
    }

    public message out()
    {

        if (phaseIs("intolerable"))
        {
            message  m = new message();
            content con = makeContent("late",currCus);
            m.add(con);
            return m;
        }

        if (phaseIs("reject"))
        {
            message  m = new message();
            content con = makeContent("reject",currCus);
            m.add(con);
            return m;
        }

        if(phaseIs("regularC"))
        {
            numLinesUsed++;
            message  m = new message();
            content con = makeContent("regQ",currCus);
            m.add(con);
            cusInRegQ++;
            return m;
        }

        if(phaseIs("silverC"))
        {
            numLinesUsed++;
            message  m = new message();
            content con = makeContent("silvQ",currCus);
            m.add(con);
            cusInSilvQ++;
            return m;
        }

        if(phaseIs("goldC"))
        {
            numLinesUsed++;
            message  m = new message();
            content con = makeContent("goldQ",currCus);
            m.add(con);
            cusInGoldQ++;
            return m;
        }

        else return new message();
    }

    public double compCurrWait(String type)
    {
        double denomG, denomS, denomR;

        if (cusInGoldQ <= (capacGoldP+capacSilvP+capacRegP))
            if (cusInGoldQ >= 1)
                denomG = cusInGoldQ;
            else denomG = 1;
        else
            denomG = capacGoldP+capacSilvP+capacRegP;

        if ((cusInGoldQ+cusInSilvQ) <= (capacSilvP+capacRegP))
            if ((cusInGoldQ+cusInSilvQ) >= 1)     //to avoid NaN due to divide by 0
                denomS = cusInGoldQ+cusInSilvQ;
            else denomS = 1;
        else
            denomS = capacSilvP+capacRegP;

        if ((cusInGoldQ+cusInSilvQ+cusInRegQ) <= capacRegP)
            if ((cusInGoldQ+cusInSilvQ+cusInRegQ) >= 1)
                denomR = cusInGoldQ+cusInSilvQ+cusInRegQ;
            else denomR = 1;
        else
            denomR = capacRegP;

        goldWait = (cusInGoldQ / denomG) * 4.148 * 0.88;  //compute gold wait and return
        if(type == "gold")
            return goldWait;

        silvWait = goldWait + (cusInSilvQ / denomS) * 4.148 * 0.95;   //compute silv wait and return
        if(type == "silver")
            return silvWait;

        regWait = goldWait + silvWait + (cusInRegQ / denomR) * 4.148;    //compute reg wait and return
        return regWait;

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

    public String getTooltipText()
    {
        return super.getTooltipText() +"\n"+"linesUsed :" + numLinesUsed
                                      +"\n"+"goldWait :" + goldWait
                                      +"\n"+"silvWait :" + silvWait
                                      +"\n"+"regWait :" + regWait
                                      +"\n"+"cusWaitToler :" + custWaitToler;
    }
}