/*  Name: Vijay Ramamurthy / Micah Lesmeister
 *  ECE 575 Project: Travel Agency Call Center Simulation
 *  Date: 12-15-03
 */

package refDemo.TravelAgency;
import GenCol.entity;

public class Customer extends entity
{
    public String type, transactionType;
    public double waitTolerance, serviceTime, afterCallTime;
    public double processing_time, totalProcTime;
    public double qEntryTime, qExitTime;

    public Customer(String typ,String tType,double wTol, double sTime,double aCT)
    {
        super("cust");
        type = typ;
        transactionType = tType;
        waitTolerance = wTol;
        serviceTime = sTime;
        afterCallTime = aCT;
        totalProcTime = serviceTime + afterCallTime;
        qEntryTime = 0;
        qExitTime = 0;
    }

    public void update(double e)
    {
        totalProcTime = totalProcTime - e;
    }

    public String type()
    {
        return type;
    }

    public String transactionType()
    {
        return transactionType;
    }

    public double waitTolerance()
    {
        return waitTolerance;
    }

    public double serviceTime()
    {
        return serviceTime;
    }

    public double afterCallTime()
    {
        return afterCallTime;
    }

    public void setQEntryTime(double arr)
    {
        qEntryTime = arr;
    }

    public void setQExitTime(double ex)
    {
        qExitTime = ex;
    }

    public double getQEntryTime()
    {
        return qEntryTime;
    }

    public double getQExitTime()
    {
        return qExitTime;
    }

    public double totalProcTime()
    {
        return totalProcTime;
    }

    public void print()
    {
        System.out.println("cus: "+type + " transType: " + transactionType + " waitTol: " + waitTolerance + " serTime: " + serviceTime +
              " afterTime: " + afterCallTime + " totalProcTime: " + totalProcTime);
    }

}













