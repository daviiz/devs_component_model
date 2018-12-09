package refDemo.BattleSim; import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class BattleArtilleryBattalion extends ViewableAtomic {

    protected entity job;
    protected double processing_time;
    double getPowerLevel;
    static double powerLevel;
    int count;
    String tempName;

    public BattleArtilleryBattalion() {
      this("Battalion", 1);
    }

    public BattleArtilleryBattalion(String  name,double  Processing_time){
      super(name);
      tempName = name;
      addInport("in1");
      addInport("in2");
      addInport("in3");
      addOutport("out1");
      addOutport("out2");

      processing_time = Processing_time;
    }

    public void initialize(){
      phase = "passive";
      sigma = INFINITY;
      job = new entity("job");
      count = 0;
      powerLevel = 100;
      super.initialize();
    }

    public void  deltext(double e,message   x)
    {
      Continue(e);
      passivate();
      count = 0;
      if (phaseIs("passive")) {
        for (int i = 0; i < x.getLength(); i++) {
          if (messageOnPort(x, "in1", i)) {
            job = x.getValOnPort("in1", i);
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
//            System.out.println("===== Artillery ReturnPower : "+BattleEnemy.returnPowerLevel);
            if(BattleDivision.orderNumber == 1)
            {
              powerLevel = powerLevel - 0.03 * BattleEnemy.returnPowerLevel;
            }
            else if(BattleDivision.orderNumber == 2)
            {
              powerLevel = powerLevel - 0.03 * (BattleEnemy.returnPowerLevel/2);
            }
          }
          else {
            content con = makeContent("out2", new entity("Require Help"));
            m.add(con);

            passivate();
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
