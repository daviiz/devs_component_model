package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class ComplexMultiplier extends realDevs
{
  protected double aR, aI, bR, bI, AR, AI;

  public ComplexMultiplier()
  {
    this("Complex Multiplier");
  }

  public ComplexMultiplier(String name)
  {
    super(name);

    addInport("aR");
    addInport("aI");
    addInport("bR");
    addInport("bI");

    addOutport("AR");
    addOutport("AI");
    addOutport("done");
  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    if (somethingOnPort(x, "aR"))
      aR = getRealValueOnPort(x, "aR");
    if (somethingOnPort(x, "aI"))
      aI = getRealValueOnPort(x, "aI");
    if (somethingOnPort(x, "bR"))
      bR = getRealValueOnPort(x, "bR");
    if (somethingOnPort(x, "bI"))
      bI = getRealValueOnPort(x, "bI");
      holdIn("multiply", 0);
  }

  public void deltint()
  {
    if (phaseIs("multiply"))
    {
      AR = (aR * bR) - (aI * bI);
      AI = (aR * bI) + (aI * bR);
      holdIn("outdata", 0);
    }
    else if (phaseIs("outdata"))
    {
      holdIn("idle", INFINITY);
    }
  }

  public message out()
  {
    content con_0 = new content("", new entity(""));
    content con_1 = new content("", new entity(""));
    content con_2 = new content("", new entity(""));

    message op = new message();

    if (phaseIs("outdata"))
    {
      //System.out.println(AR);
      //System.out.println(AI);
      con_0 = new content("AR", new doubleEnt(AR));
      con_1 = new content("AI", new doubleEnt(AI));
      con_2 = new content("done", new entity("done"));
    }

    op.add(con_0);
    op.add(con_1);
    op.add(con_2);

    return op;
  }

}