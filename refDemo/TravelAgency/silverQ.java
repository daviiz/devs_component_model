/*  Name: Vijay Ramamurthy / Micah Lesmeister
 *  ECE 575 Project: Travel Agency Call Center Simulation
 *  Date: 12-15-03
 */

package refDemo.TravelAgency;
import ComponentMod.proc;
import GenCol.Queue;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;

public class silverQ extends proc
{
    protected Queue q;
    protected Customer cuurCus;
    protected entity currJob;
    protected int numSilver;
    protected boolean informed, emptyNotice;
    protected String destProc;
    protected double clock;

    public silverQ(String  name,double  Processing_time)
    {
        super(name, Processing_time);
        q = new Queue();
    }

    public silverQ()
    {
        super("silverQ",0);
        addOutport("silvP1");
        addOutport("silvP2");
        addOutport("silvP3");
        addOutport("regP1");
        addOutport("regP2");
        addOutport("regP3");
        addOutport("regP4");
        addOutport("regP5");
        addOutport("regP6");
        addOutport("regP7");
        addOutport("status");
        addOutport("dequeued");
        addInport("cmd");
        initialize();
    }

    public void initialize()
    {
        q = new Queue();
        numSilver = 0;
        informed = false;
        emptyNotice = false;
        destProc = "";
        clock = 0;
        super.initialize();
    }

    public void  deltext(double e, message x)
    {
        clock = clock + e;
        Continue(e);

        if (somethingOnPort(x,"in"))
        {
            currJob = getEntityOnPort(x,"in");
            q.add(currJob);
            Customer currC = (Customer)currJob;
            currC.setQEntryTime(clock);
            numSilver++;
            emptyNotice = false;
            currJob = (entity)q.first();  // this makes sure the processed job is the one at the front
        }

        if (numSilver > 0 && informed == false)
        {
            holdIn("inform",processing_time);
        }

        if (somethingOnPort(x,"cmd") && informed == true)
        {
            entity temp = getEntityOnPort(x,"cmd");
            destProc = temp.getName();
            holdIn("dequeue",processing_time);
        }
    }

    public void  deltint( )
    {
        if(numSilver == 0 && emptyNotice == false)
            holdIn("empty",processing_time);

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
        if (phaseIs("inform"))
        {
            message m = new message();
            content con = makeContent("status", new entity("custWait"));
            m.add(con);
            informed = true;
            return m;
        }

        if (phaseIs("dequeue"))
        {
            message m = new message();
            content con1 = makeContent(destProc,currJob);
            Customer currC = (Customer)currJob;
            currC.setQExitTime(clock);
            m.add(con1);
            q.remove();
            numSilver--;
            content con2 = makeContent("dequeued",new entity("SCdqd"));
            m.add(con2);
            emptyNotice = false;
            return m;
        }

        if (phaseIs("empty"))
        {
            message m = new message();
            content con = makeContent("status", new entity("empty"));
            m.add(con);
            emptyNotice = true;
            informed = false;
            return m;
        }

        else return new message();
    }

    public entity getEntityOnPort(message x,String port)
    {
        for (int i=0; i< x.getLength();i++)
            if (messageOnPort(x,port,i))
                return x.getValOnPort(port,i);

        return null;
    }

    public boolean somethingOnPort(message x,String port)
    {
        for (int i=0; i< x.getLength();i++)
        {
            if (messageOnPort(x,port,i))
            {
                return true;
            }
        }

        return false;
    }

    public String getTooltipText()
    {
        return super.getTooltipText() +"\n"+"silvEnqd :" + numSilver;
    }
}


