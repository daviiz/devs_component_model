package refDemo.RadarProject;

import java.awt.Color;
import java.awt.Dimension;

import model.modeling.message;
import view.modeling.ViewableAtomic;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RadarCellGridPlot extends ViewableAtomic {
  protected double clock;
  protected CellGridView cellGridView;
  protected boolean m_bIsTq;
 // protected Point cellGridViewLocation = new Point(500, 500);
 public RadarCellGridPlot(String name, boolean btype)
 {


super(name);

m_bIsTq = btype;
   cellGridView = new CellGridView(name, "X 50", "Y 50");

  // cellGridView.setXScale(100);
//   cellGridView.setYScale(100);
// x and y
//
cellGridView.setSpaceSize(new Dimension(100, 100));
// radius of diameter
cellGridView.setCellSize(5);
cellGridView.setVisible(true);
   clock = 0;
   setHidden(true);

 }

  public RadarCellGridPlot(String name, boolean btype,String xname, String yname,
                           int xdimen, int ydimen)
  {


super(name);

    m_bIsTq = btype;
    cellGridView = new CellGridView(name,xname + " " + (xdimen/2),yname + " " + (ydimen/2));

   // cellGridView.setXScale(100);
 //   cellGridView.setYScale(100);
 // x and y
 //
 cellGridView.setSpaceSize(new Dimension(xdimen, ydimen));
// radius of diameter
cellGridView.setCellSize(5);
cellGridView.setVisible(true);
    clock = 0;
    setHidden(true);

  }
  public void initialize()
  {
    addInport("TrackEntity");
    clock = 0;
    passivate();
    super.initialize();
   // cellGridView = new

   }


   public void PlotRadar(double x, double y, Color c)
   {
     cellGridView.drawCellToScale(x, y, c);
   }


  public void  deltext(double e,message x)
{
   passivateIn("passive");
clock = clock + 1;
   double time = clock;
   for (int i = 0; i < x.getLength(); i++) {
       // if this content is on the draw-string port
       if (messageOnPort(x, "RadarIn", i))
       {
         // add this content to the jobs that have arrived
         radarentity entity = (radarentity)
          x.getValOnPort("RadarIn", i);

        //  System.out.println("/////////////time " + clock);

         // System.out.println("///////////TQ " + entity.getTQ());

          if(m_bIsTq)
          {
            cellGridView.drawCellToScale(clock, entity.getTQ(), entity.getColor());
          }
          else {
            cellGridView.drawCellToScale(entity.getX(), entity.getY(),
                                         entity.getColor());

          }
       }

    }



      }


}