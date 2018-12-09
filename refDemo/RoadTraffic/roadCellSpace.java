package refDemo.RoadTraffic;

import java.awt.Dimension;

import GenCol.Pair;
import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.componentIterator;
import model.modeling.CAModels.Cell;
import model.modeling.CAModels.TwoDimCellSpace;
import model.plots.CellGridPlot;

public class roadCellSpace  extends TwoDimCellSpace {
  int xDim, yDim;

public roadCellSpace (){
        //
        this(40,1);
        //        this(6,40);
        //        this(,4);
      //      this(20,20);
}

public roadCellSpace(int xDim, int yDim){
        super("Road Cell Space", xDim, yDim );
        this.numCells = xDim*yDim;
        this.xDim=xDim;
        this.yDim=yDim;
/* * generate id for each cell in the cellSpace
         */
        for (int i = 0; i < xDimCellspace; i++){
          for (int j = 0; j < yDimCellspace; j++){
            roadCell fc = new roadCell(i, j);
           fc.visible = true;
          addCell(fc,xDim,yDim,xDimCellspace,yDimCellspace);
        }
           }
        hideAll();  //hides only components so far
        // Do the couplings
        addInport("lightcontrol");
        addTestInput("lightcontrol",new entity(""));
        addOutport("queue");
        addTestInput("start",new entity(""));
 if(xDim>1){
         roadCell cx = (roadCell) withId(0, yDim / 2);
         addCoupling(this, "start", cx, "roadstart");
         addCoupling(cx,"number",this, "queue");
 }

if (yDim>1){
         roadCell by = (roadCell) withId(xDim / 2, 0);
         addCoupling(this, "start", by, "roadstart");
         addCoupling(by,"number",this, "queue");
        }

        this.doNeighborToNeighborCoupling();
        coupleOneToAll(this,"stop","stop");

     lightControler contr =new lightControler("light",10);
     add(contr);
     contr.setHidden(true);

    //start the control light
      addCoupling(this,"lightcontrol",contr,"start");

if(yDim>1){
        roadCell up = (roadCell) withId(xDim / 2, yDim / 2 + 1);
        roadCell down = (roadCell) withId(xDim / 2, yDim / 2 - 1);
        addCoupling(contr, "columcontrol", up, "accident");
        addCoupling(contr, "columcontrol", down, "accident");
      }
if(xDim>1){
        roadCell left = (roadCell) withId(xDim / 2 - 1, yDim / 2);
        roadCell right = (roadCell) withId(xDim / 2 + 1, yDim / 2);
        addCoupling(contr, "rowcontrol", left, "accident");
        addCoupling(contr, "rowcontrol", right, "accident");
      }
         addInport("accident");
         addTestInput("accident",new doubleEnt(1.0/3.0),0);//0.3 of road blocked
         addTestInput("accident",new doubleEnt(2.0/3.0),0);//0.6 of road blocked
         addTestInput("accident",new doubleEnt(1.0),0);//totally blocked
         addTestInput("accident",new doubleEnt(0.0),0);//free flow

         roadCell a = (roadCell)withId(xDim/3,0);
         addCoupling(this, "accident", a, "accident");

        CellGridPlot t = new CellGridPlot("roadCellSpace", 1,
                                   "", 400, "", 400);
        t.setCellSize(10);
        t.setCellGridViewLocation(570,100);
        add(t);
        // t.setHidden(false);
        coupleAllTo("outDraw",t,"drawCellToScale");

}
      /**
       *  This method was modified to do neighbor to neighbor coupling for the
       *   cells
       */
     public void doNeighborToNeighborCoupling(){
        componentIterator it1 = components.cIterator();
        while(it1.hasNext()) {
          Cell d1 = (Cell)it1.nextComponent();
          Pair myid = (Pair)d1.getId();
          Integer my_xint = (Integer)myid.getKey();
          Integer my_yint = (Integer)myid.getValue();
          int my_x = my_xint.intValue();
          int my_y = my_yint.intValue();

          componentIterator it2 = components.cIterator();
          while(it2.hasNext()) {
            Cell d2 = (Cell)it2.nextComponent();
            Pair other_id = (Pair)d2.getId();
            if (!other_id.equals(myid)) {
              Integer other_xint = (Integer)other_id.getKey();
              Integer other_yint = (Integer)other_id.getValue();
              int other_x = other_xint.intValue();
              int other_y = other_yint.intValue();

              if (my_y==yDim/2){//row
                if (my_x == other_x - 1 && my_y == other_y) {
                  addCoupling(d1, "number", d2, "numBehind");
                  addCoupling(d2, "number", d1, "numAhead");
                  addCoupling(d2, "outaccident", d1, "aheadAcc");
                } //couple me to Ahead Neighbor
                if (my_x == other_x + 1 && my_y == other_y) {
                  addCoupling(d2, "number", d1, "numBehind");
                  addCoupling(d1, "number", d2, "numAhead");
                  addCoupling(d1, "outaccident", d2, "aheadAcc");
                //   addCoupling(d2,"sendId",d1,"leftId");
                } //  Neighbor Behind me
              }//End of if() row

              if (my_x==xDim/2){//col
                if (my_x == other_x && my_y == other_y - 1) {
                  addCoupling(d1, "number", d2, "numBehind");
                  addCoupling(d2, "number", d1, "numAhead");
                  addCoupling(d2, "outaccident", d1, "aheadAcc");
                } //my to up Neighbor
                if (my_x == other_x && my_y == other_y + 1) {
                  addCoupling(d2, "number", d1, "numBehind");
                  addCoupling(d1, "number", d2, "numAhead");
                  addCoupling(d1, "outaccident", d2, "aheadAcc");
                } //Neighbor is down me
              } //End of if () col
         } // end if(!other_id.equals(myid))
          }// End inner while loop
        }// End outer while loop
      }

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(269, 279);
    }
}
// End lightSpreadCellSpace

