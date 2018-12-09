package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class fft extends realDevs
{
  protected int counter, N;
  //protected final int  N = 16;
  protected double[] hR;
  protected double[] hI;

  public fft()
  {
    this("FFT");
  }

  public fft(String name)
  {
    super(name);

    addInport("start");
    addInport("load");
    addInport("N");
    addInport("i_dataR");
    addInport("i_dataI");

    addOutport("done");
    addOutport("dataR");
    addOutport("dataI");
    addOutport("row");
    addOutport("wr");
    addOutport("oN");

    addTestInput("start", new entity("start"));

    /*hR = new double[N];
    hI = new double[N];

    hR[0] = 1.0;
    hR[1] = 2.0;
    hR[2] = 3.0;
    hR[3] = 4.0;
    hR[4] = 0.0;
    hR[5] = 0.0;
    hR[6] = 0.0;
    hR[7] = 0.0;
    hR[8] = 0.0;
    hR[9] = 0.0;
    hR[10] = 0.0;
    hR[11] = 0.0;
    hR[12] = 0.0;
    hR[13] = 0.0;
    hR[14] = 0.0;
    hR[15] = 0.0;

    hI[0] = 0.0;
    hI[1] = 0.0;
    hI[2] = 0.0;
    hI[3] = 0.0;
    hI[4] = 0.0;
    hI[5] = 0.0;
    hI[6] = 0.0;
    hI[7] = 0.0;
    hI[8] = 0.0;
    hI[9] = 0.0;
    hI[10] = 0.0;
    hI[11] = 0.0;
    hI[12] = 0.0;
    hI[13] = 0.0;
    hI[14] = 0.0;
    hI[15] = 0.0;*/
    counter = 0;
  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    Continue(e);

    /*if (phaseIs("idle"))
    {
      if (somethingOnPort(x, "start"))
      {
        counter = 0;
        holdIn("load", INFINITY);
      }
    }
    else if (phaseIs("load"))
    {*/
      if (somethingOnPort(x, "load"))
      {
        if (counter == 0)
        {
          N = getIntValueOnPort(x, "N");
          hR = new double[N];
          hI = new double[N];
        }

        hR[counter] = getRealValueOnPort(x, "i_dataR");
        hI[counter] = getRealValueOnPort(x, "i_dataI");

        counter++;
        //System.out.println(counter + " : " + N);
        if (counter < N)
          holdIn("load", INFINITY);
        else
          passivateIn("idle");
      }
      else if (somethingOnPort(x, "start"))
      {
        holdIn("calculate", 0);
      }
    //}
  }

  public void calculateFFT()
  {
    //System.out.println(Math.log(N));
    //System.out.println(stage);
    int j, k, l, noMuls, iSpacing, index;
    int noStages = (int)(Math.log(N)/Math.log(2));

    double[] bfly = new double[4];

    noMuls = N;

    for(k=0; k<noStages; k++)
    {
      iSpacing = (int)(N/(Math.pow(2, k+1)));

      noMuls /= 2;

      for(l=0; l<Math.pow(2, k); l++)
      {
        for(j=0; j<noMuls; j++)
        {
          index = 2*iSpacing*l + j;

          //System.out.println(k + " : " + j + " : " + index);
          //System.out.println(k + " : " + j + " : " + index + " : " + (index + iSpacing) + " : " + j*((int)(Math.pow(2, k))));
          bfly = Butterfly(hR[index], hI[index], hR[index + iSpacing], hI[index + iSpacing], N, j*((int)(Math.pow(2, k))));
          hR[index] = bfly[0];
          hI[index] = bfly[1];
          hR[index + iSpacing] = bfly[2];
          hI[index + iSpacing] = bfly[3];
        }
      }
    }


    /*if (stage == (Math.log(N)/Math.log(2))-1)
    {
      int k, i;
      double g, theta;

      for (k = 0; k < thisN / 2; k++) {
        //if ((k % 2) == 0)
        {
          HR[2 * k] = 0.0;
          HI[2 * k] = 0.0;

          for (i = 0; i < thisN / 2; i++) {
            g = h[i] + h[i + thisN / 2];
            theta = 4 * Math.PI * k * i / thisN;

            HR[2 * k] += g * Math.cos(theta);
            HI[2 * k] += -1 * g * Math.sin(theta);
          }
        }
        //else
        {
          HR[2 * k + 1] = 0.0;
          HI[2 * k + 1] = 0.0;

          for (i = 0; i < thisN / 2; i++) {
            g = h[i] - h[i + thisN / 2];

            theta = (2 * Math.PI * i / thisN) + (4 * Math.PI * k * i / thisN);

            HR[2 * k + 1] += g * Math.cos(theta);
            HI[2 * k + 1] += -1 * g * Math.sin(theta);
          }

        }
      }

    }
    else
    {

      //calculate(N, thisN/2, stage+1);
    }*/
  }

  public double[] Butterfly(double aR, double aI, double bR, double bI, int N, int k)
  {
    double O[] = new double[4];
    double phi, a, b;

    phi = 2*Math.PI*k/N;

    O[0] = aR + bR;
    O[1] = aI + bI;

    a = aR - bR;
    b = aI - bI;

    O[2] = a*Math.cos(phi) + b*Math.sin(phi);
    O[3] = b*Math.cos(phi) - a*Math.sin(phi);

    return O;
  }

  public void deltint()
  {
    if (phaseIs("calculate"))
    {
      //System.out.println("----b4 fft filter ------");
      //System.out.println("---------------Real--------------");
      //DisplayArray(hR);
      //System.out.println("------------Imaginary------------");
      //DisplayArray(hI);
      //System.out.println("---------------------------------");
      //passivateIn("idle");

      //calculateFFT();
      double[] tempR, tempI;
        tempR = new double[hR.length];
        tempI = new double[hI.length];

        for (int j=0; j<hR.length; j++)
        {
          tempR[j] = hR[j];
          tempI[j] = hI[j];
        }

        for (int k=0; k<N; k++)
        {
          hR[k] = 0;
          for (int n=0; n<N; n++)
          {
            double phi = 2*Math.PI*k*n/N;
            hR[k] += tempR[n]*Math.cos(phi) + tempI[n]*Math.sin(phi);
            hI[k] += tempI[n]*Math.cos(phi) - tempR[n]*Math.sin(phi);
          }
          //hR[i]/=N;
          //hI[i]/=N;
        }


      /*System.out.println("----after fft filter ------");
      System.out.println("---------------Real--------------");
      DisplayArray(hR);
      System.out.println("------------Imaginary------------");
      DisplayArray(hI);
      System.out.println("---------------------------------");*/


      counter = 0;
      holdIn("load_filter", 1);
    }
    else if(phaseIs("load_filter"))
    {
      counter++;
      if (counter < N)
        holdIn("load_filter", 1);
      else
        holdIn("done", 0);
    }
    else if(phaseIs("done"))
    {
      /*System.out.println("----after fft filter ------");
      System.out.println("---------------Real--------------");
      DisplayArray(hR);
      System.out.println("------------Imaginary------------");
      DisplayArray(hI);
      System.out.println("---------------------------------");*/
      passivateIn("idle");
    }
  }

  protected void DisplayArray(double[] data)
  {
    for(int i=0; i<data.length; i++)
      System.out.println("val " + i + " = " + data[i]);
  }

  public message out()
  {
    content con_0 = new content("", new entity(""));
    content con_1 = new content("", new entity(""));
    content con_2 = new content("", new entity(""));
    content con_3 = new content("", new entity(""));

    message op = new message();
    if (phaseIs("load_filter"))
    {
      if (counter == 0)
      {
        content con_4 = new content("oN", new intEnt(N));
        op.add(con_4);
      }
      con_0=makeContent("dataR", new doubleEnt(hR[counter]));
      con_1=makeContent("dataI", new doubleEnt(hI[counter]));
      con_2=makeContent("wr", new entity("write"));
      con_3=makeContent("row", new intEnt(counter));
    }
    else if (phaseIs("done"))
    {
      con_0=makeContent("done", new entity("done"));
    }


    op.add(con_0);
    op.add(con_1);
    op.add(con_2);
    op.add(con_3);

    return op;
  }

}