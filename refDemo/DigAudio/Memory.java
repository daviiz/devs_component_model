package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class Memory extends realDevs
{
  protected double[] dataR;
  protected double[] dataI;
  protected double wrR, wrI;
  protected int row;

  public Memory()
  {
    this("Memory", 128);
  }

  public void reset()
  {
    for (int i=0; i<dataR.length; i++)
    {
      dataR[i] = 0.0;
      dataI[i] = 0.0;
    }
  }
  public Memory(String name, int memSize)
  {
    super(name);

    dataR = new double[memSize];
    dataI = new double[memSize];

    this.reset();

    addInport("N");
    addInport("dataR");
    addInport("dataI");
    addInport("rd");
    addInport("row");
    addInport("wr");
    addInport("enable");

    addOutport("odataR");
    addOutport("odataI");

    addTestInput("rd", new entity("read"));
    addTestInput("wr", new entity("write"));
  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    if (somethingOnPort(x, "enable"))
    {
      int en = getIntValueOnPort(x, "enable");
      if (en == 1)
        passivateIn("ready");
      else
        passivateIn("idle");
    }
    if (phaseIs("idle"))
      return;

    if(somethingOnPort(x, "N"))
    {
      int memSize = getIntValueOnPort(x, "N");
      dataR = new double[memSize];
      dataI = new double[memSize];
    }
    if (somethingOnPort(x, "rd"))
    {
      //row = 1;
      row = getIntValueOnPort(x, "row");
      holdIn("read", 1);
    }
    else if (somethingOnPort(x, "wr"))
    {
      //row = 1;
      //wrR = 1;
      //wrI = 1;
      row = getIntValueOnPort(x, "row");
      wrR = getRealValueOnPort(x, "dataR");
      wrI = getRealValueOnPort(x, "dataI");
      holdIn("write", 1);
    }
  }

  public void deltint()
  {
    if (phaseIs("write"))
    {
      dataR[row] = wrR;
      dataI[row] = wrI;
    }
    passivateIn("ready");
  }

public message out()
 {
   content con_R = new content("", new entity(""));
   content con_I = new content("", new entity(""));
   message op = new message();
   if (phaseIs("read"))
   {
     con_R=makeContent("odataR", new doubleEnt(dataR[row]));
     con_I=makeContent("odataI", new doubleEnt(dataI[row]));
   }

   op.add(con_R);
   op.add(con_I);

   return op;
 }

}