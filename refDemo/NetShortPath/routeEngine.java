package refDemo.NetShortPath;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class routeEngine extends ViewableAtomic {

  boolean discard = false;
  double prevRouteCost = 10000;
  double currentRouteCost = 10000;

  //Constants for ID type identification
  final double hello = 0.0;
  final double bq = 1;
  final double reply = 2;

  //Constants for package field definition
  final double ID = 0;
  final double sAddress = 1;
  final double dAddress = 2;
  final double pkLife = 5;
  final double totalCost = 4;
  final double routeNodes = 3;
  final double inPort = 6.0;

  protected double procTime;
  protected int sourceAddress;

  protected list packet;

  public routeEngine () {
    this("routeEngine", 0.2, 1);
  }

  public routeEngine (String name, double t, int ip) {
    super(name);

    packet = new list ();

    addInport("in");
    addOutport("out");

    sourceAddress = ip;
    procTime = t;
  }

  public void initialize(){
    super.initialize();

    packet = new list ();

    prevRouteCost = 10000;
    currentRouteCost = 10000;
    discard = false;

    holdIn ("idle", INFINITY);
  }

  public void deltext (double e, message x) {
    list temp = new list();
    boolean detinationReached = false;

    Continue(e);

    if (somethingOnPort (x, "in")) {
      temp = getPortMessage(x, "in");
      packet = temp.copyList();

      Field f = new Field();
      f = packet.firstNode;

      holdIn("processing", procTime);

      while (packet.firstNode.getFieldID () != dAddress) {
        packet.firstNode = packet.firstNode.nextField;
      }

      //if destination reached - process
      if (packet.firstNode.getFieldData () == sourceAddress) {
        detinationReached = true;
      }

      packet.firstNode = f;

      if (!detinationReached) {
        //check if pk traverse this node, if so discard don't forward
        while (packet.firstNode != null && discard == false) {
          if (packet.firstNode.getFieldID() == routeNodes &&
              packet.firstNode.getFieldData() == sourceAddress) {

            discard = true;
            break;
          }

          packet.firstNode = packet.firstNode.nextField;
        }

        if (!discard) {
          packet.firstNode = f;

          //check for pk life
          while (packet.firstNode.getFieldID() != pkLife) {
            packet.firstNode = packet.firstNode.nextField;
          }

          if (packet.firstNode.getFieldData() != 0) {
            double count = 0;

            count = packet.firstNode.getFieldData();
            packet.firstNode.setFieldData(count - 1);
            packet.firstNode = f;
            packet.insertAtBack(routeNodes, sourceAddress);
          }
          else {
            discard = true;
          }
        }
      }
      else {

        packet.firstNode = f;

        while (packet.firstNode.getFieldID() != totalCost) {
          packet.firstNode = packet.firstNode.nextField;
        }

        double cost = packet.firstNode.getFieldData();

        if (cost < currentRouteCost) {
          packet.firstNode = f;
          createReplyPk (); //creates reply message
        }
        else
          packet = new list ();
      }
    }
  }

  public void createReplyPk () {
    list temp = new list ();
    list replyPk = new list ();
    Field f = packet.firstNode;

    double source;
    double pathCost;

    temp = packet.copyList ();

    replyPk.insertAtFront (ID, reply);
    replyPk.insertAtBack (sAddress, sourceAddress);

    while (temp.firstNode != null) {
      if (temp.firstNode.getFieldID () == sAddress) {
        source = temp.firstNode.getFieldData();
        replyPk.insertAtBack (dAddress, source);
      }
      else if (temp.firstNode.getFieldID () == totalCost) {
        pathCost = temp.firstNode.getFieldData();
        prevRouteCost = currentRouteCost = pathCost;
      }
      else if (temp.firstNode.getFieldID () == routeNodes) {
        replyPk.insertAtBack (routeNodes, temp.firstNode.getFieldData ());
      }
      temp.firstNode = temp.firstNode.nextField;
    }

    packet = replyPk.copyList ();
  }

  public void deltint () {
    holdIn ("idle", INFINITY);
  }

  public message out () {

    if (!discard) {
      message m = new message();
      content con = makeContent ("out", packet);
      m.add(con);

      return m;
    }
    else {
      message m = new message();
      content con = makeContent("out", new list());
      m.add(con);

      discard = false;

      return m;
    }
 }

  public void showState () {
    super.showState ();
  }

  /**********************Other Useful functions defintions*********************/
  public message outputOnPort (list l, String port) {
    message m = new message ();
    m.add (makeContent (port, l));
    return m;
  }

  public boolean somethingOnPort (message x, String port) {
    for (int i = 0; i < x.getLength (); i++) {
      if (messageOnPort (x, port, i))
        return true;
    }
    return false;
  }

  public entity getEntityOnPort (message x, String port) {
    for (int i = 0; i < x.getLength (); i++) {
      if (messageOnPort (x, port, i)) {
        return x.getValOnPort (port, i);
      }
    }
    return null;
  }

  public void addPortTestInput (String port, list l) {
    addTestInput (port, l, 0);
  }

  public list getPortMessage (message x, String port) {
    list tl = (list) getEntityOnPort (x, port);
    return tl;
  }

  public message outputRealOnPort (double r, String port) {
    message m = new message ();
    m.add (makeContent (port, new doubleEnt (r)));
    return m;
  }

  public double getRealValueOnPort (message x, String port) {
    doubleEnt dv = (doubleEnt) getEntityOnPort (x, port);
    return dv.getv ();
  }

  public void addRealTestInput (String port, double value) {
    addTestInput (port, new doubleEnt (value), 0);
  }
}

