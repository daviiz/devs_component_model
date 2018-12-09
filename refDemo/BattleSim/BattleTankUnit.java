package refDemo.BattleSim; import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class BattleTankUnit extends ViewableAtomic {

    protected entity job;
    protected double processing_time;
    double getPowerLevel;
    static double powerLevel;
    int count;
    public BattleTankUnit() {
      this("Tank Unit", 1);
    }

    public BattleTankUnit(String  name,double  Processing_time){
      super(name);
      addInport("in");
      addOutport("out1");
      addOutport("out2");
      processing_time = Processing_time;
    }

    public void initialize(){
      phase = "passive";
      sigma = INFINITY;
      job = new entity("job");
      count = 0;
      powerLevel = 300;
      super.initialize();
    }

    public void  deltext(double e,message   x)
    {
      Continue(e);
      passivate();
      count = 0;
      if (phaseIs("passive")) {
        for (int i = 0; i < x.getLength(); i++) {
          if (messageOnPort(x, "in", i)) {
            holdIn("Get Command", processing_time);
          }
        }
      }
    }

    public void  deltint( )
    {
    }

    public message    out( )
    {
      holdIn("attack", processing_time);
      message m = new message();

      if((BattleEnemy.state) == true) {
        if(count==0)
        {
          m.add(makeContent("out1", new doubleEnt(powerLevel)));
          count++;
        }
        else if(count>0)
        {
          if (powerLevel > 10) {
            m.add(makeContent("out1", new doubleEnt(powerLevel)));
//            System.out.println("===== Tank ReturnPower : "+SimpArc.BattleEnemy.returnPowerLevel);
            powerLevel = powerLevel - 0.03 * BattleEnemy.returnPowerLevel;
          }
          else {
            if(BattleDivision.orderNumber == 1)
            {
              content con = makeContent("out2", new entity("Require Help"));
              m.add(con);
            }
            else if(BattleDivision.orderNumber == 2)
            {
              content con = makeContent("out2", new entity("Require Help"));
              m.add(con);
            }
            phase = "in danger";
            passivate();
          }


          count++;
        }
      }

      else
      {
        m.add(makeContent("out2", new entity("Enemy Distroyed")));
        phase = "win";
        passivate();
      }

      return m;
    }

}
