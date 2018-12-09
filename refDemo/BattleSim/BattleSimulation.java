package refDemo.BattleSim; import java.awt.Dimension;
import java.awt.Point;

import model.plots.CellGridPlot;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;


public class BattleSimulation extends ViewableDigraph{

    public BattleSimulation() {
      super("Battle Simulation");
      BattleDivision Division = new BattleDivision("Division", 1);
      BattleRegiment Regiment1 = new BattleRegiment("Regiment 1", 1);
      BattleRegiment Regiment2 = new BattleRegiment("Regiment 2", 1);
      BattleReconnaissance Reconnaissance = new BattleReconnaissance("Reconnaissance", 1);
      BattleTankUnit Tank = new BattleTankUnit("Tank Unit", 1);
      BattleEnemy Enemy = new BattleEnemy("Enemy", 1);

      add(Division);
      add(Regiment1);
      add(Regiment2);
      add(Reconnaissance);
      add(Tank);
      add(Enemy);

      addOutport("out1");
      addOutport("out2");
      addOutport("out3");
      addOutport("out4");
      addOutport("out5");

      addCoupling(Division, "out1", Reconnaissance, "in");
      addCoupling(Division, "out2", Tank, "in");
      addCoupling(Division, "out5", this, "out4");
      addCoupling(Tank, "out1", Enemy, "in1");
      addCoupling(Tank, "out2", Division, "in3");
      addCoupling(Tank, "out1", Division, "in7");
      addCoupling(Reconnaissance, "out3", Division, "in2");
      addCoupling(Reconnaissance, "out2", Division, "in6");
      addCoupling(Reconnaissance, "out1", this, "out1");
      addCoupling(Enemy, "out", this, "out3");
      addCoupling(Division, "out3", Regiment1, "in1");
      addCoupling(Division, "out4", Regiment2, "in1");

      addCoupling(Regiment1, "out1", Enemy, "in2");
      addCoupling(Regiment1, "out2", Enemy, "in4");
      addCoupling(Regiment1, "out3", Division, "in4");
      addCoupling(Regiment1, "out4", Division, "in8");
      addCoupling(Regiment2, "out1", Enemy, "in3");
      addCoupling(Regiment2, "out2", Enemy, "in5");
      addCoupling(Regiment2, "out3", Division, "in5");
      addCoupling(Regiment2, "out4", Division, "in9");


      initialize();
      showState();

      CellGridPlot timeP = new CellGridPlot("Time Plot", 1, 1600);
      timeP.setCellGridViewLocation(100, 200);
      timeP.setSpaceSize(160, 80);
      timeP.setCellSize(5);
      timeP.setTimeScale(100);

      add(timeP);
      addCoupling(Enemy, "out", timeP, "timePlot");
      addCoupling(Division, "out5", timeP, "timePlot");


      preferredSize = new Dimension(1000, 590);
      Division.setPreferredLocation(new Point(30, 210));
      Reconnaissance.setPreferredLocation(new Point(300, 30));
      Tank.setPreferredLocation(new Point(300, 150));
      Regiment1.setPreferredLocation(new Point(300, 300));
      Regiment2.setPreferredLocation(new Point(300, 450));
      Enemy.setPreferredLocation(new Point(750, 210));
    }

    public void layoutForSimView()
    {
      preferredSize = new Dimension(1000, 590);
      ((ViewableComponent) withName("Division")).setPreferredLocation(new Point(30, 210));
      ((ViewableComponent) withName("Reconnaissance")).setPreferredLocation(new Point(300, 20));
      ((ViewableComponent) withName("Tank Unit")).setPreferredLocation(new Point(300, 100));
      ((ViewableComponent) withName("Regiment 1")).setPreferredLocation(new Point(250, 170));
      ((ViewableComponent) withName("Regiment 2")).setPreferredLocation(new Point(250, 360));
      ((ViewableComponent)withName("Enemy")).setPreferredLocation(new Point(750, 210));
    }

}
