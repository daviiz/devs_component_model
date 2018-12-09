package refDemo.PursuerEvader;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class StopObserver extends ViewableAtomic{

  protected double clock;
  protected vect2DEnt pursuer, evader;

  public StopObserver() {
    this("Stop Observer");
  }

  public StopObserver(String nm) {
    super(nm);
    addInport("pursuer");
    addInport("evader");
    addOutport("outTime");
  }

  public void initialize(){
    clock = 0;
    super.initialize();
    passivate();
  }

  public void  deltext(double e,message x){
    clock = clock + e;
    Continue(e);

    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "pursuer", i)) {
        entity en = x.getValOnPort("pursuer", i);
        pursuer = vect2DEnt.toObject(en);
      }
      if (messageOnPort(x, "evader", i)) {
        entity en = x.getValOnPort("evader", i);
        evader = vect2DEnt.toObject(en);
      }
    }

    if (Math.round(pursuer.x) == Math.round(evader.x) &&
        Math.round(pursuer.y) == Math.round(evader.y)) {
      holdIn("Stop", 0);
    }

  }

  public void  deltint( ){
    clock = clock + sigma;
    passivate();
  }

  public message  out( ){
    message  m = new message();

    if (phaseIs("Stop")) {
        m.add(makeContent("outTime",  new doubleEnt(clock)));
    }
    return m;
  }

  public void showState(){
    super.showState();
  }


}