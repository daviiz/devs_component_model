package refDemo.BattleSim; import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class BattleDivision extends ViewableAtomic {

    protected entity job;
    protected double processing_time;
    double getValueFromGenerator,clock;
    static double powerLevel,reconnaissancePower,tankPower,reg1Power,reg2Power;
    static int orderNumber;
    String tempPowerLevel;
    boolean searchedEnemy;

    public BattleDivision() {
      this("Division", 1);
    }

    public BattleDivision(String  name,double  Processing_time){
      super(name);
      addInport("Command In");
      addInport("in2");
      addInport("in3");
      addInport("in4");
      addInport("in5");
      addInport("in6");    //reconnaissance power level in
      addInport("in7");    //tank unit power level in
      addInport("in8");    //regiment1 power level in
      addInport("in9");    //regiment2 power level in
      addOutport("out1");
      addOutport("out2");
      addOutport("out3");
      addOutport("out4");
      addOutport("out5");

      addTestInput("Command In",new entity("1. Search Enemy"));
      addTestInput("Command In",new entity("2. Fight One by One"));
      addTestInput("Command In",new entity("3. Fight All to Enemy"));

      processing_time = Processing_time;
    }

    public void initialize(){
      phase = "passive";
      sigma = INFINITY;
      job = new entity("job");
      searchedEnemy = false;
      orderNumber = 0;
      reconnaissancePower = 30;
      tankPower = 300;
      reg1Power = 200;
      reg2Power = 200;
      calculatePowerLevel();
      super.initialize();
    }

    public void  deltext(double e,message   x)
    {
      Continue(e);
      for (int i = 0; i < x.getLength(); i++) {
        if (phaseIs("Win")) {
        }
        else {
          if (messageOnPort(x, "Command In", i)) {
            job = x.getValOnPort("Command In", i);
            if (job.toString() == "1. Search Enemy") {
              searchedEnemy = true;
              holdIn("busy", processing_time);
            }
            else if (job.toString() == "2. Fight One by One" && searchedEnemy == true) {
              orderNumber = 1;
              holdIn("Order 1", processing_time);
            }
            else if (job.toString() == "3. Fight All to Enemy" && searchedEnemy == true) {
              orderNumber = 2;
              holdIn("Order 2", processing_time);
            }
          }

          if (messageOnPort(x, "in2", i)) {
            job = x.getValOnPort("in2", i);
            if (job.toString() == "Require Help") {
              holdIn("Loose", INFINITY);
            }
            else if (job.toString() == "Enemy Distroyed") {
              holdIn("Win", INFINITY);
            }
            else {
              holdIn("Require Order", INFINITY);
            }
          }

          if (messageOnPort(x, "in3", i)) {
            job = x.getValOnPort("in3", i);
            if (job.toString() == "Require Help") {
              holdIn("Loose", INFINITY);
            }
            else if (job.toString() == "Enemy Distroyed") {
              System.out.println("Get distroy from tank");
              holdIn("Win", INFINITY);
            }
          }

          if (messageOnPort(x, "in4", i)) {
            if (x.getValOnPort("in4", i).toString() == "Require Help") {
              holdIn("Support Reg1", processing_time);
            }
            else if (x.getValOnPort("in4", i).toString() == "Enemy Distroyed") {
              holdIn("Win", INFINITY);
            }
          }

          if (messageOnPort(x, "in5", i)) {
            if (x.getValOnPort("in5", i).toString() == "Require Help") {
              holdIn("Support Reg2", processing_time);
            }
            else if (x.getValOnPort("in5", i).toString() == "Enemy Distroyed") {
              holdIn("Win", INFINITY);
            }
          }

          if (messageOnPort(x, "in6", i)) {
            reconnaissancePower = Double.parseDouble(x.getValOnPort("in6", i).
                toString());
            holdIn("Get PowerLevel", processing_time);
          }

          if (messageOnPort(x, "in7", i)) {
            tankPower = Double.parseDouble(x.getValOnPort("in7", i).toString());
            holdIn("Get PowerLevel", processing_time);
          }

          if (messageOnPort(x, "in8", i)) {
            reg1Power = Double.parseDouble(x.getValOnPort("in8", i).toString());
            holdIn("Get PowerLevel", processing_time);
          }

          if (messageOnPort(x, "in9", i)) {
            reg2Power = Double.parseDouble(x.getValOnPort("in9", i).toString());
            holdIn("Get PowerLevel", processing_time);
          }
          calculatePowerLevel();
        }
      }
    }

    public void calculatePowerLevel()
    {
      powerLevel = reconnaissancePower+tankPower+reg1Power+reg2Power ;
    }
    public void  deltint( )
    {
    }

    public message    out( )
    {
      message m = new message();
      if (phaseIs("busy")) {
        m.add(makeContent("out1", new entity("Search Enemy")));
        passivate();
      }

      if (phaseIs("Order 1"))
      {
        holdIn("attack", processing_time);
        BattleRegiment reg1 = new BattleRegiment("Regiment 1", 1);
        m.add(makeContent("out3", new entity("Attack")));
        passivate();
      }


      if (phaseIs("Order 2"))
      {
        holdIn("attack", processing_time);
        BattleRegiment reg1 = new BattleRegiment("Regiment 1", 1);
        BattleRegiment reg2 = new BattleRegiment("Regiment 2", 1);
        BattleTankUnit tank = new BattleTankUnit("Tank Unit", 1);
        m.add(makeContent("out2", new entity("Attack")));
        m.add(makeContent("out3", new entity("Attack")));
        m.add(makeContent("out4", new entity("Attack")));
        passivate();
      }

      if (phaseIs("Support Reg1") && orderNumber == 1) {
        holdIn("attack", processing_time);
        BattleRegiment reg2 = new BattleRegiment("Regiment 2", 1);
        m.add(makeContent("out4", new entity("Attack")));
        passivate();
      }

      if (phaseIs("Support Reg2") && orderNumber == 1) {
        holdIn("attack", processing_time);
        BattleTankUnit tank = new BattleTankUnit("Tank Unit", 1);
        m.add(makeContent("out2", new entity("Attack")));
        passivate();
      }

      if (phaseIs("Get PowerLevel")) {
        m.add(makeContent("out5", new doubleEnt(powerLevel)));
        passivate();
      }
      return m;
    }

}
