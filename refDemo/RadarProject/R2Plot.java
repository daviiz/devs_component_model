package refDemo.RadarProject;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class R2Plot extends RadarCellGridPlot {

  public R2Plot() {

   super("R2Plot",true,"Updates"," TQ",100,30);

  }

/*
  public R2Plot(String name, double delay, double yRange) {

    super(name, delay, "Y", yRange);

  }

  protected void deltextHook1(message message)
  {
      // for each content in the given message
      doubleEnt dueTime = new doubleEnt(clock + jobDueDelay);
      double time = clock;
      for (int i = 0; i < message.getLength(); i++) {
          // if this content is on the draw-string port
          if (messageOnPort(message, "RadarIn", i))
          {
            // add this content to the jobs that have arrived
            radarentity entity = (radarentity)
             message.getValOnPort("RadarIn", i);
         System.out.println("/////////////time " + time);
         System.out.println("///////////TQ " + entity.getTQ());

            cellGridView.drawCellToScale(time, entity.getTQ(), entity.getColor());

          }


       }
  }
*/
}