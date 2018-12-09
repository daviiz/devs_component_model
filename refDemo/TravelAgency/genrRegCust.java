/*  Name: Vijay Ramamurthy / Micah Lesmeister
 *  ECE 575 Project: Travel Agency Call Center Simulation
 *  Date: 12-15-03
 */

package refDemo.TravelAgency;

import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import util.rand;
import view.modeling.ViewableAtomic;

public class genrRegCust extends ViewableAtomic
{
    protected double int_arr_time, clock, currMean;
    protected int count, totalRegular;
    protected rand r;

    public genrRegCust()
    {
        super("genrRegCust");
        addInport("stop");
        addInport("start");
        addOutport("out");
        addTestInput("start",new entity("start"));
        addTestInput("stop",new entity("stop"));
        r = new rand(1);
        initialize();
    }

    public void initialize()
    {
        if (r != null)// must be left in
            holdIn("busy",int_arr_time);

        count = 0;
        clock = 0;
        int_arr_time = (double)(60.0/87) *3;  //arrival rate for 1st hour
        super.initialize();
    }

    public void  deltext(double e,message x)
    {
        Continue(e);

        for (int i=0; i< x.size();i++)
            if (messageOnPort(x,"start",i))
            {
                holdIn("busy",int_arr_time);
            }

        for (int i=0; i< x.size();i++)
            if (messageOnPort(x,"stop",i))
                passivate();
    }

    public void  deltint( )
    {
        clock = clock + sigma;

        if(clock > 0 && clock <= 60)
            int_arr_time = (double)(60.0/87) *3;
        else if(clock > 60 && clock <= 120)
            int_arr_time = (double)(60.0/165) *3;
        else if(clock > 120 && clock <= 180)
            int_arr_time = (double)(60.0/236) *3;
        else if(clock > 180 && clock <= 240)
            int_arr_time = (double)(60.0/323) *3;
        else if(clock > 240 && clock <= 300)
            int_arr_time = (double)(60.0/277) *3;
        else if(clock > 300 && clock <= 360)
            int_arr_time = (double)(60.0/440) *3;
        else if(clock > 360 && clock <= 420)
            int_arr_time = (double)(60.0/269) *3;
        else if(clock > 420 && clock <= 480)
            int_arr_time = (double)(60.0/342) *3;
        else if(clock > 480 && clock <= 540)
            int_arr_time = (double)(60.0/175) *3;
        else if(clock > 540 && clock <= 600)
            int_arr_time = (double)(60.0/273) *3;
        else if(clock > 600 && clock <= 660)
            int_arr_time = (double)(60.0/115) *3;
        else if(clock > 660 && clock <= 720)
            int_arr_time = (double)(60.0/56) *3;
        else if(clock > 720)
        {
            holdIn("closed",INFINITY);
        }

        if(phaseIs("busy"))
        {
            count = count +1;
            holdIn("busy",int_arr_time);
        }
    }

    public message  out( )
    {
        String type, transactionType;
        double waitTolerance, serviceTime, afterCallTime;
        double rnum;

        type = "";                //initialize parameters
        transactionType = "";
        waitTolerance = 0;
        serviceTime = 0;
        afterCallTime = 0;

        //generate customer paramters randomly and according to
        //required proportions

        rnum = r.uniform(0,1);

        type = "regular";   //set customer type
        totalRegular++;

        //transactiontype:info,reservations,changes:16%,76%,8%

        if(rnum <= 0.76)
        {
            transactionType = "reservations";
            serviceTime = r.uniform(2.25, 4.0);   //betw 2.95 and 8.6
            afterCallTime = r.uniform(0.5, 0.8);
        }

        else if(rnum > 0.76 && rnum <= 0.92)
        {
            transactionType = "info";
            serviceTime = r.uniform(1.5, 3.2);
            afterCallTime = r.uniform(0.05, 0.10);
        }

        else if(rnum > 0.92 && rnum <= 1.0)
        {
            transactionType = "changes";
            serviceTime = r.uniform(1.3, 3.0); //betw 1.9 and 5.8
            afterCallTime = r.uniform(0.4, 0.6);
        }

        //same for all card holders

        waitTolerance = r.uniform(12,30);

        //create new customer w/generated parameters

        Customer newc=new Customer(type,transactionType,waitTolerance,serviceTime,afterCallTime);

        message  m = new message();
        content con = makeContent("out",newc);
        m.add(con);
        return m;
    }

    public String getTooltipText()
    {
        return super.getTooltipText() +"\n"+"totalRegular :" + totalRegular;
    }

}

