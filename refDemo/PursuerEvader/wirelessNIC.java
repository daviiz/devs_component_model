package refDemo.PursuerEvader;

import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;


/**
 * <p>Title: wirelessNIC</p>
 * <p>Description: Wireless Network card to broadcast and receive the position</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Eddie Mak
 * @version 1.0
 */

public class wirelessNIC extends ViewableAtomic{

  protected vect2DEnt recvPos;  //other robot's position
  protected vect2DEnt broadPos;  //broadcast current robot's position

  public wirelessNIC() {
    this("wirelessNIC");
  }

  public wirelessNIC(String nm) {
    super(nm);
    addInport("inRecv");
    addInport("inBroadcast");
    addOutport("outRecv");
    addOutport("outBroadcast");
  }

  public void initialize(){
    recvPos = vect2DEnt.ZERO;
    broadPos = vect2DEnt.ZERO;
    super.initialize();
    passivate();
  }

  public void deltext(double e,message x){
    Continue(e);

    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "inRecv", i)) {
        entity en = x.getValOnPort("inRecv", i);
        recvPos = vect2DEnt.toObject(en);
        holdIn("Received", 0);
      } else if (messageOnPort(x, "inBroadcast", i)) {
        entity en = x.getValOnPort("inBroadcast", i);
        broadPos = vect2DEnt.toObject(en);
        holdIn("Broadcast", 0);
      }
    }

  }

  public void deltint(){
    passivate();
  }


  public message out(){
    message m = new message();
    if (phaseIs("Received"))
      m.add(makeContent("outRecv", recvPos));
    else if (phaseIs("Broadcast"))
      m.add(makeContent("outBroadcast", broadPos));

    return m;
  }


}