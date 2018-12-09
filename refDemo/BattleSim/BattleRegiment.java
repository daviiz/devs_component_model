package refDemo.BattleSim; import java.awt.Dimension;
import java.awt.Point;

import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;


public class BattleRegiment extends ViewableDigraph {

    protected double processing_time;
    static double powerLevel;

    public BattleRegiment() {
      this("Regiment", 1);
    }

    public BattleRegiment(String  name,double  Processing_time){
      super(name);

      BattleInfantryBattalion Infantry = new BattleInfantryBattalion("Infantry", 1);
      BattleArtilleryBattalion Artillery = new BattleArtilleryBattalion("Artillery", 1);
      BattleRegimentCommand Commander = new BattleRegimentCommand("Regiment Header", 1);

      add(Infantry);
      add(Artillery);
      add(Commander);

      addInport("in1");
      addOutport("out1");
      addOutport("out2");
      addOutport("out3");
      addOutport("out4");

      addCoupling(this, "in1", Commander, "in1");
      addCoupling(Commander, "out1", Infantry, "in1");
      addCoupling(Commander, "out2", Artillery, "in1");
      addCoupling(Commander, "out3", this, "out3");
      addCoupling(Commander, "out4", this, "out4");

      addCoupling(Infantry, "out1", this, "out1");
      addCoupling(Infantry, "out2", Commander, "in2");
//      addCoupling(Infantry, "out3", this, "out3");

      addCoupling(Artillery, "out1", this, "out2");
      addCoupling(Artillery, "out2", Commander, "in2");
//      addCoupling(Artillery, "out3", this, "out3");

      initialize();
      showState();

      preferredSize = new Dimension(500, 150);

      Commander.setPreferredLocation(new Point(30, 50));
      Artillery.setPreferredLocation(new Point(150, 15));
      Infantry.setPreferredLocation(new Point(150, 80));

      processing_time = Processing_time;
    }

    public void  deltint( )
   {
   }


    public void layoutForSimView()
    {
        preferredSize = new Dimension(475, 171);
        ((ViewableComponent)withName("Infantry")).setPreferredLocation(new Point(189, 24));
        ((ViewableComponent)withName("Regiment Header")).setPreferredLocation(new Point(3, 48));
        ((ViewableComponent)withName("Artillery")).setPreferredLocation(new Point(189, 94));
    }
}
