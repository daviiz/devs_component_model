package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class inputUnitTest extends realDevs
{
  protected double data;
  protected int M, counter;
  protected int F;

  public inputUnitTest()
  {
    this("Input Unit Tester");
  }
  public inputUnitTest(String name)
  {
    super(name);

    addInport("start");
    addOutport("data");
    addOutport("M");

    addTestInput("start", new entity("start"));
  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    if (somethingOnPort(x, "start"))
    {

      M = 81;
      counter = 0;
      //data = 1.0;
      F = 100;//100;
      data = Math.sin(2*Math.PI*F*counter/5000);
      holdIn("generate", 1);
    }
  }

  public void deltint()
  {
    if (phaseIs("generate"))
    {
      counter++;
      //data = 1.0;

      data = Math.sin(2*Math.PI*F*counter/5000);
      holdIn("generate", 1);
    }
  }

  public message out()
 {
   content con_R = new content("", new entity(""));

   message op = new message();
   if (phaseIs("generate"))
   {
     if (counter == 0)
     {
       content con = new content("M", new intEnt(M));
       op.add(con);
     }
     con_R=makeContent("data", new doubleEnt(data));
   }

   op.add(con_R);

   return op;
 }

}