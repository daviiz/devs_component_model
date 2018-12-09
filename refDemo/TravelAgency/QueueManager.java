/*  Name: Vijay Ramamurthy / Micah Lesmeister
 *  ECE 575 Project: Travel Agency Call Center Simulation
 *  Date: 12-15-03
 */

package refDemo.TravelAgency;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class QueueManager extends ViewableAtomic
{
    protected double processing_time;
    protected boolean goldQEmpty, silvQEmpty, regQEmpty;
    protected boolean goldP1Idle, silvP1Idle, silvP2Idle, silvP3Idle, regP1Idle, regP2Idle, regP3Idle, regP4Idle, regP5Idle, regP6Idle, regP7Idle;

    public QueueManager()
    {
        this("QManager", 0.0);
    }

    public QueueManager(String  name, double Processing_time)
    {
        super("QManager");
        addInport("goldQStat");
        addInport("silvQStat");
        addInport("regQStat");
        addInport("goldPIdle");
        addInport("silvPIdle");
        addInport("regPIdle");
        addOutport("goldQCmd");
        addOutport("silvQCmd");
        addOutport("regQCmd");
        processing_time = Processing_time;
        initialize();
    }

    public void initialize()
    {
        phase = "passive";
        sigma = INFINITY;
        goldQEmpty = true;
        silvQEmpty = true;
        regQEmpty = true;
        goldP1Idle = true;
        silvP1Idle = true;
        silvP2Idle = true;
        silvP3Idle = true;
        regP1Idle = true;
        regP2Idle = true;
        regP3Idle = true;
        regP4Idle = true;
        regP5Idle = true;
        regP6Idle = true;
        regP7Idle = true;
        super.initialize();
    }

    public void deltext(double e,message x)
    {
        Continue(e);

        if(somethingOnPort(x,"goldQStat"))
        {
            entity temp = getEntityOnPort(x,"goldQStat");
            if(temp.getName() == "empty")
            	goldQEmpty = true;
            else if(temp.getName() == "custWait")
            {
                goldQEmpty = false;
                holdIn("busy",processing_time);
            }
        }

        if(somethingOnPort(x,"silvQStat"))
        {
            entity temp = getEntityOnPort(x,"silvQStat");
            if(temp.getName() == "empty")
            	silvQEmpty = true;
            else if(temp.getName() == "custWait")
            {
            	silvQEmpty = false;
                holdIn("busy",processing_time);
            }
        }

        if(somethingOnPort(x,"regQStat"))
        {
            entity temp = getEntityOnPort(x,"regQStat");
            if(temp.getName() == "empty")
            	regQEmpty = true;
            else if(temp.getName() == "custWait")
            {
                regQEmpty = false;
                holdIn("busy",processing_time);
            }
        }

		    if(somethingOnPort(x,"goldPIdle"))
		    {
            entity temp = getEntityOnPort(x,"goldPIdle");
            if(temp.getName() == "goldP1")
            {
                goldP1Idle = true;
                holdIn("busy",processing_time);
            }
        }

        if(somethingOnPort(x,"silvPIdle"))
        {
            entity temp = getEntityOnPort(x,"silvPIdle");
            if(temp.getName() == "silvP1")
            {
                silvP1Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "silvP2")
            {
                silvP2Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "silvP3")
            {
                silvP3Idle = true;
                holdIn("busy",processing_time);
            }
        }

        if(somethingOnPort(x,"regPIdle"))
        {
            entity temp = getEntityOnPort(x,"regPIdle");
            if(temp.getName() == "regP1")
            {
                regP1Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "regP2")
            {
                regP2Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "regP3")
            {
                regP3Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "regP4")
            {
                regP4Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "regP5")
            {
                regP5Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "regP6")
            {
                regP6Idle = true;
                holdIn("busy",processing_time);
            }

            else if(temp.getName() == "regP7")
            {
                regP7Idle = true;
                holdIn("busy",processing_time);
            }
        }
    }

    public void  deltint( )
    {
      if(goldQEmpty == false || silvQEmpty == false || regQEmpty == false || goldP1Idle == true || silvP1Idle == true || silvP2Idle == true || silvP3Idle == true || regP1Idle == true || regP2Idle == true || regP3Idle == true || regP4Idle == true || regP5Idle == true || regP6Idle == true || regP7Idle == true)
      {
          holdIn("busy",processing_time);
      }

      passivate();

    }

    public void deltcon(double e,message x)
    {
        deltint();
        deltext(0,x);
    }

    public message out()
    {

        if(goldQEmpty == false && goldP1Idle == true)
        {
            message m = new message();
            m.add(makeContent("goldQCmd",new entity("goldP1")));
            goldP1Idle = false;
            holdIn("active",processing_time);
            return m;
        }

        if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == true)
        {
            message m = new message();
            m.add(makeContent("goldQCmd",new entity("silvP1")));
            silvP1Idle = false;
            holdIn("active",processing_time);
            return m;
        }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == true )
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("silvP2")));
        silvP2Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == true )
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("silvP3")));
        silvP3Idle = false;
        holdIn("active",processing_time);
        return m;
    }

		if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == true)
		{
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP1")));
        regP1Idle = false;
        holdIn("active",processing_time);
        return m;
		}

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == true)
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP2")));
        regP2Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == true)
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP3")));
        regP3Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == true)
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP4")));
        regP4Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == true)
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP5")));
        regP5Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == false && regP6Idle == true)
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP6")));
        regP6Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(goldQEmpty == false && goldP1Idle == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == false && regP6Idle == false && regP7Idle == true)
    {
        message m = new message();
        m.add(makeContent("goldQCmd",new entity("regP7")));
        regP7Idle = false;
        holdIn("active",processing_time);
        return m;
    }

		if(silvQEmpty == false && silvP1Idle == true)
		{
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("silvP1")));
        silvP1Idle = false;
        holdIn("active",processing_time);
        return m;
		}

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("silvP2")));
        silvP2Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("silvP3")));
        silvP3Idle = false;
        holdIn("active",processing_time);
        return m;
    }

		if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == true)
		{
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP1")));
        regP1Idle = false;
        holdIn("active",processing_time);
        return m;
		}

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP2")));
        regP2Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP3")));
        regP3Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP4")));
        regP4Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP5")));
        regP5Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == false && regP6Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP6")));
        regP6Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(silvQEmpty == false && silvP1Idle == false && silvP2Idle == false && silvP3Idle == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == false && regP6Idle == false && regP7Idle == true)
    {
        message m = new message();
        m.add(makeContent("silvQCmd",new entity("regP7")));
        regP7Idle = false;
        holdIn("active",processing_time);
        return m;
    }

		if(regQEmpty == false && regP1Idle == true)
		{
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP1")));
        regP1Idle = false;
        holdIn("active",processing_time);
        return m;
		}

    if(regQEmpty == false && regP1Idle == false && regP2Idle == true)
    {
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP2")));
        regP2Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(regQEmpty == false && regP1Idle == false && regP2Idle == false && regP3Idle == true)
    {
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP3")));
        regP3Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(regQEmpty == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == true)
    {
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP4")));
        regP4Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(regQEmpty == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == true)
    {
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP5")));
        regP5Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(regQEmpty == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == false && regP6Idle == true)
    {
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP6")));
        regP6Idle = false;
        holdIn("active",processing_time);
        return m;
    }

    if(regQEmpty == false && regP1Idle == false && regP2Idle == false && regP3Idle == false && regP4Idle == false && regP5Idle == false && regP6Idle == false && regP7Idle == true)
    {
        message m = new message();
        m.add(makeContent("regQCmd",new entity("regP7")));
        regP7Idle = false;
        holdIn("active",processing_time);
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