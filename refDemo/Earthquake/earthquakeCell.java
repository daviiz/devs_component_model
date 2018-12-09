package refDemo.Earthquake;

import java.awt.Color;

import GenCol.Pair;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;
import model.plots.DrawCellEntity;



public class earthquakeCell
    extends TwoDimCell {

  protected double k = 1, alpha = 1 / (4 + k), threshold = 1, force, forcex,
      forcey, initialforce;
  protected Pair inpairn, inpairs, inpairw, inpaire;
  protected int crack = 1, change = 0, inns = 0, inew = 0, xPos, yPos, Wxp, Exp,
      Nyp, Syp, west = 0, east = 0, south = 0, north = 0, inpn = 0, inps = 0,
      inpw = 0, inpe = 0;
  public earthquakeCell() {
    this(0, 0, 0.0);
  }

  public earthquakeCell(int xcoord, int ycoord, double initialEnergy) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));
    addOutport("outEnergy");
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.xPos = xcoord;
    this.yPos = ycoord;
    this.initialforce = initialEnergy;
  }

  public void initialize() {
    super.initialize();
    holdIn("output", 0);
  }

  public void update(double elapsed) {
    double i, force1;
    if (inns == 1) {
      forcey = (Nyp + Syp) / 2 - ycoord;
    }
    else {
      forcey = 0;
//forcex = initialforce+(elapsed + Wxp+Exp-(2+k)*xcoord);
    }
    if (inew == 1 && (east == 1 || west == 1)) {
      forcex = initialforce + elapsed + (Wxp + Exp) / 2 - (k + 1) * xcoord;
    }
    else {
      forcex = initialforce + elapsed;
    }
    initialforce = 0;
    force = (forcex + forcey) * alpha;
    System.out.println("force UPDATE output =" + force + "and inew.." + inew +
                       "and inns.." + inns + " (xpos,ypos)....(" + xPos + "," +
                       yPos + ")..........and (nxpos,nypos)....(" + xcoord +
                       "," + ycoord + ")and Nyp,Syp,Exp,Wxp" + Nyp + "," + Syp +
                       "," + Exp + "," + Wxp);
    if (Math.abs(force) >= threshold) { //relax to myPos making force = 0;
      change = 1;
      if (Math.abs(force) >= 2 * threshold) {
        crack = 1;
      }
      else {
        crack = 0;
      }
      if (north == 1 || south == 1) {
        ycoord = (int) ( (Nyp + Syp) / 2);
        north = 0;
        south = 0;
      }
      if (east == 1 || west == 1) {
        xcoord = (int) ( (elapsed + (Wxp + Exp) / 2) / (1 + k));
        east = 0;
        west = 0;
      }
      else {
        xcoord = xcoord + (int) elapsed;
      }
      System.out.println("force in output =" + force + "and xcoord,ycoord=(" +
                         xcoord + "," + ycoord + ")and Nyp,Syp,Exp,Wxp" + Nyp +
                         "," + Syp + "," + Exp + "," + Wxp);
      holdIn("output", 0);
    }
    else {
      change = 0;
      i = elapsed;
      force1 = force;
//forcey=Nyp+Syp-2*yPos; + Wxp+Exp-(2+k)*xcoord;
      if (inns == 1) {
        forcey = (Nyp + Syp) / 2 - ycoord;
      }
      else {
        forcey = 0;
      }
      while (Math.abs(force1) < threshold) {
        i = i + 0.01;
        if (inew == 1) {
          forcex = i + (Wxp + Exp) / 2 - (k + 1) * xcoord;
        }
        else {
          forcex = i;
        }
        force1 = alpha * (forcey + forcex);
      }
      System.out.println("force in active =" + force1 + "and i = " + i +
                         "and Nyp,Syp,Exp,Wxp" + Nyp + "," + Syp + "," + Exp +
                         "," + Wxp);
      holdIn("active", i);
    }
  }

  public void deltext(double e, message x) {
    double m;
    Continue(e);
    for (int i = 0; i < x.getLength(); i++) {
      if (somethingOnPort(x, "start")) {
        holdIn("active", 0);
      }
      else if (somethingOnPort(x, "stop")) {
        passivate();
      }
      else {
        // Get notification message from neighbor cells
        if (somethingOnPort(x, "inN")) {
          inpairn = (Pair) x.getValOnPort("inN", i);
          if (inpairn != null) {
            System.out.println("indeltextN" + " " + inpairn);
            Integer my_int4 = (Integer) inpairn.getValue();
            Nyp = my_int4.intValue();
            System.out.println("Nyp" + Nyp);
            inns = 1;
            north = 1;
            //update(e);
          }
        }
        else if (somethingOnPort(x, "inE")) {
          inpaire = (Pair) x.getValOnPort("inE", i);
          if (inpaire != null) {
            inpe = 1;
            System.out.println("indeltextE" + " " + inpaire);
            Integer my_int2 = (Integer) inpaire.getKey();
            Exp = my_int2.intValue();
            System.out.println("Exp" + Exp);
            inew = 1;
            east = 1;
            //update(e);
          }
        }
        else if (somethingOnPort(x, "inS")) {
          inpairs = (Pair) x.getValOnPort("inS", i);
          if (inpairs != null) {
            System.out.println("indeltextS" + " " + inpairs);
            Integer my_int1 = (Integer) inpairs.getValue();
            Syp = my_int1.intValue();
            System.out.println("Syp" + Syp);
            inns = 1;
            south = 1;
            //update(e);
          }
        }
        else if (somethingOnPort(x, "inW")) {
          inpairw = (Pair) x.getValOnPort("inW", i);
          if (inpairw != null) {
            Integer my_int3 = (Integer) inpairw.getKey();
            Wxp = my_int3.intValue();
            System.out.println("Wxp" + Wxp);
            System.out.println("indeltextW" + " " + inpairw);
            inew = 1;
            west = 1;
            //update(e);
          }
        }
        if (inns == 1 || inew == 1) {
          update(e);
        }
      }
    }
  }

  public void deltint() {
    numTransitions++;
    if (phaseIs("active")) {
      update(sigma);
    }
    else if (phaseIs("output")) {
      force = 0;
      update(sigma);
    }
  }

  public void deltcon(double e, message x) {
    deltint();
    deltext(e, x);
  }

  public message out() {
    message m = super.out();
    if (phaseIs("output")) {
      if (change == 0) {
        m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
            xPos, yPos /*(int)force*/, Color.blue, Color.blue)));
        System.out.println("(xPos,yPos)...(" + xPos + "," + yPos + ")");
      }
      else if (crack == 1) {
        crack = 0;
        threshold = threshold * 5;
        m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
            xcoord, ycoord, Color.MAGENTA, Color.MAGENTA)));
      }
      else {
        m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
            xcoord, ycoord, Color.GREEN, Color.gray)));
      }
      m.add(makeContent("outEnergy", new DrawCellEntity("drawCellToScale",
          xcoord, (int) Math.abs(force), Color.MAGENTA, Color.gray)));
      m.add(makeContent("outCoord",
                        new Pair(new Integer( (int) xcoord),
                                 new Integer( (int) ycoord))));
      m.add(makeContent("outN",
                        new Pair(new Integer(xcoord), new Integer(ycoord))));
      m.add(makeContent("outS",
                        new Pair(new Integer(xcoord), new Integer(ycoord))));
      m.add(makeContent("outE",
                        new Pair(new Integer(xcoord), new Integer(ycoord))));
      m.add(makeContent("outW",
                        new Pair(new Integer(xcoord), new Integer(ycoord))));
      return m;
    }
    else {
      return m;
    }
  }
}
