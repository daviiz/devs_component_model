package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;


public class inputUnit extends realDevs
{
  protected static int L = 256;
  protected int counter, M, N, outDataCounter;
  protected boolean firstSeq;

  protected double[] data;
  protected double hR[], hI[];

  public inputUnit()
  {
    this("Input Unit");
  }
  public inputUnit(String name)
  {
    super(name);

    addInport("M");
    addInport("data");

    addOutport("oxR");
    addOutport("oxI");
    addOutport("rd");
    addOutport("row");

    data = new double[L];
    //pdata = new double[L];

    counter = 0;

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
      //M = getIntValueOnPort(x, "M");
      //N = L + M - 1;
      N = 256;
      M = 81;
      L = N - M + 1;

      hR = new double[N];
      hI = new double[N];
    }
    if (somethingOnPort(x, "data"))
    {
      counter++;

      if (counter == N-1)
      {
        holdIn("read_filt", 0);
      }

      if (counter < N)
      {
        if (phaseIs("idle"))
        {
          firstSeq = true;
          counter = 0;
          holdIn("record", INFINITY);
        }
        data[counter] = getRealValueOnPort(x, "data");
      }
      else
      {
        firstSeq = false;
        outDataCounter = 0;
        counter = 0;

        int i;

        //for (i=0; i<M; i++)
        //{
        //  hR[i] = 0.0;
        //  hI[i] = 0.0;
        //}

        //copy the last block's M-1 data points to the new block
        //for (i=N-M; i<N; i++)
        //{
        // hR[i-N+M] = pdata[i-L+M];
        //  hI[i-N+M] = 0.0;
        //}

        /*for (i=M; i<L; i++)
        {
          hR[i] = data[i-M];
          hI[i] = 0.0;
        }*/

        //DisplayArray(hI);
        //calculateFFT();

        /*for (i=0; i<M; i++)
        {
          hR[i] = 0.0;
          hI[i] = 0.0;
        }
        for (i=0; i<N-M; i++)
        {
          hR[i+M] = data[i];
          hI[i] = 0.0;
        }*/

        /*for (i=N-M+1; i<N; i++)
        {
          data[i-N+M-1] = data[i];
          data[i] = 0;
        }*/

        for (i=0; i<N; i++)
        {
          hR[i] = data[i];
          hI[i] = 0.0;
        }


        System.out.println("------input b4 fft-------");
        DisplayArray(hR);


        double[] tempR, tempI;
        tempR = new double[hR.length];
        tempI = new double[hI.length];

        for (int j=0; j<hR.length; j++)
        {
          tempR[j] = hR[j];
          tempI[j] = hI[j];
        }

        //System.out.println("---temp----");
        //DisplayArray(tempR);
        //DisplayArray(tempI);

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

        System.out.println("------input after fft-------");
        DisplayArray(hR);
        DisplayArray(hI);
      }
    }
    if (!firstSeq)
    {
      holdIn("outdata", 0);
    }
  }

  public void deltint()
  {
    if (phaseIs("outdata"))
    {
      outDataCounter++;
      holdIn("record", INFINITY);
    }
    else if (phaseIs("read_filt"))
    {
      holdIn("record", INFINITY);
    }

  }

  public message out()
  {
    content con_0 = new content("", new entity(""));
    content con_1 = new content("", new entity(""));
    content con_2 = new content("", new entity(""));
    content con_3 = new content("", new entity(""));

    message op = new message();

    if (phaseIs("read_filt"))
    {
      con_2 = new content("rd", new entity("read"));
      con_3 = new content("row", new intEnt(0));
    }



    if (phaseIs("outdata"))
    {
      con_0 = new content("oxR", new doubleEnt(hR[outDataCounter]));
      con_1 = new content("oxI", new doubleEnt(hI[outDataCounter]));

      con_2 = new content("rd", new entity("read"));
      con_3 = new content("row", new intEnt(outDataCounter+1));
    }

    op.add(con_0);
    op.add(con_1);
    op.add(con_2);
    op.add(con_3);

    return op;
  }


  public void calculateFFT()
  {
    int j, k, l, noMuls, iSpacing, index;
    int noStages = (int)(Math.log(N)/Math.log(2));

    System.out.println("N = " + N);
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

         bfly = Butterfly(hR[index], hI[index], hR[index + iSpacing], hI[index + iSpacing], N, j*((int)(Math.pow(2, k))));
          hR[index] = bfly[0];
          hI[index] = bfly[1];
          hR[index + iSpacing] = bfly[2];
          hI[index + iSpacing] = bfly[3];
        }
      }
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