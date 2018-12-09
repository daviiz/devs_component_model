package refDemo.BattleSim; import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class BattleEnemy extends ViewableAtomic {

    protected entity job;
    protected double processing_time;
    double getPowerLevel,getPowerLevel1,getPowerLevel2,getPowerLevel3,getPowerLevel4,getPowerLevel5;
    static double powerLevel,returnPowerLevel;
    static boolean state;  //T:powerLever is over 0, F:powereLevel is below 0, destroyed.
    boolean isExtInput;

    public BattleEnemy() {
      this("Enemy", 1);
    }

    public BattleEnemy(String  name,double  Processing_time){
      super(name);
      addInport("in1");
      addInport("in2");
      addInport("in3");
      addInport("in4");
      addInport("in5");
      addInport("in6");
      addOutport("out");

      addTestInput("in6",new entity("100"));
      addTestInput("in6",new entity("300"));
      addTestInput("in6",new entity("500"));
      addTestInput("in6",new entity("600"));
      addTestInput("in6",new entity("700"));
      addTestInput("in6",new entity("800"));

      processing_time = Processing_time;
    }

    public void initialize(){
      phase = "passive";
      sigma = INFINITY;
      powerLevel = 500;
      returnPowerLevel = powerLevel;
      isExtInput = false;
      getPowerLevel = getPowerLevel1 = getPowerLevel2 = getPowerLevel3 = getPowerLevel4 = getPowerLevel5 = 0;

      if(powerLevel>0)
        state = true;
      else
        state = false;
      job = new entity("job");
      super.initialize();
    }

    public void  deltext(double e,message   x)
    {
      Continue(e);
      isExtInput = true;

      if (phaseIs("passive")) {
        for (int i = 0; i < x.getLength(); i++) {
          System.out.println("---------  Input number : "+x.getLength()+"  ---------");
          if (messageOnPort(x, "in1", i)) {
            getPowerLevel1 = Double.parseDouble(x.getValOnPort("in1", i).toString());
            holdIn("ready", processing_time);
          }

          if (messageOnPort(x, "in2", i)) {
            getPowerLevel2 = Double.parseDouble(x.getValOnPort("in2", i).toString());
            holdIn("ready", processing_time);
          }

          if (messageOnPort(x, "in3", i)) {
            getPowerLevel3 = Double.parseDouble(x.getValOnPort("in3", i).toString());
            holdIn("ready", processing_time);
          }

          if (messageOnPort(x, "in4", i)) {
            getPowerLevel4 = Double.parseDouble(x.getValOnPort("in4", i).toString());
            holdIn("ready", processing_time);
          }

          if (messageOnPort(x, "in5", i)) {
            getPowerLevel5 = Double.parseDouble(x.getValOnPort("in5", i).toString());
            holdIn("ready", processing_time);
          }


          if (messageOnPort(x, "in6", i)) {
            powerLevel = Double.parseDouble(x.getValOnPort("in6", i).toString());
          }
        }

        getPowerLevel = getPowerLevel1 + getPowerLevel2 + getPowerLevel3 + getPowerLevel4 + getPowerLevel5;
        powerLevel = powerLevel - 0.03*getPowerLevel;
        returnPowerLevel = powerLevel/x.getLength();

        System.out.println("Get power Level : "+getPowerLevel);
        System.out.println("Return Power Level : "+returnPowerLevel);
        System.out.println("Power Level : "+powerLevel);
      }
    }

    public void  deltint( )
    {
       passivate();
    }

    public message    out( )
    {
      message m = new message();
      holdIn("attack", processing_time);

        if (powerLevel > 20) { //���� �߰��ϴ� ����
          m.add(makeContent("out", new doubleEnt(powerLevel)));
        }
        else if (powerLevel > 0 && powerLevel <= 20) {
          m.add(makeContent("out", new doubleEnt(powerLevel)));
        }
        else if (powerLevel <= 0) {
          powerLevel = 0;
          m.add(makeContent("out", new doubleEnt(powerLevel)));
          state = false;
          phase = "destroyed";
        }
      return m;
    }

}
