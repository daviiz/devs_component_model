package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class FFTButterfly extends realDevs
{
  protected double aR, aI, bR, bI;
  protected int N, k;

  public FFTButterfly()
  {
    this("FFT Butterfly");
  }
  public FFTButterfly(String name)
  {
    super(name);

    addInport("aR");
    addInport("aI");
    addInport("bR");
    addInport("bI");
    addInport("N");
    addInport("k");
    addInport("start");

    addOutport("AR");
    addOutport("AI");
    addOutport("BR");
    addOutport("BI");
    addOutport("done");

    addTestInput("start", new entity("start"));
    addTestInput("aR", new doubleEnt(1.0));
    addTestInput("aI", new doubleEnt(1.0));
    addTestInput("bR", new doubleEnt(1.0));
    addTestInput("bI", new doubleEnt(1.0));
    addTestInput("N", new doubleEnt(16));
    addTestInput("k", new doubleEnt(1.0));
  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    Continue(e);

    if (somethingOnPort(x, "start"))
    {
      aR = getRealValueOnPort(x, "aR");
      aI = getRealValueOnPort(x, "aI");
      bR = getRealValueOnPort(x, "bR");
      bI = getRealValueOnPort(x, "bI");
      N = getIntValueOnPort(x, "N");
      k = getIntValueOnPort(x, "k");

      /*aR = 1.0;
      aI = 1.0;
      bR = 1.0;
      bI = 1.0;
      N = 16;
      k = 1;*/

      holdIn("calculate", 1);
    }
  }

  public double[] Butterfly(double aR, double aI, double bR, double bI, int N, int k)
  {
    double O[] = new double[4];
    double phi, a, b;

    phi = 2*Math.PI*k/N;

    O[0] = aR + bR;
    O[1] = aI + bI;

    a = aR - bR;
    b = aI - bI;

    O[2] = a*Math.cos(phi) + b*Math.sin(phi);
    O[3] = b*Math.cos(phi) - a*Math.sin(phi);

    return O;
  }

  public void deltint()
  {
    if (phaseIs("calculate"))
    {
      double[] rArray;

      rArray = Butterfly(aR, aI, bR, bI, N, k);

      aR = rArray[0];
      aI = rArray[1];
      bR = rArray[2];
      bI = rArray[3];

      holdIn("done", 0);
    }
    else if (phaseIs("done"))
    {
      passivateIn("idle");
    }
  }
  public message out()
 {
   content con_0 = new content("", new entity(""));
   content con_1 = new content("", new entity(""));
   content con_2 = new content("", new entity(""));
   content con_3 = new content("", new entity(""));
   content con_4 = new content("", new entity(""));

   message op = new message();
   if (phaseIs("done"))
   {
     con_0=makeContent("AR", new doubleEnt(aR));
     con_1=makeContent("AI", new doubleEnt(aI));
     con_2=makeContent("BR", new doubleEnt(bR));
     con_3=makeContent("BI", new doubleEnt(bI));
     con_4=makeContent("done", new entity("done"));
   }

   op.add(con_0);
   op.add(con_1);
   op.add(con_2);
   op.add(con_3);
   op.add(con_4);

   return op;
 }


}