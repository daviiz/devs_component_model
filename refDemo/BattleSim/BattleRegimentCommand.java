package refDemo.BattleSim; import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;

public class BattleRegimentCommand extends ViewableAtomic {

    protected entity job;
    protected double processing_time;
    double getPowerLevel;
    static double powerLevel;
    int count, numCommand, orderNumber;
    String  tempName;

    public BattleRegimentCommand() {
      this("Reg Header", 1);

    }

    public BattleRegimentCommand(String  name,double  Processing_time){
      super(name);
      tempName = name;
      addInport("in1");
      addInport("in2");
      addOutport("out1");
      addOutport("out2");
      addOutport("out3");
      addOutport("out4");

      processing_time = Processing_time;
    }

    public void initialize(){
      phase = "passive";
      sigma = INFINITY;
      job = new entity("job");
      count = 0;
      numCommand = 0;
      powerLevel = 0;
      orderNumber = 0;
      super.initialize();
    }

    public void  deltext(double e,message   x)
    {
      Continue(e);
      passivate();
      count = 0;
        for (int i = 0; i < x.getLength(); i++) {
          if(numCommand<2)
          {
            if (messageOnPort(x, "in1", i)) {
              job = x.getValOnPort("in1", i);
              orderNumber = 1;
              holdIn("Get Command", processing_time);
            }

            if (messageOnPort(x, "in2", i)) {
              job = x.getValOnPort("in2", i);
              orderNumber = 2;
              holdIn("Get Command", processing_time);
              count = 0;
              numCommand++;
            }
          }
        }
    }

    public void  deltint( )
   {
     powerLevel = BattleInfantryBattalion.powerLevel + BattleArtilleryBattalion.powerLevel;
   }


     public message    out( )
     {
       message m = new message();
       if(numCommand<2)
       {
         if (BattleDivision.orderNumber == 1) {
           if (count == 0) {
             if (orderNumber == 1) {
               holdIn("attack", processing_time);
               m.add(makeContent("out1", job));
               count++;
             }

             else if (orderNumber == 2) {
               holdIn("attack", processing_time);
               m.add(makeContent("out2", new entity("attack")));
               count++;
             }
           }
           else {
             m.add(makeContent("out4", new doubleEnt(powerLevel)));
             count++;
           }

         }

         else if(BattleDivision.orderNumber == 2) {
           if (count == 0) {
               holdIn("attack", processing_time);
               m.add(makeContent("out1", job));
               m.add(makeContent("out2", job));
               count++;
           }
           else {
             m.add(makeContent("out4", new doubleEnt(powerLevel)));
             count++;
           }
         }
       }

       else
       {

         if (BattleDivision.orderNumber == 1) {
           m.add(makeContent("out3", new entity("Require Help")));
         }
         else if(BattleDivision.orderNumber == 2) {

         }
         passivate();
       }

       return m;
     }

}