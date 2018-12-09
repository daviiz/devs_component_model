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

public class SystemStats extends ViewableAtomic
{
    protected double processing_time;
    protected Customer currCus;
    protected String currType;
    protected double totalRegular, totalSilver, totalGold, totalCard, totalServed;
    protected double totalRegCreated, totalSilvCreated, totalGoldCreated;
    protected double totalCardBusy, totalRegBusy, totalSilvBusy, totalGoldBusy, totalBusy;
    protected double percCardBusy, percRegBusy, percSilvBusy, percGoldBusy;
    protected double totalGoldLate, totalSilverLate, totalCardLate, totalRegLate, totalLate;
    protected double waitGCExceeded, waitSCExceeded, waitRCExceeded, percGCWaitExc, percSCWaitExc, percRCWaitExc;

    public SystemStats()
    {
        this("SysStats", 0.0);
    }

    public SystemStats(String  name, double Processing_time)
    {
        super("SysStats");
        addInport("done");
        addInport("busySig");
        addInport("late");
        addInport("finstats");
        addTestInput("finstats",new entity("stats"));
        addOutport("out");
        processing_time = Processing_time;
        initialize();
    }

    public void initialize()
    {
        phase = "passive";
        sigma = INFINITY;
        totalRegular = 0;
        totalSilver = 0;
        totalGold = 0;
        totalCard= 0;
        totalServed = 0;
        totalBusy = 0;
        totalLate = 0;
        totalRegCreated = 0;
        totalSilvCreated = 0;
        totalGoldCreated = 0;
        totalCardBusy = 0;
        totalRegBusy = 0;
        totalSilvBusy = 0;
        totalGoldBusy = 0;
        percCardBusy = 0;
        percRegBusy = 0;
        percSilvBusy = 0;
        percGoldBusy = 0;
        waitGCExceeded = 0;
        waitGCExceeded = 0;
        waitGCExceeded = 0;
        percGCWaitExc = 0;
        percSCWaitExc = 0;
        percRCWaitExc = 0;
        super.initialize();
    }

    public void deltext(double e,message x)
    {
        Continue(e);

        if(somethingOnPort(x,"done"))
        {
            entity temp = getEntityOnPort(x,"done");
            currCus = (Customer)temp;
            this.process("done");
            holdIn("busy",processing_time);
        }

        if(somethingOnPort(x,"busySig"))
        {
            entity temp = getEntityOnPort(x,"busySig");
            currCus = (Customer)temp;
            this.process("busySig");
            holdIn("busy",processing_time);
        }

        if(somethingOnPort(x,"late"))
        {
            entity temp = getEntityOnPort(x,"late");
            currCus = (Customer)temp;
            this.process("late");
            holdIn("busy",processing_time);
        }

        if(somethingOnPort(x,"finstats"))
        {
            this.finalStats();
        }
    }

    public void  deltint( )
    {
           passivate();
    }

    public void deltcon(double e,message x)
    {
        deltint();
        deltext(0,x);
    }

    public message out()
    {
        if (phaseIs("busy"))
        {
            message  m = new message();
            content con = makeContent("out",currCus);
            m.add(con);
            return m;
        }

        else return new message();
    }

    public void process(String status)
    {
        if(status == "done")
        {
            totalServed++;
            currType = currCus.type();
            if(currType == "gold")
            {
                totalGoldCreated++;
                totalGold++;
                totalCard++;
                double waitTime = currCus.getQExitTime() - currCus.getQEntryTime();
                if (waitTime > 1.5) //gold:90 seconds wait time: 98% should be below
                    waitGCExceeded++;
            }

            else if(currType == "silver")
            {
                totalSilvCreated++;
                totalSilver++;
                totalCard++;
                double waitTime = currCus.getQExitTime() - currCus.getQEntryTime();
                if (waitTime > 3)  //silv:3 minutes wait time: 95% should be below
                    waitSCExceeded++;
            }

            else if(currType == "regular")
            {
                totalRegCreated++;
                totalRegular++;
                double waitTime = currCus.getQExitTime() - currCus.getQEntryTime();
                if (waitTime > 15)  //reg:15 minutes wait time: 85% should be below
                    waitRCExceeded++;
            }
        }

        else if(status == "busySig")
        {
            totalBusy++;
            currType = currCus.type();
            if(currType == "gold")
            {
                totalGoldCreated++;
                totalGoldBusy++;
                totalCardBusy++;
            }

            else if(currType == "silver")
            {
                totalSilvCreated++;
                totalSilvBusy++;
                totalCardBusy++;
            }

            else if(currType == "regular")
            {
                totalRegCreated++;
                totalRegBusy++;
            }
        }

        else if(status == "late")
        {
            totalLate++;
            currType = currCus.type();
            if(currType == "gold")
            {
                totalGoldCreated++;
                waitGCExceeded++;
            }

            else if(currType == "silver")
            {
                totalSilvCreated++;
                waitSCExceeded++;
            }

            else if(currType == "regular")
            {
                totalRegCreated++;
                waitRCExceeded++;
            }
        }
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

    public void finalStats()
    {
        percGCWaitExc = (waitGCExceeded/totalGoldCreated)*100;
        percSCWaitExc = (waitSCExceeded/totalSilvCreated)*100;
        percRCWaitExc = (waitRCExceeded/totalRegCreated)*100;

        System.out.println("Total Gold Customers Created: " + totalGoldCreated);
        System.out.println("Total Silver Customers Created: " + totalSilvCreated);
        System.out.println("Total Regular Customers Created: " + totalRegCreated);
        System.out.println("Total Gold Served: " + totalGold);
        System.out.println("Total Silver Served: " + totalSilver);
        System.out.println("Total Regular Served: " + totalRegular);
        System.out.println("Total Gold Busy Signal: " + totalGoldBusy + " = " + ((totalGoldBusy/totalGoldCreated)*100) + "%");
        System.out.println("Total Silver Busy Signal: " + totalSilvBusy + " = " + ((totalSilvBusy/totalSilvCreated)*100) + "%");
        System.out.println("Total CardHold Busy Signal: " + totalCardBusy + " = " + ((totalCardBusy/(totalSilvCreated+totalGoldCreated))*100) + "%");
        System.out.println("Total Regular Busy Signal: " + totalRegBusy + " = " + ((totalRegBusy/totalRegCreated)*100) + "%");
        System.out.println("Total Gold Wait Exceeded 1.5 mins: " + waitGCExceeded + " = " + percGCWaitExc + "%");
        System.out.println("Total Silver Wait Exceeded 3 mins: " + waitSCExceeded + " = " + percSCWaitExc + "%");
        System.out.println("Total Regular Wait Exceeded 15 mins: " + waitGCExceeded + " = " + percGCWaitExc + "%");
    }
}