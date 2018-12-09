/*
* @author J Phelps 12/2003
*/

package refDemo.RadarProject;
import java.awt.Color;

import GenCol.ExternalRepresentation;




public class radarentity extends GenCol.entity {

protected int m_TQ;
protected double m_x;
protected double m_y;
protected Color m_color;



public radarentity(){
    name = "anradarentity";
    m_TQ = 0;
    m_x = 0.0;
    m_y = 0.0;
    m_color = Color.black;
}

  public radarentity(radarentity r){
      name = r.name;
      m_TQ = r.m_TQ;
      m_x = r.m_x;
      m_y = r.m_y;
      m_color = r.m_color;
  }

public radarentity(String nm,int TQ, double x, double y, Color color){
name = nm;
m_TQ = TQ;
m_x = x;
m_y = y;
m_color = color;

}
public boolean eq(String nm){
return getName().equals(nm);
}

public Object equalName(String nm){
if (eq(nm)) return this;
else return null;
}

public boolean equals(Object o){    //overrides pointer equality of Object
if  (!(o instanceof radarentity))return false;
else return eq(((radarentity)o).getName());
}

public ExternalRepresentation getExtRep(){
return new ExternalRepresentation.ByteArray();
}

public String getName(){
return name;
}



/**/

public String toString(){

return ("X  " + m_x
            + " Y  " + m_y
        + " TQ  " + m_TQ    );
}

public void print(){
System.out.println(name);
}


  public double getX()
  {

   return m_x;
  }
 public double getY()
 {

   return m_y;

 }
 public Color getColor()
{
  return m_color;
}

public int getTQ()
{
 return m_TQ;
}

}

