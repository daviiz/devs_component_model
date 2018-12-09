package refDemo.NetShortPath;

//Java native libraries
//DEVS libraries
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

//This class defines a common node (computer)
public class packetProcessor extends ViewableAtomic {

  //Constants for ID type identification
  final static double hello = 0;
  final static double bq = 1;
  final static double reply = 2;
  final static double data = 3;

  //Constants for package field definition
  final static  double ID = 0;
  final  static double sAddress = 1;
  final  static double dAddress = 2;
  final  static double pkLife = 5;
  final  static double inPort = 6;
  final  static double totalCost = 4;
  final  static double routeNodes = 3;

  //Node constants
  final int lifeCount = 5;
  protected double sourceAddress, destinationAddress;
  protected String name;
  protected list packet;
  protected String packetStr;//by sm
  protected double procTime;
  protected double destination;

  protected int ports [];

  //default constructor
  public packetProcessor () {
    this ("node", 1, 1.0);

    packet = new list ();
    packetStr = new String("nl");//by sm
  }

  //construtor
  public packetProcessor (String n, double ip, double pt) {
    super(n);

    name = n;
    sourceAddress = ip;
    procTime = pt;

    double pkSequence = Math.random () % 100;
    packet = new list (Double.toString (pkSequence));

    //atomic model port declarations
    addInport ("in");
    addOutport ("out");

    addInport ("send");
    addInport ("proc");
    addOutport ("route");

    addTestInput ("R", new list ("list"), 1.0);
    addTestInput ("R", new list ("list"), 1.0);

    addRealTestInput ("send", 1);
    addRealTestInput ("send", 2);
    addRealTestInput ("send", 4);
    addRealTestInput ("send", 8);
    addRealTestInput ("send", 16);
    addRealTestInput ("send", 32);
    addRealTestInput ("send", 64);
    addRealTestInput ("send", 128);
  }

  //initialization
  public void initialize () {

    //create package
    packet = new list();
    ports = new int[4];
    destinationAddress = -1;

    for (int i = 0; i < ports.length; i++) {
      ports[i] = i;
    }

    holdIn("powerOff", 0);
    super.initialize ();
  }

  //external function definition
  public void deltext(double e, message x) {
    Continue(e);

    if (phaseIs("idle") || phaseIs("waiting")) {
      for (int i = 0; i < x.size(); i++) {
        if (messageOnPort(x, "send", i)) {
          double temp;
          temp = getRealValueOnPort(x, "send");
          if (temp != sourceAddress) {
            destinationAddress = temp;
            createPacket();
            holdIn("sending", 0.0);
          }
        }
        else if (messageOnPort(x, "in", i)) {
          boolean process = true;

          list temp = new list();
          temp = (list) x.getValOnPort("in", i);

          Field f = new Field();
          f = temp.firstNode;

          if (temp.firstNode.getFieldData() == bq) {
            //pk reached the source -- discard
            while (temp.firstNode.getFieldID() != sAddress) {
              temp.firstNode = temp.firstNode.nextField;
            }

            if (temp.firstNode.getFieldData() == sourceAddress) {
              process = false;
            }

            temp.firstNode = f;

            //pk reached in intermidiate node
            if (process) {
              while (temp.firstNode.getFieldID() != dAddress) {
                temp.firstNode = temp.firstNode.nextField;
              }

              //if destination reached - process
              if (temp.firstNode.getFieldData() == sourceAddress) {
                holdIn("processing", 1.0);
              }
              //if not destination -- route
              else {
                holdIn("routing", 1.0);
              }

              temp.firstNode = f;
              packet = temp;
            }
          }
          else if (temp.firstNode.getFieldData() == reply) {
            while (temp.firstNode != null) {
              if (temp.firstNode.getFieldID() == dAddress) {
                if (temp.firstNode.getFieldData() == sourceAddress) {
                  holdIn("transmitting", 5.0);
                }
              }
              else if (temp.firstNode.getFieldID() == routeNodes) {
                if (temp.firstNode.getFieldData() == sourceAddress) {
                  holdIn("forwarding", 0.5);
                  break;
                }
              }
              temp.firstNode = temp.firstNode.nextField;
            }
            temp.firstNode = f;
            packet = temp;
          }
          else if (temp.firstNode.getFieldData() == data
                   && phaseIs("waiting")) {
            holdIn("receiving", 0);
          }
        }
        else if (messageOnPort(x, "proc", i)) {
          list temp = (list) x.getValOnPort("proc", i);
          packet = temp.copyList();
          if (!packet.isEmpty()) {
            holdIn("replying", 0.0);
          }
          else {
            holdIn("waiting", INFINITY);
          }
        }
      }
    }
    else if (phaseIs("routing")) {
      for (int i = 0; i < x.size(); i++) {
        if (messageOnPort(x, "proc", i)) {
          list temp = (list) x.getValOnPort("proc", i);
          packet = temp.copyList();

          if (!packet.isEmpty()) {
            holdIn("sending", 1.0);
          }
          else {
            holdIn("idle", INFINITY);
          }
        }
      }
    }
    else if (phaseIs("forwarding")) {
      boolean changeState = true;

      for (int i = 0; i < x.size(); i++) {
        if (messageOnPort(x, "in", i)) {
          list temp = new list();
          temp = (list) x.getValOnPort("in", i);

          packet = temp.copyList();

          if (packet.firstNode.getFieldData() == reply) {
            while (packet.firstNode != null) {
              if (packet.firstNode.getFieldID() == routeNodes) {
                if (packet.firstNode.getFieldData() == sourceAddress) {
                  changeState = false;
                  break;
                }
              }
              packet.firstNode = packet.firstNode.nextField;
            }

            if (changeState) {
              holdIn("idle", INFINITY);
            }
            changeState = true;
          }
          else if (packet.firstNode.getFieldData() == data) {
            boolean processed = false;
            Field head = new Field();
            head = packet.firstNode;

            while (packet.firstNode != null) {
              if (packet.firstNode.getFieldID() == routeNodes) {
                if (packet.firstNode.getFieldData() == sourceAddress) {
                  processed = true;
                  break;
                }
              }
              packet.firstNode = packet.firstNode.nextField;
            }

            packet.firstNode = head;

            if (!processed) {
              packet.insertAtBack (routeNodes, sourceAddress);
              packet.print();
              holdIn("forwarding", 0.0);
            }
          }
        }
      }
    }
  }

  public void createPacket() {
    packet.insertAtFront (pkLife, lifeCount);
    packet.insertAtFront (totalCost, 0);
    packet.insertAtFront (dAddress, destinationAddress);
    packet.insertAtFront (sAddress, sourceAddress);
    packet.insertAtFront (ID, bq);
    packet.print();

    packetStr = bq+ "\n  src:"+ sourceAddress+"   \ndest: "+destinationAddress
        +"    \ncost:"+totalCost+"   \nlifeCnt:"+lifeCount;

  }
  public String getTooltipText () {
    double id = 0;
    try{
      id = packet.firstNode.getFieldData();
    }
    catch(NullPointerException e){
      id = -1;
      return super.getTooltipText() +
        "\n" + "IP: " + sourceAddress;
    }
    String strId = "null";
    if(id == 0){
      strId = "Hello";
    }
    else if(id ==1){
      strId = "Bq";
    }
    else if(id ==2){
      strId = "Reply";
    }
    else if (id ==3){
      strId = "Data";
    }

    double dest = packet.firstNode.getFieldID();

    return super.getTooltipText() +
        "\n" + "IP: " + sourceAddress+
        "\n"+packet.printList();

  }


  //internal function definition
  public void deltint () {
    if (phaseIs("routing")) {
      holdIn("routing", INFINITY);
    }
    else if (phaseIs("forwarding")) {
      holdIn("forwarding", INFINITY);
    }
    else if (phaseIs("transmitting")) {
      holdIn("transmitting", 5.0);
    }
    else if (phaseIs("replying") ||
             phaseIs("receiving")) {
      holdIn("waiting", INFINITY);
    }
    else {
      holdIn("idle", INFINITY);
    }
  }

  //output function definition
  public message out () {
    message m = new message ();

    if (phaseIs ("sending") ||
        phaseIs ("replying") ||
        phaseIs ("forwarding")) {
      content con = makeContent ("out", packet);
      m.add (con);
      return m;
    }
    else if (phaseIs ("routing") ||
             phaseIs ("processing") ||
             phaseIs ("replying") ||
             phaseIs ("updating route")) {
      content con = makeContent ("route", packet);
      m.add (con);
      return m;
    }
    else if (phaseIs ("transmitting")) {
      list temp = new list ();
      temp.insertAtBack (ID, data);
      content con = makeContent ("out", temp);
      m.add (con);
      return m;
    }

    return m;
  }

  //current state function definition
  public void showState () {
    super.showState ();
  }

  public void deltcon (double e, message x) { //usual devs
    deltext (0, x);
    deltint ();
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

  public entity getEntityOnPort(message x, String port) {
    for (int i = 0; i < x.getLength(); i++) {
      if (messageOnPort (x, port, i)) {
        return x.getValOnPort (port, i);
      }
    }
    return null;
  }

  public void addPortTestInput(String port, list l) {
    addTestInput(port, l, 0);
  }

  public list getPortMessage (message x, String port) {
    list tl = (list) getEntityOnPort (x, port);
    return tl;
  }

  public message outputRealOnPort(double r, String port) {
    message m = new message();
    m.add(makeContent(port, new doubleEnt(r)));
    return m;
  }

  public double getRealValueOnPort(message x, String port) {
    doubleEnt dv = (doubleEnt) getEntityOnPort(x, port);
    return dv.getv();
  }

  public void addRealTestInput(String port, double value) {
    addTestInput(port, new doubleEnt(value), 0);
  }
}