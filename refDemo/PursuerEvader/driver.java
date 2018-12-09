package refDemo.PursuerEvader;


import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import util.rand;
import view.modeling.ViewableAtomic;

public class driver extends  ViewableAtomic{
  protected vect2DEnt lastPosition, position, newPosition, desiredPosition,difference;
  protected double tolerance = 2,maxPos = 100;
  protected vect2DEnt obs[] = new vect2DEnt[4];  //obstacle position;
  protected boolean collision;
  protected vect2DEnt posDiff;


  public driver(String nm){
    super(nm);
    addInport("position");
    addInport("desiredPosition");
    addOutport("brake");
    addOutport("accelerate");
    addOutport("maxSpeed");
    addOutport("maxAccel");
    addOutport("collision");
  }



  public driver(){
    this("evader");
    addTestInput("position",vect2DEnt.ZERO,0);
    addTestInput("position",new vect2DEnt(50,50),0);
    addTestInput("position",new vect2DEnt(80,60),0);
    addTestInput("desiredPosition",new vect2DEnt(80,60),0);
  }


  public void initialize(){
    desiredPosition = vect2DEnt.ZERO;
    position = vect2DEnt.ZERO;
    lastPosition = vect2DEnt.ZERO;
    obs[0] = new vect2DEnt(-80, 80);
    obs[1] = new vect2DEnt(-80, -80);
    obs[2] = new vect2DEnt(80, -80);
    obs[3] = new vect2DEnt(80, 80);
    collision = false;
    super.initialize();
    holdIn("setSpeed", 0);
  }

  rand r = new rand(System.currentTimeMillis());
  java.util.Random generator = new java.util.Random(System.currentTimeMillis());

  public void deltext(double e,message x){
    Continue(e);

    collision = false;
    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x,"position",i)) {
         entity en = x.getValOnPort("position",i);
         position = vect2DEnt.toObject(en);
         newPosition = vect2DEnt.toObject(en);

         if (lastPosition == vect2DEnt.ZERO) {
           newPosition.addSelf(new vect2DEnt(r.uniform(-10,10),r.uniform(-10,10)));
         } else {
           if (lastPosition.x > position.x && lastPosition.y > position.y) {
             newPosition.addSelf(new vect2DEnt(r.uniform( -10, 0),
                                            r.uniform( -10, 0)));
           }else if (lastPosition.x > position.x && lastPosition.y < position.y) {
             newPosition.addSelf(new vect2DEnt(r.uniform( -10, 0),
                                            r.uniform( 0, 10)));
           }else if (lastPosition.x < position.x && lastPosition.y > position.y) {
             newPosition.addSelf(new vect2DEnt(r.uniform( 0, 10),
                                            r.uniform( -10, 0)));
           } else {
             newPosition.addSelf(new vect2DEnt(r.uniform( 0, 10),
                                            r.uniform( 0, 10)));
           }

         }
         collision = obstacles(position, newPosition);
         System.out.println(getName()+ " position "+ position.toString());
         lastPosition = newPosition;
      }
      else if (messageOnPort(x,"desiredPosition",i)) {
        entity en = x.getValOnPort("desiredPosition",i);
        desiredPosition = vect2DEnt.toObject(en);
        // desiredPosition = (vect2DEnt)en;
        System.out.println(getName()+ " desiredPosition "+ desiredPosition.toString());
      }
    }

    if (collision == true) {
      System.out.println("Collision");
      posDiff = position.subtract(newPosition);
      if (generator.nextBoolean() == true)
        posDiff = posDiff.leftPerpendicular();
      else
        posDiff = posDiff.rightPerpendicular();
      newPosition = position.add(posDiff);
      System.out.println("Collision");
    }

    difference = desiredPosition.subtract(newPosition);

    if (collision == false) {
      if (name.startsWith("evade")) {
        if (generator.nextBoolean() == true)
          difference = difference.leftPerpendicular();
        else
          difference = difference.rightPerpendicular();
      }
    }

    if (difference.norm()<= tolerance)
      holdIn("doBrake",0);
    else {
      if (position.x < -maxPos) {
        difference.x = Math.abs(difference.x);
        collision = true;
      }
      if( position.x > maxPos) {
        difference.x = -Math.abs(difference.x);
        collision = true;
      }
      if (position.y < -maxPos) {
        difference.y = Math.abs(difference.y);
        collision = true;
      }
      if(position.y > maxPos) {
        difference.y = -Math.abs(difference.y);
        collision = true;
      }

      holdIn("doAccel",0);
    }

}

public void deltint(){
  passivate();
}

public message out(){
  message m = new message();
  if (phaseIs("setSpeed")) {
    if (name.startsWith("evader")) {
      m.add(makeContent("maxSpeed", new doubleEnt(10)));
      m.add(makeContent("maxAccel",new doubleEnt(20)));
    } else {
      m.add(makeContent("maxSpeed", new doubleEnt(12)));
      m.add(makeContent("maxAccel",new doubleEnt(20)));
    }
  } else if (phaseIs("doBrake")) {
    m.add(makeContent("brake", new doubleEnt(100)));
    m.add(makeContent("collision", new entity(Boolean.toString(collision))));
  }
  else if (phaseIs("doAccel")) {
    m.add(makeContent("accelerate", difference));
    m.add(makeContent("collision", new entity(Boolean.toString(collision))));
  }
  return m;
}

public void showState(){
   super.showState();
   System.out.println(
  "\n"+ "difference :"+ difference
  );
  }

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"difference :"+ difference;
  }

// return true if it will hit obstacles
// v1 = current position, v2 = new predicted position
public boolean obstacles(vect2DEnt v1, vect2DEnt v2) {
    double m1, m2, s1, s2, s3;

    m1 = (v1.y - v2.y)/(v1.x - v2.x);
    s1 = Math.sqrt(Math.pow((v1.x-v2.x), 2) + Math.pow((v1.y-v2.y),2));

    for(int i=0; i<obs.length; i++) {
      m2 = (v2.y - obs[i].y)/(v2.x - obs[i].x);
      if (Math.round(m1) == Math.round(m2)) {
        s2 = Math.sqrt(Math.pow((v1.x-obs[i].x), 2) + Math.pow((v1.y-obs[i].y),2));
        s3 = Math.sqrt(Math.pow((v2.x-obs[i].x), 2) + Math.pow((v2.y-obs[i].y),2));
        if (Math.round(s1) == Math.round(s2+s3)) {
          return true;
        }
      }

    }
    return false;
}

  public double round(double val, int decimals){
    java.math.BigDecimal dec = new java.math.BigDecimal( new Double(val).doubleValue() );
    dec = dec.setScale(decimals,java.math.BigDecimal.ROUND_UP);
    return dec.doubleValue();
  }

public static void main(String args[]){
//new  driver(" ");
//entity e = new intEnt(1);
}

 }





