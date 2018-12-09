package refDemo.NetShortPath;

import GenCol.Queue;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class link extends ViewableAtomic {

  //Constants for ID type identification
  final double hello = 0;
  final double bq = 1;
  final double reply = 2;
  final double data = 3;

  //Constants for package field definition
  final double weight = 4;

  protected int linkCost;
  protected double procTime;
  protected Queue q;
  protected list packet;
  int count = 0;

  public link () {
    this ("link", -100);

    q = new Queue ();
    packet = new list ();
  }

  public link (String name, int c) {
    super(name);

    q = new Queue ();
    packet = new list ();

    linkCost = c;

    addInport ("cost");
    addInport ("r");
    addOutport ("t");

    addTestInput ("cost", new entity("cost"), 0);
    addTestInput ("cost", new entity("cost"), 2);
    addTestInput ("cost", new entity("cost"), 4);
    addTestInput ("cost", new entity("cost"), 8);
    addTestInput ("cost", new entity("cost"), 16);
    addTestInput ("cost", new entity("cost"), 32);
  }

  public void initialize () {
    procTime = 0.0;

    q = new Queue ();
    packet = new list ();

    holdIn ("idle", INFINITY);
    super.initialize();
  }

  public void deltext (double e, message x) {
    Continue (e);

    if (somethingOnPort (x, "r")) {
      if (phaseIs("idle")) {
        list temp = (list) getPortMessage(x, "r");
        packet = temp.copyList();
        q.add (packet);
        packet = (list) q.first();
        holdIn ("transmitting", procTime);
      }

      else if (phaseIs("transmitting")) {
        if (somethingOnPort(x, "r")) {
          list l = new list ();
          l = (list) getPortMessage(x, "r");
          packet = l.copyList();
          q.add (packet);
        }
      }
    }
  }

  public void deltint () {
    q.remove ();
    if (!q.isEmpty ()) {
      packet = (list) q.first ();
      holdIn ("transmitting", procTime);
    }
    else {
      holdIn ("idle", INFINITY);
    }
  }

  public message out () {
   if (phaseIs ("transmitting")) {
     insertLinkCost (packet);
     return outputOnPort (packet, "t");
   }
   else {
     return new message ();
   }
 }

 void insertLinkCost (list p) {
   double cost = 0;

   list temp = new list ();
   temp = p.copyList();

   Field node = new Field();

   node = temp.firstNode;

   if (temp.firstNode.getFieldData () != reply &&
       temp.firstNode.getFieldData () != data) {
     while (temp.firstNode.getFieldID() != weight) {
       temp.firstNode = temp.firstNode.getNextField();
     }
     cost = linkCost + temp.firstNode.getFieldData();
     temp.firstNode.setFieldData(cost);
     temp.firstNode = node;
   }

   packet = temp.copyList();
 }

 public String getTooltipText () {
   return super.getTooltipText() +
       "\n" + "Cost: " + linkCost;
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
}

