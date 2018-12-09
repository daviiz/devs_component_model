package refDemo.PursuerEvader;

import java.awt.Color;

import model.modeling.message;
import model.plots.DrawCellEntity;
import view.modeling.ViewableAtomic;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class obstacles extends ViewableAtomic{

  protected vect2DEnt position;

  public obstacles() {
    this("obstacle");
  }

  public obstacles(String nm){
    super(nm);
    addInport("in");
    addOutport("outDraw");
  }

  public void initialize(){
    position = new vect2DEnt(80, 80);
    super.initialize();
    holdIn("setObstacle", 0);
  }

  public void deltext(double e,message x){
    Continue(e);
    passivate();
  }

  public void deltint(){
    passivate();
  }

  public message out(){
    message m = new message();
    if (phaseIs("setObstacle")) {
      m.add(makeContent("outDraw", new DrawCellEntity( -80, -80, Color.black, Color.black)));
      m.add(makeContent("outDraw", new DrawCellEntity( -80, 80, Color.black, Color.black)));
      m.add(makeContent("outDraw", new DrawCellEntity( 80, -80, Color.black, Color.black)));
      m.add(makeContent("outDraw", new DrawCellEntity( 80, 80, Color.black, Color.black)));
    }
    return m;
  }


}