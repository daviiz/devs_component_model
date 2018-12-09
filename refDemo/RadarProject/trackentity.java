/*
* @author J Phelps 12/2003
*/

package refDemo.RadarProject;
import java.awt.Color;

import GenCol.ExternalRepresentation;



public class trackentity extends radarentity {

protected double m_course;
protected double m_speed;
protected double m_RCS;




public trackentity(){
    name = "antrackentity";
    m_x = 0;
    m_y = 0;
}
public trackentity(String nm,double x, double y, double course,double speed, double RCS, Color color){
name = nm;
m_x = x;
m_y = y;
m_course = course;
m_speed = speed;
m_RCS = RCS;
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
if  (!(o instanceof trackentity))return false;
else return eq(((trackentity)o).getName());
}

public ExternalRepresentation getExtRep(){
return new ExternalRepresentation.ByteArray();
}

public String getName(){
return name;
}

 public double getCourse()
     {
       return m_course;
     }

     public double getSpeed()
     {    return m_speed;
     }
     public double getRCS()
     {
    return  m_RCS;
     }




/**/

public String toString(){
return getName();
}

public void print(){
System.out.println(name);
}

}

