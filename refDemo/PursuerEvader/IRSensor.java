package refDemo.PursuerEvader;

import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 * <p>Title: IRSensor</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Eddie Mak
 * @version 1.0
 */

public class IRSensor extends ViewableAtomic{

  protected vect2DEnt pos;  //current position

  public IRSensor() {
    this("obstacle", vect2DEnt.ZERO);
  }

  public IRSensor(String nm, vect2DEnt vect) {
    super(nm);
    addInport("position");
    addOutport("collision");
  }

  public void initialize(){
    pos = vect2DEnt.ZERO;
    super.initialize();
    passivate();
  }

  public void deltext(double e,message x){
    Continue(e);

    for (int i = 0; i < x.getLength(); i++) {
      if (messageOnPort(x, "position", i)) {
        entity en = x.getValOnPort("position", i);
        pos = vect2DEnt.toObject(en);

      }
    }
  }

  public void deltint(){
    passivate();
  }

  public message out(){
    message m = new message();
    m.add(makeContent("collision",new entity("true")));
    return m;
  }


}