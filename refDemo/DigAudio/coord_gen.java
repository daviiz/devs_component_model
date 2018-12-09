package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class coord_gen extends realDevs
{
  public coord_gen()
  {
    this("Coord Gen");
  }

  public coord_gen(String name)
  {
    super(name);

    addInport("start");
    addInport("in");

    addOutport("ostart");
    addOutport("oload");
    addOutport("filter_type");
    addOutport("window_type");
    addOutport("fc1");
    addOutport("fc1_1");
    addOutport("fc1_2");
    addOutport("fc2");
    addOutport("fc2_1");
    addOutport("fc2_2");
    addOutport("fs");

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
      holdIn("start", 0);
    }
  }

  public void deltint()
  {
    if (phaseIs("start"))
    {
      holdIn("filter1", 1);
    }
    else if (phaseIs("filter1"))
    {
      holdIn("filter2", 1);
    }
    else if (phaseIs("filter2"))
    {
      holdIn("filter3", 1);
    }
    else if (phaseIs("filter3"))
    {
      holdIn("filter4", 1);
    }
    else if (phaseIs("filter4"))
    {
      holdIn("idle", INFINITY);
    }
  }

  public message out()
  {
    content con = new content("", new entity(""));
    content fs = new content("", new entity(""));
    content fc1 = new content("", new entity(""));
    content fc1_1 = new content("", new entity(""));
    content fc1_2 = new content("", new entity(""));
    content fc2 = new content("", new entity(""));
    content fc2_1 = new content("", new entity(""));
    content fc2_2 = new content("", new entity(""));
    content filter_type = new content("", new entity(""));
    content window_type = new content("", new entity(""));

    message op = new message();
    if (phaseIs("start"))
    {
      con=makeContent("ostart", new entity("start"));
    }
    else if (phaseIs("filter1"))
    {
      con=makeContent("oload", new entity("load"));
      fs = new content("fs", new doubleEnt(5000));
      fc1 = new content("fc1", new doubleEnt(500));
      fc1_1 = new content("fc1_1", new doubleEnt(400));
      fc1_2 = new content("fc1_2", new doubleEnt(600));
      fc2 = new content("fc2", new doubleEnt(0));
      fc2_1 = new content("fc2_1", new doubleEnt(0));
      fc2_2 = new content("fc2_2", new doubleEnt(0));
      filter_type = new content("filter_type", new intEnt(1));
      window_type = new content("window_type", new intEnt(1));
      op.add(fs);
      op.add(fc1);
      op.add(fc1_1);
      op.add(fc1_2);
      op.add(fc2);
      op.add(fc2_1);
      op.add(fc2_2);
      op.add(filter_type);
      op.add(window_type);
    }
    else if (phaseIs("filter2"))
    {
      con=makeContent("oload", new entity("load"));

      fc1 = new content("fc1", new doubleEnt(2000));
      fc1_1 = new content("fc1_1", new doubleEnt(1900));
      fc1_2 = new content("fc1_2", new doubleEnt(2100));
      fc2 = new content("fc2", new doubleEnt(0));
      fc2_1 = new content("fc2_1", new doubleEnt(0));
      fc2_2 = new content("fc2_2", new doubleEnt(0));
      filter_type = new content("filter_type", new intEnt(2));
      window_type = new content("window_type", new intEnt(1));
      op.add(fc1);
      op.add(fc1_1);
      op.add(fc1_2);
      op.add(fc2);
      op.add(fc2_1);
      op.add(fc2_2);
      op.add(filter_type);
      op.add(window_type);

    }
    else if (phaseIs("filter3"))
    {
      con=makeContent("oload", new entity("load"));

      fc1 = new content("fc1", new doubleEnt(1000));
      fc1_1 = new content("fc1_1", new doubleEnt(950));
      fc1_2 = new content("fc1_2", new doubleEnt(1050));
      fc2 = new content("fc2", new doubleEnt(2000));
      fc2_1 = new content("fc2_1", new doubleEnt(1950));
      fc2_2 = new content("fc2_2", new doubleEnt(2050));
      filter_type = new content("filter_type", new intEnt(3));
      window_type = new content("window_type", new intEnt(1));
      op.add(fc1);
      op.add(fc1_1);
      op.add(fc1_2);
      op.add(fc2);
      op.add(fc2_1);
      op.add(fc2_2);
      op.add(filter_type);
      op.add(window_type);

    }
    else if (phaseIs("filter4"))
    {
      con=makeContent("oload", new entity("load"));

      fc1 = new content("fc1", new doubleEnt(1000));
      fc1_1 = new content("fc1_1", new doubleEnt(950));
      fc1_2 = new content("fc1_2", new doubleEnt(1050));
      fc2 = new content("fc2", new doubleEnt(1200));
      fc2_1 = new content("fc2_1", new doubleEnt(1150));
      fc2_2 = new content("fc2_2", new doubleEnt(1250));
      filter_type = new content("filter_type", new intEnt(4));
      window_type = new content("window_type", new intEnt(1));
      op.add(fc1);
      op.add(fc1_1);
      op.add(fc1_2);
      op.add(fc2);
      op.add(fc2_1);
      op.add(fc2_2);
      op.add(filter_type);
      op.add(window_type);

    }

    op.add(con);

    return op;
  }

}