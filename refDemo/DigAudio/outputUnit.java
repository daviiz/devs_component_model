package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class outputUnit extends realDevs
{
  protected static int L = 256;
  protected int N, M, counter, cntr;
  protected double[] hR, hI;
  protected double[] y;

  protected double[] YR, YI, yr, yi;

  public outputUnit()
  {
    this("Output Unit");
  }

  public outputUnit(String name)
  {
    super(name);

    addInport("M");
    addInport("load");
    addInport("aR");
    addInport("aI");

    addOutport("out");


  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    if (somethingOnPort(x, "M"))
    {
      M = getIntValueOnPort(x, "M");
      N = L + M - 1;
      YR = new double[N];
      YI = new double[N];
      y = new double[N];
    }


    if (somethingOnPort(x, "load"))
    {
      if (phaseIs("idle"))
      {
        N = 256;
        M = 81;

        L = N - M + 1;
        //N = L + M - 1;

        YR = new double[N];
        YI = new double[N];

        y = new double[N];
        counter = 0;
      }

      YR[counter] = getRealValueOnPort(x, "aR");
      YI[counter] = getRealValueOnPort(x, "aI");

      counter++;

      holdIn("display", 0);
      //holdIn("load", INFINITY);
      //System.out.println("counter=" + counter);
      if (counter >= N)
      {
        counter = 0;
        hR = new double[N];
        hI = new double[N];

        for(int i=0; i<N; i++)
        {
          hR[i] = YR[i];
          hI[i] = YI[i];
        }

        System.out.println("before ifft");
        DisplayArray(YR);
        DisplayArray(YI);


        for (int n=0; n<N; n++)
        {
          y[n] = 0.0;
          //hI[n] = 0;
          for (int k=0; k<N; k++)
          {
            double phi = 2*Math.PI*k*n/N;
            y[n] += hR[k]*Math.cos(phi) - hI[k]*Math.sin(phi);
            //hI[n] += YI[k]*Math.cos(phi) + YR[k]*Math.sin(phi);
          }
          y[n]/=N;
          //hI[n]/=N;
        }

        //System.out.println("N=" + N);
        System.out.println("-----yR-----");
        DisplayArray(y);
        /*System.out.println("-----yI-----");
        DisplayArray(hI);
        counter = 0;*/
        /*for (int i=0; i<N; i++)
        {
          double m = Math.sqrt(Math.pow(hR[i], 2) + Math.pow(hI[i], 2));
          System.out.println(i + " = " + m);
        }*/

      }


    }
  }

  public void deltint()
  {
    holdIn("load", INFINITY);
  }

  public message out()
  {
    content con_0 = new content("", new entity(""));
    message op = new message();

    if (phaseIs("display"))
    {
      con_0 = new content("out", new doubleEnt(y[counter]));
    }

    op.add(con_0);
    return op;
  }
  public void calculateIFFT()
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
    for(k=0; k<hR.length; k++)
    {
      hR[k]/=N;
      hI[k]/=N;
    }
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

  protected void DisplayArray(double[] data)
  {
     for(int i=0; i<data.length; i++)
       System.out.println("val " + i + " = " + data[i]);
  }

}