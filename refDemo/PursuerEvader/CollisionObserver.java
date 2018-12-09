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

public class CollisionObserver extends ViewableAtomic{

  protected int count;
  protected double clock;

  public CollisionObserver() {
    this("Collision");
  }

  public CollisionObserver(String nm) {
    super(nm);
    addInport("in");
    addOutport("out");
  }

  public void initialize(){
    clock = 0;
    count = 0;
    super.initialize();
    passivate();
  }

  public void  deltext(double e,message x){
    clock = clock + e;
    Continue(e);

    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "in", i)) {
        entity en = x.getValOnPort("in", i);
        if (en.eq("true")) {
          count = count + 1;
          holdIn("counting", 0);
        }
      }
    }


  }

  public void  deltint( ){
    clock = clock + sigma;
    passivate();
  }

  public message  out( ){
    message  m = new message();

    if (phaseIs("counting")) {
        m.add(makeContent("out",  new doubleEnt(count)));
    }
    return m;
  }

  public void showState(){
    super.showState();
  }

}