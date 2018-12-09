package refDemo.BattleSim; import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class BattleReconnaissance extends ViewableAtomic {

    protected entity job;
    protected double processing_time;
    double getPowerLevel;
    static double powerLevel;
    int count;

    public BattleReconnaissance() {
      this("Reconnaissance", 1);
    }

    public BattleReconnaissance(String  name,double  Processing_time){
      super(name);
      addInport("in");
      addOutport("out1");
      addOutport("out2");
      addOutport("out3");
      processing_time = Processing_time;
    }

    public void initialize(){
      phase = "passive";
      sigma = INFINITY;
      job = new entity("job");
      powerLevel = 20;
      count=0;
      super.initialize();
    }

    public void  deltext(double e,message   x)
    {
      Continue(e);
      if (phaseIs("passive"))
      {
        for (int i = 0; i < x.getLength(); i++) {
          if (messageOnPort(x, "in", i)) {
            job = x.getValOnPort("in", i);
            holdIn("Wait Command", processing_time);
          }
        }
      }

    }

    public void  deltint( )
    {
    }

    public message    out( )
    {
      message m = new message();
      if (phaseIs("passive")) {
        holdIn("passive", processing_time);
 //       return m;
      }
      else {
        holdIn("searching", processing_time);
        if (count < 1) { //���� �߰��ϴ� ����
          m.add(makeContent("out1", new entity("searching")));
          count++;
//          return m;
        }
        else { //���� �߰��ϸ�
          getPowerLevel = BattleEnemy.powerLevel;
          holdIn("found enemy", processing_time);
          content con = makeContent("out3",
                                    new entity("Enemy has " + getPowerLevel +
                                               " power"));
          m.add(con);
          count++;
          phase = "passive";
          sigma = INFINITY;
//          return m;
        }
      }
//      m.add(makeContent("out2", new entity("Power Level : " + powerLevel)));
      return m;
    }


}
