package refDemo.NetShortPath;

import GenCol.entity;

class Field {

  private double fieldID;
  private double fieldData;

  Field nextField;
  Field previousField;

  Field () {
    fieldID = 0;
    fieldData = 0;
    nextField = null;
  }

  Field (double id, double data, Field n) {
    fieldID = id;
    fieldData = data;
    nextField = n;
  }

  double getFieldID () {
    return fieldID;
  }

  double getFieldData () {
    return fieldData;
  }

  Field getNextField () {
    return nextField;
  }

  void setFieldID (double id) {
    fieldID = id;
  }

  void setFieldData (double data) {
    fieldData = data;
  }

  void setNextField (Field f) {
    nextField = f;
  }
}

class EmptyListException extends RuntimeException {
  public EmptyListException (String name) {
    super ("The " + name + " is empty.");
  }
}

public class list extends entity {
  Field firstNode;
  Field lastNode;
  String name;

  public list () {
    this ("list");
  }

  public list (String s) {
    firstNode = null;
    lastNode = null;
    name = s;
  }

  public void insertAtFront (double id, double data) {
    if (isEmpty ()) {
      firstNode = lastNode = new Field (id, data, lastNode);
    }
    else
    {
      firstNode = new Field (id, data, firstNode);
    }
  }

  public void insertAtBack (double id, double data) {
    if (isEmpty ()) {
      lastNode = firstNode = new Field (id, data, lastNode);
    }
    else
    {
      Field temp = new Field ();
      temp = this.firstNode;

      while (firstNode != lastNode) {
        firstNode = firstNode.nextField;
      }

      lastNode = new Field (id, data, null);
      firstNode.nextField = lastNode;
      firstNode = temp;
    }
  }

  public void removeFromFront () throws EmptyListException {
    double id = -1;
    double data = -1;

    if (isEmpty ()) {
      throw new EmptyListException (name);
    }
    else
    {
      id = firstNode.getFieldID ();
      data = firstNode.getFieldData ();
    }

    if (firstNode.equals (lastNode)) {
      firstNode = lastNode = null;
    }
    else
    {
      Field current = firstNode;
      firstNode = firstNode.getNextField();
      current.setNextField (null);
    }

    System.out.println ("Field ID: " + id);
    System.out.println ("Field data: " + data);
  }

  public boolean isEmpty () {
    return firstNode == null;
  }

  public void print () {
    String str = new String("");

    if (isEmpty ()) {
      System.out.println ("Empty " + name);
      return ;
    }

    System.out.print ("The " + name + " is: ");

    Field current = firstNode;

    while (current != null) {
      System.out.print (Double.toString(current.getFieldID()) +  " ");
      System.out.print (Double.toString(current.getFieldData()) +  " \n");
      current = current.getNextField();

    }

    current = firstNode;

    System.out.println ();
    System.out.println ();
  }


  public String printList () {
    String str = new String("");

    if (isEmpty ()) {
      System.out.println ("Empty " + name);
      return "Empty ";
    }

    System.out.print ("The " + name + " is: \n");

    Field current = firstNode;

    while (current != null) {
      System.out.print ("FieldId: "+Double.toString(current.getFieldID()) +  " ");
      System.out.print ("FieldData: "+Double.toString(current.getFieldData()) +  " \n");
      str += "FieldID: "+Double.toString(current.getFieldID()) +  " "+
          "FieldData:" +Double.toString(current.getFieldData()) +  " \n";
      current = current.getNextField();

    }

    current = firstNode;

    System.out.println ();
    System.out.println ();
    return str;
  }


  public list copyList () {
    list temp = new list ();

    Field head = new Field ();
    head = this.firstNode;

    while (this.firstNode != null) {
      temp.insertAtBack(this.firstNode.getFieldID(), this.firstNode.getFieldData());
      this.firstNode = this.firstNode.nextField;
    }

    this.firstNode = head;

    return temp;
  }

public String toString(){
Field current = firstNode;
             String s = " ";
             int count = 0;
             boolean isBQ = false;
while (current != null) {
               if (count == packetProcessor.ID){
                 if ( (int) current.getFieldData() != 0)
                   isBQ = true;
               }
if (count == packetProcessor.totalCost){
  if (isBQ)
  s += "totalCost " + " " + current.getFieldData();
      }
   current = current.getNextField();
  count++;
}
return s;
}

}